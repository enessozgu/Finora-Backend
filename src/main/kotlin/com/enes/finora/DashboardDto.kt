package com.enes.finora

data class TransactionDto(
    val id: String,
    val title: String,
    val description: String,
    val amount: Double,
    val type: String
)

data class DashboardDto(
    val balance: Double,
    val currency: String,
    val accountName: String,
    val transactions: List<TransactionDto>
)