package com.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.controllers.LogInController;
import com.controllers.sync.SyncNotesHandler;
import com.controllers.sync.SyncUserHandler;
import com.controllers.sync.interfaces.SyncInterface;
import com.models.User;
import com.models.mappers.UserMapper;
import com.models.services.AlertDialogService;
import com.example.usuario.androidadmin.R;
import com.models.services.LoginService;


/**
 * Clase que se encarga de mostar el login para acceder a la app.
 * @author Edgar
 * @version 0.1 13/02/2015.
 *
 */
public class Login extends ActionBarActivity implements SyncInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.logincontroller = new LogInController(getApplicationContext());

        ediTextUsername = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        ediTextUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.e("Login","OnEditTorAction");
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    Log.e("Login","ENTRO");
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(ediTextUsername.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        /*ediTextUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e("Login","Entro a onFocusChange");
                if (!hasFocus) {
                    Log.e("Login","Se perdio el focus");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        }); */

       if(this.logincontroller.isUserLogIn()){
          // AlertDialogService alert = new AlertDialogService();
          // alert.showAlertDialog(Login.this, "Entro1", "Email/Contraseña son incorrectos", false);
           Intent i = new Intent(this,MainActivity.class);
           startActivity(i);
           this.finish();
        }

    }

    public void logIn(View view){
    //  EditText txtUsername = (EditText) findViewById(R.id.email);
    //  EditText  txtPassword = (EditText) findViewById(R.id.password);

      String email =   ediTextUsername.getText().toString();
      String password = editTextPassword.getText().toString();

        AlertDialogService alert = new AlertDialogService();
        if(email.trim().length() > 0 && password.trim().length() > 0){
            if(this.logincontroller.logIn(email, password)){
               // Intent i = new Intent(this,ListNotes.class);
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                this.finish();
                //alert.showAlertDialog(Login.this, "Login Success..", "Username/Password is correct", true);
            }else{
                SyncUserHandler sync = new SyncUserHandler(getApplicationContext(), this);
                User user = new User();
                user.setEmail(email);
                user.setPassword(password);
                sync.getToken(user);
                Button btnLogin = (Button) findViewById(R.id.button7);
                Button register = (Button) findViewById(R.id.registerButton);
                btnLogin.setEnabled(false);
                register.setEnabled(false);
                ediTextUsername.setEnabled(false);
                editTextPassword.setEnabled(false);
                //alert.showAlertDialog(Login.this, "Error", "Email/Contraseña son incorrectos", false);
            }

        }else{
            alert.showAlertDialog(Login.this, "Error", "Email/Contraseña son incorrectos", false);
        }
    }

    public void register(View view) {
        Intent i = new Intent(this, Register.class);
        startActivity(i);
        this.finish();
    }

    private LogInController logincontroller;

    @Override
    public void onResponse(Object response) {

        final EditText txtUsername = (EditText) findViewById(R.id.email);
        final EditText  txtPassword = (EditText) findViewById(R.id.password);
        final Button btnLogin = (Button) findViewById(R.id.button7);
        final Button register = (Button) findViewById(R.id.registerButton);

        final AlertDialogService alert = new AlertDialogService();

        final User user = (User) response;

        Log.i("error", user.getPassword());

        UserMapper mapper = new UserMapper(getApplicationContext());
        user.setId(-1);
        mapper.dropUsers();
        int jo = (int) mapper.insertUser(user);
        user.setId(jo);

        SyncNotesHandler notesHandler = new SyncNotesHandler(getApplicationContext(), new SyncInterface() {
            @Override
            public void onResponse(Object response) {
                LoginService service = new LoginService(getApplicationContext());
                service.addUserToSession(user);
                goToMain();
            }

            @Override
            public void onError(int StatusCode, String error) {
                btnLogin.setEnabled(true);
                register.setEnabled(true);
                txtUsername.setEnabled(true);
                txtPassword.setEnabled(true);
                alert.showAlertDialog(Login.this, "Error", "No se pudo sincronizar", false);
            }
        });
        notesHandler.getNotesFromuser(user.getToken());
    }

    @Override
    public void onError(int StatusCode, String error) {
        EditText txtUsername = (EditText) findViewById(R.id.email);
        EditText  txtPassword = (EditText) findViewById(R.id.password);
        Button btnLogin = (Button) findViewById(R.id.button7);
        Button register = (Button) findViewById(R.id.registerButton);
        btnLogin.setEnabled(true);
        register.setEnabled(true);
        txtUsername.setEnabled(true);
        txtPassword.setEnabled(true);
        AlertDialogService alert = new AlertDialogService();
        alert.showAlertDialog(Login.this, "Error", "Email/Contraseña son incorrectos", false);
    }

    public void goToMain(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        this.finish();
    }

    private EditText ediTextUsername;
    private EditText editTextPassword;
}
