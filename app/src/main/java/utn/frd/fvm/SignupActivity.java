package utn.frd.fvm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button btnRegistrarse = findViewById(R.id.btnRegistrarse);
        final EditText nombreApellido = findViewById(R.id.nombreApellido);
        final EditText usuario = findViewById(R.id.usuario);
        final EditText direccion = findViewById(R.id.direccion);
        final EditText password = findViewById(R.id.password);
        final EditText confPassword = findViewById(R.id.confPassword);


        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contra1 = password.getText().toString();
                String contra2 = confPassword.getText().toString();
                if (contra1.equals(contra2)){
                    new registro(nombreApellido.getText().toString(),usuario.getText().toString(),direccion.getText().toString(),contra1).execute();
                }else{
                    Toast wrongpass = Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden :(", Toast.LENGTH_LONG);
                    wrongpass.show();
                    password.setText("");
                    confPassword.setText("");
                }
            }
        });
    }

    private class registro extends AsyncTask <String,String,String>{
        String nombreApellido;
        String usuario;
        String direccion;
        String password;

        public registro (String nombreApellido,String usuario,String direccion,String password){
            this.nombreApellido = nombreApellido;
            this.usuario = usuario;
            this.direccion = direccion;
            this.password = password;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                JSONObject nuevoCliente = new JSONObject();
                nuevoCliente.put("usuario",this.usuario);
                nuevoCliente.put("nombre",this.nombreApellido);
                nuevoCliente.put("direccion",this.direccion);
                nuevoCliente.put("password",this.password);
                result = RESTService.restCall(RESTService.apiUrl() + "rest/clientes", "POST",nuevoCliente);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) { //Lo que retorna doInBackground, es lo que consume onPostExecute
            if(!result.equals("Connection refused")) {
                Toast registroExitoso = Toast.makeText(getApplicationContext(), "Genial! ya sos parte del mejor banco de sistemas", Toast.LENGTH_LONG);
                registroExitoso.show();
                EditText nombreApellido = findViewById(R.id.nombreApellido);
                EditText usuario = findViewById(R.id.usuario);
                EditText direccion = findViewById(R.id.direccion);
                EditText password = findViewById(R.id.password);
                EditText confPassword = findViewById(R.id.confPassword);
                nombreApellido.setText("");
                usuario.setText("");
                direccion.setText("");
                password.setText("");
                confPassword.setText("");
            } else {
                Toast.makeText(getApplicationContext(), "Servidor apagado", Toast.LENGTH_SHORT).show();
            }
        }
    }

}