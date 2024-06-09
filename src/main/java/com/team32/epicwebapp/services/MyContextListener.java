package com.team32.epicwebapp.services;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyContextListener implements ServletContextListener {

    private com.team32.epicwebapp.SSHConnection sshConnection;


    public MyContextListener() {
        super();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("Context initialized ... !");
        try {
            sshConnection = new com.team32.epicwebapp.SSHConnection();
        } catch (Throwable e) {
            e.printStackTrace(); // error connecting SSH server
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("Context destroyed ... !");
        sshConnection.closeSSH(); // disconnect
    }

}
