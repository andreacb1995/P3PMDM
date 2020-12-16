package com.example.andreacarballidop3pmdm.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andreacarballidop3pmdm.R;
import com.example.andreacarballidop3pmdm.core.Entrenamiento;

import java.util.ArrayList;

public class ActivityEstadisticas extends AppCompatActivity {
    private static final String ESTADISTICAS = "estadisticas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estadisticas_generales);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String tiempoTotal = getIntent().getStringExtra("tiempoTotal");
        String distanciaTotal = getIntent().getStringExtra("distanciaTotal");
        String  mediaKm = getIntent().getStringExtra("mediakm");
        String  velocidadMedia = getIntent().getStringExtra("velocidadMedia");

        TextView tvTiempoTotal = findViewById(R.id.tv_mostarTiempoTotal);
        TextView tvDistanciaTotal = findViewById(R.id.tv_mostrarkm);
        TextView tvMediaMinutosKm = findViewById(R.id.tv_mostrarmediakm);
        TextView tvVelocidadMedia= findViewById(R.id.tv_mostrarmediakm);

        tvTiempoTotal.setText(tiempoTotal);
        tvDistanciaTotal.setText(distanciaTotal);
        tvMediaMinutosKm.setText(mediaKm);
        tvVelocidadMedia.setText(velocidadMedia);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

}