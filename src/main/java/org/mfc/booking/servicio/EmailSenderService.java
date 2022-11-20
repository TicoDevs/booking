package org.mfc.booking.servicio;

import org.mfc.booking.dto.ReservacionDto;

import javax.mail.MessagingException;
import java.io.File;

public interface EmailSenderService {

    String sendEmail(String to, String subject, String message);
    String sendAttachment(String subject, String to, File file) throws MessagingException;

    String sendEmailToUsers(String to,String subject, String name, String newToken);

    String sendEmailToMiembro(ReservacionDto reservacionDto);

    String sendEmailToAdmin(ReservacionDto reservacionDto);
}
