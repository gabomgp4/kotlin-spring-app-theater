package com.virtualpairprogrammers.theater.data

import com.virtualpairprogrammers.theater.domain.Seat
import org.springframework.data.jpa.repository.JpaRepository

interface SeatRepository : JpaRepository<Seat, Long>