package com.kts.unity.config.web.backend.utils.email;

import com.kts.facebookapp.FacebookSocialPluginAction;
import com.kts.unity.config.web.backend.utils.Settings;
import java.util.Map;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailUtil {

    public int init() {
        int status = 0;

        return status;
    }

    public boolean generateAndSendEmail(EmailType mailType, String messageText, Map<String, String> params) throws Exception {
        boolean status = false;
        try {
            Properties p = System.getProperties();
            p.put("mail.smtp.auth", "true");
            p.put("mail.smtp.starttls.enable", "true");
            //Host and port initialization goes in section where email settings are being set

            Authenticator auth = null;
            String mailFrom = null;
            String mailTo = null;
            String subject = null;
            StringBuilder mesgText = new StringBuilder();//Russian letters might be used as well !!!

            switch (mailType) {
                case SEND_DIRECT_PRIVATE_EMAIL_TO_ADMINS:
                    p.put("mail.smtp.host", Settings.getSmtpHostNameAdminMsg());
                    p.put("mail.smtp.port", Settings.getSmtpPortAdminMsg());
                    auth = new SMTPAuthenticator(Settings.getMailFromAdminAddress(), Settings.getMailFromAdminAddressPassword());
                    mailFrom = Settings.getMailFromAdminAddress();
                    mailTo = Settings.getLogHealthMonitorEmailsSendTo();
                    subject = Settings.getMailSubjectLogHealthMonitor();
                    mesgText.append("This letter was generated to provide info to admins \n");
                    mesgText.append("******************************************************************* \n");
                    mesgText.append("******************* Details will go here ************************** \n");
                    mesgText.append("******************************************************************* \n");
                    break;
                case LOGS_MESSAGE_TO_ADMINS:
                    p.put("mail.smtp.host", Settings.getSmtpHostNameAdminMsg());
                    p.put("mail.smtp.port", Settings.getSmtpPortAdminMsg());
                    auth = new SMTPAuthenticator(Settings.getMailFromAdminAddress(), Settings.getMailFromAdminAddressPassword());
                    mailFrom = Settings.getMailFromAdminAddress();
                    mailTo = Settings.getEmailToAddress();
                    subject = Settings.getMailSubject();
                    mesgText.append("This letter was generated to provide info to admins \n");
                    mesgText.append("******************************************************************* \n");
                    mesgText.append("Mobile device log message: " + messageText + "\n");
                    mesgText.append("******************************************************************* \n");
                    break;
                case PASSWORD_FB_PLUGIN_AUTH_TO_CLIENT:
                    if (!Settings.isDevelopmentModeEnabled()) {//if true, app runs in prod mode send messages using according email settings
                        p.put("mail.smtp.host", Settings.getSmtpServiceUserHostNameProd());
                        p.put("mail.smtp.port", Settings.getSmtpServiceUserPortProd());
                        auth = new SMTPAuthenticator(Settings.getMailFromServiceUserAddressProd(), Settings.getMailFromServiceUserAddressPasswordProd());
                        mailFrom = Settings.getMailFromServiceUserAddressProd();
                        subject = "BFS Facebook social plugin - game account validation";
                    } else {
                        p.put("mail.smtp.host", Settings.getSmtpServiceUserHostNameDevTest());
                        p.put("mail.smtp.port", Settings.getSmtpServiceUserPortDevTest());
                        auth = new SMTPAuthenticator(Settings.getMailFromServiceUserAddressDevTest(), Settings.getMailFromServiceUserAddressPasswordDevTest());
                        mailFrom = Settings.getMailFromServiceUserAddressDevTest();
                        subject = "BFS Facebook social plugin - game  account validation - DEV/TEST environment";
                    }

                    mailTo = params.get(FacebookSocialPluginAction.PLAYER_EMAIL_TO_GET_SECURE_LINK_TO);

                    mesgText.append("Authorization link. Please click it to authorize in social plugin \n");
                    mesgText.append("******************************************************************* \n");
                    mesgText.append(params.get(FacebookSocialPluginAction.SECURE_LINK_URL_KEY) + "\n");
                    mesgText.append("******************************************************************* \n");
                    mesgText.append(" ");
                    mesgText.append("Thank you.");
                    mesgText.append("Best regards,");
                    mesgText.append("Battlefield-Space Support Team");
                    break;
                default:
                    ;//add default initialization !!!
                    break;
            }



            Session session = Session.getDefaultInstance(p, auth);
            MimeMessage message = new MimeMessage(session);


            message.setFrom(new InternetAddress(mailFrom));

            //InternetAddress mailAddress_TO = new InternetAddress(mailTo);
            message.setRecipients(Message.RecipientType.TO,  InternetAddress.parse(mailTo));


            message.setSubject(subject, "UTF-8");




            MimeBodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText(mesgText.toString(), "UTF-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            status = true;
        } catch (SendFailedException sfe) {
            sfe.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }

        return status;
    }

    /**
     * SimpleAuthenticator is used to do simple authentication
     * when the SMTP server requires it.
     */
    private class SMTPAuthenticator extends javax.mail.Authenticator {

        private String username;
        private String password;

        public SMTPAuthenticator(String mailFrom, String mailFromPassword) {
            this.username = mailFrom;
            this.password = mailFromPassword;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(this.username, this.password);
        }
    }
}
