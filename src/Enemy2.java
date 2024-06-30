public class Enemy2 extends Enemy {
    private double spawn = GameLib.WIDTH * 0.20;
    public static int count = 0;

    public Enemy2(Position position, Speed speed, double radius, Explosion explosion, long nextEnemy) {
        super(position, speed, radius, explosion, nextEnemy);
        count++;
    }

    @Override
    public void explode(long currentTime) {
        this.state = State.EXPLODING;
        this.explosion = new Explosion(currentTime, currentTime + 500);
    }

    public double getSpawn() {
        return spawn;
    }
}
