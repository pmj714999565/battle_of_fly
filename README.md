C/S模式的飞机生存小游戏（JAVA代码编写）
=
## 使用方法
  需要将client中的host接口的IP地址改为sever服务器运行的地址，以及需要将sendmail.properties中的默认邮箱账号和密码更改（默认为163网易邮箱的配置）
jeids的配置可以一边调试一边更改，默认密码为123456，JDBC的连接需要用到game数据库，battle_of_fly表，创建代码如下：
```
CREATE DATABASE game;
use game2;
CREATE TABLE battle_of_fly(
ID CHAR(16) NOT NULL PRIMARY KEY,
`password` CHAR(16) NOT NULL,
mail CHAR(32) NOT NULL,
date date NOT NULL,
lv double(16,8) NOT NULL
)
```
## 实现方法
JAVA SwingGUI工具，SocketUDP、TCP连接以及他们的连接池，JDBC，Jedis连接池的使用，多线程模式下的线程安全管理，JAVAmail。
## 运行实例
