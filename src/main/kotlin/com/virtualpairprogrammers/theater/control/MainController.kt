package com.virtualpairprogrammers.theater.control

import com.virtualpairprogrammers.theater.data.SeatRepository
import com.virtualpairprogrammers.theater.services.BookingService
import com.virtualpairprogrammers.theater.services.TheaterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class MainController(
        val theaterService: TheaterService,
        val bookingService: BookingService,
        val seatRepository: SeatRepository
) {
    @RequestMapping("")
    fun homePage() =
            ModelAndView("seatBooking", "bean", CheckAvailabilityBackingBean())

    @RequestMapping("checkAvailability", method = [RequestMethod.POST])
    fun checkAvailability(bean: CheckAvailabilityBackingBean) = run {
        val selectedSeat = theaterService.find(bean.selectedSeatRow, bean.selectedSeatNum)
        val result = bookingService.isSeatFree(selectedSeat)
        ModelAndView("seatBooking", mapOf("bean" to bean.copy(
                result = "Seat $selectedSeat is ${if (result) "available" else "booked"}"
        )))
    }

//    @RequestMapping("bootstrap")
//    fun createInitialData() = run {
//        seatRepository.saveAll(TheaterService.seats)
//        homePage()
//    }

}

data class CheckAvailabilityBackingBean(
        val selectedSeatNum: Int = 1,
        val selectedSeatRow: Char = 'A',
        val result: String = ""
) {
    val seatNums = 1..36
    val seatRows = 'A'..'O'
}