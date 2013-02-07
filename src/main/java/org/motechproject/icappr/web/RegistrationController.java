package org.motechproject.icappr.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegistrationController {

    @RequestMapping("/registration")
    @ResponseBody
    public String getAllObjects(HttpServletRequest request) {
        String param = request.getParameter("patientId");

        return param;
    }
}
