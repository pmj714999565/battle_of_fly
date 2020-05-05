package client;

import client.fire.BossFire;
import client.fire.EpFire;
import client.fire.PlayerFire;
import client.flyobject.Boss;
import client.flyobject.Enemy;
import client.flyobject.Player;
import client.flyobject.Tools;
import client.mune.Createframe;
import client.mune.Framelogin;
import client.ts.Host;
import client.ts.TCP_send;
import client.ts.UDP_send;

import redis.clients.jedis.Jedis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static java.lang.Thread.sleep;

public class GamePanel extends JPanel implements Host {
    BufferedImage bg;//背景图
    int x1=0,y1=0,x2=0,y2=-683;//滚屏参数
    Player player1 = new Player();//玩家飞机对象
    Player player2 = new Player();//玩家飞机对象
    Boss boss = new Boss();//boss机
    List<Enemy> eps=new ArrayList<Enemy>();//敌机的种类数组
    List<PlayerFire> fs = new ArrayList<PlayerFire>();//玩家飞机弹药库
    List<EpFire> epfs = new ArrayList<EpFire>();//敌机子弹库
    List<Tools> tls = new ArrayList<Tools>();//道具库
    List<BossFire> bfs = new ArrayList<BossFire>();//boss机库
    int score;//得分
    int t=0;//时间
    int bosst=0;//boss产生时间控制
    int bossfg=0;//boss出现，则敌机不产生
    int lv=1;
    boolean gameover;//游戏开始为false，结束为true
    int mx1=500,my1=500;
    boolean up=false,down=false,left=false,right=false;
    UDP_send udp_send;

    public void action(final Createframe frame, final String roomnum, final String otherid) {
        new Thread() {
            public void run() {
                HashMap<String, String> mes=new HashMap<String, String>();
                udp_send=new UDP_send();
                Jedis jedis=new Jedis(host);
                jedis.auth("123456");
                HashMap<String,Object> hashMap=new HashMap<String, Object>();
                hashMap.put("ID",Framelogin.getID());
                hashMap.put("flag_status","gameover");
                check();
                while(true){
                    t++;
                    bosst++;
                    mes.put("px",String.valueOf(player1.x));
                    mes.put("py",String.valueOf(player1.y));
                    mes.put("hp",String.valueOf(player1.hp));
                    mes.put("power",String.valueOf(player1.power));
                    jedis.hmset(Framelogin.getID(),mes);
                    udp_send.UDP_sends(roomnum+otherid);
                    player2.x= udp_send.getPx();
                    player2.y= udp_send.getPy();
                    player2.hp=udp_send.getHp();
                    player2.power=udp_send.getPower();
                    if(udp_send.isGameover()||gameover||player2.hp<=0){
                        TCP_send tcp_send=new TCP_send();
                        hashMap.put("score",String.valueOf(score*0.0001));
                        hashMap.put("roomnum",roomnum);
                        JOptionPane.showMessageDialog(null, "经验："+score, "GAME OVER", JOptionPane.PLAIN_MESSAGE);
                        tcp_send.TCP_send_flag(hashMap);
                        new Thread(new Runnable() {
                            public void run() {
                                RoomStart roomStart=new RoomStart();
                                roomStart.startroom();
                            }
                        }).start();
                        try {
                            frame.setVisible(false);
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        y1 += 1;
                        y2 += 1;
                        if (y1 == 683) {
                            y1 = 0;
                            y2 = -683;
                        }
                        shoot(player1);//子弹发射
                        shoot(player2);
                        fireMove();//子弹移动
                        if (bosst < 2000) {
                            epEnter();//敌机产生
                            epfire();//敌机子弹
                        } else {
                            enBoss();//boss产生后不产生其他东西
                        }
                        if (bosst % 1000 == 0) {
                            toolsEnter();
                            lv++;
                        }//道具产生
                        toolsMove();//道具移动
                        EnemyMove();//敌机移动
                        bossMove();
                        BossShoot();//boss机子弹发射
                        BfMove();//boss子弹移动
                        epfireMove();//敌机子弹移动
                        toolsHit();//是否吃到道具
                        try {
                            sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        repaint();
                    }
                }
            }

        }.start();
    }
    public void check(){
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    heroHit();//敌机与英雄机碰撞
                    epfierHit();//敌机子弹与英雄碰撞
                    shootEp();//打敌机
                    bossfierHit();//boss机子弹击中英雄机
                    shootBoss();//打boss机
                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    repaint();
                }
            }
        }).start();
    }
    protected void shootBoss() {
        if(bossfg==1)
        {
            for (int i=0;i<fs.size();i++) {
                PlayerFire f=fs.get(i);
                bossbang(f);
            }
        }
    }
    private void bossbang(PlayerFire f) {
        if(boss.shootBy(f)) {
            boss.hp--;
            score+=20;
            fs.remove(f);//子弹消失
            if(boss.hp<0)
            {
                bossfg=0;
                bosst=0;
            }
        }
    }
    int bmindex=0;//控制boss移动速度
    protected void bossMove() {
        if(bossfg==1)
        {
            bmindex++;
            if(bmindex>2)
            {
                boss.move();
                bmindex=0;
            }
        }
    }
    protected void bossfierHit() {
        if(bossfg==1)
        {
            for(int i=0;i<bfs.size();i++) {
                BossFire bf =bfs.get(i);
                if(bf.shootBy(player1)) {
                    bfs.remove(bf);
                    player1.hp=player1.hp-1;
                    if(player1.power>1) player2.power--;
                    if(player1.hp<=0) {
                        gameover =true;//游戏结束
                    }
                }
                if(bf.shootBy(player2)) {
                    bfs.remove(bf);
                }
            }
        }

    }
    int bfmindex;
    protected void BfMove() {
        if(bossfg==1)
        {
            bfmindex++;
            if(bfmindex>2)
            {
                for (int i=0;i<bfs.size();i++) {
                    BossFire bf=bfs.get(i);
                    bf.move();
                    if (bf.x > 1100 || bf.x < 0 || bf.y < 0 || bf.y > 700) {
                        epfs.remove(bf);
                    }
                }
                bfmindex=0;
            }
        }

    }
    int bfindex=0;
    int bftype=1;
    protected void BossShoot() {
        if(bossfg==1)
        {
            bfindex++;
            if(bfindex>200)
            {
                for(int i=0;i<11;i++)
                {
                    BossFire b=new BossFire(boss.x, boss.y,i);
                    bfs.add(b);
                    bfindex=0;
                }
                for(int i=0;i<11;i++)
                {
                    BossFire b=new BossFire(boss.x+200, boss.y,i);
                    bfs.add(b);
                    bfindex=0;
                }
            }
            bftype++;
            if(bftype>=3) {
                bftype=1;
            }
        }
    }
    protected void enBoss() {
        bossfg=1;
        eps.clear();
        epfs.clear();
    }
    int tindex=0;
    protected void toolsMove() {
        tindex++;
        if(tindex>2) {
            for (int i=0;i<tls.size();i++) {
                Tools t=tls.get(i);
                t.MoveTools();
                tindex = 0;
            }
        }
    }
    protected void toolsEnter() {
        Tools t=new Tools(udp_send.getTtype(),udp_send.getTx());
        tls.add(t);
    }
    protected void epfierHit() {
        for(int i=0;i<epfs.size();i++) {
            EpFire e =epfs.get(i);
            if(e.shootBy(player1)) {
                epfs.remove(e);
                player1.hp=player1.hp-1;
                if(player1.power>1) player1.power--;
                if(player1.hp<=0) {
                    gameover =true;//游戏结束
                }
            }
            else if(e.shootBy(player2)) {
                epfs.remove(e);
            }
        }
    }
    private void shootEp() {
        for (int i=0;i<fs.size();i++) {
            PlayerFire f=fs.get(i);
            bang(f);
        }
    }
    private void bang(PlayerFire f) {
        for(int i=0;i<eps.size();i++) {
            Enemy e = eps.get(i);
            if(e.shootBy(f)) {
                e.hp--;
                if(e.hp<=0) {
                    if(e.kind==7) {
                        if(player1.power>3&&player1.hp<5) {
                            player1.hp++;
                        }
                        else if (player1.power<3) player1.power++;
                    }
                    eps.remove(e);//敌机消失
                    score +=100;//得分增加
                }
                fs.remove(f);//子弹消失
            }
        }
    }
    protected void heroHit() {
        for(int i=0;i<eps.size();i++) {
            Enemy e =eps.get(i);
            if(e.hitBy(player1)) {
                eps.remove(e);
                player1.hp--;
                if(player1.power>1) player1.power--;
                score+=100;
                if(player1.hp<=0) {
                    gameover =true;//游戏结束
                }
            }
            else if(e.hitBy(player2)) {
                eps.remove(e);
            }
        }
    }
    protected void toolsHit() {
        for(int i=0;i<tls.size();i++) {
            Tools t =tls.get(i);
            if(t.hitBy(player1)) {
                if(t.type==1)
                {
                    if(player1.hp<5) player1.hp++;
                }
                else if(t.type==2)
                {
                    if(player1.power<3) player1.power++;
                }
                tls.remove(t);
            }
            if(t.hitBy(player2)) {
                tls.remove(t);
            }
        }
    }
    int ept=0;
    private void epfireMove() {
        ept++;
        if(ept>2) {
            for(int i=0;i<epfs.size();i++){
                EpFire f=epfs.get(i);
                f.move();
                ept=0;
                if(f.x>1100||f.x<0||f.y<0||f.y>700)
                {
                    epfs.remove(f);
                }
            }
        }
    }
    private void epfire() {
        if(t<500) {
            for (Enemy e : eps) {
                if(e.ef>0) {
                    EpFire fire1 = new EpFire(e.x, e.y, player1.x, player1.y);
                    epfs.add(fire1);
                    EpFire fire2 = new EpFire(e.x, e.y, player2.x, player2.y);
                    epfs.add(fire2);
                    e.ef--;
                }
            }
        }
        else {
            for (Enemy e : eps) {
                EpFire fire = new EpFire(e.x, e.y, player1.x, player1.y);
                epfs.add(fire);
            }
            for (Enemy e : eps) {
                EpFire fire = new EpFire(e.x, e.y, player2.x, player2.y);
                epfs.add(fire);
            }
            t=0;
        }
    }// 敌机子弹生成方法
    int eindex=0;//敌机间隔计数
    protected void epEnter()//敌机生成方法
    {
        eindex++;
        if(eindex>50) {
            Enemy e=new Enemy(udp_send.getEtype(),udp_send.getDir(),udp_send.getEx(),lv);//////////////////////
            eps.add(e);
            eindex=0;
        }
    }
    int emindex=0;//敌机移动间隔计数
    protected void EnemyMove() {
        emindex++;
        if(emindex>2) {
            for(int i=0;i<eps.size();i++){
                Enemy e=eps.get(i);
                e.MoveEnemy();
                emindex=0;
                if(e.x>1100||e.x<0||e.y<0||e.y>700)
                {
                    eps.remove(e);
                }
            }
        }
    }
    int fsindex=0;
    protected void fireMove() {
        fsindex++;
        if(fsindex>2) {
            for(int i=0;i<fs.size();i++){
                PlayerFire f=fs.get(i);
                f.move();
                fsindex=0;
                if(f.x<0||f.x>1100||f.y>700||f.y<0) {
                    fs.remove(f);
                }
            }
        }
    }//子弹移动方法
    int findex=0;//计数器
    protected void shoot(Player player)
    {
        findex++;
        if(findex>40)
        {
            if(player.power==3)
            {
                PlayerFire f1=new PlayerFire(player.x-15,player.y);
                PlayerFire f2=new PlayerFire(player.x,player.y-5);
                PlayerFire f3=new PlayerFire(player.x+15,player.y);
                fs.add(f1);
                fs.add(f2);
                fs.add(f3);

            }else if(player.power==2)
            {
                PlayerFire f1=new PlayerFire(player.x-15,player.y);
                PlayerFire f2=new PlayerFire(player.x+15,player.y);
                fs.add(f1);
                fs.add(f2);
            }else
            {
                PlayerFire f1=new PlayerFire(player.x,player.y);
                fs.add(f1);
            }
            findex=0;
        }
    }//子弹生成方法
    public GamePanel()
    {
        bg = Loadimg.getImg("/img/1.jpg");
        KeyAdapter adapter=new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_UP){
                    up=true;
                }
                else if(e.getKeyCode()==KeyEvent.VK_DOWN){
                    down=true;
                }
                else if(e.getKeyCode()==KeyEvent.VK_LEFT){
                    left=true;
                }
                else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                    right=true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(e.getKeyCode()==KeyEvent.VK_UP){
                    up=false;
                }
                else if(e.getKeyCode()==KeyEvent.VK_DOWN){
                    down=false;
                }
                else if(e.getKeyCode()==KeyEvent.VK_LEFT){
                    left=false;
                }
                else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                    right=false;
                }
            }
        };
        new Thread(new Runnable() {
            public void run() {
                while(true){
                    if(up&&!down&&!left&&!right&&my1>5){
                        player1.moveHero(mx1,my1=my1-5);
                    }
                    else if(!up&&down&&!left&&!right&&my1<678){
                        player1.moveHero(mx1,my1=my1+5);
                    }
                    else if(!up&&!down&&left&&!right&&mx1>5){
                        player1.moveHero(mx1=mx1-5,my1);
                    }
                    else if(!up&&!down&&!left&&right&&mx1<1019){
                        player1.moveHero(mx1=mx1+5,my1);
                    }
                    else if(up&&!down&&left&&!right&&my1>5&&mx1>5){
                        player1.moveHero(mx1=mx1-5,my1=my1-5);
                    }
                    else if(up&&!down&&!left&&right&&my1>5&&mx1<1019){
                        player1.moveHero(mx1=mx1+5,my1=my1-5);
                    }
                    else if(!up&&down&&left&&!right&&my1<678&&mx1>5){
                        player1.moveHero(mx1=mx1-5,my1=my1+5);
                    }
                    else if(!up&&down&&!left&&right&&my1<678&&mx1<1019){
                        player1.moveHero(mx1=mx1+5,my1=my1+5);
                    }
                    repaint();
                    try {
                        sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        this.addKeyListener(adapter);
        this.setFocusable(true);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(gameover)
        {
            bg=Loadimg.getImg("/img/gameover.png");
            g.drawImage(bg, 0, 0,1024, 683,null);

        }
        else
        {
            g.drawImage(bg, x1, y1,1024, 683,null);
            g.drawImage(bg, x2, y2,1024, 683,null);
            g.drawImage(player1.img, player1.x,player1.y, null);
            g.drawImage(player2.img, player2.x,player2.y, null);
            for(int i=0;i<fs.size();i++) {
                PlayerFire fire =fs.get(i);
                g.drawImage(fire.img, fire.x,fire.y, null);
            }//画子弹
            for(int i=0;i<eps.size();i++) {
                Enemy enemy =eps.get(i);
                g.drawImage(enemy.img, enemy.x,enemy.y, null);
            }//画敌机
            for(int i=0;i<epfs.size();i++) {
                EpFire epfire=epfs.get(i);
                g.drawImage(epfire.img, epfire.x,epfire.y, null);
            }//敌机子弹
            for(int i=0;i<tls.size();i++) {
                Tools t=tls.get(i);
                g.drawImage(t.img, t.x,t.y, null);
            }//道具
            if(bossfg==1)
            {
                g.drawImage(boss.hpimg1,217,5,500,25,null);
                for(int i=0;i<boss.hp;i++) {
                    g.drawImage(boss.hpimg2,218+i,6,1,23,null);
                }//boss机血量
                g.drawImage(boss.img, boss.x,boss.y, null);
                for(int i=0;i<bfs.size();i++)
                {
                    BossFire bf=bfs.get(i);
                    g.drawImage(bf.img,bf.x,bf.y,null);
                }
            }
            g.setColor(Color.RED);
            g.setFont(new Font("楷体",Font.BOLD,20));
            g.drawString("分数:"+score,10,30);
            g.drawString("HP:",800,30);
            g.drawString("队友HP:",780,85);
            for(int i=0;i<player1.hp;i++) {
                g.drawImage(player1.img,850+i*35,5,45,45,null);
            }//英雄机血量
            for(int i=0;i<player2.hp;i++) {
                g.drawImage(player2.img,850+i*35,55,45,45,null);
            }//英雄机血量
        }
    }
}
