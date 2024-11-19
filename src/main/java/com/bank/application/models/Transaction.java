package com.bank.application.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entity that represents transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(example = "DEPOSIT", description = "Transaction type")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Schema(example = "1.0", description = "Transaction amount")
    @Column(nullable = false)
    private BigDecimal amount;

    @Schema(example = "Plain text", description = "Optional message attached to transaction")
    private String comment;

    @Schema(example = "dummy", description = "Transaction creation date")
    @Column(nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
}
