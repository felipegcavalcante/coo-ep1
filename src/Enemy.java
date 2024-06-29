public abstract class Enemy {
    protected State state;
    protected Position position;
    protected Speed speed;
    protected double radius;
    protected Explosion explosion;
    protected long nextEnemy;

    public Enemy(Position position, Speed speed, double radius, Explosion explosion, long nextEnemy) {
        this.state = State.INACTIVE;
        this.position = position;
        this.speed = speed;
        this.radius = radius;
        this.explosion = explosion;
        this.nextEnemy = nextEnemy;
    }

    public State getState() {
        return state;
    }

    public Position getPosition() {
        return position;
    }

    public Speed getSpeed() {
        return speed;
    }

    public double getRadius() {
        return radius;
    }

    public Explosion getExplosion() {
        return explosion;
    }

    public abstract void explode(long currentTime);

//    public void updateStateAfterExplosion(long currentTime) {
//        if (this.state == EXPLODING && currentTime > this.explosionEnd) {
//            this.state = INACTIVE;
//        }
//    }
}
