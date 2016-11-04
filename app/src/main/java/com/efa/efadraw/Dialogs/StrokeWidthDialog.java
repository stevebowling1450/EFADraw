package com.efa.efadraw.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.efa.efadraw.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by stevebowling on 11/4/16.
 */

public class StrokeWidthDialog extends Dialog {



    @Bind(R.id.stroke_seekbar)
    SeekBar strokeSeekBar;

    @Bind(R.id.stroke_value)
    TextView strokeValue;

    @Bind(R.id.select_button)
    Button selectButton;


    public int width;

    public StrokeWidthDialog(Context context, int width) {
        super(context);
        this.width= width;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.stroke_width_view);

        ButterKnife.bind(this);

        strokeSeekBar.setMax(99);
        strokeSeekBar.setProgress(width - 1);
        strokeValue.setText(String.valueOf(width));

        SeekBar.OnSeekBarChangeListener colorChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                changeWidth();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                changeWidth();
            }
        };

        strokeSeekBar.setOnSeekBarChangeListener(colorChangeListener);
    }
    private void changeWidth(){
        width= strokeSeekBar.getProgress()+ 1;
        strokeValue.setText(String.valueOf(width));

    }
    @OnClick(R.id.select_button)
    public void selectTapped(){

    }
    }



