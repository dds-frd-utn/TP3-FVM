package utn.frd.fvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class TuPerfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_perfil);

        //Obtengo el id del cliente
        Integer idCliente = getIntent().getIntExtra("ID",0);

        Toolbar mainToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new GetCliente(idCliente).execute();


        Button btnModificarDatos = findViewById(R.id.btnModificarDatos);

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
                result = RESTService.makeGetRequest("http://192.168.100.6:8080/TP1-FVM/rest/clientes/" + id);
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
                EditText inputNombre = findViewById(R.id.inputNombre);
                inputNombre.setText(result.getString("nombre"));
                EditText inputDireccion = findViewById(R.id.inputDireccion);
                inputDireccion.setText(result.getString("direccion"));
                EditText inputUsuario = findViewById(R.id.inputUsuario);
                inputUsuario.setText(result.getString("usuario"));
                EditText inputPassword = findViewById(R.id.inputPassword);
                inputPassword.setText(result.getString("password"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}