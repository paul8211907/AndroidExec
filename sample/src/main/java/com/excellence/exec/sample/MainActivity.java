package com.excellence.exec.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.excellence.exec.CommandTask;
import com.excellence.exec.Commander;
import com.excellence.exec.CommanderOptions;
import com.excellence.exec.IListener;

public class MainActivity extends AppCompatActivity implements IListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button mButton = null;
    private TextView mTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button);
        mTextView = (TextView) findViewById(R.id.text);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Commander.init(new CommanderOptions.Builder().timeOut(10000).build());
                mTextView.setText("");
                /**
                 * 5s超时
                 */
                final CommandTask task = new CommandTask.Builder().command("ls").timeDelay(5000).build();
                task.deploy(MainActivity.this);
                // final CommandTask task = Commander.addTask("ls", MainActivity.this);
                mButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 延时8s取消任务
                         */
                        task.discard();
                    }
                }, 8000);
            }
        });

    }

    @Override
    public void onPre(String command) {
        Log.i(TAG, "onPre: " + command);
        mTextView.append(command + "\n");
    }

    @Override
    public void onProgress(String message) {
        //Log.i(TAG, "onProgress: " + message);
        mTextView.append(message + "\n");
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
        mTextView.setText("Error:" + t.getMessage());
    }

    @Override
    public void onSuccess(String message) {
        Log.i(TAG, "onSuccess: " + message);
        mTextView.append(message + "\n");
    }
}
