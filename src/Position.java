public class Position {
    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void moveUp() {
        this.x++;
    }

    public void moveDown() {
        this.x--;
    }

    public void moveRight() {
        this.y++;
    }

    public void moveLeft() {
        this.y--;
    }

    public void setX(double x) {
        this.x = x;
    }
}
