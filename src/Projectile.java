public class Projectile {
    private Position position;
    private System speed;
    private State state;
    private double radius;

    public Projectile(Position position, System speed, double radius) {
        this.position = position;
        this.speed = speed;
        this.state = State.INACTIVE;
        this.radius = radius;
    }

    public Position getPosition() { return position; }
    public System getSpeed() { return speed; }
    public State getState() { return state; }
    public double getRadius() { return radius; }
}
