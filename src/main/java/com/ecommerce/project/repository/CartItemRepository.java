package com.ecommerce.project.repository;

import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("Select ci from CartItem ci where ci.product.productId = ?1 and ci.cart.cartId = ?2")
    CartItem findCartItemByProductIdAndCartId(Long productId, Long cartId);

    @Query("Select c from CartItem c where c.cart.user.email = ?1 and c.product.productId = ?2")
    CartItem findCartByEmailAndProductId(String email, Long productId);


    @Transactional
    @Modifying
    @Query("Delete from CartItem c where c.cart.cartId = ?1 and c.product.productId = ?2")
    void deleteCartItemByCartIdAndProductId(Long cartId, Long productId);
}
