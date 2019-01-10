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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.util.Map;

/**
 *
 * @author Angie
 */
public class ScreensController  extends StackPane {
    //Holds the screens to be displayed

    private HashMap<String, Node> screens = new HashMap<>();
    private HashMap<String, ControlledScreen> controller = new HashMap<>();
    
    public ScreensController() {
        super();
    }

    //Add the screen to the collection
    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    //Add the screen to the collection
    public void addController(String name, ControlledScreen screen) {
        controller.put(name, screen);
    }

    //Returns the Node with the appropriate name
    public Node getScreen(String name) {
        return screens.get(name);
    }
    
    //Returns the Node with the appropriate name
    public ControlledScreen getController(String name) {
        return controller.get(name);
    }

    //Loads the fxml file, add the screen to the screens collection and
    //finally injects the screenPane to the controller.
    public boolean loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen myScreenControler = ((ControlledScreen) myLoader.getController());
            myScreenControler.setScreenParent(this);
            addController(name, myScreenControler);
            addScreen(name, loadScreen);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //This method tries to displayed the screen with a predefined name.
    //First it makes sure the screen has been already loaded.  Then if there is more than
    //one screen the new screen is been added second, and then the current screen is removed.
    // If there isn't any screen being displayed, the new screen is just added to the root.
    public boolean setScreen(final String name) {       
        if (screens.get(name) != null) {   //screen loaded
            final DoubleProperty opacity = opacityProperty();

            if (!getChildren().isEmpty()) {    //if there is more than one screen
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(1000), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        getChildren().remove(0);                    //remove the displayed screen
                        getChildren().add(0, screens.get(name));     //add the screen
                        Timeline fadeIn = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
                        fadeIn.play();
                    }
                }, new KeyValue(opacity, 0.0)));
                fade.play();

            } else {
                setOpacity(0.0);
                getChildren().add(screens.get(name));       //no one else been displayed, then just show
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(1500), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            return true;
        } else {
            System.out.println("screen hasn't been loaded!!! \n");
            return false;
        }


        /*Node screenToRemove;
         if(screens.get(name) != null){   //screen loaded
         if(!getChildren().isEmpty()){    //if there is more than one screen
         getChildren().add(0, screens.get(name));     //add the screen
         screenToRemove = getChildren().get(1);
         getChildren().remove(1);                    //remove the displayed screen
         }else{
         getChildren().add(screens.get(name));       //no one else been displayed, then just show
         }
         return true;
         }else {
         System.out.println("screen hasn't been loaded!!! \n");
         return false;
         }*/
    }

    //This method will remove the screen with the given name from the collection of screens
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null) {
            System.out.println("Screen didn't exist");
            return false;
        } else {
            return true;
        }
    }
    
    public Map jsonPaserGet(String jsonString) {
        int numberOfKey = 0;
        int counter = 0;
        Map<String, String> jsonMap = new HashMap<String, String>();
        
        for( int i=0; i<jsonString.length(); i++ ) {
            if( jsonString.charAt(i) == ',' ) {
                counter++;
            } 
        }
        
        String [] arrOfStr = jsonString.split(",");
        for(int i = 0; i < counter + 1; i++) {
            String [] keyVal = ((arrOfStr[i].replace("{","")).replace("\"","").replace("}", "")).split(":");
            jsonMap.put(keyVal[0], keyVal[1]);
        }
        return jsonMap;
    }
    
    public String jsonPaserPut(Map<String,String> obj) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        int total = 0;
        for(Map.Entry m:obj.entrySet()){ 
            total += 1;
        }

        for(Map.Entry m:obj.entrySet()){ 
            stringBuilder.append("\"" + m.getKey()+"\":\""+m.getValue() + "\"");
            total -= 1;
            if(total != 0) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("}");
        String jsonString = stringBuilder.toString();
        return jsonString;
    } 
    
    public void createGameScoreSheet(Boolean clear) throws FileNotFoundException, IOException {
        FileInputStream in = null;
        Map<String, String> jsonMap = new HashMap<String, String>();
        File folder = new File("c:/calcGame");
        folder.mkdir();
        File file = new File("c:/calcGame/temp.txt");
        if(file.exists()) {
            in = new FileInputStream("c:/calcGame/temp.txt");
        }
        if (!file.exists() || (in.read() != '{') || (clear == true)) {
            file.createNewFile();
            try (FileWriter fileWrite = new FileWriter(file)) {
                jsonMap.put("score1", "0");
                jsonMap.put("score2", "0");
                jsonMap.put("score3", "0");
                jsonMap.put("score4", "0");
                jsonMap.put("score5", "0");
                jsonMap.put("lastGameScore", "0");
                jsonMap.put("addSatus", "0");
                jsonMap.put("subStatus", "0");
                jsonMap.put("mulStatus", "0");
                jsonMap.put("divStatus", "0");
                String jsonText = jsonPaserPut(jsonMap);
                fileWrite.write(jsonText);
                fileWrite.flush();
                fileWrite.close();
            }
        }       
    }
}

