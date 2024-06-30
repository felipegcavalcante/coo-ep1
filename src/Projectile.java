public class Projectile {
    private Position position;
    private Speed speed;
    private State state;
    private double radius;

    public Projectile(Position position, Speed speed, double radius) {
        this.position = position;
        this.speed = speed;
        this.state = State.INACTIVE;
        this.radius = radius;
    }

    public double getPositionX() { return position.getX(); }
    public double getPositionY() { return position.getY(); }
    public Speed getSpeed() { return speed; }
    public State getState() { return state; }
    public double getRadius() { return radius; }

    public boolean isActive() {
        return this.state == State.ACTIVE;
    }

    public void deactivate() {
        this.state = State.INACTIVE;
    }

    public void setSpeed(double x, double y) {
        this.speed = new Speed(x, y);
    }

    public void setPosition(double x, double y) {
        this.position = new Position(x, y);
    }

    public void setPositionX(double x) {
        this.position = new Position(x, this.position.getY());
    }

    public void setPositionY(double y) {
        this.position = new Position(this.position.getX(), y);
    }


    public void increasePositionX(double x) {
        this.position.increaseX(x);
    }

    public void increasePositionY(double y) {
        this.position.increaseY(y);
    }

    public void setState(State state) {
        this.state = state;
    }
}
