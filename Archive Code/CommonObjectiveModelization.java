/* Opzione modellizzazione delle carte, non esiste una funzione matematica
che metta in correlazione le diverse carte, l’unica cosa che possiamo fare
è controllare tramite codice se l’utente ha completato qualche obiettivo.
Ci dovrebbero essere delle modalità per escludere a priori un utente dalla 
realizzazione di un determinato obiettivo comune se non soddisfa ad un certo
punto della partita dei requisiti minimi, cercherò di indicarlo qui il meglio possibile.s

Caratteristiche iniziali con le quali lavoriamo:
Ogni shelf è una matrice 5x6.
Ogni tessera si distingue per: colore, tipo
*/

/* IPOTESI DI CARTA CON ENUM (approssimativa e con molte caratteristiche non tenute 
in considerazione, ma per rendere l'idea) */

public class CommonObjective {
    enum objectives {
      one,
      two,
      three,
      four,
      five,
      six,
      seven,
      eight,
      nine,
      ten,
      eleven,
      twelve
    }
  
    public static void check(String[] args) {
      objectives nextObj = objectives.three; 

    switch(nextObj) {
        case one:
            if(checkObb1(matrix[][])){
                //assign token
            }
            break;
        case two:
            if(checkObb1(matrix[][])){
                //assign token
            }
            break;
        case three:
            if(checkObb1(matrix[][])){
                //assign token
            }
            break;
        //eccetera eccetera

    }

    }

    public static boolean checkObb1(int[][] matrix) {
        //code
    }
    public static boolean checkObb2(int[][] matrix) {
        //code
    }
    public static boolean checkObb3(int[][] matrix) {
        //code
    }
  }

//CARTA 1:

public static boolean checkObb1(int[][] matrix) {
    int numGroups = 0;
    boolean[][] visited = new boolean[6][5];
    for (int i = 0; i < 6; i++) {
        for (int j = 0; j < 5; j++) {
            if (!visited[i][j]) {
                int val = matrix[i][j];
                boolean foundGroup = false;
                // check horizontal group
                if (j < 4 && matrix[i][j+1] == val) {
                    numGroups++;
                    foundGroup = true;
                    visited[i][j] = true;
                    visited[i][j+1] = true;
                    if(i<5) {
                        visited[i+1][j] = true;
                        visited[i+1][j+1] = true;
                        if(j<3)
                            visited[i+1][j+2] = true;
                        if(j>0)
                            visited[i+1][j-1] = true;
                    }
                    if(j<3)
                        visited[i][j+2] = true;
                    if(j>0)
                        visited[i][j-1] = true;
                    if(i>0) {
                        visited[i-1][j] = true;
                        visited[i-1][j+1] = true;
                        if(j<3)
                            visited[i-1][j+2] = true;
                        if(j>0)
                            visited[i-1][j-1] = true;
                    }
                }
                // check vertical group
                if (i < 5 && matrix[i+1][j] == val && foundGroup == false) {
                    numGroups++;
                    foundGroup = true;
                    visited[i][j] = true;
                    visited[i+1][j] = true;
                    if(j<4) {
                        visited[i][j+1] = true;
                        visited[i+1][j+1] = true;
                        if(i<5)
                            visited[i+2][j+1] = true;
                        if(i>0)
                            visited[i-1][j+1] = true;
                    }
                    if(i<5)
                        visited[i+2][j] = true;
                    if(i>0)
                        visited[i-1][j] = true;
                    if(j>0) {
                        visited[i][j-1] = true;
                        visited[i+1][j-1] = true;
                    if(i<5)
                        visited[i+2][j-1] = true;
                    if(i>0)
                        visited[i-1][j-11] = true;
	                }
                }
                if (foundGroup && numGroups == 6) {
                    return true; // found all groups
                }
            }
        }
    }
    return false; // didn't find enough groups
}


//CARTA 2:

public static boolean checkObb2(int[][] matrix) {
	int unfound = 0;
	for (int k = 0; k <=1; k++){
		for(int i = k; i < k+5; i++)
            for (int j = 0; j < 4; j++)
                if(matrix[i][j]!=matrix[i+1][j+1]){
                    unfound = 1;
                    break;
                }
        if (unfound==0){
            return true;
        }
        unfound = 0;
    }
    for (int k = 0; k <=1; k++){
        for(int i = k; i < k+5; i++) 
            for (int j = 4; j >0; j--)
                if(matrix[i][j]!=matrix[i+1][j-1]){
                    unfound = 1;
                    break;
                }
        if (unfound==0){
            return true;
        }
        unfound = 0;
    }
    return false;
}

//CARTA 3:

public static boolean checkObb3(int[][] matrix) {
    if(matrix[0][0]==matrix[0][4]==matrix[5][4]==matrix[5][0])
        return true;
    return false;
}

//CARTA 4:

public static boolean checkObb4(int[][]matrix){
	return true;
}

//CARTA 5:

public static boolean checkObb4(int[][]matrix){
	return true;
}

//CARTA 6:

public static boolean checkObb4(int[][]matrix){
	return true;
}

//CARTA 7:

public static boolean checkObb4(int[][]matrix){
	return true;
}

//CARTA 8:

public static boolean checkObb4(int[][]matrix){
	return true;
}

//CARTA 9:

public static boolean checkObb4(int[][]matrix){
	return true;
}

//CARTA 10:

public static boolean checkObb4(int[][]matrix){
	return true;
}

//CARTA 11:

public static boolean checkObb4(int[][]matrix){
	return true;
}

//CARTA 12:

public static boolean checkObb4(int[][]matrix){
	return true;
}
