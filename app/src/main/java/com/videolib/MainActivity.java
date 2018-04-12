package com.videolib;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;
    public static ArrayList<String> namesList;
    public static ArrayList<String> pathsList;
    public static int firstPlayingVideoIndex;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checking permissions
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);

            }
        } else {
            doMain();
        }

    }

    /**
     * Something like main method of activity.
     * Initializes fields, gets all videos and opens player.
     */
    public void doMain(){
        listView = (ListView) findViewById(R.id.listView);
        namesList = new ArrayList<>();
        pathsList = new ArrayList<>();
        getVideos();

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, namesList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                firstPlayingVideoIndex = position;
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                intent.putExtra("index", firstPlayingVideoIndex);
                intent.putExtra("list", pathsList);
                startActivity(intent);
            }
        });
    }

    /**
     * Getting URI and names of all videos to lists of names and paths
     */
    public void getVideos(){
        ContentResolver contentResolver = getContentResolver();
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor videoCursor = contentResolver.query(videoUri, null,null,null, null);

        if(videoCursor != null && videoCursor.moveToFirst()){
            int videoTitle = videoCursor.getColumnIndex(MediaStore.Video.Media.TITLE);
            int videoPath = videoCursor.getColumnIndex(MediaStore.Video.Media.DATA);

            do{
                String currentTitle = videoCursor.getString(videoTitle);
                String currentPath = videoCursor.getString(videoPath);
                namesList.add(currentTitle);
                pathsList.add(currentPath);
            } while (videoCursor.moveToNext());

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();

                        doMain();
                    }
                } else {
                    Toast.makeText(this, "No Permission granted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }
}
