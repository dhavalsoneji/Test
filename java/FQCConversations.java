package com.riontech.firebasequickchat.kit;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.riontech.firebasequickchat.listener.FQCFetchContactListener;
import com.riontech.firebasequickchat.utils.FireBaseReference;

/**
 * Created by Riontech-5 on 15/3/17.
 */

public class FQCConversations extends FQCBase {
    private Context mContext;
    private DatabaseReference mPrivateDatabaseReference;
    private DatabaseReference mGroupDatabaseReference;
    private String mLoginUid;

    public FQCConversations(Context context) {
        super();
        mContext = context;
        mPrivateDatabaseReference = FireBaseReference.getOneToOneMessageRef().getRef();
        mGroupDatabaseReference = FireBaseReference.getGroupTableReference().getRef();
        mLoginUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void fetch(final FQCFetchContactListener listener) {

    }
}
