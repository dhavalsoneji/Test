package com.riontech.firebasequickchat.kit;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.riontech.firebasequickchat.model.Configs;
import com.riontech.firebasequickchat.utils.AppLog;
import com.riontech.firebasequickchat.utils.ConnectivityUtils;
import com.riontech.firebasequickchat.utils.PreferanceUtils;
import com.riontech.firebasequickchat.utils.StringUtils;

/**
 * Created by Riontech-5 on 6/3/17.
 */

public class FQCConfig {
    private static final String TAG = FQCConfig.class.getSimpleName();
    private static FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListener;

    /**
     * @param activity - for use to fetch applicationInfo & used as Context
     * @param listener - this listener used to indicate login result in either
     *                 a) loginRequire() - indicate user is not logged In, developer can redirect to login screen
     *                 b) homeRequire() - indicate user is logged In, developer can redirect to home screen
     *                 c) onException() - handle exception
     */
    public static void startChat(final Activity activity, final FQCStartChatListener listener) {
        try {

            ApplicationInfo applicationInfo = activity.getPackageManager().getApplicationInfo
                    (activity.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            String storageUrl = bundle.getString("storage_url");
            String serverKey = bundle.getString("server_key");
            String requestIdToken = bundle.getString("request_id_token");
            configureChat(activity, storageUrl, serverKey, requestIdToken);

        } catch (PackageManager.NameNotFoundException e) {

            AppLog.e(TAG, "Failed to load meta-data, NameNotFound");
            listener.onException(e);
            return;
        } catch (NullPointerException e) {

            AppLog.e(TAG, "Failed to load meta-data, NullPointer");
            listener.onException(e);
            return;
        } catch (Exception e) {

            listener.onException(e);
            return;
        }

        if (PreferanceUtils.getIsConfig(activity)) {
            if (ConnectivityUtils.checkInternetConnection(activity)) {
                try {
                    mAuth = FirebaseAuth.getInstance();
                    mAuthListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            try {

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {

                                    mAuth.removeAuthStateListener(mAuthListener);
                                    listener.homeRequire();
                                } else {

                                    mAuth.removeAuthStateListener(mAuthListener);
                                    listener.loginRequire();
                                }
                            } catch (Exception e) {

                                mAuth.removeAuthStateListener(mAuthListener);
                                listener.onException(e);
                            }
                        }
                    };
                    mAuth.addAuthStateListener(mAuthListener);
                } catch (Exception e) {
                    listener.onException(e);
                }
            }
        }
    }

    /**
     * @param storageUrl     - you can find storage url from "firebase console --> storage"
     * @param serverKey      - you can find auth key from "firebase console --> project settings --> cloud messaging --> server key"
     * @param requestIdToken - you can find value from google-services.json file
     *                       1) Open google-services.json file -> client -> oauth_client -> client_id
     *                       2) Copy this client ID and hardcode this to below variable
     */
    public static void configureChat(Context context, String storageUrl, String serverKey, String requestIdToken) {
        if (!StringUtils.isValidString(storageUrl.trim())) {
            Toast.makeText(context, "storageUrl can not be empty, please add into AndroidManifest.xml",
                    Toast.LENGTH_LONG).show();
        } else if (!StringUtils.isValidString(serverKey.trim())) {
            Toast.makeText(context, "serverKey can not be empty, please add into AndroidManifest.xml",
                    Toast.LENGTH_LONG).show();
        } else if (!StringUtils.isValidString(requestIdToken.trim())) {
            Toast.makeText(context, "requestIdToken can not be empty, please add into AndroidManifest.xml",
                    Toast.LENGTH_LONG).show();
        } else {
            serverKey = "key=" + serverKey;
            PreferanceUtils.setConfiguration(context, new Configs(storageUrl.trim(), serverKey.trim(),
                    requestIdToken.trim()));
        }
    }
}
