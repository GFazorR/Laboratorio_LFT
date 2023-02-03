public class es1p7{

  // Esercizio 1.7. Progettare e implementare un DFA con alfabeto {/, *, a} che riconosca il linguaggio
  // di "commenti" delimitati da /* (all'inizio) e */ (alla fine): cioe l'automa deve accettare le ´
  // stringhe che contengono almeno 4 caratteri che iniziano con /*, che finiscono con */, e che contengono
  // una sola occorrenza della sequenza */, quella finale (dove l'asterisco della sequenza */
  // non deve essere in comune con quello della sequenza /* all'inizio, ). Quindi l'automa deve accettare
  // le stringhe "/****/", "/*a*a*/", "/*a/**/", "/**a///a/a**/", "/**/" e "/*/*/"
  // ma non "/*/", oppure "/**/***/".

    public static boolean scan (String s){
        int state=0;
        int i=0;
        while (state>=0 && i<s.length()){
            final char ch= s.charAt(i++);
            switch (state){

                //Stato iniziale: inizia il commento
                case 0:
                    if (ch=='/')
                        state=1;
                    else state=-1;
                    break;

                //Inizio del commento ("*" forzato dopo il primo "/")
                case 1:
                    if (ch=='*')
                        state=2;
                    else state=-1;
                    break;

                //Commento composto da "a" e "/", "*" controlla l'eventuale fine del commento
                case 2:
                    if (ch=='*')
                        state=3;
                    else if (ch=='a' || ch=='/')
                        state=2;
                    else state = -1;
                    break;

                //Loop di "*" che possono precedere la "/" finale
                case 3:
                    if (ch=='*')
                        state=3;
                    else if (ch=='a')
                        state=2;
                    else if (ch=='/')
                        state=4;
                    else state = -1;
                    break;

                //Stato di accettazione
                case 4:
                    state = -1;
                    break;
            }

        }
        return (state==4);
    }

    public static void main (String[]args){
            System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
