package com.hcmus.lovelybackend.entity.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(name = "current_price", nullable = false)
    private Double currentPrice;

    @Column(name = "step_price", nullable = false)
    private Double stepPrice;

    @Column(name = "quick_price")
    private Double quickPrice;

    @ManyToOne()
    @JoinColumn(name = "bidder_highest_id", foreignKey = @ForeignKey(name = "FK_Product_Bidder_Highest"))
    private UserDAO bidderHighest;

    @Column(name = "auto_bid", nullable = false)
    private Boolean autoBid;

    @ManyToOne()
    @JoinColumn(name = "seller_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Product_Seller"))
    private UserDAO seller;

    @ManyToOne()
    @JoinColumn(name = "subcategory_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Product_Subcategory"))
    private SubCategory subCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.MERGE, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private Set<ProductBidder> productBidders;

    @OneToMany(mappedBy = "product", cascade = CascadeType.MERGE, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private Set<AutoBidList> autoBidLists;

    @OneToMany(mappedBy = "product", cascade = CascadeType.MERGE, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private Set<TemporaryBidder> temporaryBidders;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Image> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("createAt ASC")
    private List<Description> descriptions;

    @Column(name = "notified", nullable = false)
    private Boolean notified = false;

    private transient Integer currentBids = 0;

    public String getCreateAt() {
        if (this.createAt != null)
            return this.createAt.toString();
        return null;
    }

    public String getEndAt() {
        if (this.endAt != null)
            return this.endAt.toString();
        return null;
    }

    @JsonIgnore
    public LocalDateTime getEndAtTypeDateTime() {
        return this.endAt;
    }

    public Integer getCurrentBids() {
        return this.productBidders.size();
    }
}
