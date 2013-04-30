package org.motechproject.icappr.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.motechproject.icappr.form.model.PillReminderRegistration;
import org.motechproject.icappr.service.PillReminderRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class RegistrationController {
    
    private Gson gson = new Gson();
    private PillReminderRegistrar pillReminderRegistrar;
    
    @Autowired
    public RegistrationController(PillReminderRegistrar pillReminderRegistrar) {
        this.pillReminderRegistrar = pillReminderRegistrar;
    }

    @RequestMapping("/registration")
    @ResponseBody
    public String getAllObjects(HttpServletRequest request, HttpServletResponse response) {
        String patientId = request.getParameter("patientId");
        PillReminderRegistration registration = pillReminderRegistrar.getRegistrationForPatient(patientId);
        if (registration == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Not Found";
        }
        
        return gson.toJson(registration);
    }
}
