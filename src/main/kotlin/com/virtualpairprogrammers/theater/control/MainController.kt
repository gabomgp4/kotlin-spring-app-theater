package com.virtualpairprogrammers.theater.control

import com.virtualpairprogrammers.theater.control.Availability.Available
import com.virtualpairprogrammers.theater.control.Availability.Unavailable
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

@Controller
class MainController(
        val theaterService: TheaterService,
        val bookingService: BookingService,
        val seatRepository: SeatRepository,
        val performanceRepository: PerformanceRepository
) {
    var logger = LoggerFactory.getLogger(MainController::class.java)

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
                bean.copy(availability = Unavailable(booking))
            else
                bean.copy(availability = Available(
                        performance = selectedPerformance,
                        seat = selectedSeat,
                        customerName = ""))
        }

        withBean(bean)
    }

    fun withBean(bean: CheckAvailabilityBackingBean) = run {
        val modelAndView = ModelAndView("seatBooking")
        modelAndView.addObject("performances", performanceRepository.findAll())
        modelAndView.addObject("seatNums", CheckAvailabilityBackingBean.seatNums)
        modelAndView.addObject("seatRows", CheckAvailabilityBackingBean.seatRows)
        modelAndView.addObject("bean", bean)
        modelAndView
    }
}

data class CheckAvailabilityBackingBean(
        val selectedSeatNum: Int = 1,
        val selectedSeatRow: Char = 'A',
        val selectedPerformanceId: Long? = null,

        val availability: Availability? = null
) {
    companion object {
        val seatNums = 1..36
        val seatRows = 'A'..'O'
    }
}

sealed class Availability {
    data class Available(val seat: Seat, val performance: Performance, val customerName: String) : Availability()
    data class Unavailable(val booking: Booking) : Availability()

    val available get() = this as? Available
    val unavailable get() = this as? Unavailable
}
