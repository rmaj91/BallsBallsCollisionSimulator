package com.rmaj91.model;

import com.rmaj91.repository.BallRepo;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SimulationEditor {
    private GraphicsContext graphicsContext;
    private Canvas canvas;
    private BallRepo ballRepo;

    public void add(Ball ball) {
        ballRepo.add(ball);
    }

    public void remove(int index) {

    }

    public SimulationEditor(Canvas canvas) {
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.canvas = canvas;
        this.ballRepo = new BallRepo();
    }

    public void draw(Ball ball) {
        graphicsContext.setFill(ball.getColor());
        double radius = ball.getRadius();
        double yCavasCoordinate =  -(ball.getY()-canvas.getHeight());
        graphicsContext.fillOval(ball.getX()- radius, yCavasCoordinate- radius, radius *2, radius*2);
    }

    public void drawAll(){
        if (!ballRepo.isEmpty())
            for (Ball ball : ballRepo.getBalls())
                draw(ball);
    }

    public void clearView() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public BallRepo getBallRepo() {
        return ballRepo;
    }

    public void clearRepo() {
        ballRepo = new BallRepo();
    }

    public void drawArrow(Ball currentBall) {
        double x = currentBall.getX();
        double y = currentBall.getY();

        graphicsContext.setStroke(Color.RED);
        graphicsContext.strokeLine(x,y,x+50,y+50);

    }
}
