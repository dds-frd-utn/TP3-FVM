package utn.frd.fvm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import utn.frd.fvm.R;

public class CuentasAdapter extends RecyclerView.Adapter<CuentasAdapter.MyViewHolder> {

    private List<Cuenta> listaCuentas;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView saldo, alias;

        public MyViewHolder(View view) {
            super(view);
            alias = view.findViewById(R.id.alias);
            saldo = view.findViewById(R.id.saldo);
        }
    }

    public CuentasAdapter(List<Cuenta> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cuenta_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cuenta cuenta = listaCuentas.get(position);
        holder.saldo.setText("$" + cuenta.getSaldo());
        holder.alias.setText(cuenta.getAlias());
    }

    @Override
    public int getItemCount() {
        return listaCuentas.size();
    }
}
