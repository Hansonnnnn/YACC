package yacc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class IOHelper {
    /**
     * 按行读取文件
     * @param path
     * @return
     * @throws IOException
     */
    private static ArrayList<String> readFile(String path) throws IOException{
        File file=new File(path);
        FileReader fileReader=new FileReader(file);
        BufferedReader br=new BufferedReader(fileReader);
        ArrayList<String> dataList=new ArrayList<>();
        String nextLine;
        while((nextLine=br.readLine())!=null){
            dataList.add(nextLine);
        }

        br.close();

        return dataList;
    }

    public static char[] getInputSequence(String path){
        try {
            ArrayList<String> inputData = readFile(path);
            String inputSequence = "";
            for (String singleLine:inputData){
                inputSequence += singleLine;
            }
            return inputSequence.toCharArray();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String[][] getStateData(String path){
        ArrayList<String> dataList = null;
        String stateTable[][];
        try {
            dataList= readFile(path);
            int length = dataList.size();
            //硬编码害死人！！！！！！！！！！！！！！！！！！！！！！！！！！！
            //Lab1的状态转换表为3列，但是Lab2的分析表为6列
            stateTable = new String[length][6];
            for (int i = 0;i < length;i++){
                String tempList[] = dataList.get(i) .split(" ");
                //硬编码害死人！！！！！！！！！！！！！！！！！！！！！！！！！！！
                //Lab1的状态转换表为3列，但是Lab2的分析表为6列
                for (int j = 0; j < 6; j++){
                    stateTable[i][j] = tempList[j];
                }
            }
            return stateTable;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
