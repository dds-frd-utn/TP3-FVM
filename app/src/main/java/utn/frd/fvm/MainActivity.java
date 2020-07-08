package utn.frd.fvm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  {

    JSONObject clienteActual;
    int id = 0;
    String nombreUsuario;
    TextView textUsuario;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView btCuentas = findViewById(R.id.btCuentas);
        CardView btRealizarPago = findViewById(R.id.btRealizarPago);
        CardView btTuPerfil = findViewById(R.id.btTuPerfil);
        CardView btMovimientos = findViewById(R.id.btMovimientos);
        Button logoutBtn = findViewById(R.id.logoutBtn);

        new GetCliente().execute();

        //Mostrar el nombre de usuario
        btRealizarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RealizarPagoActivity.class);
                i.putExtra("ID", id);
                v.getContext().startActivity(i);
            }
         });
        btCuentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), CuentasActivity.class);
                i.putExtra("ID", id);
                v.getContext().startActivity(i);
            }
        });
        btMovimientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MovimientosActivity.class);
                i.putExtra("ID", id);
                v.getContext().startActivity(i);
            }
        });
        btTuPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), TuPerfilActivity.class);
                i.putExtra("ID", id);
                v.getContext().startActivity(i);
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("userId").apply();
                Intent i = new Intent(v.getContext(), LoginActivity.class);
                startActivity(i);
            }
        });

    }

    private class GetCliente extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                int clienteId = preferences.getInt("userId", 0);
                result = RESTService.makeGetRequest(RESTService.apiUrl() + "rest/clientes/" + clienteId);
            } catch(Exception e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject cliente = new JSONObject(s);
                id = cliente.getInt("id");
                nombreUsuario = cliente.getString("nombre");
                textUsuario = findViewById(R.id.textUsuario);
                textUsuario.setText(nombreUsuario);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}