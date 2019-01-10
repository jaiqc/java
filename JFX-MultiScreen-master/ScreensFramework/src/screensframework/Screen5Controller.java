/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author I50992
 */
public class Screen5Controller implements Initializable, ControlledScreen {

    ScreensController myController;
    
    @FXML
    public Label aboutMe;

    @FXML
    public Label ownerId;

    @FXML
    public Label qoutes;
    
    @FXML
    public Label devId;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    } 
    
    @FXML
    private void mainScreen(ActionEvent event){
        myController.setScreen(ScreensFramework.screen1ID);
    }
    
    
}
