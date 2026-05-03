package com.school.util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class AppLogger {

    private static final String LOG_FILE = "data/app.log";
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String level, String message) {
        String entry = String.format("[%s] [%s] %s",
                LocalDateTime.now().format(FMT), level, message);
        System.out.println(entry);
        try {
            new File("data").mkdirs();
            try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
                pw.println(entry);
            }
        } catch (IOException ignored) {}
    }
}
