package co.danchez.moneymanager.Utilidades;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import co.danchez.moneymanager.R;

public class Util {

    /**
     * This method change button state
     */
    public static void changeButtonState(Context ctx, Button btView, Boolean activo) {
        if (activo) {
            btView.setBackground(ctx.getResources().getDrawable(R.drawable.button_bg_disabled));
            btView.setTextColor(ctx.getResources().getColor(R.color.white));
            btView.setActivated(true);
        } else {
            btView.setBackground(ctx.getResources().getDrawable(R.drawable.button_bg_enabled));
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

    /***
     * Crea alertDialog con un mensaje informativo simple
     * ***/
    public static void alertDialogSimple(Context context, String texto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(texto);
        builder.setPositiveButton(context.getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

}
