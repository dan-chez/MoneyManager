package co.danchez.moneymanager.Utilidades;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    /***
     * Convierte fecha timestamp en string con el formato que se ingrese
     * ***/
    public static String convertTimestampToString(Timestamp timestamp, String format) {
        if (timestamp != null) {
            Date date = timestamp.toDate();
            Locale locale = new Locale("es_ES");
            SimpleDateFormat dateFormat;
            if (format != null) {
                dateFormat = new SimpleDateFormat(format, locale);
            } else {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", locale);
            }
            return dateFormat.format(date);
        } else {
            return "";
        }
    }

}
