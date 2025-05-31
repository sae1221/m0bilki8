package com.example.mobilki8;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.*;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private final OkHttpClient client = new OkHttpClient();
    private final Executor backgroundExecutor = Runnable::run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnSeq = findViewById(R.id.btnSequential);
        Button btnPar = findViewById(R.id.btnParallel);
        Button btnDownload = findViewById(R.id.btnDownload);
        imageView = findViewById(R.id.imageView);

        String imageUrl = getIntent().getStringExtra("image");
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).into(imageView);
        }

        btnSeq.setOnClickListener(v -> runSequentialTasks());
        btnPar.setOnClickListener(v -> runParallelTasks());
        btnDownload.setOnClickListener(v -> downloadImage());
    }

    private void runSequentialTasks() {
        WorkManager wm = WorkManager.getInstance(this);
        OneTimeWorkRequest task1 = new OneTimeWorkRequest.Builder(SimpleWorker.class)
                .setInputData(new Data.Builder().putString("name", "Task 1").build())
                .build();
        OneTimeWorkRequest task2 = new OneTimeWorkRequest.Builder(SimpleWorker.class)
                .setInputData(new Data.Builder().putString("name", "Task 2").build())
                .build();
        OneTimeWorkRequest task3 = new OneTimeWorkRequest.Builder(SimpleWorker.class)
                .setInputData(new Data.Builder().putString("name", "Task 3").build())
                .build();

        wm.beginWith(task1).then(task2).then(task3).enqueue();
    }

    private void runParallelTasks() {
        WorkManager wm = WorkManager.getInstance(this);
        OneTimeWorkRequest taskA = new OneTimeWorkRequest.Builder(SimpleWorker.class)
                .setInputData(new Data.Builder().putString("name", "Parallel A").build())
                .build();
        OneTimeWorkRequest taskB = new OneTimeWorkRequest.Builder(SimpleWorker.class)
                .setInputData(new Data.Builder().putString("name", "Parallel B").build())
                .build();

        wm.enqueue(Arrays.asList(taskA, taskB));
    }

    private void downloadImage() {
        WorkManager.getInstance(this).enqueue(
                new OneTimeWorkRequest.Builder(DownloadWorker.class).build()
        );
    }
}
