import java.util.ArrayList;
import java.util.List;


public class Game {
    private final Pawn[] pawnsWhite = new Pawn[12];
    private final Pawn[] pawnsBlack = new Pawn[12];
    private final int[][] boardTable = new int[8][8];

    private int PawnSelected = 0;
    private int numberPawnsWhite;
    private int numberPawnsBlack;

    private boolean gameInProgress;
    private boolean moveInProgress;
    private boolean computerMove;
    private String listOfMovements;
    private final Logic logic;

    private final int WHITE_PAWN = 1;
    private final int BLACK_PAWN = 3;
    private final int NO_CROSSING = -1;


    public Game() {
        logic = new Logic(8);
        moveInProgress = false;
        gameInProgress = true;
        computerMove = false;
        listOfMovements = "";
        pawnsStartPosition();

        // przypadki testowe
        //boardTableTestCase(1);
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
        computerMove = false;
        listOfMovements = "";
        int pawnWhite = 0;
        int pawnBlack = 0;
        if (pawnsWhite[0] == null) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 3; j++) {
                    if ((i + j) % 2 != 0) {
                        boardTable[i][j] = pawnWhite + 1;
                        pawnsWhite[pawnWhite] = new Pawn(true, false, false, i, j);
                        pawnWhite++;
                    } else {
                        boardTable[i][j + 5] = pawnBlack + 13;
                        pawnsBlack[pawnBlack] = new Pawn(true, false, false, i, j + 5);
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

        // Przypadki testowe
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
                // bicie wielokrotne przez biały pion
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
                // czarny pinek nie ma możliwości ruch
                boardTable[0][5] = 0;
                pawnsBlack[13 - 13].setActive(false);
                boardTable[0][7] = 0;
                pawnsBlack[14 - 13].setActive(false);
                boardTable[2][7] = 0;
                pawnsBlack[15 - 13].setActive(false);
                boardTable[1][6] = 0;
                pawnsBlack[16 - 13].setActive(false);
                boardTable[2][5] = 0;
                pawnsBlack[17 - 13].setActive(false);
                boardTable[4][5] = 0;
                pawnsBlack[18 - 13].setActive(false);
                boardTable[3][6] = 0;
                pawnsBlack[19 - 13].setActive(false);
                boardTable[4][7] = 0;
                pawnsBlack[20 - 13].setActive(false);
                boardTable[6][7] = 0;
                pawnsBlack[21 - 13].setActive(false);
                boardTable[5][6] = 0;
                pawnsBlack[22 - 13].setPosX(0);
                pawnsBlack[22 - 13].setPosY(3);
                boardTable[6][5] = 0;
                boardTable[0][3] = 22;
                pawnsBlack[23 - 13].setActive(false);
                boardTable[7][6] = 0;
                pawnsBlack[24 - 13].setActive(false);
                numberPawnsBlack -= 11;
                break;
        }
        printBoard("BoardTable", boardTable);
        System.out.println();
        createLogicBoardTable();
        printBoard("LogicBoardTable", logic.getLogicBoardTable());
        System.out.println();
    }

    public void pawnSelect(int posX, int posY) {
        List<Movement> pawnMovementTable = new ArrayList<>();
        List<Movement> pawnCurrentMovementTable = new ArrayList<>();
        int currentField = boardTable[posX][posY];
        System.out.println();
        System.out.println("X=" + posX + " Y=" + posY);
        System.out.println("Pion " + currentField);


        // Zaznaczanie/odznaczanie piona
        if (currentField > 0 && currentField < 13 && !moveInProgress) {
            // jesli pion nie jest jeszcze zaznaczony i nie ma obowiązku ruchu innym pionem
            if (!pawnsWhite[currentField - 1].isSelected() && PawnSelected == 0 &&
                    !logic.isOtherPawnMustBeMove(WHITE_PAWN, posX, posY) && !moveInProgress) {
                pawnsWhite[currentField - 1].setSelected(true);
                PawnSelected = currentField;
                System.out.println("Pion " + currentField + " zaznaczony");
            } else { // jeśli pion jest zaznaczony ale ruch się jeszcze nie rozpoczoł
                pawnsWhite[currentField - 1].setSelected(false);
                if (PawnSelected > 0) {pawnsWhite[PawnSelected - 1].setSelected(false);}
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
            pawnMovementTable.addAll(logic.pawnPossibleCaptures(startX, startY));

            // Jesli pion ma bicie
            if (pawnMovementTable.size() > 0) {
                for (int i = 0; i < pawnMovementTable.size(); i++) {
                    if (pawnMovementTable.get(i).stopX == stopX && pawnMovementTable.get(i).stopY == stopY) {
                        System.out.println("Przesuniecie piona z biciem");
                        pawnCurrentMovementTable.add(pawnMovementTable.get(i));
                        pawnMoveExecute(new ArrayList<>(pawnCurrentMovementTable));

                        pawnMovementTable.clear();
                        pawnMovementTable.addAll(logic.pawnPossibleCaptures(stopX, stopY));
                        // Jesli pion ma dalsze bicie
                        if (pawnMovementTable.size() > 0) {
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
                pawnMovementTable.clear();
                pawnMovementTable.addAll(logic.moveAllowed(startX, startY, stopX, stopY));
                for (Movement movement : pawnMovementTable) {
                    if (movement.stopX == stopX && movement.stopY == stopY) {
                        System.out.println("Przesuniecie piona bez bicia");
                        pawnCurrentMovementTable.add(movement);
                        pawnMoveExecute(new ArrayList<>(pawnCurrentMovementTable));
                        allowNewMovement();
                    }
                }
            }
        }

    }

    public void computerMove() {
        List<Movement> computerMoveTable = new ArrayList<>(logic.computerSillyMove());

        if (computerMoveTable.get(computerMoveTable.size()-1).stopY == 0) {
            int startX = computerMoveTable.get(0).startX;
            int startY = computerMoveTable.get(0).startY;
            int pawn = boardTable[startX][startY];
            pawnsBlack[pawn -13].setCrownhead(true);
            createLogicBoardTable();
        }
        pawnMoveExecute(new ArrayList<>(computerMoveTable));
        computerMove = false;
    }

    private void allowNewMovement() {
        moveInProgress = false;
        pawnsWhite[PawnSelected - 1].setSelected(false);
        if (pawnsWhite[PawnSelected - 1].getPosY() == 7) {
            pawnsWhite[PawnSelected - 1].setCrownhead(true);
            createLogicBoardTable();
        }
        PawnSelected = 0;
        computerMove = true;
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
        }

        for (Movement currentMovement : currentMovements) {

            startX = currentMovement.startX;
            startY = currentMovement.startY;
            stopX = currentMovement.stopX;
            stopY = currentMovement.stopY;
            crossingX = currentMovement.crossingX;
            crossingY = currentMovement.crossingY;
            currentPawn = boardTable[startX][startY];

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
        if (numberPawnsWhite == 0 || numberPawnsBlack == 0) {
            listOfMovements += "\nGAME OVER\n";
            if (pawnColor == WHITE_PAWN) {
                listOfMovements += "You win :(";
            } else {
                listOfMovements += "I win :)";
            }
            gameInProgress = false;
        } else {
            if (pawnColor == WHITE_PAWN && logic.isPlayerHaVeNoMove(BLACK_PAWN)) {
                listOfMovements += "I Have no move.\n You win :(";
                gameInProgress = false;
            }
            if (pawnColor == BLACK_PAWN && logic.isPlayerHaVeNoMove(WHITE_PAWN)) {
                listOfMovements += "You have no move.\n I win :)";
                gameInProgress = false;
            }
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

    public boolean isComputerMove() {
        return computerMove;
    }

    public String getListOfMovements() {
        return listOfMovements;
    }
}
