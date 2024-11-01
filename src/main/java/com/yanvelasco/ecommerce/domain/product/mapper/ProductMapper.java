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

    private final class ConverterImplementation implements Converter<ProductEntity, ProductResponseDTO> {
        @Override
        public ProductResponseDTO convert(MappingContext<ProductEntity, ProductResponseDTO> context) {
            ProductEntity source = context.getSource();
            return new ProductResponseDTO(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getQuantity(),
                source.getPrice(),
                source.getDiscount(),
                source.getSpecialPrice(),
                source.getImage(),
                source.getCategory().getId()
            );
        }
    }

    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void configureModelMapper() {
        Converter<ProductEntity, ProductResponseDTO> toResponseDTOConverter = new ConverterImplementation();

        modelMapper.addConverter(toResponseDTOConverter);
    }

    public ProductEntity toEntity(ProductRequestDTO productRequestDTO, UUID categoryId) {
        ProductEntity entity = new ProductEntity();
        entity.setName(productRequestDTO.name());
        entity.setDescription(productRequestDTO.description());
        entity.setQuantity(productRequestDTO.quantity());
        entity.setPrice(productRequestDTO.price());
        entity.setDiscount(productRequestDTO.discount());
        entity.setImage(productRequestDTO.image());
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        entity.setCategory(category);
        entity.calculateSpecialPrice();
        return entity;
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
