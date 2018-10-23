package com.hencoder.plus;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.hencoder.plus.view.Dashboard;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Dashboard dashboard;
    Button button;

    Timer timer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dashboard = findViewById(R.id.db);
        button = findViewById(R.id.button);

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        actionDown();
                        break;
                    case MotionEvent.ACTION_UP:
                        actionUp();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void actionUp() {
        timer.cancel();
        timer.purge();
        timer = null;
        timer = new Timer();
        doPoint(false);
    }

    private void actionDown() {
        try {
            timer.cancel();
            timer.purge();
            timer = null;
        } catch (Exception e) {
        }
        timer = new Timer();
        doPoint(true);
    }

    void doPoint(final boolean eventDown) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dashboard.incrementSpeedBy(eventDown ? 1 : -1);
            }
        }, 0, 10);
    }
}
