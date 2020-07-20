package com.virtualpairprogrammers.theater.domain

import java.math.BigDecimal


data class Seat(val row: Char, val num: Int, val price: BigDecimal, val description: String)