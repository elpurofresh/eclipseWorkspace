package com.eeril.bentoBox;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
 
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
 
//import com.sun.mail.pop3.POP3Store;

/**
* A utility class the retrieves e-mail messages from a POP3 server
* @author Prasanth Harpanahalli
*
*/
public class ReceiveEmail {
    
    /**
     * Retrieves new incoming e-mail messages from inbox of a POP3 account
     * @param host host name of the POP3 server
     * @param port port number of the server
     * @param userName user name of the e-mail account
     * @param password password of the e-mail account
     * @throws MessagingException
     * @throws IOException
     */
    public void getEmail(String host, String port, String userID, String password)
            throws MessagingException, IOException {
        // sets POP3 properties
        Properties properties = new Properties();
        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", port);
        
        // sets POP3S properties
        properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.pop3.socketFactory.fallback", "false");       
        properties.setProperty("mail.pop3.socketFactory.port", "995");
        
        
        // connects to the POP3 server
        Session session = Session.getDefaultInstance(properties);
        Store store = session.getStore("pop3");
        store.connect(userID, password);
        
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        Message[] mails = inbox.getMessages();
        
        //System.out.println("No of mails in inbox: "+messages.length);
        for (int mailCount = 0; mailCount < mails.length; mailCount++) {
            Message aMail = mails[mailCount];
            String senderAddress = aMail.getFrom()[0].toString();
            String subject = aMail.getSubject();
            
            // parses recipient address in To field
            String toAddresses = "";
            Address[] toList = aMail.getRecipients(RecipientType.TO);
            if (toList != null) {
                for (int toCount = 0; toCount < toList.length; toCount++) {
                    toAddresses += toList[toCount].toString() + ", ";
                }
            }   
            if (toAddresses.length() > 1) {
                toAddresses = toAddresses.substring(0, toAddresses.length() - 2);
            }
            
            // parses recipient addresses in CC field
/*            String ccAddresses = "";
            Address[] listCC = aMail.getRecipients(RecipientType.CC);
            if (listCC != null) {
                for (int ccCount = 0; ccCount < listCC.length; ccCount++) {
                    ccAddresses = listCC[ccCount].toString() + ", ";
                }
            }           
            if (ccAddresses.length() > 1) {
                ccAddresses = ccAddresses.substring(0, ccAddresses.length() - 2);
            } */          
            
            String sentDate = aMail.getSentDate().toString();
            
            String contentType = aMail.getContentType();
            String textMessage = "";
            String attachFiles = "";
            
            if (contentType.contains("text/plain") || contentType.contains("text/html")) {
                textMessage = aMail.getContent() != null ? aMail.getContent().toString() : "";
            } else if (contentType.contains("multipart")) {
                Multipart multiPart = (Multipart) aMail.getContent();
                int numberOfParts = multiPart.getCount();
                for (int partCount = 0; partCount < numberOfParts; partCount++) {
                    BodyPart part = multiPart.getBodyPart(partCount);
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        attachFiles += part.getFileName() + ", ";
                        storeAttachment(part);
                    } else {
                        textMessage = part.getContent() != null ? part.getContent().toString() : "";
                    }
                }
                
                if (attachFiles.length() > 1) {
                    attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                }
            }   
            
            // prints out the message details
            System.out.println("Message #" + mailCount + ":");
            System.out.println("\t From: " + senderAddress);
            System.out.println("\t To: " + toAddresses);
//          System.out.println("\t CC: " + ccAddresses);
            System.out.println("\t Subject: " + subject);
            System.out.println("\t Sent Date: " + sentDate);
            System.out.println("\t Message: " + textMessage);
            System.out.println("\t Attachments: " + attachFiles);
            System.out.println("--------------------------------------");
            
        }
        
        // closes the folder and disconnects from the server
        inbox.close(false);
        store.close();       
        
    }
    
    /**
     * Saves an attachment part to a file on disk
     * @param part a part of the e-mail's multipart content.
     * @throws MessagingException
     * @throws IOException
     */
    private void storeAttachment(BodyPart part) throws MessagingException, IOException {
        String destFilePath = "D:/Attachments/" + part.getFileName();
        
        FileOutputStream output = new FileOutputStream(destFilePath);
        
        InputStream input = part.getInputStream();
        
        byte[] buffer = new byte[4096];
        
        int byteRead;
        
        while ((byteRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, byteRead);
        }
        output.close();
    }
}