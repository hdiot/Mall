package com.mebee.mall.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mebee.mall.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(this).inflate(R.layout.activity_splash,null);
        setContentView(mView);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> mView.animate()
                        .scaleXBy(0.1f)
                        .scaleYBy(0.1f)
                        .alphaBy(0.1f)
                        .setDuration(2000)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                                overridePendingTransition(0,0);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).start());
            }
        },0);
    }
}
