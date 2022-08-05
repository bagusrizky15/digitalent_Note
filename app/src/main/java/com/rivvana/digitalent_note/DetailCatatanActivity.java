package com.rivvana.digitalent_note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class DetailCatatanActivity extends AppCompatActivity {

        EditText etCatatan;
        Button btnUpdate, btnDelete;
        String fileName;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail_catatan);

            fileName = getIntent().getStringExtra("FILENAME");
            getSupportActionBar().setTitle(fileName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            etCatatan = findViewById(R.id.et_catatan);
            btnUpdate = findViewById(R.id.btn_update);
            btnDelete = findViewById(R.id.btn_delete);

            readData();

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hapusFile();
                    finish();
                }
            });

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateFile();
                    finish();
                }
            });

        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    this.finish();
                    return true;
            }
            return true;
        }

        private void readData() {
            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                return;
            }
            File dir = new File(Environment.getExternalStorageDirectory() + "/Catatan Harian");
            File file = new File(dir, fileName);
            if (file.exists()) {
                StringBuilder text = new StringBuilder();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line = br.readLine();
                    while (line != null) {
                        text.append(line);
                        line = br.readLine();
                    }
                    br.close();
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    System.out.println("Error " + e.getMessage());
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                etCatatan.setText(text.toString());
            } else {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            }
        }

        private void hapusFile() {
            File file = new File(Environment.getExternalStorageDirectory() + "/Catatan Harian", fileName);
            if (file.exists()) {
                file.delete();
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            }
        }

        public void updateFile() {
            File files = new File(Environment.getExternalStorageDirectory() + "/Catatan Harian", fileName);
            if (files.exists()) {
                String catatanFile = etCatatan.getText().toString();
                String state = Environment.getExternalStorageState();
                if (!Environment.MEDIA_MOUNTED.equals(state)) {
                    return;
                }
                File file = new File(Environment.getExternalStorageDirectory() + "/Catatan Harian", fileName);
                FileOutputStream outputStream = null;
                try {
                    file.createNewFile();
                    outputStream = new FileOutputStream(file);
                    outputStream.write(catatanFile.getBytes());
                    outputStream.flush();
                    outputStream.close();
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            }
        }
    }