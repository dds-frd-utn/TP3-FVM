package utn.frd.fvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class TuPerfilActivity extends AppCompatActivity {

    private EditText inputNombre;
    private EditText inputDireccion;
    private EditText inputUsuario;
    private EditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_perfil);

        //Obtengo el id del cliente
        final Integer idCliente = getIntent().getIntExtra("ID",0);

        Toolbar mainToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputNombre = findViewById(R.id.inputNombre);
        inputDireccion = findViewById(R.id.inputDireccion);
        inputUsuario = findViewById(R.id.inputUsuario);
        inputPassword = findViewById(R.id.inputPassword);

        new GetCliente(idCliente).execute();

        Button btnModificarDatos = findViewById(R.id.btnModificarDatos);

        btnModificarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateCliente(idCliente).execute();
            }
        });
    }

    private class GetCliente extends AsyncTask<String, String, String> {
        private Integer id;

        private GetCliente(Integer id) {
            this.id = id;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result;
            try {
                result = RESTService.makeGetRequest("http://192.168.100.6:8080/TP1-FVM/rest/clientes/" + this.id);
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject result = new JSONObject(s);
                inputNombre.setText(result.getString("nombre"));
                inputDireccion.setText(result.getString("direccion"));
                inputUsuario.setText(result.getString("usuario"));
                inputPassword.setText(result.getString("password"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class UpdateCliente extends AsyncTask<String, String, String> {
        private Integer id;

        private UpdateCliente(Integer id) { this.id = id; }

        @Override
        protected String doInBackground(String... strings) {
            String result;
            try {
                JSONObject updateCliente = new JSONObject();
                updateCliente.put("id", this.id)
                             .put("nombre", inputNombre.getText())
                             .put("direccion", inputDireccion.getText())
                             .put("usuario", inputUsuario.getText())
                             .put("password", inputPassword.getText());
                result = RESTService.restCall("http://192.168.100.6:8080/TP1-FVM/rest/clientes/"+ this.id,"PUT", updateCliente);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), "Tus datos fueron modificados", Toast.LENGTH_SHORT).show();
        }
    }
}