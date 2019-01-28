package com.anik.workmanagerexample;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    Button btnEnqueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating a data object
        //to pass the data with workRequest
        //we can put as many variables needed
        Data data = new Data.Builder()
                .putString(MyWorker.TASK_DESC, "The task data passed from MainActivity")
                .build();


        //creating constraints
        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true) // you can add as many constraints as you want
                .build();


        final OneTimeWorkRequest workRequest =
                new OneTimeWorkRequest.Builder(MyWorker.class)
                        .setInputData(data)
                      //  .setConstraints(constraints)
                        .build();


//        final PeriodicWorkRequest workRequest
//                = new PeriodicWorkRequest.Builder(MyWorker.class, 10, TimeUnit.SECONDS)
//                .setInputData(data)
//                .build();



//        WorkManager.getInstance().
//                beginWith(workRequest)
//                .enqueue();

        btnEnqueue=findViewById(R.id.buttonEnqueue);

        //This is the subclass of our WorkRequest
       // final OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class).build();

        //A click listener for the button
        //inside the onClick method we will perform the work
        btnEnqueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enqueuing the work request
                WorkManager.getInstance().enqueue(workRequest);
            }
        });

        //Getting the TextView
        final TextView textView = findViewById(R.id.textViewStatus);

        //Listening to the work status
        WorkManager.getInstance().getWorkInfoByIdLiveData(workRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {

                        //receiving back the data
                        if(workInfo != null && workInfo.getState().isFinished()) {
                            textView.append(workInfo.getOutputData().getString(MyWorker.TASK_DESC) + "\n");

                        }

                        //Displaying the status into TextView
                        textView.append(workInfo.getState().name() + "\n");
                    }
                });
    }
}