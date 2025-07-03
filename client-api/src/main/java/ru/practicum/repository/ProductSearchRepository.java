package ru.practicum.repository;

import java.util.List;
import ru.practicum.dto.es.ProductDocument;
import org.springframework.stereotype.Repository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {
    List<ProductDocument> findAllByNameContainsIgnoreCase(String name);
}