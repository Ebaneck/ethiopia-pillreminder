package org.motechproject.icappr.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportingController {

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public ModelAndView generateDailyReport(HttpServletRequest reqest) {
        
        return null;
    }
}
