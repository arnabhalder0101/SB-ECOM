package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.HashSet;
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
    private Integer userId;

    @NotNull
    @Size(min=10)
    @Column(name = "user_name")
    private String userName;

    @Email
    @Size(min=15, message = "email must be of min 15 char")
    @NotBlank
    @Column(name = "email")
    private String email;

    @Size(min=5, message = "password must be of min 5 char")
    @NotBlank
    @Column(name = "password")
    private String password;

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }


    // creates a new table named "user_role" having user_id & role_id -> maintains relationship with both the tables.
    @Getter
    @Setter
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();


    @Getter
    @Setter
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name="user_address", joinColumns = @JoinColumn(name="user_id"),
        inverseJoinColumns = @JoinColumn(name = "address_id"))
    private Set<Address> addresses = new HashSet<>();


    // Because you specified mappedBy = "user" inside the User class,
    // Hibernate looks specifically for a field named user in the Product entity.
    @Getter
    @Setter
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", // field name
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}
            ,orphanRemoval = true)
    private Set<Product> products= new HashSet<>();
}
