public class Game {
    private Pawn[] pawnsWhite = new Pawn[8];
    private Pawn[] pawnsBlack = new Pawn[8];

    public Game() {
        int pawnWhite = 0;
        int pawnBlack = 0;
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                if ((i + j) % 2 != 0) {
                    pawnsWhite[pawnWhite] = new Pawn(true, false, i, j);
                    pawnWhite++;
                } else {
                    pawnsBlack[pawnBlack] = new Pawn(true, false, i, j);
                    pawnBlack++;
                }
            }
        }
    }
}
