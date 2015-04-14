package com.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        Button btnRegister = (Button) findViewById(R.id.registerButton);
        Button btnCancel = (Button) findViewById(R.id.cancelButton);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAction();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToLogin();
            }
        });
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

    public void registerAction(){
        EditText txtUsername = (EditText) findViewById(R.id.emailText);
        EditText  txtPassword = (EditText) findViewById(R.id.passText);
        EditText txtPassword2 = (EditText) findViewById(R.id.pass2Text);

        String email =   txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        String password2 = txtPassword2.getText().toString();

        if (isEmailValid(email)){
            if (isPasswordCorrect(password, password2)){
                if (!controller.isUserRegistered(email, password)){
                    User user = new User();
                    user.setEmail(email);
                    user.setPassword(password);
                    this.sync.createUser(user);
                } else showToastMessage("Ya existe el usuario");
            } else showToastMessage("Contraseñas no coinciden");
        } else showToastMessage("E-Mail invalido");


    }

    public void returnToLogin(){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        this.finish();
    }

    public void showToastMessage(String msg){
        Toast toast1 = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast1.setGravity(Gravity.CENTER_HORIZONTAL, 0, 100);
        toast1.show();
    }

    public boolean isPasswordCorrect(String pass, String pass2) {
        boolean result = false;
        if (!pass.equals("") && !pass.equals("") && pass.equals(pass2)) result = true;
        return result;
    }

    boolean isEmailValid(CharSequence email) {
        if (email.equals(""))   return false;
        else return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private RegisterController controller;
    private syncUserHandler sync;

    @Override
    public void onResponse(Object thisNote) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("REGISTRO:");

        // set dialog message
        alertDialogBuilder
                .setMessage("Se ha registrado correctamente, por favor inicie sesion")
                .setCancelable(false)
                .setPositiveButton("Ok Im ready!",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        returnToLogin();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    @Override
    public void onError(int StatusCode, String error) {
        String msg;
        switch (StatusCode){
            case 500:
                msg = "Fatal error";
                break;
            case 1:
                msg = "Email esta en uso o ha sido desabilitado";
                break;
            case 2:
                msg = "La contrasena es muy corta";
                break;
            default:
                msg = "Fatal error";
                break;
        }
        showToastMessage(msg);
    }

}
