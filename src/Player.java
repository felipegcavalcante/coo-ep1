public class Player {
    private State state;
    private Position position;
    private Speed speed;
    private double radius;
    private long nextShot;
    private Explosion explosion;

    public Player(State state, Position position, Speed speed, double radius, long nextShot, Explosion explosion) {
        this.state = state;
        this.position = position;
        this.speed = speed;
        this.radius = radius;
        this.nextShot = nextShot;
        this.explosion = explosion;
    }
    public State getState() { return state; }
    public Position getPosition() { return position; }
    public Speed getSpeed() { return speed; }
    public double getRadius() { return radius; }
    public long getNextShot() { return nextShot; }
    public Explosion getExplosion() { return explosion; }
    public void explode(long currentTime) {
        this.state = State.EXPLODING;
        this.explosion = new Explosion(currentTime, currentTime + 2000);
    }
    public void activate() { this.state = State.ACTIVE; }

    public void setPositionX(double x) {
        this.position = new Position(x, this.position.getY());
    }

    public void setPositionY(double y) {
        this.position = new Position(this.position.getX(), y);
    }

    public void setNextShot(long nextShot) {
        this.nextShot = nextShot;
    }
}
