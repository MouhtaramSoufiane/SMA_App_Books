package agents;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class VendeurContainers extends Application {
    protected VendeurAgent vendeurAgent;
    ObservableList<String> observableList=FXCollections.observableArrayList();
    ListView<String> listView=new ListView<>(observableList);
    AgentContainer container;

    public static void main(String[] args) throws ControllerException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StartContainer();
        primaryStage.setTitle("Vendeur Container");
        BorderPane borderPane=new BorderPane();
        HBox hBox=new HBox();
        HBox hBox1=new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);
        hBox1.setPadding(new Insets(10));
        hBox1.setSpacing(10);

        hBox1.getChildren().add(listView);
        Label labelVendeur=new Label("Vendeur Name");
        TextField textFieldVendeur=new TextField();
        Button buttonOk=new Button("Deployer l agent");
        hBox.getChildren().addAll(labelVendeur,textFieldVendeur,buttonOk);
        borderPane.setTop(hBox);
        borderPane.setCenter(hBox1);

        Scene scene=new Scene(borderPane,600,400);
        primaryStage.setScene(scene);
        primaryStage.show();
        buttonOk.setOnAction(evt->{
            String VendeurName=textFieldVendeur.getText();
            try {
                AgentController agentController=container.createNewAgent(VendeurName,"agents.VendeurAgent",new Object[]{this});
                 agentController.start();
            } catch (StaleProxyException e) {
                throw new RuntimeException(e);
            }


        });
    }
    public void StartContainer() throws Exception {
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        container = runtime.createAgentContainer(profile);

    }
    public void viewMessage(GuiEvent guiEvent){
        String message=guiEvent.getParameter(0).toString();
        observableList.add(message);

    }
}
