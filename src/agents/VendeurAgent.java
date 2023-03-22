package agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import javafx.application.Platform;


public class VendeurAgent extends GuiAgent {
    protected VendeurContainers vendeurContainers;


    @Override
    protected void setup() {
        if(this.getArguments().length==1){
            vendeurContainers=(VendeurContainers) getArguments()[0];
            vendeurContainers.vendeurAgent=this;
        }
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
        addBehaviour(parallelBehaviour);
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
               @Override
                public void action() {
                   ACLMessage aclMessage=receive();
                   if(aclMessage!=null){
                       switch (aclMessage.getPerformative()){
                           case(ACLMessage.CFP):
                               GuiEvent guiEvent=new GuiEvent(this,1);
                               guiEvent.addParameter(aclMessage.getContent());
                               Platform.runLater(new Runnable() {
                                   @Override
                                   public void run() {
                                       vendeurContainers.viewMessage(guiEvent);
                                   }
                               });


                               break;
                           case(ACLMessage.ACCEPT_PROPOSAL):

                               break;
                       }
                       }
                   }


        }
        );
        parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                DFAgentDescription dfA=new DFAgentDescription();
                dfA.setName(getAID());
                ServiceDescription sD=new ServiceDescription();
                sD.setName("Vente-Livres");
                sD.setType("Vente");
                dfA.addServices(sD);
                try {
                    DFService.register(myAgent,dfA);
                } catch (FIPAException e) {
                    throw new RuntimeException(e);
                }


            }
        });
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        if(guiEvent.getType()==1){
            String bookName=(String) guiEvent.getParameter(0);
            ACLMessage aclMessage=new ACLMessage(ACLMessage.REQUEST);
            aclMessage.setContent(bookName);
            aclMessage.addReceiver(new AID("acheteur",AID.ISLOCALNAME));
            send(aclMessage);
        }

    }
}








