import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.InputStream;

public class Pawn {
    private int id;
    private boolean active;
    private boolean crownhead;
    private boolean selected;
    private Image image;
    private int posX;
    private int posY;

    public static Image black = new Image("file:resources/black.png");
    public static Image blackSelected = new Image("file:resources/Brown_piece.png");
    public static Image blackCrownhead = new Image("file:resources/Brown_piece_with_crown.png");
    public static Image blackCrownheadSelected = new Image("file:resources/black.png");
    public static Image white = new Image("file:resources/white.png");
    public static Image whiteSelected = new Image("file:resources/Grey_piece.png");
    public static Image whiteCrownhead = new Image("file:resources/Grey_piece_with_crown.png");
    public static Image whiteCrownheadSelected = new Image("file:resources/white.png");

    public Pawn (int id, boolean active, boolean crownhead, boolean selected, Image image, int posX, int posY) {
        this.id = id;
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

    public void setImage(Image image) {
        this.image = image;
    }
}
