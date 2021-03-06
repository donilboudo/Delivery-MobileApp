package ca.beogotechnologies.deliverymanager_mobileapp.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import ca.beogotechnologies.deliverymanager_mobileapp.activities.common.LoginActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by fabrice on 2017-03-27.
 */

public class LoginUtil {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String USER_SESSION_ID = "USER_SESSION_ID";
    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";
    public static final String CURRENT_USER_FIRST_NAME = "CURRENT_USER_FIRST_NAME";
    public static final String CURRENT_USER_LAST_NAME = "CURRENT_USER_LAST_NAME";
    public static final String CURRENT_USER_PHONE = "CURRENT_USER_PHONE";
    public static final String CURRENT_USER_EMAIL = "CURRENT_USER_LAST_EMAIL";
    public static final String CURRENT_USER_USER_NAME = "CURRENT_USER_USER_NAME";
    public static final String CURRENT_USER_PASSWORD = "CURRENT_USER_PASSWORD";
    public static final String CURRENT_USER_ROLE = "CURRENT_USER_ROLE";

    public static final String USER_ROLE_EMPLOYEE = "EMPLOYEE";
    public static final String USER_ROLE_ADMIN = "ADMIN";

    public static void logout(Context context) {
        //clean user session in shared preference
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_SESSION_ID, "");
        editor.apply();

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
