public class es1p6{

  /*
  Esercizio 1.6. Progettare e implementare un DFA che riconosca il linguaggio dei numeri binari
  
    (stringhe di 0 e 1) il cui valore e multiplo di 3.
    
  Per esempio, " 110" e "1001" sono stringhe del
  linguaggio (rappresentano rispettivamente i numeri 6 e 9), mentre "10" e "111" no (rappresentano
  rispettivamente i numeri 2 e 7).
  Suggerimento: usare tre stati per rappresentare il resto della divisione per 3 del numero.
  */

    //mi sposto in base a modulo 3
    
    public static boolean scan (String s){
        int state=0;
        int i=0;
        while (state>=0 && i<s.length()){
            final char ch= s.charAt(i++);
            switch (state){

                // Stato iniziale n=0
                case 0:
                    if (ch=='1')
                        state=1;
                    else if (ch=='0')
                            state=0;
                        else state=-1;
                    break;

                // Resto 1 mod 3
                case 1:
                    if (ch=='1')
                        state=3;
                    else if (ch=='0')
                            state=2;
                        else state=-1;
                    break;

                // Resto 2 mod 3
                case 2:
                    if (ch=='1')
                        state=2;
                    else if (ch=='0')
                            state=1;
                        else state=-1;
                    break;

                //Resto 3 mod 3 (resto 0) Accettazione
                case 3:
                    if (ch=='1')
                        state=1;
                    else if (ch=='0')
                            state=3;
                        else state=-1;
                    break;
            }
        }
        return (state==3);
    }

    public static void main (String[]args){
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
