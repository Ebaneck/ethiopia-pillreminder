package org.motechproject.converter.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.motechproject.callflow.service.FlowSessionService;

import org.motechproject.converter.old.domain.FlowSessionRecord;
import org.motechproject.converter.old.domain.CallDetailRecord;
import org.motechproject.converter.old.domain.CallDisposition;
import org.motechproject.converter.old.repository.AllFlowSessionRecords;
import org.motechproject.ivr.domain.CallEventLog;

/**
 * Converts Flow Session Records to a newer data model.
 * 
 * readRecords() reads "old" style flow session records into memory, 
 * using Call Flow and IVR repository and domain classes that are 
 * duplicated in this module and modified to accommodate the "old" 
 * AUTHENTICATOIN_FAILED call disposition.
 * 
 * writeRecords() updates each record in memory and in the repository.
 */
@Controller
public class FlowSessionRecordConverterController {
    private static final String PIN_FAILURE_CALL_EVENT = "Pin Failure";
    private static final String VERBOICE_CALL_SID = "CallSid";

    private Logger logger = LoggerFactory.getLogger("motech-flow-session-record-converter");

    private List<FlowSessionRecord> recordsInMemory;

    @Autowired
    private AllFlowSessionRecords allFlowSessionRecords;

    @RequestMapping("/readRecords")
    @ResponseBody
    public void readRecords(HttpServletRequest request) {
        recordsInMemory = allFlowSessionRecords.getAll();
        logger.debug("Read flow session records into memory: " + recordsInMemory.size());
    }

    @RequestMapping("/writeRecords")
    @ResponseBody
    public void writeRecords(HttpServletRequest request) {
        for (FlowSessionRecord oldRecord : recordsInMemory) {
            updateRecord(oldRecord);
            allFlowSessionRecords.update(oldRecord);
        }
        logger.debug("Updated flow session records in repository: " + recordsInMemory.size());
    }

    private void updateRecord(FlowSessionRecord oldRecord) {
        CallDetailRecord callDetail = oldRecord.getCallDetailRecord();
        if (null != callDetail) {

            // stop using unofficial AUTHENTICATON_FAILED
            // instead, make sure there's a "Pin failure" call event
            if (callDetail.getDisposition() == CallDisposition.AUTHENTICATION_FAILED) {
                callDetail.setDisposition(CallDisposition.ANSWERED);

                boolean hasPinFailureEvent = false;
                List<CallEventLog> callEvents = callDetail.getCallEvents();
                for (CallEventLog event : callEvents) {
                    if (PIN_FAILURE_CALL_EVENT.equals(event.getName())) {
                        hasPinFailureEvent = true;
                        break;
                    }
                }

                if (!hasPinFailureEvent) {
                    CallEventLog pinFailureEvent = new CallEventLog(PIN_FAILURE_CALL_EVENT);
                    callDetail.addCallEvent(pinFailureEvent);
                }
            }

            // move call Id from call detail to flow session record
            String VerboiceCallSid = oldRecord.get(VERBOICE_CALL_SID);
            if (null == VerboiceCallSid) {
                oldRecord.set(VERBOICE_CALL_SID, callDetail.getCallId());
            }
        }
    }
}
