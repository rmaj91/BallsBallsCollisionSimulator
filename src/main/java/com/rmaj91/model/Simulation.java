package com.rmaj91.model;

import com.rmaj91.repository.BallRepo;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class Simulation {
    private BallRepo ballRepo;
    private boolean simulationFlag;

    //=============================================================================================
    // Static Properties
    //=============================================================================================
    public static final double LEFT_WALL_ABS = 0.85;
    public static final double RIGHT_WALL_ABS = 0.85;
    public static final double FLOOR_ABS = 0.85;

    public static Canvas canvas;
    private GraphicsContext graphicsContext;
    private double earthAcceleration;
    public static final double FPS = 100;
    private Timeline timelineDraw;
    private AnimationTimer animationTimer;
    private long startNanoTime;

    // earth acc tutaj

    public Simulation(BallRepo ballRepo, Canvas canvas) {
        simulationFlag = false;
        this.ballRepo = ballRepo;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        timelineDraw = new Timeline(new KeyFrame(Duration.millis(1000/ FPS), e->{
            if(!this.ballRepo.isEmpty()){
                for (Ball ball : this.ballRepo.getBalls()) {
                    ball.moveBall(1000/ FPS);
                }
                clearView();
                drawAll();
            }
        }));
        timelineDraw.setCycleCount(Timeline.INDEFINITE);


//        animationTimer = new AnimationTimer() {
//            @Override
//            public void handle(long currentNanoTime) {
//                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
//            }
//        };
    }

    public void start(){
        simulationFlag = true;
        clearView();
        drawAll();
        timelineDraw.play();

        startNanoTime = System.nanoTime();





    }



    public void stop(){
        timelineDraw.stop();
        simulationFlag = false;

        graphicsContext.setLineWidth(2);
        Font theFont = Font.font( "Times New Roman", FontWeight.BOLD, 48 );
        graphicsContext.setFont( theFont );
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.setStroke( Color.BLACK );
        graphicsContext.strokeText( "Simulation Paused!", canvas.getWidth()/2, canvas.getHeight()/2 );
    }

    public void drawAll(){
        if (!ballRepo.isEmpty())
            for (Ball ball : ballRepo.getBalls())
                draw(ball);
    }

    public void draw(Ball ball){
        if(ball !=null){
        graphicsContext.setFill(ball.getColor());
        double radius = ball.getRadius();
        double yCavasCoordinate = -(ball.getY() - canvas.getHeight());
        graphicsContext.fillOval(ball.getX() - radius, yCavasCoordinate - radius, radius * 2, radius * 2);
        }
    }
    public void clearView(){
        graphicsContext.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
    }

    public void clearRepo(){
        ballRepo = new BallRepo();
    }

    public boolean isSimulationFlag() {
        return simulationFlag;
    }

    public void add(Ball ball) {
        ballRepo.add(ball);
    }

    public void addAll(BallRepo ballRepo) {
        for (Ball ball : ballRepo.getBalls()) {
            this.ballRepo.add(ball);
        }
    }

    public void setEarthAcceleration(double earthAcceleration) {
        this.earthAcceleration = earthAcceleration;
    }

    public BallRepo getBallRepo() {
        return ballRepo;
    }

    public static Canvas getCanvas() {
        return canvas;
    }
}
