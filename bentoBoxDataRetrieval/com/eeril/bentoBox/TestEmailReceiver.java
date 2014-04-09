package com.eeril.bentoBox;

import java.io.IOException;

import javax.mail.MessagingException;
 
public class TestEmailReceiver {
    public static void main(String... args) throws MessagingException, IOException {
        String host = "pop.gmail.com";
        String port = "995";
        String userName = "eeriladm@gmail.com";
        String password = "Sese123!";
        
        ReceiveUnreadMail emailUtil = new ReceiveUnreadMail();
        emailUtil.getEmail(host, port, userName, password);
    }
}