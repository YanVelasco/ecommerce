package com.yanvelasco.ecommerce.domain.product.service.imp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yanvelasco.ecommerce.exceptions.ResourceNotFoundException;
import com.yanvelasco.ecommerce.domain.product.dto.response.ProductResponseDTO;
import com.yanvelasco.ecommerce.domain.product.entity.ProductEntity;
import com.yanvelasco.ecommerce.domain.product.mapper.ProductMapper;
import com.yanvelasco.ecommerce.domain.product.repository.ProductRepository;
import com.yanvelasco.ecommerce.domain.product.service.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImp implements FileService {

    private final ProductMapper productMapper;

    private final ProductRepository productRepository;

    @Value("${projects.image}")
    private String path;

    @Override
    public ResponseEntity<ProductResponseDTO> updateProductImage(UUID productId, MultipartFile image) throws IOException {

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        String fileName = uploadImage(path, image);

        productEntity.setImage(fileName);

        ProductEntity updatedProduct = productRepository.save(productEntity);
        ProductResponseDTO responseDTO = productMapper.toResponseDTO(updatedProduct);
        return ResponseEntity.ok(responseDTO);

    }

    private String uploadImage(String path, MultipartFile image) throws IOException {

        String originalFilename = image.getOriginalFilename();

        String randomUUID = UUID.randomUUID().toString();

        if (originalFilename == null) {
            throw new IllegalArgumentException("Original filename is null");
        }

        String fileName = randomUUID.concat(originalFilename.substring(originalFilename.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;

        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        Files.copy(image.getInputStream(), Paths.get(filePath));

        return fileName;

    }

}
