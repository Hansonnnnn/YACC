import yacc.ParsingTableNullEleException;
import yacc.YACC;

public class Main {

    public static void main(String[] args) {
        YACC yacc = new YACC("src/yacc/TestData.txt", "src/yacc/ParsingTable.txt");
        try{
            yacc.process();
        }catch (ParsingTableNullEleException e){
            System.out.println(e);
        }

    }
}
