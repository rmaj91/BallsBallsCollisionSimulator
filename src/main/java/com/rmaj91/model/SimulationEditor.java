package com.rmaj91.model;

import com.rmaj91.repository.BallRepo;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

public class SimulationEditor {
    private GraphicsContext graphicsContext;
    private Canvas canvas;
    private BallRepo ballRepo;

    public void add(Ball ball) {
        ballRepo.add(ball);
    }

    public void remove(double x, double y) {
        for (Ball ball : ballRepo.getBalls()) {
            double x1 = ball.getX();
            double y1 = ball.getY();
            double distance = Math.sqrt((x1 - x) * (x1 - x) + (y1 - y) * (y1 - y));
            if (ball.getRadius() > distance) {
                System.out.println("removing: " + ball);
                ballRepo.getBalls().remove(ball);
                break;
            }
        }
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
        for (Ball ball : this.ballRepo.getBalls()) {
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
        double y = -(ball.getY() - canvas.getHeight());
        graphicsContext.setLineWidth(1.2);
        graphicsContext.setLineDashes(10, 5);
        graphicsContext.setStroke(Color.BLACK);
        double x2 = x + ball.getVelocity().getX();
        double y2 = y - ball.getVelocity().getY();
        graphicsContext.strokeLine(x, y, x2, y2);

        //todo arrowHead
    }

// todo
//    void drawArrow(GraphicsContext gc, double x1, double y1, double x2, double y2) {
//
//        int ARR_SIZE = 8;
//        double dx = x2 - x1, dy = y2 - y1;
//        double angle = Math.atan2(dy, dx);
//        int len = (int) Math.sqrt(dx * dx + dy * dy);
//
//        Transform transform = Transform.translate(x1, y1);
//        transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
//        gc.setTransform(new Affine(transform));
//        graphicsContext.setFill(Color.RED);
//        //gc.strokeLine(0, 0, len, 0);
//        gc.fillPolygon(new double[]{len, len - ARR_SIZE, len - ARR_SIZE, len}, new double[]{0, -ARR_SIZE, ARR_SIZE, 0},
//                4);
//        gc.setTransform(new Affine(null));
//        //gc.setTransform(null);
//    }


    public void drawAllVectors() {
        for (Ball ball : ballRepo.getBalls())
            drawVector(ball);
    }
}
