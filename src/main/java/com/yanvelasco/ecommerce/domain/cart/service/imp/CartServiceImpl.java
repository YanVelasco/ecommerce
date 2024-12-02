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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartMapper cartMapper;

    @Override
    public ResponseEntity<CartResponseDto> addProductToCart(UUID productId, Integer quantity) {
        CartEntity cart = createCart();

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItemEntity cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getId(), productId);

        if (cartItem != null) {
            throw new QuantityException("Product already in cart");
        }

        if (product.getQuantity() == 0) {
            throw new QuantityException("Product out of stock");
        }

        if (product.getQuantity() < quantity) {
            throw new QuantityException("Product out of stock");
        }

        CartItemEntity newCartItem = new CartItemEntity();

        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity());

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
        String emailId = authUtil.loggedInEmail();
        CartEntity cart = cartRepository.findCartByEmail(emailId);
        UUID cartId = cart.getId();

        CartEntity cartEntity = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItemEntity cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "productId", productId);
        }

        if (delete < 0) {
            if (cartItem.getQuantity() == 1) {
                cartEntity.setTotalPrice(cartEntity.getTotalPrice() - cartItem.getProductPrice());
                cartItemRepository.delete(cartItem);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartEntity.setTotalPrice(cartEntity.getTotalPrice() - cartItem.getProductPrice());
                cartItemRepository.save(cartItem);
            }
        } else {
            if (productEntity.getQuantity() < cartItem.getQuantity() + 1) {
                throw new QuantityException("Product out of stock");
            }
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartEntity.setTotalPrice(cartEntity.getTotalPrice() + cartItem.getProductPrice());
            cartItemRepository.save(cartItem);
        }

        productEntity.setQuantity(productEntity.getQuantity() - (delete < 0 ? -1 : 1));
        productRepository.save(productEntity);
        cartRepository.save(cartEntity);

        CartResponseDto cartDTO = cartMapper.toResponseDTO(cartEntity);

        return ResponseEntity.ok(cartDTO);
    }

    private CartEntity createCart() {
        CartEntity userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }

        CartEntity cart = new CartEntity();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        CartEntity newCart = cartRepository.save(cart);

        return newCart;
    }
}