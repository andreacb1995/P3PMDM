package com.example.andreacarballidop3pmdm.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.andreacarballidop3pmdm.core.DateConverter;
import com.example.andreacarballidop3pmdm.core.Entrenamiento;


@Database(entities = {Entrenamiento.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class EntrenamientoDataBase extends RoomDatabase {
    public abstract EntrenamientoDao getEntrenamientoDao();
}
