package com.team32.epicwebapp;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.SQLOutput;
import java.util.Properties;
import com.team32.epicwebapp.EpicWebAppApplication;

@Component
public class SSHConnection {

    private final static String S_PATH_FILE_KNOWN_HOSTS = "\\Users\\alonzo\\.ssh\\known_hosts"; //Windows absolute path of known_hosts file
    private final static int LOCAl_PORT = 8989; //Local port, change if needed
    private final static int REMOTE_PORT = 3306;
    private final static int SSH_REMOTE_PORT = 22;
    private final static String SSH_USER = "b8065014"; //University username
    private final static String SSH_PASSWORD = "Qwerty.212"; //University password
    private final static String SSH_REMOTE_SERVER = "linux.cs.ncl.ac.uk";
    private final static String MYSQL_REMOTE_SERVER = "cs-db.ncl.ac.uk";

    private Session session; //represents each ssh session

    public void closeSSH ()
    {
        session.disconnect();
    }


    public SSHConnection () throws Throwable
    {

        if(!EpicWebAppApplication.connected) {

        JSch jsch;

        jsch = new JSch();
        jsch.setKnownHosts(S_PATH_FILE_KNOWN_HOSTS);

        session = jsch.getSession(SSH_USER, SSH_REMOTE_SERVER, SSH_REMOTE_PORT);
        session.setPassword(SSH_PASSWORD);
        Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);


            session.connect(); //ssh connection established!
            System.out.println("Connected to SSH");

            //by security policy, you must connect through a fowarded port
            session.setPortForwardingL(LOCAl_PORT, MYSQL_REMOTE_SERVER, REMOTE_PORT);
            System.out.println("Port forwarded");
        }

    }
}
