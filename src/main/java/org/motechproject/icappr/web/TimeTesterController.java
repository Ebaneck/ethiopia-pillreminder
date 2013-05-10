package org.motechproject.icappr.web;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.joda.time.chrono.EthiopicChronology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TimeTesterController {

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    @RequestMapping("/timetest")
    @ResponseBody
    public String testTime(HttpServletRequest request) {

        String monthsToAdd = request.getParameter("months");
        String daysToAdd = request.getParameter("days");
        String hoursToAdd = request.getParameter("hours");
        String minutesToAdd = request.getParameter("minutes");


        StringBuilder returnString = new StringBuilder("Gregorian vs Ethiopian dates");

        EthiopicChronology ethiopiaChronology = EthiopicChronology.getInstance();

        DateTime gregorian = DateTime.now();
        DateTime ethiopian = DateTime.now(ethiopiaChronology);

        returnString.append("Time in Gregorian: " + gregorian.toString() + " day of week: " + gregorian.getDayOfWeek() + " month of year" + gregorian.getMonthOfYear() + " and year: " + gregorian.getYear() + "\r\n");
        returnString.append("Time in Ethiopia: " + ethiopian.toString() +  " day of week: " + ethiopian.getDayOfWeek() + " month of year" + ethiopian.getMonthOfYear() + " and year: " + ethiopian.getYear() + "\r\n");

        gregorian = transformTime(gregorian, monthsToAdd, daysToAdd, hoursToAdd, minutesToAdd);
        ethiopian = transformTime(ethiopian, monthsToAdd, daysToAdd, hoursToAdd, minutesToAdd);

        returnString.append("Time in Gregorian: " + gregorian.toString() + " day of week: " + gregorian.getDayOfWeek() + " month of year" + gregorian.getMonthOfYear() + " and year: " + gregorian.getYear() + "\r\n");
        returnString.append("Time in Ethiopia: " + ethiopian.toString() +  " day of week: " + ethiopian.getDayOfWeek() + " month of year" + ethiopian.getMonthOfYear() + " and year: " + ethiopian.getYear() + "\r\n");

        return returnString.toString();

    }

    private DateTime transformTime(DateTime time, String monthsToAdd, String daysToAdd, String hoursToAdd, String minutesToAdd) {

        if (monthsToAdd != null) {
            time = time.plusMonths(Integer.parseInt(monthsToAdd));
        }
        if (daysToAdd != null) {
            time = time.plusDays(Integer.parseInt(daysToAdd));
        }
        if (hoursToAdd != null) {
            time = time.plusHours(Integer.parseInt(hoursToAdd));
        }
        if (minutesToAdd != null) {
            time = time.plusMinutes(Integer.parseInt(minutesToAdd));
        }

        return time;
    }
}
