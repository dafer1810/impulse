package com.example.impulseapp;

import android.content.ContentValues;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class detalle_venta extends AppCompatActivity {

    private TextView txtDetalleId, txtDetalleFecha, txtDetalleTotal;
    private RecyclerView rvDetalleItems;
    private MaterialButton btnDescargarPDF, btnVolverDetalle;
    private ItemFacturaAdapter adapter;
    private List<ItemFactura> listaItems;
    private FirebaseFirestore db;
    private Venta ventaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_venta);

        db = FirebaseFirestore.getInstance();
        txtDetalleId = findViewById(R.id.txtDetalleId);
        txtDetalleFecha = findViewById(R.id.txtDetalleFecha);
        txtDetalleTotal = findViewById(R.id.txtDetalleTotal);
        rvDetalleItems = findViewById(R.id.rvDetalleItems);
        btnDescargarPDF = findViewById(R.id.btnDescargarPDF);
        btnVolverDetalle = findViewById(R.id.btnVolverDetalle);

        listaItems = new ArrayList<>();
        adapter = new ItemFacturaAdapter(listaItems, null);
        
        rvDetalleItems.setLayoutManager(new LinearLayoutManager(this));
        rvDetalleItems.setAdapter(adapter);

        String idVenta = getIntent().getStringExtra("idVenta");
        if (idVenta != null) {
            cargarDetalle(idVenta);
        }

        btnVolverDetalle.setOnClickListener(v -> finish());
        btnDescargarPDF.setOnClickListener(v -> {
            if (ventaActual != null) {
                generarPDF(ventaActual);
            } else {
                Toast.makeText(this, "Cargando datos, intente de nuevo", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarDetalle(String idVenta) {
        db.collection("ventas").document(idVenta).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                ventaActual = documentSnapshot.toObject(Venta.class);
                if (ventaActual != null) {
                    txtDetalleId.setText("Venta: #" + (ventaActual.getId() != null ? ventaActual.getId().substring(0, 8) : "---"));
                    txtDetalleFecha.setText("Fecha: " + ventaActual.getFecha());
                    txtDetalleTotal.setText(String.format(Locale.getDefault(), "$%.2f", ventaActual.getTotal()));
                    
                    if (ventaActual.getItems() != null) {
                        listaItems.clear();
                        listaItems.addAll(ventaActual.getItems());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al cargar detalles", Toast.LENGTH_SHORT).show();
        });
    }

    private void generarPDF(Venta venta) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        // Configuración de la página (A4 aprox 595 x 842)
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Título
        titlePaint.setTextSize(20f);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("FACTURA DE VENTA - IMPULSE", 50, 50, titlePaint);

        // Información de la venta
        paint.setTextSize(12f);
        canvas.drawText("ID Venta: " + venta.getId(), 50, 90, paint);
        canvas.drawText("Fecha: " + venta.getFecha(), 50, 110, paint);
        canvas.drawLine(50, 130, 545, 130, paint);

        // Encabezados de tabla
        paint.setFakeBoldText(true);
        canvas.drawText("Producto", 50, 150, paint);
        canvas.drawText("Cant.", 350, 150, paint);
        canvas.drawText("P. Unit.", 420, 150, paint);
        canvas.drawText("Subtotal", 500, 150, paint);
        paint.setFakeBoldText(false);

        int y = 180;
        if (venta.getItems() != null) {
            for (ItemFactura item : venta.getItems()) {
                canvas.drawText(item.getNombre(), 50, y, paint);
                canvas.drawText(String.valueOf(item.getCantidad()), 350, y, paint);
                canvas.drawText(String.format(Locale.getDefault(), "$%.2f", item.getPrecioUnitario()), 420, y, paint);
                canvas.drawText(String.format(Locale.getDefault(), "$%.2f", item.getSubtotal()), 500, y, paint);
                y += 20;
                
                // Si la lista es muy larga, podríamos necesitar otra página, pero para este caso simplificamos
                if (y > 750) break;
            }
        }

        canvas.drawLine(50, y + 10, 545, y + 10, paint);
        paint.setFakeBoldText(true);
        paint.setTextSize(14f);
        canvas.drawText("TOTAL:", 400, y + 40, paint);
        canvas.drawText(String.format(Locale.getDefault(), "$%.2f", venta.getTotal()), 500, y + 40, paint);

        pdfDocument.finishPage(page);

        // Guardar el archivo
        String fileName = "Factura_" + (venta.getId() != null ? venta.getId().substring(0, 8) : "Venta") + ".pdf";
        
        OutputStream fos = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
                if (uri != null) {
                    fos = getContentResolver().openOutputStream(uri);
                }
            } else {
                // Para versiones anteriores (esto requiere permisos de escritura en el manifest)
                // Implementación simplificada para modernidad
                Toast.makeText(this, "Versión de Android no soportada para descarga directa", Toast.LENGTH_SHORT).show();
                pdfDocument.close();
                return;
            }

            if (fos != null) {
                pdfDocument.writeTo(fos);
                Toast.makeText(this, "PDF guardado en Descargas", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al generar PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            pdfDocument.close();
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}