package com.yanvelasco.ecommerce.domain.user.mappers;

import com.yanvelasco.ecommerce.domain.user.dto.request.AddressRequestDTO;
import com.yanvelasco.ecommerce.domain.user.dto.response.AddressResponseDTO;
import com.yanvelasco.ecommerce.domain.user.entities.AddressEntity;
import com.yanvelasco.ecommerce.domain.user.entities.UserEntity;
import jakarta.annotation.PostConstruct;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    private final ModelMapper modelMapper;

    public AddressMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    private final class ConverterImplementation implements Converter<AddressEntity, AddressResponseDTO> {
        @Override
        public AddressResponseDTO convert(MappingContext<AddressEntity, AddressResponseDTO> context) {
            AddressEntity source = context.getSource();
            return new AddressResponseDTO(
                source.getId(),
                source.getStreet(),
                source.getCity(),
                source.getCountry(),
                source.getZipCode(),
                source.getState(),
                source.getNumber()
            );
        }
    }

    @PostConstruct
    public void configureModelMapper() {
        Converter<AddressEntity, AddressResponseDTO> toResponseDTOConverter = new ConverterImplementation();
        modelMapper.addConverter(toResponseDTOConverter);
    }

    public AddressEntity toEntity(AddressRequestDTO addressRequestDTO, UserEntity user) {
        AddressEntity entity = new AddressEntity();
        entity.setStreet(addressRequestDTO.street());
        entity.setCity(addressRequestDTO.city());
        entity.setCountry(addressRequestDTO.country());
        entity.setZipCode(addressRequestDTO.zipCode());
        entity.setState(addressRequestDTO.state());
        entity.setNumber(addressRequestDTO.number());
        entity.setUser(user);
        return entity;
    }

    public AddressResponseDTO toResponseDTO(AddressEntity addressEntity) {
        return modelMapper.map(addressEntity, AddressResponseDTO.class);
    }
}