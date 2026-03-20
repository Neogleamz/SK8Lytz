package com.example.seedpoint.db;

import androidx.room.RoomDatabase;
import androidx.room.m;
import androidx.room.p0;
import androidx.room.u;
import com.example.seedpoint.dao.EventPODao;
import com.example.seedpoint.dao.EventPODao_Impl;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import r1.f;
import t1.b;
import t1.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class EventDatabase_Impl extends EventDatabase {
    private volatile EventPODao _eventPODao;

    @Override // androidx.room.RoomDatabase
    public void clearAllTables() {
        super.assertNotMainThread();
        b v02 = super.getOpenHelper().v0();
        try {
            super.beginTransaction();
            v02.H("DELETE FROM `event_po`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            v02.w0("PRAGMA wal_checkpoint(FULL)").close();
            if (!v02.A1()) {
                v02.H("VACUUM");
            }
        }
    }

    @Override // androidx.room.RoomDatabase
    protected u createInvalidationTracker() {
        return new u(this, new HashMap(0), new HashMap(0), "event_po");
    }

    @Override // androidx.room.RoomDatabase
    protected c createOpenHelper(m mVar) {
        return mVar.f7144a.a(c.b.a(mVar.f7145b).c(mVar.f7146c).b(new p0(mVar, new p0.a(1) { // from class: com.example.seedpoint.db.EventDatabase_Impl.1
            @Override // androidx.room.p0.a
            public void createAllTables(b bVar) {
                bVar.H("CREATE TABLE IF NOT EXISTS `event_po` (`object_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `event_log` TEXT, `create_date` INTEGER)");
                bVar.H("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
                bVar.H("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '24765b46afda9e7b625bfb88909be61c')");
            }

            @Override // androidx.room.p0.a
            public void dropAllTables(b bVar) {
                bVar.H("DROP TABLE IF EXISTS `event_po`");
                if (((RoomDatabase) EventDatabase_Impl.this).mCallbacks != null) {
                    int size = ((RoomDatabase) EventDatabase_Impl.this).mCallbacks.size();
                    for (int i8 = 0; i8 < size; i8++) {
                        ((RoomDatabase.b) ((RoomDatabase) EventDatabase_Impl.this).mCallbacks.get(i8)).b(bVar);
                    }
                }
            }

            @Override // androidx.room.p0.a
            protected void onCreate(b bVar) {
                if (((RoomDatabase) EventDatabase_Impl.this).mCallbacks != null) {
                    int size = ((RoomDatabase) EventDatabase_Impl.this).mCallbacks.size();
                    for (int i8 = 0; i8 < size; i8++) {
                        ((RoomDatabase.b) ((RoomDatabase) EventDatabase_Impl.this).mCallbacks.get(i8)).a(bVar);
                    }
                }
            }

            @Override // androidx.room.p0.a
            public void onOpen(b bVar) {
                ((RoomDatabase) EventDatabase_Impl.this).mDatabase = bVar;
                EventDatabase_Impl.this.internalInitInvalidationTracker(bVar);
                if (((RoomDatabase) EventDatabase_Impl.this).mCallbacks != null) {
                    int size = ((RoomDatabase) EventDatabase_Impl.this).mCallbacks.size();
                    for (int i8 = 0; i8 < size; i8++) {
                        ((RoomDatabase.b) ((RoomDatabase) EventDatabase_Impl.this).mCallbacks.get(i8)).c(bVar);
                    }
                }
            }

            @Override // androidx.room.p0.a
            public void onPostMigrate(b bVar) {
            }

            @Override // androidx.room.p0.a
            public void onPreMigrate(b bVar) {
                r1.c.a(bVar);
            }

            @Override // androidx.room.p0.a
            protected p0.b onValidateSchema(b bVar) {
                HashMap hashMap = new HashMap(3);
                hashMap.put("object_id", new f.a("object_id", "INTEGER", true, 1, null, 1));
                hashMap.put("event_log", new f.a("event_log", "TEXT", false, 0, null, 1));
                hashMap.put("create_date", new f.a("create_date", "INTEGER", false, 0, null, 1));
                f fVar = new f("event_po", hashMap, new HashSet(0), new HashSet(0));
                f a9 = f.a(bVar, "event_po");
                if (fVar.equals(a9)) {
                    return new p0.b(true, null);
                }
                return new p0.b(false, "event_po(com.example.seedpoint.po.EventPO).\n Expected:\n" + fVar + "\n Found:\n" + a9);
            }
        }, "24765b46afda9e7b625bfb88909be61c", "0258c8b11353abf806a8b94d743c8cfb")).a());
    }

    @Override // com.example.seedpoint.db.EventDatabase
    public EventPODao eventPODao() {
        EventPODao eventPODao;
        if (this._eventPODao != null) {
            return this._eventPODao;
        }
        synchronized (this) {
            if (this._eventPODao == null) {
                this._eventPODao = new EventPODao_Impl(this);
            }
            eventPODao = this._eventPODao;
        }
        return eventPODao;
    }

    @Override // androidx.room.RoomDatabase
    protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
        HashMap hashMap = new HashMap();
        hashMap.put(EventPODao.class, EventPODao_Impl.getRequiredConverters());
        return hashMap;
    }
}
