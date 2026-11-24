package com.ecommerce.project.controller;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.payload.AllCartsDTO;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    private AuthUtil authUtil;
    private CartRepository cartRepository;
    private CartService cartService;
    private ModelMapper modelMapper;

    @Autowired
    public CartController(CartService cartService, CartRepository cartRepository, AuthUtil authUtil, ModelMapper modelMapper){
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.authUtil = authUtil;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/carts/product/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);

        return ResponseEntity.ok().body(cartDTO);
    }

    @GetMapping("/carts/all")
    public ResponseEntity<AllCartsDTO> getCarts(){

        List<CartDTO> cartDTOS = cartService.getAllCarts();

        AllCartsDTO allCartsDTO= new AllCartsDTO();
        allCartsDTO.setCarts(cartDTOS);
        allCartsDTO.setCount(cartDTOS.size());
        allCartsDTO.setTotalCartsValue(cartDTOS.stream().mapToDouble(cart -> cart.getTotalPrice()).sum());

        return new ResponseEntity<>(allCartsDTO, HttpStatus.OK);

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

    @PutMapping("/cart/product/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProductQuantity(
            @PathVariable Long productId,
            @PathVariable String operation
    ){
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete")? -1 : 1);

        return ResponseEntity.ok().body(cartDTO);

    }

    @DeleteMapping("/cart/{cartId}/product/{productId}")
    public ResponseEntity<CartDTO> deleteProductFromCart(
            @PathVariable Long cartId,
            @PathVariable Long productId
    ){
        CartDTO cartDTO = cartService.deleteProductFromCart(cartId, productId);
        return ResponseEntity.ok().body(cartDTO);
    }
}
