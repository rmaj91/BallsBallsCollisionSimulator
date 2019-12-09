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
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

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
    @FXML
    private Label speedVectorLbl;
    @FXML
    private Spinner<Double> earthAcceleration;

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
        // todo kasowanie ball po kliknieciu SECONDARY
        if (!event.getButton().name().equals("PRIMARY"))
            return;
        xFromClick = event.getSceneX();
        yFromClick = -(event.getSceneY() - simulatorCanvas.getHeight());

        if (xFromClick - ballRadius < 0)
            xFromClick = ballRadius;
        if (xFromClick + ballRadius > simulatorCanvas.getWidth())
            xFromClick = simulatorCanvas.getWidth() - ballRadius;
        if (yFromClick + ballRadius > simulatorCanvas.getHeight())
            yFromClick = simulatorCanvas.getHeight() - ballRadius;

        radiusLbl.setText(String.format("%.1f px", ballRadius));
        speedVectorLbl.setText("0.0, 0.0");
        ballCoordinatesLbl.setText(String.format("%.1f, %.1f", xFromClick, yFromClick));
        Color color = colorPicker.getValue();
        currentBall = new Ball(xFromClick, yFromClick, ballRadius, color);
        simulationEditor.add(currentBall);
        simulationEditor.drawAll();
        System.out.println("Created: " + currentBall.toString());
    }


    @FXML
    private void displayCoordinates(MouseEvent event) {
        double yCoordinate = -(event.getSceneY() - simulatorCanvas.getHeight());
        cursorCoordinatesLbl.setText(String.format("%.1f, %.1f", event.getSceneX(), yCoordinate));
    }

    @FXML
    private void setBallRadius(KeyEvent event) {
        if (currentBall != null) {
            String kChar = event.getText();
            if (kChar.equalsIgnoreCase("W") && ballRadius < MAX_BALL_RADIUS)
                ballRadius++;
            if (kChar.equalsIgnoreCase("S") && ballRadius > MIN_BALL_RADIUS)
                ballRadius--;

            radiusLbl.setText(String.format("%.1f px", ballRadius));
            currentBall.setRadius(ballRadius);
            simulationEditor.clearView();
            simulationEditor.drawAll();
        }
    }

    @FXML
    private void setBallVelocity(MouseEvent event) {
        if (currentBall != null) {
            double velocityX = -(xFromClick - event.getSceneX());
            double velocityY = -(yFromClick + event.getSceneY() - simulatorCanvas.getHeight());
            simulationEditor.drawArrow(currentBall);
            speedVectorLbl.setText(String.format("%.1f, %.1f", velocityX, velocityY));
            currentBall.getVelocity().setX(velocityX);
            currentBall.getVelocity().setY(velocityY);
        }
    }


    @FXML
    private void clearBallRef() {
        simulationEditor.clearView();
        if (simulation.isSimulationFlag()){
            simulation.add(currentBall);
            simulationEditor.clearRepo();
            System.out.print("adding to simulator...");
        }
        else{
            simulationEditor.add(currentBall);
            System.out.print("adding to editor...");
        }
        currentBall = null;
        System.out.println("deleting ref...");
    }

    @FXML
    private void resetBtnClicked() {

        System.out.println("Editor: "+simulationEditor.getBallRepo().getBalls().toString());
        System.out.println("Sim: "+simulation.getBallRepo().getBalls().toString());

        simulationEditor.clearRepo();
        simulationEditor.clearView();
        simulation.clearRepo();
        simulation.clearView();
        System.out.println("Cleared canvas and balls repo");

        System.out.println("Editor: "+simulationEditor.getBallRepo().getBalls().toString());
        System.out.println("Sim: "+simulation.getBallRepo().getBalls().toString());
    }

    @FXML
    private void simulationBtnClicked() {
        if (simulation.isSimulationFlag()) {
            simulation.stop();
            simulationBtn.setText("Start Simulation");
        } else {
            System.out.println("Editor: "+simulationEditor.getBallRepo().getBalls().toString());
            System.out.println("Sim: "+simulation.getBallRepo().getBalls().toString());

            simulation.addAll(simulationEditor.getBallRepo());
            simulationEditor.clearView();
            simulationEditor.clearRepo();

            System.out.println("Editor: "+simulationEditor.getBallRepo().getBalls().toString());
            System.out.println("Sim: "+simulation.getBallRepo().getBalls().toString());

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
        setEarthAccValueFactory();
        colorPicker.setValue(Color.DARKGREY);
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
            simulation.clearView();
            System.out.println("deleting balls and clearing canvas...");
        });

        background.widthProperty().addListener(e -> {
            double width = background.getWidth();
            simulatorCanvas.setWidth(width);
            simulatorEditorCanvas.setWidth(width);
            ballRepo.clear();
            simulation.clearView();
            System.out.println("deleting balls and clearing canvas...");
        });
    }

    private void setEarthAccValueFactory() {
        SpinnerValueFactory.DoubleSpinnerValueFactory svf
                = new SpinnerValueFactory.DoubleSpinnerValueFactory(-1000, 1000, 200, 1);
        earthAcceleration.setValueFactory(svf);
    }

}