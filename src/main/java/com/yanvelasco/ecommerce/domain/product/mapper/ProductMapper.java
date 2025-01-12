package com.yanvelasco.ecommerce.domain.product.mapper;

import com.yanvelasco.ecommerce.domain.category.entity.CategoryEntity;
import com.yanvelasco.ecommerce.domain.category.repository.CategoryRepository;
import com.yanvelasco.ecommerce.domain.product.dto.request.ProductRequestDTO;
import com.yanvelasco.ecommerce.domain.product.dto.response.PagedProductResponseDTO;
import com.yanvelasco.ecommerce.domain.product.dto.response.ProductResponseDTO;
import com.yanvelasco.ecommerce.domain.product.entity.ProductEntity;
import com.yanvelasco.ecommerce.exceptions.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @PostConstruct
    public void configureModelMapper() {
        Converter<ProductEntity, ProductResponseDTO> toResponseDTOConverter = new ConverterImplementation();
        modelMapper.addConverter(toResponseDTOConverter);
    }

    private String getImageUrl(String fileName) {
        return imageBaseUrl.endsWith("/")
                ? imageBaseUrl + fileName
                : imageBaseUrl + "/" + fileName;
    }

    public ProductEntity toEntity(ProductRequestDTO productRequestDTO, UUID categoryId) {
        ProductEntity entity = new ProductEntity();
        entity.setName(productRequestDTO.name());
        entity.setDescription(productRequestDTO.description());
        entity.setQuantity(productRequestDTO.quantity());
        entity.setPrice(productRequestDTO.price());
        entity.setDiscount(productRequestDTO.discount());
        entity.setImage("default.jpg");
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
                    getImageUrl(source.getImage()),
                    source.getCategory().getId()
            );
        }
    }
}