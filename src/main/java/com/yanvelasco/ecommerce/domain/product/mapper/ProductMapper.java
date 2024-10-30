package com.yanvelasco.ecommerce.domain.product.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.yanvelasco.ecommerce.domain.category.entity.CategoryEntity;
import com.yanvelasco.ecommerce.domain.category.repository.CategoryRepository;
import com.yanvelasco.ecommerce.domain.exceptions.ResourceNotFoundException;
import com.yanvelasco.ecommerce.domain.product.dto.request.ProductRequestDTO;
import com.yanvelasco.ecommerce.domain.product.dto.response.PagedProductResponseDTO;
import com.yanvelasco.ecommerce.domain.product.dto.response.ProductResponseDTO;
import com.yanvelasco.ecommerce.domain.product.entity.ProductEntity;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void configureModelMapper() {
        Converter<ProductEntity, ProductResponseDTO> toResponseDTOConverter = new Converter<>() {
            @Override
            public ProductResponseDTO convert(MappingContext<ProductEntity, ProductResponseDTO> context) {
                ProductEntity source = context.getSource();
                return new ProductResponseDTO(
                    source.getId(),
                    source.getProductName(),
                    source.getProductDescription(),
                    source.getProductQuantity(),
                    source.getProductPrice(),
                    source.getSpecialPrice(),
                    source.getProductImage(),
                    source.getCategory().getId()
                );
            }
        };

        Converter<ProductRequestDTO, ProductEntity> toEntityConverter = new Converter<>() {
            @Override
            public ProductEntity convert(MappingContext<ProductRequestDTO, ProductEntity> context) {
                ProductRequestDTO source = context.getSource();
                ProductEntity entity = new ProductEntity();
                entity.setProductName(source.productName());
                entity.setProductDescription(source.productDescription());
                entity.setProductQuantity(source.productQuantity());
                entity.setProductPrice(source.productPrice());
                entity.setSpecialPrice(source.specialPrice());
                entity.setProductImage(source.productImage());
                CategoryEntity category = categoryRepository.findById(UUID.fromString(source.categoryId().toString()))
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", UUID.fromString(source.categoryId().toString())));
                entity.setCategory(category);                
                return entity;
            }
        };

        modelMapper.addConverter(toResponseDTOConverter);
        modelMapper.addConverter(toEntityConverter);
    }

    public ProductEntity toEntity(ProductRequestDTO productRequestDTO) {
        return modelMapper.map(productRequestDTO, ProductEntity.class);
    }

    public ProductResponseDTO toResponseDTO(ProductEntity productEntity) {
        return modelMapper.map(productEntity, ProductResponseDTO.class);
    }

    public PagedProductResponseDTO toPagedProductResponseDTO(Page<ProductEntity> productsPage) {
        List<ProductResponseDTO> productResponseDTOs = productsPage.getContent().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return new PagedProductResponseDTO(
                productResponseDTOs,
                productsPage.getNumber(),
                productsPage.getSize(),
                productsPage.getTotalElements(),
                productsPage.getTotalPages(),
                productsPage.isLast()
        );
    }
}
