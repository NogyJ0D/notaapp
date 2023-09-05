package com.valentingiarra.notaapp.service;

import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class LogService {
    public void WriteToLog(String method, String endpoint, Object message) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
            String formattedDate = dateFormat.format(new Date());

            FileWriter writer = new FileWriter("log.txt", true);

            // log datetime, method, endpoint and message
            writer.write("[" + formattedDate + "] [" + method + "] [" + endpoint + "] " + message + "\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
