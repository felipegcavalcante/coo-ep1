public class Enemy1 extends Enemy {

    public Enemy1(Position position, Speed speed, double radius, Explosion explosion, long nextEnemy) {
        super(position, speed, radius, explosion, nextEnemy);
    }

    @Override
    public void explode(long currentTime) {
        this.state = State.EXPLODING;
        this.explosion = new Explosion(currentTime, currentTime + 500);
    }
}
