package com.yanvelasco.ecommerce.domain.category.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.yanvelasco.ecommerce.domain.category.dto.request.CategoryRequestDTO;
import com.yanvelasco.ecommerce.domain.category.dto.response.CategoryResponseDTO;
import com.yanvelasco.ecommerce.domain.category.dto.response.PagedCategoryResponseDTO;
import com.yanvelasco.ecommerce.domain.category.entity.CategoryEntity;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final class ConverterImplementation2 implements Converter<CategoryRequestDTO, CategoryEntity> {
        @Override
        public CategoryEntity convert(MappingContext<CategoryRequestDTO, CategoryEntity> context) {
            CategoryRequestDTO source = context.getSource();
            CategoryEntity entity = new CategoryEntity();
            entity.setName(source.name());
            return entity;
        }
    }

    private final class ConverterImplementation implements Converter<CategoryEntity, CategoryResponseDTO> {
        @Override
        public CategoryResponseDTO convert(MappingContext<CategoryEntity, CategoryResponseDTO> context) {
            CategoryEntity source = context.getSource();
            return new CategoryResponseDTO(
                source.getId(),
                source.getName()
            );
        }
    }

    private final ModelMapper modelMapper;

    @PostConstruct
    public void configureModelMapper() {
        Converter<CategoryEntity, CategoryResponseDTO> toResponseDTOConverter = new ConverterImplementation();

        Converter<CategoryRequestDTO, CategoryEntity> toEntityConverter = new ConverterImplementation2();

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