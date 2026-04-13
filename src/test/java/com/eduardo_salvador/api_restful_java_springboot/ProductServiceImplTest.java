package com.eduardo_salvador.api_restful_java_springboot;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductRecordDto;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductResponseDto;
import com.eduardo_salvador.api_restful_java_springboot.exceptions.NoFindException;
import com.eduardo_salvador.api_restful_java_springboot.mappers.ProductMapper;
import com.eduardo_salvador.api_restful_java_springboot.models.ProductModel;
import com.eduardo_salvador.api_restful_java_springboot.repositories.ProductRepository;
import com.eduardo_salvador.api_restful_java_springboot.services.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductMapper productMapper;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void correctSave() {
        ProductRecordDto dto = new ProductRecordDto("Notebook", BigDecimal.valueOf(3000));
        ProductModel productModel = new ProductModel();
        ProductResponseDto responseDto = new ProductResponseDto(UUID.randomUUID(), "Notebook", BigDecimal.valueOf(3000));

        when(productMapper.toModel(dto)).thenReturn(productModel);
        when(productRepository.save(productModel)).thenReturn(productModel);
        when(productMapper.toResponseDto(productModel)).thenReturn(responseDto);

        ProductResponseDto result = productService.save(dto);

        assertNotNull(result);
        assertEquals("Notebook", result.getName());
        verify(productRepository, times(1)).save(productModel);
    }

    @Test
    void correctFindAll() {
        ProductModel productModel = new ProductModel();
        ProductResponseDto responseDto = new ProductResponseDto(UUID.randomUUID(), "Notebook", BigDecimal.valueOf(3000));
        Page<ProductModel> page = new PageImpl<>(List.of(productModel));

        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(productMapper.toResponseDto(productModel)).thenReturn(responseDto);

        Page<ProductResponseDto> result = productService.findAll(null, null, null, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void incorrectFindAll() {
        Page<ProductModel> page = new PageImpl<>(List.of());

        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        assertThrows(NoFindException.class, () -> productService.findAll(null, null, null, Pageable.unpaged()));
    }

    @Test
    void correctFindById() {
        UUID id = UUID.randomUUID();
        ProductModel productModel = new ProductModel();
        ProductResponseDto responseDto = new ProductResponseDto(id, "Notebook", BigDecimal.valueOf(3000));

        when(productRepository.findById(id)).thenReturn(Optional.of(productModel));
        when(productMapper.toResponseDto(productModel)).thenReturn(responseDto);

        ProductResponseDto result = productService.findById(id);

        assertNotNull(result);
        assertEquals("Notebook", result.getName());
    }

    @Test
    void incorrectFindById() {
        UUID id = UUID.randomUUID();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoFindException.class, () -> productService.findById(id));
    }

    @Test
    void correctUpdate() {
        UUID id = UUID.randomUUID();
        ProductRecordDto dto = new ProductRecordDto("Notebook Pro", BigDecimal.valueOf(4000));
        ProductModel productModel = new ProductModel();
        ProductResponseDto responseDto = new ProductResponseDto(id, "Notebook Pro", BigDecimal.valueOf(4000));

        when(productRepository.findById(id)).thenReturn(Optional.of(productModel));
        when(productRepository.save(productModel)).thenReturn(productModel);
        when(productMapper.toResponseDto(productModel)).thenReturn(responseDto);

        ProductResponseDto result = productService.update(id, dto);

        assertNotNull(result);
        assertEquals("Notebook Pro", result.getName());
        verify(productRepository, times(1)).save(productModel);
    }

    @Test
    void incorrectUpdate() {
        UUID id = UUID.randomUUID();
        ProductRecordDto dto = new ProductRecordDto("Notebook Pro", BigDecimal.valueOf(4000));

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoFindException.class, () -> productService.update(id, dto));
        verify(productRepository, never()).save(any());
    }

    @Test
    void correctDelete() {
        UUID id = UUID.randomUUID();
        ProductModel productModel = new ProductModel();

        when(productRepository.findById(id)).thenReturn(Optional.of(productModel));

        productService.delete(id);

        verify(productRepository, times(1)).delete(productModel);
    }

    @Test
    void incorrectDelete() {
        UUID id = UUID.randomUUID();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoFindException.class, () -> productService.delete(id));
        verify(productRepository, never()).delete((ProductModel) any());
    }
}