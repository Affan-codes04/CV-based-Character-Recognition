package com.example.got_character_prediction;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.tensorflow.lite.Interpreter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {
    private ImageView uploadedImage, sigil;
    private Button upload;
    private TextView result;
    private Interpreter tflite;
    private MediaPlayer mediaPlayer;
    private static final int IMAGE_SIZE = 150; // Model's expected input size
    private static final int PICK_IMAGE = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadedImage = findViewById(R.id.uploadedImage);
        sigil = findViewById(R.id.sigil);
        upload = findViewById(R.id.submit);
        result = findViewById(R.id.result);

        // Start playing music
        mediaPlayer = MediaPlayer.create(this, R.raw.got);
        mediaPlayer.setLooping(true); // Keep playing in a loop
        mediaPlayer.start();

        // Load the TensorFlow Lite model
        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading model", Toast.LENGTH_SHORT).show();
        }

        // Image selection button
        upload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Free up media player resources
            mediaPlayer = null;
        }
    }

    // Handle selected image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                uploadedImage.setImageBitmap(bitmap);

                // Run prediction
                String characterName = predictCharacter(bitmap);
                result.setText("Predicted Character: " + characterName);
                result.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error processing image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Load TFLite model from assets
    private ByteBuffer loadModelFile() throws IOException {
        InputStream inputStream = getAssets().open("got_character_model.tflite");
        byte[] modelBytes = new byte[inputStream.available()];
        inputStream.read(modelBytes);
        ByteBuffer buffer = ByteBuffer.allocateDirect(modelBytes.length);
        buffer.order(ByteOrder.nativeOrder());
        buffer.put(modelBytes);
        return buffer;
    }

    // Process image and make prediction
    private String predictCharacter(Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, IMAGE_SIZE, IMAGE_SIZE, true);
        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(IMAGE_SIZE * IMAGE_SIZE * 3 * 4);
        inputBuffer.order(ByteOrder.nativeOrder());

        for (int y = 0; y < IMAGE_SIZE; y++) {
            for (int x = 0; x < IMAGE_SIZE; x++) {
                int pixel = resizedBitmap.getPixel(x, y);
                inputBuffer.putFloat(((pixel >> 16) & 0xFF) / 255.0f); // Red
                inputBuffer.putFloat(((pixel >> 8) & 0xFF) / 255.0f);  // Green
                inputBuffer.putFloat((pixel & 0xFF) / 255.0f);         // Blue
            }
        }

        // Fix: Change output shape to [1, 15]
        float[][] output = new float[1][15];
        tflite.run(inputBuffer, output);

        // Get index of highest probability
        int predictedIndex = getMaxIndex(output[0]);
        return getCharacterName(predictedIndex);
    }

    // Find index with the highest probability
    private int getMaxIndex(float[] arr) {
        int maxIndex = 0;
        float maxVal = arr[0];

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > maxVal) {
                maxVal = arr[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    // Map index to character names
    private String getCharacterName(int index) {
        String[] characterNames = {"Arya Stark", "Daenerys Targaryen", "Jaime Lannister", "Jaimie Fookin Lannister",
                "Jon Snow", "Sansa Stark", "Tyrion Lannister", "Tyrion Lannister", "Arya Stark", "Cersie Lannister",
                "Danaerys Targaryen", "Aegon Targaryen", "Lord Eddard Stark", "Peter Baelish", "Sansa Stark"};

        if (index >= 0 && index < characterNames.length) {

            if (index == 0 || index == 5 || index == 8 || index == 12 || index == 14){
                sigil.setImageResource(R.drawable.hs);
            }
            else if (index == 2 || index == 3 || index == 6 || index == 7 || index == 9 || index == 13){
                sigil.setImageResource(R.drawable.hl);
            }
            else{
                sigil.setImageResource(R.drawable.ht);
            }

            return characterNames[index];
        } else {
            return "Unknown";
        }
    }
}
