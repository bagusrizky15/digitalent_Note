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

import java.io.File;
import java.io.FileOutputStream;

public class TambahDataActivity extends AppCompatActivity {

    EditText etNamaFile, etCatatan;
    Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data);

        getSupportActionBar().setTitle("Tambah Catatan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNamaFile = findViewById(R.id.et_nama_file);
        etCatatan = findViewById(R.id.et_catatan);
        btnSimpan = findViewById(R.id.btn_simpan);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNamaFile.getText().toString().length() != 0 && etCatatan.getText().toString().length() != 0) {
                    buatFile();
                } else {
                    Toast.makeText(TambahDataActivity.this, "Nama file atau catatan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
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

    public void buatFile() {
        String namaFile = etNamaFile.getText().toString();
        String catatanFile = etCatatan.getText().toString();
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }
        File path = new File(Environment.getExternalStorageDirectory() + "/Catatan Harian");
        FileOutputStream outputStream = null;
        if (!path.exists())
            path.mkdirs();
        File file = new File(path, namaFile);
        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file, true);
            outputStream.write(catatanFile.getBytes());
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
