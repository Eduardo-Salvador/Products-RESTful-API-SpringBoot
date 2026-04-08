package com.eduardo_salvador.api_restful_java_springboot.services;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductRecordDto;
import com.eduardo_salvador.api_restful_java_springboot.models.ProductModel;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductModel save(ProductRecordDto dto);
    List<ProductModel> findAll();
    ProductModel findById(UUID id);
    ProductModel update(UUID id, ProductRecordDto dto);
    void delete(UUID id);
}