package nz.ac.aucklanduni.softeng700.d2langjavaapi.driver;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import nz.ac.aucklanduni.softeng700.d2langjavaapi.diagram.style.ColourTheme;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.diagram.style.LayoutEngine;

public class D2Executor {

    private static final String EXPORT_DIRECTORY = "export";

    private LayoutEngine layoutEngine = LayoutEngine.DAGRE;
    private ColourTheme theme = ColourTheme.COLOURBLIND_CLEAR;

    // TODO: Add more supported architectures w/ accompanying binaries
    private final Map<String, String> osToBinaryMappings = Map.of(
            "aarch64", "d2-blueprint4j-lib-arm64",
            "x86_64", "d2-blueprint4j-lib-amd64"
    );

    public D2Executor() {
        setupD2Lib();
    }

    public void setLayoutEngine(LayoutEngine layoutEngine) {
        this.layoutEngine = layoutEngine;
    }

    public void setTheme(ColourTheme theme) {
        this.theme = theme;
    }

    public String runBuildCommand(File d2File, String outputFileName) throws RuntimeException {
        String fileType = "png";  // Can also be jpg or svg
        String imageFileName = outputFileName + "." + fileType;

        String executablePath = setupD2Lib();
        ProcessBuilder pb = new ProcessBuilder(executablePath,
                                               d2File.getPath(),
                                               EXPORT_DIRECTORY + "/" + imageFileName);

        pb.environment().put("D2_THEME", theme.getSelector());
        pb.environment().put("D2_LAYOUT", layoutEngine.getSelector());

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
        // Determine the right D2 binary by system architecture
        String localOSArch = System.getProperty("os.arch");
        String binaryFilename = osToBinaryMappings.get(localOSArch);
        if (binaryFilename == null) {
            throw new RuntimeException("Unsupported runtime architecture for D2: " + localOSArch);
        }

        try {
            File file = new File("./temp/" + binaryFilename);
            file.setExecutable(true);
            if (!file.exists()) {
                new File("temp").mkdir();
                InputStream binIn = getClass().getClassLoader().getResourceAsStream(binaryFilename);
                OutputStream binOut = new FileOutputStream(file);
                IOUtils.copy(binIn, binOut);
            }

            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not make copy of D2 binary!");
        }
    }
}
