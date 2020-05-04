import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Warcaby extends Application {

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600);
        Rectangle[][] cellRectangle = new Rectangle[8][8];
        Group cells = new Group();

        Image black = new Image(new FileInputStream("black.png"));
        Image white = new Image(new FileInputStream("white.png"));
        ImageView[] imageViewBlack = new ImageView[12];;
        ImageView[] imageViewWhite = new ImageView[12];

        for (int i=0; i<12; i++) {
            imageViewBlack[i] = new ImageView(black);
            imageViewWhite[i] = new ImageView(white);
        }

        int x0 = 40;
        int y0 = 40;
        int squaresSize;

        squaresSize = (int) (scene.getHeight() - 2 * y0) / 8;

        System.out.println(squaresSize);

        // Rysowanie pól
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

        // Rysowanie pionków
        int pionWhite = 0;
        int pionBlack = 0;
        for(int i = 0; i<8; i++)
            for(int j = 0; j<3; j++) {
                if ((i+j)%2 != 0) {
                    imageViewWhite[pionWhite].setX(x0 + i * squaresSize);
                    imageViewWhite[pionWhite].setY(y0 + j * squaresSize);
                    root.getChildren().add(imageViewWhite[pionWhite]);
                    pionWhite++;
                }
                else {
                    imageViewBlack[pionBlack].setX(x0 + i * squaresSize);
                    imageViewBlack[pionBlack].setY(y0 + (j + 5) * squaresSize);
                    root.getChildren().add(imageViewBlack[pionBlack]);
                    pionBlack++;
                }
            }

        // linnie planszy
        Line[] lineH = new Line[9];
        for (int i = 0; i < 9; i++) {
            lineH[i] = new Line(x0, y0 + i * squaresSize, x0 + squaresSize * 8, y0 + i * squaresSize);
            root.getChildren().add(lineH[i]);
        }

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

        // Pole historii posunięć
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setFont(Font.font(20));
        textArea.setLayoutX(590);
        textArea.setLayoutY(40);
        textArea.setPrefHeight(340);
        textArea.setPrefWidth(180);
        textArea.setText("d2-b4    a5-c3\nd2-b4    a5-c3\nd2-b4    a5-c3\nd2-b4    a5-c3\n" +
                "d2-b4    a5-c3\nd2-b4    a5-c3\nd2-b4    a5-c3\nd2-b4    a5-c3\n" +
                "d2-b4    a5-c3\nd2-b4    a5-c3\nd2-b4    a5-c3\nd2-b4    a5-c3\n");
        root.getChildren().add(textArea);

        // pole komunikatów
        Label statement = new Label();
        statement.setLayoutX(590);
        statement.setLayoutY(390);
        statement.setFont(Font.font(24));
        root.getChildren().add(statement);

        // Obsługa położenia myszki
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int ix = 0;
                int iy = 0;
                if (mouseEvent.getX() > 10 && mouseEvent.getY() > 50) {
                    ix = ((int) mouseEvent.getX() - x0) / squaresSize;
                    iy = ((int) mouseEvent.getY() - y0) / squaresSize;
                }
                if (ix < 8 && iy < 8) statement.setText("Pole: " + Character.toString((char) (65 + ix)) + (iy + 1));
            }
        });

        // Obsługa naciśnieńcia przycisku myszki
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int ix = 0;
                int iy = 0;
                if (mouseEvent.getX() > 10 && mouseEvent.getY() > 50) {
                    ix = ((int) mouseEvent.getX() - x0) / squaresSize;
                    iy = ((int) mouseEvent.getY() - y0) / squaresSize;
                }
                if (ix < 8 && iy < 8)
                    System.out.println("Zazanaczyłeś pole : " + Character.toString((char) (65 + ix)) + (iy + 1));

                // Test przesuwania pionka
                imageViewWhite[11].setX(x0 + 6 * squaresSize);
                imageViewWhite[11].setY(y0 + 3 * squaresSize);

            }
        });

        // Przycisk start
        Button startBtn = new Button();
        startBtn.setText("Start");
        startBtn.setFont(Font.font(24));
        startBtn.setLayoutX(590);
        startBtn.setLayoutY(500);
        startBtn.setPrefWidth(180);
        startBtn.setPrefHeight(60);
        root.getChildren().add(startBtn);

        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Start gry");

                // Resetowanie połozenia pionków
                int pionWhite = 0;
                int pionBlack = 0;
                for(int i = 0; i<8; i++)
                    for(int j = 0; j<3; j++) {
                        if ((i+j)%2 != 0) {
                            imageViewWhite[pionWhite].setX(x0 + i * squaresSize);
                            imageViewWhite[pionWhite].setY(y0 + j * squaresSize);
                            pionWhite++;
                        }
                        else {
                            imageViewBlack[pionBlack].setX(x0 + i * squaresSize);
                            imageViewBlack[pionBlack].setY(y0 + (j + 5) * squaresSize);
                            pionBlack++;
                        }
                    }
            }
        });

        primaryStage.setTitle("Warcaby");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
