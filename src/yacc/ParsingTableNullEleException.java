package yacc;

/**
 * 该异常类为当查表得到空值时抛出的异常
 */
public class ParsingTableNullEleException extends Exception {
    private String exceptionMessage;

    public ParsingTableNullEleException(){
        this.exceptionMessage = "查询预测表出现空值，输入的字符串序列与CFG不相对应。";
    }
    public ParsingTableNullEleException(String exceptionMessage){
        this.exceptionMessage = exceptionMessage;
    }

    public String toString(){
        return "ParsingTableNullEleException[ " + exceptionMessage + " ]";
    }
}
