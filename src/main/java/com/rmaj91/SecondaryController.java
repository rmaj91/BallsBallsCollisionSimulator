package com.rmaj91;

import java.net.URL;
import java.util.ResourceBundle;

import com.rmaj91.model.Ball;
import com.rmaj91.model.Simulation;
import com.rmaj91.model.SimulationEditor;
import com.rmaj91.repository.BallRepo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;

public class SecondaryController implements Initializable {

    public static double ballRadius = 20;
    public static final double MIN_BALL_RADIUS = 5;
    public static final double MAX_BALL_RADIUS = 100;


    //=============================================================================================
    // JavaFX elements
    //=============================================================================================
    @FXML
    private Canvas simulatorCanvas;
    @FXML
    private Canvas simulatorEditorCanvas;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Button simulationBtn;
    @FXML
    private BorderPane rootPane;
    @FXML
    private StackPane background;
    @FXML
    private Label ballCoordinatesLbl;
    @FXML
    private Label cursorCoordinatesLbl;
    @FXML
    private Label radiusLbl;

    //=============================================================================================
    // Properties
    //=============================================================================================
    private BallRepo ballRepo;
    private Ball currentBall;
    private Simulation simulation;
    private SimulationEditor simulationEditor;

    private double xFromClick;
    private double yFromClick;


    //=============================================================================================
    // EVENTS METHODS
    //=============================================================================================
    @FXML
    private void createBall(MouseEvent event) {
        if (!event.getButton().name().equals("PRIMARY"))
            return;
        xFromClick = event.getSceneX();
        yFromClick = event.getSceneY();

        if (xFromClick - ballRadius < 0)
            xFromClick = ballRadius;
        if (xFromClick + ballRadius > simulatorCanvas.getWidth())
            xFromClick = simulatorCanvas.getWidth() - ballRadius;
        if (yFromClick + ballRadius > simulatorCanvas.getHeight())
            yFromClick = simulatorCanvas.getHeight() - ballRadius;


        ballCoordinatesLbl.setText(String.format("%.1f, %.1f", xFromClick, yFromClick));
        Color color = colorPicker.getValue();
        currentBall = new Ball(xFromClick, yFromClick, ballRadius, color);
        simulationEditor.add(currentBall);
        simulationEditor.drawAll();
        System.out.println("Created: " + currentBall.toString());
    }


    @FXML
    private void displayCoordinates(MouseEvent event) {
        cursorCoordinatesLbl.setText(String.format("%.1f, %.1f", event.getSceneX(), event.getSceneY()));
    }

    @FXML
    private void setBallRadius(KeyEvent event) {
        if (currentBall == null)
            return;


        simulationEditor.drawAll();
        System.out.println("New radius: " + currentBall.getRadius());

//        simulationEditor.clear();
//
//        double radius = currentBall.getRadius();
//        String kChar = event.getText();
//
//        if (kChar.equalsIgnoreCase("W"))
//            radius++;
//        if (kChar.equalsIgnoreCase("S"))
//            radius--;
//
//        if (radius < MIN_BALL_RADIUS || radius > MAX_BALL_RADIUS)
//            return;
//
//        currentBall.setRadius(radius);
//
//        if (xFromClick + radius > simulatorCanvas.getWidth())
//            xFromClick = simulatorCanvas.getWidth() - radius;
//        if (yFromClick + radius > simulatorCanvas.getHeight())
//            yFromClick = simulatorCanvas.getHeight() - radius;
//
//        currentBall.setX(xcursor-radius/2);
//        currentBall.setY(ycursor-radius/2);
//
//


    }

    @FXML
    private void setBallVelocity(MouseEvent event) {
//        if (currentBall != null) {
//            double x = xFromClick - event.getX();
//            double y = yFromClick - event.getY();
//            currentBall.getVelocity().setX(x);
//            currentBall.getVelocity().setY(y);
//            System.out.println("Velocity: " + x + "\t" + y);
//        } else
//            return;
    }


    @FXML
    private void clearBallRef() {
        //todo dodawac z editora?
        ballRepo.add(currentBall);
        currentBall = null;
        ballCoordinatesLbl.setText(String.format("%.1f, %.1f", 0, 0));
        System.out.println("deleting ball ref...");
    }

    @FXML
    private void resetBtnClicked() {
        simulation.clearRepo();
        simulation.clearCanvas();
        System.out.println("Cleared canvas and balls repo");
    }

    @FXML
    private void simulationBtnClicked() {
        if (simulation.isSimulationFlag()) {
            simulation.stop();
            simulationBtn.setText("Start Simulation");
        } else {
            simulationEditor.clear();
            simulation.start();
            simulationBtn.setText("Stop Simulation");
        }
    }

    //=============================================================================================
    // Initialization
    //=============================================================================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        App.stage.setWidth(1280);
        App.stage.setHeight(600);
        App.stage.setResizable(true);
        simulationEditor = new SimulationEditor(simulatorEditorCanvas);
        ballRepo = new BallRepo();
        simulation = new Simulation(ballRepo, simulatorCanvas);
        setBackgroundSizeListeners();
    }


    //=============================================================================================
    // Private Methods
    //=============================================================================================

    private void setBackgroundSizeListeners() {
        background.heightProperty().addListener(e -> {
            double height = background.getHeight();
            simulatorCanvas.setHeight(height);
            simulatorEditorCanvas.setHeight(height);
            ballRepo.clear();
            simulation.clearCanvas();
            System.out.println("deleting balls and clearing canvas...");
        });

        background.widthProperty().addListener(e -> {
            double width = background.getWidth();
            simulatorCanvas.setWidth(width);
            simulatorEditorCanvas.setWidth(width);
            ballRepo.clear();
            simulation.clearCanvas();
            System.out.println("deleting balls and clearing canvas...");
        });
    }

}