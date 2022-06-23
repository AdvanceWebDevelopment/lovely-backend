package com.hcmus.lovelybackend.entity.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_bidder")
public class ProductBidder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "bidder_id", foreignKey = @ForeignKey(name = "FK_Product_Bidder_Bidder"))
    private UserDAO bidder;

    @ManyToOne()
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "FK_Product_Bidder_Product"))
    private Product product;

    @Column(name = "bid_at")
    private LocalDateTime bidAt = LocalDateTime.now();

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "reject", nullable = false)
    private Boolean reject = false;

    @Column(name = "status", nullable = false)
    private Boolean status = false;

    public String getBidAt() {
        if (this.bidAt != null)
            return this.bidAt.toString();
        return null;
    }
}
