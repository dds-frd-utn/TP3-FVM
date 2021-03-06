package utn.frd.fvm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Si hay una sesion activa, ir al main
        SharedPreferences pref = getSharedPreferences("myPrefs", MODE_PRIVATE);
        if(pref.getInt("userId", -1) != -1) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        Button btnLogin = findViewById(R.id.btnLogin);
        TextView textSignup = findViewById(R.id.textSignup);
        final EditText usuario = findViewById(R.id.inputUsuario);
        final EditText password = findViewById(R.id.inputPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DoLogin(usuario.getText().toString(), password.getText().toString(), v.getContext()).execute();
            }
        });

        textSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), SignupActivity.class);
                v.getContext().startActivity(i);
            }
        });
    }
    private class DoLogin extends AsyncTask<String, String, String> {
        private String usuario;
        private String password;
        private Context context;

        private DoLogin(String usuario, String password, Context context) {
            this.usuario = usuario;
            this.password = password;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) { //Peticion a la base de datos
            String result = null;
            JSONObject datosUsuario = new JSONObject();
            try {
                datosUsuario
                        .put("usuario", this.usuario)
                        .put("password", this.password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                result = RESTService.restCall(RESTService.apiUrl() +  "rest/clientes/login", "POST", datosUsuario);
            } catch (Exception e) {
                Log.d("LOGGER", e.toString());
                e.printStackTrace();
            }
            if (result != null) {
                Log.d("LOGGER", result.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) { //Lo que retorna doInBackground, es lo que consume onPostExecute
            if(!result.equals("Connection refused")) {
                try {
                    JSONObject cliente = new JSONObject(result);
                    if(cliente.getInt("error_code") != 1) {
                        Integer id = cliente.getInt("id");
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                        preferences.edit().putInt("userId", id).apply();
                        startActivity(i);
                    } else {
                        //Error en los datos de ingreso
                        Toast loginError = Toast.makeText(getApplicationContext(), "Error en los datos de ingreso", Toast.LENGTH_LONG);
                        loginError.show();
                        EditText usuario = findViewById(R.id.inputUsuario);
                        EditText password = findViewById(R.id.inputPassword);
                        usuario.setText("");
                        password.setText("");
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Servidor apagado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}