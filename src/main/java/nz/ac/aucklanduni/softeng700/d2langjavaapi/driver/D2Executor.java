package nz.ac.aucklanduni.softeng700.d2langjavaapi.driver;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class D2Executor {

    private static final String EXPORT_DIRECTORY = "export";

    public D2Executor() {
        setupD2Lib();
    }

    public String runBuildCommand(File d2File, String outputFileName) throws RuntimeException {
        String fileType = "png";  // Can also be jpg or svg
        String imageFileName = outputFileName + "." + fileType;

        String executablePath = setupD2Lib();
        ProcessBuilder pb = new ProcessBuilder(executablePath,
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

    private String setupD2Lib() {
        try {
            File file = new File("./temp/d2-blueprint4j-lib");
            file.setExecutable(true);
            if (!file.exists()) {
                new File("temp").mkdir();
                InputStream binIn = getClass().getResourceAsStream("/d2-blueprint4j-lib");
                OutputStream binOut = new FileOutputStream(file);
                IOUtils.copy(binIn, binOut);
            }

            return file.getPath();
        } catch (IOException e) {
            throw new RuntimeException("Could not make copy of D2 binary!");
        }
    }
}
