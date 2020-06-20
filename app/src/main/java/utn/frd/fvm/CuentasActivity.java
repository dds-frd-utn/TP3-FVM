package utn.frd.fvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utn.frd.fvm.adapters.Cuenta;
import utn.frd.fvm.adapters.CuentasAdapter;
import utn.frd.fvm.adapters.Movimiento;
import utn.frd.fvm.adapters.MovimientosAdapter;

public class CuentasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CuentasAdapter mAdapter;
    private List<Cuenta> listaCuentas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuentas);

        //Obtengo el id del cliente
        final Integer idCliente = getIntent().getIntExtra("ID",0);

        Toolbar mainToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerCuentas);
        mAdapter = new CuentasAdapter(listaCuentas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        new GetCuentasCliente(idCliente, this).execute();
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
                Log.d("CUENTAS", result);
                JSONArray cuentas = new JSONArray(result);
                for(int i=0; i < cuentas.length(); i++) {
                    Log.d("CONTADOR", ""+i);
                    JSONObject cuenta = cuentas.getJSONObject(i);
                    //Cuenta(saldo, alias)
                    listaCuentas.add(new Cuenta(cuenta.getString("aliasCuenta"),cuenta.getInt("saldo")));
                }
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}