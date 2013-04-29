package org.motechproject.icappr.domain;

import java.util.ArrayList;
import java.util.List;

import org.motechproject.callflow.domain.CallDetailRecord;
import org.motechproject.callflow.domain.FlowSessionRecord;
import org.motechproject.callflow.service.FlowSessionService;
import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.decisiontree.core.model.AudioPrompt;
import org.motechproject.decisiontree.core.model.ITransition;
import org.motechproject.decisiontree.core.model.Node;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.content.SoundFiles;
import org.motechproject.icappr.events.Events;
import org.springframework.beans.factory.annotation.Autowired;

public class SideEffectTransition implements ITransition {

    public SideEffectTransition() {
        SideEffectQuestion question = new SideEffectQuestion(SoundFiles.YELLOW_SKIN, Events.YES_YELLOW_SKIN_OR_EYES, "1", "3");
        SideEffectQuestion question2 = new SideEffectQuestion(SoundFiles.ABDOMINAL_PAIN, Events.YES_ABDOMINAL_PAIN_OR_VOMITING, "1", "3");
        SideEffectQuestion question3 = new SideEffectQuestion(SoundFiles.SKIN_RASH, Events.YES_SKIN_RASH_OR_ITCHY_SKIN, "1", "3");
        SideEffectQuestion question4 = new SideEffectQuestion(SoundFiles.TINGLING_NUMBNESS, Events.TINGLING_OR_NUMBNESS_OF_HANDS_OR_FEET, "1", "3");

        sideEffects.add(question);
        sideEffects.add(question2);
        sideEffects.add(question3);
        sideEffects.add(question4);
    }

    @Autowired
    private FlowSessionService flowSessionService;

    @Autowired
    private PillReminderSettings settings;

    List<SideEffectQuestion> sideEffects = new ArrayList<SideEffectQuestion>();

    @Autowired
    public SideEffectTransition(FlowSessionService flowSessionService, PillReminderSettings settings) {
        SideEffectQuestion question = new SideEffectQuestion(SoundFiles.YELLOW_SKIN, Events.YES_YELLOW_SKIN_OR_EYES, "1", "3");
        SideEffectQuestion question2 = new SideEffectQuestion(SoundFiles.ABDOMINAL_PAIN, Events.YES_ABDOMINAL_PAIN_OR_VOMITING, "1", "3");
        SideEffectQuestion question3 = new SideEffectQuestion(SoundFiles.SKIN_RASH, Events.YES_SKIN_RASH_OR_ITCHY_SKIN, "1", "3");
        SideEffectQuestion question4 = new SideEffectQuestion(SoundFiles.TINGLING_NUMBNESS, Events.TINGLING_OR_NUMBNESS_OF_HANDS_OR_FEET, "1", "3");

        sideEffects.add(question);
        sideEffects.add(question2);
        sideEffects.add(question3);
        sideEffects.add(question4);

        this.flowSessionService = flowSessionService;
        this.settings = settings;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession session) {
        FlowSessionRecord record = (FlowSessionRecord) session;
        CallDetailRecord callDetail = record.getCallDetailRecord();

        int attempts = 0;

        String numRetryString = (String) callDetail.getCustomProperties().get("attempts");
        String currentQuestion = (String) callDetail.getCustomProperties().get("currentQuestion");
        String language = (String) callDetail.getCustomProperties().get("language");

        if (currentQuestion == null) {
            currentQuestion = sideEffects.get(0).getAudioUrl();
        }

        if (numRetryString != null) {
            attempts = Integer.parseInt(numRetryString);
        } 

        if (!("1".equals(input) || "3".equals(input))) {
            attempts++;
        } else {
            if ("1".equals(input)) {
                record.set("answeredYes", "true");
            }
            currentQuestion = getNextQuestion(currentQuestion);
            //move to next question, reset attempts
            attempts = 0;
        }

        Node returnNode = new Node();

        if (attempts == 3) {
            //call over
            String answeredYes = (String) callDetail.getCustomProperties().get("answeredYes");
            if (answeredYes == null) {
                returnNode.setPrompts(new AudioPrompt().setAudioFileUrl(getUrl(SoundFiles.SIDE_EFFECTS_SELECTED_NO, language)));
            } else {
                returnNode.setPrompts(new AudioPrompt().setAudioFileUrl(getUrl(SoundFiles.SIDE_EFFECTS_SELECTED_YES, language)));
            }
        } else {
            returnNode.setPrompts(new AudioPrompt().setAudioFileUrl(getUrl(currentQuestion, language)));
            returnNode.addTransition("?", this);
        }

        record.set("currentQuestion", currentQuestion);
        record.set("attempts", attempts);

        flowSessionService.updateSession(record);

        return returnNode;
    }
    private String getNextQuestion(String currentQuestion) {
        switch (currentQuestion) {
        case SoundFiles.YELLOW_SKIN : return SoundFiles.ABDOMINAL_PAIN;
        case SoundFiles.ABDOMINAL_PAIN : return SoundFiles.SKIN_RASH;
        case SoundFiles.SKIN_RASH : return SoundFiles.TINGLING_NUMBNESS;
        case SoundFiles.TINGLING_NUMBNESS : break;
        }
        return null;
    }

    private String getUrl(String currentQuestion, String language) {
        return settings.getCmsliteUrlFor(currentQuestion, language);
    }

}
