package server;

import java.util.HashMap;
import java.util.Random;

public class RandomRequest {
    private static int[] ex;
    private static int[] etype;
    private static int[] ttype;
    private static int[] tx;
    private static int[] dir;
    private static int[] statistics;
    private static String[] hasid;
    private static Random random;

    static {
        ex=new int[6];
        etype=new int[6];
        ttype=new int[6];
        tx=new int[6];
        dir=new int[6];
        statistics=new int[6];
        hasid=new String[6];
        random=new Random();
    }

    public static int getStatistics(int roomnum) {
        return statistics[roomnum];
    }

    public static void setStatistics(int roomnum) {
        statistics[roomnum] = 0;
        hasid[roomnum]=" ";
    }

    public static HashMap<String,Object> getRandom(String ori){
        HashMap<String,Object> hashMap=new HashMap<String, Object>();
        int roomnum= ori.charAt(0)-'0';
        String id=ori.substring(1);
        if(statistics[roomnum]!=1){
            ex[roomnum]=random.nextInt(1000);
            etype[roomnum]=random.nextInt(7)+1;
            ttype[roomnum]=random.nextInt(2)+1;
            tx[roomnum]=random.nextInt(1010);
            dir[roomnum]=random.nextInt(7);
            statistics[roomnum]=0;
            hasid[roomnum]= " ";
        }
        hashMap.put("ex", ex[roomnum]);
        hashMap.put("etype", etype[roomnum]);
        hashMap.put("ttype", ttype[roomnum]);
        hashMap.put("tx",tx[roomnum]);
        hashMap.put("dir",dir[roomnum]);
        if(id.equals(hasid[roomnum])){
            return hashMap;
        }
        statistics[roomnum]++;
        hasid[roomnum]=id;
        return hashMap;
    }
}
