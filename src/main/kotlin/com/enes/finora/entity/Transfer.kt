package com.enes.finora.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "transfers")
class Transfer(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 26)
    var iban: String,

    @Column(name = "recipient_name", nullable = false)
    var recipientName: String,

    @Column(nullable = false, precision = 19, scale = 2)
    var amount: BigDecimal,

    @Column(length = 100)
    var description: String? = null,

    @Column(nullable = false, length = 30)
    var status: String = "DEMO_PREVIEW",

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)