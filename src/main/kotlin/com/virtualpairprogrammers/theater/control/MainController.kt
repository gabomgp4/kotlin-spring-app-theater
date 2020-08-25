package com.virtualpairprogrammers.theater.control

import com.virtualpairprogrammers.theater.annotations.GenerateNoArgConstructor
import com.virtualpairprogrammers.theater.data.PerformanceRepository
import com.virtualpairprogrammers.theater.data.SeatRepository
import com.virtualpairprogrammers.theater.domain.Booking
import com.virtualpairprogrammers.theater.domain.Performance
import com.virtualpairprogrammers.theater.domain.Seat
import com.virtualpairprogrammers.theater.services.BookingService
import com.virtualpairprogrammers.theater.services.TheaterService
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import javax.persistence.Embeddable

@Controller
class MainController(
        val theaterService: TheaterService,
        val bookingService: BookingService,
        val seatRepository: SeatRepository,
        val performanceRepository: PerformanceRepository,
) {
    @RequestMapping("")
    fun homePage() = withBean(CheckAvailabilityBackingBean())

    @RequestMapping("checkAvailability", method = [RequestMethod.POST])
    fun checkAvailability(bean: CheckAvailabilityBackingBean) = run {
        val selectedSeat = theaterService.find(bean.selectedSeatRow, bean.selectedSeatNum)
        val selectedPerformance = performanceRepository.findByIdOrNull(bean.selectedPerformanceId)

        if (selectedSeat == null || selectedPerformance == null)
            throw NullPointerException("Not found entities: selectedPerformance or selectedSeat")

        val bean = run {
            val booking = bookingService.find(selectedSeat, selectedPerformance)
            if (booking != null)
                bean.copy(unavailable = Unavailable(booking))
            else
                bean.copy(available = Available(
                        performance = selectedPerformance,
                        seat = selectedSeat,
                        customerName = ""))
        }

        withBean(bean)
    }

    @RequestMapping("booking", method = [RequestMethod.POST])
    fun bookASeat(bean: CheckAvailabilityBackingBean) = run {
        if (bean.available == null) {
            throw NullPointerException("available cannot be null")
        }

        val available = bean.available
        val booking = bookingService.reserveSeat(available.seat, available.performance, available.customerName)
        ModelAndView("bookingConfirmed", "booking", booking)
    }

    private fun withBean(bean: CheckAvailabilityBackingBean) = ModelAndView("seatBooking")
            .addObject("performances", performanceRepository.findAll())
            .addObject("seatNums", CheckAvailabilityBackingBean.seatNums)
            .addObject("seatRows", CheckAvailabilityBackingBean.seatRows)
            .addObject("bean", bean)
}

data class CheckAvailabilityBackingBean(
        val selectedSeatNum: Int = 1,
        val selectedSeatRow: Char = 'A',
        val selectedPerformanceId: Long? = null,

        val available: Available? = null,
        val unavailable: Unavailable? = null,
) {
    companion object {
        val seatNums = 1..36
        val seatRows = 'A'..'O'
    }
}

@GenerateNoArgConstructor
data class Available(val seat: Seat, val performance: Performance, val customerName: String)

@GenerateNoArgConstructor
data class Unavailable(val booking: Booking)
