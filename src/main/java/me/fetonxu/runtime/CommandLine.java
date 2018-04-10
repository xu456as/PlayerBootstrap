package me.fetonxu.runtime;


import java.io.*;

public class CommandLine {
    public static String startPlayer(String absolutePath, int port, long userId) throws IOException{

        String cmd = String.format("%s %d", absolutePath, port);

        Process process = Runtime.getRuntime().exec(cmd);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String retVal = bufferedReader.readLine();

        return retVal;

    }

    public static boolean existPort(int port)throws IOException, InterruptedException{
        String cmd = String.format("lsof -i:%d", port);
        System.out.println(cmd);
        Process process = Runtime.getRuntime().exec(cmd);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String retVal = bufferedReader.readLine();

        return retVal != null;

    }
}
