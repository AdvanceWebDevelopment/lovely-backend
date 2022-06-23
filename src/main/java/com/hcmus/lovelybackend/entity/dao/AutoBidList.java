package com.hcmus.lovelybackend.entity.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="auto_bid_list",  uniqueConstraints={
        @UniqueConstraint(name = "u_auto_bid_list", columnNames={"bidder_id", "product_id"})
})
public class AutoBidList implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "bidder_id", foreignKey = @ForeignKey(name = "FK_Product_Bidder_Bidder_Auto"))
    private UserDAO bidder;

    @ManyToOne()
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "FK_Product_Bidder_Product_Auto"))
    private Product product;

    @Column(name = "price", nullable = false)
    private Double price;
}
