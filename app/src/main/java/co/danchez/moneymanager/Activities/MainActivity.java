package co.danchez.moneymanager.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import co.danchez.moneymanager.R;
import co.danchez.moneymanager.Utilidades.Intefaces.OnPushingNewFragmentListener;
import co.danchez.moneymanager.ui.Transactions.AddTransactionsFragment;

import static co.danchez.moneymanager.Utilidades.ConstantList.FRAGMENT_ADD_TRANSACTIONS;

public class MainActivity extends AppCompatActivity implements OnPushingNewFragmentListener {

    private static final String LOG_TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    private RelativeLayout rl_loading;

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
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onPushNewFragment(Fragment fragment, String tag) {
        Log.d(LOG_TAG, Objects.requireNonNull(fragment.getClass().getCanonicalName()));
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.nav_host_fragment);
        if (!tag.equals(currentFragment.getTag())) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                    .addToBackStack(tag)
                    .replace(R.id.nav_host_fragment, fragment, tag)
                    .commit();
        }
    }

    public void showProgress() {
        rl_loading.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        rl_loading.setVisibility(View.GONE);
    }

}