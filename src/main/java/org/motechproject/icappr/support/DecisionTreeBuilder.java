package org.motechproject.icappr.support;

import java.util.ArrayList;
import java.util.List;
import org.motechproject.decisiontree.core.DecisionTreeService;
import org.motechproject.decisiontree.core.model.Action;
import org.motechproject.decisiontree.core.model.AudioPrompt;
import org.motechproject.decisiontree.core.model.Node;
import org.motechproject.decisiontree.core.model.Prompt;
import org.motechproject.decisiontree.core.model.Transition;
import org.motechproject.decisiontree.core.model.Tree;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.content.SoundFiles;
import org.motechproject.icappr.domain.SideEffectQuestion;
import org.motechproject.icappr.events.Events;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DecisionTreeBuilder {

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    private final DecisionTreeService decisionTreeService;
    private final PillReminderSettings settings;
    private List<String> languages = new ArrayList<String>();
    private List<SideEffectQuestion> sideEffects = new ArrayList<SideEffectQuestion>();

    @Autowired
    public DecisionTreeBuilder(DecisionTreeService decisionTreeService, PillReminderSettings settings) {
        this.decisionTreeService = decisionTreeService;
        this.settings = settings;
        languages.add("english");
        languages.add("amharic");
        languages.add("harari");
        languages.add("oromiffa");
        languages.add("somali");

        SideEffectQuestion question = new SideEffectQuestion(SoundFiles.YELLOW_SKIN, Events.YES_YELLOW_SKIN_OR_EYES, "1", "3");
        SideEffectQuestion question2 = new SideEffectQuestion(SoundFiles.ABDOMINAL_PAIN, Events.YES_ABDOMINAL_PAIN_OR_VOMITING, "1", "3");
        SideEffectQuestion question3 = new SideEffectQuestion(SoundFiles.SKIN_RASH, Events.YES_SKIN_RASH_OR_ITCHY_SKIN, "1", "3");
        SideEffectQuestion question4 = new SideEffectQuestion(SoundFiles.TINGLING_NUMBNESS, Events.TINGLING_OR_NUMBNESS_OF_HANDS_OR_FEET, "1", "3");

        sideEffects.add(question);
        sideEffects.add(question2);
        sideEffects.add(question3);
        sideEffects.add(question4);
    }

    public void buildTree() {
        logger.info("Creating ivr-ui-test decision trees");
        for (String language : languages) {
            deleteOldTree("IVRUITree", language);
            createIvrUIDecisionTree("IVRUITree", language);
        }
    }

    public void buildSideEffectTree() {
        for (String language : languages) {
            deleteOldTree("SideEffectTree", language);
            createSideEffectCallTree("SideEffectTree", language);
        }
    }

    public void buildPillReminderTree() {
        logger.info("Creating pill reminder decision trees");
        for (String language : languages) {
            deleteOldTree("PillReminderTree", language);
            createPillReminderCampaignTree("PillReminderTree", language);
        }
    }

    private void deleteOldTree(String treeName, String language) {
        List<Tree> trees = decisionTreeService.getDecisionTrees();
        for (Tree tree : trees) {
            if ((treeName + language).equals(tree.getName())) {
                decisionTreeService.deleteDecisionTree(tree.getId());
                break;
            }
        }
    }

    private void createSideEffectCallTree(String treeName, String language) {
        Tree tree = new Tree();
        tree.setName(treeName + language);
        Transition rootTransition = new Transition();

        rootTransition.setDestinationNode(new Node()
        .setPrompts(
                new Prompt[] { new AudioPrompt().setAudioFileUrl(settings
                        .getCmsliteUrlFor(SoundFiles.CONTINUE_PROMPTS, language)) }).setTransitions(
                                new Object[][] { { "1", getSideEffectTransition(true, language, 0, false) }, { "3", getSideEffectTransition(false, language, 0, false) } }));
        tree.setRootTransition(rootTransition);

        decisionTreeService.saveDecisionTree(tree);
    }

    private Node nextSideEffectNode(int level, boolean yesOrNo, String language, boolean hasSelectedYes) {
        Node node = new Node();
        AudioPrompt prompt = new AudioPrompt();
        node.setPrompts(prompt);
        if (level != sideEffects.size()) {
            SideEffectQuestion sideEffectQuestion = sideEffects.get(level);
            prompt.setAudioFileUrl(settings.getCmsliteUrlFor(sideEffectQuestion.getAudioUrl(), language));
            node.setTransitions(new Object[][] { {sideEffectQuestion.getAffirmativeKeyPress(), getSideEffectTransition(true, language, level, hasSelectedYes) }, {sideEffectQuestion.getNegativeKeyPress(), getSideEffectTransition(false, language, level, hasSelectedYes) } });
        } else {
            if (hasSelectedYes) {
                prompt.setAudioFileUrl("SELECTED YES AT SOME POINT");
            } else {
                prompt.setAudioFileUrl("NEVER SELECTED YES");
            }
        }
        
        return node;
    }

    private Transition getSideEffectTransition(boolean response, String language, int level, boolean hasSelectedYes) {
        Transition transition = new Transition();
        Action action1 = new Action();
        SideEffectQuestion sideEffectQuestion = sideEffects.get(level);
        if (response) {
            action1.setEventId(sideEffectQuestion.getConceptName());
        }
        transition.setActions(action1);

        if (response) {
            hasSelectedYes = true;
        }
        transition.setDestinationNode(nextSideEffectNode(level + 1, response, language, hasSelectedYes));
        transition.setName("continue");
        return transition;
    }

    private void createPillReminderCampaignTree(String treeName, String language) {
        Tree tree = new Tree();
        tree.setName(treeName + language);
        Transition rootTransition = new Transition();

        rootTransition.setDestinationNode(new Node()
        .setPrompts(
                new Prompt[] { new AudioPrompt().setAudioFileUrl(settings
                        .getCmsliteUrlFor(SoundFiles.CONTINUE_PROMPTS, language)) }).setTransitions(
                                new Object[][] { { "1", getPillReminderContinueTransition(language) }, { "3", getPillReminderStopTransition(language) } }));
        tree.setRootTransition(rootTransition);

        decisionTreeService.saveDecisionTree(tree);
    }

    private void createIvrUIDecisionTree(String treeName, String language) {
        Tree tree = new Tree();
        tree.setName(treeName + language);
        Transition rootTransition = new Transition();

        /*
         * The Destination Node is set in such a way that if the user selects
         * "1," they will continue to receive calls that prompt them for their
         * pin number, re-initiating the whole process.
         */

        rootTransition.setDestinationNode(new Node()
        .setPrompts(
                new Prompt[] { new AudioPrompt().setAudioFileUrl(settings
                        .getCmsliteUrlFor(SoundFiles.CONTINUE_PROMPTS, language)) }).setTransitions(
                                new Object[][] { { "1", getContinueTransition(language) }, { "3", getStopTransition(language) } }));
        tree.setRootTransition(rootTransition);

        decisionTreeService.saveDecisionTree(tree);
    }

    private Transition getStopTransition(String language) {
        Transition transition = new Transition();
        Action action1 = new Action();
        action1.setEventId(Events.PATIENT_SELECTED_STOP);
        transition.setActions(action1);
        transition.setDestinationNode(new Node().setPrompts(new AudioPrompt().setAudioFileUrl(settings
                .getCmsliteUrlFor(SoundFiles.GOODBYE, language))));
        transition.setName("stop");
        return transition;
    }

    private Transition getContinueTransition(String language) {
        Transition transition = new Transition();
        Action action1 = new Action();
        action1.setEventId(Events.PATIENT_SELECTED_CONTINUE);
        transition.setActions(action1);
        transition.setDestinationNode(new Node().setNoticePrompts(new Prompt[] { new AudioPrompt()
        .setAudioFileUrl(settings.getCmsliteUrlFor(SoundFiles.CONTINUE_PROMPTS, language)) }));
        transition.setName("continue");
        return transition;
    }

    private Transition getPillReminderStopTransition(String language) {
        Transition transition = new Transition();
        Action action1 = new Action();
        action1.setEventId(Events.PATIENT_SELECTED_END_PILL_REMINDER_CALL);
        transition.setActions(action1);
        transition.setDestinationNode(new Node().setPrompts(new AudioPrompt().setAudioFileUrl(settings
                .getCmsliteUrlFor(SoundFiles.GOODBYE, language))));
        transition.setName("stop");
        return transition;
    }

    private Transition getPillReminderContinueTransition(String language) {
        Transition transition = new Transition();
        Action action1 = new Action();
        action1.setEventId(Events.PATIENT_WANTS_CLINIC_CALL);
        transition.setActions(action1);
        transition.setDestinationNode(new Node().setNoticePrompts(new Prompt[] { new AudioPrompt()
        .setAudioFileUrl(settings.getCmsliteUrlFor(SoundFiles.CONTINUE_PROMPTS, language)) }));
        transition.setName("continue");
        return transition;
    }
}
