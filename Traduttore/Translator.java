
import java.io.*;
import supporto_traduttore.*;
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


public class Translator {

    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator(Lexer l, BufferedReader br) {
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
                int lnext_prog = code.newLabel();
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }
                ;
                break;

            default:
                error("Errore in prog");
        }
    }

    private void stat(int lnext) {
        switch (look.tag) {
            case Tag.ID:
                if (look.tag == Tag.ID) {
                    int read_id_addr = st.lookupAddress(((Word) look).lexeme);
                    if (read_id_addr == -1) {
                        read_id_addr = count;
                        st.insert(((Word) look).lexeme, count++);
                    }
                    match(Tag.ID);
                    match(Token.assign.tag);
                    expr();
                    code.emit(OpCode.istore, read_id_addr);
                } else {
                    error("Error in grammar (stat) after read( with " + look + " )");
                }
                break;

            case Tag.PRINT:
                match(Tag.PRINT);
                match(Token.lpt.tag);
                expr();
                code.emit(OpCode.invokestatic, 1);
                match(Token.rpt.tag);
                break;

            case Tag.READ:
                match(Tag.READ);
                match(Token.lpt.tag);
                if (look.tag == Tag.ID) {
                    int read_id_addr = st.lookupAddress(((Word) look).lexeme);
                    if (read_id_addr == -1) {
                        read_id_addr = count;
                        st.insert(((Word) look).lexeme, count++);
                    }
                    match(Tag.ID);
                    match(')');
                    code.emit(OpCode.invokestatic, 0);
                    code.emit(OpCode.istore, read_id_addr);
                } else {
                    error("Error in grammar (stat) after read( with " + look + " )");
                }
                break;

            case Tag.IF:
                match(Tag.IF);
                int bltrue = code.newLabel();
                int blfalse = code.newLabel();
                bexpr(bltrue, blfalse);
                match(Tag.THEN);
                code.emitLabel(bltrue);
                int s1next = lnext;
                stat(s1next);
                code.emit(OpCode.GOto, s1next);
                statp(blfalse, lnext);
                break;

            case Tag.FOR:
                match(Tag.FOR);
                int begin = code.newLabel();
                match(Token.lpt.tag);
                int for_id_addr = -1;
                if (look.tag == Tag.ID) {
                    for_id_addr = st.lookupAddress(((Word) look).lexeme);
                    if (for_id_addr == -1) {
                        for_id_addr = count;
                        st.insert(((Word) look).lexeme, count++);
                    }
                    match(Tag.ID);
                    match(Token.assign.tag);
                    expr();
                    code.emit(OpCode.istore, for_id_addr);
                } else {
                    error("Error in grammar (stat) after read( with " + look + " )");
                }

                match(Token.semicolon.tag);
                code.emitLabel(begin);
                int bfltrue = code.newLabel();
                int bflfalse = lnext;
                bexpr(bfltrue, bflfalse);
                match(Token.rpt.tag);
                match(Tag.DO);
                code.emitLabel(bfltrue);
                int snext = begin;
                stat(snext);
                code.emitLabel(code.newLabel());
                code.emit(OpCode.iload, for_id_addr);
                code.emit(OpCode.ldc, 1);
                code.emit(OpCode.iadd);
                code.emit(OpCode.istore, for_id_addr);
                code.emit(OpCode.GOto, snext);
                break;

            case Tag.BEGIN:
                match(Tag.BEGIN);
                statlist(lnext);
                match(Tag.END);
                break;

            default:
                error("Errore in stat");
        }
    }

    private void statp(int lfalse, int lnext) {
        switch (look.tag) {
            case Tag.ELSE:
                match(Tag.ELSE);
                code.emitLabel(lfalse);
                int s2next = lnext;
                stat(s2next);
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
                code.emitLabel(lfalse);
                break;

            default:
                error("Errore in statp");
        }
    }

    private void statlist(int lnext) {
        switch (look.tag) {
            case Tag.ID:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.IF:
            case Tag.FOR:
            case Tag.BEGIN:
                int slpnext = code.newLabel();
                stat(slpnext);
                code.emitLabel(slpnext);
                statlist_p(lnext);
                break;

            default:
                error("Errore in statlist");
        }
    }

    private void statlist_p(int lnext) {
        switch (look.tag) {
            case ';':
                match(Token.semicolon.tag);
                int slpnext = code.newLabel();
                stat(slpnext);
                //if(!(look.tag == Tag.END || look.tag == Tag.EOF))
                code.emitLabel(slpnext);
                statlist_p(lnext);
                break;

            case Tag.END:
            case Tag.EOF:
                break;

            default:
                error("Errore in statlist_p");
        }
    }

    private void bexpr(int ltrue, int lfalse) {
        switch (look.tag) {
            case '(':
            case Tag.NUM:
            case Tag.ID:
                expr();
                if (look == Word.eq) {
                    match(Tag.RELOP);
                    expr();
                    code.emit(OpCode.if_icmpeq, ltrue);
                    code.emit(OpCode.GOto, lfalse);
                } else if (look == Word.lt) {
                    match(Tag.RELOP);
                    expr();
                    code.emit(OpCode.if_icmplt, ltrue);
                    code.emit(OpCode.GOto, lfalse);
                } else if (look == Word.gt) {
                    match(Tag.RELOP);
                    expr();
                    code.emit(OpCode.if_icmpgt, ltrue);
                    code.emit(OpCode.GOto, lfalse);
                } else if (look == Word.le) {
                    match(Tag.RELOP);
                    expr();
                    code.emit(OpCode.if_icmple, ltrue);
                    code.emit(OpCode.GOto, lfalse);
                } else if (look == Word.ge) {
                    match(Tag.RELOP);
                    expr();
                    code.emit(OpCode.if_icmpge, ltrue);
                    code.emit(OpCode.GOto, lfalse);
                } else if (look == Word.ne) {
                    match(Tag.RELOP);
                    expr();
                    code.emit(OpCode.if_icmpne, ltrue);
                    code.emit(OpCode.GOto, lfalse);
                } else {
                    error("Errore look bexpr");
                }
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
                code.emit(OpCode.iadd);
                exprp();
                break;

            case '-':
                match(Token.minus.tag);
                term();
                code.emit(OpCode.isub);
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
                code.emit(OpCode.imul);
                termp();
                break;

            case '/':
                match(Token.div.tag);
                fact();
                code.emit(OpCode.idiv);
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
                code.emit(OpCode.ldc, Integer.parseInt(((supporto.Number) look).number));
                match(Tag.NUM);
                break;

            case Tag.ID:
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    error("Errore metodo fact, variabile non inizializzata");
                }
                move();
                code.emit(OpCode.iload, id_addr);
                break;

            default:
                error("Errore trovato nel metodo Fact");

        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "testtranslator.pas";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
