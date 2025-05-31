package com.example.mobilki8;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadWorker extends Worker {
    public static String IMAGE_URL = "";

    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://random.dog/woof.json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) return Result.failure();
            JSONObject json = new JSONObject(response.body().string());
            IMAGE_URL = json.getString("url");

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("image", IMAGE_URL);
            getApplicationContext().startActivity(intent);

            Log.d("DownloadWorker", "Image URL: " + IMAGE_URL);
            return Result.success();

        } catch (Exception e) {
            Log.e("DownloadWorker", "Error", e);
            return Result.failure();
        }
    }
}
