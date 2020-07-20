package com.virtualpairprogrammers.theater.services

import com.virtualpairprogrammers.theater.domain.Seat
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TheaterService {
    fun find(row: Char, num: Int) = seats.firstOrNull { it.num == num && it.row == row }

    companion object {
        private fun getPrice(row: Int, num: Int) = BigDecimal(when {
            row >= 14 -> 14.5
            num <= 3 || num >= 34 -> 16.5
            row == 1 -> 21.0
            else -> 18.0
        })

        private fun getDescription(row: Int, num: Int) = when {
            row == 15 -> "Back Row"
            row == 14 -> "Cheaper Seat"
            num <= 3 || num >= 34 -> "Restricted View"
            row <= 2 -> "Best View"
            else -> "Standard Seat"
        }

        val seats = (1..15).flatMap { row ->
            (1..36).map { num ->
                Seat((row + 64).toChar(), num, getPrice(row, num), getDescription(row, num))
            }
        }
    }

}