package talentshow.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dana on 15-Jun-17.
 */
public class AppViewController extends UnicastRemoteObject implements IClient, Serializable {
    private IServer service;
    private Juriu userlogat;

    //main page components
    @FXML
    TableView<Participant> tableview;
    @FXML
    TableColumn<Participant, String> columnNume;
    @FXML
    TableColumn<Participant, String> columnStatus;
    @FXML
    TextField textfieldNota;
    @FXML
    Button buttonNota;

    private ObservableList<Participant> model;

    public AppViewController() throws RemoteException {
        super();
    }

    @FXML
    public void initialize(){
        initTable();
    }

    public void setService(IServer service, Juriu organizator) {
        this.service = service;
        this.userlogat = organizator;
        loadTable();
    }

    //Alert for error
    private void showErrorMessage(String msg){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Whoops");
        message.setContentText(msg);
        message.showAndWait();
    }

    //Alert for succes
    private static void showMessage(Alert.AlertType type){
        Alert message = new Alert(type);
        message.setHeaderText("Succes");
        message.setContentText("Nota acordata cu succes");
        message.showAndWait();
    }

    //deschiderea unei noi ferestre, loader = fisierul fxml, title = titlul ferestrei
    private void openLoginPage(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/loginpage.fxml"));
        Parent parent = fxmlLoader.load();
        Stage stage = new Stage();
        ((Node) (e.getSource())).getScene().getWindow().hide();
        LoginViewController loginViewController = fxmlLoader.getController();
        loginViewController.setService(service);
        stage.setTitle("Autentificare");
        stage.setScene(new Scene(parent));
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
    }

    public void handleButtonLogout(ActionEvent actionEvent) {
        try {
            service.logout(userlogat);
            //deschide LOGIN PAGE
            openLoginPage(actionEvent);
        } catch (ConfException e1) {
            showErrorMessage("Logout Error.");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void initTable(){
        System.out.println("Aici intra ");
        columnNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadTable(){
        try {
            this.model = FXCollections.observableArrayList(service.getParticipanti());
            tableview.setItems(model);
            tableview.getSelectionModel().selectFirst();
        } catch (ConfException e) {
            e.printStackTrace();
        }
    }

    public void handleButtonNota() throws ConfException {

        try {
            if (textfieldNota.getText().equals("")){
                throw  new ConfException("Introduceti nota.");
            }
            if (Integer.parseInt(textfieldNota.getText())<=0 || Integer.parseInt(textfieldNota.getText())>10){
                throw  new ConfException("Introduceti o nota valida.");
            }
            if (!tableview.getSelectionModel().getSelectedItem().getStatus().equals("NO RESULTS") && !tableview.getSelectionModel().getSelectedItem().getStatus().equals("PENDING")) {
                throw new ConfException("Nu se poate da nota.");
            } else {
                int nota = Integer.parseInt(textfieldNota.getText());
                int idP = tableview.getSelectionModel().getSelectedItem().getId();
                if (service.verificaNota(userlogat.getId(), idP)){
                    showErrorMessage("Deja ati dat nota participantului");
                } else{
                    service.adaugaNota(userlogat.getId(), idP, nota);
                    //notaAdaugata(service.getParticipanti());
                    showMessage(Alert.AlertType.INFORMATION);
                }
            }
        } catch (ConfException e){
            showErrorMessage(e.getMessage());
        } catch (RemoteException e) {
            showErrorMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public  void notaAdaugata(Participant[] par) throws ConfException {
        System.out.println("Refresh participanti");
        List<Participant> p=new ArrayList<>();
        Collections.addAll(p, par);
        this.model= FXCollections.observableArrayList(p);
        this.tableview.setItems(model);
    }

}
