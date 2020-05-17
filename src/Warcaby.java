import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.FileNotFoundException;

public class Warcaby extends Application {
    private Group root = new Group();
    private Scene scene = new Scene(root, 800, 600);
    private Rectangle[][] cellRectangle = new Rectangle[8][8];
    private Group cells = new Group();
    private Label statement = new Label();
    private Button startBtn = new Button();
    private TextArea textArea = new TextArea();

    private ImageView[] imageViewBlack;
    private ImageView[] imageViewWhite;
    private Game game = new Game();
    // Odległość planszy od krawędzi
    private int x0 = 40;
    private int y0 = 40;
    private int squaresSize = (int) (scene.getHeight() - 2 * y0) / 8;

    public static void main(String[] args) {
         launch(args);
    }

    public void creatBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cellRectangle[i][j] = new Rectangle(x0 + i * squaresSize, y0 + j * squaresSize,
                        squaresSize, squaresSize);
                if ((i + j) % 2 == 0) cellRectangle[i][j].setFill(Color.CADETBLUE);
                else cellRectangle[i][j].setFill(Color.BROWN);
                cells.getChildren().add(cellRectangle[i][j]);
            }
        }
        root.getChildren().add(cells);

        // poziome linnie planszy
        Line[] lineH = new Line[9];
        for (int i = 0; i < 9; i++) {
            lineH[i] = new Line(x0, y0 + i * squaresSize, x0 + squaresSize * 8, y0 + i * squaresSize);
            root.getChildren().add(lineH[i]);
        }

        // pionowe linnie planszy
        Line[] lineV = new Line[9];
        for (int j = 0; j < 9; j++) {
            lineV[j] = new Line(x0 + j * squaresSize, y0, x0 + j * squaresSize, y0 + squaresSize * 8);
            root.getChildren().add(lineV[j]);
        }

        // Opisy planszy
        Label labelV = new Label();
        labelV.setText("A         B         C        D         E         F        G        H");
        labelV.setFont(Font.font(22));
        labelV.setLayoutX(60);
        labelV.setLayoutY(0);
        root.getChildren().add(labelV);

        Label labelH = new Label();
        labelH.setText("1\n\n2\n\n3\n\n4\n\n5\n\n6\n\n7\n\n8");
        labelH.setFont(Font.font(22));
        labelH.setLayoutX(10);
        labelH.setLayoutY(60);
        root.getChildren().add(labelH);
    }

    public void creatPawnsImage() throws FileNotFoundException {

        imageViewBlack = new ImageView[12];
        imageViewWhite = new ImageView[12];
        for (int i = 0; i < 12; i++) {
            imageViewWhite[i] = new ImageView(Pawn.white);
            imageViewBlack[i] = new ImageView(Pawn.black);
            root.getChildren().add(imageViewWhite[i]);
            root.getChildren().add(imageViewBlack[i]);
        }
    }

    public void creatGameInterface() {
        // Pole historii posunięć
        textArea.setEditable(false);
        textArea.setFont(Font.font(20));
        textArea.setLayoutX(590);
        textArea.setLayoutY(40);
        textArea.setPrefHeight(340);
        textArea.setPrefWidth(180);
//        textArea.setText("d2-b4    a5-c3\nd2-b4    a5-c3\nd2-b4    a5-c3\nd2-b4    a5-c3\n" +
//                "d2-b4    a5-c3\nd2-b4    a5-c3\nd2-b4    a5-c3\nd2-b4    a5-c3\n" +
//                "d2-b4    a5-c3\nd2-b4    a5-c3\nd2-b4    a5-c3\nd2-b4    a5-c3\n");
        textArea.setText(game.getListOfMovements());
        root.getChildren().add(textArea);

        // pole komunikatów
        statement.setLayoutX(590);
        statement.setLayoutY(390);
        statement.setFont(Font.font(24));
        root.getChildren().add(statement);

        // Przycisk start
        startBtn.setText("Start");
        startBtn.setFont(Font.font(24));
        startBtn.setLayoutX(590);
        startBtn.setLayoutY(500);
        startBtn.setPrefWidth(180);
        startBtn.setPrefHeight(60);
        root.getChildren().add(startBtn);
    }

    public void drawPawns() {
        Pawn[] pawnsWhite = game.getPawnsWhite();
        Pawn[] pawnsBlack = game.getPawnsBlack();

        for(int i = 0; i < 12; i++) {

            if (pawnsWhite[i].isActive()) {
                imageViewWhite[i].visibleProperty().set(true);
                if (!pawnsWhite[i].isSelected()) {
                    if (!pawnsWhite[i].isCrownhead()) {
                        imageViewWhite[i].setImage(Pawn.white);
                    } else {
                        imageViewWhite[i].setImage(Pawn.whiteCrownhead);
                    }
                }
                if (pawnsWhite[i].isSelected()) {
                    if (!pawnsWhite[i].isCrownhead()) {
                        imageViewWhite[i].setImage(Pawn.whiteSelected);
                    } else {
                        imageViewWhite[i].setImage(Pawn.whiteCrownheadSelected);
                    }
                }
                imageViewWhite[i].setX(x0 + pawnsWhite[i].getPosX() * squaresSize);
                imageViewWhite[i].setY(y0 + pawnsWhite[i].getPosY() * squaresSize);
            } else {
                imageViewWhite[i].visibleProperty().set(false);
            }

            if (pawnsBlack[i].isActive()) {
                imageViewBlack[i].visibleProperty().set(true);
                if (!pawnsWhite[i].isSelected()) {
                    if (!pawnsBlack[i].isCrownhead()) {
                        imageViewBlack[i].setImage(Pawn.black);
                    } else {
                        imageViewBlack[i].setImage(Pawn.blackCrownhead);
                    }
                }
                if (pawnsBlack[i].isSelected()) {
                    if (!pawnsBlack[i].isCrownhead()) {
                        imageViewBlack[i].setImage(Pawn.blackSelected);
                    } else {
                        imageViewBlack[i].setImage(Pawn.blackCrownheadSelected);
                    }
                }
                imageViewBlack[i].setX(x0 + pawnsBlack[i].getPosX() * squaresSize);
                imageViewBlack[i].setY(y0 + pawnsBlack[i].getPosY() * squaresSize);
            } else {
                imageViewBlack[i].visibleProperty().set(false);
            }

            textArea.setText("");
        }
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {

        // Rysowanie pól
        creatBoard();

        // Tworzenie obrazów pionów
        creatPawnsImage();

        // Rysowanie pionków
        drawPawns();

        // Tworzenie pozostałych elementów layoutu
        creatGameInterface();

        // Głowne okno aplikacji
        primaryStage.setTitle("Warcaby");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Start gry");
                // Resetowanie połozenia pionków
                game.pawnsStartPosition();
                drawPawns();
            }
        });

        // Obsługa położenia myszki
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int ix = 0;
                int iy = 0;
                if (mouseEvent.getX() > x0 && mouseEvent.getX() < (x0 + 8*squaresSize) &&
                        mouseEvent.getY() > y0 && mouseEvent.getY() < (y0 + 8*squaresSize)) {
                    ix = ((int) mouseEvent.getX() - x0) / squaresSize;
                    iy = ((int) mouseEvent.getY() - y0) / squaresSize;
                    statement.setText("Pole: " + Character.toString((char) (65 + ix)) + (iy + 1));
                } else {
                    statement.setText("");
                }
            }
        });


        // Obsługa naciśnieńcia przycisku myszki
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int ix = 0;
                int iy = 0;
                if (mouseEvent.getX() > x0 && mouseEvent.getX() < (x0 + 8*squaresSize) &&
                        mouseEvent.getY() > y0 && mouseEvent.getY() < (y0 + 8*squaresSize)) {
                    ix = ((int) mouseEvent.getX() - x0) / squaresSize;
                    iy = ((int) mouseEvent.getY() - y0) / squaresSize;
                    System.out.println("Zazanaczyłeś pole : " + Character.toString((char) (65 + ix)) + (iy + 1));

                    // Przesuwanie pionka
                    if (game.isGameInProgress()) {
                        game.pawnSelect(ix,iy);
                        drawPawns();
                        textArea.setText(game.getListOfMovements());
                    }

                }
            }
        });

    }
}
