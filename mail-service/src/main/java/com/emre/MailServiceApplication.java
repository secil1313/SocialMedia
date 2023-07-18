package com.emre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailServiceApplication.class);
    }
/*  Görmek içindi kullanılmayacak.
    private final JavaMailSender javaMailSender;
    public MailServiceApplication(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    @EventListener(ApplicationReadyEvent.class)
    public void sendMail() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("ekazanci97@gmail.com");
        mailMessage.setTo("emre.kzanci@gmail.com");
        mailMessage.setSubject("AKTIVASYON KODU");
        mailMessage.setText(UUID.randomUUID().toString());
        javaMailSender.send(mailMessage);
    }
*/
}