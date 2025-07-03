package ru.practicum.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.dto.es.RecommendationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@Repository
public interface RecommendationRepository extends ElasticsearchRepository<RecommendationDocument, String> {
    RecommendationDocument findByUserId(String userId);
}