package com.rmaj91.model;

import com.rmaj91.repository.BallRepo;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class Simulation {
    private BallRepo ballRepo;
    private boolean simulationFlag;
    private Canvas canvas;
    private GraphicsContext graphicsContext;

    public Simulation(BallRepo ballRepo, Canvas canvas) {
        simulationFlag = false;
        this.ballRepo = ballRepo;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
    }

    public void start(){
        simulationFlag = true;
        clearView();
        drawAll();
    }

    public void stop(){
        simulationFlag = false;
        graphicsContext.setFont(new Font(25));
        graphicsContext.strokeText("Stopped",canvas.getWidth()/2,canvas.getHeight()/2,100);
    }

    public void drawAll(){
        if (!ballRepo.isEmpty()) {
            for (Ball ball : ballRepo.getBalls()) {
                graphicsContext.setFill(ball.getColor());
                graphicsContext.fillOval(ball.getX(),ball.getY(),ball.getRadius(),ball.getRadius());
            }
        }
    }

    public void draw(Ball ball){
        graphicsContext.setFill(ball.getColor());
        graphicsContext.fillOval(ball.getX(),ball.getY(),ball.getRadius(),ball.getRadius());
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
        ballRepo.addAll(ballRepo);
    }

    public BallRepo getBallRepo() {
        return ballRepo;
    }
}
