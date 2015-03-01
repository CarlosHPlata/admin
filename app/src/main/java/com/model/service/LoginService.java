package com.model.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.model.mapper.UserMapper;
import com.model.User;
import com.view.Login;

/**
 * Created by Edgar on 28/02/2015.
 */
public class LoginService {

    SharedPreferences pref;

    Editor editor;

    Context context;

    int PRIVATE_MODE = 0;

    // nombre del archivo preference
    private String PREFER_NAME = "AndroidExamplePref";

   private String IS_USER_LOGIN = "IsUserLoggedIn";

   public String KEY_ID = "id";
   public String KEY_PASSWORD = "password";
   public String KEY_EMAIL = "email";
   public String KEY_TOKEN = "token";

    // Constructor
    public LoginService(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        userMAnager = new UserMapper(context);
    }

    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, Login.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);

            return false;
        }
        return true;
    }


    public boolean logIn(String email,String password){
       // = new UserMapper(this.context);
        User user = userMAnager.findOneByEmailAndPassword(email, password);
        if(user != null){
            return true;
        }else{
            return false;
        }

    }

    public void addUserToSession(String email, String password){
        //UserMapper userMAnager = new UserMapper(this.context);
        User user = userMAnager.findOneByEmailAndPassword(email, password);

        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putInt(this.KEY_ID, user.getId());
        editor.putString(this.KEY_EMAIL, user.getEmail());
        editor.putString(this.KEY_PASSWORD, user.getPassword());
        editor.putString(this.KEY_TOKEN, user.getToken());
        // commit changes
        editor.commit();
    }

    public boolean isUserLoggedIn(){
        return pref.getBoolean(this.IS_USER_LOGIN, false);
    }

    public boolean logOut(){

        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, Login.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
        return true;
    }

    public User getUserOfSession(){
        User user = new User();
        user.setId(pref.getInt(this.KEY_ID, 0));
        user.setEmail(pref.getString(this.KEY_EMAIL,null));
        user.setPassword(pref.getString(this.KEY_PASSWORD, null));
        user.setToken(pref.getString(this.KEY_TOKEN, null));

        return user;
    }

    private UserMapper userMAnager;



}
