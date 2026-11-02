
package com.enes.finora

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class DashboardController {

    @GetMapping("/dashboard")
    fun getDashboard(): DashboardDto {
        return DashboardDto(
            balance = 48250.75,
            currency = "TRY",
            accountName = "Ana Hesabım",
            transactions = listOf(
                TransactionDto(
                    "txn-001",
                    "Market alışverişi",
                    "Kart - Bugün",
                    -425.50,
                    "EXPENSE"
                ),
                TransactionDto(
                    "txn-002",
                    "Gelen transfer",
                    "Transfer - Dün",
                    2500.00,
                    "INCOME"
                )
            )
        )
    }
}
