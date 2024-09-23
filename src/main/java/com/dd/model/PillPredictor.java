package com.dd.model;


import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PillPredictor {
    public record PillPrediction(
            String name
    ) {
    }

    private static final String MODEL = "model/yolov8.onnx";
    private static final String PILL_NAMES_FILE = "model/labels.txt";

    private static final long[] INPUT_SHAPE = new long[]{1, 3, 640, 640};

    private static String[] IDX_TO_PILL_NAME = new String[101];
    private static boolean isPillNameLoaded = false;

    private static OrtEnvironment ortEnv = OrtEnvironment.getEnvironment();

    private static void loadPillNames() {
        try {
            List<String> lines = Files.readAllLines(new File(PILL_NAMES_FILE).toPath(), StandardCharsets.UTF_8);
            for (int i = 0; i < lines.size(); i++) {
                IDX_TO_PILL_NAME[i] = lines.get(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage loadImage(String imagePath, int width, int height) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));

        Image tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return scaledImage;
    }

    private static OnnxTensor imageToTensor(String imagePath) throws IOException, OrtException {
        BufferedImage image = loadImage(imagePath, (int) INPUT_SHAPE[2], (int) INPUT_SHAPE[3]);
        int width = image.getWidth();
        int height = image.getHeight();

        float[][][][] floatArray = new float[(int) INPUT_SHAPE[0]][(int) INPUT_SHAPE[1]][(int) INPUT_SHAPE[2]][(int) INPUT_SHAPE[3]]; // 3 채널(RGB)

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                floatArray[0][0][y][x] = color.getRed() / 255.0f;   // R
                floatArray[0][1][y][x] = color.getGreen() / 255.0f; // G
                floatArray[0][2][y][x] = color.getBlue() / 255.0f;  // B
            }
        }

        OnnxTensor tensor = OnnxTensor.createTensor(ortEnv, floatArray);
        return tensor;
    }

    private static OrtSession getOrtSession() throws OrtException {
        return ortEnv.createSession(MODEL, new OrtSession.SessionOptions());
    }

    private static List<PillPrediction> detect(OnnxTensor inputTensor, OrtSession session) throws OrtException {
        if (!isPillNameLoaded) {
            loadPillNames();
            isPillNameLoaded = true;
        }

        int labelCount = IDX_TO_PILL_NAME.length;
        int offsetToLabel = 4;  // output format is x, y, w, h, class-0 ... class-N

        Map<String, OnnxTensor> inputs = new HashMap<>();
        inputs.put("images", inputTensor);
        session.getOutputNames();

        OrtSession.Result result = session.run(inputs);
        float[][][] output = (float[][][]) result.get(0).getValue();
        int captureCount = output[0][0].length;

        Map<String, Float> labels = new HashMap<>();

        for (int capture = 0; capture < captureCount; capture++) {
//            int center_x = (int) output[0][0][capture];
//            int center_y = (int) output[0][1][capture];
//            int width = (int) output[0][2][capture];
//            int height = (int) output[0][3][capture];

            int bestLabel = offsetToLabel;
            float maxProb = output[0][bestLabel][capture];

            for (int label = offsetToLabel; label < labelCount + offsetToLabel; label++) {
                float prob = output[0][label][capture];
                if (prob > maxProb) {
                    bestLabel = label;
                    maxProb = prob;
                }
            }
            bestLabel -= offsetToLabel;

            if (maxProb < 0.4) {
                continue;
            }
            labels.put(IDX_TO_PILL_NAME[bestLabel], maxProb);
        }

        List<PillPrediction> detections = new ArrayList<>();
        labels.keySet().stream()
                .sorted((o1, o2) -> labels.get(o2).compareTo(labels.get(o1)))
                .forEach(label -> detections.add(new PillPrediction(label)));

        return detections;
    }

    public static PillPrediction predict(String imagePath) {
        try {
            OnnxTensor inputTensor = imageToTensor(imagePath);
            OrtSession ortSession = getOrtSession();
            return detect(inputTensor, ortSession).get(0);
        } catch (IOException | OrtException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        predict("F:\\zer0ken\\관절약.jpg");
    }
}
