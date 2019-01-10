/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework;

/**
 *
 * @author X00323
 */
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import javafx.application.Platform;
import java.util.Random;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyTimerTask extends TimerTask {

    private int value1 = 0;
    private int value2 = 0;
    public int gCount = 0;
    static public int operatorState = 0;
    static public boolean nextGetValueState = false;
    static public boolean resultStates = false;
    static public int gameScore = 0;
    static public int addPass = 0;
    static public int addFail = 0;
    static public int mulPass = 0;
    static public int mulFail = 0;
    static public int divPass = 0;
    static public int divFail = 0;
    static public int subPass = 0;
    static public int subFail = 0;

    Screen2Controller ui;
    Screen3Controller screenScore;
    ScreensController myController = new ScreensController();

    Random random = new Random();

    public MyTimerTask(Screen2Controller ui) {
        this.ui = ui;
        nextGetValueState = false;
    }

    public MyTimerTask(Screen3Controller ui) {
        this.screenScore = ui;
    }

    @Override
    public void run() {
        if (nextGetValueState == false) {
            nextGetValueState = true;
            resultStates = false;
            operatorState = 0; // Clear the operator postions
            updateValue();
            updateScoreUI();
        }
        getUserInputUI();
        gCount = gCount + 1;
        if (gCount >= 62) {
            gCount = 0;
            updateTimesUpUI();
            try {
                stopCountVal();
            } catch (IOException ex) {
                Logger.getLogger(MyTimerTask.class.getName()).log(Level.SEVERE, null, ex);
            }
            waitCount();
            clearTimesUpUI();
            clearScoreUI();
            clearCountUI();
        }

        if (gCount != 0) {
            updateTimeUI();
        }
        waitCount();
    }

    private void updateTimeUI() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ui.gameTime.setText(Integer.toString(61 - gCount));
            }
        });

    }

    private void updateScoreUI() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ui.gameScore.setText(Integer.toString(gameScore));
            }
        });
    }

    private void updateResultUI() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (resultStates == true) {
                    ui.passStatus.setText("PASS +50");
                    gameScore = gameScore + 50;
                } else {
                    ui.passStatus.setText("FAIL -15");
                    gameScore = gameScore - 15;
                    if (gameScore <= 0) {
                        gameScore = 0;
                    }
                }
            }
        });
    }

    private void updateTimesUpUI() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ui.passStatus.setText("Time's Up");
            }
        });
    }

    private void clearTimesUpUI() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ui.passStatus.setText("");
            }
        });
    }

    private void clearScoreUI() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ui.gameScore.setText("");
            }
        });
    }

    private void clearCountUI() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ui.gameTime.setText("");
            }
        });
    }

    private void updateValueUI() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if ((operatorState & 0x8) == 0x8) {
                    ui.showValue.setText(Integer.toString(value1) + "/" + Integer.toString(value2) + "=");
                } else if (((operatorState & 0x4) == 0x4)) {
                    ui.showValue.setText(Integer.toString(value1) + "x" + Integer.toString(value2) + "=");
                } else if (((operatorState & 0x2) == 0x2)) {
                    ui.showValue.setText(Integer.toString(value2) + "-" + Integer.toString(value1) + "=");
                } else if (((operatorState & 0x1) == 0x1)) {
                    ui.showValue.setText(Integer.toString(value1) + "+" + Integer.toString(value2) + "=");
                }
            }
        });
    }

    private void updateValueClearUI() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ui.showValue.setText("");
                ui.showValue.setVisible(false);
            }
        });
    }

    private void updateValue() {

        value1 = random.nextInt(100);
        value2 = random.nextInt(100);

        if (((value1 & 1) == 0) && ((value2 & 1) == 0) || ((value1 & 1) != 0) && ((value2 & 1) != 0)) {
            value1 = random.nextInt(100 / 2) * 2;
            value2 = random.nextInt(10 / 2) * 2;
            if (value1 > value2) {
                if (value2 == 0) { // Avoid Div by Zero
                    value2 = 2;
                }
                if ((value1 % value2) == 0) {
                    operatorState = operatorState | 0x8; //Div
                    updateValueUI();
                } else {
                    operatorState = operatorState | 0x4; //Mul
                    updateValueUI();
                }
            } else {
                operatorState = operatorState | 0x4; //Mul
                updateValueUI();
            }
        } else if (value1 > value2) {
            operatorState = operatorState | 0x1; //Sub
            updateValueUI();
        } else {
            operatorState = operatorState | 0x2; //Add
            updateValueUI();
        }
    }

    public void getUserInputUI() {
        ui.getInputValue.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    String text = ui.getInputValue.getText();
                    if ((text.matches("^\\d*$") == true) && (text.equals("") == false)) {
                        if ((operatorState & 0x8) == 0x8) {
                            if ((value1 / value2) == Integer.parseInt(text)) {
                                resultStates = true;
                                divPass = divPass + 1;
                            } else {
                                divFail = divFail + 1;
                            }
                        } else if (((operatorState & 0x4) == 0x4)) {
                            if ((value1 * value2) == Integer.parseInt(text)) {
                                resultStates = true;
                                mulPass = mulPass + 1;
                            } else {
                                mulFail = mulFail + 1;
                            }
                        } else if (((operatorState & 0x2) == 0x2)) {
                            if ((value2 - value1) == Integer.parseInt(text)) {
                                resultStates = true;
                                subPass = subPass + 1;
                            } else {
                                subFail = subFail + 1;
                            }
                        } else if (((operatorState & 0x1) == 0x1)) {
                            if ((value1 + value2) == Integer.parseInt(text)) {
                                resultStates = true;
                                addPass = addPass + 1;
                            } else {
                                addFail = addFail + 1;
                            }
                        }
                    }
                    updateResultUI();
                    ui.getInputValue.setText("");
                    nextGetValueState = false;
                }
            }
        });
    }

    public void setCountValue(int value) {
        gCount = value;
    }

    private void shortingScore() {

    }

    private void computeGameStauts() {
        //Game Score
        Map<String, String> jsonMap = new HashMap<String, String>();

        try {
            myController.createGameScoreSheet(false);  
            byte[] encoded = Files.readAllBytes(Paths.get("c:/calcGame/temp.txt"));
            String fileData = new String(encoded, StandardCharsets.UTF_8);
            jsonMap = myController.jsonPaserGet(fileData);

            int[] myArray = new int[7];
            for (int i = 0; i < 5; i++) {
                myArray[i] = Integer.parseInt((String) jsonMap.get("score" + Integer.toString(i + 1)));
            }

            jsonMap.clear();
            myArray[6] = gameScore;
            myArray = Arrays.stream(myArray).sorted().toArray();

            File file = new File("c:/calcGame/temp.txt");
            try (FileWriter filewrite = new FileWriter(file)) {
      
                for (int i = 0; i < 5; i++) {
                    jsonMap.put("score" + Integer.toString(i + 1), Integer.toString(myArray[6 - i]));
                }

                jsonMap.put("lastGameScore", Integer.toString(gameScore));
                if ((addPass + addFail) != 0) {
                    jsonMap.put("addSatus", Integer.toString((addPass / (addPass + addFail)) * 100));
                } else {
                    jsonMap.put("addSatus", Integer.toString(0));
                }
                if ((subPass + subFail) != 0) {
                    jsonMap.put("subStatus", Integer.toString((subPass / (subPass + subFail)) * 100));
                } else {
                    jsonMap.put("subStatus", Integer.toString(0));
                }
                if ((mulPass + mulFail) != 0) {
                    jsonMap.put("mulStatus", Integer.toString((mulPass / (mulPass + mulFail)) * 100));
                } else {
                    jsonMap.put("mulStatus", Integer.toString(0));
                }
                if ((divPass + divPass) != 0) {
                    jsonMap.put("divStatus", Integer.toString((divPass / (divPass + divFail)) * 100));
                } else {
                    jsonMap.put("divStatus", Integer.toString(0));
                }
                String jsonText = myController.jsonPaserPut(jsonMap);
                filewrite.write(jsonText);
                filewrite.flush();
                filewrite.close();
            }
        } catch (Exception e) {
            System.out.println(e);

        }
    }

    public void stopCountVal() throws IOException {
        computeGameStauts();
        updateValueClearUI();
        ui.stopCount();
        ui.scoreScreenUpdate();
    }

    private void waitCount() {
        try {
            //assuming it takes 20 secs to complete the task
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
