package agents;

import agents.AcheteurAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AcheteurContainers extends Application {
    protected AcheteurAgent acheteurAgent;
    ObservableList<String> observableList=FXCollections.observableArrayList();
    ListView<String> listView=new ListView<>(observableList);

    public static void main(String[] args) throws ControllerException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StartContainer();
        primaryStage.setTitle("Consumer Container");
        GridPane gridPane=new GridPane();
        BorderPane borderPane=new BorderPane();
        VBox vBox=new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        gridPane.add(listView,0,0);
        vBox.getChildren().add(gridPane);

        borderPane.setCenter(vBox);
        Scene scene=new Scene(borderPane,600,400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Acheteur Container");
        primaryStage.show();

    }
    public void StartContainer() throws Exception {
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        AgentContainer container = runtime.createAgentContainer(profile);
        //AgentController agentController=container.createNewAgent("consumer","agents.Server",new Object[]{});
        AgentController agentController=container.createNewAgent("acheteur","agents.AcheteurAgent",new Object[]{this});

        //agentController.start();
        agentController.start();

    }
    public void viewMessage(GuiEvent guiEvent){
        String message=guiEvent.getParameter(0).toString();
        observableList.add(message);

    }
}
