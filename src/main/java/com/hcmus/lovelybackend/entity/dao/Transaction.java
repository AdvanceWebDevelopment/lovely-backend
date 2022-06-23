package com.hcmus.lovelybackend.entity.dao;

import com.hcmus.lovelybackend.constant.TransactionStatus;
import com.hcmus.lovelybackend.constant.TransactionType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="transaction",  uniqueConstraints={
        @UniqueConstraint(name = "u_evaluation", columnNames={"assessor_id", "recipient_id", "product_id"})
})
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "assessor_id", nullable = false, foreignKey = @ForeignKey(name = "FK_assessor"))
    private UserDAO assessor;

    @ManyToOne()
    @JoinColumn(name = "recipient_id", nullable = false, foreignKey = @ForeignKey(name = "FK_recipient"))
    private UserDAO recipient;

    @OneToOne()
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "FK_evaluation_product"))
    private Product product;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();

    @Column(name = "is_like", nullable = false)
    private Boolean isLike;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.SUCCESS;

    public String getCreateAt() {
        if(this.createAt != null)
            return this.createAt.toString();
        return null;
    }
}
