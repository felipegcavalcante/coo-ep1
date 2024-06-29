public class Speed {
    private double speedX;
    private double speedY;

    public Speed(double x, double y) {
        this.speedX = x;
        this.speedY = y;
    }

    public double getSpeedX() { return speedX; }
    public double getSpeedY() { return speedY; }

    public void increaseSpeedX() {
        this.speedX++;
    }

    public void decreaseSpeedX() {
        this.speedX--;
    }

    public void increaseSpeedY() {
        this.speedY++;
    }

    public void decreaseSpeedY() {
        this.speedY--;
    }
}
