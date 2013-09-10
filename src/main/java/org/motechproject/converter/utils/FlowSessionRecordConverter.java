package org.motechproject.converter.utils;

import org.motechproject.converter.old.domain.FlowSessionRecord;

public class FlowSessionRecordConverter {

    public static FlowSessionRecord createUpdatedRecord(FlowSessionRecord oldRecord) {
        FlowSessionRecord newRecord = new FlowSessionRecord(oldRecord.getSessionId(), oldRecord.getPhoneNumber());

        // copy top-level fields

        // copy callDetailRecord
        // disposition AUTHENTICATION_FAILED -> ANSWERED

        // copy data map

        // add callID to data.callSid

        return newRecord;
    }
}
