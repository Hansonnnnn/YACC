package yacc;

import java.util.Stack;

public class YACC {
    private char inputStream[];
    private String parsingTable[][];
    private Stack<Character> stack;

    private int isp;


    public YACC(String inputSequenceFilepath, String parsingTableFilePath){
        inputStream = IOHelper.getInputSequence(inputSequenceFilepath);
        parsingTable = IOHelper.getStateData(parsingTableFilePath);
        stack = new Stack<>();
        isp = 0;
        stack.push('$');
        stack.push('e');
    }

    public void process() throws ParsingTableNullEleException{
        char topEleInStack;
        char nextChar;
        nextChar = getNextChar();
        String eleInParsingTable;
        char[] productionRightPart;
        //实实在在很关键的问题：什么时候变topEleInStack
        while(!stack.isEmpty()){
            topEleInStack = stack.peek();
            if(topEleInStack == nextChar){//match
                if(topEleInStack == '$'){//success
                    //判断栈内是否为空
                    stack.pop();
                    System.out.println("Job Completed!");
                }else{//not end but match--->reader shift
                    nextChar = getNextChar();
                    stack.pop();
                }
            }else{//not match
                eleInParsingTable = lookUpParsingTable(topEleInStack, nextChar);
                switch (eleInParsingTable.charAt(eleInParsingTable.length() - 1)){
                    case '0'://空产生式--->栈顶元素出栈
                        stack.pop();
                        indirectPrint(eleInParsingTable);
                        break;
                    case '1'://parsingTable位置为空
                        throw new ParsingTableNullEleException();
                    default://正确应该得到解析的产生式--->栈顶元素出栈，产生式右部元素入栈---->倒叙入栈--->入栈结束，完成归约--->输出相应产生式
                        productionRightPart = eleInParsingTable.split(">")[1].toCharArray();
                        stack.pop();
                        for(int i = productionRightPart.length - 1;i >= 0;i--){
                            stack.push(productionRightPart[i]);
                        }
                        //输出相应产生式
                        indirectPrint(eleInParsingTable);
                        break;
                }
            }
        }
    }

    /**
     * 根据栈顶元素和当前读头下的字符查表----------仅实现查找行的功能，方法内部调用查找列的方法lookUpParsingTableSubOp（）
     * @param topEleInStack 栈顶元素
     * @param nextChar 读头下的字符
     * @return parsing table 中的产生式（String类型--暂定为String类型）
     * 该类存在的问题：1.大量硬编码，case怎么转换？------>表驱动怎么结合switch-case来使用？？？？  2.多次重复语句，怎么抽象？
     */
    private String lookUpParsingTable(char topEleInStack, char nextChar){
        String eleInParsingTable = "";
        switch (topEleInStack){
            case 'e':
                eleInParsingTable = lookUpParsingTableSubOp(0, nextChar);
                break;
            case 'E':
                eleInParsingTable = lookUpParsingTableSubOp(1, nextChar);
                break;
            case 't':
                eleInParsingTable = lookUpParsingTableSubOp(2, nextChar);
                break;
            case 'T':
                eleInParsingTable = lookUpParsingTableSubOp(3, nextChar);
                break;
            case 'f':
                eleInParsingTable = lookUpParsingTableSubOp(4, nextChar);
                break;
        }
        return eleInParsingTable;
    }

    /**
     * 查找列的方法
     * @param rowIndex ParsingTable行元素对应的下标
     * @param colEle ParsingTable列元素
     * @return 返回查找到的分析表中的元素
     */
    private String lookUpParsingTableSubOp(int rowIndex, char colEle){
        String eleInParsingTable = "";
        switch (colEle){
            case 'i':
                eleInParsingTable = parsingTable[rowIndex][0];
                break;
            case '+':
                eleInParsingTable = parsingTable[rowIndex][1];
                break;
            case '*':
                eleInParsingTable = parsingTable[rowIndex][2];
                break;
            case '(':
                eleInParsingTable = parsingTable[rowIndex][3];
                break;
            case ')':
                eleInParsingTable = parsingTable[rowIndex][4];
                break;
            case '$':
                eleInParsingTable = parsingTable[rowIndex][5];
                break;
        }
        return eleInParsingTable;
    }

    /**
     * 输出辅助程序，需要将要输出的产生式做进一步的改进，已符合要求的格式, 如e>tT   ----> E->TT`
     * @param sourceOutput 要输出的数据
     */
    private void indirectPrint(String sourceOutput){
        String realOutput = "";
        String[] twoPartsOfSentence = sourceOutput.split(">");

        realOutput += transferSymbol(twoPartsOfSentence[0]);

        realOutput += " -> ";

        for (int i = 0;i < twoPartsOfSentence[1].length();i++){
            realOutput += transferSymbol(twoPartsOfSentence[1].substring(i, i+1));
        }

        System.out.println(realOutput);
    }

    /**
     * 实现字符串转换功能，对源数据不符合规范的字符串进行规范化操作
     * @param sourceString 输入的源数据
     * @return 返回规范化后的字符串
     */
    private String transferSymbol(String sourceString){
        String transferredString;
        switch (sourceString){
            case "e":
                transferredString = "E";
                break;
            case "t":
                transferredString = "T";
                break;
            case "f":
                transferredString = "F";
                break;
            case "E":
                transferredString = "E`";
                break;
            case "T":
                transferredString = "T`";
                break;
            case "0":
                transferredString = "ε";
                break;
            default:
                transferredString = sourceString;
        }
        return transferredString;
    }

    private char getNextChar(){
        return inputStream[isp++];
    }



    public char[] getInputStream() {
        return inputStream;
    }

    public String[][] getParsingTable() {
        return parsingTable;
    }
}
