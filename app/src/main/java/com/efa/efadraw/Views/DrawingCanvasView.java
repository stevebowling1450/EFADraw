package com.efa.efadraw.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.efa.efadraw.Modles.ImageLoadedEvent;
import com.efa.efadraw.Modles.ImageSized;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by stevebowling on 11/3/16.
 */

public class DrawingCanvasView extends View {

    public int color;
    public int width;

    private Path drawPath;
    private Paint drawPaint;
    private Paint canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;

    private boolean isErasing = false;

    private String selectedImage;
    public Bitmap scaledImage;



    public DrawingCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        color = Color.BLACK;
        width = 10;
        setupDrawing();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    public void setupDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor(color);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(width);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }


    public void colorChange(int color){
        this.color = color;

        drawPaint.setColor(color);
        invalidate();
    }
    public void  strokeWidthChange (int width){
        this.width = width;
        drawPaint.setStrokeWidth(width);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap= Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap,0,0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY= event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX,touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }
    public void setErasing(boolean erasing){
        this.isErasing = erasing;
        if (isErasing){
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }else {
            drawPaint.setXfermode(null);
        }

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void imageLoading(ImageLoadedEvent event){
        selectedImage = event.selectedImage;
        int width = drawCanvas.getWidth();
        int height = drawCanvas.getHeight();

        Bitmap image = BitmapFactory.decodeFile(selectedImage);

        float ratio = (float)image.getWidth() / (float)image.getHeight();
        if (ratio >1){
            height = (int)(width/ratio);
        }else {
            width =(int) (height * ratio);
        }

        scaledImage = Bitmap.createScaledBitmap(image, width, height, true);
        EventBus.getDefault().post(new ImageSized());

    }
    public void resetCanvas(){
        drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }
}
