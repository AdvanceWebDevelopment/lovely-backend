package com.hcmus.lovelybackend.entity.dao;

import com.fasterxml.jackson.annotation.*;
import com.hcmus.lovelybackend.constant.AuthProvider;
import com.hcmus.lovelybackend.repository.TransactionRepository;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class UserDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    @NotNull(message = "Name is mandatory")
    @Size(min = 2, max = 64)
    private String name;

    @Column(name = "birth_date")
    private LocalDateTime birthDay;

    @Column(name = "email", nullable = false)
    @Email
    @NotNull(message = "Email is mandatory")
    @Size(max = 100)
    private String email;

    @Column(name = "image")
    @URL
    private String imageUrl;

    @Column(name = "email_verified")
    @JsonIgnore
    private Boolean emailVerified = false;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @NotNull(message = "provider is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    @JsonIgnore
    private AuthProvider provider;

    @Column(name = "provider_id")
    @JsonIgnore
    private String providerId;

    @Column(name = "role", nullable = false)
    private Integer role = 1;

//    @Column(name = "point")
//    private Integer point = 0;

//    @Column(name = "id_watch_list")
//    @JsonIgnore
//    private Integer idWatchList;
//
//    @Column(name = "id_bid_list")
//    @JsonIgnore
//    private Integer idBidList;

    @Column(name = "rf_token")
    @JsonIgnore
    private String refreshToken;

    @Column(name = "verification_code", unique = true)
    @JsonIgnore
    private String verificationCode;

    @Column(name = "enabled")
    @JsonIgnore
    private Boolean enabled;

    @OneToMany(mappedBy = "bidder", cascade = CascadeType.MERGE, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    Set<ProductBidder> productBidders;

    @OneToMany(mappedBy = "assessor", cascade = CascadeType.MERGE, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    Set<Transaction> sellTransaction;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.MERGE, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    Set<Transaction> buyTransaction;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.MERGE, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    List<Product> productsSeller;

    @Column(name = "point")
    private Double point;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserDAO userDAO = (UserDAO) o;
        return false;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String getBirthDay() {
        if(this.birthDay != null)
            return this.birthDay.toString();
        return null;
    }

}
