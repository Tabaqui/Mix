package ru.motleycrew.presentation.service;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import ru.motleycrew.repo.Fetcher;

/**
 * Created by User on 11.04.2016.
 */
public class MessageFetcher extends AbstractService {

    private static final String TAG = "MessageFetcher";
    private static final String EXTRA_MESSAGE_ID = "ru.motleycrew.presentation.service.messageId";

    @Inject
    Fetcher fetcher;

    public static Intent newInstance(Context context, String messageId) {
        Intent i = new Intent(context, MessageFetcher.class);
        i.putExtra(EXTRA_MESSAGE_ID, messageId);
        return i;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initDI();
    }

    public MessageFetcher() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String messageId = intent.getStringExtra(EXTRA_MESSAGE_ID);
        fetcher.downloadMessage(messageId);
    }


}
