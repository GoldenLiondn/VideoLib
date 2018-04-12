package com.videolib;


import android.content.Context;
import android.widget.MediaController;

/**
 * Created by Maks on 11.04.2018.
 *
 * My realization of MediaController with showing and hiding additional buttons
 */

public class MyController extends MediaController {

    Context context;


    public MyController(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    public void show() {
        super.show();
        PlayerActivity.bSearch.setVisibility(VISIBLE);
        PlayerActivity.bStop.setVisibility(VISIBLE);
        PlayerActivity.bPlayingType.setVisibility(VISIBLE);

    }

    @Override
    public void hide() {
        super.hide();
        PlayerActivity.bSearch.setVisibility(INVISIBLE);
        PlayerActivity.bStop.setVisibility(INVISIBLE);
        PlayerActivity.bPlayingType.setVisibility(INVISIBLE);
    }
}