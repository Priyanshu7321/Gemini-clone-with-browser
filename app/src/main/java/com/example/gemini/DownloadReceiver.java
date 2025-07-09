package com.example.gemini;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.DownloadManager;
import android.database.Cursor;
import android.os.Handler;

public class DownloadReceiver extends BroadcastReceiver {

    private static final int POLLING_INTERVAL = 1000; // 1 second

    @Override
    public void onReceive(Context context, Intent intent) {
        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

        if (downloadId != -1) {
            checkDownloadProgress(context, downloadId);
        }
    }

    private void checkDownloadProgress(Context context, final long downloadId) {
        final DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final Handler handler = new Handler();
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                Cursor cursor = dm.query(new DownloadManager.Query().setFilterById(downloadId));
                if (cursor.moveToFirst()) {
                    @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    @SuppressLint("Range") long totalBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    @SuppressLint("Range") long downloadedBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));

                    int progress = calculateProgress(totalBytes, downloadedBytes);

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        Intent progressIntent = new Intent(context, DownloadProgressActivity.class);
                        progressIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        progressIntent.putExtra("status", "completed");
                        progressIntent.putExtra("progress", 100); // 100% when completed
                        context.startActivity(progressIntent);
                        handler.removeCallbacks(this);
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        Intent progressIntent = new Intent(context, DownloadProgressActivity.class);
                        progressIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        progressIntent.putExtra("status", "failed");
                        progressIntent.putExtra("progress", progress);
                        context.startActivity(progressIntent);
                        handler.removeCallbacks(this);
                    } else {
                        Intent progressIntent = new Intent(context, DownloadProgressActivity.class);
                        progressIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        progressIntent.putExtra("status", "downloading");
                        progressIntent.putExtra("progress", progress);
                        context.startActivity(progressIntent);
                        handler.postDelayed(this, POLLING_INTERVAL);
                    }
                }
                cursor.close();
            }
        };
        handler.post(progressRunnable);
    }

    private int calculateProgress(long totalBytes, long downloadedBytes) {
        if (totalBytes <= 0) return 0;
        return (int) ((downloadedBytes * 100) / totalBytes);
    }
}
