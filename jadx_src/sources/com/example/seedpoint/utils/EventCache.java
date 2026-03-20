package com.example.seedpoint.utils;

import android.content.Context;
import android.util.Log;
import androidx.room.l0;
import com.example.seedpoint.dao.EventPODao;
import com.example.seedpoint.db.EventDatabase;
import com.example.seedpoint.event.EventData;
import com.example.seedpoint.po.EventPO;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.gson.e;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class EventCache {
    private static final String TAG = "com.example.seedpoint.utils.EventCache";
    private final int BUFFER_SIZE;
    private final EventDatabase db;
    private final EventPODao eventPODao;
    private final ExecutorService executor;
    private final ThreadPoolExecutor executorThread;
    private final n.a<List<EventData>, Boolean> flushListener;
    private final e gson;
    private final ExecutorService reporter;
    private final boolean startReport;
    private final AtomicLong bufferSize = new AtomicLong(0);
    private boolean flush = false;

    public EventCache(Context context, int i8, n.a<List<EventData>, Boolean> aVar) {
        EventDatabase eventDatabase = (EventDatabase) l0.a(context, EventDatabase.class, "event_database").a();
        this.db = eventDatabase;
        this.eventPODao = eventDatabase.eventPODao();
        this.BUFFER_SIZE = i8;
        this.flushListener = aVar;
        this.gson = new e();
        this.startReport = true;
        this.executorThread = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue((int) RecognitionOptions.UPC_E));
        this.executor = Executors.newSingleThreadExecutor();
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        this.reporter = newSingleThreadExecutor;
        newSingleThreadExecutor.execute(new Runnable() { // from class: com.example.seedpoint.utils.a
            @Override // java.lang.Runnable
            public final void run() {
                EventCache.this.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$flush$1(List list, List list2) {
        if (this.flushListener.apply(list).booleanValue()) {
            return;
        }
        this.eventPODao.batchInsert(list2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        while (this.startReport) {
            try {
                Thread.sleep(2000L);
                flush();
            } catch (InterruptedException e8) {
                Log.e(TAG, e8.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$put$2(EventData eventData) {
        synchronized (this) {
            String u8 = this.gson.u(eventData);
            this.eventPODao.insert(new EventPO(u8));
            long incrementAndGet = this.bufferSize.incrementAndGet();
            String str = TAG;
            Log.i(str, " put  size " + incrementAndGet + " " + u8);
            if (incrementAndGet >= this.BUFFER_SIZE) {
                flush();
            }
        }
    }

    public void flush() {
        synchronized (this) {
            if (this.flushListener != null && this.flush) {
                final List<EventPO> findEventPOs = this.eventPODao.findEventPOs();
                final ArrayList arrayList = new ArrayList(findEventPOs.size());
                for (EventPO eventPO : findEventPOs) {
                    arrayList.add((EventData) this.gson.l(eventPO.getEventLog(), EventData.class));
                }
                if (arrayList.size() > 0) {
                    this.eventPODao.deleteAll();
                    this.executorThread.execute(new Runnable() { // from class: com.example.seedpoint.utils.c
                        @Override // java.lang.Runnable
                        public final void run() {
                            EventCache.this.lambda$flush$1(arrayList, findEventPOs);
                        }
                    });
                    this.bufferSize.set(0L);
                }
            }
        }
    }

    public void put(final EventData eventData) {
        this.executor.execute(new Runnable() { // from class: com.example.seedpoint.utils.b
            @Override // java.lang.Runnable
            public final void run() {
                EventCache.this.lambda$put$2(eventData);
            }
        });
    }

    public void startFlush() {
        synchronized (this) {
            this.flush = true;
            flush();
        }
    }

    public void stopFlush() {
        synchronized (this) {
            this.flush = false;
        }
    }
}
