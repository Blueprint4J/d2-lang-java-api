package nz.ac.aucklanduni.softeng700.d2langjavaapi.driver;

import java.io.File;
import java.io.IOException;

public class D2Executor {

    private static final String EXPORT_DIRECTORY = "export";

    public static String runBuildCommand(File d2File, String outputFileName) throws RuntimeException {
        String fileType = "png";  // Can also be jpg or svg
        String imageFileName = outputFileName + "." + fileType;

        ProcessBuilder pb = new ProcessBuilder("d2",
                                               d2File.getPath(),
                                               EXPORT_DIRECTORY + "/" + imageFileName);

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

        return EXPORT_DIRECTORY + "/" + imageFileName;
    }
}
