package com.virtualpairprogrammers.theater.services

import com.virtualpairprogrammers.theater.data.BookingRepository
import com.virtualpairprogrammers.theater.domain.Performance
import com.virtualpairprogrammers.theater.domain.Seat
import org.springframework.stereotype.Service

@Service
class BookingService(val bookingRepository: BookingRepository) {
    fun find(seat: Seat, performance: Performance) = bookingRepository
            .findAll()
            .firstOrNull { it.seat == seat && it.performance == performance }
}