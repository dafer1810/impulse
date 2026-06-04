package com.example.impulseapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class ItemFacturaAdapter extends RecyclerView.Adapter<ItemFacturaAdapter.ViewHolder> {

    private List<ItemFactura> items;
    private OnItemRemoveListener listener;

    public interface OnItemRemoveListener {
        void onRemove(int position);
    }

    public ItemFacturaAdapter(List<ItemFactura> items, OnItemRemoveListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_factura_linea, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemFactura item = items.get(position);
        holder.txtNombre.setText(item.getNombre());
        holder.txtDetalle.setText(String.format(Locale.getDefault(), "%d x $%.2f", item.getCantidad(), item.getPrecioUnitario()));
        holder.txtSubtotal.setText(String.format(Locale.getDefault(), "$%.2f", item.getSubtotal()));
        
        holder.btnRemove.setOnClickListener(v -> {
            if (listener != null) listener.onRemove(position);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDetalle, txtSubtotal;
        ImageButton btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreItem);
            txtDetalle = itemView.findViewById(R.id.txtDetalleItem);
            txtSubtotal = itemView.findViewById(R.id.txtSubtotalItem);
            btnRemove = itemView.findViewById(R.id.btnRemoveItem);
        }
    }
}