package com.example.andreacarballidop3pmdm.UI;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andreacarballidop3pmdm.R;
import com.example.andreacarballidop3pmdm.core.Entrenamiento;
import com.example.andreacarballidop3pmdm.database.EntrenamientoLab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AccionesEntrenamiento {
    protected static final int CODIGO_EDICION_ITEM = 100;
    final int ENTRENAMIENTO = 0;
    TextView tvFecha;
    Entrenamiento mEntrenamiento;
    Entrenamiento e;
    MyRecyclerViewAdapter adapter;

    private ArrayList<Entrenamiento> listaEntrenamientos;
    private ArrayList<Entrenamiento> entrenamientos;
    private Entrenamiento entrenamientoM;

    private EntrenamientoLab mEntrenamientoLab;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    EditText edNombre;
    EditText edEdad;
    EditText edAltura;
    EditText edPeso;
    ImageButton botonGuardar;
    ImageButton botonBorrar;

    public static final String Nombre = "nombre";
    public static final String Edad = "edad";
    public static final String Altura = "altura";
    public static final String Peso = "peso";
    final Calendar calendar = Calendar.getInstance();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btAdd = findViewById(R.id.btAdd);

        listaEntrenamientos = new ArrayList<>();
        entrenamientos = new ArrayList<>();

        mEntrenamientoLab = EntrenamientoLab.get(this);
        entrenamientos = mEntrenamientoLab.getEntrenamientos();
        for (int i = 0; i < entrenamientos.size(); i++) {
            listaEntrenamientos.add(entrenamientos.get(i));
        }

        RecyclerView recyclerView = findViewById(R.id.rvEntrenamientos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(listaEntrenamientos, this);

        recyclerView.setAdapter(adapter);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.example.andreacarballidop3pmdm.UI.MainActivity.this.onAdd(null);
            }
        });
        edNombre = (EditText) findViewById(R.id.edNombreUsuario);
        edEdad = (EditText) findViewById(R.id.edEdadUsuario);
        edAltura = (EditText) findViewById(R.id.edAlturaUsuario);
        edPeso = (EditText) findViewById(R.id.edPesoUsuario);

    }

    public void guardar_datos() {

        String n = edNombre.getText().toString();
        String e = edEdad.getText().toString();
        String a = edAltura.getText().toString();
        String p = edPeso.getText().toString();

        editor = sharedpreferences.edit();
        editor.putString(Nombre, n);
        editor.putString(Edad, e);
        editor.putString(Altura, a);
        editor.putString(Peso, p);
        editor.commit();
    }

    public void mostrar_datos() {
        edNombre = (EditText) findViewById(R.id.edNombreUsuario);
        edEdad = (EditText) findViewById(R.id.edEdadUsuario);
        edAltura = (EditText) findViewById(R.id.edAlturaUsuario);
        edPeso = (EditText) findViewById(R.id.edPesoUsuario);

        sharedpreferences = getSharedPreferences("info", Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Nombre)) {
            edNombre.setText(sharedpreferences.getString(Nombre, ""));
        }
        if (sharedpreferences.contains(Edad)) {
            edEdad.setText(sharedpreferences.getString(Edad, ""));
        }
        if (sharedpreferences.contains(Altura)) {
            edAltura.setText(sharedpreferences.getString(Altura, ""));
        }
        if (sharedpreferences.contains(Peso)) {
            edPeso.setText(sharedpreferences.getString(Peso, ""));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        guardar_datos();
    }


    @Override
    public void onResume() {
        super.onResume();
        mostrar_datos();

        File internalStorageDir = getFilesDir();
        File estadisticas = new File(internalStorageDir, "EstadisticasGenerales.txt");
        String textToWrite = guardar_EstadisticasGenerales();
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(estadisticas);
            fos.write(textToWrite.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void onAdd(Entrenamiento entrenamientoModifico) {
        Intent intent = new Intent(MainActivity.this, ActivityAddEntrenamiento.class);
        if (entrenamientoModifico != null) {
            intent.putExtra("Id", entrenamientoModifico.getId());
            intent.putExtra("FechaFormato", entrenamientoModifico.getFormatoFecha());
//            intent.putExtra("Fecha", entrenamientoModifico.getFecha());
            intent.putExtra("Horas", String.valueOf(entrenamientoModifico.getHoras()));
            intent.putExtra("Minutos", String.valueOf(entrenamientoModifico.getMinutos()));
            intent.putExtra("Segundos", String.valueOf(entrenamientoModifico.getSegundos()));
            intent.putExtra("Metros", String.valueOf(entrenamientoModifico.getMetros()));

        }
        startActivityForResult(intent, ENTRENAMIENTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ENTRENAMIENTO) {
            if (resultCode == RESULT_OK) {

                String returnid = data.getStringExtra("id");
//                String returnidEntrenamientoModifico = data.getStringExtra("idEntrenamientoModifico");

                if(!returnid.equals(" ")){
                    Entrenamiento entrenamiento;
                    entrenamiento = mEntrenamientoLab.getEntrenamiento(returnid);
                    listaEntrenamientos.add(entrenamiento);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(com.example.andreacarballidop3pmdm.UI.MainActivity.this, "Entrenamiento creado", Toast.LENGTH_SHORT).show();
                }
//                if (!returnidEntrenamientoModifico.equals(" ")) {
//                    Entrenamiento entrenamientoM = mEntrenamientoLab.getEntrenamiento(returnidEntrenamientoModifico);
//                    mEntrenamientoLab.updateEntrenamiento(entrenamientoM);
//                    adapter.notifyDataSetChanged();
//
//                    Toast.makeText(com.example.andreacarballidop3pmdm.UI.MainActivity.this, "Entrenamiento modificado", Toast.LENGTH_SHORT).show();
//                }


            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        this.getMenuInflater().inflate(R.menu.actions_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        boolean toret = false;

        switch (menuItem.getItemId()) {
            case R.id.añadirEntrenamiento:
                onAdd(null);
                toret = true;
                break;
            case R.id.modificar:
                modificar();
                toret = true;
                break;
            case R.id.eliminar:
                eliminar();
                toret = true;
                break;
            case R.id.estadisticase:
                activityEstadisticas();
                toret = true;
                break;
        }
        return toret;
    }

    private void modificar() {

        AlertDialog.Builder builder = new AlertDialog.Builder(com.example.andreacarballidop3pmdm.UI.MainActivity.this);
        builder.setTitle("Modificar entrenamiento");

        final String[] arrayEntrenamientos = new String[listaEntrenamientos.size()];
        for (int i = 0; i < listaEntrenamientos.size(); i++) {
            arrayEntrenamientos[i] = listaEntrenamientos.get(i).getFormatoFecha() + ", " + listaEntrenamientos.get(i).getTiempo() + "," + listaEntrenamientos.get(i).getm();
        }

        builder.setSingleChoiceItems(arrayEntrenamientos, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {
                entrenamientoM = listaEntrenamientos.get(i);
            }
        });
        builder.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int i) {
                if (entrenamientoM == null) {
                    Toast.makeText(com.example.andreacarballidop3pmdm.UI.MainActivity.this, "No ha seleccionado un entrenamiento", Toast.LENGTH_SHORT).show();
                } else {
                    onAdd(entrenamientoM);
                }
            }

        });
        builder.setNegativeButton("Cancelar", null);
        builder.create().show();

    }

    public void eliminar() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar entrenamientos");

        String[] stringEntrenamientos = new String[listaEntrenamientos.size()];
        final boolean[] entrenamientoseleccion = new boolean[listaEntrenamientos.size()];
        for (int i = 0; i < listaEntrenamientos.size(); i++) {
            stringEntrenamientos[i] = listaEntrenamientos.get(i).getFormatoFecha() + ", " + listaEntrenamientos.get(i).getTiempo() + ", " + listaEntrenamientos.get(i).getm();
        }
        builder.setMultiChoiceItems(stringEntrenamientos, entrenamientoseleccion, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i, boolean isChecked) {
                entrenamientoseleccion[i] = isChecked;
            }
        });
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                AlertDialog.Builder buildereliminar = new AlertDialog.Builder(com.example.andreacarballidop3pmdm.UI.MainActivity.this);
                buildereliminar.setMessage("¿Está seguro de que desea eliminar los elementos?");
                buildereliminar.setNegativeButton("No", null);
                buildereliminar.setPositiveButton("Sí", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < listaEntrenamientos.size(); i++)
                            if (entrenamientoseleccion[i])
                                EntrenamientoLab.deleteEntrenamiento(listaEntrenamientos.get(i));
                        adapter.notifyDataSetChanged();

                        for (int i = listaEntrenamientos.size() - 1; i >= 0; i--) {
                            if (entrenamientoseleccion[i]) {
                                listaEntrenamientos.remove(i);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        Toast.makeText(com.example.andreacarballidop3pmdm.UI.MainActivity.this, "Entrenamientos eliminados correctamente", Toast.LENGTH_SHORT).show();
                        com.example.andreacarballidop3pmdm.UI.MainActivity.this.adapter.notifyDataSetChanged();
                    }
                });
                buildereliminar.create().show();
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }


    public String guardar_EstadisticasGenerales() {
        float kmTotales = 0;
        float mediaMinutosKm = 0;
        float minPorKm = 0;
        for (int i = 0; i < listaEntrenamientos.size(); i++) {
            float metrosEntrenamiento = listaEntrenamientos.get(i).getMetros();
            float conversion = metrosEntrenamiento / 1000;
            kmTotales = kmTotales + conversion;
            minPorKm = listaEntrenamientos.get(i).getMinutosKm();
            mediaMinutosKm = (mediaMinutosKm + minPorKm) / listaEntrenamientos.size();

        }
        String texto = "Estadísticas generales\n" +
                "Kilómetros totales nadados: " + kmTotales + " km\n" + "Minutos por kilómetro de media: " +
                mediaMinutosKm + " km/m";
        return texto;

    }

    @Override
    public void mostrar(Entrenamiento entrenamiento) {

        final TextView textview = new TextView(com.example.andreacarballidop3pmdm.UI.MainActivity.this);
        textview.setText(entrenamiento.toString());

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(com.example.andreacarballidop3pmdm.UI.MainActivity.this);
        builder.setTitle("           Entrenamiento ");
        builder.setView(textview);
        builder.setNegativeButton("Volver", null);
        builder.create().show();
    }

    @Override
    public void eliminar(Entrenamiento entrenamiento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(com.example.andreacarballidop3pmdm.UI.MainActivity.this);
        builder.setTitle("Borrar Elemento");
        builder.setMessage("Está seguro de que desea eliminar este elemento?\n\n" + entrenamiento.eliminarEntrenamiento());
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listaEntrenamientos.remove(entrenamiento);
                mEntrenamientoLab.deleteEntrenamiento(entrenamiento);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }

    @Override
    public void modificar(Entrenamiento entrenamiento) {
        onAdd(entrenamiento);
    }


    public void activityEstadisticas() {

        int horasTotal = 0;
        int minutosTotal = 0;
        int segundosTotal = 0;
        float kmTotales = 0;
        float mediaMinutosKm = 0;
        float velocidadMediaGeneral = 0;


        for (Entrenamiento entrenamiento : listaEntrenamientos) {
            int horasEntrenamiento = entrenamiento.getHoras();
            horasTotal += horasEntrenamiento;
            int minutosEntrenamiento = entrenamiento.getMinutos();
            minutosTotal += minutosEntrenamiento;
            int segundosEntrenamiento = entrenamiento.getSegundos();
            segundosTotal += segundosEntrenamiento;

            float metrosEntrenamiento = entrenamiento.getMetros();
            float conversion = metrosEntrenamiento / 1000;
            kmTotales = kmTotales + conversion;

            float minPorKm = entrenamiento.getMinutosKm();
            mediaMinutosKm += minPorKm;

            float velocidadMedia = entrenamiento.getVelocidadmediakmporhora();
            velocidadMediaGeneral += velocidadMedia;

        }
        float mK = mediaMinutosKm / listaEntrenamientos.size();
        float vT = velocidadMediaGeneral / listaEntrenamientos.size();


        Intent intent = new Intent(MainActivity.this, ActivityEstadisticas.class);
        intent.putExtra("tiempoTotal", String.valueOf(horasTotal) + " h " + String.valueOf(minutosTotal) + " m " + String.valueOf(segundosTotal) + " s");
        intent.putExtra("distanciaTotal", String.valueOf(kmTotales) + " km");
        intent.putExtra("mediakm", String.valueOf(mK) + " m/h");
        intent.putExtra("velocidadMedia", String.valueOf(vT) + " km/h");
        startActivity(intent);
    }

    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;

    @Override
    public void onBackPressed() {

        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }
}
