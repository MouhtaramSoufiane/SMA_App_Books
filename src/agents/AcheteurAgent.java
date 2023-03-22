package agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class AcheteurAgent extends GuiAgent {
    protected AcheteurContainers acheteurContainers;
    AID[] listVendeurs;


    @Override
    protected void setup() {
        if(this.getArguments().length==1){
            acheteurContainers=(AcheteurContainers) getArguments()[0];
            acheteurContainers.acheteurAgent=this;
        }
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
        addBehaviour(parallelBehaviour);

//        ACLMessage message = new ACLMessage(ACLMessage.CFP);
//        message.setContent("hello");
//        message.setLanguage("Fr");
//
//        message.addReceiver(new AID("consumer", AID.ISLOCALNAME));

//        send(message);
        parallelBehaviour.addSubBehaviour(new TickerBehaviour(this,6000) {
            @Override
            protected void onTick() {
                try {
                    DFAgentDescription dfAgentDescription=new DFAgentDescription();
                    ServiceDescription serviceDescription=new ServiceDescription();
                    serviceDescription.setName("Vente-Livres");
                    serviceDescription.setType("Vente");
                    dfAgentDescription.addServices(serviceDescription);

                    DFAgentDescription[] resultSearch=DFService.search(myAgent,dfAgentDescription);
                    listVendeurs=new AID[resultSearch.length];
                    for(int i=0;i<listVendeurs.length;i++){
                        listVendeurs[i]=resultSearch[i].getName();
                    }
                } catch (FIPAException e) {
                    throw new RuntimeException(e);
                }


            }
        });
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {

               @Override

                public void action() {
                   MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                   ACLMessage messageRequest = receive(messageTemplate);
                   if (messageRequest != null) {
                       System.out.println(messageRequest.getSender().getName() + " Message :" + messageRequest.getContent());
                       GuiEvent guiEvent = new GuiEvent(this, 1);
                       guiEvent.addParameter(messageRequest.getContent());
                       acheteurContainers.viewMessage(guiEvent);

                       //Operation de l achat
                       ACLMessage aclMessage = new ACLMessage(ACLMessage.CFP);
                       String namebook = messageRequest.getContent();
                       aclMessage.setContent(namebook);
                       for (AID aid : listVendeurs) {
                           aclMessage.addReceiver(aid);

                       }
                       send(aclMessage);
                   }
               }
        }
        );
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        if(guiEvent.getType()==1){
            String bookName=(String) guiEvent.getParameter(0);
            ACLMessage aclMessage=new ACLMessage(ACLMessage.REQUEST);
            aclMessage.setContent(bookName);
            aclMessage.addReceiver(new AID("vendeur",AID.ISLOCALNAME));
            send(aclMessage);
        }

    }
}








