package co.danchez.moneymanager.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import co.danchez.moneymanager.Connectivity.FirebaseManager;
import co.danchez.moneymanager.R;
import co.danchez.moneymanager.Utilidades.ConstantList;
import co.danchez.moneymanager.Utilidades.DialogGeneral;
import co.danchez.moneymanager.Utilidades.LoadingView;
import co.danchez.moneymanager.Utilidades.SharedPreferencesUtil;

import static co.danchez.moneymanager.Utilidades.ConstantList.EMAIL_USER;
import static co.danchez.moneymanager.Utilidades.ConstantList.ID_USER_PREFERENCES;
import static co.danchez.moneymanager.Utilidades.ConstantList.USERS_COLLECTION;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private LoadingView loadingView;
    private FirebaseManager firebaseManager;
    private SharedPreferencesUtil sharedPreferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingView = findViewById(R.id.loadingView);
        SignInButton signInButton = findViewById(R.id.signInButton);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);

        firebaseManager = new FirebaseManager();

        sharedPreferencesUtil = new SharedPreferencesUtil(LoginActivity.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

    }

    private void firebaseAuthWithGoogle(String idToken) {
        // [START_EXCLUDE silent]
        loadingView.showLoading();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        checkIfUserExist(Objects.requireNonNull(user));
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        DialogGeneral
                                .newInstance()
                                .setIcon(R.drawable.ic_error)
                                .setTitle(getString(R.string.error))
                                .setSubtitle(getString(R.string.error_login))
                                .isAccept(null)
                                .show(getSupportFragmentManager(), "");
                        updateUI(null);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        loadingView.hideLoading();
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signInButton) {
            signIn();
        }
    }

    private void checkIfUserExist(final FirebaseUser user) {
        firebaseManager.readDataFromCollection(task -> {
            loadingView.hideLoading();
            if (task.isSuccessful()) {
                if (Objects.requireNonNull(task.getResult()).size() == 0) {
                    addUser(Objects.requireNonNull(user));
                } else {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        sharedPreferencesUtil.saveStringPreference(ID_USER_PREFERENCES, document.getId());
                    }
                    updateUI(user);
                }
            } else {
                DialogGeneral
                        .newInstance()
                        .setIcon(R.drawable.ic_error)
                        .setTitle(getString(R.string.error))
                        .setSubtitle(getString(R.string.error_get_data))
                        .isAccept(null)
                        .show(getSupportFragmentManager(), "");
            }
        }, e -> {
            loadingView.hideLoading();
            DialogGeneral
                    .newInstance()
                    .setIcon(R.drawable.ic_error)
                    .setTitle(getString(R.string.error))
                    .setSubtitle(getString(R.string.error_get_data))
                    .isAccept(null)
                    .show(getSupportFragmentManager(), "");
        }, USERS_COLLECTION, EMAIL_USER, user.getEmail());
    }

    private void addUser(final FirebaseUser user) {
        loadingView.showLoading();
        Map<String, Object> newObject = new HashMap<>();
        newObject.put(ConstantList.NAME_USER, user.getDisplayName());
        newObject.put(ConstantList.EMAIL_USER, user.getEmail());
        newObject.put(ConstantList.UID_USER, user.getUid());
        Uri uriFoto = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl();
        newObject.put(ConstantList.PHOTO_USER, Objects.requireNonNull(uriFoto).toString());
        firebaseManager.addElement(newObject, documentReference -> {
            sharedPreferencesUtil.saveStringPreference(ID_USER_PREFERENCES, documentReference.getId());
            updateUI(user);
        }, e -> updateUI(null), USERS_COLLECTION);
    }

}