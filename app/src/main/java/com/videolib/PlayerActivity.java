package com.videolib;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

/**
 * Created by Maks on 12.04.2018.
 */

public class PlayerActivity extends AppCompatActivity  {

    private boolean loopVideo = true;
    VideoView videoView;
    MyController controller;
    int indexOfVideo;
    ArrayList<String> videos;
    public static ImageButton bSearch, bPlayingType, bStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initComponents();

        videoView.setVideoPath(videos.get(indexOfVideo));
        controller.setAnchorView(videoView);
        videoView.requestFocus(0);

        videoView.start();

    }

    /**
     * Initialization of components
     */
    private void initComponents(){

        videoView = (VideoView)findViewById(R.id.videoView);
        bSearch = (ImageButton) findViewById(R.id.search);
        bStop = (ImageButton) findViewById(R.id.stop);
        bPlayingType = (ImageButton) findViewById(R.id.loopOrNext);
        controller = new MyController(this);
        Intent intent = getIntent();
        videos =  intent.getStringArrayListExtra("list");
        videoView.setMediaController(controller);
        indexOfVideo = intent.getIntExtra("index", 0);


        addListeners();
    }

    /**
     * If user entered something in search field and pressed OK, searchResult activity opens.
     * Else getting message with No results
     * @param searchQuery
     */
    public void openSearchAct(String searchQuery){
        for(String s:MainActivity.namesList){
            if(s.contains(searchQuery)){
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra("query", searchQuery);
                startActivity(intent);
                finish();
                return;
            }
        }

        Toast toast = Toast.makeText(getApplicationContext(),
                "No Results", Toast.LENGTH_SHORT);
        toast.show();

    }

    /**
     * adding listeners
     */
    private void addListeners(){


        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new DialogScreen().getDialog(PlayerActivity.this);
                dialog.show();
            }
        });


        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bPlayingType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loopVideo==false) {
                    loopVideo = true;
                    bPlayingType.setImageResource(R.drawable.loop);
                }
                else {
                    loopVideo = false;
                    bPlayingType.setImageResource(R.drawable.list);
                }
            }
        });

        controller.setPrevNextListeners(new View.OnClickListener() {
            public void onClick(View v) {
                if(indexOfVideo==videos.size()-1){
                    indexOfVideo=0;
                } else indexOfVideo++;
                videoView.setVideoPath(videos.get(indexOfVideo));
                videoView.start();
            }
        }, new View.OnClickListener() {
            public void onClick(View v) {
                if (indexOfVideo == 0) {
                    indexOfVideo = videos.size() - 1;
                } else indexOfVideo--;
                videoView.setVideoPath(videos.get(indexOfVideo));
                videoView.start();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(!loopVideo) {
                    if (indexOfVideo == videos.size() - 1) {
                        indexOfVideo = 0;
                    } else indexOfVideo++;
                }
                videoView.setVideoPath(videos.get(indexOfVideo));
                videoView.start();
            }
        });

    }

    /**
     * Class that describing dialog window
     */
    public class DialogScreen {
        EditText tf1;
        View view;

        public  AlertDialog getDialog(Activity activity) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);


            view = activity.getLayoutInflater().inflate(R.layout.search_dialog, null); // Получаем layout по его ID
            builder.setView(view);
            builder.setTitle("Search");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                public void onClick(DialogInterface dialog, int whichButton) {
                    tf1 = (EditText) view.findViewById(R.id.textField);
                    openSearchAct(tf1.getText().toString());
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // Кнопка Отмена
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(true);
            return builder.create();

        }

    }
}
