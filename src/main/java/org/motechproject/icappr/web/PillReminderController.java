package org.motechproject.icappr.web;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.icappr.domain.AdherenceCallEnrollmentRequest;
import org.motechproject.icappr.domain.AdherenceCallEnrollmentResponse;
import org.motechproject.icappr.domain.MrsPatientSearchResult;
import org.motechproject.icappr.domain.AdherenceCallResponse;
import org.motechproject.icappr.mrs.MrsEntityFacade;
import org.motechproject.icappr.service.AdherenceCallEnroller;
import org.motechproject.icappr.service.AdherenceCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PillReminderController {

    private final AdherenceCallEnroller enroller;
    private final MrsEntityFacade mrsEntityFacade;
    private final AdherenceCallService adherenceCallService;

    @Autowired
    public PillReminderController(AdherenceCallEnroller enroller, MrsEntityFacade mrsEntityFacade,
            AdherenceCallService adherenceCallService) {
        this.enroller = enroller;
        this.mrsEntityFacade = mrsEntityFacade;
        this.adherenceCallService = adherenceCallService;
    }

    @RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) {
        return modelWithPath("index", request);
    }

    private ModelAndView modelWithPath(String viewName, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(viewName);
        mv.addObject("path", getFullPathFromRequest(request));
        return mv;
    }

    private String getFullPathFromRequest(HttpServletRequest request) {
        String scheme = request.getScheme();
        String host = request.getHeader("Host");
        String contextPath = request.getContextPath();

        return scheme + "://" + host + contextPath;
    }

    @RequestMapping(value = "/enroll", method = RequestMethod.GET)
    public ModelAndView getEnrollPartial(HttpServletRequest request) {
        return modelWithPath("enroll", request);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView getSearchPartial(HttpServletRequest request) {
        return new ModelAndView("search");
    }

    @RequestMapping(value = "/enrollment", method = RequestMethod.POST)
    @ResponseBody
    public AdherenceCallEnrollmentResponse addNewEnrollment(@RequestBody AdherenceCallEnrollmentRequest request) {
        DateTime now = DateUtil.now();
        // round up to nearest minute
        // for example, Hour=10,Minute=20,second=30
        // start time will be Hour=10,Minute=21
        if (now.getSecondOfMinute() % 60 != 0) {
            now = now.plusMinutes(1);
        }
        request.setDosageStartTime(now.getHourOfDay() + ":" + String.format("%02d", now.getMinuteOfHour()));
        return enroller.enrollPatientWithId(request);
    }

    @RequestMapping(value = "/search-patient/{motechId}", method = RequestMethod.GET)
    @ResponseBody
    public MrsPatientSearchResult searchForPatient(@PathVariable String motechId) {
        return MrsPatientSearchResult.fromMrsPatient(mrsEntityFacade.findPatientByMotechId(motechId));
    }

    @RequestMapping(value = "/pillreminders/{motechId}", method = RequestMethod.GET)
    @ResponseBody
    public AdherenceCallResponse searchForPillReminder(@PathVariable String motechId) {
        return adherenceCallService.findAdherenceCallByMotechId(motechId);
    }

    @RequestMapping(value = "/pillreminders/{motechId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deletePatient(@PathVariable String motechId) {
        adherenceCallService.deleteAdherenceCall(motechId);
    }
}
