package com.example.impulseapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class InventarioAdapter extends RecyclerView.Adapter<InventarioAdapter.ViewHolder> {

    private List<Producto> productos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Producto producto);
    }

    public InventarioAdapter(List<Producto> productos, OnItemClickListener listener) {
        this.productos = productos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.txtNombre.setText(producto.getNombre());
        holder.txtCategoria.setText(producto.getCategoria());
        holder.txtStock.setText("Stock: " + producto.getStock());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(producto));
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtCategoria, txtStock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreProducto);
            txtCategoria = itemView.findViewById(R.id.txtCategoriaProducto);
            txtStock = itemView.findViewById(R.id.txtStockProducto);
        }
    }
}