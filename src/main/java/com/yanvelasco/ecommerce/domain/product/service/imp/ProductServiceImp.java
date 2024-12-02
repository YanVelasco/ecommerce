package com.yanvelasco.ecommerce.domain.product.service.imp;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.yanvelasco.ecommerce.exceptions.EmpytException;
import com.yanvelasco.ecommerce.exceptions.ResourceNotFoundException;
import com.yanvelasco.ecommerce.domain.product.dto.request.ProductRequestDTO;
import com.yanvelasco.ecommerce.domain.product.dto.response.PagedProductResponseDTO;
import com.yanvelasco.ecommerce.domain.product.dto.response.ProductResponseDTO;
import com.yanvelasco.ecommerce.domain.product.entity.ProductEntity;
import com.yanvelasco.ecommerce.domain.product.mapper.ProductMapper;
import com.yanvelasco.ecommerce.domain.product.repository.ProductRepository;
import com.yanvelasco.ecommerce.domain.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    public ResponseEntity<ProductResponseDTO> createProduct(UUID categoryId, ProductRequestDTO productRequestDTO) {
        ProductEntity productEntity = productMapper.toEntity(productRequestDTO, categoryId);
        ProductEntity savedProduct = productRepository.save(productEntity);
        ProductResponseDTO responseDTO = productMapper.toResponseDTO(savedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Override
    public ResponseEntity<PagedProductResponseDTO> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrderBy = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrderBy);
        Page<ProductEntity> productsPage = productRepository.findAll(pageable);

        if (productsPage.isEmpty()) {
            throw new EmpytException("No products found");
        }

        PagedProductResponseDTO response = productMapper.toPagedProductResponseDTO(productsPage);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PagedProductResponseDTO> getProductsByCategory(UUID categoryId, int pageNumber, int pageSize, String sortBy, String sortOrder) throws ResourceNotFoundException {

        if (!productRepository.existsByCategoryId(categoryId)) {
            throw new ResourceNotFoundException("Category", "id", categoryId);
        }

        Sort sortByAndOrderBy = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrderBy);
        Page<ProductEntity> productsPage = productRepository.findByCategoryId(categoryId, pageable);

        if (productsPage.isEmpty()) {
            throw new EmpytException("No products found");
        }

        PagedProductResponseDTO response = productMapper.toPagedProductResponseDTO(productsPage);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PagedProductResponseDTO> getProductsByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrderBy = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrderBy);
        Page<ProductEntity> productsPage = productRepository.searchByKeyword(keyword, pageable);

        if (productsPage.isEmpty()) {
            throw new EmpytException("No products found");
        }

        PagedProductResponseDTO response = productMapper.toPagedProductResponseDTO(productsPage);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProductResponseDTO> updateProduct(UUID productId, ProductRequestDTO productRequestDTO) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        if (productRequestDTO.name() != null) {
            productEntity.setName(productRequestDTO.name());
        }

        if (productRequestDTO.description() != null) {
            productEntity.setDescription(productRequestDTO.description());
        }

        if (productRequestDTO.quantity() != null) {
            productEntity.setQuantity(productRequestDTO.quantity());
        }

        if (productRequestDTO.price() != null) {
            productEntity.setPrice(productRequestDTO.price());
        }

        if (productRequestDTO.discount() != null) {
            productEntity.setDiscount(productRequestDTO.discount());
        }

        productEntity.calculateSpecialPrice();

        ProductEntity updatedProduct = productRepository.save(productEntity);
        ProductResponseDTO responseDTO = productMapper.toResponseDTO(updatedProduct);

        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(UUID productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        productRepository.delete(productEntity);

        return ResponseEntity.noContent().build();
    }

}
