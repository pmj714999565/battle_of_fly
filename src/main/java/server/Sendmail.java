package server;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class Sendmail {
    private String to;
    private String key;
    static Properties props=new Properties();;
    static Session session;
    static String address;
    static String password;
    public Sendmail(String to,String key){
        this.to=to;
        this.key=key;
    }

    static {
        InputStream inputStream=Sendmail.class.getClassLoader().getResourceAsStream("mailset.properties");
        try {
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        session=Session.getInstance(props);
        address=props.getProperty("address");
        password=props.getProperty("password");
        session.setDebug(true);
    }
    public void send(){
        MimeMessage message=new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(address,"Sever","UTF-8"));
            message.setRecipients(MimeMessage.RecipientType.TO,to);
            message.setSubject("邮箱验证","UTF-8");
            message.setContent(getweb(),"text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();
            Transport ts=session.getTransport();
            ts.connect(address,password);
            ts.sendMessage(message,message.getAllRecipients());
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private String getweb(){
        return "<div>邮箱验证码有效时间为30分钟，验证码如下： </div>"+
                "<td colspan=\"2\" style=\"font-size\": 12px;line-height: 20px; padding-top: 14px;" +
                "padding-bottom: 25px; color: #909090;>"+key+"</td>";
    }
}
