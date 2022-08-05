package com.rivvana.digitalent_note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView lvData;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isStoragePermissionGranted();

        lvData = findViewById(R.id.lv_data);

    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted / Disetujui");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked / Di tolak");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted sdk kurang 23 langsung di setujuii");
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            startActivity(new Intent(this, TambahDataActivity.class));
        }
        return true;
    }

    private void readFile() {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }

//        File file = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Catatan Harian"));
        File dir = new File(Environment.getExternalStorageDirectory() + "/Catatan Harian");

        if (dir.exists()) {
            List<Map<String, String>> data = new ArrayList<>();
            for (File item : dir.listFiles()) {
                Map<String, String> datum = new HashMap<>(2);

                String strDateFormat = "dd MMM yyyy HH:mm:ss";
                DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                String date = dateFormat.format(item.lastModified());

                datum.put("title", item.getName());
                datum.put("date", date);
                data.add(datum);
            }

            SimpleAdapter adapter = new SimpleAdapter(this, data,
                    android.R.layout.simple_list_item_2,
                    new String[]{"title", "date"},
                    new int[]{android.R.id.text1,
                            android.R.id.text2});

            lvData.setAdapter(adapter);
            lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, DetailCatatanActivity.class);
                    intent.putExtra("FILENAME", data.get(position).get("title"));
                    startActivity(intent);
//                Toast.makeText(MainActivity.this, data.get(position).get("title"), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        readFile();
    }
}