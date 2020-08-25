package com.virtualpairprogrammers.theater.domain

import javax.persistence.*

@Entity
data class Booking(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,
        val customerName: String
) {
    constructor(id: Long,
                customerName: String,
                seat: Seat,
                performance: Performance)
            : this(id, customerName) {
        this.seat = seat
        this.performance = performance
    }

    @ManyToOne
    lateinit var seat: Seat

    @ManyToOne
    lateinit var performance: Performance
}