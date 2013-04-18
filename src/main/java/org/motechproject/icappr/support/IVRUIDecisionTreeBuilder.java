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
import org.motechproject.icappr.events.Events;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IVRUIDecisionTreeBuilder {

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    private final DecisionTreeService decisionTreeService;
    private final PillReminderSettings settings;

    @Autowired
    public IVRUIDecisionTreeBuilder(DecisionTreeService decisionTreeService, PillReminderSettings settings) {
        this.decisionTreeService = decisionTreeService;
        this.settings = settings;
    }

    public void buildTree() {
        logger.info("Creating ivr-ui-test decision trees");
        List<String> languages = new ArrayList<String>();
        languages.add("english");
        languages.add("amharic");
        languages.add("harari");
        languages.add("oromiffa");
        languages.add("somali");
        if (languages != null ) {
            for (String language : languages) {
                deleteOldTree(language);
                createDecisionTree(language);
            }
        }
    }

    private void deleteOldTree(String language) {
        List<Tree> trees = decisionTreeService.getDecisionTrees();
        for (Tree tree : trees) {
            if (("IVRUITree" + language).equals(tree.getName())) {
                decisionTreeService.deleteDecisionTree(tree.getId());
                break;
            }
        }
    }

    private void createDecisionTree(String language) {
        Tree tree = new Tree();
        tree.setName("IVRUITree" + language);
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
}
