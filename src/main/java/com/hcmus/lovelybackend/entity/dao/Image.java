package com.hcmus.lovelybackend.entity.dao;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image")
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JsonIgnore
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "FK_Image_Product"))
    private Product product;

    @Column(name = "main", nullable = false)
    private Boolean isMain;

    @Column(name = "url", nullable = false)
    private String url;
}
