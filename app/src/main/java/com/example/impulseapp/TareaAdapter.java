package com.example.impulseapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private final List<Tarea> listaTareas;

    public TareaAdapter(List<Tarea> listaTareas) {
        this.listaTareas = listaTareas;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea tarea = listaTareas.get(position);
        if (tarea == null) return;

        holder.txtTitulo.setText(tarea.getTitulo());
        holder.txtDescripcion.setText(tarea.getDescripcion());
        holder.txtEstado.setText(tarea.getEstado());
        holder.txtFecha.setText("Fecha: " + (tarea.getFecha() != null ? tarea.getFecha() : "Sin fecha"));

        // Cambiar color del badge según el estado
        String estado = tarea.getEstado() != null ? tarea.getEstado() : "";

        int backgroundRes;
        int textColorRes;

        if ("Completada".equals(estado)) {
            backgroundRes = R.drawable.bg_badge_green;
            textColorRes = android.R.color.white;
        } else if ("En progreso".equals(estado)) {
            backgroundRes = R.drawable.bg_badge_yellow;
            textColorRes = android.R.color.black;
        } else {
            // Por defecto "Pendiente" o errores
            backgroundRes = R.drawable.bg_badge_red;
            textColorRes = android.R.color.white;
        }

        holder.txtEstado.setBackgroundResource(backgroundRes);
        holder.txtEstado.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), textColorRes));
    }

    @Override
    public int getItemCount() {
        return listaTareas != null ? listaTareas.size() : 0;
    }

    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDescripcion, txtEstado, txtFecha;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloTarea);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcionTarea);
            txtEstado = itemView.findViewById(R.id.txtEstadoTarea);
            txtFecha = itemView.findViewById(R.id.txtFechaTarea);
        }
    }
}