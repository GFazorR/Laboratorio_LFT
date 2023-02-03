public class es1p5{

  /*
  Esercizio 1.5. Progettare e implementare un DFA che, come in Esercizio 1.3, riconosca il linguaggio
  di stringhe che contengono matricola e cognome di studenti del turno 2 o del turno 3 del
  laboratorio, ma in cui 
    
    il cognome precede il numero di matricola 
    
    (in altre parole, le posizioni del
  cognome e matricola sono scambiate rispetto all’Esercizio 1.3). Assicurarsi che il DFA sia minimo.
  */
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {

            //Stato iniziale
            case 0:
                if (ch>='A' && ch<='K')
                    state = 1;
                else if (ch>='L' && ch<='Z')
                    state = 2;
                    else state=-1;
                break;

            //Cognomi con A/K
            case 1:
                if (ch>='a' && ch<='z')
                    state = 1;
                else if (ch=='0' || ch=='2' || ch=='4' || ch=='6' || ch=='8')
                    state = 3;
                    else if (ch=='1' || ch=='3' || ch=='5' || ch=='7' || ch=='9')
                            state=4;
                break;

            //Cognomi con L/Z
            case 2:
                if (ch>='a' && ch<='z')
                    state = 2;
                else if (ch=='0' || ch=='2' || ch=='4' || ch=='6' || ch=='8')
                    state = 5;
                    else if (ch=='1' || ch=='3' || ch=='5' || ch=='7' || ch=='9')
                            state=6;
                break;

            //Accettazione numero pari dopo cognomi con A/K
            case 3:
                if (ch=='0' || ch=='2' || ch=='4' || ch=='6' || ch=='8')
                    state=3;
                else if (ch=='1' || ch=='3' || ch=='5' || ch=='7' || ch=='9')
                        state=4;
                    else state=-1;
                break;

            //Numeri dispari dopo cognomi con A/K
            case 4:
                if (ch=='0' || ch=='2' || ch=='4' || ch=='6' || ch=='8')
                    state=3;
                else if (ch=='1' || ch=='3' || ch=='5' || ch=='7' || ch=='9')
                        state=4;
                    else state=-1;
                break;

            //Numeri pari dopo cognomi con L/Z
            case 5:
                if (ch=='0' || ch=='2' || ch=='4' || ch=='6' || ch=='8')
                    state=5;
                else if (ch=='1' || ch=='3' || ch=='5' || ch=='7' || ch=='9')
                        state=6;
                    else state=-1;
                break;

            //Accettazione numero dispari dopo cognomi con L/Z
            case 6:
                if (ch=='0' || ch=='2' || ch=='4' || ch=='6' || ch=='8')
                    state=5;
                else if (ch=='1' || ch=='3' || ch=='5' || ch=='7' || ch=='9')
                        state=6;
                    else state=-1;
                break;
            }
        }
    return (state ==3 || state==6);
    }

public static void main(String[] args){
    System.out.println(scan(args[0]) ? "OK" : "NOPE");
}
}
