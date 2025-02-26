package com.yanvelasco.ecommerce.domain.product.service.imp;

import com.yanvelasco.ecommerce.domain.cart.dto.response.CartResponseDto;
import com.yanvelasco.ecommerce.domain.cart.entities.CartEntity;
import com.yanvelasco.ecommerce.domain.cart.repositories.CartRepository;
import com.yanvelasco.ecommerce.domain.cart.service.CartService;
import com.yanvelasco.ecommerce.domain.product.dto.request.ProductRequestDTO;
import com.yanvelasco.ecommerce.domain.product.dto.response.PagedProductResponseDTO;
import com.yanvelasco.ecommerce.domain.product.dto.response.ProductResponseDTO;
import com.yanvelasco.ecommerce.domain.product.entity.ProductEntity;
import com.yanvelasco.ecommerce.domain.product.mapper.ProductMapper;
import com.yanvelasco.ecommerce.domain.product.repository.ProductRepository;
import com.yanvelasco.ecommerce.domain.product.service.ProductService;
import com.yanvelasco.ecommerce.exceptions.EmpytException;
import com.yanvelasco.ecommerce.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final ProductMapper productMapper;
    private final CartService cartService;

    @Override
    public ResponseEntity<ProductResponseDTO> createProduct(UUID categoryId, ProductRequestDTO productRequestDTO) {
        ProductEntity productEntity = productMapper.toEntity(productRequestDTO, categoryId);
        ProductEntity savedProduct = productRepository.save(productEntity);
        ProductResponseDTO responseDTO = productMapper.toResponseDTO(savedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Override
    public ResponseEntity<PagedProductResponseDTO> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy
            , String sortOrder, String keyword, String category) {
        Sort sortByAndOrderBy = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrderBy);

        Specification<ProductEntity> specification = Specification.where(null);

        if (keyword != null && !keyword.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%")
            ));
        }

        if (category != null && !category.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("category").get("name"), category
            ));
        }


        Page<ProductEntity> productsPage = productRepository.findAll(specification, pageable);

        if (productsPage.isEmpty()) {
            throw new EmpytException("No products found");
        }

        PagedProductResponseDTO response = productMapper.toPagedProductResponseDTO(productsPage);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PagedProductResponseDTO> getProductsByCategory(UUID categoryId, int pageNumber,
                                                                         int pageSize, String sortBy,
                                                                         String sortOrder) throws ResourceNotFoundException {

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
    public ResponseEntity<PagedProductResponseDTO> getProductsByKeyword(String keyword, int pageNumber, int pageSize,
                                                                        String sortBy, String sortOrder) {
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
        ProductEntity productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        ProductEntity product = productMapper.toEntity(productRequestDTO, productFromDb.getId());

        if (product.getName() != null) {
            productFromDb.setName(product.getName());
        }
        if (product.getDescription() != null) {
            productFromDb.setDescription(product.getDescription());
        }
        if (product.getQuantity() != null) {
            productFromDb.setQuantity(product.getQuantity());
        }
        if (product.getDiscount() != null) {
            productFromDb.setDiscount(product.getDiscount());
        }
        if (product.getPrice() != null) {
            productFromDb.setPrice(product.getPrice());
        }
        if (product.getSpecialPrice() != null) {
            productFromDb.setSpecialPrice(product.getSpecialPrice());
        }

        ProductEntity savedProduct = productRepository.save(productFromDb);

        List<CartEntity> carts = cartRepository.findCartsByProductId(productId);

        List<CartResponseDto> cartDTOs = carts.stream().map(cart -> {
            List<ProductResponseDTO> products = cart.getCartItems().stream()
                    .map(p -> productMapper.toResponseDTO(p.getProduct())).collect(Collectors.toList());

            return new CartResponseDto(cart.getId(), cart.getTotalPrice(), products);
        }).toList();

        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.id(), productId));

        ProductResponseDTO responseDTO = productMapper.toResponseDTO(savedProduct);

        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(UUID productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        List<CartEntity> carts = cartRepository.findCartsByProductId(productId);

        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getId(), productId));

        productRepository.delete(productEntity);

        return ResponseEntity.noContent().build();
    }

}
