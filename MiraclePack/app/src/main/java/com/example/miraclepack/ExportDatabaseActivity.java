package com.example.miraclepack;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ExportDatabaseActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> filePickerLauncher;
    private Uri selectedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exportDatabase();
    }

    private void exportDatabase() {
        // Create a file picker launcher
        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                selectedUri = data.getData();
                                performExport();
                            }
                        } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                            finish();
                        }
                    }
                });

        // Launch the file picker
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        filePickerLauncher.launch(intent);
    }

    private void performExport() {
        if (selectedUri != null) {
            try {
                // Get the current database file
                File currentDB = getDatabasePath("MiraclePack.db");

                // Get the document URI for the selected directory
                String directoryPath = getDirectoryPath(selectedUri);

                // Create the export file name and path
                String exportFileName = "MiraclePack.db";
                String exportFilePath = directoryPath + "/" + exportFileName;

                // Create the export file
                DocumentFile exportDocument = DocumentFile.fromTreeUri(this, selectedUri);
                DocumentFile exportFile = exportDocument.createFile("application/octet-stream", exportFileName);

                // Open output stream for the export file
                OutputStream outputStream = getContentResolver().openOutputStream(exportFile.getUri());

                // Open input stream for the current database file
                FileInputStream inputStream = new FileInputStream(currentDB);
                byte[] buffer = new byte[8192];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                // Close input and output streams
                inputStream.close();
                outputStream.close();

                Toast.makeText(this, "Database geëxporteerd naar: " + exportFilePath, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                finish();
            }
        }
    }

    private String getDirectoryPath(Uri uri) {
        String directoryPath = null;
        if (DocumentsContract.isTreeUri(uri)) {
            String documentId = DocumentsContract.getTreeDocumentId(uri);
            Uri documentUri = DocumentsContract.buildDocumentUriUsingTree(uri, documentId);
            ContentResolver resolver = getContentResolver();
            try {
                DocumentFile documentFile = DocumentFile.fromTreeUri(this, documentUri);
                if (documentFile != null && documentFile.isDirectory()) {
                    directoryPath = documentFile.getUri().toString();
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return directoryPath;
    }
}
