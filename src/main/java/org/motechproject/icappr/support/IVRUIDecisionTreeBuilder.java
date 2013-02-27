package org.motechproject.icappr.support;

import java.util.List;

import javax.annotation.PostConstruct;

import org.motechproject.decisiontree.core.DecisionTreeService;
import org.motechproject.decisiontree.core.model.AudioPrompt;
import org.motechproject.decisiontree.core.model.EventTransition;
import org.motechproject.decisiontree.core.model.Node;
import org.motechproject.decisiontree.core.model.Prompt;
import org.motechproject.decisiontree.core.model.Transition;
import org.motechproject.decisiontree.core.model.Tree;
import org.motechproject.decisiontree.server.service.FlowSessionService;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.content.SoundFiles;
import org.motechproject.icappr.events.Events;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class IVRUIDecisionTreeBuilder {
	
    private final Logger logger = LoggerFactory.getLogger(IVRUIDecisionTreeBuilder.class);

    private final DecisionTreeService decisionTreeService;
    private final PillReminderSettings settings;
    private final FlowSessionService flowSessionService;

    @Autowired
    public IVRUIDecisionTreeBuilder(DecisionTreeService decisionTreeService, PillReminderSettings settings, FlowSessionService flowSessionService) {
        this.decisionTreeService = decisionTreeService;
        this.settings = settings;
        this.flowSessionService = flowSessionService;
    }

    @PostConstruct
    public void buildTree() {
        logger.info("Creating a new ivr-ui-test decision tree");

        // spinning up a new thread so that this will not block the bundle
        // loading process
        new Thread(new Runnable() {

            @Override
            public void run() {
                deleteOldTree();
                createDecisionTree();
            }

        }).start();
    }

    private void deleteOldTree() {
        List<Tree> trees = decisionTreeService.getDecisionTrees();
        for (Tree tree : trees) {
            if ("IVRUITestTree".equals(tree.getName())) {
                decisionTreeService.deleteDecisionTree(tree.getId());
                break;
            }
        }
    }

    private void createDecisionTree() {
        Tree tree = new Tree();
        tree.setName("IVRUITestTree");
        Transition rootTransition = new Transition();

        /*The Destination Node is set in such a way that if the user selects "1," they will 
         * continue to receive calls that prompt them for their pin number, re-initiating the 
         * whole process.
         */
        rootTransition.setDestinationNode(new Node()
                .setNoticePrompts(
                        new Prompt[] { new AudioPrompt().setAudioFileUrl(settings
                                .getCmsliteUrlFor(SoundFiles.CONTINUE_PROMPTS)) }).setTransitions(
                        new Object[][] { { "1", getContinueTransition() }, { "3", getStopTransition() } }));
        tree.setRootTransition(rootTransition);

        decisionTreeService.saveDecisionTree(tree);
    }

    private Transition getStopTransition() {
        EventTransition transition = new EventTransition();
        transition.setEventSubject(Events.PATIENT_SELECTED_STOP);
        transition.setDestinationNode(new Node().setPrompts(new AudioPrompt().setAudioFileUrl(settings
                .getCmsliteUrlFor(SoundFiles.GOODBYE))));
        transition.setName("stop");
        return transition;
    }

    private Transition getContinueTransition() {
        EventTransition transition = new EventTransition();
        transition.setEventSubject(Events.PATIENT_SELECTED_CONTINUE);
        transition.setDestinationNode(new Node()
                .setNoticePrompts(
                        new Prompt[] { new AudioPrompt().setAudioFileUrl(settings
                                .getCmsliteUrlFor(SoundFiles.CONTINUE_PROMPTS)) }).setTransitions(
                        new Object[][] { { "1", getContinueTransition() }, { "3", getStopTransition() } }));
        transition.setName("continue");
        reinitiateCalls();
        return transition;
    }
    
    private void reinitiateCalls(){
    	/**Somehow we need to get the motech ID and phone number from the call request right here.*/
    	
    	//get sessionId somehow
    	String sessionId = "sessionId";
    	flowSessionService.getSession(sessionId);
    }
}
