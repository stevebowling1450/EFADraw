package com.efa.efadraw.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

import com.efa.efadraw.MainActivity;
import com.efa.efadraw.Modles.DrawStartEvent;
import com.efa.efadraw.Modles.ImageSized;
import com.efa.efadraw.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by stevebowling on 11/3/16.
 */

public class MainView extends LinearLayout {

    private Context context;

    @Bind(R.id.new_button)
    Button newButton;


    @Bind(R.id.picture_button)
    Button pictureButton;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @OnClick(R.id.new_button)
    public void newTapped(){
        ((MainActivity)context).flipView();

        EventBus.getDefault().post(new DrawStartEvent());

    }
    @OnClick(R.id.picture_button)
    public void pictureTapped(){
        ((MainActivity)context).getImage();

        EventBus.getDefault().post(new DrawStartEvent());

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void imageReady(ImageSized event){
        ((MainActivity)context).flipView();
    }
}
