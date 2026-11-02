
package com.enes.finora

import com.enes.finora.service.TransferService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.Locale

// Android'den gelen transfer isteği
data class TransferRequest(
    val iban: String,
    val recipientName: String,
    val amount: BigDecimal,
    val description: String? = null
)

// Android'e döndürülen transfer cevabı
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
class TransferController(
    private val transferService: TransferService
) {

    // POST: Yeni transfer oluştur ve veritabanına kaydet
    @PostMapping
    fun createTransfer(
        @RequestBody request: TransferRequest
    ): ResponseEntity<TransferResponse> {

        val iban = request.iban
            .filterNot { it.isWhitespace() }
            .uppercase(Locale.ROOT)

        val recipientName = request.recipientName.trim()

        // IBAN kontrolü
        if (!iban.matches(Regex("^TR\\d{24}$"))) {
            return ResponseEntity.badRequest().body(
                TransferResponse(
                    status = "REJECTED",
                    message = "Geçerli bir TR IBAN girilmelidir."
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

        // Açıklama kontrolü
        if ((request.description?.length ?: 0) > 100) {
            return ResponseEntity.badRequest().body(
                TransferResponse(
                    status = "REJECTED",
                    message = "Açıklama en fazla 100 karakter olabilir."
                )
            )
        }

        // Transferi PostgreSQL'e kaydet
        val savedTransfer = transferService.saveDemoTransfer(
            iban = iban,
            recipientName = recipientName,
            amount = request.amount,
            description = request.description?.trim()
        )

        // API cevabını hazırla
        val response = TransferResponse(
            status = savedTransfer.status,
            message = "Demo transfer veritabanına kaydedildi. Gerçek para transferi yapılmadı.",
            transferId = savedTransfer.id.toString(),
            recipientName = savedTransfer.recipientName,
            iban = savedTransfer.iban,
            amount = savedTransfer.amount,
            description = savedTransfer.description,
            createdAt = savedTransfer.createdAt.toString()
        )

        return ResponseEntity.ok(response)
    }

    // GET: Veritabanındaki tüm transferleri listele
    @GetMapping
    fun getAllTransfers(): ResponseEntity<List<TransferResponse>> {

        val transfers = transferService.getAllTransfers()

        val response = transfers.map { transfer ->

            TransferResponse(
                status = transfer.status,
                message = "Kayıtlı demo transfer",
                transferId = transfer.id.toString(),
                recipientName = transfer.recipientName,
                iban = transfer.iban,
                amount = transfer.amount,
                description = transfer.description,
                createdAt = transfer.createdAt.toString()
            )
        }

        return ResponseEntity.ok(response)
    }
}
