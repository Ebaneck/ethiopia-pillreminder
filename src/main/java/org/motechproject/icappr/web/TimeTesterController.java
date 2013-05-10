package org.motechproject.icappr.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TimeTesterController {

    @RequestMapping("/timetest")
    @ResponseBody
    public void testTime(HttpServletRequest httpServletRequest) {

    }
}
