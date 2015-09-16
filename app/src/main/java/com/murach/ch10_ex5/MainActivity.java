package com.murach.ch10_ex5;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView messageTextView;
    public Button startTimerButton;
    public Button stopTimerButton;
    public Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // references
        messageTextView = (TextView) findViewById(R.id.messageTextView);

        startTimerButton = (Button) findViewById(R.id.startTimerButton);
        stopTimerButton = (Button) findViewById(R.id.stopTimerButton);

        // listeners
        startTimerButton.setOnClickListener(this);
        stopTimerButton.setOnClickListener(this);
        startTimer();
    }
    
    public void startTimer() {
        final long startMillis = System.currentTimeMillis();
        timer = new Timer(true);

        TimerTask task = new TimerTask() {

            
            @Override
            public void run() {
                long elapsedMillis = System.currentTimeMillis() - startMillis;

                final String FILENAME = "news_feed.xml";{
                    try{
                        // get the input stream
                        URL url = new URL("http://rss.cnn.com/rss/cnn_tech.rss");
                        InputStream in = url.openStream();

                        // get the output stream
                        FileOutputStream out = openFileOutput(FILENAME, Context.MODE_PRIVATE);

                        // read input and write output
                        byte[] buffer = new byte[1024];
                        int bytesRead = in.read(buffer);
                        while (bytesRead != -1)
                        {
                            out.write(buffer, 0, bytesRead);
                            bytesRead = in.read(buffer);
                        }
                        out.close();
                        in.close();
                    }
                    catch (IOException e) {
                        Log.e("News reader", e.toString());
                    }

                    catch (NullPointerException n) {
                        Log.e("News Reader", n.toString());
                    }
                }

                updateView(elapsedMillis);

            }
        };
        timer.schedule(task, 0, 10000);
        
        
    }

    




    private void updateView(final long elapsedMillis) {
        // UI changes need to be run on the UI thread
        messageTextView.post(new Runnable() {

            int elapsedSeconds = (int) elapsedMillis / 1000;

            @Override
            public void run() {
                messageTextView.setText("File downloaded " + elapsedSeconds / 10 + " time(s).");
            }
        });
    }

    @Override
    public void onPause() {
        // call the cancel method to stop the timer when phone is flipped


        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startTimerButton:
                startTimer();
                break;
            case R.id.stopTimerButton:
                timer.cancel();
                break;
        }
    }
}