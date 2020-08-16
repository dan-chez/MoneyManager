package co.danchez.moneymanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

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
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import co.danchez.moneymanager.Fragments.Transactions.AddJoinTeamFragment;
import co.danchez.moneymanager.Fragments.Transactions.TransactionsFragment;
import co.danchez.moneymanager.R;
import co.danchez.moneymanager.Utilidades.DialogGeneral;
import co.danchez.moneymanager.Utilidades.Intefaces.OnPushingNewFragmentListener;
import co.danchez.moneymanager.Fragments.Transactions.AddTransactionsFragment;
import co.danchez.moneymanager.Utilidades.SharedPreferencesUtil;

import static co.danchez.moneymanager.Utilidades.ConstantList.FRAGMENT_ADD_TEAM;
import static co.danchez.moneymanager.Utilidades.ConstantList.FRAGMENT_ADD_TRANSACTIONS;
import static co.danchez.moneymanager.Utilidades.ConstantList.FRAGMENT_TRANSACTIONS;
import static co.danchez.moneymanager.Utilidades.ConstantList.ID_TEAM_PREFERENCES;
import static co.danchez.moneymanager.Utilidades.ConstantList.ID_USER_PREFERENCES;

public class MainActivity extends AppCompatActivity implements OnPushingNewFragmentListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = "MainActivity";
    private RelativeLayout rl_loading;
    private SharedPreferencesUtil sharedPreferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        rl_loading = findViewById(R.id.rl_loading);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTransactionsFragment addTransactionsFragment = AddTransactionsFragment.newInstance();
                onPushNewFragment(addTransactionsFragment, FRAGMENT_ADD_TRANSACTIONS);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        sharedPreferencesUtil = new SharedPreferencesUtil(this);

        if (sharedPreferencesUtil.readStringPreference(ID_TEAM_PREFERENCES) != null && !sharedPreferencesUtil.readStringPreference(ID_TEAM_PREFERENCES).isEmpty()) {
            loadTransactionsFragment();
        } else {
            loadAddJoinFragment();
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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
        // Firebase sign out
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Google sign out
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(MainActivity.this);
                        sharedPreferencesUtil.removePreference(ID_TEAM_PREFERENCES);
                        sharedPreferencesUtil.removePreference(ID_USER_PREFERENCES);
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                });
    }

    public void showProgress() {
        rl_loading.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        rl_loading.setVisibility(View.GONE);
    }

}