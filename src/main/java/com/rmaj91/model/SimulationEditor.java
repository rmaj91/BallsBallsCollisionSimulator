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
        graphicsContext.fillOval(ball.getX()- radius, ball.getY()- radius, radius *2, radius*2);
    }

    public void drawAll(){
        if (!ballRepo.isEmpty())
            for (Ball ball : ballRepo.getBalls())
                draw(ball);
    }

    public void clear() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

}
