package ru.practicum.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
@EnableElasticsearchRepositories(basePackages = "ru.practicum.repository")
public class ElasticsearchConfig {
    @Value("${spring.elasticsearch.host}")
    private String elasticsearchHost;
    @Value("${spring.elasticsearch.port}")
    private Integer elasticsearchPort;

    @Bean
    public RestClient elasticsearchRestClient() {
        return RestClient.builder(new HttpHost(elasticsearchHost, elasticsearchPort)).build();
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport() {
        return new RestClientTransport(elasticsearchRestClient(), new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        return new ElasticsearchClient(elasticsearchTransport());
    }
}