public class es1p2 {

  /*
  Esercizio 1.2. Progettare e implementare un DFA che riconosca il linguaggio degli identificatori
  in un linguaggio in stile Java: 
    
    un identificatore e una sequenza non vuota di lettere, numeri, ed il simbolo di underscore "_" che non comincia con un numero e che non puo essere composto solo `
  dal simbolo _. 
    
    Compilare e testare il suo funzionamento su un insieme significativo di esempi.
  */

    public static boolean scan (String s){
        int state=0;
        int i=0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state){

                // stato iniziale
                case 0:
                    if (ch=='_')
                        state=1;
                    else if (Character.isLetter(ch))
                        state=2;
                    else state=-1;
                break;

                // loop degli underscore
                case 1:
                    if (ch=='_')
                        state=1;
                    else if (Character.isLetter(ch) || ch>='0'|| ch<='9')
                        state = 2;
                    else state = -1;
                break;

                // stato finale
                case 2:
                    if(Character.isLetter(ch) || ch>='0'|| ch<='9' || ch=='_')
                      state=2;
                    else state =-1;
            }
        }
        return (state==2);
    }

    public static void main(String[] args){
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
