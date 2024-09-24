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
            String name,
            int x,
            int y,
            int w,
            int h,
            float prob
    ) {
    }

    private static final String MODEL = "model/yolov8.onnx";
    private static final String PILL_NAMES_FILE = "model/labels.txt";

    private static final long[] INPUT_SHAPE = new long[]{1, 3, 640, 640};
    private static final float PROB_THRESHOLD = 0.4F;

    private static final String[] IDX_TO_PILL_NAME = new String[101];
    private static boolean isPillNameLoaded = false;

    private static final OrtEnvironment ortEnv = OrtEnvironment.getEnvironment();

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

    private static BufferedImage loadImage(String imagePath, int targetWidth, int targetHeight) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(imagePath));

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // 원본 이미지의 종횡비를 계산합니다.
        double aspectRatio = (double) originalWidth / originalHeight;

        // 타겟 이미지 크기에 맞게 맞춰진 너비와 높이를 계산합니다.
        int newWidth;
        int newHeight;

        if (originalWidth > originalHeight) {
            newWidth = targetWidth;
            newHeight = (int) (targetWidth / aspectRatio);
        } else {
            newHeight = targetHeight;
            newWidth = (int) (targetHeight * aspectRatio);
        }

        // 새 이미지 생성 (배경은 검정색)
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();

        // 배경색 설정
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, targetWidth, targetHeight);

        // 이미지 크기를 조정하고 중앙에 맞춥니다.
        int x = (targetWidth - newWidth) / 2;
        int y = (targetHeight - newHeight) / 2;
        g.drawImage(originalImage, x, y, newWidth, newHeight, null);
        g.dispose();

        return resizedImage;
    }

    private static void saveDetection(String imagePath, PillPrediction prediction) throws IOException {
        BufferedImage image = loadImage(imagePath, (int) INPUT_SHAPE[2], (int) INPUT_SHAPE[3]);
        Graphics2D g2d = image.createGraphics();
        int x1 = prediction.x - prediction.w / 2;
        int y1 = prediction.y - prediction.h / 2;
        g2d.drawRect(x1, y1, prediction.w, prediction.h);
        g2d.drawString(prediction.name, prediction.x, prediction.y);
        g2d.dispose();

        ImageIO.write(image, "png", new File(String.format("detect/%s_%d_%d_%d_%d_%f.png", prediction.name, prediction.x, prediction.y, prediction.w, prediction.h, prediction.prob)));
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

    private static List<PillPrediction> detect(OnnxTensor inputTensor, OrtSession session, float threshold) throws OrtException {
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

        Map<PillPrediction, Float> labels = new HashMap<>();

        for (int capture = 0; capture < captureCount; capture++) {
            int center_x = (int) output[0][0][capture];
            int center_y = (int) output[0][1][capture];
            int width = (int) output[0][2][capture];
            int height = (int) output[0][3][capture];

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

            if (maxProb < 0.1) {
                continue;
            }
            labels.put(new PillPrediction(
                    IDX_TO_PILL_NAME[bestLabel],
                    center_x,
                    center_y,
                    width,
                    height,
                    maxProb
            ), maxProb);

        }

        List<PillPrediction> detections = new ArrayList<>();
        labels.keySet().stream()
                .sorted((o1, o2) -> labels.get(o2).compareTo(labels.get(o1)))
                .forEach(detections::add);

        return detections;
    }

    public static List<PillPrediction> predict(String imagePath) {
        try {
            OnnxTensor inputTensor = imageToTensor(imagePath);
            OrtSession ortSession = getOrtSession();
            Map<String, PillPrediction> predictions = new HashMap<>();

            float threshold = PROB_THRESHOLD;
            List<PillPrediction> results;
            while ((results = detect(inputTensor, ortSession, threshold)).isEmpty() && threshold > 0.05) {
                threshold *= 0.9F;
            }
            for (PillPrediction pillPrediction : results) {
                if (!predictions.containsKey(pillPrediction.name)) {
                    predictions.put(pillPrediction.name, pillPrediction);
                }
            }
            return predictions.values().stream().toList();
        } catch (IOException | OrtException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        String imagePath = "D:\\K-001900-010224-016551-029345_0_2_0_2_70_000_200.png";
        for (PillPrediction prediction : predict(imagePath)) {
            System.out.println(String.format("detect/%s_%d_%d_%d_%d_%f.png", prediction.name, prediction.x, prediction.y, prediction.w, prediction.h, prediction.prob));
            saveDetection(imagePath, prediction);
        }
    }
}
