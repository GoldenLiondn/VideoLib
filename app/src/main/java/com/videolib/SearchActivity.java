package com.videolib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Maks on 12.04.2018.
 */

public class SearchActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> namesList;
    ArrayList<String> pathsList;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        namesList = new ArrayList<>();
        pathsList = new ArrayList<>();
        Intent intent = getIntent();
        query =  intent.getStringExtra("query");
        listView = (ListView) findViewById(R.id.listViewSearch);
        getVideos();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, namesList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, PlayerActivity.class);
                intent.putExtra("index", position);
                intent.putExtra("list", pathsList);
                startActivity(intent);
            }
        });

    }

    /**
     * getting lists with paths and filenames of videos that had found
     */
    private void getVideos() {
        for(String s: MainActivity.namesList){
            if(s.contains(query)){
                namesList.add(s);
                pathsList.add(MainActivity.pathsList.get(MainActivity.namesList.indexOf(s)));
            }
        }

    }
}
