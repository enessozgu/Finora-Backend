package com.enes.finora.service

import com.enes.finora.entity.Transfer
import com.enes.finora.repository.TransferRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import org.springframework.data.domain.Sort

@Service
class TransferService(
    private val transferRepository: TransferRepository
) {

    @Transactional
    fun saveDemoTransfer(
        iban: String,
        recipientName: String,
        amount: BigDecimal,
        description: String?
    ): Transfer {

        val transfer = Transfer(
            iban = iban,
            recipientName = recipientName,
            amount = amount,
            description = description,
            status = "DEMO_PREVIEW"
        )

        return transferRepository.save(transfer)
    }

    @Transactional(readOnly = true)
    fun getAllTransfers(): List<Transfer> {
        return transferRepository.findAll(
            Sort.by(Sort.Direction.DESC, "createdAt")
        )
    }
}