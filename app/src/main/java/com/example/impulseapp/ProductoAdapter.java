package com.example.impulseapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private final List<Producto> listaProductos;

    public ProductoAdapter(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        if (producto == null) return;

        holder.txtNombre.setText(producto.getNombre() != null ? producto.getNombre() : "Sin nombre");
        
        String codigo = producto.getCodigo() != null ? producto.getCodigo() : "---";
        String categoria = producto.getCategoria() != null ? producto.getCategoria() : "General";
        holder.txtDetalle.setText(String.format("Código: %s | Cat: %s", codigo, categoria));
        
        holder.txtPrecio.setText(String.format(Locale.getDefault(), "$%.2f", producto.getPrecioVenta()));
        holder.txtStock.setText(String.format(Locale.getDefault(), "Stock: %d", producto.getStock()));
    }

    @Override
    public int getItemCount() {
        return listaProductos != null ? listaProductos.size() : 0;
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDetalle, txtPrecio, txtStock;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreProducto);
            txtDetalle = itemView.findViewById(R.id.txtDetalleProducto);
            txtPrecio = itemView.findViewById(R.id.txtPrecioProducto);
            txtStock = itemView.findViewById(R.id.txtStockProducto);
        }
    }
}