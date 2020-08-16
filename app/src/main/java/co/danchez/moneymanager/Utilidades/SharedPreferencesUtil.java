package co.danchez.moneymanager.Utilidades;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    private SharedPreferences sharedPreferences;

    public SharedPreferencesUtil(Activity activity) {
        sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveStringPreference(String namePreference, String valueStringPreference) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (valueStringPreference != null && !valueStringPreference.isEmpty()) {
            editor.putString(namePreference, valueStringPreference);
        }
        editor.apply();
    }

    public void saveIntPreference(String namePreference, int valueIntPreference) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(namePreference, valueIntPreference);
        editor.apply();
    }

    public void saveBooleanPreference(String namePreference, boolean valueBooleanPreference) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(namePreference, valueBooleanPreference);
        editor.apply();
    }

    public String readStringPreference(String namePreference) {
        return sharedPreferences.getString(namePreference, null);
    }

    public int readIntPreference(String namePreference) {
        return sharedPreferences.getInt(namePreference, -1);
    }

    public boolean readBooleanPreference(String namePreference) {
        return sharedPreferences.getBoolean(namePreference, false);
    }

    public void removePreference(String namePreference) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(namePreference);
        editor.apply();
    }

}
