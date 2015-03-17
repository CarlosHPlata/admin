package com.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.controllers.LogInController;
import com.models.services.AlertDialogService;
import com.example.usuario.androidadmin.R;

public class Login extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.logincontroller = new LogInController(getApplicationContext());

       if(this.logincontroller.isUserLogIn()){
          // AlertDialogService alert = new AlertDialogService();
          // alert.showAlertDialog(Login.this, "Entro1", "Email/Contraseña son incorrectos", false);
           Intent i = new Intent(this,ListNotes.class);
           startActivity(i);
           this.finish();
        }

    }


    public void logIn(View view){
      EditText txtUsername = (EditText) findViewById(R.id.email);
      EditText  txtPassword = (EditText) findViewById(R.id.password);

      String email =   txtUsername.getText().toString();
      String password = txtPassword.getText().toString();

        AlertDialogService alert = new AlertDialogService();
        if(email.trim().length() > 0 && password.trim().length() > 0){
            if(this.logincontroller.logIn(email, password)){
                Intent i = new Intent(this,ListNotes.class);
                startActivity(i);
                this.finish();
                //alert.showAlertDialog(Login.this, "Login Success..", "Username/Password is correct", true);
            }else{
                alert.showAlertDialog(Login.this, "Error", "Email/Contraseña son incorrectos", false);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    private LogInController logincontroller;
}
