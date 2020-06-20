package utn.frd.fvm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import utn.frd.fvm.R;

public class MovimientosAdapter extends RecyclerView.Adapter<MovimientosAdapter.MyViewHolder> {

    private List<Movimiento> listaMovimientos;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView msg, monto, fecha;

        public MyViewHolder(View view) {
            super(view);
            fecha = view.findViewById(R.id.fecha);
            msg = view.findViewById(R.id.msg);
            monto = view.findViewById(R.id.monto);
        }
    }

    public MovimientosAdapter(List<Movimiento> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movimiento, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movimiento mov = listaMovimientos.get(position);
        holder.monto.setText("$" + mov.getMonto());
        holder.msg.setText(mov.getMsg());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String format = formatter.format(mov.getFecha());
        holder.fecha.setText(format);
    }

    public void clear() {
        int size = listaMovimientos.size();
        listaMovimientos.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemCount() {
        return listaMovimientos.size();
    }
}
