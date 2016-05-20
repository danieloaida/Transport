package ro.ratt.transport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

public class BeginActivity extends AppCompatActivity {
    DBHandler dbHandler;
    InitDB initDB;
    Thread initialisation;
    Runnable process;

    private int progress = 0;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(200);

        dbHandler = new DBHandler(this, null, null, 1);
        initDB = new InitDB(this, dbHandler);

        process = new Runnable() {
            public void run() {
                initDB.StartInit();
            }
        };

        initialisation =  new Thread(process);
        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("message");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();


        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 200) {
                    progressStatus = doSomeWork();
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                }
                handler.post(new Runnable() {
                    public void run() {
                        // ---0 - VISIBLE; 4 - INVISIBLE; 8 - GONE---
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(BeginActivity.this, MapsActivity.class);
                    //    intent.putExtra("dbHandler", dbHandler.getWritableDatabase());
                        startActivity(intent);
                    }
                });
            }

            private int doSomeWork() {
                try {
                    initDB.StartInit();
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return ++progress;
            }
        }).start();
    }
}
