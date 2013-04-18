package org.motechproject.icappr.openmrs;

import java.util.List;
import org.motechproject.mrs.domain.MRSAttribute;

public class OpenMRSUtil {

    public static String getAttrValue(String name, List<MRSAttribute> attrs) {
        for (MRSAttribute attr : attrs) {
            if (name.equals(attr.getName())) {
                return attr.getValue();
            }
        }

        return null;
    }
}
