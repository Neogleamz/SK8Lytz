package com.example.seedpoint.dao;

import android.database.Cursor;
import androidx.room.RoomDatabase;
import androidx.room.o;
import androidx.room.p;
import androidx.room.q0;
import androidx.room.t0;
import com.example.seedpoint.po.Converters;
import com.example.seedpoint.po.EventPO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import r1.b;
import r1.c;
import t1.f;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class EventPODao_Impl implements EventPODao {
    private final RoomDatabase __db;
    private final o<EventPO> __deletionAdapterOfEventPO;
    private final p<EventPO> __insertionAdapterOfEventPO;
    private final t0 __preparedStmtOfDeleteAll;

    public EventPODao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfEventPO = new p<EventPO>(roomDatabase) { // from class: com.example.seedpoint.dao.EventPODao_Impl.1
            @Override // androidx.room.p
            public void bind(f fVar, EventPO eventPO) {
                fVar.h0(1, eventPO.getObjectId());
                String str = eventPO.eventLog;
                if (str == null) {
                    fVar.o1(2);
                } else {
                    fVar.I(2, str);
                }
                Long dateToTimestamp = Converters.dateToTimestamp(eventPO.createDate);
                if (dateToTimestamp == null) {
                    fVar.o1(3);
                } else {
                    fVar.h0(3, dateToTimestamp.longValue());
                }
            }

            @Override // androidx.room.t0
            public String createQuery() {
                return "INSERT OR REPLACE INTO `event_po` (`object_id`,`event_log`,`create_date`) VALUES (nullif(?, 0),?,?)";
            }
        };
        this.__deletionAdapterOfEventPO = new o<EventPO>(roomDatabase) { // from class: com.example.seedpoint.dao.EventPODao_Impl.2
            @Override // androidx.room.o
            public void bind(f fVar, EventPO eventPO) {
                fVar.h0(1, eventPO.getObjectId());
            }

            @Override // androidx.room.o, androidx.room.t0
            public String createQuery() {
                return "DELETE FROM `event_po` WHERE `object_id` = ?";
            }
        };
        this.__preparedStmtOfDeleteAll = new t0(roomDatabase) { // from class: com.example.seedpoint.dao.EventPODao_Impl.3
            @Override // androidx.room.t0
            public String createQuery() {
                return "DELETE FROM event_po";
            }
        };
    }

    public static List<Class<?>> getRequiredConverters() {
        return Collections.emptyList();
    }

    @Override // com.example.seedpoint.dao.EventPODao
    public void batchInsert(List<EventPO> list) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfEventPO.insert(list);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.example.seedpoint.dao.EventPODao
    public void delete(EventPO eventPO) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfEventPO.handle(eventPO);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.example.seedpoint.dao.EventPODao
    public void deleteAll() {
        this.__db.assertNotSuspendingTransaction();
        f acquire = this.__preparedStmtOfDeleteAll.acquire();
        this.__db.beginTransaction();
        try {
            acquire.N();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteAll.release(acquire);
        }
    }

    @Override // com.example.seedpoint.dao.EventPODao
    public List<EventPO> findEventPOs() {
        q0 c9 = q0.c("SELECT * FROM event_po", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor b9 = c.b(this.__db, c9, false, null);
        try {
            int e8 = b.e(b9, "object_id");
            int e9 = b.e(b9, "event_log");
            int e10 = b.e(b9, "create_date");
            ArrayList arrayList = new ArrayList(b9.getCount());
            while (b9.moveToNext()) {
                EventPO eventPO = new EventPO(b9.isNull(e9) ? null : b9.getString(e9));
                eventPO.setObjectId(b9.getLong(e8));
                eventPO.createDate = Converters.fromTimestamp(b9.isNull(e10) ? null : Long.valueOf(b9.getLong(e10)));
                arrayList.add(eventPO);
            }
            return arrayList;
        } finally {
            b9.close();
            c9.h();
        }
    }

    @Override // com.example.seedpoint.dao.EventPODao
    public void insert(EventPO eventPO) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfEventPO.insert((p<EventPO>) eventPO);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }
}
