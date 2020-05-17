import javax.sound.midi.Soundbank;
import java.util.ArrayList;

public class Game {
    private Pawn[] pawnsWhite = new Pawn[12];
    private Pawn[] pawnsBlack = new Pawn[12];
    private int[][] boardTable = new int[8][8];

    private int PawnSelected = 0;
    private int PawnSelectedPosX = -1;
    private int PawnSelectedPosY = -1;

    private int numberPawnsWhite;
    private int numberPawnsBlack;
    private int[][] boardTableLastMove = new int[8][8];

    private boolean gameInProgress;
    private boolean moveInProgress;
    private String listOfMovements;
    private Logic logic;
    private ArrayList<Movement> movements;
    private ArrayList<Movement> currentMovements;

    private final int WHITE_PAWN = 1;
    private final int WHITE_CROWNHEAD = 2;
    private final int BLACK_PAWN = 3;
    private final int BLACK_CROWNHEAD = 4;
    private final int NO_CROSSING = -1;


    public Game() {
        logic = new Logic(8);
        movements = new ArrayList<>();
        currentMovements = new ArrayList<>();
        moveInProgress = false;
        gameInProgress = true;
        listOfMovements = "";
        pawnsStartPosition();

        // przypadki testowe
        boardTableTestCase(7);
    }

    private void setAllowedPawnsPositions() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i % 2 == 0 && j % 2 != 0) || (i % 2 != 0 && j % 2 == 0)) {
                    boardTable[i][j] = 0;
                } else {
                    boardTable[i][j] = -1;
                }
            }
        }
    }

    public void pawnsStartPosition() {
        setAllowedPawnsPositions();
        numberPawnsWhite = 12;
        numberPawnsBlack = 12;
        moveInProgress = false;
        gameInProgress = true;
        movements.clear();
        listOfMovements = "";
        int pawnWhite = 0;
        int pawnBlack = 0;
        if (pawnsWhite[0] == null) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 3; j++) {
                    if ((i + j) % 2 != 0) {
                        boardTable[i][j] = pawnWhite + 1;
                        pawnsWhite[pawnWhite] = new Pawn(pawnWhite + 1, true, false, false,
                                Pawn.white, i, j);
                        pawnWhite++;
                    } else {
                        boardTable[i][j + 5] = pawnBlack + 13;
                        pawnsBlack[pawnBlack] = new Pawn(pawnBlack + 13, true, false, false,
                                Pawn.black, i, j + 5);
                        pawnBlack++;
                    }
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 3; j++) {
                    if ((i + j) % 2 != 0) {
                        boardTable[i][j] = pawnWhite + 1;
                        pawnsWhite[pawnWhite].setActive(true);
                        pawnsWhite[pawnWhite].setCrownhead(false);
                        pawnsWhite[pawnWhite].setSelected(false);
                        pawnsWhite[pawnWhite].setPosX(i);
                        pawnsWhite[pawnWhite].setPosY(j);
                        pawnWhite++;
                    } else {
                        boardTable[i][j + 5] = pawnBlack + 13;
                        pawnsBlack[pawnBlack].setActive(true);
                        pawnsBlack[pawnBlack].setCrownhead(false);
                        pawnsBlack[pawnBlack].setSelected(false);
                        pawnsBlack[pawnBlack].setPosX(i);
                        pawnsBlack[pawnBlack].setPosY(j + 5);
                        pawnBlack++;
                    }
                }
            }
        }
        createLogicBoardTable();
    }

    private void createLogicBoardTable() {
        int[][] logicBoardTable = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // pole jest niedozwolone lub puste
                if (boardTable[i][j] < 1) {
                    logicBoardTable[i][j] = boardTable[i][j];
                } else {
                    // jesli pionek biały
                    if (boardTable[i][j] < 13) {
                        // jesli jest damką
                        if (pawnsWhite[boardTable[i][j] - 1].isCrownhead()) {
                            logicBoardTable[i][j] = 2;
                        } else {
                            logicBoardTable[i][j] = 1;
                        }
                    }
                    // jesli pionek czarny
                    if (boardTable[i][j] > 12) {
                        // jesli jest damką
                        if (pawnsBlack[boardTable[i][j] - 13].isCrownhead()) {
                            logicBoardTable[i][j] = 4;
                        } else {
                            logicBoardTable[i][j] = 3;
                        }
                    }
                }

            }
        }
        logic.setLogicBoardTable(logicBoardTable);
    }

    public void printBoard(String description, int[][] Table) {
        System.out.println(description);
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                if ((Table[i][j] > -1) && (Table[i][j] < 10)) {
                    System.out.print(" " + Table[i][j] + "\t");
                } else {
                    System.out.print(Table[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

    public void boardTableTestCase(int testCase) {
        movements = new ArrayList<>();
        switch (testCase) {
            case 1:
                // nowe rozdanie

                break;
            case 2:
                // bicie przez biały pionek do przodu
                boardTable[7][2] = 0;
                boardTable[6][3] = 12;
                pawnsWhite[11].setPosX(6);
                pawnsWhite[11].setPosY(3);

                boardTable[4][5] = 0;
                boardTable[5][4] = 19;
                pawnsBlack[19 - 13].setPosX(5);
                pawnsBlack[19 - 13].setPosY(4);
                break;
            case 3:
                // bicie przez biały pionek do tyłu
                boardTable[7][2] = 0;
                boardTable[5][4] = 12;
                pawnsWhite[11].setPosX(5);
                pawnsWhite[11].setPosY(4);

                boardTable[4][5] = 0;
                boardTable[6][3] = 19;
                pawnsBlack[19 - 13].setPosX(6);
                pawnsBlack[19 - 13].setPosY(3);
                break;
            case 4:
                // bicie przez biała damkę do przodu
                pawnsWhite[11].setCrownhead(true);

                boardTable[4][5] = 0;
                boardTable[5][4] = 19;
                pawnsBlack[19 - 13].setPosX(5);
                pawnsBlack[19 - 13].setPosY(4);
                break;
            case 5:
                // bicie wielokrotne przez biała damkę
                pawnsWhite[11].setCrownhead(true);

                boardTable[4][5] = 0;
                boardTable[5][4] = 19;
                pawnsBlack[19 - 13].setPosX(5);
                pawnsBlack[19 - 13].setPosY(4);
                boardTable[2][5] = 0;
                boardTable[3][4] = 16;
                pawnsBlack[19 - 16].setPosX(3);
                pawnsBlack[19 - 16].setPosY(4);
                break;
            case 6:
                // bicie wielokrotne przez biała pion
                boardTable[7][2] = 0;
                boardTable[6][3] = 12;
                pawnsWhite[11].setPosX(6);
                pawnsWhite[11].setPosY(3);

                boardTable[4][5] = 0;
                boardTable[5][4] = 19;
                pawnsBlack[19 - 13].setPosX(5);
                pawnsBlack[19 - 13].setPosY(4);
                boardTable[2][5] = 0;
                boardTable[3][4] = 16;
                pawnsBlack[19 - 16].setPosX(3);
                pawnsBlack[19 - 16].setPosY(4);
                break;
            case 7:
                // wszystkie czarne możliwe do zbicia
                boardTable[0][5] = 0;
                pawnsBlack[13 - 13].setActive(false);
                boardTable[0][7] = 0;
                pawnsBlack[14 - 13].setActive(false);
                boardTable[2][7] = 0;
                pawnsBlack[17 - 13].setActive(false);
                boardTable[4][5] = 0;
                pawnsBlack[19 - 13].setActive(false);
                boardTable[4][7] = 0;
                pawnsBlack[20 - 13].setActive(false);
                boardTable[6][7] = 0;
                pawnsBlack[23 - 13].setActive(false);
                boardTable[7][6] = 0;
                pawnsBlack[24 - 13].setActive(false);
                numberPawnsBlack -= 7;
                break;
        }
        printBoard("BoardTable", boardTable);
        System.out.println();
        createLogicBoardTable();
        printBoard("LogicBoardTable", logic.getLogicBoardTable());
        System.out.println();
    }

    public void pawnSelect(int posX, int posY) {
        int currentField = boardTable[posX][posY];

        System.out.println("X=" + posX + " Y=" + posY);
        System.out.println("Pion " + currentField);

        // Zaznaczanie/odznaczanie piona
        if (currentField > 0 && currentField < 13 && !moveInProgress) {
            // jesli pion nie jest jeszcze zaznaczony i nie ma obowiązku ruchu innym pionem
            if (!pawnsWhite[currentField - 1].isSelected() &&
                    !logic.isOtherPawnMustBeMove(WHITE_PAWN, posX, posY) && !moveInProgress) {

                pawnsWhite[currentField - 1].setSelected(true);
                PawnSelected = currentField;
                System.out.println("Pion " + currentField + " zaznaczony");
            } else { // jeśli pion jest zaznaczony ale ruch się jeszcze nie rozpoczoł
                pawnsWhite[currentField - 1].setSelected(false);
                PawnSelected = 0;
                System.out.println("Pion " + currentField + " odznaczony");
            }
        }

        // jesli jakiś pion jest zaznaczony i wskazano puste pole
        if (PawnSelected > 0 && currentField == 0) {
            int startX = pawnsWhite[PawnSelected - 1].getPosX();
            int startY = pawnsWhite[PawnSelected - 1].getPosY();
            int stopX = posX;
            int stopY = posY;
            movements.clear();
            movements.addAll(logic.pawnPossibleCaptures(startX, startY));

            // Jesli pion ma bicie
            if (movements.size() > 0) {
                for (int i = 0; i < movements.size(); i++) {
                    if (movements.get(i).stopX == stopX && movements.get(i).stopY == stopY) {
                        System.out.println("Przesuniecie piona z biciem");
                        currentMovements.add(movements.get(i));
                        pawnMoveExecute(currentMovements);

                        movements.clear();
                        movements.addAll(logic.pawnPossibleCaptures(stopX, stopY));
                        // Jesli pion ma dalsze bicie
                        if (movements.size() > 0) {
                            System.out.println("Konieczny dalszy ruch piona. Bicie wielokrotne.");
                            moveInProgress = true;
                        } else { // Jeśli pion nie ma dalszego bicia
                            System.out.println("Pion nie ma dalszego bicia");
                            allowNewMovement();
                        }

                    }
                }
            } else {
                // Jeśli pion nie ma bicia
                System.out.println("Pion nie ma bicia");
                movements.clear();
                movements.addAll(logic.moveAllowed(startX, startY, stopX, stopY));
                for (int i = 0; i < movements.size(); i++) {
                    if (movements.get(i).stopX == stopX && movements.get(i).stopY == stopY) {
                        System.out.println("Przesuniecie piona bez bicia");
                        currentMovements.add(movements.get(i));
                        pawnMoveExecute(currentMovements);
                        allowNewMovement();
                    }
                }
            }
        }

    }

    private void allowNewMovement() {
        moveInProgress = false;
        pawnsWhite[PawnSelected - 1].setSelected(false);
        if (pawnsWhite[PawnSelected - 1].getPosY() == 7) {
            pawnsWhite[PawnSelected - 1].setCrownhead(true);
        }
        ;
        PawnSelected = 0;
    }

    private void pawnMoveExecute(ArrayList<Movement> currentMovements) {
        int startX;
        int startY;
        int stopX;
        int stopY;
        int crossingX;
        int crossingY;
        int currentPawn;
        int crrosingPawn;
        int pawnColor;

        String currentMoveStart;
        String currentMoveStop;

        startX = currentMovements.get(0).startX;
        startY = currentMovements.get(0).startY;
        currentPawn = boardTable[startX][startY];
        if (currentPawn < 13) {
            pawnColor = WHITE_PAWN;
        } else {
            pawnColor = BLACK_PAWN;
            listOfMovements += "\t\t";
        }

        for (int i = 0; i < currentMovements.size(); i++) {

            startX = currentMovements.get(i).startX;
            startY = currentMovements.get(i).startY;
            stopX = currentMovements.get(i).stopX;
            stopY = currentMovements.get(i).stopY;
            crossingX = currentMovements.get(i).crossingX;
            crossingY = currentMovements.get(i).crossingY;
            currentPawn = boardTable[startX][startY];

            currentMoveStart = "";
            currentMoveStop = "";

            if (currentPawn < 13) {
                pawnColor = WHITE_PAWN;
            } else {
                pawnColor = BLACK_PAWN;
                listOfMovements += "\t\t";
            }

            if (pawnColor == WHITE_PAWN) {
                boardTable[startX][startY] = 0;
                boardTable[stopX][stopY] = currentPawn;
                pawnsWhite[currentPawn - 1].setPosX(stopX);
                pawnsWhite[currentPawn - 1].setPosY(stopY);
                //pawnsWhite[currentPawn - 1].setSelected(false);

                if (crossingX != NO_CROSSING) {
                    crrosingPawn = boardTable[crossingX][crossingY];
                    boardTable[crossingX][crossingY] = 0;
                    pawnsBlack[crrosingPawn - 13].setActive(false);
                    numberPawnsBlack--;
                }
            }

            if (pawnColor == BLACK_PAWN) {
                boardTable[startX][startY] = 0;
                boardTable[stopX][stopY] = currentPawn;
                pawnsBlack[currentPawn - 13].setPosX(stopX);
                pawnsBlack[currentPawn - 13].setPosY(stopY);
                if (crossingX != NO_CROSSING) {
                    crrosingPawn = boardTable[crossingX][crossingY];
                    boardTable[crossingX][crossingY] = 0;
                    pawnsWhite[crrosingPawn - 1].setActive(false);
                    numberPawnsWhite--;
                }
            }

            currentMoveStart = Character.toString((char) (65 + startX)) + (startY + 1);
            currentMoveStop = Character.toString((char) (65 + stopX)) + (stopY + 1);
            listOfMovements += currentMoveStart + "-" + currentMoveStop;

            if (crossingX != NO_CROSSING) {
                listOfMovements += " (";
                listOfMovements += Character.toString((char) (65 + crossingX)) + (crossingY + 1);
                listOfMovements += ")\n";
            } else {
                listOfMovements += "\n";
            }

        }
        currentMovements.clear();
        createLogicBoardTable();
        isGameOver(pawnColor);
    }

    public void isGameOver(int pawnColor) {
        System.out.println("Sprawdzanie czy nie koniec gry");
        System.out.println("numberPawnsWhite = " + numberPawnsWhite);
        System.out.println("numberPawnsBlack = " + numberPawnsBlack);
        gameInProgress = true;
        if (numberPawnsWhite == 0 || numberPawnsBlack == 0) {
            listOfMovements += "\nGAME OVER\n";
            if (pawnColor == WHITE_PAWN) {
                listOfMovements += "You win :(";
            } else {
                listOfMovements += "I win :)";
            }
            gameInProgress = false;
        } else {
            //if (pawnColor == WHITE_PAWN && logic.i)
            // ToDo dopisac do klasy Logic metodę sprawdzająca czy dany gracz ma jakąkolwiek możliwość ruchu
            // z biciem czy bez bicia
        }
    }

    public Pawn[] getPawnsWhite() {
        return pawnsWhite;
    }

    public Pawn[] getPawnsBlack() {
        return pawnsBlack;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public String getListOfMovements() {
        return listOfMovements;
    }
}
