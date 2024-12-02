package com.yanvelasco.ecommerce.domain.cart.service.imp;

import com.yanvelasco.ecommerce.domain.cart.dto.response.CartResponseDto;
import com.yanvelasco.ecommerce.domain.cart.entities.CartEntity;
import com.yanvelasco.ecommerce.domain.cart.entities.CartItemEntity;
import com.yanvelasco.ecommerce.domain.cart.mapper.CartMapper;
import com.yanvelasco.ecommerce.domain.cart.repositories.CartItemRepository;
import com.yanvelasco.ecommerce.domain.cart.repositories.CartRepository;
import com.yanvelasco.ecommerce.domain.cart.service.CartService;
import com.yanvelasco.ecommerce.domain.product.entity.ProductEntity;
import com.yanvelasco.ecommerce.domain.product.repository.ProductRepository;
import com.yanvelasco.ecommerce.exceptions.QuantityException;
import com.yanvelasco.ecommerce.exceptions.ResourceNotFoundException;
import com.yanvelasco.ecommerce.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final AuthUtil authUtil;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;

    public CartServiceImpl(CartRepository cartRepository, AuthUtil authUtil, ProductRepository productRepository, CartItemRepository cartItemRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.authUtil = authUtil;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public ResponseEntity<CartResponseDto> addProductToCart(UUID productId, Integer quantity) {
        CartEntity cart = createCart();
        ProductEntity product = findProductById(productId);
        CartItemEntity cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getId(), productId);

        if (cartItem != null) {
            throw new QuantityException("Product already in cart");
        }

        if (product.getQuantity() == 0 || product.getQuantity() < quantity) {
            throw new QuantityException("Product out of stock");
        }

        CartItemEntity newCartItem = new CartItemEntity();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);
        product.setQuantity(product.getQuantity() - quantity);
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);

        CartResponseDto cartDTO = cartMapper.toResponseDTO(cart);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDTO);
    }

    @Override
    public ResponseEntity<List<CartResponseDto>> getAllCart() {
        List<CartEntity> carts = cartRepository.findAll();
        List<CartResponseDto> cartDTOs = cartMapper.toListResponseDTO(carts);
        return ResponseEntity.ok(cartDTOs);
    }

    @Override
    public ResponseEntity<CartResponseDto> getCartByUser(String emailId, UUID cartId) {
        CartEntity cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        CartResponseDto cartDTO = cartMapper.toResponseDTO(cart);
        return ResponseEntity.ok(cartDTO);
    }

    @Transactional
    @Override
    public ResponseEntity<CartResponseDto> updateProductQuantityInCart(UUID productId, int delete) {
        CartEntity cart = findCartByEmail();
        ProductEntity product = findProductById(productId);
        CartItemEntity cartItem = findCartItem(cart.getId(), productId);

        if (delete < 0) {
            if (cartItem.getQuantity() == 1) {
                cart.setTotalPrice(cart.getTotalPrice() - cartItem.getProductPrice());
                cartItemRepository.delete(cartItem);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cart.setTotalPrice(cart.getTotalPrice() - cartItem.getProductPrice());
                cartItemRepository.save(cartItem);
            }
        } else {
            if (product.getQuantity() < cartItem.getQuantity() + 1) {
                throw new QuantityException("Product out of stock");
            }
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cart.setTotalPrice(cart.getTotalPrice() + cartItem.getProductPrice());
            cartItemRepository.save(cartItem);
        }

        product.setQuantity(product.getQuantity() - (delete < 0 ? -1 : 1));
        productRepository.save(product);
        cartRepository.save(cart);

        CartResponseDto cartDTO = cartMapper.toResponseDTO(cart);
        return ResponseEntity.ok(cartDTO);
    }

    @Override
    public ResponseEntity<String> deleteProductFromCart(UUID cartId, UUID productId) {
        CartEntity cart = findCartById(cartId);
        ProductEntity product = findProductById(productId);
        CartItemEntity cartItem = findCartItem(cartId, productId);

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
        cartRepository.save(cart);

        product.setQuantity(product.getQuantity() + cartItem.getQuantity());
        productRepository.save(product);

        cartItemRepository.delete(cartItem);
        return ResponseEntity.ok("Product deleted from cart");
    }

    private CartEntity createCart() {
        CartEntity userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }

        CartEntity cart = new CartEntity();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        return cartRepository.save(cart);
    }

    private CartEntity findCartByEmail() {
        String emailId = authUtil.loggedInEmail();
        return cartRepository.findCartByEmail(emailId);
    }

    private CartEntity findCartById(UUID cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
    }

    private ProductEntity findProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
    }

    private CartItemEntity findCartItem(UUID cartId, UUID productId) {
        CartItemEntity cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "productId", productId);
        }
        return cartItem;
    }
}