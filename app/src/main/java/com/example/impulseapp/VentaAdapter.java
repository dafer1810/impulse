package com.example.impulseapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;
import java.util.Locale;

public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.ViewHolder> {

    private List<Venta> listaVentas;
    private Context context;

    public VentaAdapter(List<Venta> listaVentas) {
        this.listaVentas = listaVentas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_venta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Venta venta = listaVentas.get(position);
        
        holder.txtFecha.setText(venta.getFecha());
        holder.txtTotal.setText(String.format(Locale.getDefault(), "$%.2f", venta.getTotal()));
        holder.txtId.setText("ID: #" + (venta.getId() != null ? venta.getId().substring(0, 8) : "---"));
        
        int numItems = venta.getItems() != null ? venta.getItems().size() : 0;
        holder.txtCantidad.setText(numItems + (numItems == 1 ? " producto" : " productos"));

        holder.btnVerDetalle.setOnClickListener(v -> {
            Intent intent = new Intent(context, detalle_venta.class);
            intent.putExtra("idVenta", venta.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaVentas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFecha, txtTotal, txtId, txtCantidad;
        MaterialButton btnVerDetalle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFecha = itemView.findViewById(R.id.txtFechaVenta);
            txtTotal = itemView.findViewById(R.id.txtTotalVenta);
            txtId = itemView.findViewById(R.id.txtIdVenta);
            txtCantidad = itemView.findViewById(R.id.txtCantidadItems);
            btnVerDetalle = itemView.findViewById(R.id.btnVerDetalleVenta);
        }
    }
}