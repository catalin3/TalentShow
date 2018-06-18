import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import talentshow.controller.LoginViewController;
import talentshow.service.IServer;

/**
 * Created by Dana on 15-Jun-17.
 */
public class StartClient extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
            IServer server = (IServer) factory.getBean("service");
            System.out.println("Obtained a reference to remote server");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClassLoader.getSystemResource("loginpage.fxml"));
            BorderPane root = new BorderPane();
            root = loader.load();

            LoginViewController loginViewController = loader.getController();
            loginViewController.setService(server);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Autentificare");
            primaryStage.show();
        } catch (Exception e){
            System.err.println("Initialization  exception:"+e);
            e.printStackTrace();
        }

    }
}
