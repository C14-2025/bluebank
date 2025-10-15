package com.inatel.blue_bank.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Getter
@Setter
@ToString
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "card_number", length = 50, nullable = false,unique = true)
    private String cardNumber;

    @Column(name = "card_validity", nullable = false)
    private LocalDate cv;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private DocType cardType;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @OneToMany(mappedBy = "card")
    private List<CardBill> cardBills;
}
