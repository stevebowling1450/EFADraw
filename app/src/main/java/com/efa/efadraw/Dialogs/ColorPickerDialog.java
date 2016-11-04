package com.efa.efadraw.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.efa.efadraw.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by stevebowling on 11/3/16.
 */

public class ColorPickerDialog extends Dialog {

    @Bind(R.id.color_view)
    View colorView;

    @Bind(R.id.red_seekbar)
    SeekBar redSeekBar;

    @Bind(R.id.red_value)
    TextView redValue;

    @Bind(R.id.green_seekbar)
    SeekBar greenSeekBar;

    @Bind(R.id.green_value)
    TextView greenValue;

    @Bind(R.id.blue_seekbar)
    SeekBar blueSeekBar;

    @Bind(R.id.blue_value)
    TextView blueValue;

    @Bind(R.id.select_button)
    Button selectButton;

    public int color;

    public ColorPickerDialog(Context context, int color) {
        super(context);
        this.color = color;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.color_picker_view);

        ButterKnife.bind(this);
        colorView.setBackgroundColor(color);
        redSeekBar.setMax(255);
        greenSeekBar.setMax(255);
        blueSeekBar.setMax(255);
        redSeekBar.setProgress(Color.red(color));
        greenSeekBar.setProgress(Color.green(color));
        blueSeekBar.setProgress(Color.blue(color));
        redValue.setText(String.valueOf(Color.red(color)));
        greenValue.setText(String.valueOf(Color.green(color)));
        blueValue.setText(String.valueOf(Color.blue(color)));

        SeekBar.OnSeekBarChangeListener colorChangeListener=
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        changeColor();

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        changeColor();

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        changeColor();

                    }
                };
        redSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        greenSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        blueSeekBar.setOnSeekBarChangeListener(colorChangeListener);
    }

    private void changeColor(){
        color = Color.rgb(redSeekBar.getProgress(),
                greenSeekBar.getProgress(),
                blueSeekBar.getProgress());
        colorView.setBackgroundColor(color);
        redValue.setText(String.valueOf(Color.red(color)));
        greenValue.setText(String.valueOf(Color.green(color)));
        blueValue.setText(String.valueOf(Color.blue(color)));
    }
    @OnClick(R.id.select_button)
    public void selectTapped(){
        dismiss();
    }
}
