
package com.enes.finora

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Locale
import java.util.UUID

// Android'den gelecek transfer bilgileri
data class TransferRequest(
    val iban: String,
    val recipientName: String,
    val amount: BigDecimal,
    val description: String? = null
)

// Backend'in Android'e döndüreceği cevap
data class TransferResponse(
    val status: String,
    val message: String,
    val transferId: String? = null,
    val recipientName: String? = null,
    val iban: String? = null,
    val amount: BigDecimal? = null,
    val description: String? = null,
    val createdAt: String? = null
)

@RestController
@RequestMapping("/api/v1/transfers")
class TransferController {

    @PostMapping
    fun createTransfer(
        @RequestBody request: TransferRequest
    ): ResponseEntity<TransferResponse> {

        // IBAN boşluklarını temizle
        val iban = request.iban
            .filterNot { it.isWhitespace() }
            .uppercase(Locale.ROOT)

        val recipientName = request.recipientName.trim()

        // IBAN format kontrolü
        if (!iban.matches(Regex("^TR\\d{24}$"))) {
            return ResponseEntity.badRequest().body(
                TransferResponse(
                    status = "REJECTED",
                    message = "Geçerli formatta bir TR IBAN girilmelidir."
                )
            )
        }

        // Alıcı adı kontrolü
        if (recipientName.length < 3) {
            return ResponseEntity.badRequest().body(
                TransferResponse(
                    status = "REJECTED",
                    message = "Alıcı adı en az 3 karakter olmalıdır."
                )
            )
        }

        // Tutar kontrolü
        if (request.amount <= BigDecimal.ZERO) {
            return ResponseEntity.badRequest().body(
                TransferResponse(
                    status = "REJECTED",
                    message = "Transfer tutarı sıfırdan büyük olmalıdır."
                )
            )
        }

        // Demo transfer cevabı
        val response = TransferResponse(
            status = "DEMO_PREVIEW",
            message = "Transfer talebi başarıyla doğrulandı. Gerçek para transferi yapılmadı.",
            transferId = UUID.randomUUID().toString(),
            recipientName = recipientName,
            iban = iban,
            amount = request.amount,
            description = request.description?.trim(),
            createdAt = LocalDateTime.now().toString()
        )

        return ResponseEntity.ok(response)
    }
}
