package com.php.design_patten_demo.entity;

import com.php.design_patten_demo.enums.ApprovalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "approval_request")
public class ApprovalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_no", unique = true, nullable = false, length = 50)
    private String requestNo;

    @Column(name = "applicant_id", nullable = false)
    private Long applicantId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApprovalStatus status = ApprovalStatus.PENDING;

    @Column(name = "approver_id")
    private Long approverId;

    @Column(name = "approval_comment", columnDefinition = "TEXT")
    private String approvalComment;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "applicant_id", insertable = false, updatable = false, referencedColumnName = "id")
    private User applicant;

    @ManyToOne
    @JoinColumn(name = "approver_id", insertable = false, updatable = false, referencedColumnName = "id")
    private User approver;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false, referencedColumnName = "id")
    private Product product;
}
