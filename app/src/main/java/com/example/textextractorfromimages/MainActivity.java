package com.example.textextractorfromimages;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText recgText;
    private ImageView copy, clear, getImage, chatGpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        recgText = findViewById(R.id.recgText);
        copy = findViewById(R.id.copy);
        clear = findViewById(R.id.clear);
        getImage = findViewById(R.id.getImage);
        chatGpt = findViewById(R.id.chatGpt); // ChatGPT button

        Toast.makeText(this, "This app is developed by Jk with lots of ❤", Toast.LENGTH_SHORT).show();

        // Handle image selection
        getImage.setOnClickListener(v -> ImagePicker.with(MainActivity.this)
                .crop() // Crop image(Optional)
                .compress(1024) // Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080) // Final image resolution will be less than 1080 x 1080(Optional)
                .start());

        // Handle text copy
        copy.setOnClickListener(v -> {
            String extractedText = recgText.getText().toString();
            if (!extractedText.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Extracted Text", extractedText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "No text to copy", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle text clear
        clear.setOnClickListener(v -> recgText.setText(""));

        // Handle ChatGPT button click
        chatGpt.setOnClickListener(v -> {
            String extractedText = recgText.getText().toString();
            if (!extractedText.isEmpty()) {
                // Copy text to clipboard
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Extracted Text", extractedText);
                clipboard.setPrimaryClip(clip);

                // Show toast message
                Toast.makeText(MainActivity.this, "Text copied to clipboard. Opening ChatGPT....", Toast.LENGTH_LONG).show();

                // Open ChatGPT in a browser
                String url = "https://chat.openai.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "No text to send", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                // Initialize Firebase ML Kit Text Recognition
                InputImage image = InputImage.fromFilePath(this, imageUri);
                TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

                recognizer.process(image)
                        .addOnSuccessListener(text -> {
                            // Display recognized text in EditText
                            recgText.setText(text.getText());
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(MainActivity.this, "Failed to recognize text", Toast.LENGTH_SHORT).show();
                        });

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Image selection canceled", Toast.LENGTH_SHORT).show();
        }
    }
}
