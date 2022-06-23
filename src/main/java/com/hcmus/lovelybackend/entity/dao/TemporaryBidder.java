package com.hcmus.lovelybackend.entity.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="temporary_bidder",  uniqueConstraints={
        @UniqueConstraint(name = "u_temporary_bidder_product", columnNames={"bidder_id", "product_id"})
})
public class TemporaryBidder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "bidder_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Bidder_Temporary"))
    private UserDAO bidder;

    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Product_Temporary"))
    private Product product;

    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();

    @Column(name = "status")
    private Boolean status;

    public String getCreateAt() {
        if(this.createAt != null)
            return this.createAt.toString();
        return null;
    }

    @JsonIgnore
    public LocalDateTime getCreatedAtTypeDateTime(){
        return this.createAt;
    }
}
