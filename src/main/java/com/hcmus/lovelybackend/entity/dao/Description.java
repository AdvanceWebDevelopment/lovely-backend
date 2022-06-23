package com.hcmus.lovelybackend.entity.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "description")
public class Description implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JsonIgnore
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "FK_Description_Product"))
    private Product product;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "description")
    private String description;

    public String getCreateAt() {
        return this.createAt.toString();
    }
}
