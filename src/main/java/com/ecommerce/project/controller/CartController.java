package com.ecommerce.project.controller;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.security.services.UserDetailsImpl;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {


    private AuthUtil authUtil;
    private CartRepository cartRepository;
    private CartService cartService;

    @Autowired
    public CartController(CartService cartService, CartRepository cartRepository, AuthUtil authUtil){
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.authUtil = authUtil;
    }

    @PostMapping("/carts/product/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);

        return ResponseEntity.ok().body(cartDTO);
    }

    @GetMapping("/carts/all")
    public ResponseEntity<List<CartDTO>> getCarts(){

        List<CartDTO> cartDTOS = cartService.getAllCarts();

        return new ResponseEntity<>(cartDTOS, HttpStatus.OK);

    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById(){
        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Long cartId = 0l;
        if (userCart!=null)
            cartId = userCart.getCartId();
        else
            throw new APIException("Cart does not exists for this user: "+ emailId);


        CartDTO cartDTO=  cartService.getCart(emailId, cartId);

        return ResponseEntity.ok().body(cartDTO);

    }
}
