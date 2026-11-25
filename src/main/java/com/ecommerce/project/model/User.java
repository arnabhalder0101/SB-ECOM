package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "user_name"),
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "username must not be blank")
    @Size(min = 3, message = "username must be min 3 character")
    @Column(name = "user_name")
    private String userName;

    @Email
    @Size(min = 5, message = "email must be of min 5 char")
    @NotBlank(message = "email must not be blank")
    @Column(name = "email")
    private String email;

    @Size(min = 5, message = "password must be of min 5 char")
    @NotBlank(message = "password must not be blank")
    @Column(name = "password")
    private String password;

    // creates a new table named "user_role" having user_id & role_id -> maintains relationship with both the tables.
    @Getter
    @Setter
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @Getter
    @Setter
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();


    // Because you specified mappedBy = "user" inside the User class,
    // Hibernate looks specifically for a field named user in the Product entity.
    @Getter
    @Setter
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", // field name
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}
            , orphanRemoval = true)
    private Set<Product> products = new HashSet<>();


    @ToString.Exclude
    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Cart cart;

    // Even though it's optional, adding private Cart cart; to your User model gives you a huge convenience: navigation from User to Cart. --> user.getCart()

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
}
