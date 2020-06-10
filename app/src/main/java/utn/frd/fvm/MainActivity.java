package utn.frd.fvm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mainToolbar);

        CardView btCuentas = findViewById(R.id.btCuentas);
        CardView btRealizarPago = findViewById(R.id.btRealizarPago);
        CardView btTuPerfil = findViewById(R.id.btTuPerfil);
        CardView btMovimientos = findViewById(R.id.btMovimientos);


        btRealizarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),RealizarPago.class);
                v.getContext().startActivity(i);
            }
         });
        btCuentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Cuentas.class);
                v.getContext().startActivity(i);
            }
        });
        btMovimientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),Movimientos.class);
                v.getContext().startActivity(i);
            }
        });
        btTuPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),TuPerfil.class);
                v.getContext().startActivity(i);
            }
        });
    }
}