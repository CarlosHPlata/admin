package com.view;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.controllers.RegisterController;
import com.controllers.SyncController;
import com.controllers.sync.SyncNotesHandler;
import com.controllers.sync.interfaces.SyncInterface;
import com.controllers.sync.syncUserHandler;
import com.example.usuario.androidadmin.R;
import com.models.Note;
import com.models.User;
import com.models.services.AlertDialogService;

public class Register extends ActionBarActivity implements SyncInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.controller = new RegisterController(getApplicationContext());
        this.sync = new syncUserHandler(getApplicationContext(), Register.this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void registerAction(View view){
        EditText txtUsername = (EditText) findViewById(R.id.emailText);
        EditText  txtPassword = (EditText) findViewById(R.id.passText);
        EditText txtPassword2 = (EditText) findViewById(R.id.pass2Text);

        String email =   txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        String password2 = txtPassword2.getText().toString();

        AlertDialogService alert = new AlertDialogService();

        if (isEmailValid(email)){
            if (isPasswordCorrect(password, password2)){
                if (!controller.isUserRegistered(email, password)){
                    User user = new User();
                    user.setEmail(email);
                    user.setPassword(password);
                    this.sync.getToken(user);
                } else alert.showAlertDialog(Register.this, "Error", "Ya existe", false);

            } else alert.showAlertDialog(Register.this, "Error", "Contraseñas no coinciden", false);
        }


    }

    public boolean isPasswordCorrect(String pass, String pass2) {
        boolean result = false;
        if (!pass.equals("") && !pass.equals("") && pass.equals(pass2)) result = true;
        return result;
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private RegisterController controller;
    private syncUserHandler sync;

    @Override
    public void onResponse(Object thisNote) {
        AlertDialogService alert = new AlertDialogService();
        alert.showAlertDialog(Register.this, "REGISTRO:", "Se registro correctamente", false);
    }

    @Override
    public void onError(int StatusCode, String error) {
        //TODO HERE
    }

}
