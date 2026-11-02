package com.enes.finora.repository

import com.enes.finora.entity.Transfer
import org.springframework.data.jpa.repository.JpaRepository

interface TransferRepository : JpaRepository<Transfer, Long>