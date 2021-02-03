package co.danchez.moneymanager.Utilidades;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import co.danchez.moneymanager.R;

public class LoadingView extends RelativeLayout {

    private final RelativeLayout rl_loading;

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_loading, this);
        rl_loading = findViewById(R.id.rl_loading);
    }

    public void showLoading() {
        rl_loading.setVisibility(VISIBLE);
    }

    public void hideLoading() {
        rl_loading.setVisibility(GONE);
    }

}
