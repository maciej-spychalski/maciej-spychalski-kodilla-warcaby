public class Game {
    private Pawn[] pawnsWhite = new Pawn[12];
    private Pawn[] pawnsBlack = new Pawn[12];
    private int[][] boardTable = new int[8][8];
    private String listOfMovements;
    private int PawnSelected = 0;
    private int PawnSelectedPosX = -1;
    private int PawnSelectedPosY = -1;

    private int[][] boardTableLastMove = new int[8][8];
    private String listOfMovementsLastMove;
    boolean isMoveCopleated;


    public Game() {
        pawnsStartPosition();
    }

    private void setAllowedPawnsPositions() {
        for(int i = 0; i < 8; i++) {
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
        listOfMovements = "";
        int pawnWhite = 0;
        int pawnBlack = 0;
        if (pawnsWhite[0] == null) {
            for(int i = 0; i < 8; i++) {
                for (int j = 0; j < 3; j++) {
                    if ((i + j) % 2 != 0) {
                        boardTable[i][j] = pawnWhite + 1;
                        pawnsWhite[pawnWhite] = new Pawn(pawnWhite+1,true, false, false,
                                Pawn.white, i, j);
                        pawnWhite++;
                    } else {
                        boardTable[i][j+5] = pawnBlack + 13;
                        pawnsBlack[pawnBlack] = new Pawn(pawnBlack + 13,true, false, false,
                                Pawn.black, i, j + 5);
                        pawnBlack++;
                    }
                }
            }
        } else {
            for(int i = 0; i < 8; i++) {
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
                        boardTable[i][j+5] = pawnBlack + 13;
                        pawnsBlack[pawnBlack].setActive(true);
                        pawnsBlack[pawnBlack].setCrownhead(false);
                        pawnsBlack[pawnBlack].setSelected(false);
                        pawnsBlack[pawnBlack].setPosX(i);
                        pawnsBlack[pawnBlack].setPosY(j+5);
                        pawnBlack++;
                    }
                }
            }
        }

    }

    public void pawnSelect(int posX, int posY) {
        int currentPawn = boardTable[posX][posY];
        System.out.println("X=" + posX + " Y=" + posY);
        System.out.println("Pion " + currentPawn);

        // Zaznaczanie/odznaczanie piona
        if (currentPawn > 0 && currentPawn < 13) {
            if (!pawnsWhite[currentPawn-1].isSelected() && PawnSelected == 0) {
                pawnsWhite[currentPawn-1].setSelected(true);
                PawnSelected = currentPawn;
                PawnSelectedPosX = posX;
                PawnSelectedPosY = posY;
                System.out.println("Pion " + currentPawn + " zaznaczony");
            } else {
                if (PawnSelected == currentPawn) {
                    PawnSelected = 0;
                    pawnsWhite[currentPawn-1].setSelected(false);
                    System.out.println("Pion " + currentPawn + " odznaczony");
                    PawnSelectedPosX = -1;
                    PawnSelectedPosY = -1;
                }

            }
        }

        // Wykonywanie ruchu
        if (PawnSelected != 0 && currentPawn == 0) {
            pawnsWhite[PawnSelected-1].setPosX(posX);
            pawnsWhite[PawnSelected-1].setPosY(posY);
            pawnsWhite[PawnSelected-1].setSelected(false);
            boardTable[PawnSelectedPosX][PawnSelectedPosY] = 0;
            boardTable[posX][posY] = PawnSelected;
            PawnSelected = 0;
            writeMove(PawnSelectedPosX, PawnSelectedPosY, posX, posY);
        }
    }

    private void writeMove(int PosXStart, int PosYStart, int PosXStop, int PosYStop) {
        String currentMoveStart = Character.toString((char) (65 + PosXStart)) + (PosYStart + 1);
        String currentMoveStop = Character.toString((char) (65 + PosXStop)) + (PosYStop + 1);
        listOfMovements += currentMoveStart + "-" + currentMoveStop +"\n";
    }

    public String getListOfMovements() {
        return listOfMovements;
    }

    public Pawn[] getPawnsWhite() {
        return pawnsWhite;
    }

    public Pawn[] getPawnsBlack() {
        return pawnsBlack;
    }

}
