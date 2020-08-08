package co.danchez.moneymanager.Utilidades;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import co.danchez.moneymanager.R;

public class Util {

    /**
     * This method change button state
     */
    public static void cambiarEstadoButton(Context ctx, Button btView, Boolean activo) {
        if (activo) {
            btView.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary));
            btView.setTextColor(ctx.getResources().getColor(R.color.white));
            btView.setActivated(true);
        } else {
            btView.setBackground(ctx.getResources().getDrawable(R.drawable.button_bg_primarycolor_borders));
            btView.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
            btView.setActivated(false);
        }
    }

    /**
     * This method hide the keyboard
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

}
