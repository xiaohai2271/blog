package cn.celess.blog.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * @author : xiaohai
 * @date : 2019/04/22 14:25
 */
@Service
public interface MailService {
    Boolean AsyncSend(SimpleMailMessage message);

    Boolean send(SimpleMailMessage message);
}
