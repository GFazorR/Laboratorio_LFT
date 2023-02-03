
import java.io.*;
import supporto.*;

/*
start ::= expr EOF
expr ::= term exprp
exprp ::=  + term exprp
         | - term exprp
         | epsilon
term ::= fact termp
termp ::= * fact termp
        | / fact termp
        | epsilon
fact ::= ( expr ) | NUM
*/

public class Parser {

    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.err.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) {
                move();
            }
        } else {
            error("sytax error");
        }
    }

    public void start() {
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                expr();
                match(Tag.EOF);
                break;

            default:
                error("errore in start");
        }
    }

    private void expr() {
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                term();
                exprp();
                break;

            default:
                error("Errore trovato nel metodo Expr");
        }
    }

    private void exprp() {

        switch (look.tag) {

            case '+':
                match(Token.plus.tag);
                term();
                exprp();
                break;

            case '-':
                match(Token.minus.tag);
                term();
                exprp();
                break;

            case ')':
            case Tag.EOF:
                break;

            default:
                error("Errore trovato nel metodo Exprp");
        }
    }

    private void term() {
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                fact();
                termp();
                break;

            default:
                error("Errore trovato nel metodo Term");
        }
    }

    private void termp() {
        switch (look.tag) {
            case '*':
                match(Token.mult.tag);
                fact();
                termp();
                break;

            case '/':
                match(Token.div.tag);
                fact();
                termp();
                break;

            case '+':
            case '-':
            case ')':
            case Tag.EOF:
                break;

            default:
                error("Errore trovato nel metodo Termp");
        }
    }

    private void fact() {
        switch (look.tag) {
            case '(':
                match(Token.lpt.tag);
                expr();
                match(Token.rpt.tag);
                break;

            case Tag.NUM:
                match(Tag.NUM);
                break;

            default:
                error("Errore trovato nel metodo Fact");

        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "testparser.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
