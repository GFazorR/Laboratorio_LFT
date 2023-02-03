
import java.io.*;
import java.util.*;
import supporto.*;

// lexer con underscore che ignora i commmenti
public class Lexer {

    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
            if (peek == '\n') {
                line++;
            }
            readch(br);
        }

        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;

            case '+':
                peek = ' ';
                return Token.plus;

            case '-':
                peek = ' ';
                return Token.minus;

            case '*':
                peek = ' ';
                return Token.mult;

            case '/':
                readch(br);
                switch (peek) {
                    case '/':
                        readch(br);
                        while (!(peek == '\n' || peek == '\r')) {
                            readch(br);
                        }

                        return lexical_scan(br);
                    case '*':
                        boolean comm = true;  // � un commento
                        while (comm){
                            if (peek == '*') {    // pu� essere fine commento
                                readch(br);
                                if (peek == '/') {   // se � fine commento
                                    comm = false;
                                }
                            } else{
                              readch(br);
                            }
                            if (peek == (char) -1) {        // se EOF concludo
                                return new Token(Tag.EOF);
                            }

                        } while (comm);

                        peek = ' ';
                        return lexical_scan(br);

                    default:                    // nel caso in cui sia un diviso

                        return Token.div;


                }

            case ';':
                peek = ' ';
                return Token.semicolon;

            case '(':
                peek = ' ';
                return Token.lpt;

            case ')':
                peek = ' ';
                return Token.rpt;

            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character at line " + line + " after & : " + peek);
                    return null;
                }

            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character at line " + line + " after | : " + peek);
                    return null;
                }

            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else {
                    return Word.lt;
                }

            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    return Word.gt;
                }

            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    return Token.assign;
                }

            case (char) -1:
                return new Token(Tag.EOF);

            default:

                if (Character.isLetter(peek) || peek == '_') {
                    boolean control = false;
                    String s = "";
                    do {
                        if (Character.isLetter(peek) || Character.isDigit(peek)) {
                            control = true;
                        }
                        s += peek;
                        readch(br);
                    } while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_');

                    switch (s) {

                        case "if":
                            return Word.iftok;

                        case "then":
                            return Word.then;

                        case "else":
                            return Word.elsetok;

                        case "for":
                            return Word.fortok;

                        case "do":
                            return Word.dotok;

                        case "print":
                            return Word.print;

                        case "read":
                            return Word.read;

                        case "begin":
                            return Word.begin;

                        case "end":
                            return Word.end;

                        default:
                            if (!control) {
                                System.err.println("Erroneous String");
                                return null;
                            } else {
                                return new Word(Tag.ID, s);
                            }
                    }

                } else if (Character.isDigit(peek)) {
                    String n = "";
                    do {
                        n = n + peek;
                        readch(br);
                    } while (Character.isDigit(peek));
                    return new supporto.Number(Tag.NUM, n);

                } else {
                    System.err.println("Erroneous character: " + peek);
                    return null;
                }
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "testvalutatore.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
