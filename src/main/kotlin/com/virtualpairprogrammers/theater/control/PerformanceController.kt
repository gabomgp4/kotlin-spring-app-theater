package com.virtualpairprogrammers.theater.control

import com.virtualpairprogrammers.theater.data.PerformanceRepository
import com.virtualpairprogrammers.theater.domain.Performance
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("performances")
class PerformanceController(
        val performanceRepository: PerformanceRepository
) {

    @RequestMapping("")
    fun performancesHomePage() =
            ModelAndView("performances/home", "performances", performanceRepository.findAll())

    @RequestMapping("/add")
    fun addPerformance() =
            ModelAndView("performances/add", "performance", Performance(0, ""))

    @RequestMapping("save", method = [RequestMethod.POST])
    fun savePerformance(performance: Performance) = run {
        performanceRepository.save(performance)
        "redirect:/performances/"
    }
}