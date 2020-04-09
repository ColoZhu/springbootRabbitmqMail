package czs.utils;


import javax.mail.*;
import javax.mail.internet.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;



public class SendMailUtil {

    /**
     * 发送邮件
     * @param subject 主题
     * @param content 内容
     * @throws Exception  这里可以try catch一下异常~
     */
    public static void sendMail(String subject, String content) throws Exception {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.exmail.qq.com");  //腾讯企业邮箱的smtp服务器
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "true");


        String from = "xxx1@qq.com";    // 发送方地址
        String to = "xxx2@qq.com";      // 收件方地址
        String username = "xxx1@qq.com";  //邮箱名称
        String password = "mima";       //邮箱密码

        List<String> recipients = Arrays.asList(to);

        //认证 git remote set-url origin https://github.com/ColoZhu/springbootRabbitmqMail.git
        Authenticator smtpAuth = new PopupAuthenticator(from, password);
        Session session = Session.getDefaultInstance(props, smtpAuth);

        //上线关闭debug
        session.setDebug(true);
        //session.setDebug(false);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));

        //多个收件人
        int num = recipients.size();
        InternetAddress[] rece_addresses = new InternetAddress[recipients.size()];
        for (int i = 0; i < num; i++) {
            rece_addresses[i] = new InternetAddress(recipients.get(i));
        }
        message.setRecipients(Message.RecipientType.TO, rece_addresses);

        message.setContent(content, "text/html; charset=utf-8");
        message.setSubject(subject);
        message.saveChanges();

        Transport transport = session.getTransport("smtp");
        transport.connect("smtp.exmail.qq.com", username, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }


    public static void main(String[] args) throws Exception {
        sendMail("主题测试的", "hello");
    }

}


class PopupAuthenticator extends Authenticator {
    String username = null;
    String password = null;


    public PopupAuthenticator() {
    }

    public PopupAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }


    PasswordAuthentication performCheck(String user, String pass) {
        username = user;
        password = pass;
        return getPasswordAuthentication();
    }


    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }


}