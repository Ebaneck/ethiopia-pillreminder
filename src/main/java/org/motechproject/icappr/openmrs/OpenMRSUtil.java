package org.motechproject.icappr.openmrs;

import java.util.List;

import org.motechproject.mrs.domain.Attribute;

public class OpenMRSUtil {

    public static String getAttrValue(String name, List<Attribute> attrs) {
        for (Attribute attr : attrs) {
            if (name.equals(attr.getName())) {
                return attr.getValue();
            }
        }

        return null;
    }
}
