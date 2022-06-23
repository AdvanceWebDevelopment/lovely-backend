package com.hcmus.lovelybackend.entity.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subcategory", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class SubCategory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    @Size(min = 3, max = 100)
    private String name;

    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "FK_subcategory_category"))
    @JsonIgnoreProperties({"subCategories"})
    private Category category;
}
