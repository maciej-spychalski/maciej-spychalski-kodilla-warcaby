// Gra
// Sprawdzenie czy gra nie jest zakończona

// Gracz
// 1. Sprawdzenie czy gracz nie ma przymusowego bicia
// 2. Sprawdzenie czy gracz może wykonać dany ruch
// 3. Ponowne sprawdzenie czy nie ma przymusowego bicia (czy nie jest to bicie wielokrotne
// 4. Powtarzanie punktów 2,3 aż do zakończenia ruchu
// 5. Sprawdzenie czy pion nie zmienił się na damkę
// 6. Zmiana statusu ruchu na zakończony i przepisanie aktualnej tablicy położeń pionów do tablicy przechowującej
//    zatwierdzony stan gry

// Komputer
// 1. Sprawdzenie czy komputer nie ma przymusowego bicia
// 2. Jeżeli jest przymusowe bicie - rozpatrzenie najlepszego wyboru - algorytm min-max z uwzględnieniem
//    bicia wielokrotnego, możliwości uzyskania damki, obrony swoich pozycji, ....
// 3. Sprawdzenie wszystkich możliwych pusunięć komputera i wybranie najlepszego - algorytm min-max, z uwzględnieniem
//    bicia wielokrotnego, możliwości uzyskania damki, obrony swoich pozycji, ....
// 4. Sprawdzenie czy pion nie zmienił się na damkę
// 5. Zmiana statusu ruchu na zakończony i przepisanie aktualnej tablicy położeń pionów do tablicy przechowującej
//    zatwierdzony stan gry

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

import static java.lang.Math.*;

public class Logic {

    private int[][] logicBoardTable;
    private ArrayList<Movement> movements;
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
    private int boardSize;
    private int MAX_X;
    private int MAX_Y;

    public Logic(int boardSize) {
        this.boardSize = boardSize - 1;
        MAX_X = this.boardSize;
        MAX_Y = this.boardSize;
        logicBoardTable = new int[boardSize][boardSize];
        movements = new ArrayList<>();
    }

    public void setLogicBoardTable(int[][] logicBoardTable) {
        this.logicBoardTable = logicBoardTable;
    }

    public int[][] getLogicBoardTable() {
        return logicBoardTable;
    }

    public ArrayList<Movement> moveAllowed(int startPosX, int startPosY, int stopPosX, int stopPosY) {
//        System.out.println("\n\n***Testowanie czy dany ruch jest dozwolony***");
//        {
//            System.out.println("\nmoveAllowed: Dane wejściowe");
//            System.out.println("moveAllowed: startPosX = " + startPosX);
//            System.out.println("moveAllowed: startPosY = " + startPosY);
//            System.out.println("moveAllowed: stopPosX = " + stopPosX);
//            System.out.println("moveAllowed: stopPosY = " + stopPosY);
//            System.out.println("\nmoveAllowed: Dane wynikowe");
//        }

        movements.clear();
        int crossingX = NO_CROSSING;
        int crossingY = NO_CROSSING;

        // If final field isn't free
        if (logicBoardTable[stopPosX][stopPosY] != FREE_FIELD) {
//             System.out.println("moveAllowed: Końcowe pole jest zajęte");
            return new ArrayList<>(movements);
        }

        int pawn = logicBoardTable[startPosX][startPosY];

        int direction = -1;

        if (startPosX > stopPosX && startPosY > stopPosY) {
//             System.out.println("moveAllowed: Badany jest ruch w kierunku NW");
            direction = NORTH_WEST;
        }
        if (startPosX < stopPosX && startPosY > stopPosY) {
//             System.out.println("moveAllowed: Badany jest ruch w kierunku NE");
            direction = NORTH_EAST;
        }
        if (startPosX < stopPosX && startPosY < stopPosY) {
//             System.out.println("moveAllowed: Badany jest ruch w kierunku SE");
            direction = SOUTH_EAST;
        }
        if (startPosX > stopPosX && startPosY < stopPosY) {
//             System.out.println("moveAllowed: Badany jest ruch w kierunku SW");
            direction = SOUTH_WEST;
        }

        int distance = abs(startPosX - stopPosX);

        // Movement of pawn without capture
        if ((distance == 1) && isFreeFields(startPosX, startPosY, direction, distance)) {
            if (pawn == WHITE_PAWN && (direction == SOUTH_WEST || direction == SOUTH_EAST)) {
//                System.out.println("moveAllowed: Biały pion ma możliwość ruchu w kierunku SW lub SE\n");
                movements.add(new Movement(startPosX, startPosY, stopPosX, stopPosY, crossingX, crossingY));
//                System.out.println("movements = " + movements.size());
                return new ArrayList<>(movements);
            }
            if (pawn == BLACK_PAWN && (direction == NORTH_WEST || direction == NORTH_EAST)) {
//                 System.out.println("moveAllowed: Czarny pion ma możliwość ruchu w kierunku NW lub NE\n");
                movements.add(new Movement(startPosX, startPosY, stopPosX, stopPosY, crossingX, crossingY));
                return new ArrayList<>(movements);
            }
        }

        // Movement of pawn with capture
        if ((distance == 2) && isOpponentsPawn(startPosX, startPosY, direction, (distance - 1))) {
//             System.out.println("moveAllowed: Badany pion biały/czarny ma możliwość ruchu z biciem\n");
            movements.add(new Movement(startPosX, startPosY, stopPosX, stopPosY, crossingX, crossingY));
            return new ArrayList<>(movements);
        }

        // Movement of crownhead without capture
        if ((pawn == WHITE_CROWNHEAD || pawn == BLACK_CROWNHEAD) &
                isFreeFields(startPosX, startPosY, direction, (distance - 1))) {
//             System.out.println("moveAllowed: Badana damka biała/czarna ma możliwość ruchu bez bicia\n");
            movements.add(new Movement(startPosX, startPosY, stopPosX, stopPosY, crossingX, crossingY));
            return new ArrayList<>(movements);
        }

        // Movement of crownhead with capture
        if ((pawn == WHITE_CROWNHEAD || pawn == BLACK_CROWNHEAD) &
                isFreeFields(startPosX, startPosY, direction, (distance - 2)) &
                isOpponentsPawn(startPosX, stopPosY, direction, (distance - 1))) {
//             System.out.println("moveAllowed: Badana damka biała/czarna ma możliwość ruchu z biciem\n");
            movements.add(new Movement(startPosX, startPosY, stopPosX, stopPosY, crossingX, crossingY));
            return new ArrayList<>(movements);
        }

//         System.out.println("moveAllowed: Niedozwolony ruch pionka/damki\n");
        return new ArrayList<>(movements);
    }

    public ArrayList<Movement> pawnPossibleCaptures(int startPosX, int startPosY) {
        // Zwrócenie tablicy możliwych bijących ruchów danego piona
//         System.out.println("***Testowanie czy pion ma bicie***");
//        {
//             System.out.println("\npawnPossibleCaptures: Dane wejściowe");
//             System.out.println("pawnPossibleCaptures: startPosX = " + startPosX);
//             System.out.println("pawnPossibleCaptures: startPosY = " + startPosY);
//             System.out.println("\npawnPossibleCaptures: Dane wynikowe");
//        }
        movements.clear();
        int crossingX = NO_CROSSING;
        int crossingY = NO_CROSSING;

        boolean crownhead = false;
        if ((logicBoardTable[startPosX][startPosY] == WHITE_CROWNHEAD) ||
                (logicBoardTable[startPosX][startPosY] == BLACK_CROWNHEAD)) {
            crownhead = true;
        }

        int maxEmptyFieds = 0;
        if (crownhead) {
            maxEmptyFieds = boardSize - 2;
        }

        int freeFields = 0;
        do {
            // Sprawdzanie czy ruch wypadnie na planszy
            // && sprawdzenie czy jest po drodze pionek do zbicia
            // && sprawdzenie czy pionek zakonczy ruch na wolnym polu
            // && sprawdzenie czy między bijącym pionkiem a pionkiem bitym nie ma żadnych dodatkowych pionów
            // - dla zwykłego piona (freeFields=0) warunke jest zawsze spełniony, (freeFields > 0) - w przypadku damki

            // NORTH_WEST
            if (startPosX >= (2 + freeFields) && startPosY >= (2 + freeFields) &&
                    isOpponentsPawn(startPosX, startPosY, NORTH_WEST, (1 + freeFields)) &&
                    logicBoardTable[startPosX - (2 + freeFields)][startPosY - (2 + freeFields)] == FREE_FIELD &&
                    isFreeFields(startPosX, startPosY, NORTH_WEST, freeFields)) {
//                 System.out.println("pawnPossibleCaptures: Badany pion/damka biała czarna ma możliwośc bicia w kierunku NW");
                crossingX = startPosX - (1 + freeFields);
                crossingY = startPosY - (1 + freeFields);
                movements.add(new Movement(startPosX, startPosY,
                        startPosX - (2 + freeFields), startPosY - (2 + freeFields),
                        crossingX, crossingY));
            }

            // NORTH_EAST
            if (startPosX <= (boardSize - 2 - freeFields) && startPosY >= (2 + freeFields) &&
                    isOpponentsPawn(startPosX, startPosY, NORTH_EAST, (1 + freeFields)) &&
                    logicBoardTable[startPosX + (2 + freeFields)][startPosY - (2 + freeFields)] == FREE_FIELD &&
                    isFreeFields(startPosX, startPosY, NORTH_EAST, freeFields)) {
//                 System.out.println("pawnPossibleCaptures: Badany pion/damka biała czarna ma możliwośc bicia w kierunku NE");
                crossingX = startPosX + (1 + freeFields);
                crossingY = startPosY - (1 + freeFields);
                movements.add(new Movement(startPosX, startPosY,
                        startPosX + (2 + freeFields), startPosY - (2 + freeFields),
                        crossingX, crossingY));
            }

            // SOUTH_EAST
            if (startPosX <= (boardSize - 2 - freeFields) && startPosY <= (boardSize - 2 - freeFields) &&
                    isOpponentsPawn(startPosX, startPosY, SOUTH_EAST, (1 + freeFields)) &&
                    logicBoardTable[startPosX + (2 + freeFields)][startPosY + (2 + freeFields)] == FREE_FIELD &&
                    isFreeFields(startPosX, startPosY, SOUTH_EAST, freeFields)) {
//                 System.out.println("pawnPossibleCaptures: Badany pion/damka biała czarna ma możliwośc bicia w kierunku SE");
                crossingX = startPosX + (1 + freeFields);
                crossingY = startPosY + (1 + freeFields);
                movements.add(new Movement(startPosX, startPosY,
                        startPosX + (2 + freeFields), startPosY + (2 + freeFields),
                        crossingX, crossingY));
            }

            // SOUTH_WEST
            if (startPosX >= (2 + freeFields) && startPosY <= (boardSize - 2 - freeFields) &&
                    isOpponentsPawn(startPosX, startPosY, SOUTH_WEST, (1 + freeFields)) &&
                    logicBoardTable[startPosX - (2 + freeFields)][startPosY + (2 + freeFields)] == FREE_FIELD &&
                    isFreeFields(startPosX, startPosY, SOUTH_WEST, freeFields)) {
//                 System.out.println("pawnPossibleCaptures: Badany pion/damka biała czarna ma możliwośc bicia w kierunku SW");
                crossingX = startPosX - (1 + freeFields);
                crossingY = startPosY + (1 + freeFields);
                movements.add(new Movement(startPosX, startPosY, startPosX - (2 + freeFields), startPosY + (2 + freeFields),
                        crossingX, crossingY));
            }

            freeFields++;
        } while (freeFields <= maxEmptyFieds);

//        if (movements.size() > 0) {
//             System.out.println("pawnPossibleCaptures: Badany pion na pozycji x=" + startPosX + " y=" + startPosY +
//                     " ma " + movements.size() + " bicie\n");
//        }
//        if (movements.size() == 0) {
//             System.out.println("pawnPossibleCaptures: Badany pion na pozycji x=" + startPosX + " y=" + startPosY +
//                     " nie ma bicie\n");
//        }

        return new ArrayList<>(movements);
    }

    public ArrayList<Movement> playerPossibleCaptures(int pawnColor) {
        // System.out.println("***Sprawdzenie czy gracz ma jakiekolwiek bicie***");
        {
            // System.out.println("\nplayerPossibleCaptures: Dane wejściowe");
            //if (pawnColor == WHITE_PAWN) // System.out.println("playerPossibleCaptures: Badanie białych pionów");
            //if (pawnColor == BLACK_PAWN) // System.out.println("playerPossibleCaptures: Badanie czarnych pionów");
            // System.out.println("\nplayerPossibleCaptures: Dane wynikowe");
        }

        movements.clear();

        int pawnCrownheadColor;
        if (pawnColor == WHITE_PAWN) {
            pawnCrownheadColor = WHITE_CROWNHEAD;
        } else {
            pawnCrownheadColor = BLACK_CROWNHEAD;
        }

        for (int j = 0; j <= MAX_Y; j++) {
            for (int i = 0; i <= MAX_X; i++) {
                if (logicBoardTable[i][j] == pawnColor || logicBoardTable[i][j] == pawnCrownheadColor) {
                    movements.addAll(pawnPossibleCaptures(i, j));
                }
            }
        }
//         System.out.println("playerPossibleCaptures: Gracz ma " + possibleCapturesTable.size() + " bicia\n");
        return new ArrayList<>(movements);
    }

    public boolean isOtherPawnMustBeMove(int pawnColor, int startPosX, int startPosY) {
//         System.out.println("***Sprawdzenie czy inny pion nie musi byc przesunięty***");
//        {
//             System.out.println("\nisOtherPawnMustBeMove: Dane wejściowe");
//             if (pawnColor == WHITE_PAWN) // System.out.println("isOtherPawnMustBeMove: Badanie białych pionów");
//             if (pawnColor == BLACK_PAWN) // System.out.println("isOtherPawnMustBeMove: Badanie czarnych pionów");
//             System.out.println("isOtherPawnMustBeMove: startPosX = " + startPosX);
//             System.out.println("isOtherPawnMustBeMove: startPosY = " + startPosY);
//             System.out.println("\nisOtherPawnMustBeMove: Dane wynikowe");
//        }

        movements.clear();

        boolean otherPawnMustBeMove = true;
        // spradzenie czy jakikolwiek pion o dany kolorze ma bicie ma bicie
        movements.addAll(playerPossibleCaptures(pawnColor));
        if ((movements.size() == 0) && isPawnHaveFreeSpace(startPosX, startPosY)) {
            otherPawnMustBeMove = false;
//             System.out.println("isOtherPawnMustBeMove: Pion może być przesunięty bez bicia\n");
            return otherPawnMustBeMove;
        } else {
            // sprawdzenie czy wybrany pion ma bicie
            movements.addAll(pawnPossibleCaptures(startPosX, startPosY));
            if (movements.size() > 0) {
//                 System.out.println("isOtherPawnMustBeMove: Pion może być przesunięty z biciem\n");
                otherPawnMustBeMove = false;
                return otherPawnMustBeMove;
            }
        }
        // System.out.println("isOtherPawnMustBeMove: Inny pion musi być przesunięty\n");
        return otherPawnMustBeMove;
    }

    public boolean isPawnHaveFreeSpace(int startPosX, int startPosY) {
        // System.out.println("***Sprawdzenie czy pion ma wolna przestrzen do ruchu***");
        {
            // System.out.println("\nisPawnHaveFreeSpace: Dane wejściowe");
            // System.out.println("isPawnHaveFreeSpace: startPosX = " + startPosX);
            // System.out.println("isPawnHaveFreeSpace: startPosY = " + startPosY);
            // System.out.println("\nisPawnHaveFreeSpace: Dane wynikowe");
        }

        boolean pawnHaveFreeSpace = false;
        int pawn = logicBoardTable[startPosX][startPosY];

        // Sprawdzanie czy ruch wypadnie na planszy
        // && sprawdzenie czy pionek zakonczy ruch na wolnym polu w dozwolonym dla niego kierunku

        // NORTH_WEST
        if ((startPosX >= 1) && (startPosY >= 1) &&
                (pawn == BLACK_PAWN || pawn == WHITE_CROWNHEAD || pawn == BLACK_CROWNHEAD) &&
                isFreeFields(startPosX, startPosY, NORTH_WEST, 1)) {
            pawnHaveFreeSpace = true;
            // System.out.println("isPawnHaveFreeSpace: Pion/damka biała/czarna ma wolne pole na kierunku NW\n");
            return pawnHaveFreeSpace;
        }

        // NORTH_EAST
        if ((startPosX <= (boardSize - 1)) && (startPosY >= 1) &&
                (pawn == BLACK_PAWN || pawn == WHITE_CROWNHEAD || pawn == BLACK_CROWNHEAD) &&
                isFreeFields(startPosX, startPosY, NORTH_EAST, 1)) {
            pawnHaveFreeSpace = true;
            // System.out.println("isPawnHaveFreeSpace: Pion/damka biała/czarna ma wolne pole na kierunku NE\n");
            return pawnHaveFreeSpace;
        }

        // SOUTH_EAST
        if ((startPosX <= (boardSize - 1)) && (startPosY <= (boardSize - 1)) &&
                (pawn == WHITE_PAWN || pawn == WHITE_CROWNHEAD || pawn == BLACK_CROWNHEAD) &&
                isFreeFields(startPosX, startPosY, SOUTH_EAST, 1)) {
            pawnHaveFreeSpace = true;
            // System.out.println("isPawnHaveFreeSpace: Pion/damka biała/czarna ma wolne pole na kierunku SE\n");
            return pawnHaveFreeSpace;
        }

        // SOUTH_WEST
        if ((startPosX >= 1) && (startPosY <= (boardSize - 1)) &&
                (pawn == WHITE_PAWN || pawn == WHITE_CROWNHEAD || pawn == BLACK_CROWNHEAD) &&
                isFreeFields(startPosX, startPosY, SOUTH_WEST, 1)) {
            pawnHaveFreeSpace = true;
            // System.out.println("isPawnHaveFreeSpace: Pion/damka biała/czarna ma wolne pole na kierunku SW\n");
            return pawnHaveFreeSpace;
        }

        return pawnHaveFreeSpace;
    }

    public boolean isOpponentsPawn(int startPosX, int startPosY, int direction, int distance) {
//        System.out.println("***Testowanie czy na danym polu jest pion przeciwnika***");
//        {
//            System.out.println("\nisOpponentsPawn: Dane wejściowe");
//            System.out.println("isOpponentsPawn: startPosX = " + startPosX);
//            System.out.println("isOpponentsPawn: startPosY = " + startPosY);
//            if (direction == NORTH_WEST) {
//                System.out.println("isOpponentsPawn: kierunek NW");
//            }
//            if (direction == NORTH_EAST) {
//                System.out.println("isOpponentsPawn: kierunek NE");
//            }
//            if (direction == SOUTH_EAST) {
//                System.out.println("isOpponentsPawn: kierunek SE");
//            }
//            if (direction == SOUTH_WEST) {
//                System.out.println("isOpponentsPawn: kierunek SW");
//            }
//            System.out.println("isOpponentsPawn: distance = " + distance);
//            System.out.println("\nisOpponentsPawn: Dane wynikowe");
//        }

        boolean opponetsPawn = false;
        if (distance == 0) {
//             System.out.println("isOpponentsPawn: Nie ma na tym polu pionka przeciwnika bo distance = 0\n");
            return opponetsPawn;
        }

        if (logicBoardTable[startPosX][startPosY] == WHITE_PAWN ||
                logicBoardTable[startPosX][startPosY] == WHITE_CROWNHEAD) {
            switch (direction) {
                case NORTH_WEST:
                    if (startPosX >= distance && startPosY >= distance &&
                            (logicBoardTable[startPosX - distance][startPosY - distance] == BLACK_PAWN ||
                                    logicBoardTable[startPosX - distance][startPosY - distance] == BLACK_CROWNHEAD)) {
//                         System.out.println("isOpponentsPawn: Na pozycji NW stoi czarny pionek przeciwnika");
                        opponetsPawn = true;
                    }
                    break;
                case NORTH_EAST:
                    if (startPosX <= (MAX_X - distance) && startPosY >= distance &&
                            (logicBoardTable[startPosX + distance][startPosY - distance] == BLACK_PAWN ||
                                    logicBoardTable[startPosX + distance][startPosY - distance] == BLACK_CROWNHEAD)) {
//                         System.out.println("isOpponentsPawn: Na pozycji NE stoi czarny pionek przeciwnika");
                        opponetsPawn = true;
                    }
                    break;
                case SOUTH_EAST:
                    if (startPosX <= (MAX_X - distance) && startPosY <= (MAX_Y - distance) &&
                            (logicBoardTable[startPosX + distance][startPosY + distance] == BLACK_PAWN ||
                                    logicBoardTable[startPosX + distance][startPosY + distance] == BLACK_CROWNHEAD)) {
//                         System.out.println("isOpponentsPawn: Na pozycji SE stoi czarny pionek przeciwnika");
                        opponetsPawn = true;
                    }
                    break;
                case SOUTH_WEST:
                    if (startPosX >= distance && startPosY <= (MAX_Y - distance) &&
                            (logicBoardTable[startPosX - distance][startPosY + distance] == BLACK_PAWN ||
                                    logicBoardTable[startPosX - distance][startPosY + distance] == BLACK_CROWNHEAD)) {
//                         System.out.println("isOpponentsPawn: Na pozycji SW stoi czarny pionek przeciwnika");
                        opponetsPawn = true;
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
//                         System.out.println("isOpponentsPawn: Na pozycji NW stoi biały pionek przeciwnika");
                        opponetsPawn = true;
                    }
                    break;
                case NORTH_EAST:
                    if (startPosX <= (MAX_X - distance) && startPosY >= distance &&
                            (logicBoardTable[startPosX + distance][startPosY - distance] == WHITE_PAWN ||
                                    logicBoardTable[startPosX + distance][startPosY - distance] == WHITE_CROWNHEAD)) {
//                         System.out.println("isOpponentsPawn: Na pozycji NE stoi biały pionek przeciwnika");
                        opponetsPawn = true;
                    }
                    break;
                case SOUTH_EAST:
                    if (startPosX <= (MAX_X - distance) && startPosY <= (MAX_Y - distance) &&
                            (logicBoardTable[startPosX + distance][startPosY + distance] == WHITE_PAWN ||
                                    logicBoardTable[startPosX + distance][startPosY + distance] == WHITE_CROWNHEAD)) {
//                         System.out.println("isOpponentsPawn: Na pozycji SE stoi biały pionek przeciwnika");
                        opponetsPawn = true;
                    }
                    break;
                case SOUTH_WEST:
                    if (startPosX >= distance && startPosY <= (MAX_Y - distance) &&
                            (logicBoardTable[startPosX - distance][startPosY + distance] == WHITE_PAWN ||
                                    logicBoardTable[startPosX - distance][startPosY + distance] == WHITE_CROWNHEAD)) {
//                         System.out.println("isOpponentsPawn: Na pozycji SW stoi biały pionek przeciwnika");
                        opponetsPawn = true;
                    }
                    break;
            }
        }
//         System.out.println("isOpponentsPawn: Pionek przeciwnika na badanym polu: " + opponetsPawn + "\n");
        return opponetsPawn;
    }

    public boolean isFreeFields(int startPosX, int startPosY, int direction, int distance) {
//        System.out.println("***Testowanie warunku wolnego pola***");
//        {
//            System.out.println("\nisFreeFields: Dane wejściowe");
//            System.out.println("isFreeFields: startPosX = " + startPosX);
//            System.out.println("isFreeFields: startPosY = " + startPosY);
//            if (direction == NORTH_WEST) {
//                System.out.println("isFreeFields: kierunek NW");
//            }
//            if (direction == NORTH_EAST) {
//                System.out.println("isFreeFields: kierunek NE");
//            }
//            if (direction == SOUTH_EAST) {
//                System.out.println("isFreeFields: kierunek SE");
//            }
//            if (direction == SOUTH_WEST) {
//                System.out.println("isFreeFields: kierunek SW");
//            }
//            System.out.println("isFreeFields: distance = " + distance);
//            System.out.println("\nisOpponentsPawn: Dane wynikowe");
//        }


        boolean freeFields = true;
        if (distance == 0) {
//             System.out.println("isFreeFields: Warunek wolnego pola spełniony bo distance = 0\n");
            return freeFields;
        }

        for (int i = 1; i <= distance; i++) {
            switch (direction) {
                case NORTH_WEST:
                    if (startPosX >= i && startPosY >= i &&
                            logicBoardTable[startPosX - i][startPosY - i] != FREE_FIELD) {
//                         System.out.println("isFreeFields: Brak wolnego pola na pozycji NW\n");
                        freeFields = false;
                    }
                    break;
                case NORTH_EAST:
                    if (startPosX <= (MAX_X - i) && startPosY >= i &&
                            logicBoardTable[startPosX + i][startPosY - i] != FREE_FIELD) {
//                         System.out.println("isFreeFields: Brak wolnego pola na pozycji NE\n");
                        freeFields = false;
                    }
                    break;
                case SOUTH_EAST:
                    if (startPosX <= (MAX_X - i) && startPosY <= (MAX_Y - i) &&
                            logicBoardTable[startPosX + i][startPosY + i] != FREE_FIELD) {
//                         System.out.println("isFreeFields: Brak wolnego pola na pozycji SE\n");
                        freeFields = false;
                    }
                    break;
                case SOUTH_WEST:
                    if (startPosX >= i && startPosY <= (MAX_Y - i) &&
                            logicBoardTable[startPosX - i][startPosY + i] != FREE_FIELD) {
//                         System.out.println("isFreeFields: Brak wolnego pola na pozycji SW\n");
                        freeFields = false;
                    }
                    break;
            }
        }

//        if (direction == NORTH_WEST && !freeFields) {
//             System.out.println("isFreeFields: Wolne pole na pozycji NW\n");
//        }
//        if (direction == NORTH_EAST && !freeFields) {
//             System.out.println("isFreeFields: Wolne pole na pozycji NE\n");
//        }
//        if (direction == SOUTH_EAST && !freeFields) {
//             System.out.println("isFreeFields: Wolne pole na pozycji SE\n");
//        }
//        if (direction == SOUTH_WEST && !freeFields) {
//             System.out.println("isFreeFields: Wolne pole na pozycji SW\n");
//        }

        return freeFields;
    }

    public ArrayList<Movement> computerMove() {
        movements.clear();
//        int crossingX = NO_CROSSING;
//        int crossingY = NO_CROSSING;
        return new ArrayList<>(movements);
    }

}
