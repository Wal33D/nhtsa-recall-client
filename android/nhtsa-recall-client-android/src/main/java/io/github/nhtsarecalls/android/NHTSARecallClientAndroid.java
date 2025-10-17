package io.github.nhtsarecalls.android;

import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.nhtsarecalls.NHTSARecallClient;
import io.github.nhtsarecalls.RecallRecord;

/**
 * Android-friendly wrapper that executes recall lookups off the main thread.
 */
public class NHTSARecallClientAndroid {

    public interface RecallCallback {
        void onSuccess(List<RecallRecord> recalls);

        void onError(Throwable throwable);
    }

    private final NHTSARecallClient client;
    private final ExecutorService executor;
    private final Handler mainHandler;

    public NHTSARecallClientAndroid() {
        this(new NHTSARecallClient());
    }

    public NHTSARecallClientAndroid(NHTSARecallClient client) {
        this.client = client;
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void getRecallsByVin(String vin, RecallCallback callback) {
        executor.execute(() -> {
            try {
                List<RecallRecord> recalls = client.getRecallsByVin(vin);
                mainHandler.post(() -> callback.onSuccess(recalls));
            } catch (Exception ex) {
                mainHandler.post(() -> callback.onError(ex));
            }
        });
    }

    public void getRecallsByVehicle(String make, String model, String modelYear, RecallCallback callback) {
        executor.execute(() -> {
            try {
                List<RecallRecord> recalls = client.getRecallsByVehicle(make, model, modelYear);
                mainHandler.post(() -> callback.onSuccess(recalls));
            } catch (Exception ex) {
                mainHandler.post(() -> callback.onError(ex));
            }
        });
    }

    public void getRecallsByCampaign(String campaignNumber, RecallCallback callback) {
        executor.execute(() -> {
            try {
                List<RecallRecord> recalls = client.getRecallsByCampaign(campaignNumber);
                mainHandler.post(() -> callback.onSuccess(recalls));
            } catch (Exception ex) {
                mainHandler.post(() -> callback.onError(ex));
            }
        });
    }

    public void shutdown() {
        executor.shutdownNow();
    }
}
