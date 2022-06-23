package com.hcmus.lovelybackend.entity.dao;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="upgrade_list")
public class UpgradeList implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne()
    @JoinColumn(name = "bidder_id", nullable = false, foreignKey = @ForeignKey(name = "FK_Bidder_UpgradeList"))
    private UserDAO bidder;
}
