package talentshow.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import talentshow.entities.Juriu;
import talentshow.entities.Participant;
import talentshow.service.ConfException;
import talentshow.service.IClient;
import talentshow.service.IServer;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Dana on 17-Jun-17.
 */
public class LoginViewController extends UnicastRemoteObject implements IClient, Serializable {
    private IServer service;
    private Juriu userLogat;
    private AppViewController appViewController;

    @FXML
    TextField textfieldUsername;
    @FXML
    TextField textfieldParola;
    @FXML
    Button buttonAutentificare;

    public LoginViewController() throws RemoteException {
        super();
    }

    public void setService(IServer service) {
        this.service = service;
    }


    //Alert for error
    private void showErrorMessage(String msg){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Whoops");
        message.setContentText(msg);
        message.showAndWait();
    }

    //deschiderea unei noi ferestre, loader = fisierul fxml, title = titlul ferestrei
    private void openMainPage(ActionEvent e, String title, Juriu userLogat) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mainpage.fxml"));
        Parent parent = fxmlLoader.load();
        Stage stage = new Stage();
        ((Node) (e.getSource())).getScene().getWindow().hide();
        appViewController = fxmlLoader.getController();
        appViewController.setService(service, userLogat);
        stage.setTitle(title);
        stage.setScene(new Scene(parent));
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
    }

    public void handleButtonAutentificare(ActionEvent actionEvent) {
        try {
            String username = textfieldUsername.getText();
            String password = textfieldParola.getText();
            if (username.equals("") || password.equals(""))
                throw new NullPointerException();

            Juriu user = new Juriu(username, password);
            service.login(user, this);
            Juriu fullUser = service.getOrganizator(username, password);
            userLogat = fullUser;
            openMainPage(actionEvent, "Talent Show System - " + user.getUsername(), userLogat);
            System.out.println("openMainPage");
        } catch (NullPointerException e1) {
            showErrorMessage("Empty fields");
        } catch (IOException | ConfException e1) {
            showErrorMessage("Invalid username or password.");
        }
    }

    @Override
    public void notaAdaugata(Participant[] par) throws ConfException, RemoteException {
        appViewController.notaAdaugata(par);
    }

}
