package org.motechproject.icappr.web;

import javax.servlet.http.HttpServletRequest;
import org.motechproject.icappr.domain.MrsPatientSearchResult;
import org.motechproject.icappr.mrs.MrsEntityFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PillReminderController {

    private final MrsEntityFacade mrsEntityFacade;

    @Autowired
    public PillReminderController(MrsEntityFacade mrsEntityFacade) {
        this.mrsEntityFacade = mrsEntityFacade;
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

    @RequestMapping(value = "/search-patient/{motechId}", method = RequestMethod.GET)
    @ResponseBody
    public MrsPatientSearchResult searchForPatient(@PathVariable String motechId) {
        return MrsPatientSearchResult.fromMrsPatient(mrsEntityFacade.findPatientByMotechId(motechId));
    }

    @RequestMapping(value = "/clearAll", method = RequestMethod.GET)
    @ResponseBody
    public String deleteAllDemoPersons() {
        mrsEntityFacade.clearAllPersons();
        return "Deleted all MRS Persons";
    }
}
