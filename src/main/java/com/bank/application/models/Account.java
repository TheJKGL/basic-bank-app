package com.bank.application.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Schema(description = "Entity that represents account details")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(example = "1", description = "Account number randomly generated")
    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Schema(example = "admin@admin.com", description = "Account emails specified by user")
    @Column(unique = true, nullable = false)
    private String email;

    @Schema(example = "1.0", description = "Account balance")
    @Column(nullable = false)
    private BigDecimal balance;

    @Schema(example = "2024-11-18T23:34:29.283273Z", description = "Account creation date")
    @CreationTimestamp
    private OffsetDateTime createdTime;
}
