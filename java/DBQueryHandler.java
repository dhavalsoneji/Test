package com.riontech.firebasequickchat.kit;

import com.riontech.firebasequickchat.ui.adapter.ConversationModel;

/**
 * Created by Riontech-5 on 17/3/17.
 */

public class DBQueryHandler {
    public interface OnQueryHandlerListener<T> {
        void onStart();

        void onComplete(T list);

        void onException(Exception e);
    }

    public interface OnInitializeLibraryListener {
        void loginRequire();

        void homeRequire();

        void onException(Exception e);
    }

    public interface OnLoginListener {
        void onRequest();

        void onResponse();

        void onException(Exception e);
    }

    public interface OnConversationHandlerListener<T> {
        void onStart();

        void onComplete(T list);

        void onException(Exception e);

        void onReceivedMessage(String chatType, String key, ConversationModel conversationModel);
    }
}
