public class es1p4{

  /*
  Esercizio 1.4. Modificare l’automa dell’esercizio precedente in modo che riconosca le combinazioni
  di matricola e cognome di studenti del turno 2 o del turno 3 del laboratorio, dove il numero
  di matricola e il cognome possono essere separati da una sequenza di spazi, e possono essere
  precedute e/o seguite da sequenze eventualmente vuote di spazi. Per esempio, l’automa deve
  accettare la stringa "654321 Rossi" e " 123456 Bianchi " (dove, nel secondo esempio, ci
  sono spazi prima del primo carattere e dopo l’ultimo carattere), ma non "1234 56Bianchi" e
  "123456Bia nchi". Per questo esercizio, i cognomi composti (con un numero arbitrario di parti)
  possono essere accettati: per esempio, la stringa "123456De Gasperi" deve essere accettato.
  Modificare l’implementazione Java dell’automa di conseguenza.
  */

    public static boolean scan (String s){
        int state=0;
        int i=0;
        while (state >=0 && i<s.length()){
            final char ch = s.charAt(i++);
            switch (state){
                // stato iniziale
                case 0:
                    if (ch==' ')
                        state=0;
                    else if (ch=='0' || ch=='2' || ch=='4' || ch=='6' || ch=='8')
                            state=1;
                        else if (ch=='1' || ch=='3' || ch=='5' || ch=='7' || ch=='9')
                                state=2;
                            else state=-1;
                    break;

                //Matricola pari
                case 1:
                    if (ch=='0' || ch=='2' || ch=='4' || ch=='6' || ch=='8')
                        state=1;
                    else if (ch=='1' || ch=='3' || ch=='5' || ch=='7' || ch=='9')
                            state=2;
                        else if (ch==' ')
                            state=3;
                            else if ((Character.isLetter(ch)) && (ch>='A' && ch<='K'))
                                    state=4;
                                    else state=-1;
                    break;

                //Matricola dispari
                case 2:
                    if (ch=='0' || ch=='2' || ch=='4' || ch=='6' || ch=='8')
                        state=1;
                    else if (ch=='1' || ch=='3' || ch=='5' || ch=='7' || ch=='9')
                            state=2;
                        else if (ch==' ')
                            state=6;
                                else if ((Character.isLetter(ch)) && ch>='L' && ch<='Z')
                                        state=7;
                                    else state=-1;
                    break;

                //Spazio vuoto dopo matricola pari
                case 3:
                    if (ch==' ')
                        state=3;
                    else if ((Character.isLetter(ch)) && (ch>='A' && ch<='K'))
                            state=4;
                              else state=-1;
                    break;

                //Spazio vuoto dopo matricola dispari
                case 6:
                    if (ch==' ')
                        state=6;
                         else if ((Character.isLetter(ch)) && ch>='L' && ch<='Z')
                                state=7;
                               else state=-1;
                    break;

                //Accettazione turno 3
				        case 7:
					           if (ch==' ')
						               state=9;
					           else if (ch>='a'&& ch<='z')
						               state=7;
					           else state=-1;
					      break;

                //Accettazione turno 2
        				case 4:
        					if(ch==' ')
        						state=9;
        					else if (ch>='a'&& ch<='z')
        						state=4;
        					else state=-1;
                break;

                //Accettazione con spazio dopo il cognome
        				case 9:
        					if(ch==' ')
        						state=9;
        					else if (ch>='A'&& ch<='Z')
        						state=10;
        					else state=-1;
                break;

                //Accettazione cognome composto
                case 10:
                  if (ch>='a'&& ch<='z')
                    state=10;
                  else if(ch==' ')
                    state=9;
                  else state = -1;
                break;
          }
        }
        return (state==4 || state==7 ||state==9 || state==10);
    }
    public static void main(String[] args){
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
