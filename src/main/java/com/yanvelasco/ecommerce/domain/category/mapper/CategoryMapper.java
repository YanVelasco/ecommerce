package com.yanvelasco.ecommerce.domain.category.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.yanvelasco.ecommerce.domain.category.dto.CategoryRequestDTO;
import com.yanvelasco.ecommerce.domain.category.dto.CategoryResponseDTO;
import com.yanvelasco.ecommerce.domain.category.dto.PagedCategoryResponseDTO;
import com.yanvelasco.ecommerce.domain.category.entity.CategoryEntity;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void configureModelMapper() {
        Converter<CategoryEntity, CategoryResponseDTO> toResponseDTOConverter = new Converter<>() {
            @Override
            public CategoryResponseDTO convert(MappingContext<CategoryEntity, CategoryResponseDTO> context) {
                CategoryEntity source = context.getSource();
                return new CategoryResponseDTO(
                    source.getId(),
                    source.getName()
                );
            }
        };

        Converter<CategoryRequestDTO, CategoryEntity> toEntityConverter = new Converter<>() {
            @Override
            public CategoryEntity convert(MappingContext<CategoryRequestDTO, CategoryEntity> context) {
                CategoryRequestDTO source = context.getSource();
                CategoryEntity entity = new CategoryEntity();
                entity.setName(source.name());
                return entity;
            }
        };

        modelMapper.addConverter(toResponseDTOConverter);
        modelMapper.addConverter(toEntityConverter);
    }

    public CategoryEntity toEntity(CategoryRequestDTO categoryRequestDTO) {
        return modelMapper.map(categoryRequestDTO, CategoryEntity.class);
    }

    public CategoryResponseDTO toResponseDTO(CategoryEntity categoryEntity) {
        return modelMapper.map(categoryEntity, CategoryResponseDTO.class);
    }

    public PagedCategoryResponseDTO toPagedCategoryResponseDTO(Page<CategoryEntity> categoriesPage) {
        List<CategoryResponseDTO> categoryResponseDTOs = categoriesPage.getContent().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return new PagedCategoryResponseDTO(
                categoryResponseDTOs,
                categoriesPage.getNumber(),
                categoriesPage.getSize(),
                categoriesPage.getTotalElements(),
                categoriesPage.getTotalPages(),
                categoriesPage.isLast()
        );
    }
}