package nz.ac.aucklanduni.softeng700.d2langjavaapi.driver;

import java.io.IOException;

public class D2Executor {

    public static void runBuildCommand(String d2FileName, String outputFileName) throws RuntimeException {
        String fileType = "png";  // Can also be jpg or svg
        String imageFileName = outputFileName + "." + fileType;

        ProcessBuilder pb = new ProcessBuilder("d2", "temp/" + d2FileName, "export/" + imageFileName);

        pb.environment().put("D2_THEME", "4");
        pb.environment().put("D2_LAYOUT", "dagre");

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
