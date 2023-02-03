
import java.io.*;
import supporto.*;

/*
prog ::= statlist EOF

stat ::= ID = expr
        | print ( expr )
        | read ( ID )
        | if bexpr then stat
        | if bexpr then stat else stat
        | for ( ID = expr ; bexpr ) do stat
        | begin statlist end

statlist ::= stat statlistp

statlistp ::= ; stat statlistp | epsilon

bexpr ::= expr RELOP expr

expr ::= term exprp

exprp ::= + term exprp 
        | - term exprp 
        | epsilon

term ::= fact termp

termp ::= * fact termp 
        | / fact termp 
        | epsilon

fact ::= ( expr ) 
        | NUM 
        | ID

*/

public class Parser2 {

    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser2(Lexer l, BufferedReader br) {
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

    public void prog() {
        switch (look.tag) {
            case Tag.ID:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.IF:
            case Tag.FOR:
            case Tag.BEGIN:
                statlist();
                match(Tag.EOF);
                break;

            default:
                error("Errore in prog");
        }
    }

    private void stat() {
        switch (look.tag) {
            case Tag.ID:
                match(Tag.ID);
                match(Token.assign.tag);
                expr();
                break;

            case Tag.PRINT:
                match(Tag.PRINT);
                match(Token.lpt.tag);
                expr();
                match(Token.rpt.tag);
                break;

            case Tag.READ:
                match(Tag.READ);
                match(Token.lpt.tag);
                match(Tag.ID);
                match(Token.rpt.tag);
                break;

            case Tag.IF:
                match(Tag.IF);
                bexpr();
                match(Tag.THEN);
                stat();
                statp();
                break;

            case Tag.FOR:
                match(Tag.FOR);
                match(Token.lpt.tag);
                match(Tag.ID);
                match(Token.assign.tag);
                expr();
                match(Token.semicolon.tag);
                bexpr();
                match(Token.rpt.tag);
                match(Tag.DO);
                stat();
                break;

            case Tag.BEGIN:
                match(Tag.BEGIN);
                statlist();
                match(Tag.END);
                break;

            default:
                error("Errore in stat");
        }
    }

    private void statp() {
        switch (look.tag) {
            case Tag.ELSE:
                match(Tag.ELSE);
                stat();
                break;

            case ';':
            case Tag.EOF:
            case Tag.ID:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.IF:
            case Tag.FOR:
            case Tag.BEGIN:
            case Tag.END:
                break;

            default:
                error("Errore in statp");
        }
    }

    private void statlist() {
        switch (look.tag) {
            case Tag.ID:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.IF:
            case Tag.FOR:
            case Tag.BEGIN:
                stat();
                statlist_p();
                break;

            default:
                error("Errore in statlist");
        }
    }

    private void statlist_p() {
        switch (look.tag) {
            case ';':
                match(Token.semicolon.tag);
                stat();
                statlist_p();
                break;

            case Tag.END:
            case Tag.EOF:
                break;

            default:
                error("Errore in statlist_p");
        }
    }

    private void bexpr() {
        switch (look.tag) {
            case '(':
            case Tag.NUM:
            case Tag.ID:
                expr();
                match(Tag.RELOP);
                expr();
                break;

            default:
                error("Errore in bexpr");
        }
    }

    private void expr() {
        switch (look.tag) {
            case '(':
            case Tag.NUM:
            case Tag.ID:
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
            case Tag.RELOP:
            case ';':
            case Tag.THEN:
            case Tag.ELSE:
            case Tag.END:
                break;

            default:
                error("Errore trovato nel metodo Exprp");
        }
    }

    private void term() {
        switch (look.tag) {
            case '(':
            case Tag.NUM:
            case Tag.ID:
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
            case Tag.RELOP:
            case ';':
            case Tag.THEN:
            case Tag.ELSE:
            case Tag.END:
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

            case Tag.ID:
                match(Tag.ID);
                break;

            default:
                error("Errore trovato nel metodo Fact");

        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "testparser2.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser2 parser = new Parser2(lex, br);
            parser.prog();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
