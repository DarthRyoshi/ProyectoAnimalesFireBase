package com.example.proyectofirebase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private EditText txtCodigo, txtNombre, txtDueño, txtDireccion;
    private ListView lista;
    private Button btnEnviar, btnCargar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();

        txtCodigo = findViewById(R.id.txtCodigo);
        txtNombre = findViewById(R.id.txtNombre);
        txtDueño = findViewById(R.id.txtDueño);
        txtDireccion = findViewById(R.id.txtDireccion);
        lista = findViewById(R.id.Lista);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnCargar = findViewById(R.id.btnCargar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatosFirestore();
            }
        });

        btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDatos();
            }
        });
    }

    private void enviarDatosFirestore() {
        String codigo = txtCodigo.getText().toString();
        String nombre = txtNombre.getText().toString();
        String dueño = txtDueño.getText().toString();
        String direccion = txtDireccion.getText().toString();

        if (codigo.isEmpty() || nombre.isEmpty() || dueño.isEmpty() || direccion.isEmpty()) {
            showToast("Por favor complete todos los campos.");
            return;
        }

        // Crear una instancia de PetInfo
        PetInfo petInfo = new PetInfo(codigo, nombre, dueño, direccion, "");

        firestore.collection("mascotas")
                .document(codigo)
                .set(petInfo)
                .addOnSuccessListener(aVoid -> showToast("Datos enviados correctamente"))
                .addOnFailureListener(e -> showToast("Error al enviar los datos: " + e.getMessage()));
    }

    private void cargarDatos() {
        firestore.collection("mascotas")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_list_item_1);
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String nombreMascota = document.getString("nombre");
                            if (nombreMascota != null) {
                                adapter.add(nombreMascota);
                            }
                        }
                        lista.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(e -> showToast("Error al cargar los datos"));
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
class PetInfo {
    private String codigo;
    private String nombre;
    private String dueño;
    private String direccion;
    private String mascota;

    // Constructor vacío requerido para Firestore
    public PetInfo() { }

    public PetInfo(String codigo, String nombre, String dueño, String direccion, String mascota) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.dueño = dueño;
        this.direccion = direccion;
        this.mascota = mascota;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDueño() { return dueño; }
    public void setDueño(String dueño) { this.dueño = dueño; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getMascota() { return mascota; }
    public void setMascota(String mascota) { this.mascota = mascota; }
}