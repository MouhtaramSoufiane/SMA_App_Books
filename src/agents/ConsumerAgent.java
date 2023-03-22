package agents;

import jade.core.AID;
import jade.core.behaviours.*;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;


public class ConsumerAgent extends GuiAgent {
    protected ConsumerContainers consumerContainers;


    @Override
    protected void setup() {
        if(this.getArguments().length==1){
            consumerContainers=(ConsumerContainers) getArguments()[0];
            consumerContainers.consumerAgent=this;
        }
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
        addBehaviour(parallelBehaviour);

//        ACLMessage message = new ACLMessage(ACLMessage.CFP);
//        message.setContent("hello");
//        message.setLanguage("Fr");
//
//        message.addReceiver(new AID("consumer", AID.ISLOCALNAME));
//        send(message);
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {

               @Override

                public void action() {
                   ACLMessage messageRequest = receive();
                   if (messageRequest != null) {
                       System.out.println(messageRequest.getSender().getName() + " Message :" + messageRequest.getContent());
                       GuiEvent guiEvent=new GuiEvent(this,1);
                       guiEvent.addParameter(messageRequest.getContent());
                       consumerContainers.viewMessage(guiEvent);
                   } else
                       System.out.println("block....");
                   block();
               }
        }
        );
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        if(guiEvent.getType()==1){
            String bookName=(String) guiEvent.getParameter(0);
//            System.out.println("Agent ===>"+getAID().getName()+" "+bookName);
            ACLMessage aclMessage=new ACLMessage(ACLMessage.REQUEST);
            aclMessage.setContent(bookName);
            aclMessage.addReceiver(new AID("acheteur",AID.ISLOCALNAME));
            send(aclMessage);
        }

    }
}








