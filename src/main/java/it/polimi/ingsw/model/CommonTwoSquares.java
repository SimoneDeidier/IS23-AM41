package it.polimi.ingsw.model;

public class CommonTwoSquares implements CommonTargetCard {
    @Override
    public boolean check(Item[][] shelf) {

        boolean[][] bitMask = new boolean[6][5]; //è inizializzato a false
        Item tipoQuadrato;
        int Quadrati = 0;

        for( int row = 0; row < 5; row++ ){
            for( int col = 0; col < 4; col ++) {
                if (!bitMask[row][col]) {
                    tipoQuadrato = shelf[row][col];

                    if (shelf[row][col + 1] != null && shelf[row + 1][col] != null && shelf[row + 1][col + 1] != null &&
                            tipoQuadrato.getType() == shelf[row    ][col + 1].getType() &&
                            tipoQuadrato.getType() == shelf[row + 1][col    ].getType() &&
                            tipoQuadrato.getType() == shelf[row + 1][col + 1].getType()) {
                        //il controllo funziona così: item a destra, item sotto, item in diagonale
                        //allora ho trovato un quadrato

                        //segnamo i 4 posti del quadrato sulla bitmask

                        bitMask[row    ][col    ] = true;
                        bitMask[row    ][col + 1] = true;
                        bitMask[row + 1][col    ] = true;
                        bitMask[row + 1][col + 1] = true;




                        Quadrati++;
                        if (Quadrati == 2)
                            return true;
                    }

                }
            }
        }




        return false;
    }
}
