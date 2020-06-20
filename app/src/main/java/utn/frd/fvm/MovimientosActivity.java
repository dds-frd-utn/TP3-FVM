package utn.frd.fvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utn.frd.fvm.adapters.Cuenta;
import utn.frd.fvm.adapters.Movimiento;
import utn.frd.fvm.adapters.MovimientosAdapter;

public class MovimientosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovimientosAdapter mAdapter;
    private List<Movimiento> listaMovimientos = new ArrayList<>();
    private Spinner inputCuentaOrigen;
    private EditText inputCantidad;
    private Button btnVerMovimientos;
    private Integer idCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimientos);

        Toolbar mainToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Obtengo el id del cliente
        final Integer idCliente = getIntent().getIntExtra("ID",0);

        recyclerView = findViewById(R.id.listaMovimientos);

        inputCuentaOrigen = findViewById(R.id.inputCuentaOrigen);
        inputCantidad = findViewById(R.id.inputCantidad);
        btnVerMovimientos = findViewById(R.id.btnVerMovimientos);

        mAdapter = new MovimientosAdapter(listaMovimientos);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        btnVerMovimientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inputCantidad.getText().toString().equals("")) {
                    mAdapter.clear();
                    new GetUltimosMovimientos(idCuenta, Integer.parseInt(inputCantidad.getText().toString())).execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese una cantidad", Toast.LENGTH_SHORT).show();
                }
            }
        });

        new GetCuentasCliente(idCliente, this).execute();
    }

    private class GetUltimosMovimientos extends AsyncTask<String, String, String> {
        private Integer idCuenta;
        private int cantidad;

        private GetUltimosMovimientos(Integer idCuenta, int cantidad) {
            this.idCuenta = idCuenta;
            this.cantidad = cantidad;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result;
            try {
                result = RESTService.makeGetRequest("http://192.168.100.6:8080/TP1-FVM/rest/transacciones/"+this.idCuenta+"/ultimas/"+this.cantidad);
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
            return result;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String s) {
            //Log.d("ULTIMOS", s);
            try {
                JSONArray movimientos = new JSONArray(s);

                for(int i=0; i < movimientos.length(); i++) {
                    JSONObject movimiento = movimientos.getJSONObject(i);
                    String msg;
                    //Verifico si es emisor o receptor para setear el mensaje
                    if(movimiento.getInt("idOrigen") == this.idCuenta) {
                        //Es emisor
                        msg = "Pagaste a " + movimiento.getString("aliasDestino");
                    } else {
                        //Es receptor
                        msg = movimiento.getString("aliasOrigen") + " te pago";
                    }
                    Movimiento mov = new Movimiento(movimiento.getInt("monto"),msg, new Date());
                    listaMovimientos.add(mov);
                }
                mAdapter.notifyDataSetChanged();
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
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
                    result = RESTService.makeGetRequest("http://192.168.100.6:8080/TP1-FVM/rest/cuentas/clientes/"+this.id);
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
                    ArrayList<Cuenta> listaCuentas = new ArrayList<>();
                    ArrayAdapter<Cuenta> adapter;
                    listaCuentas.clear();
                    listaCuentas.add(new Cuenta(0, "Seleccione una cuenta"));
                    for(int i=0; i < resJson.length(); i++) {
                        JSONObject cuenta = resJson.getJSONObject(i);
                        //listaAlias.add(resJson.getJSONObject(i).getString("aliasCuenta"));
                        listaCuentas.add(new Cuenta(cuenta.getInt("id"),cuenta.getString("aliasCuenta")));
                    }
                    adapter = new ArrayAdapter<Cuenta>(this.context,R.layout.spinner_item,listaCuentas){
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
                            Cuenta cuenta = (Cuenta) parent.getSelectedItem();
                            idCuenta = cuenta.getId();
                            if(position > 0) {
                                Toast.makeText(getApplicationContext(), cuenta.toString(), Toast.LENGTH_SHORT).show();
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

}