package com.example.andreacarballidop3pmdm.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.emergency.EmergencyNumber;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andreacarballidop3pmdm.R;
import com.example.andreacarballidop3pmdm.core.Entrenamiento;
import com.example.andreacarballidop3pmdm.database.EntrenamientoLab;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.Locale;

public class ActivityAddEntrenamiento extends AppCompatActivity {
    private EntrenamientoLab mEntrenamientoLab;
    Entrenamiento entrenamiento;
    String fechaFormato;
    //    Date fecha;
    String id;
    String horas;
    String minutos;
    String segundos;
    String metros;
    Entrenamiento entrenamientoModifico;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrenamiento_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEntrenamientoLab = EntrenamientoLab.get(this);
        id = getIntent().getStringExtra("Id");
        fechaFormato = getIntent().getStringExtra("FechaFormato");
//        fecha = getIntent().getStringExtra("Fecha");
        horas = getIntent().getStringExtra("Horas");
        minutos = getIntent().getStringExtra("Minutos");
        segundos = getIntent().getStringExtra("Segundos");
        metros = getIntent().getStringExtra("Metros");
//
        añadirEntrenamiento();



    }

    public void añadirEntrenamiento() {

        TextView tvFecha = findViewById(R.id.edFecha);
        EditText editTextHoras = findViewById(R.id.edHoras);
        EditText editTextMinutos = findViewById(R.id.edMinutos);
        EditText editTextSegundos = findViewById(R.id.edSegundos);
        EditText editTextMetros = findViewById(R.id.edMetros);
        ImageButton botonguardar = findViewById(R.id.boton_guardar);
        final Calendar calendar = Calendar.getInstance();

        entrenamientoModifico=mEntrenamientoLab.getEntrenamiento(id);

        if (entrenamientoModifico != null) {
            tvFecha.setText(fechaFormato);
            editTextHoras.setText(horas);
            editTextMinutos.setText(minutos);
            editTextSegundos.setText(segundos);
            editTextMetros.setText(metros);
//            calendar.setTime(fecha);
        }

        tvFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                final DatePickerDialog datePicker = new DatePickerDialog(com.example.andreacarballidop3pmdm.UI.ActivityAddEntrenamiento.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        calendar.set(Calendar.YEAR, selectedYear);
                        calendar.set(Calendar.MONTH, selectedMonth);
                        calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd MMMM 'de' yyyy", Locale.getDefault());
                        tvFecha.setText(formatoFecha.format(calendar.getTime()));

                    }
                }, year, month, day);
                datePicker.show();
            }
        });
        botonguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String h = editTextHoras.getText().toString();
                String m = editTextMinutos.getText().toString();
                String s = editTextSegundos.getText().toString();
                String d = editTextMetros.getText().toString();

                if (h.isEmpty()) {
                    h = "0";
                }

                if (m.isEmpty()) {
                    m = "0";
                }
                if (s.isEmpty()) {
                    s = "0";
                }
                if (m.isEmpty()) {
                    m = "0";

                }
                if (h.isEmpty() && m.isEmpty() && s.isEmpty() && m.isEmpty()) {
                    Toast.makeText(com.example.andreacarballidop3pmdm.UI.ActivityAddEntrenamiento.this, "No se permiten todos los campos vacíos", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (h.equals("0") && m.equals("0") && s.equals("0") && m.equals("0")) {
                    Toast.makeText(com.example.andreacarballidop3pmdm.UI.ActivityAddEntrenamiento.this, "No se permiten todos los datos a 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tvFecha == null) {

                    Toast.makeText(com.example.andreacarballidop3pmdm.UI.ActivityAddEntrenamiento.this, "El campo de la fecha está vacío", Toast.LENGTH_SHORT).show();
                    return;
                }

                int horas = Integer.parseInt(h);
                int minutos = Integer.parseInt(m);
                int segundos = Integer.parseInt(s);
                int metros = Integer.parseInt(d);

                if (horas > 24) {

                    Toast.makeText(com.example.andreacarballidop3pmdm.UI.ActivityAddEntrenamiento.this, "El dato introducido en el campo de las horas es inválido ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (minutos > 59) {

                    Toast.makeText(com.example.andreacarballidop3pmdm.UI.ActivityAddEntrenamiento.this, "El dato introducido en el campo de los minutos es inválido", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (segundos > 59) {

                    Toast.makeText(com.example.andreacarballidop3pmdm.UI.ActivityAddEntrenamiento.this, "El dato introducido en el campo de los segundos es inválido", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (metros == 0) {
                    Toast.makeText(com.example.andreacarballidop3pmdm.UI.ActivityAddEntrenamiento.this, "No se permite el campo de la distancia a 0", Toast.LENGTH_SHORT).show();
                    return;
                }

               if (entrenamientoModifico == null) {
                    entrenamiento = new Entrenamiento();
                    entrenamiento.setFecha(calendar.getTime());
                    entrenamiento.setHoras(horas);
                    entrenamiento.setMinutos(minutos);
                    entrenamiento.setSegundos(segundos);
                    entrenamiento.setMetros(metros);
                    float resultadominutosporkm = entrenamiento.calcularMinKm(horas, minutos, segundos, metros);
                    float velocidadmedia = entrenamiento.calcularVelocidadmedia(horas, minutos, segundos, metros);
                    entrenamiento.setMinutosKm(resultadominutosporkm);
                    entrenamiento.setVelocidadmediakmporhora(velocidadmedia);
                    mEntrenamientoLab.addEntrenamiento(entrenamiento);
                   Intent i = getIntent();
                   i.putExtra("id", entrenamiento.getId());
                   setResult(RESULT_OK, i);
                   finish();

                 } else {
                    float resultadominutosporkm = entrenamientoModifico.calcularMinKm(horas, minutos, segundos, metros);
                    float velocidadmedia = entrenamientoModifico.calcularVelocidadmedia(horas, minutos, segundos, metros);
                    entrenamientoModifico.modificarEntrenamiento(calendar.getTime(), horas, minutos, segundos, metros, resultadominutosporkm, velocidadmedia);
                    entrenamientoModifico.setFecha(calendar.getTime());
                    entrenamientoModifico.setHoras(horas);
                    mEntrenamientoLab.updateEntrenamiento(entrenamientoModifico);
                    Intent i = getIntent();
                    i.putExtra("idEntrenamientoModifico", entrenamientoModifico.getId());
                    setResult(RESULT_OK, i);
                    finish();

                }


            }

        });



    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}