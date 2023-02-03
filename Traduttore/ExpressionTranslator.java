import java.io.*;
import supporto.*;
import supporto_traduttore.*;

/*
Es: 5.1
*/

/*
prog ::= print ( expr ) EOF

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


public class ExpressionTranslator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    CodeGenerator code = new CodeGenerator();

    public ExpressionTranslator(Lexer l, BufferedReader br) {
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
        if(look.tag == t){
          if(look.tag != Tag.EOF) move();
       }else error("sytax error");
    }

    public void prog() {
	     switch(look.tag){
         case Tag.PRINT:
          	match(Tag.PRINT);
          	match('(');
          	expr();
          	code.emit(OpCode.invokestatic,1);
          	match(')');
          	match(Tag.EOF);
            try {
                code.toJasmin();
            }
            catch(java.io.IOException e) {
                System.out.println("IO error\n");
            };
            break;
         default: error("Errore in start");
     }
  }
    private void expr() {
        switch(look.tag) {
            case '(': case Tag.NUM:
                term();
                exprp();
                break;
            default: error("Errore in expr");

     }
   }

    private void exprp() {
        switch(look.tag) {
            case '+':
                match('+');
                term();
                code.emit(OpCode.iadd);
                exprp();
                break;
            case '-':
                match('-');
                term();
                code.emit(OpCode.isub);
                exprp();
                break;
            case ')': case Tag.EOF:
                break;
            default: error("Errore in exprp");
     }
   }

   private void term() {
       switch(look.tag) {
           case '(': case Tag.NUM:
               fact();
               termp();
               break;
           default: error("Errore in term");
    }
  }

  private void termp() {
      switch(look.tag) {
          case '*':
              match('*');
              fact();
              code.emit(OpCode.imul);
              termp();
              break;
          case '/':
              match('/');
              fact();
              code.emit(OpCode.idiv);
              termp();
              break;
          case ')': case Tag.EOF: case '+': case '-':
              break;
          default: error("Errore in termp");
   }
 }

 private void fact() {
     switch(look.tag) {
         case '(':
             match('(');
             expr();
             match(')');
             break;
         case Tag.NUM:
             code.emit(OpCode.ldc, Integer.parseInt(((supporto.Number)look).number));
             match(Tag.NUM);
             break;
         default: error("Errore in fact");
  }
}

public static void main(String[] args) {
  Lexer lex = new Lexer();
  String path = "testExprTranslator.pas";
  try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      ExpressionTranslator translator = new ExpressionTranslator(lex, br);
      translator.prog();
      br.close();
  } catch (IOException e) {e.printStackTrace();}
}
}
