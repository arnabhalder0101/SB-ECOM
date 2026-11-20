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
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
