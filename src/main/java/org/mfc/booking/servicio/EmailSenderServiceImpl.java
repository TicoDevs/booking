package org.mfc.booking.servicio;

import org.mfc.booking.dto.ReservacionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}") private String sender;

    public EmailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public String sendEmail(String to, String subject, String message) {
        String result =null;
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        try {
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(message);
            this.mailSender.send(simpleMailMessage);
            result="success";
            } catch (MailParseException e) {
            throw new MailParseException(e);
        }finally {
            if(result !="success"){
                result="fail";
            }
        }
        return result;
    }

    @Override
    public String sendAttachment(String subject, String to, File file) throws MessagingException {
        String result = null;
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(subject);
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setReplyTo(sender);
            helper.setText("stub", false);
            helper.addAttachment("document.txt", file);
            mailSender.send(message);
            result="success";
        } catch (MessagingException e) {
            throw new MailParseException(e);
        } finally {
            if (result != "success") {
                result = "fail";
            }
        }
        result = "success";
        return result;
    }

    @Override
    public String sendEmailToUsers(String to,String subject, String name, String newToken){
        String result =null;
        MimeMessage message =mailSender.createMimeMessage();
        try {

            MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
            String htmlMsg = "<body>"+
                    "<table style=Ffont-family:arial,helvetica,sans-serif;' role='presentation' cellpadding='0' cellspacing='0' width='100%' border='0'>"+
                    "<tbody><tr><td style='overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;' align='left'>"+
                    "<div><strong>Hola </strong><strong>"+ name +" !!!!</strong> </div></td></tr>"+
                    "<tr><td style='overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;' align='left'>"+
                    "<div style='line-height: 140%; text-align: left; word-wrap: break-word;'>"+
                    "<p style='font-size: 14px; line-height: 140%;'>Su OTP para cambio de contraseña es: <strong>"+ newToken +"</strong></p></div></td></tr>"+
                    "<tr><td style='overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;' align='left'>"+
                    "<div style='line-height: 140%; text-align: left; word-wrap: break-word;'>"+
                    "<p style='font-size: 14px; line-height: 140%;'>Utilice esta OTP para completar su nuevo registro de usuario.  OTP es confidencial, no comparta esto con nadie.</p>"+
                    "</div></td></tr>" +
                    "<tr><td style='overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;' align='left'>"+
                    "<div style='line-height: 140%; text-align: left; word-wrap: break-word;'>"+
                    "<p style='font-size: 14px; line-height: 140%;'>URL para cambio de contraseña :<strong> http://localhost:4200/cambioContrasenna </strong></p></div></td></tr>"+
                    "</tbody></table>"+
                    "</body>";
            message.setContent(htmlMsg, "text/html");
            helper.setTo(to);
            helper.setSubject(subject);
            mailSender.send(message);
            result="success";
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }finally {
            if(result !="success"){
                result="fail";
            }
        }

        return result;

    }

    @Override
    public String sendEmailToMiembro(ReservacionDto reservacionDto){
        String result =null;
        MimeMessage message =mailSender.createMimeMessage();
        try {

            MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
            String est = reservacionDto.getEstado().equals(Integer.valueOf(1))? "<strong>ACEPTADA</strong>" : "<strong>RECHAZADA</strong>";
            String htmlMsg = "<body>"+
                    "<table style=Ffont-family:arial,helvetica,sans-serif;' role='presentation' cellpadding='0' cellspacing='0' width='100%' border='0'><tbody>"+
                    "<tr><td style='overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;' align='left'> "+
                    "<div><strong>Hola " + reservacionDto.getUsuario().getNombre() + " !!!!</strong></div></td></tr>"+
                    "<tr><td style='overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;' align='left'>"+
                    "<div style='line-height: 140%; text-align: left; word-wrap: break-word;'>"+
                    "<p style='font-size: 14px; line-height: 140%;'>Su reserva fue&nbsp; " + est +"</p></div></td></tr>"+
                    "<tr><td style='overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;' align='left'>"+
                    "<div style='line-height: 140%; text-align: left; word-wrap: break-word;'>"+
                    "<p style='font-size: 14px; line-height: 140%;'>Para cualquier consulta solo responda este correo</p></div></td></tr>"+
                    "</tbody></table></body>";
            message.setContent(htmlMsg, "text/html");
            helper.setTo(reservacionDto.getUsuario().getEmail());
            helper.setSubject("Notificacón de la reservación MFC");
            mailSender.send(message);
            result="success";
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }finally {
            if(result !="success"){
                result="fail";
            }
        }

        return result;

    }
    @Override
    public String sendEmailToAdmin(ReservacionDto reservacionDto){
        String result =null;
        MimeMessage message =mailSender.createMimeMessage();
        try {

            MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
            String htmlMsg = "<body>"+
                    "<table style=Ffont-family:arial,helvetica,sans-serif;' role='presentation' cellpadding='0' cellspacing='0' width='100%' border='0'><tbody>"+
                    "<tr><td style='overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;' align='left'> "+
                    "<div><strong>Notificación reserva realizada</strong></div></td></tr>"+
                    "<tr><td style='overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;' align='left'> "+
                    "<div><strong>Datos del miembro</strong></div></td></tr>"+
                    "<tr><td style='overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;' align='left'> "+
                    "<div><p style='font-size: 14px; line-height: 140%;'>Nombre: "+ reservacionDto.getUsuario().getNombre() + " </p></div></td></tr>"+
                    "<tr><td style='overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;' align='left'> "+
                    "<div><p style='font-size: 14px; line-height: 140%;'>Email: "+ reservacionDto.getUsuario().getEmail() + " </p></div></td></tr>"+
                    "<tr><td style='overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;' align='left'> "+
                    "<div><p style='font-size: 14px; line-height: 140%;'>Teléfono: "+ reservacionDto.getUsuario().getTelefono() + " </p></div></td></tr>"+
                    "<tr><td style='overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;' align='left'>"+
                    "<div style='line-height: 140%; text-align: left; word-wrap: break-word;'>"+
                    "<p style='font-size: 14px; line-height: 140%;'>Numero de Reserva pendiente de aprobación: "+ reservacionDto.getId() + "</p></div></td></tr>"+
                    "<tr><td style='overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;' align='left'>"+
                    "<div style='line-height: 140%; text-align: left; word-wrap: break-word;'>"+
                    "<p style='font-size: 14px; line-height: 140%;'>Ir al sitio para realizar la aprobación</p></div></td></tr>"+
                    "</tbody></table></body>";
            message.setContent(htmlMsg, "text/html");
            helper.setTo(sender);
            helper.setSubject("Notificación de la reservación MFC");
            mailSender.send(message);
            result="success";
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }finally {
            if(result !="success"){
                result="fail";
            }
        }

        return result;

    }
}
