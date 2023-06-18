package nz.ac.aucklanduni.softeng700.d2langjavaapi.driver;

import java.io.IOException;

public class D2Executor {

    public static void runBuildCommand(String fileName) throws RuntimeException {
        ProcessBuilder pb = new ProcessBuilder("d2", fileName, fileName.split("\\.")[0] + ".png");

        try {
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("D2 compilation command failed with exit code: " + exitCode);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
