public class es1p8{

  // Esercizio 1.8. Modificare l'automa dell'esercizio precedente in modo che riconosca il linguaggio
  // di stringhe (sull'alfabeto {/, *, a}) che contengono
    
    //"commenti" delimitati da /* e */, ma con la possibilita di avere stringhe prima e dopo 
    
    //come specificato qui di seguito. L'idea ` e che sia `
  // possibile avere eventualmente commenti (anche multipli) immersi in una sequenza di simboli
  // dell'alfabeto. Quindi l'unico vincolo e che l'automa deve accettare le stringhe in cui un'occorren- `
  // za della sequenza /* deve essere seguita (anche non immediatamente) da un'occorrenza della
  // sequenza */. Le stringhe del linguaggio possono non avere nessuna occorrenza della sequenza
  // /* (caso della sequenza di simboli senza commenti). Ad esempio, il DFA deve accettare le stringhe
  // "aaa/****/aa", "aa/*a*a*/", "aaaa", "/****/", "/*aa*/", "*/a", "a/**/***a",
  // "a/**/***/a" e "a/**/aa/***/a", ma non "aaa/*/aa" oppure "aa/*aa". Implementare
  // l'automa seguendo la costruzione vista in Figura 2.

    public static boolean scan (String s){
        int state=0;
        int i=0;
        while (state>=0 && i<s.length()){
            final char ch= s.charAt(i++);
            switch (state){

              //Stato iniziale: inizia il commento oppure una stringa
              //accettazione di una stringa singola oppure dopo un commento
              case 0:
                  if (ch=='/')
                      state = 1;
                  else if (ch == 'a' || ch == '*')
                      state = 0;
                  else state=-1;
                  break;

              //Inizio del commento oppure continuazione di stringa ("*" forzato dopo il primo "/")
              //accettazione di una stringa singola oppure dopo un commento
              case 1:
                  if (ch=='*')
                      state=2;
                  else if(ch=='/')
                      state=1;
                  else if(ch=='a')
                       state=0;
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

              //Stato di accettazione per stringhe che finiscono con un commento
              case 4:
                  if (ch=='a' || ch=='*')
                    state = 0;
                  else if(ch=='/')
                    state = 1;
                  else state = -1;
                  break;
            }

        }
        return (state==0 || state==1 || state == 4);
    }

    public static void main (String[]args){
            System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
