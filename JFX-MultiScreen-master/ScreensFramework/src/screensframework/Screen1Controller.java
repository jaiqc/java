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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Angie
 */
public class Screen1Controller implements Initializable, ControlledScreen {

    ScreensController myController;
    Screen3Controller scoreScreen;
    Screen2Controller gameScreen;
    Screen4Controller statsScreen;
    Screen5Controller aboutScreen;
    float avgScore = 0;
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
    private void newGame(ActionEvent event) {
        gameScreen = (Screen2Controller) myController.getController(ScreensFramework.screen2ID);
        gameScreen.getInputValue.setVisible(false);
        myController.setScreen(ScreensFramework.screen2ID);
    }

    @FXML
    private void highScore(ActionEvent event) throws IOException {
        Map<String, String> jsonMap = new HashMap<String, String>();
        myController.createGameScoreSheet(false);
        StringBuilder stringBuilder = new StringBuilder();
        byte[] encoded = Files.readAllBytes(Paths.get("c:/calcGame/temp.txt"));
        String fileData = new String(encoded, StandardCharsets.UTF_8);
        jsonMap = myController.jsonPaserGet(fileData);
        for (int i = 0; i < 5; i++) {
            stringBuilder.append(Integer.toString(i + 1)).append(". ");
            stringBuilder.append((String) jsonMap.get("score" + Integer.toString(i + 1)));
            stringBuilder.append("\n");
            avgScore = avgScore + Integer.parseInt((String) jsonMap.get("score" + Integer.toString(i + 1)));
        }
        String finalString = stringBuilder.toString();
        
        scoreScreen = (Screen3Controller) myController.getController(ScreensFramework.screen3ID);
        scoreScreen.highScoreLabel.setText(finalString);
        scoreScreen.yourScore.setText("Your Last Score: " + (String) jsonMap.get("lastGameScore"));

        myController.setScreen(ScreensFramework.screen3ID);
    }

    @FXML
    private void gameStats(ActionEvent event) throws IOException {
        int add;
        int sub;
        int mul;
        int div;
        float total;
        Map<String, String> jsonMap = new HashMap<String, String>();
        myController.createGameScoreSheet(false);   
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
        statsScreen = (Screen4Controller) myController.getController(ScreensFramework.screen4ID);
        statsScreen.showStats.setText(finalString);
        myController.setScreen(ScreensFramework.screen4ID);
    }

    @FXML
    private void aboutMe(ActionEvent event) {
        aboutScreen = (Screen5Controller) myController.getController(ScreensFramework.screen5ID);
        aboutScreen.qoutes.setText("Mathematics is exercise your left brain!");
        aboutScreen.aboutMe.setText("Doing mathematics should always create your own atomic\n kitny(\"brain\") thinking power. :-p");
        aboutScreen.ownerId.setText("Quotes\n © Copyright All Rights reserved");
        aboutScreen.devId.setText("</> Jayachandran");
        myController.setScreen(ScreensFramework.screen5ID);
    }
}
