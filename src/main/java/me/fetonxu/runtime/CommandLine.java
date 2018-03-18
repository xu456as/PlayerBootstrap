package me.fetonxu.runtime;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLine {
    public static String startPlayer(String absolutePath, int port, long userId) throws IOException{

        Process process = Runtime.getRuntime().exec(absolutePath);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String retVal = bufferedReader.readLine();

        return retVal;

    }
}
