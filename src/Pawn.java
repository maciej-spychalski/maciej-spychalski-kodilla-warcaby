import javafx.scene.image.Image;

public class Pawn {
    private boolean active;
    private boolean crownhead;
    private boolean selected;
    private int posX;
    private int posY;

    public static Image black = new Image("file:resources/black.png");
    public static Image blackSelected = new Image("file:resources/black_selected.png");
    public static Image blackCrownhead = new Image("file:resources/black_piece_with_crown.png");
    public static Image blackCrownheadSelected = new Image("file:resources/black_piece_with_crown_selected.png");
    public static Image white = new Image("file:resources/white.png");
    public static Image whiteSelected = new Image("file:resources/white_selected.png");
    public static Image whiteCrownhead = new Image("file:resources/white_piece_with_crown.png");
    public static Image whiteCrownheadSelected = new Image("file:resources/white_piece_with_crown_selected.png");

    public Pawn (boolean active, boolean crownhead, boolean selected, int posX, int posY) {
        this.active = active;
        this.crownhead = crownhead;
        this.selected = selected;
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
        return crownhead;
    }

    public void setCrownhead(boolean crownhead) {
        this.crownhead = crownhead;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
