package com.riontech.firebasequickchat.kit;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.riontech.firebasequickchat.model.UserTable;
import com.riontech.firebasequickchat.model.UserTableDao;
import com.riontech.firebasequickchat.sync.SyncContacts;
import com.riontech.firebasequickchat.utils.ConnectivityUtils;
import com.riontech.firebasequickchat.utils.FireBaseReference;
import com.riontech.firebasequickchat.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Riontech-5 on 11/3/17.
 */

public class FQCContacts extends FQCBase {
    private Context mContext;
    private Query mQuery;
    private List<UserTable> mList;
    private org.greenrobot.greendao.query.Query<UserTable> matesQuery;
    private UserTableDao matesDao;

    public FQCContacts(Context context) {
        super();
        mQuery = FireBaseReference.getUserTableReference().orderByChild(FireBaseReference.NAME);
        mList = new ArrayList<>();
        mContext = context;
        matesDao = getDaoSession().getUserTableDao();
        matesQuery = matesDao.queryBuilder().orderAsc(UserTableDao.Properties.Name).build();
    }

    public void fetchContacts(final DBQueryHandler.OnQueryHandlerListener listener) {
        if (matesQuery.list().size() == 0) {
            if (ConnectivityUtils.checkInternetConnection(mContext)) {
                try {
                    listener.onStart();
                    mList.clear();
                    mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot userSnapshot :
                                    dataSnapshot.getChildren()) {
                                UserTable userTable = userSnapshot.getValue(UserTable.class);
                                if (userTable != null) {
                                    if (!userTable.getUId().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        userTable.setKey(dataSnapshot.getKey());
                                        mList.add(userTable);

                                        UserTable table = new UserTable();
                                        table.toUsertable(userTable);
                                        matesDao.insert(table);
                                    }
                                }
                            }
                            if (StringUtils.isValidList(mList)) {
                                listener.onComplete(mList);
                            } else {
                                listener.onComplete(null);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listener.onException(databaseError.toException());
                        }
                    });
                } catch (Exception e) {
                    listener.onException(e);
                }
            } else {
                listener.onComplete(null);
            }
        } else {
            try {
                listener.onStart();
                mList.clear();
                for (UserTable mates :
                        matesQuery.list()) {
                    mList.add(mates);
                }
                listener.onComplete(mList);
                if (ConnectivityUtils.checkInternetConnection(mContext)) {
                    SyncContacts.syncContacts();
                }
            } catch (Exception e) {
                listener.onException(e);
            }
        }
    }
}
