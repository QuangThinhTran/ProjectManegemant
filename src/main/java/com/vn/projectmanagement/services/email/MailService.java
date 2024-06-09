package com.vn.projectmanagement.services.email;

import com.vn.projectmanagement.common.constants.ValidationConstants;
import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import com.vn.projectmanagement.exceptions.ApiRequestException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
public class MailService {
    @Value("${spring.mail.from}")
    private String from;

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    public MailService(SpringTemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    @SneakyThrows
    public void sendMail(String to, String templateName, Map<String, Object> attributes) {
        if (!isValidEmail(to)) {
            logger.error("{}" + ValidationConstants.IS_NOT_VALID_EMAIL, to);
            throw new ApiRequestException(to + ValidationConstants.IS_NOT_VALID_EMAIL, HttpStatus.BAD_REQUEST);
        }

        String template = setTemplateEngine(templateName, attributes);

        MimeMessage message = setMailSender(to, template);

        javaMailSender.send(message);
    }

    /**
     * Thiết lập nội dung email từ template
     *
     * @param templateName email template
     * @return mail content
     */
    private String setTemplateEngine(String templateName, Map<String, Object> attributes) {
        Context context = new Context();
        context.setVariables(attributes);
        return templateEngine.process(templateName, context);
    }

    /**
     * Thiết lập thông tin gửi email như người gửi, người nhận, nội dung email
     *
     * @param to       email address
     * @param template email template
     * @return MimeMessage (Multipurpose Internet Mail Extensions)
     * @throws MessagingException if an error occurs
     */
    private MimeMessage setMailSender(String to, String template) throws MessagingException {
        // MIME mô tả cách mã hóa các thông điệp email để chúng có thể chứa nhiều loại dữ liệu khác nhau
        // như văn bản, hình ảnh, âm thanh, video, ứng dụng, v.v.
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setContent(template, "text/html; charset=UTF-8");

        // Sử dụng đối tượng MimeMessageHelper để thiết lập thông tin gửi email như người gửi, người nhận, nội dung email
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setText(template, true);
        helper.setSubject(SwaggerMessages.REGISTRATION_SUCCESS_MESSAGE);
        return message;
    }

    /**
     * Validate email address
     *
     * @param email email address to validate
     * @return true if email is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        return email.contains("@");
    }
}
