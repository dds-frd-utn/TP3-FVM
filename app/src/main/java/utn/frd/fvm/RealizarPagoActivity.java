package utn.frd.fvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RealizarPagoActivity extends AppCompatActivity {

    private Spinner inputCuentaOrigen;
    private EditText inputCuentaDestino;
    private EditText inputCantidad;
    private Button btnEnviarDinero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_pago);

        //Obtengo el id del cliente
        final Integer idCliente = getIntent().getIntExtra("ID",0);

        Toolbar mainToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mainToolbar);

        inputCuentaOrigen = findViewById(R.id.inputCuentaOrigen);
        inputCuentaDestino = findViewById(R.id.inputCuentaDestino);
        inputCantidad = findViewById(R.id.inputCantidad);
        btnEnviarDinero = findViewById(R.id.btnEnviarDinero);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new GetCuentasCliente(idCliente, this).execute();

        btnEnviarDinero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inputCantidad.getText().toString().equals("")) {
                    new VerificarSaldo(inputCuentaOrigen.getSelectedItem().toString()).execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese una cantidad", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private class GetCuentasCliente extends AsyncTask<String, String, String> {
        private Integer id;
        private Context context;

        private GetCuentasCliente(Integer id, Context context) {
            this.id = id;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result;
            try {
                result = RESTService.makeGetRequest(RESTService.apiUrl() + "rest/cuentas/clientes/"+this.id);
                return result;
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray resJson = new JSONArray(result);
                List<String> listaAlias = new ArrayList<>();
                ArrayAdapter<String> adapter;
                listaAlias.add("Seleccione una cuenta");
                for(int i=0; i < resJson.length(); i++) {
                    //Log.d("ALIAS",resJson.getJSONObject(i).getString("aliasCuenta"));
                    listaAlias.add(resJson.getJSONObject(i).getString("aliasCuenta"));
                }
                adapter = new ArrayAdapter<String>(this.context,R.layout.spinner_item,listaAlias){
                    @Override
                    public boolean isEnabled(int position) {
                        if(position == 0){
                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if(position == 0) {
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                adapter.setDropDownViewResource(R.layout.spinner_item);
                inputCuentaOrigen.setAdapter(adapter);
                inputCuentaOrigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItem = (String) parent.getItemAtPosition(position);
                        if(position > 0) {
                            Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class EnviarDinero extends AsyncTask<String, String, String> {
        private JSONObject transaccion;

        private EnviarDinero(JSONObject transaccion) {
            this.transaccion = transaccion;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result;
            try {
                result = RESTService.restCall(RESTService.apiUrl() + "rest/transacciones/realizar", "POST", transaccion);
            } catch(Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Ocurrio un error, intente mas tarde",Toast.LENGTH_SHORT).show();
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.d("RP", s);
                JSONObject res = new JSONObject(s);
                Toast.makeText(getApplicationContext(),res.getString("descripcion"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class VerificarSaldo extends AsyncTask<String, String, String> {
        private String alias;

        private VerificarSaldo(String alias) {
            this.alias = alias;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                result = RESTService.makeGetRequest(RESTService.apiUrl() + "rest/cuentas/alias/"+this.alias);
            } catch(Exception e) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject resJson = new JSONObject(result);
                JSONObject transaccion = new JSONObject();
                transaccion.put("cuentaOrigen", resJson.getString("aliasCuenta").toUpperCase())
                           .put("cuentaDestino", inputCuentaDestino.getText().toString())
                           .put("monto", Integer.parseInt(inputCantidad.getText().toString()))
                           .put("tipoTransaccion", 1) //Compra-Venta = 1
                           .put("fecha", new Date());
                int saldo = resJson.getInt("saldo");
                int monto = transaccion.getInt("monto");

                if(saldo > monto) {
                    new EnviarDinero(transaccion).execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Saldo insuficiente", Toast.LENGTH_SHORT).show();
                    inputCantidad.setText("");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}