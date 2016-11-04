package com.efa.efadraw.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.efa.efadraw.Dialogs.ColorPickerDialog;
import com.efa.efadraw.Dialogs.StrokeWidthDialog;
import com.efa.efadraw.Modles.DrawStartEvent;
import com.efa.efadraw.Modles.ImageSized;
import com.efa.efadraw.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by stevebowling on 11/3/16.
 */

public class DrawView extends LinearLayout {
    private Context context;

    @Bind(R.id.draw_button)
    ImageButton drawButton;

    @Bind(R.id.erase_button)
    ImageButton eraseButton;

    @Bind(R.id.stroke_button)
    ImageButton strokeButton;

    @Bind(R.id.color_button)
    ImageButton colorButton;

    @Bind(R.id.save_button)
    ImageButton saveButton;

    @Bind(R.id.draw_container)
    FrameLayout drawContainer;


    @Bind(R.id.draw_background)
    View drawBackground;

    @Bind(R.id.drawing_canvas)
    DrawingCanvasView drawingCanvas;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        drawButton.setBackgroundColor(context.getResources().getColor(R.color.selectedColor));

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @OnClick(R.id.draw_button)
        public void drawTapped(){
        drawButton.setBackgroundColor(context.getResources().getColor(R.color.selectedColor));
        eraseButton.setBackgroundColor(context.getResources().getColor(R.color.clearColor));
        drawingCanvas.setErasing(false);

    }
    @OnClick(R.id.erase_button)
    public void eraseTapped(){
        drawButton.setBackgroundColor(context.getResources().getColor(R.color.clearColor));
        eraseButton.setBackgroundColor(context.getResources().getColor(R.color.selectedColor));
        drawingCanvas.setErasing(true);

    }

    @OnClick(R.id.stroke_button)
    public void strokeTapped () {

        final StrokeWidthDialog strokeWidthDialog = new StrokeWidthDialog(context, drawingCanvas.width);
        strokeWidthDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                drawingCanvas.strokeWidthChange(strokeWidthDialog.width);
            }
        });
        strokeWidthDialog.show();
    }

    @OnClick(R.id.color_button)
    public void colorTapped(){
        final ColorPickerDialog colorPickerDialog=
                new ColorPickerDialog(context, drawingCanvas.color);
        colorPickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                colorButton.setColorFilter(colorPickerDialog.color);
                drawingCanvas.colorChange(colorPickerDialog.color);
            }
        });
        colorPickerDialog.show();

    }
    @OnClick(R.id.save_button)
    public void saveTapped(){
        drawContainer.setDrawingCacheEnabled(true);
        drawContainer.buildDrawingCache();

        Bitmap bitmap = drawContainer.getDrawingCache();

        String image = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                bitmap,
                UUID.randomUUID().toString()+ ".png","new Image");

        if (image != null){
            Toast.makeText(context, "Drawing Saved", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Could not save Image", Toast.LENGTH_SHORT).show();
        }

        drawContainer.destroyDrawingCache();


    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void imageReady(ImageSized event){
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(drawingCanvas.scaledImage);
        drawContainer.addView(imageView);
        drawingCanvas.bringToFront();
    }

@Subscribe(threadMode = ThreadMode.MAIN)
public void drawStart (DrawStartEvent event) {
    int imageIndex = -1;
    for (int i = 0; i < drawContainer.getChildCount(); i++) {
        View child = drawContainer.getChildAt(i);

        if (child instanceof ImageView) {
            imageIndex = i;
        } else if (child instanceof DrawingCanvasView) {
            ((DrawingCanvasView)child).resetCanvas();
        }
    }

    if (imageIndex > -1) {
        drawContainer.removeView(drawContainer.getChildAt(imageIndex));
    }
}

}
