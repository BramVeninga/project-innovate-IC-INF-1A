package com.example.miraclepack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImportDatabaseActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        importDatabase();
    }

    private void importDatabase() {
        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri uri = data.getData();
                                importDatabaseFromUri(uri);
                            } else {
                                Toast.makeText(ImportDatabaseActivity.this, "Failed to pick the file.", Toast.LENGTH_SHORT).show();
                            }
                        } else if (result.getResultCode() == RESULT_CANCELED) {
                            finish();
                        }
                    }
                });

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        filePickerLauncher.launch(intent);
    }

    private void importDatabaseFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File currentDB = getDatabasePath("MiraclePack.db");
            File importFile = new File(currentDB.getParent(), "MiraclePack.db");

            copyFile(inputStream, new FileOutputStream(importFile));
            inputStream.close();

            Toast.makeText(this, "Database succesvol geïmporteerd.", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Fout bij het importeren van de database.", Toast.LENGTH_LONG).show();
        }

        finish();
    }

    private void copyFile(InputStream inputStream, FileOutputStream outputStream) throws IOException {
        byte[] buffer = new byte[8192];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
    }
}
