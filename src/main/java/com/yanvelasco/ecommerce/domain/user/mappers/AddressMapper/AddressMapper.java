package com.yanvelasco.ecommerce.domain.user.mappers.AddressMapper;

import com.yanvelasco.ecommerce.domain.user.dto.request.AddressRequestDTO;
import com.yanvelasco.ecommerce.domain.user.dto.response.AddressResponseDTO;
import com.yanvelasco.ecommerce.domain.user.entities.AddressEntity;
import com.yanvelasco.ecommerce.domain.user.entities.UserEntity;
import jakarta.annotation.PostConstruct;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressMapper {

    private final ModelMapper modelMapper;

    public AddressMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<AddressResponseDTO> toResponseListDTO(List<AddressEntity> addresses) {
        return addresses.stream()
            .map(this::toResponseDTO)
            .toList();
    }

    public void updateEntity(AddressEntity address, AddressRequestDTO addressRequestDto) {
        if (addressRequestDto.street() != null) {
            address.setStreet(addressRequestDto.street());
        }
        if (addressRequestDto.city() != null) {
            address.setCity(addressRequestDto.city());
        }
        if (addressRequestDto.country() != null) {
            address.setCountry(addressRequestDto.country());
        }
        if (addressRequestDto.zipCode() != null) {
            address.setZipCode(addressRequestDto.zipCode());
        }
        if (addressRequestDto.state() != null) {
            address.setState(addressRequestDto.state());
        }
        if (addressRequestDto.number() != null) {
            address.setNumber(addressRequestDto.number());
        }
    }

    private final class ConverterImplementation implements Converter<AddressEntity, AddressResponseDTO> {
        @Override
        public AddressResponseDTO convert(MappingContext<AddressEntity, AddressResponseDTO> context) {
            AddressEntity source = context.getSource();
            return new AddressResponseDTO(
                source.getId(),
                source.getStreet(),
                source.getNumber(),
                source.getCity(),
                source.getState(),
                source.getZipCode(),
                source.getCountry()
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
        entity.setNumber(addressRequestDTO.number());
        entity.setCity(addressRequestDTO.city());
        entity.setState(addressRequestDTO.state());
        entity.setZipCode(addressRequestDTO.zipCode());
        entity.setCountry(addressRequestDTO.country());
        entity.setUser(user);
        return entity;
    }

    public AddressResponseDTO toResponseDTO(AddressEntity addressEntity) {
        return new AddressResponseDTO(
            addressEntity.getId(),
            addressEntity.getStreet(),
            addressEntity.getNumber(),
            addressEntity.getCity(),
            addressEntity.getState(),
            addressEntity.getZipCode(),
            addressEntity.getCountry()
        );
    }
}