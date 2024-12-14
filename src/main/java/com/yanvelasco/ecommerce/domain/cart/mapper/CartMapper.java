package com.yanvelasco.ecommerce.domain.cart.mapper;

import com.yanvelasco.ecommerce.domain.cart.dto.response.CartResponseDto;
import com.yanvelasco.ecommerce.domain.cart.entities.CartEntity;
import com.yanvelasco.ecommerce.domain.product.dto.response.ProductResponseDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartMapper {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void configureModelMapper() {
        Converter<CartEntity, CartResponseDto> toResponseDTOConverter = new ConverterImplementation();
        modelMapper.addConverter(toResponseDTOConverter);
    }

    public CartResponseDto toResponseDTO(CartEntity cartEntity) {
        return modelMapper.map(cartEntity, CartResponseDto.class);
    }

    public List<CartResponseDto> toListResponseDTO(List<CartEntity> cartEntities) {
        return cartEntities.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private final class ConverterImplementation implements Converter<CartEntity, CartResponseDto> {
        @Override
        public CartResponseDto convert(MappingContext<CartEntity, CartResponseDto> context) {
            CartEntity source = context.getSource();
            List<ProductResponseDTO> products = source.getCartItems().stream()
                    .map(item -> new ProductResponseDTO(
                            item.getProduct().getId(),
                            item.getProduct().getName(),
                            item.getProduct().getDescription(),
                            item.getQuantity(),
                            item.getProduct().getPrice(),
                            item.getProduct().getDiscount(),
                            item.getProduct().getSpecialPrice(),
                            item.getProduct().getImage(),
                            item.getProduct().getCategory().getId()
                    ))
                    .toList();
            return new CartResponseDto(source.getId(), source.getTotalPrice(), products);
        }
    }
}