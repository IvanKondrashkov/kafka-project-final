package ru.practicum.service;

import java.util.*;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.Trigger;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import ru.practicum.Topics;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.Dataset;
import ru.practicum.schema.Schemas;
import java.util.concurrent.TimeoutException;
import static org.apache.spark.sql.functions.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SparkProcessorServiceImpl {
    private final SparkSession sparkSession;
    @Value("${spring.hadoop.hdfs.uri}")
    private String hdfsUri;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Value("${spring.spark.kafka.truststore.location}")
    private String kafkaSslTrustStoreLocation;
    @Value("${spring.kafka.ssl.trust-store-password}")
    private String kafkaSslTrustStorePassword;
    @Value("${spring.spark.kafka.keystore.location}")
    private String kafkaSslKeyStoreLocation;
    @Value("${spring.kafka.ssl.key-store-password}")
    private String kafkaSslKeyStorePassword;
    @Value("${spring.kafka.admin.properties.security.protocol}")
    private String kafkaSecurityProtocol;
    @Value("${spring.kafka.admin.properties.sasl.mechanism}")
    private String kafkaSaslMechanism;
    @Value("${spring.kafka.admin.properties.sasl.jaas.config}")
    private String kafkaSaslJaasConfig;
    @Value("${spring.spark.kafka.startingOffsets}")
    private String sparkKafkaStartingOffsets;

    @PostConstruct
    private void readOrders() {
        Dataset<Row> kafkaDf = sparkSession.readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", bootstrapAddress)
                .option("subscribe", Topics.FILTERED_ORDERS_TOPIC)
                .option("kafka.security.protocol", kafkaSecurityProtocol)
                .option("kafka.ssl.truststore.location", kafkaSslTrustStoreLocation)
                .option("kafka.ssl.truststore.password", kafkaSslTrustStorePassword)
                .option("kafka.ssl.keystore.location", kafkaSslKeyStoreLocation)
                .option("kafka.ssl.keystore.password", kafkaSslKeyStorePassword)
                .option("kafka.sasl.mechanism", kafkaSaslMechanism)
                .option("kafka.sasl.jaas.config", kafkaSaslJaasConfig)
                .option("startingOffsets", sparkKafkaStartingOffsets)
                .load();

        Dataset<Row> orderDf = kafkaDf
                .selectExpr("CAST(value AS STRING) as json")
                .select(from_json(col("json"), Schemas.getOrdersSchema()).alias("data"))
                .select("data.*");
        saveToHdfs(orderDf, "/data/orders");
    }

    @PostConstruct
    private void readEvents() {
        Dataset<Row> kafkaDf = sparkSession.readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", bootstrapAddress)
                .option("subscribe", Topics.USER_ANALYTICS_TOPIC)
                .option("kafka.security.protocol", kafkaSecurityProtocol)
                .option("kafka.ssl.truststore.location", kafkaSslTrustStoreLocation)
                .option("kafka.ssl.truststore.password", kafkaSslTrustStorePassword)
                .option("kafka.ssl.keystore.location", kafkaSslKeyStoreLocation)
                .option("kafka.ssl.keystore.password", kafkaSslKeyStorePassword)
                .option("kafka.sasl.mechanism", kafkaSaslMechanism)
                .option("kafka.sasl.jaas.config", kafkaSaslJaasConfig)
                .option("startingOffsets", sparkKafkaStartingOffsets)
                .load();

        Dataset<Row> eventDf = kafkaDf
                .selectExpr("CAST(value AS STRING) as json")
                .select(from_json(col("json"), Schemas.getAnalyticsSchema()).alias("data"))
                .select("data.*");
        saveToHdfs(eventDf, "/data/analytics");
    }

    private void sendMessageKafka(Dataset<Row> recommendations) {
        try {
            recommendations
                    .write()
                    .format("kafka")
                    .option("kafka.bootstrap.servers", bootstrapAddress)
                    .option("topic", Topics.RECOMMENDATIONS_TOPIC)
                    .option("kafka.security.protocol", kafkaSecurityProtocol)
                    .option("kafka.ssl.truststore.location", kafkaSslTrustStoreLocation)
                    .option("kafka.ssl.truststore.password", kafkaSslTrustStorePassword)
                    .option("kafka.ssl.keystore.location", kafkaSslKeyStoreLocation)
                    .option("kafka.ssl.keystore.password", kafkaSslKeyStorePassword)
                    .option("kafka.sasl.mechanism", kafkaSaslMechanism)
                    .option("kafka.sasl.jaas.config", kafkaSaslJaasConfig)
                    .option("startingOffsets", sparkKafkaStartingOffsets)
                    .save();
        } catch (Exception e) {
            log.error("Send to kafka error", e);
        }
    }

    private void saveToHdfs(Dataset<Row> df, String path) {
        try {
            df.writeStream()
                    .format("parquet")
                    .option("path", hdfsUri + path)
                    .option("checkpointLocation", hdfsUri + "/checkpoints" + path)
                    .trigger(Trigger.ProcessingTime("1 minute"))
                    .start();
        } catch (TimeoutException e) {
            log.error("Save to hdfs error", e);
        }
    }

    @Scheduled(cron = "${spring.analytics.processing.interval.cron:0 */5 * * * *}")
    private void processRecommendations() {
        Dataset<Row> orders = sparkSession.read()
                .schema(Schemas.getOrdersSchema())
                .parquet(hdfsUri + "/data/orders").alias("o");
        Dataset<Row> analytics = sparkSession.read()
                .schema(Schemas.getAnalyticsSchema())
                .parquet(hdfsUri + "/data/analytics").alias("a");

        Dataset<Row> recommendations = processRecommendationLogic(orders, analytics);
        log.info("Recommendations count: {}", recommendations.count());
        if (recommendations.count() > 0) {
            saveRecommendations(recommendations);
        }
    }

    private Dataset<Row> processRecommendationLogic(Dataset<Row> orders, Dataset<Row> analytics) {
        Dataset<Row> searchEvents = analytics.filter(col("event_type").equalTo("SEARCH"));
        Dataset<Row> productPopularity = searchEvents
                .groupBy(col("search_name"))
                .agg(
                        count("*").alias("view_count"),
                        countDistinct(col("user_id")).alias("unique_users"))
                .withColumn(
                        "popularity_score",
                        col("view_count").multiply(0.7).plus(col("unique_users").multiply(0.3))
                ).alias("p");

        return searchEvents
                .groupBy(col("a.user_id"), col("a.search_name"))
                .agg(count("*").alias("search_count"))
                .join(orders, col("a.search_name").equalTo(col("o.name")))
                .join(productPopularity, col("a.search_name").equalTo(col("p.search_name")))
                .groupBy(col("a.user_id"))
                .agg(
                        collect_set(
                                struct(
                                        col("o.product_id"),
                                        col("o.name"),
                                        col("o.category"),
                                        col("search_count").multiply(0.5).plus(col("p.popularity_score").multiply(0.5)).alias("relevance_score")
                                )
                        ).alias("products"),
                        max("p.popularity_score").alias("max_score"),
                        count("*").alias("product_count")
                )
                .withColumn(
                        "recommendation_type",
                        when(col("max_score").gt(1000), lit("TRENDING"))
                                .when(col("product_count").lt(5), lit("PERSONALIZED_SEARCH"))
                                .otherwise(lit("PERSONALIZED"))
                );
    }

    private void saveRecommendations(Dataset<Row> recommendations) {
        Dataset<Row> prepared = recommendations.select(
                col("user_id"),
                col("products"),
                current_timestamp().alias("timestamp"),
                col("recommendation_type"),
                col("max_score")
        );

        Dataset<Row> payload = prepared.select(
                lit(UUID.randomUUID().toString()).alias("key"),
                to_json(struct(col("*"))).alias("value")
        );
        sendMessageKafka(payload);
    }
}