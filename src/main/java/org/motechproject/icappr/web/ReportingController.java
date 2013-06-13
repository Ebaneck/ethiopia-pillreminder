package org.motechproject.icappr.web;

import javax.servlet.http.HttpServletRequest;
import org.motechproject.icappr.PillReminderSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportingController {

    private Logger logger = LoggerFactory.getLogger("reporting-log");

    @Autowired
    private PillReminderSettings pillReminderSettings;

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public ModelAndView generateDailyReport(HttpServletRequest reqest) {

        return null;
    }
}
