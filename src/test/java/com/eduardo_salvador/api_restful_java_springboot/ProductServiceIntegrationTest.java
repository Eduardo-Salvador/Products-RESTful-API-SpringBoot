package com.eduardo_salvador.api_restful_java_springboot;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductRecordDto;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductResponseDto;
import com.eduardo_salvador.api_restful_java_springboot.exceptions.NoFindException;
import com.eduardo_salvador.api_restful_java_springboot.models.ProductModel;
import com.eduardo_salvador.api_restful_java_springboot.repositories.ProductRepository;
import com.eduardo_salvador.api_restful_java_springboot.services.ProductServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class ProductServiceIntegrationTest {

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void correctSave() {
        ProductRecordDto dto = new ProductRecordDto("Notebook", BigDecimal.valueOf(3000));

        ProductResponseDto result = productService.save(dto);

        assertNotNull(result.getIdProduct());
        assertEquals("Notebook", result.getName());
        assertEquals(BigDecimal.valueOf(3000), result.getPrice());
        assertEquals(1, productRepository.count());
    }

    @Test
    void correctFindAll() {
        ProductModel product1 = new ProductModel();
        product1.setName("Notebook");
        product1.setPrice(BigDecimal.valueOf(3000));
        productRepository.save(product1);

        ProductModel product2 = new ProductModel();
        product2.setName("Mouse");
        product2.setPrice(BigDecimal.valueOf(200));
        productRepository.save(product2);

        Page<ProductResponseDto> result = productService.findAll(null, null, null, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void correctFindAllWithNameFilter() {
        ProductModel product1 = new ProductModel();
        product1.setName("Notebook");
        product1.setPrice(BigDecimal.valueOf(3000));
        productRepository.save(product1);

        ProductModel product2 = new ProductModel();
        product2.setName("Mouse");
        product2.setPrice(BigDecimal.valueOf(200));
        productRepository.save(product2);

        Page<ProductResponseDto> result = productService.findAll("Notebook", null, null, Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        assertEquals("Notebook", result.getContent().get(0).getName());
    }

    @Test
    void correctFindAllWithPriceFilter() {
        ProductModel product1 = new ProductModel();
        product1.setName("Notebook");
        product1.setPrice(BigDecimal.valueOf(3000));
        productRepository.save(product1);

        ProductModel product2 = new ProductModel();
        product2.setName("Mouse");
        product2.setPrice(BigDecimal.valueOf(200));
        productRepository.save(product2);

        Page<ProductResponseDto> result = productService.findAll(null, BigDecimal.valueOf(500), BigDecimal.valueOf(5000), Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        assertEquals("Notebook", result.getContent().get(0).getName());
    }

    @Test
    void incorrectFindAll() {
        assertThrows(NoFindException.class, () -> productService.findAll(null, null, null, Pageable.unpaged()));
    }

    @Test
    void correctFindById() {
        ProductModel productModel = new ProductModel();
        productModel.setName("Notebook");
        productModel.setPrice(BigDecimal.valueOf(3000));
        ProductModel saved = productRepository.save(productModel);

        ProductResponseDto result = productService.findById(saved.getIdProduct());

        assertNotNull(result);
        assertEquals("Notebook", result.getName());
    }

    @Test
    void incorrectFindById() {
        assertThrows(NoFindException.class, () -> productService.findById(UUID.randomUUID()));
    }

    @Test
    void correctUpdate() {
        ProductModel productModel = new ProductModel();
        productModel.setName("Notebook");
        productModel.setPrice(BigDecimal.valueOf(3000));
        ProductModel saved = productRepository.save(productModel);

        ProductRecordDto dto = new ProductRecordDto("Notebook Pro", BigDecimal.valueOf(4000));
        ProductResponseDto result = productService.update(saved.getIdProduct(), dto);

        assertNotNull(result);
        assertEquals("Notebook Pro", result.getName());
        assertEquals(BigDecimal.valueOf(4000), result.getPrice());
    }

    @Test
    void incorrectUpdate() {
        ProductRecordDto dto = new ProductRecordDto("Notebook Pro", BigDecimal.valueOf(4000));

        assertThrows(NoFindException.class, () -> productService.update(UUID.randomUUID(), dto));
    }

    @Test
    void correctDelete() {
        ProductModel productModel = new ProductModel();
        productModel.setName("Notebook");
        productModel.setPrice(BigDecimal.valueOf(3000));
        ProductModel saved = productRepository.save(productModel);

        productService.delete(saved.getIdProduct());

        assertEquals(0, productRepository.count());
    }

    @Test
    void incorrectDelete() {
        assertThrows(NoFindException.class, () -> productService.delete(UUID.randomUUID()));
    }
}