package server.ts;

public class domain {
    public static void main(String[] args) {
        Trysocket ts=new Trysocket(2333);
        ts.start();
        TrysocketUDP tsudp=new TrysocketUDP();
        tsudp.start();

    }
}
