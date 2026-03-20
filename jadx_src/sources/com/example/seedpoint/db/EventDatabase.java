package com.example.seedpoint.db;

import androidx.room.RoomDatabase;
import com.example.seedpoint.dao.EventPODao;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class EventDatabase extends RoomDatabase {
    public abstract EventPODao eventPODao();
}
