package org.motechproject.icappr.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.motechproject.cmslite.api.model.CMSLiteException;
import org.motechproject.cmslite.api.model.StreamContent;
import org.motechproject.cmslite.api.service.CMSLiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AudioContentUploadController {

    @Autowired
    private CMSLiteService cmsliteService;

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    @RequestMapping("/loadContent")
    @ResponseBody
    public String loadContent() {
        String userHome = (String) System.getProperties().get("user.home");
        String directory = userHome + "\\.motech\\content";

        File file = new File(directory);

        // Reading directory contents
        File[] files = file.listFiles();

        for (int i = 0; i < files.length; i++) {
            upLoadDirectory(files[i]);

        }

        return (String) System.getProperties().get("user.home");
    }

    private void upLoadDirectory(File languageDirectory) {
        // TODO Auto-generated method stub
        // Reading directory contents
        File[] files = languageDirectory.listFiles();
        String language = languageDirectory.getName().toLowerCase();
        for (int i = 0; i < files.length; i++) {
            upLoadAudio(files[i], language);

        }

    }

    private void upLoadAudio(File file, String language) {
        InputStream inputStreamToResource1 = null;
        try {
            inputStreamToResource1 = new FileInputStream(file);
        } catch (FileNotFoundException e) {
        }
        if (inputStreamToResource1 != null) {
            String soundFilename = file.getName();
            soundFilename = soundFilename.substring(0, soundFilename.length()-4);
            StreamContent cron = new StreamContent(language, soundFilename,
                    inputStreamToResource1, "checksum1", "audio/wav");
            try {
                logger.debug("Loading content with language " + cron.getLanguage() + " and file name " + soundFilename );
                cmsliteService.addContent(cron);
            } catch (CMSLiteException e) {

            }
        }

    }

}
