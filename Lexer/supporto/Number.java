package supporto;

public class Number extends Token {
    public String number = "";
    public Number(int t , String n) { super(t); number=n; }
    public String toString() { return "<256, " + number + ">";}
}
