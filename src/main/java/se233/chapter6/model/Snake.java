package se233.chapter6.model;

import javafx.geometry.Point2D;
import se233.chapter6.view.GameStage;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private Direction direction;
    private Point2D head;
    private Point2D prev_tail;
    private List<Point2D> body;
    public Snake(Point2D position) {
        direction = Direction.DOWN;
        body = new ArrayList<>();
        this.head = position;
        this.body.add(this.head);
    }
    public void move() {
        head = head.add(direction.current);
        prev_tail = body.removeLast();
        body.addFirst(head);
    }
    public void setDirection(Direction direction) {
        // Prevent 180-degree turns: ignore if the new direction is opposite to current
        if (this.direction == Direction.UP && direction == Direction.DOWN) return;
        if (this.direction == Direction.DOWN && direction == Direction.UP) return;
        if (this.direction == Direction.LEFT && direction == Direction.RIGHT) return;
        if (this.direction == Direction.RIGHT && direction == Direction.LEFT) return;
        this.direction = direction;
    }
    public Direction getDirection() { return direction; }
    public Point2D getHead() { return head; }
    public boolean collided(Food food) { return head.equals(food.getPosition()); }
    public void grow() { body.add(prev_tail); }
    public int getLength() { return body.size(); }
    public List<Point2D> getBody() { return body; }
    public boolean checkDead() {
        boolean isOutOfBound = head.getX() < 0 || head.getY() < 0 || head.getX() >= GameStage.WIDTH || head.getY() >= GameStage.HEIGHT;
        boolean isHitBody = body.lastIndexOf(head) > 0;
        if (isOutOfBound || isHitBody) {
            body.remove(head);
            body.addLast(prev_tail);
            return true;
        } else {
            return false;
        }
    }
}
