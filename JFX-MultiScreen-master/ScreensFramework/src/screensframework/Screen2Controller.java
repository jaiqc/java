/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


/**
 * FXML Controller class
 *
 * @author Angie
 */

public class Screen2Controller implements Initializable , ControlledScreen {

    @FXML
    public Label gameTime;
    
    @FXML
    public Label gameScore;
    
    @FXML
    public Label showValue;
    
    @FXML
    public Label passStatus;
    
    @FXML
    public TextField getInputValue;
            
    ScreensController myController;
    Screen3Controller scoreScreen;
    Boolean alreadyStart = false;
            
    private TimerTask timerTask = null;
    MyTimerTask counterTask = new MyTimerTask(this);
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }
    
    public void mainScreenUpdate() {
       myController.setScreen(ScreensFramework.screen1ID);
    }

    public void scoreScreenUpdate() throws IOException {
        Map<String, String> jsonMap = new HashMap<String, String>();
        StringBuilder stringBuilder = new StringBuilder();
        byte[] encoded = Files.readAllBytes(Paths.get("c:/calcGame/temp.txt"));
        String fileData = new String(encoded, StandardCharsets.UTF_8);
        jsonMap = myController.jsonPaserGet(fileData);
        
        for(int i = 0; i < 5; i++) {
            stringBuilder.append(Integer.toString(i+1)).append(". ");
            stringBuilder.append((String) jsonMap.get("score"+Integer.toString(i+1)));
            stringBuilder.append("\n"); 
        }
        String finalString = stringBuilder.toString();
        
        scoreScreen = (Screen3Controller) myController.getController(ScreensFramework.screen3ID);
        scoreScreen.highScoreLabel.setText(finalString);
        scoreScreen.yourScore.setText("Your Last Score: "+(String) jsonMap.get("lastGameScore"));
        myController.setScreen(ScreensFramework.screen3ID); 
    }

    @FXML
    private void mainScreen(ActionEvent event){
        mainScreenUpdate();
        stopCount();
        gameTime.setText("");
        gameScore.setText("");
        showValue.setText("");
        showValue.setVisible(false);
        getInputValue.setVisible(false);
        passStatus.setText("");
    }
    
    public void startCount() {
        if(alreadyStart == false) {
            counterTask.setCountValue(0);
            timerTask = new MyTimerTask(this);
            //running timer task as daemon thread
            Timer timer = new Timer(true);
            timer.scheduleAtFixedRate(timerTask, 0, 60);
            alreadyStart = true;
        }
    }
    
    public void stopCount() {
        alreadyStart = false;
        if (timerTask != null) {
            timerTask.cancel();
        }
    }
    
    @FXML
    private void gameStart(ActionEvent event){
        startCount();
        getInputValue.setVisible(true);
        showValue.setVisible(true);
        passStatus.setText("");
    }
    
    @FXML
    private void gameStop(ActionEvent event){
        stopCount();
        gameTime.setText("");
        gameScore.setText("");
        showValue.setText("");
        getInputValue.setVisible(false);
        passStatus.setText("");
    }
    
}
