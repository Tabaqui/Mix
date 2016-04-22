package ru.motleycrew.presentation.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import ru.motleycrew.model.Event;
import ru.motleycrew.repo.Fetcher;
import ru.motleycrew.repo.FetcherImpl;

/**
 * Created by User on 11.04.2016.
 */
public class MessageFetcher extends IntentService {

    private static final String TAG = "MessageFetcher";

    private static final String EXTRA_MESSAGE_ID = "ru.motleycrew.presentation.service.messageId";

    public static Intent newInstance(Context context, String messageId) {
        Intent  i = new Intent(context, MessageFetcher.class);
        i.putExtra(EXTRA_MESSAGE_ID, messageId);
        return i;
    }

    public MessageFetcher() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String messageId = intent.getStringExtra(EXTRA_MESSAGE_ID);
        Fetcher fetcher = new FetcherImpl(getApplicationContext());
        fetcher.downloadMessage(messageId);
    }


}
