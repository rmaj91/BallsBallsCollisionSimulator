package com.rmaj91.repository;

import com.rmaj91.model.Ball;

import java.util.HashSet;
import java.util.LinkedList;


public class BallRepo {

    LinkedList<Ball> balls;

    public BallRepo() {
        balls = new  LinkedList<>();
    }

    public void add(Ball ball){
        balls.add(ball);
    }

    public LinkedList<Ball> getBalls() {
        return balls;
    }

    public void clear(){
        balls.clear();
    }

    public boolean isEmpty() {
        return balls.isEmpty();
    }

    public void addAll(BallRepo ballRepo) {
        balls.addAll(ballRepo.getBalls());
    }

    public int size(){
        return balls.size();
    }

    public Ball get(int index){
        return balls.get(index);
    }
}
