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

        //private Button btTuPerfil;
        //private Button btMovimientos;
        //private Button btRealizarPago;
        CardView btCuentas = findViewById(R.id.btCuentas);
        //btTuPerfil = findViewById(R.id.btTuPerfil);
        //btMovimientos = findViewById(R.id.btMovimientos);
        //btRealizarPago = findViewById(R.id.btRealizarPago);

        btCuentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Cuentas.class);
                v.getContext().startActivity(i);
            }
        });
    }
}