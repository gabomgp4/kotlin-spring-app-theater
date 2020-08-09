@file:Suppress("JpaAttributeMemberSignatureInspection")

package com.virtualpairprogrammers.theater.domain

import java.math.BigDecimal
import javax.persistence.*

@Entity
data class Seat(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,
        @Column(name = "\"row\"")
        val row: Char,
        val num: Int,
        val price: BigDecimal,
        val description: String
)