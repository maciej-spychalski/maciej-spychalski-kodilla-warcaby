public class Pawn {
    private boolean active;
    private boolean Crownhead;
    private int posX;
    private int posY;

    public Pawn (boolean active, boolean Crownhead, int posX, int posY) {
        this.active = active;
        this.Crownhead = Crownhead;
        this.posX = posX;
        this.posY = posY;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isCrownhead() {
        return Crownhead;
    }

    public void setCrownhead(boolean crownhead) {
        Crownhead = crownhead;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
