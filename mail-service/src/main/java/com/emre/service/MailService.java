package com.emre.service;

import com.emre.dto.response.ForgotPasswordMailResponseDto;
import com.emre.rabbitmq.model.RegisterMailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    public void createMailWithRabbitMq(RegisterMailModel model) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Aktivasyon kodu");
        simpleMailMessage.setFrom("ekazanci97@gmail.com");
        simpleMailMessage.setTo(model.getEmail());
        simpleMailMessage.setText(model.getUsername() + ", aktivasyon kodunuz: " + model.getActivationCode());
        javaMailSender.send(simpleMailMessage);
    }

    public Boolean sendMailForgotPassword(ForgotPasswordMailResponseDto dto) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject("Şifre sıfırlama e-postası");
            simpleMailMessage.setFrom("ekazanci97@gmail.com");
            simpleMailMessage.setTo(dto.getEmail());
            simpleMailMessage.setText("Yeni şifreniz: " + dto.getPassword() + "\n Giriş yaptıktan sonra güvenliğiniz nedeniyle şifrenizi değiştiriniz.");
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }
}
