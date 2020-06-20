package utn.frd.fvm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView btCuentas = findViewById(R.id.btCuentas);
        CardView btRealizarPago = findViewById(R.id.btRealizarPago);
        CardView btTuPerfil = findViewById(R.id.btTuPerfil);
        CardView btMovimientos = findViewById(R.id.btMovimientos);
        TextView textUsuario = findViewById(R.id.textUsuario);

        //Mostrar el nombre de usuario
        Intent i = getIntent();
        final Integer id = i.getIntExtra("EXTRA_ID_CLIENTE", 0);
        String usuario = i.getStringExtra("EXTRA_USUARIO");
        textUsuario.setText(usuario);

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

    }



}