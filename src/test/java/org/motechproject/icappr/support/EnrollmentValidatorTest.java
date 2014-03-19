package org.motechproject.icappr.support;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import org.motechproject.mrs.domain.MRSAttribute;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.domain.MRSPerson;
import org.motechproject.icappr.support.EnrollmentValidator;
import org.motechproject.icappr.mrs.MrsConstants;

public class EnrollmentValidatorTest {

    @Mock
    private MRSPatient patient;

    @Mock
    private MRSPerson person;

    private ArrayList<MRSAttribute> personAttributes;

    private DateTime today;
    private DateTime yesterday;
    private DateTime lastYear;

    @Before
    public void setUp() {
        today = DateTime.now();
        yesterday = today.minusDays(1);
        lastYear = today.minusYears(1);
        personAttributes = new ArrayList<MRSAttribute>();
        initMocks(this);
        when(patient.getPerson()).thenReturn(person);
        when(person.getAttributes()).thenReturn(personAttributes);
    }

    @Test
    public void shouldUpdateEnrollmentWithMissingEnrollmentDate() {
        assertTrue(EnrollmentValidator.patientCanUpdateReminderFrequency(patient, today));
    }

    @Test
    public void shouldUpdateEnrollmentIfDayEnrolledInDistantPast() {
        MRSAttribute enrollDateAttribute = new SimpleMRSAttribute();
        enrollDateAttribute.setName(MrsConstants.DAY_ENROLLED);
        enrollDateAttribute.setValue(lastYear.toString());
        personAttributes.add(enrollDateAttribute);

        assertTrue(EnrollmentValidator.patientCanUpdateReminderFrequency(patient, today));
    }

    @Test
    public void shouldNotUpdateEnrollmentIfDayEnrolledIsRecent() {
        MRSAttribute enrollDateAttribute = new SimpleMRSAttribute();
        enrollDateAttribute.setName(MrsConstants.DAY_ENROLLED);
        enrollDateAttribute.setValue(yesterday.toString());
        personAttributes.add(enrollDateAttribute);

        assertFalse(EnrollmentValidator.patientCanUpdateReminderFrequency(patient, today));
    }
    
    @Test
    public void shouldUpdateEnrollmentWithSampleDateInThePast() {
        String sampleDateString = "2013-11-05T13:01:51.541Z";
        DateTime updateDate = DateTime.now();
        
        MRSAttribute enrollDateAttribute = new SimpleMRSAttribute();
        enrollDateAttribute.setName(MrsConstants.DAY_ENROLLED);
        enrollDateAttribute.setValue(sampleDateString);
        personAttributes.add(enrollDateAttribute);

        assertTrue(EnrollmentValidator.patientCanUpdateReminderFrequency(patient, updateDate));
    }

    private class SimpleMRSAttribute implements MRSAttribute {
        private static final long serialVersionUID = 1L;
        private String name;
        private String value;

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public void setValue(String value) {
            this.value = value;
        }
    }
}
