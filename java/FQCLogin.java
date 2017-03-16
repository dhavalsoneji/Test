package com.riontech.firebasequickchat.kit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.riontech.firebasequickchat.R;
import com.riontech.firebasequickchat.custom.GoogleManager;
import com.riontech.firebasequickchat.listener.FQCLoginListener;
import com.riontech.firebasequickchat.listener.OnUserInsertListener;
import com.riontech.firebasequickchat.listener.SocialConnectionListener;
import com.riontech.firebasequickchat.model.UserTable;
import com.riontech.firebasequickchat.utils.AlertDialogUtils;
import com.riontech.firebasequickchat.utils.AppConstants;
import com.riontech.firebasequickchat.utils.FireBaseReference;
import com.riontech.firebasequickchat.utils.FireBaseUtils;
import com.riontech.firebasequickchat.utils.PreferanceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Riontech-5 on 15/3/17.
 */

public class FQCLogin {
    private static final String TAG = FQCLogin.class.getSimpleName();
    private FragmentActivity mActivity;
    private GoogleManager mGoogleManager;
    private FQCLoginListener mListener;
    private GoogleSignInAccount mGoogleSignInAccount;

    public FQCLogin(FragmentActivity activity) {
        mActivity = activity;
        mGoogleManager = new GoogleManager(mActivity, mSocialConnectionListener);
    }

    public void loginWithGoogle(FQCLoginListener listener) {
        mListener = listener;
        mGoogleManager.initGoogle();
    }

    private SocialConnectionListener mSocialConnectionListener = new SocialConnectionListener() {
        @Override
        public void onSuccessGoogle(GoogleSignInAccount loginResult) {
            mListener.onStart();
            authWithGoogle(loginResult);
        }

        @Override
        public void onCancel() {
            mListener.onException(null);
            AlertDialogUtils.showToast(mActivity, mActivity.getResources()
                    .getString(R.string.error_sign_in_with_google));
        }

        @Override
        public void onFailure(Exception e) {
            mListener.onException(e);
            AlertDialogUtils.showToast(mActivity, mActivity.getResources()
                    .getString(R.string.error_sign_in_with_google));
        }
    };

    private void authWithGoogle(GoogleSignInAccount acct) {
        mGoogleSignInAccount = acct;
        if (mGoogleSignInAccount != null) {

            try {
                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                try {
                                    if (!task.isSuccessful()) {
                                        mListener.onException(null);
                                        handleException(task);
                                    } else {
                                        UserTable userTable = new UserTable();
                                        userTable.setEmail(mGoogleSignInAccount.getEmail());
                                        userTable.setLoginProvider(AppConstants.PROVIDER_GMAIL);
                                        userTable.setName(mGoogleSignInAccount.getDisplayName().toLowerCase());
                                        userTable.setUId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        userTable.setPhotoUrl(FirebaseAuth.getInstance().getCurrentUser()
                                                .getPhotoUrl().toString());
                                        userTable.setStatus(AppConstants.STATUS_ONLINE);
                                        userTable.setIsAdmin(AppConstants.VAL_FALSE);
                                        userTable.setDeviceToken(FirebaseInstanceId.getInstance().getToken());
                                        Map<String, String> deviceTokens = new HashMap<>();
                                        deviceTokens.put("token1", FirebaseInstanceId.getInstance().getToken());
                                        userTable.setDeviceTokens(deviceTokens);

                                        FireBaseUtils.insertUserIntoDatabase(userTable,
                                                FireBaseReference.getUserTableReference(), new OnUserInsertListener() {

                                                    @Override
                                                    public void onComplete(UserTable userTable, boolean isNew) {
                                                        if (isNew) {

                                                        }
                                                        FireBaseUtils.subscribeToApp();
                                                        PreferanceUtils.setCurrentUser(mActivity, userTable);
                                                        PreferanceUtils.setIsSign(mActivity, true);
                                                        PreferanceUtils.setSignInMethod(mActivity,
                                                                AppConstants.SIGNIN_TYPE_GOOGLE);
                                                        mListener.onComplete();
                                                    }

                                                    @Override
                                                    public void onInComplete(Exception e) {
                                                        mListener.onException(e);
                                                        AlertDialogUtils.showToast(mActivity, mActivity.getResources()
                                                                .getString(R.string.error_sign_in_with_google));
                                                    }
                                                });
                                    }
                                } catch (Exception e) {
                                    mListener.onException(e);
                                }
                            }
                        });
            } catch (Exception e) {
                mListener.onException(e);
            }
        } else {
            mListener.onException(null);
            AlertDialogUtils.showToast(mActivity,
                    mActivity.getResources().getString(R.string.error_gmail_retrive));
        }
    }

    private void handleException(Task<AuthResult> task) {
        try {
            throw task.getException();
        } catch (FirebaseAuthInvalidCredentialsException e) {

            if (mActivity != null) {
                AlertDialogUtils.showToast(mActivity, mActivity.getResources()
                        .getString(R.string.invalid_pass_username));
            }
        } catch (FirebaseAuthInvalidUserException e) {
            if (mActivity != null) {
                AlertDialogUtils.showToast(mActivity, mActivity.getResources()
                        .getString(R.string.no_credentials));
            }
        } catch (FirebaseAuthUserCollisionException e) {
            if (mActivity != null) {
                AlertDialogUtils.showToast(mActivity, mActivity.getResources()
                        .getString(R.string.aleready_account));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.w(TAG, "signInWithCredential", task.getException());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstants.GOOGLE_SIGN_IN_CODE)
            mGoogleManager.onActivityResult(requestCode, resultCode, data);
    }
}
