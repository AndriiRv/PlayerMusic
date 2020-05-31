package com.example.musicplayer.config;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionOutput {

    private ExceptionOutput() {
    }

    public static String exceptionStacktraceToString(Exception e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        ps.close();
        return baos.toString();
    }
}