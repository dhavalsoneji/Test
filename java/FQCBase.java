package com.riontech.firebasequickchat.kit;

import com.riontech.firebasequickchat.AppApplication;
import com.riontech.firebasequickchat.model.DaoSession;

/**
 * Created by Riontech-5 on 15/3/17.
 */

class FQCBase {
    private DaoSession daoSession;

    FQCBase() {
        daoSession = AppApplication.getInstance().getDaoSession();
    }

    DaoSession getDaoSession() {
        return daoSession;
    }
}
