public class es1p1{

  /*    
  Esercizio 1.1. Copiare il codice in Figura 2, compilarlo e testarlo su un insieme significativo di
  stringhe, per es. "010101", "1100011001", "10214", ecc.
  Come deve essere modificato il DFA in Figura 1 per riconoscere il linguaggio complementare,
  ovvero il linguaggio delle 
    
   stringhe di 0 e 1 che non contengono 3 zeri consecutivi
    
   ? Progettare e implementare il DFA modificato, e testare il suo funzionamento.
  */

    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
            case 0:
                if (ch == '0')
                    state = 1;
                else if (ch == '1')
                    state = 0;
                else
                    state = -1;
                break;
            case 1:
                if (ch == '0')
                    state = 2;
                else if (ch == '1')
                    state = 0;
                else
                    state = -1;
                break;
            case 2:
                if (ch == '0')
                    state = 3;
                else if (ch == '1')
                    state = 0;
                else
                    state = -1;
                break;
            case 3:
                if (ch == '0' || ch == '1')
                    state = 3;
            else
                state = -1;
            break;
            }
        }
    return (state != 3 && state !=-1);
    }

    public static void main(String[] args){
      System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
