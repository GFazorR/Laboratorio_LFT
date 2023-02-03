
import java.io.*;
import supporto.*;

/*
start ::= expr EOF { print(expr:val ) }

expr ::= term { exprp.i = term:val } exprp { expr.val = exprp.val }

exprp ::= + term { exprp1.i = exprp.i + term.val } exprp1 { exprp.val = exprp1.val }
        | - term { exprp1.i = exprp.i - term.val } exprp1 { exprp.val = exprp1.val }
        | epsilon { exprp.val = exprp.i }

term ::= fact { termp.i = fact.val } termp { term.val = termp.val }

termp ::= * fact { termp1.i = termp.i * fact.val } termp1 { termp.val = termp1.val }
        | / fact { termp1.i = termp.i/fact.val } termp1 { termp.val = termp1.val }
        | epsilon { termp.val = termp.i }

fact ::= ( expr ) { fact.val = expr.val } 
        | NUM { fact.val = NUM.value }

*/

public class Valutatore {

    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) {
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
                int expr_val;
                expr_val = expr();
                match(Tag.EOF);
                System.out.println(expr_val);
                break;

            default:
                error("Errore in start");
        }
    }

    private int expr() {
        int term_val, exprp_val = 0;
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                term_val = term();
                exprp_val = exprp(term_val);
                break;

            default:
                error("Errore trovato nel metodo Expr");
        }
        return exprp_val;
    }

    private int exprp(int exprp_i) {
        int term_val, exprp_val = 0;
        switch (look.tag) {

            case '+':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                break;

            case '-':
                match('-');
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                break;

            case ')':
            case Tag.EOF:
                exprp_val = exprp_i;
                break;

            default:
                error("Errore trovato nel metodo Exprp");
        }
        return exprp_val;
    }

    private int term() {
        int fact_val, termp_val = 0;
        switch (look.tag) {

            case '(':
            case Tag.NUM:
                fact_val = fact();
                termp_val = termp(fact_val);
                break;

            default:
                error("Errore trovato nel metodo Term");
        }
        return termp_val;
    }

    private int termp(int termp_i) {
        int fact_val, termp_val = 0;
        switch (look.tag) {

            case '*':
                match('*');
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                break;

            case '/':
                match('/');
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                ;
                break;

            case '+':
            case '-':
            case ')':
            case Tag.EOF:
                termp_val = termp_i;
                break;

            default:
                error("Errore trovato nel metodo Termp");
        }
        return termp_val;
    }

    private int fact() {
        int fact_val = 0;
        switch (look.tag) {

            case '(':
                match('(');
                fact_val = expr();
                match(')');
                break;

            case Tag.NUM:
                fact_val = Integer.parseInt(((supporto.Number) look).number);
                match(Tag.NUM);
                break;

            default:
                error("Errore trovato nel metodo Fact");
        }
        return fact_val;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "testvalutatore.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
