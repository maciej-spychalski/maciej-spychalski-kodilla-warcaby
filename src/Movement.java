public class Movement {
    int startX;
    int startY;
    int stopX;
    int stopY;
    int crossingX;
    int crossingY;

    public Movement(int startX, int startY, int stopX, int stopY, int crossingX, int crossingY) {
        this.startX = startX;
        this.startY = startY;
        this.stopX = stopX;
        this.stopY = stopY;
        this.crossingX = crossingX;
        this.crossingY = crossingY;
    }

    @Override
    public String toString() {
        return "Movement{" +
                "startX=" + startX +
                ", startY=" + startY +
                ", stopX=" + stopX +
                ", stopY=" + stopY +
                ", crossingX=" + crossingX +
                ", crossingY=" + crossingY +
                '}';
    }
}
