package ru.practicum.config;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.apache.spark.serializer.KryoSerializer;

@Configuration
public class SparkConfig {
    @Value("${spring.elasticsearch.host}")
    private String elasticsearchHost;
    @Value("${spring.elasticsearch.port}")
    private String elasticsearchPort;
    @Value("${spring.spark.app.name}")
    private String sparkAppName;
    @Value("${spring.spark.master.uri}")
    private String sparkMasterUri;
    @Value("${spring.spark.ui.enabled}")
    private String sparkUiEnabled;
    @Value("${spring.spark.driver.allowMultipleContexts}")
    private String sparkDriverAllowMultipleContexts;
    @Value("${spring.spark.driver.extraClassPath}")
    private String sparkDriverExtraClassPath;
    @Value("${spring.spark.executor.extraClassPath}")
    private String sparkExecutorExtraClassPath;
    @Value("${spring.spark.kryo.registrationRequired}")
    private String sparkKryoRegistrationRequired;


    @Bean
    public SparkConf sparkConf() {
        return new SparkConf()
                .setAppName(sparkAppName)
                .setMaster(sparkMasterUri)
                .set("spark.ui.enabled", sparkUiEnabled)
                .set("spark.driver.allowMultipleContexts", sparkDriverAllowMultipleContexts)
                .set("spark.driver.extraClassPath", sparkDriverExtraClassPath)
                .set("spark.executor.extraClassPath", sparkExecutorExtraClassPath)
                .set("spark.kryo.registrationRequired", sparkKryoRegistrationRequired)
                .set("spark.serializer", KryoSerializer.class.getName())
                .set("spark.es.nodes", elasticsearchHost)
                .set("spark.es.port", elasticsearchPort);
    }

    @Bean(destroyMethod = "close")
    public SparkSession sparkSession() {
        return SparkSession.builder()
                .config(sparkConf())
                .getOrCreate();
    }
}