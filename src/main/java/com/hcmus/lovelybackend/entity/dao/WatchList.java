package com.hcmus.lovelybackend.entity.dao;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="watch_list",  uniqueConstraints={
        @UniqueConstraint(name = "u_watchlist_bidder_product", columnNames={"bidder_id", "product_id"})
})
public class WatchList implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ToString.Exclude
    @ManyToOne()
    @JoinColumn(name = "bidder_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Bidder_WatchList"))
    private UserDAO bidder;

    @ToString.Exclude
    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Product_WatchList"))
    private Product product;
}
