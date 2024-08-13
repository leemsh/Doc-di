package com.dd.model;


import java.io.*;

public class PillPredictor {
    public record PillPrediction(
            String shape,
            String color1,
            String color2,
            String text1,
            String text2
    ) {
    }

    public static PillPrediction predict(String imagePath) throws IOException {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        ProcessBuilder processBuilder;
        Process process;
        if (isWindows) {
            processBuilder = new ProcessBuilder("cmd.exe", "/c", "python", "./detect.py", imagePath);
        } else {
            processBuilder = new ProcessBuilder("python3", "./detect.py", imagePath);
        }
        processBuilder.directory(new File("./pycode"));
        process = processBuilder.start();

        InputStream inputStream = process.getInputStream();

        return parseDetected(inputStream);
    }

    private static PillPrediction parseDetected(InputStream inputStream) throws IOException {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if (line.startsWith("detected`")) {
                    String[] split = line.split("`");
                    String shape = split[1];
                    String color1 = split[2];
                    String color2 = "";
                    if (color1.contains("|")) {
                        String[] colorSplit = line.split("\\|");
                        color1 = colorSplit[0];
                        color2 = colorSplit[1];
                    }
                    String text1 = split[3];
                    String text2 = "";
                    if (split.length >= 5) {
                        text2 = split[4];
                    }
                    return new PillPrediction(
                            shape,
                            color1,
                            color2,
                            text1,
                            text2
                    );
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        predict("./datasets/test_images/image5.png");
    }
}
