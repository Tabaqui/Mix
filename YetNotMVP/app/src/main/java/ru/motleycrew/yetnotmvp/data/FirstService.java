package ru.motleycrew.yetnotmvp.data;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import ru.motleycrew.yetnotmvp.Util;

/**
 * Created by vas on 19.03.16.
 */
public class FirstService extends Service {

    private static final String LOG = "FirstService";
    private int mVariable;
    private Handler mHandler;
    private Thread mThread;
    private FutureTask<Void> mTask;
    private Executor mExecutor;

    public static Intent getIntent(Context context) {
        return new Intent(context, FirstService.class);
    }

    private static class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(LOG, "Msg handled " + msg.what);
            Log.d(LOG, "Handle in thread " + Thread.currentThread().getName());

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mVariable = 1;
        if (mHandler == null) {
            mHandler = new MessageHandler();
        }
        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(LOG, "Real Thread pain " + Thread.currentThread().getName());
                    mHandler.sendEmptyMessage(5);
                }
            });
        }
        if (mTask == null) {
            mTask = new FutureTask<>(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    Log.d(LOG, "Future Task pain " + Thread.currentThread().getName());
                    mHandler.sendEmptyMessage(3);
                    return null;
                }
            });
        }
        if (mExecutor == null) {
            mExecutor = Executors.newFixedThreadPool(2);
        }
        Log.d(LOG, "Service created");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG, "Service start");
        Log.d(LOG, "Thread " + Thread.currentThread().getName());
        mVariable++;
        Util.showText(this, "Service start" + mVariable);
        mTask = new FutureTask<>(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Log.d(LOG, "Future Task pain " + Thread.currentThread().getName());
                mHandler.sendEmptyMessage(3);
                return null;
            }
        });
//        mExecutor.execute(mTask);
        mExecutor.execute(mThread);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(LOG, "Service done");
        Util.showText(this, "Service done");
    }
}
