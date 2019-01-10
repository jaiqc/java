/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
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
public class Screen4Controller implements Initializable, ControlledScreen {

    ScreensController myController;
    
    @FXML
    public Label showStats;
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
    
    @FXML
    private void clearStats(ActionEvent event) throws IOException{
        int add;
        int sub;
        int mul;
        int div;
        float total;
        float avgScore = 0;
        Map<String, String> jsonMap = new HashMap<String, String>();
        myController.createGameScoreSheet(true);   
        StringBuilder stringBuilder = new StringBuilder();
        byte[] encoded = Files.readAllBytes(Paths.get("c:/calcGame/temp.txt"));
        String fileData = new String(encoded, StandardCharsets.UTF_8);
        jsonMap = myController.jsonPaserGet(fileData);

        for (int i = 0; i < 5; i++) {
            avgScore = avgScore + Integer.parseInt((String) jsonMap.get("score" + Integer.toString(i + 1)));
        }
        
        add = Integer.parseInt((String) jsonMap.get("addSatus"));
        sub = Integer.parseInt((String) jsonMap.get("subStatus"));
        mul = Integer.parseInt((String) jsonMap.get("mulStatus"));
        div = Integer.parseInt((String) jsonMap.get("divStatus"));
        
        total = ((float)(add+sub+mul+div)/400)*100;

        stringBuilder.append("Correct response rate: ").append(Float.toString(total)).append("%\n\n");
        stringBuilder.append("  1. x Multiplication: ").append(Integer.toString(mul)).append("%\n");
        stringBuilder.append("  2. / Division: ").append(Integer.toString(div)).append("%\n");
        stringBuilder.append("  3. + Addition: ").append(Integer.toString(add)).append("%\n");
        stringBuilder.append("  4. - Subtraction: ").append(Integer.toString(sub)).append("%\n");
        stringBuilder.append("\n  Avarage score: ").append(Integer.toString((int)((avgScore/500)*100))).append("\n");
        /*
        Correct response rate: 95%
            1. x Multiplication: 100%
            2. / Division: 100%
            3. + Addition: 100%
            4. - Subtraction: 100:
            
            Avarage score: 1000
        
        Most failed Questions:
        
        */
        String finalString = stringBuilder.toString();
        showStats.setText(finalString);
        
    }
}
