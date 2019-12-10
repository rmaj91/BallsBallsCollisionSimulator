package com.rmaj91.model;

import com.rmaj91.repository.BallRepo;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class SimulationEditor {
    private GraphicsContext graphicsContext;
    private Canvas canvas;
    private BallRepo ballRepo;

    public void add(Ball ball) {
        ballRepo.add(ball);
    }

    //todo
    public void remove(double x, double y) {
//        for (Ball ball : ballRepo.getBalls()) {
//            double x1 = ball.getX();
//            double y1 = ball.getY();
//            double distance = Math.sqrt((x1 - x) * (x1 - x) + (y1 - y) * (y1 - y));
//            if (ball.getRadius() > distance)
//                ballRepo.getBalls().remove(ball);
//            break;
//        }

    }

    public SimulationEditor(Canvas canvas) {
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.canvas = canvas;
        this.ballRepo = new BallRepo();
    }

    public void draw(Ball ball) {
        graphicsContext.setFill(ball.getColor());
        double radius = ball.getRadius();
        double yCavasCoordinate = -(ball.getY() - canvas.getHeight());
        graphicsContext.fillOval(ball.getX() - radius, yCavasCoordinate - radius, radius * 2, radius * 2);
    }

    public void drawAll() {
        if (!ballRepo.isEmpty())
            for (Ball ball : ballRepo.getBalls()) {
                draw(ball);
                drawVector(ball);
            }
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

    public void drawVector(Ball ball) {
        double x = ball.getX();
        double veloX = ball.getVelocity().getX();
        double veloY = ball.getVelocity().getY();

        double y = -(ball.getY() - canvas.getHeight());

        graphicsContext.setLineWidth(1.2);
        graphicsContext.setLineDashes(10, 5);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.strokeLine(x, y, x + veloX, y - veloY);
        //todo arrowHead
//        graphicsContext.setFill(Color.RED);
//
//        double a = veloY/(veloX-x);
//        double b = y-(veloY*x)/(veloX-x);
//
//        double xTop = (y-b)/a+20;
//        double yTop = b-(veloY*x)/(veloX-x)+20;
//
//        graphicsContext.fillPolygon(new double[]{300, xTop, 312},
//                new double[]{100, -yTop, 100}, 3);

    }

    public void drawAllVectors() {
        for (Ball ball : ballRepo.getBalls())
            drawVector(ball);
    }
}
