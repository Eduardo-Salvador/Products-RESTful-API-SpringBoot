package com.eduardo_salvador.api_restful_java_springboot.repositories;
import com.eduardo_salvador.api_restful_java_springboot.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> { }