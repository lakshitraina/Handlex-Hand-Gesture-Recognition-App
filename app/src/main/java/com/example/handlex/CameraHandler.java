package com.example.handlex;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraHandler {
    private final Context context;
    private final SurfaceView surfaceView;
    private ProcessCameraProvider cameraProvider;
    private final ExecutorService cameraExecutor = Executors.newSingleThreadExecutor();
    private final FrameProcessor frameProcessor;

    public interface FrameProcessor {
        void processFrame(Bitmap bitmap);
    }

    public CameraHandler(Context context, SurfaceView surfaceView, FrameProcessor frameProcessor) {
        this.context = context;
        this.surfaceView = surfaceView;
        this.frameProcessor = frameProcessor;
    }

    public void startCamera() {
        ProcessCameraProvider.getInstance(context).addListener(() -> {
            try {
                cameraProvider = ProcessCameraProvider.getInstance(context).get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(surfaceRequest -> {
                    surfaceRequest.provideSurface(surfaceView.getHolder().getSurface(), cameraExecutor, result -> {
                        // Handle the result if needed (e.g., log or clean up resources)
                    });
                });

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, preview);

                // Capture frames for processing
                captureFrames();
            } catch (Exception e) {
                Log.e("CameraHandler", "Error starting camera: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(context));
    }

    private void captureFrames() {
        // Simulate capturing frames from the camera
        cameraExecutor.execute(() -> {
            while (true) {
                Bitmap bitmap = surfaceView.getDrawingCache(); // Replace with actual frame capture logic
                if (bitmap != null && frameProcessor != null) {
                    frameProcessor.processFrame(bitmap);
                }
            }
        });
    }

    public void stopCamera() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
        cameraExecutor.shutdown();
    }
}