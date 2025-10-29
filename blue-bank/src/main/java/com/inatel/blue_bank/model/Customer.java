package com.inatel.blue_bank.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
@ToString
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dob;

    @Column(name = "doc_number", length = 50, nullable = false)
    private String docNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "doc_type", nullable = false)
    private DocType docType;

    @Column(length = 30, nullable = false)
    private String nationality;

    @Column(length = 30, nullable = false)
    private String phone;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String occupation;

    @OneToOne(mappedBy = "customer")
    private Account account;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Customer(UUID id,
                    String fullName,
                    LocalDate dob,
                    String nationality,
                    String phone,
                    String email,
                    String occupation,
                    DocType docType,
                    String docNumber,
                    LocalDateTime createdAt,
                    LocalDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.dob = dob;
        this.nationality = nationality;
        this.phone = phone;
        this.email = email;
        this.occupation = occupation;
        this.docType = docType;
        this.docNumber = docNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
