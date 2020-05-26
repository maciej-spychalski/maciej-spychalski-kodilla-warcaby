//         Y\X   0   1   2   3   4   5   6   7   8
//              (A) (B) (C) (D) (E) (F) (G) (H) (I)
//        0 (1)|   |   |   |   |   |   |   |   |   |
//        1 (2)|   |   |   |   |   |   |   |   |   |
//        2 (3)|   |   |   |   |   |   |   |   |   |
//        3 (4)|   |   |   |   |   |   |   |   |   |
//        4 (5)|   |   |   |   |   |   |   |   |   |
//        5 (6)|   |   |   |   |   |   |   |   |   |
//        6 (7)|   |   |   |   |   |   |   |   |   |
//        7 (8)|   |   |   |   |   |   |   |   |   |
//
// Possible value in fields:
// - 1  - prohibited field
//   0  - free field
//   1  - white pawn
//   2  - white crownhead
//   3  - black pawn
//   4  - black crownhead
//
//                 NW   N   NE
//                  W   o   E
//                 SW   S   SE

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

public class Logic {

    private int[][] logicBoardTable;
    private final int FREE_FIELD = 0;
    private final int WHITE_PAWN = 1;
    private final int WHITE_CROWNHEAD = 2;
    private final int BLACK_PAWN = 3;
    private final int BLACK_CROWNHEAD = 4;
    private final int NORTH_WEST = 50;
    private final int NORTH_EAST = 10;
    private final int SOUTH_EAST = 20;
    private final int SOUTH_WEST = 40;
    private final int NO_CROSSING = -1;
    private final int boardSize;
    private final int MAX_X;
    private final int MAX_Y;

    public Logic(int boardSize) {
        this.boardSize = boardSize;
        MAX_X = this.boardSize - 1;
        MAX_Y = this.boardSize - 1;
        logicBoardTable = new int[boardSize][boardSize];
    }

    public void setLogicBoardTable(int[][] logicBoardTable) {
        this.logicBoardTable = logicBoardTable;
    }

    public int[][] getLogicBoardTable() {
        return logicBoardTable;
    }

    public List<Movement> moveAllowed(int startPosX, int startPosY, int stopPosX, int stopPosY) {

        List<Movement> moveAllowedTable = new ArrayList<>();
        int crossingX = NO_CROSSING;
        int crossingY = NO_CROSSING;

        // If final field isn't free
        if (logicBoardTable[stopPosX][stopPosY] != FREE_FIELD) {
            return new ArrayList<>(moveAllowedTable);
        }

        // If final field is out of board
        if (stopPosX > MAX_X || stopPosY > MAX_Y) {
            return new ArrayList<>(moveAllowedTable);
        }

        int pawnColor = logicBoardTable[startPosX][startPosY];

        int direction = -1;

        if (startPosX > stopPosX && startPosY > stopPosY) {
            direction = NORTH_WEST;
            crossingX = stopPosX + 1;
            crossingY = stopPosY + 1;
        }
        if (startPosX < stopPosX && startPosY > stopPosY) {
            direction = NORTH_EAST;
            crossingX = stopPosX - 1;
            crossingY = stopPosY + 1;
        }
        if (startPosX < stopPosX && startPosY < stopPosY) {
            direction = SOUTH_EAST;
            crossingX = stopPosX - 1;
            crossingY = stopPosY - 1;
        }
        if (startPosX > stopPosX && startPosY < stopPosY) {
            direction = SOUTH_WEST;
            crossingX = stopPosX + 1;
            crossingY = stopPosY - 1;
        }

        int distance = abs(startPosX - stopPosX);

        // Movement of pawn without capture
        if ((distance == 1) && isFreeFields(startPosX, startPosY, direction, distance)) {
            if (pawnColor == WHITE_PAWN && (direction == SOUTH_WEST || direction == SOUTH_EAST)) {
                moveAllowedTable.add(new Movement(startPosX, startPosY, stopPosX, stopPosY, NO_CROSSING, NO_CROSSING));
                return new ArrayList<>(moveAllowedTable);
            }
            if (pawnColor == BLACK_PAWN && (direction == NORTH_WEST || direction == NORTH_EAST)) {
                moveAllowedTable.add(new Movement(startPosX, startPosY, stopPosX, stopPosY, NO_CROSSING, NO_CROSSING));
                return new ArrayList<>(moveAllowedTable);
            }
        }

        // Movement of pawn with capture
        if ((distance == 2) && isOpponentsPawn(startPosX, startPosY, direction, (distance - 1))) {
            moveAllowedTable.add(new Movement(startPosX, startPosY, stopPosX, stopPosY, crossingX, crossingY));
            return new ArrayList<>(moveAllowedTable);
        }

        // Movement of crownhead without capture
        if ((pawnColor == WHITE_CROWNHEAD || pawnColor == BLACK_CROWNHEAD) &
                isFreeFields(startPosX, startPosY, direction, (distance - 1))) {
            moveAllowedTable.add(new Movement(startPosX, startPosY, stopPosX, stopPosY, NO_CROSSING, NO_CROSSING));
            return new ArrayList<>(moveAllowedTable);
        }

        // Movement of crownhead with capture
        if (pawnColor == WHITE_CROWNHEAD || pawnColor == BLACK_CROWNHEAD) {
            int numberOfOpponetsPawn = 0;
            int numberOfFreeFields = 0;
            for (int i = 1; i <= distance; i++) {
                if (isOpponentsPawn(startPosX, stopPosY, direction, i)) {
                    numberOfOpponetsPawn++;
                }
                if (isFreeFields(startPosX, startPosY, direction, i)) {
                    numberOfFreeFields++;
                }
            }
            if (numberOfOpponetsPawn == 1 && numberOfFreeFields == (distance - 1)) {
                moveAllowedTable.add(new Movement(startPosX, startPosY, stopPosX, stopPosY, crossingX, crossingY));
                return new ArrayList<>(moveAllowedTable);
            }

        }

        return new ArrayList<>(moveAllowedTable);
    }

    public List<Movement> pawnPossibleCaptures(int startPosX, int startPosY) {

        List<Movement> pawnPossibleCapturesTable = new ArrayList<>();
        int crossingX;
        int crossingY;

        boolean crownhead = false;
        if ((logicBoardTable[startPosX][startPosY] == WHITE_CROWNHEAD) ||
                (logicBoardTable[startPosX][startPosY] == BLACK_CROWNHEAD)) {
            crownhead = true;
        }

        int maxEmptyFieds = 0;

        if (crownhead) {
            maxEmptyFieds = boardSize - 3;
        }

        int freeFields = 0;
        do {
            // NORTH_WEST
            if (startPosX >= (2 + freeFields) && startPosY >= (2 + freeFields) &&
                    isOpponentsPawn(startPosX, startPosY, NORTH_WEST, (1 + freeFields)) &&
                    logicBoardTable[startPosX - (2 + freeFields)][startPosY - (2 + freeFields)] == FREE_FIELD &&
                    isFreeFields(startPosX, startPosY, NORTH_WEST, freeFields)) {
                crossingX = startPosX - (1 + freeFields);
                crossingY = startPosY - (1 + freeFields);
                pawnPossibleCapturesTable.add(new Movement(startPosX, startPosY,
                        startPosX - (2 + freeFields), startPosY - (2 + freeFields),
                        crossingX, crossingY));

                // Sprawdzenie czy za zbitym pionkiem/damką nie ma dodatkowych wolnych pól na których może
                // zatrzymać się bijąca damka
                if (crownhead) {
                    int maxFreeFieldsAfterCrossing;
                    int freeFieldsAfterCrossing = 1;
                    if (startPosX < startPosY) {
                        maxFreeFieldsAfterCrossing = startPosX - (2 + freeFields);
                    } else {
                        maxFreeFieldsAfterCrossing = startPosY - (2 + freeFields);
                    }

                    while (freeFieldsAfterCrossing <= maxFreeFieldsAfterCrossing) {
                        if (isFreeFields(startPosX - (2 + freeFields), startPosY - (2 + freeFields),
                                NORTH_WEST,freeFieldsAfterCrossing)) {
                            pawnPossibleCapturesTable.add(new Movement(startPosX, startPosY,
                                    startPosX - (2 + freeFields + freeFieldsAfterCrossing),
                                    startPosY - (2 + freeFields + freeFieldsAfterCrossing),
                                    crossingX, crossingY));
                        } else break;
                        freeFieldsAfterCrossing++;
                    }
                }

            }

            // NORTH_EAST
            if (startPosX <= (MAX_X - 2 - freeFields) && startPosY >= (2 + freeFields) &&
                    isOpponentsPawn(startPosX, startPosY, NORTH_EAST, (1 + freeFields)) &&
                    logicBoardTable[startPosX + (2 + freeFields)][startPosY - (2 + freeFields)] == FREE_FIELD &&
                    isFreeFields(startPosX, startPosY, NORTH_EAST, freeFields)) {
                crossingX = startPosX + (1 + freeFields);
                crossingY = startPosY - (1 + freeFields);
                pawnPossibleCapturesTable.add(new Movement(startPosX, startPosY,
                        startPosX + (2 + freeFields), startPosY - (2 + freeFields),
                        crossingX, crossingY));

                // Sprawdzenie czy za zbitym pionkiem/damką nie ma dodatkowych wolnych pól na których może
                // zatrzymać się bijąca damka
                if (crownhead) {
                    int maxFreeFieldsAfterCrossing;
                    int freeFieldsAfterCrossing = 1;
                    if (startPosX > startPosY) {
                        maxFreeFieldsAfterCrossing = MAX_X - (startPosX + (2 + freeFields));
                    } else {
                        maxFreeFieldsAfterCrossing = startPosY - (2 + freeFields);
                    }

                    while (freeFieldsAfterCrossing <= maxFreeFieldsAfterCrossing) {
                        if (isFreeFields(startPosX + (2 + freeFields), startPosY - (2 + freeFields),
                                NORTH_EAST,freeFieldsAfterCrossing)) {
                            pawnPossibleCapturesTable.add(new Movement(startPosX, startPosY,
                                    startPosX + (2 + freeFields + freeFieldsAfterCrossing),
                                    startPosY - (2 + freeFields + freeFieldsAfterCrossing),
                                    crossingX, crossingY));
                        } else break;
                        freeFieldsAfterCrossing++;
                    }
                }


            }

            // SOUTH_EAST
            if (startPosX <= (MAX_X - 2 - freeFields) && startPosY <= (MAX_Y - 2 - freeFields) &&
                    isOpponentsPawn(startPosX, startPosY, SOUTH_EAST, (1 + freeFields)) &&
                    logicBoardTable[startPosX + (2 + freeFields)][startPosY + (2 + freeFields)] == FREE_FIELD &&
                    isFreeFields(startPosX, startPosY, SOUTH_EAST, freeFields)) {
                crossingX = startPosX + (1 + freeFields);
                crossingY = startPosY + (1 + freeFields);
                pawnPossibleCapturesTable.add(new Movement(startPosX, startPosY,
                        startPosX + (2 + freeFields), startPosY + (2 + freeFields),
                        crossingX, crossingY));

                // Sprawdzenie czy za zbitym pionkiem/damką nie ma dodatkowych wolnych pól na których może
                // zatrzymać się bijąca damka
                if (crownhead) {
                    int maxFreeFieldsAfterCrossing;
                    int freeFieldsAfterCrossing = 1;
                    if (startPosX > startPosY) {
                        maxFreeFieldsAfterCrossing = MAX_X - (startPosX + (2 + freeFields));
                    } else {
                        maxFreeFieldsAfterCrossing = MAX_Y - (startPosY + (2 + freeFields));
                    }

                    while (freeFieldsAfterCrossing <= maxFreeFieldsAfterCrossing) {
                        if (isFreeFields(startPosX + (2 + freeFields), startPosX + (2 + freeFields),
                                SOUTH_EAST,freeFieldsAfterCrossing)) {
                            pawnPossibleCapturesTable.add(new Movement(startPosX, startPosY,
                                    startPosX + (2 + freeFields + freeFieldsAfterCrossing),
                                    startPosY + (2 + freeFields + freeFieldsAfterCrossing),
                                    crossingX, crossingY));
                        } else break;
                        freeFieldsAfterCrossing++;
                    }
                }

            }

            // SOUTH_WEST
            if (startPosX >= (2 + freeFields) && startPosY <= (MAX_Y - 2 - freeFields) &&
                    isOpponentsPawn(startPosX, startPosY, SOUTH_WEST, (1 + freeFields)) &&
                    logicBoardTable[startPosX - (2 + freeFields)][startPosY + (2 + freeFields)] == FREE_FIELD &&
                    isFreeFields(startPosX, startPosY, SOUTH_WEST, freeFields)) {
                crossingX = startPosX - (1 + freeFields);
                crossingY = startPosY + (1 + freeFields);
                pawnPossibleCapturesTable.add(new Movement(startPosX, startPosY,
                        startPosX - (2 + freeFields),  startPosY + (2 + freeFields),
                        crossingX, crossingY));

                // Sprawdzenie czy za zbitym pionkiem/damką nie ma dodatkowych wolnych pól na których może
                // zatrzymać się bijąca damka
                if (crownhead) {
                    int maxFreeFieldsAfterCrossing;
                    int freeFieldsAfterCrossing = 1;
                    if (startPosX < startPosY) {
                        maxFreeFieldsAfterCrossing = startPosX - (2 + freeFields);
                    } else {
                        maxFreeFieldsAfterCrossing = MAX_Y - (startPosY + (2 + freeFields));
                    }

                    while (freeFieldsAfterCrossing <= maxFreeFieldsAfterCrossing) {
                        if (isFreeFields(startPosX - (2 + freeFields), startPosY + (2 + freeFields),
                                SOUTH_WEST,freeFieldsAfterCrossing)) {
                            pawnPossibleCapturesTable.add(new Movement(startPosX, startPosY,
                                    startPosX - (2 + freeFields + freeFieldsAfterCrossing),
                                    startPosY + (2 + freeFields + freeFieldsAfterCrossing),
                                    crossingX, crossingY));
                        } else break;
                        freeFieldsAfterCrossing++;
                    }
                }

            }

            freeFields++;
        } while (freeFields <= maxEmptyFieds);

        return new ArrayList<>(pawnPossibleCapturesTable);
    }

    public List<Movement> playerPossibleCaptures(int pawnColor) {

        List<Movement> playerPossibleCapturesTable = new ArrayList<>();

        int pawnCrownheadColor;
        if (pawnColor == WHITE_PAWN) {
            pawnCrownheadColor = WHITE_CROWNHEAD;
        } else {
            pawnCrownheadColor = BLACK_CROWNHEAD;
        }

        for (int j = 0; j <= MAX_Y; j++) {
            for (int i = 0; i <= MAX_X; i++) {
                if (logicBoardTable[i][j] == pawnColor || logicBoardTable[i][j] == pawnCrownheadColor) {
                    playerPossibleCapturesTable.addAll(pawnPossibleCaptures(i, j));
                }
            }
        }

        return new ArrayList<>(playerPossibleCapturesTable);
    }

    public boolean isOtherPawnMustBeMove(int pawnColor, int startPosX, int startPosY) {

        // spradzenie czy jakikolwiek pion o dany kolorze ma bicie
        List<Movement> allPawnCaptureTable = new ArrayList<>(playerPossibleCaptures(pawnColor));
        if ((allPawnCaptureTable.size() == 0) && isPawnHaveFreeSpace(startPosX, startPosY)) {
            return false;
        } else {
            // sprawdzenie czy wybrany pion ma bicie
            List<Movement> currentPawnCaptureTable = new ArrayList<>(pawnPossibleCaptures(startPosX, startPosY));
            return currentPawnCaptureTable.size() == 0;
        }
    }

    public boolean isPawnHaveFreeSpace(int startPosX, int startPosY) {

        int pawn = logicBoardTable[startPosX][startPosY];

        // Sprawdzanie czy ruch wypadnie na planszy
        // && sprawdzenie czy pionek zakonczy ruch na wolnym polu w dozwolonym dla niego kierunku

        // NORTH_WEST
        if ((startPosX >= 1) && (startPosY >= 1) &&
                (pawn == BLACK_PAWN || pawn == WHITE_CROWNHEAD || pawn == BLACK_CROWNHEAD) &&
                isFreeFields(startPosX, startPosY, NORTH_WEST, 1)) {
            return true;
        }

        // NORTH_EAST
        if ((startPosX <= (MAX_X - 1)) && (startPosY >= 1) &&
                (pawn == BLACK_PAWN || pawn == WHITE_CROWNHEAD || pawn == BLACK_CROWNHEAD) &&
                isFreeFields(startPosX, startPosY, NORTH_EAST, 1)) {
            return true;
        }

        // SOUTH_EAST
        if ((startPosX <= (MAX_X - 1)) && (startPosY <= (MAX_Y - 1)) &&
                (pawn == WHITE_PAWN || pawn == WHITE_CROWNHEAD || pawn == BLACK_CROWNHEAD) &&
                isFreeFields(startPosX, startPosY, SOUTH_EAST, 1)) {
            return true;
        }

        // SOUTH_WEST
        if ((startPosX >= 1) && (startPosY <= (MAX_Y - 1)) &&
                (pawn == WHITE_PAWN || pawn == WHITE_CROWNHEAD || pawn == BLACK_CROWNHEAD) &&
                isFreeFields(startPosX, startPosY, SOUTH_WEST, 1)) {
            return true;
        }

        return false;
    }

    public boolean isOpponentsPawn(int startPosX, int startPosY, int direction, int distance) {

        if (distance == 0) {
            return false;
        }

        if (logicBoardTable[startPosX][startPosY] == WHITE_PAWN ||
                logicBoardTable[startPosX][startPosY] == WHITE_CROWNHEAD) {
            switch (direction) {
                case NORTH_WEST:
                    if (startPosX >= distance && startPosY >= distance &&
                            (logicBoardTable[startPosX - distance][startPosY - distance] == BLACK_PAWN ||
                                    logicBoardTable[startPosX - distance][startPosY - distance] == BLACK_CROWNHEAD)) {
                        return true;
                    }
                    break;
                case NORTH_EAST:
                    if (startPosX <= (MAX_X - distance) && startPosY >= distance &&
                            (logicBoardTable[startPosX + distance][startPosY - distance] == BLACK_PAWN ||
                                    logicBoardTable[startPosX + distance][startPosY - distance] == BLACK_CROWNHEAD)) {
                        return true;
                    }
                    break;
                case SOUTH_EAST:
                    if (startPosX <= (MAX_X - distance) && startPosY <= (MAX_Y - distance) &&
                            (logicBoardTable[startPosX + distance][startPosY + distance] == BLACK_PAWN ||
                                    logicBoardTable[startPosX + distance][startPosY + distance] == BLACK_CROWNHEAD)) {
                        return true;
                    }
                    break;
                case SOUTH_WEST:
                    if (startPosX >= distance && startPosY <= (MAX_Y - distance) &&
                            (logicBoardTable[startPosX - distance][startPosY + distance] == BLACK_PAWN ||
                                    logicBoardTable[startPosX - distance][startPosY + distance] == BLACK_CROWNHEAD)) {
                        return true;
                    }
                    break;
            }
        }

        if (logicBoardTable[startPosX][startPosY] == BLACK_PAWN ||
                logicBoardTable[startPosX][startPosY] == BLACK_CROWNHEAD) {
            switch (direction) {
                case NORTH_WEST:
                    if (startPosX >= distance && startPosY >= distance &&
                            (logicBoardTable[startPosX - distance][startPosY - distance] == WHITE_PAWN ||
                                    logicBoardTable[startPosX - distance][startPosY - distance] == WHITE_CROWNHEAD)) {
                        return true;
                    }
                    break;
                case NORTH_EAST:
                    if (startPosX <= (MAX_X - distance) && startPosY >= distance &&
                            (logicBoardTable[startPosX + distance][startPosY - distance] == WHITE_PAWN ||
                                    logicBoardTable[startPosX + distance][startPosY - distance] == WHITE_CROWNHEAD)) {
                        return true;
                    }
                    break;
                case SOUTH_EAST:
                    if (startPosX <= (MAX_X - distance) && startPosY <= (MAX_Y - distance) &&
                            (logicBoardTable[startPosX + distance][startPosY + distance] == WHITE_PAWN ||
                                    logicBoardTable[startPosX + distance][startPosY + distance] == WHITE_CROWNHEAD)) {
                        return true;
                    }
                    break;
                case SOUTH_WEST:
                    if (startPosX >= distance && startPosY <= (MAX_Y - distance) &&
                            (logicBoardTable[startPosX - distance][startPosY + distance] == WHITE_PAWN ||
                                    logicBoardTable[startPosX - distance][startPosY + distance] == WHITE_CROWNHEAD)) {
                        return true;
                    }
                    break;
            }
        }

        return false;
    }

    public boolean isFreeFields(int startPosX, int startPosY, int direction, int distance) {

        if (distance == 0) {
            return true;
        }

        for (int i = 1; i <= distance; i++) {
            switch (direction) {
                case NORTH_WEST:
                    if (startPosX >= i && startPosY >= i &&
                            logicBoardTable[startPosX - i][startPosY - i] != FREE_FIELD) {
                        return false;
                    }
                    break;
                case NORTH_EAST:
                    if (startPosX <= (MAX_X - i) && startPosY >= i &&
                            logicBoardTable[startPosX + i][startPosY - i] != FREE_FIELD) {
                        return false;
                    }
                    break;
                case SOUTH_EAST:
                    if (startPosX <= (MAX_X - i) && startPosY <= (MAX_Y - i) &&
                            logicBoardTable[startPosX + i][startPosY + i] != FREE_FIELD) {
                        return false;
                    }
                    break;
                case SOUTH_WEST:
                    if (startPosX >= i && startPosY <= (MAX_Y - i) &&
                            logicBoardTable[startPosX - i][startPosY + i] != FREE_FIELD) {
                        return false;
                    }
                    break;
            }
        }

        return true;
    }

    public boolean isPlayerHaVeNoMove(int pawnColor) {

        List<Movement> playerMovementTable = new ArrayList<>(playerPossibleCaptures(pawnColor));
        if (playerMovementTable.size() > 0) {
            return false;
        }

        for (int j = 0; j <= MAX_Y; j++) {
            for (int i = 0; i <= MAX_X; i++) {
                if ((pawnColor == WHITE_PAWN) &&
                        (logicBoardTable[i][j] == WHITE_PAWN || logicBoardTable[i][j] == WHITE_CROWNHEAD) &&
                        (isPawnHaveFreeSpace(i, j))) {
                    return false;
                }
                if ((pawnColor == BLACK_PAWN) &&
                        (logicBoardTable[i][j] == BLACK_PAWN || logicBoardTable[i][j] == BLACK_CROWNHEAD)) {
                    if (isPawnHaveFreeSpace(i, j)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public List<Movement> computerSillyMove() {
        List<Movement> computerMove = new ArrayList<>();
        Random generator = new Random();
        int startX;
        int startY;
        int stopX;
        int stopY;
        int crossingX;
        int crossingY;
        int distance;

        int[][] tempLogicBoardTable = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                tempLogicBoardTable[i][j] = logicBoardTable[i][j];
            }
        }

        // Jeśli komputer ma bicie
        List<Movement> possibleMove = new ArrayList<>(playerPossibleCaptures(BLACK_PAWN));

        if (possibleMove.size() > 0) {
            // wybór losoweg ruchu
            computerMove.add(possibleMove.get(generator.nextInt(possibleMove.size())));
            // Sprawdzenie czy nie ma dalszego bicia przez ten pionek
            do {
                startX = computerMove.get(computerMove.size() - 1).startX;
                startY = computerMove.get(computerMove.size() - 1).startY;
                stopX = computerMove.get(computerMove.size() - 1).stopX;
                stopY = computerMove.get(computerMove.size() - 1).stopY;
                crossingX = computerMove.get(computerMove.size() - 1).crossingX;
                crossingY = computerMove.get(computerMove.size() - 1).crossingY;

                logicBoardTable[stopX][stopY] = logicBoardTable[startX][startY];
                logicBoardTable[startX][startY] = FREE_FIELD;
                logicBoardTable[crossingX][crossingY] = FREE_FIELD;

                startX = stopX;
                startY = stopY;
                possibleMove.clear();
                possibleMove.addAll(pawnPossibleCaptures(startX, startY));
                if (possibleMove.size() > 0) {
                    // wybór losoweg ruchu
                    computerMove.add(possibleMove.get(generator.nextInt(possibleMove.size())));
                }
            } while (possibleMove.size() != 0);
            logicBoardTable = tempLogicBoardTable;
            return new ArrayList<>(computerMove);
        }

        // Jeśli komputer nie ma bicia
        for (int i = 0; i <= MAX_X; i++) {
            for (int j = 0; j <= MAX_Y; j++) {
                if ((logicBoardTable[i][j] == BLACK_PAWN || logicBoardTable[i][j] == BLACK_CROWNHEAD) &&
                        (isPawnHaveFreeSpace(i, j))) {
                    startX = i;
                    startY = j;

                    if (logicBoardTable[i][j] == BLACK_CROWNHEAD) {
                        distance = MAX_X - 1;
                    } else {
                        distance = 1;
                    }

                    do {
                        // NW
                        if ((startX - distance >= 0) && (startY - distance >= 0) &&
                                isFreeFields(startX, startY, NORTH_WEST, distance)) {
                            possibleMove.add(new Movement(startX, startY,
                                    startX - distance, startY - distance, NO_CROSSING, NO_CROSSING));
                            for (Movement currentMove : possibleMove) {
                                System.out.println("NW" + currentMove);
                            }
                        }
                        // NE
                        if ((startX + distance <= MAX_X) && (startY - distance >= 0) &&
                                isFreeFields(startX, startY, NORTH_EAST, distance)) {
                            possibleMove.add(new Movement(startX, startY,
                                    startX + distance, startY - distance, NO_CROSSING, NO_CROSSING));
                            for (Movement currentMove : possibleMove) {
                                System.out.println("NW" + currentMove);
                            }
                        }
                        // SE
                        if ((logicBoardTable[startX][startY] == BLACK_CROWNHEAD) &&
                                (startX + distance <= MAX_X) && (startY + distance <= MAX_Y) &&
                                isFreeFields(startX, startY, SOUTH_EAST, distance)) {
                            possibleMove.add(new Movement(startX, startY,
                                    startX + distance, startY + distance, NO_CROSSING, NO_CROSSING));
                            for (Movement currentMove : possibleMove) {
                                System.out.println(currentMove);
                            }
                        }
                        // SW
                        if ((logicBoardTable[startX][startY] == BLACK_CROWNHEAD) &&
                                (startX - distance >= 0) && (startY + distance <= MAX_Y) &&
                                isFreeFields(startX, startY, SOUTH_WEST, distance)) {
                            possibleMove.add(new Movement(startX, startY,
                                    startX - distance, startY + distance, NO_CROSSING, NO_CROSSING));
                            for (Movement currentMove : possibleMove) {
                                System.out.println(currentMove);
                            }
                        }
                        distance--;
                    } while (distance > 0);

                }
            }
        }
        // wybór losoweg ruchu computera bez bicia
        if (possibleMove.size() > 0) {
            for (Movement currentMove : possibleMove) {
                System.out.println(currentMove);
            }
            computerMove.add(possibleMove.get(generator.nextInt(possibleMove.size())));
        }

        return new ArrayList<>(computerMove);
    }


    private void printLogicBoardTable(String description) {
        System.out.println(description);
        for (int j = 0; j < boardSize; j++) {
            for (int i = 0; i < boardSize; i++) {
                if ((logicBoardTable[i][j] > -1) && (logicBoardTable[i][j] < 10)) {
                    System.out.print(" " + logicBoardTable[i][j] + "\t");
                } else {
                    System.out.print(logicBoardTable[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

}
