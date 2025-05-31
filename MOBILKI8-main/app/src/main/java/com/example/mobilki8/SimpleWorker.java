package com.example.mobilki8;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SimpleWorker extends Worker {
    public SimpleWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        String name = getInputData().getString("name");
        Log.d("SimpleWorker", "Executing: " + name);
        try {
            Thread.sleep(2000); // Simulate delay
        } catch (InterruptedException e) {
            return Result.failure();
        }
        return Result.success();
    }
}
