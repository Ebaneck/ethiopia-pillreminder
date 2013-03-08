package org.motechproject.icappr.web;

import java.io.File;

import org.joda.time.DateTime;
import org.motechproject.appointments.api.service.AppointmentService;
import org.motechproject.appointments.api.service.contract.CreateVisitRequest;
import org.motechproject.appointments.api.service.contract.ReminderConfiguration;
import org.motechproject.appointments.api.service.contract.ReminderConfiguration.IntervalUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AppointmentTester {

	@Autowired
	   private AppointmentService appointmentService;

	    @RequestMapping("/testapt")
		    @ResponseBody
		    public String testApt() {
               String externalId = "6251";
			   CreateVisitRequest createVisitRequest = new CreateVisitRequest();
			   createVisitRequest.setAppointmentDueDate(DateTime.now().plusMinutes(10));
			   createVisitRequest.setTypeOfVisit("test");
			   createVisitRequest.setVisitName("tester");
			   ReminderConfiguration appointmentReminderConfiguration = new ReminderConfiguration();
			   appointmentReminderConfiguration.setIntervalCount(90);
			   appointmentReminderConfiguration.setIntervalUnit(IntervalUnit.SECONDS);
			   appointmentReminderConfiguration.setRemindFrom(480);
			   appointmentReminderConfiguration.setRepeatCount(3);
			   createVisitRequest.addAppointmentReminderConfiguration(appointmentReminderConfiguration);
			   appointmentService.addVisit(externalId, createVisitRequest);
			   return "success";
	    }

}
