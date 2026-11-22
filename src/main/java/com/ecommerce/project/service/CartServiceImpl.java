package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.repository.CartItemRepository;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.security.services.UserDetailsImpl;
import com.ecommerce.project.util.AuthUtil;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        // find existing cart or create one
        Cart cart = createCart();

        // retrive product from DB
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));


        // perform validation -like stock out etc.
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cart.getCartId());

        if (cartItem != null) {
//            throw new APIException("Product " + product.getProductName() + " already exists in cart");
            /*
            CR: if alredy exists, and quantity is available in stock
                then increase cartItem quantity.
            */
            if (product.getProductQuantity() > quantity) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItemRepository.save(cartItem);

                cart.setTotalPrice(cart.getTotalPrice() + product.getProductSpecialPrice() * quantity);

                cartRepository.save(cart);
            }


            return modelMapper.map(cart, CartDTO.class);


        }
        if (product.getProductQuantity() == 0) {
            throw new APIException("Product " + product.getProductName() + " is not available/Out of Stock");
        }
        if (product.getProductQuantity() < quantity) {
            throw new APIException("Requested Product Stock: " + product.getProductQuantity() + "\nYou ordered: " + quantity);
        }
        // create cartItem
        CartItem newCartItem = new CartItem();

        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setProductPrice(product.getProductSpecialPrice());
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getProductDiscount());

        // save cartitem
        cartItemRepository.save(newCartItem);

        cart.setTotalPrice(cart.getTotalPrice() + newCartItem.getProductPrice() * quantity);

        List<CartItem> cartCartItems = cart.getCartItems();
        cartCartItems.add(newCartItem);

        cart.setCartItems(cartCartItems);
        System.out.println("LOGG::\n\n" + cartCartItems.size());

        cartRepository.save(cart);
        //return updated Cart
        return modelMapper.map(cart, CartDTO.class);
    }

    @Override
    public List<CartDTO> getAllCarts() {

        List<Cart> carts = cartRepository.findAll();

        if(carts.isEmpty()){
            throw new APIException("No Carts Available");
        }

        List<CartDTO> cartDTOs = carts.stream()
                .map(item -> modelMapper.map(item, CartDTO.class))
                .toList();

        return cartDTOs;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);

        if(cart == null){
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        return modelMapper.map(cart, CartDTO.class);
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {

        String email = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product", "productId", productId));
        if(product.getProductQuantity() == 0){
            throw new APIException(product.getProductName() + " is not available");
        }
        if(product.getProductQuantity() < quantity){
            throw new APIException(product.getProductName() + " Product out of Stock");
        }

        if(cart == null){
            throw new ResourceNotFoundException("Cart", "user emailId", email);
        }
        // to update cart item it must be in cart
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cart.getCartId());

        if(cartItem == null){
            throw  new APIException("CartItem " + product.getProductName()+ " not exists in the Cart");
        }

        cartItem.setProductPrice(product.getProductSpecialPrice());
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setDiscount(product.getProductDiscount());

        cart.setTotalPrice(cart.getTotalPrice() + cartItem.getProductPrice() * quantity);

        Cart updatedCart = cartRepository.save(cart);
        CartItem updatedCartItem =  cartItemRepository.save(cartItem);

        if(updatedCartItem.getQuantity() == 0){
            updatedCart.getCartItems().remove(updatedCartItem);
            cartItemRepository.deleteById(updatedCartItem.getCartItemId());
        }

        return modelMapper.map(updatedCart, CartDTO.class);


//        String email = authUtil.loggedInEmail();
//        CartItem cartItem = cartItemRepository.findCartByEmailAndProductId(email, productId);
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Product", "productId", productId));
//
//        if(product.getProductQuantity() < operation){
//            throw new APIException("Product out of Stock");
//        }
//        if(cartItem== null){
//            throw new ResourceNotFoundException("Product", "productId", productId);
//        }
//
//        if(cartItem.getQuantity()>0) {
//            cartItem.setQuantity(cartItem.getQuantity() + operation);
//        }
//
//        CartItem savedCartItem = cartItemRepository.save(cartItem);
//        if(savedCartItem.getQuantity() == 0){
//            cartItemRepository.delete(savedCartItem);
//        }
//
//        Cart cart = cartRepository.findCartByEmailAndCartId(email, savedCartItem.getCart().getCartId());
//
//        if(operation>0)
//            cart.setTotalPrice(cart.getTotalPrice() + product.getProductSpecialPrice());
//        else
//            cart.setTotalPrice(cart.getTotalPrice() - product.getProductSpecialPrice());
//
//        Cart dbCart = cartRepository.save(cart);
//
//        return modelMapper.map(dbCart, CartDTO.class);
    }

    private Cart createCart() {
        Cart existingCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (existingCart == null) {
            Cart cart = new Cart();

            cart.setTotalPrice(0.0);
            cart.setUser(authUtil.loggedInUser());

            return cartRepository.save(cart);
        }

        return existingCart;
    }
}
