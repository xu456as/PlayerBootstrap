package me.fetonxu.runtime;


import jdk.nashorn.internal.runtime.ECMAException;
import org.omg.PortableInterceptor.INACTIVE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class CommandLine {

    private final static Logger logger = LoggerFactory.getLogger(CommandLine.class);

    private final static String DIRECTORY = "src/main/resources";

    public static String startPlayer(String absolutePath, int port, long userId) throws IOException{

//        String cmd = String.format("%s %d", absolutePath, port);
//
//        Process process = Runtime.getRuntime().exec(cmd);
//
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//        String retVal = bufferedReader.readLine();
//
//        return retVal;

        ProcessBuilder pBuilder = new ProcessBuilder(absolutePath, String.valueOf(port));
        Process process = pBuilder.start();
        return receiveOutput(process.getInputStream());

    }

    public static boolean existPort(int port)throws IOException, InterruptedException{

        ProcessBuilder pBuilder = new ProcessBuilder("shell/existPort.sh", String.valueOf(port));
        pBuilder.directory(new File(DIRECTORY));
        Process process = pBuilder.start();

        String output = receiveOutput(process.getInputStream());
        int resultInt = -1;
        String result = null;
        try{
            String[] outputList = output.split("\n");
            result = outputList[outputList.length - 1];
            resultInt = Integer.parseInt(result);
        }catch (Exception e){
            logger.error(String.format("check output error, result is %s", result));
        }
        return resultInt != 0;
    }

    public static String copyFile(String src, String dest) throws IOException{
        ProcessBuilder pBuilder = new ProcessBuilder("cp", "-r", src, dest);
        pBuilder.directory(new File(DIRECTORY));
        System.out.println(pBuilder.directory().toString());
        Process process = pBuilder.start();
        return receiveOutput(process.getInputStream());
    }

    public static String compilePlayer(long userId) throws Exception{
        String path = String.format("/data/tank/%d/build.xml", userId);
        ProcessBuilder pBuilder = new ProcessBuilder("ant", "-buildfile", path, "compile");
        Process process = pBuilder.start();
        return receiveOutput(process.getInputStream());
    }

    private static String receiveOutput(InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder buffer = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append('\n');
            }
        }catch (Exception e){
            logger.error(String.format("Command line receive output error, %s", e));
        }
        return buffer.toString();
    }

    public static void main(String[] args) throws Exception{
        System.out.println(compilePlayer(1234));

    }
}
