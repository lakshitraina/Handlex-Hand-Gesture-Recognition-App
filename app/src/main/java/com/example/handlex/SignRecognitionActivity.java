package com.example.handlex;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class SignRecognitionActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_CODE = 100;
    private SurfaceView cameraPreview;
    private TextView detectedText;
    private Interpreter tflite;
    private CameraHandler cameraHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_recognition);

        cameraPreview = findViewById(R.id.cameraPreview);
        detectedText = findViewById(R.id.detectedText);
        Button startRecognition = findViewById(R.id.startRecognition);
        Button stopRecognition = findViewById(R.id.stopRecognition);

        // Request Camera Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            initializeCamera();
        }

        // Initialize TensorFlow Lite model
        initializeModel();

        // Start recognition
        startRecognition.setOnClickListener(v -> {
            if (cameraHandler != null) {
                cameraHandler.startCamera();
            }
        });

        // Stop recognition
        stopRecognition.setOnClickListener(v -> {
            if (cameraHandler != null) {
                cameraHandler.stopCamera();
            }
        });
    }

    private void initializeCamera() {
        cameraHandler = new CameraHandler(this, cameraPreview, bitmap -> classifyGestureAsync(bitmap));
    }

    private void initializeModel() {
        try {
            tflite = new Interpreter(loadModelFile());
        } catch (Exception e) {
            Log.e("SignRecognition", "Error initializing TensorFlow Lite model: " + e.getMessage());
            tflite = null;
        }
    }

    private MappedByteBuffer loadModelFile() throws Exception {
        FileInputStream fileInputStream = new FileInputStream(getAssets().openFd("model.tflite").getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
    }

    private String classifyGesture(Bitmap bitmap) {
        float[][] output = new float[1][10]; // Adjust based on model output classes
        tflite.run(bitmapToFloatArray(bitmap), output);

        int maxIndex = 0;
        for (int i = 0; i < output[0].length; i++) {
            if (output[0][i] > output[0][maxIndex]) {
                maxIndex = i;
            }
        }

        String[] gestureLabels = loadGestureLabels();
        return gestureLabels[maxIndex];
    }

    private float[][][][] bitmapToFloatArray(Bitmap bitmap) {
        int width = 224, height = 224; // Model input size
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        float[][][][] input = new float[1][width][height][3];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = resizedBitmap.getPixel(x, y);
                input[0][x][y][0] = (pixel >> 16 & 0xFF) / 255.0f;
                input[0][x][y][1] = (pixel >> 8 & 0xFF) / 255.0f;
                input[0][x][y][2] = (pixel & 0xFF) / 255.0f;
            }
        }
        return input;
    }

    private String[] loadGestureLabels() {
        return getResources().getStringArray(R.array.gesture_labels);
    }

    private void classifyGestureAsync(Bitmap bitmap) {
        new Thread(() -> {
            String prediction = classifyGesture(bitmap);
            runOnUiThread(() -> detectedText.setText("Detected Text: " + prediction));
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeCamera();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tflite != null) {
            tflite.close();
            tflite = null;
        }
    }
}