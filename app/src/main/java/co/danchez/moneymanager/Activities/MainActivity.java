package co.danchez.moneymanager.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import co.danchez.moneymanager.Connectivity.FirebaseManager;
import co.danchez.moneymanager.Fragments.Transactions.AddJoinTeamFragment;
import co.danchez.moneymanager.Fragments.Transactions.TransactionsFragment;
import co.danchez.moneymanager.R;
import co.danchez.moneymanager.Utilidades.DialogGeneral;
import co.danchez.moneymanager.Utilidades.Intefaces.OnPushingNewFragmentListener;
import co.danchez.moneymanager.Fragments.Transactions.AddTransactionsFragment;
import co.danchez.moneymanager.Utilidades.LoadingView;
import co.danchez.moneymanager.Utilidades.SharedPreferencesUtil;

import static co.danchez.moneymanager.Utilidades.ConstantList.FRAGMENT_ADD_TEAM;
import static co.danchez.moneymanager.Utilidades.ConstantList.FRAGMENT_ADD_TRANSACTIONS;
import static co.danchez.moneymanager.Utilidades.ConstantList.FRAGMENT_TRANSACTIONS;
import static co.danchez.moneymanager.Utilidades.ConstantList.ID_TEAM_PREFERENCES;
import static co.danchez.moneymanager.Utilidades.ConstantList.ID_USER_PREFERENCES;

public class MainActivity extends AppCompatActivity implements OnPushingNewFragmentListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = "MainActivity";
    public LoadingView loadingView;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private FirebaseManager firebaseManager;
    private FirebaseUser currentUser;
    private NavigationView navigationView;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        loadingView = findViewById(R.id.loadingView);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTransactionsFragment addTransactionsFragment = AddTransactionsFragment.newInstance();
                onPushNewFragment(addTransactionsFragment, FRAGMENT_ADD_TRANSACTIONS);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menu = navigationView.getMenu();

        sharedPreferencesUtil = new SharedPreferencesUtil(this);
        firebaseManager = new FirebaseManager();

        if (sharedPreferencesUtil.getIdTeamUser() != null && !sharedPreferencesUtil.getIdTeamUser().isEmpty()) {
            loadTransactionsFragment();
        } else {
            loadAddJoinFragment();
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        ImageView iv_photo = headerLayout.findViewById(R.id.iv_photo);
        Picasso.get().load(currentUser.getPhotoUrl()).resize(200, 200).centerCrop().into(iv_photo);
        TextView tv_user_name = headerLayout.findViewById(R.id.tv_user_name);
        TextView tv_email = headerLayout.findViewById(R.id.tv_email);
        tv_user_name.setText(currentUser.getDisplayName());
        tv_email.setText(currentUser.getEmail());
        showCopyIDTeam();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_logout) {
            DialogGeneral
                    .newInstance()
                    .setIcon(R.drawable.ic_menu_logout)
                    .setTitle(getString(R.string.logout))
                    .setSubtitle(getString(R.string.logut_confirm))
                    .isConfirm(v -> signOut())
                    .show(getSupportFragmentManager(), "");
        } else if (id == R.id.nav_copy_id) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("text", sharedPreferencesUtil.getIdTeamUser());
            clipboard.setPrimaryClip(clip);
            Snackbar.make(MainActivity.this.getCurrentFocus(), getString(R.string.id_team_copiado), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onPushNewFragment(Fragment fragment, String tag) {
        Log.d(LOG_TAG, Objects.requireNonNull(fragment.getClass().getCanonicalName()));
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.nav_host_fragment);
        if (tag.equals(FRAGMENT_TRANSACTIONS)) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (!tag.equals(Objects.requireNonNull(currentFragment).getTag())) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                    .addToBackStack(tag)
                    .replace(R.id.nav_host_fragment, fragment, tag)
                    .commit();
        }
    }

    public void loadTransactionsFragment() {
        TransactionsFragment transactionsFragment = TransactionsFragment.newInstance();
        onPushNewFragment(transactionsFragment, FRAGMENT_TRANSACTIONS);
    }

    public void loadAddJoinFragment() {
        AddJoinTeamFragment addTeamFragment = AddJoinTeamFragment.newInstance();
        onPushNewFragment(addTeamFragment, FRAGMENT_ADD_TEAM);
    }

    private void signOut() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> {
                    SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(MainActivity.this);
                    sharedPreferencesUtil.logOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                });
    }

    public FirebaseManager getFirebaseManager() {
        return firebaseManager;
    }

    public SharedPreferencesUtil getSharedPreferencesUtil() {
        return sharedPreferencesUtil;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public void showCopyIDTeam() {
        menu.findItem(R.id.nav_copy_id).setVisible(sharedPreferencesUtil.getIdTeamUser() != null && !sharedPreferencesUtil.getIdTeamUser().isEmpty());
    }

}