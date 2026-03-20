package com.google.android.gms.cloudmessaging;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m implements ServiceConnection {

    /* renamed from: c  reason: collision with root package name */
    n f11503c;

    /* renamed from: f  reason: collision with root package name */
    final /* synthetic */ r f11506f;

    /* renamed from: a  reason: collision with root package name */
    int f11501a = 0;

    /* renamed from: b  reason: collision with root package name */
    final Messenger f11502b = new Messenger(new b7.f(Looper.getMainLooper(), new Handler.Callback() { // from class: com.google.android.gms.cloudmessaging.k
        @Override // android.os.Handler.Callback
        public final boolean handleMessage(Message message) {
            int i8 = message.arg1;
            if (Log.isLoggable("MessengerIpcClient", 3)) {
                Log.d("MessengerIpcClient", "Received response to request: " + i8);
            }
            m mVar = m.this;
            synchronized (mVar) {
                p pVar = (p) mVar.f11505e.get(i8);
                if (pVar == null) {
                    Log.w("MessengerIpcClient", "Received response for unknown request: " + i8);
                    return true;
                }
                mVar.f11505e.remove(i8);
                mVar.f();
                Bundle data = message.getData();
                if (data.getBoolean("unsupported", false)) {
                    pVar.c(new zzs(4, "Not supported by GmsCore", null));
                    return true;
                }
                pVar.a(data);
                return true;
            }
        }
    }));

    /* renamed from: d  reason: collision with root package name */
    final Queue f11504d = new ArrayDeque();

    /* renamed from: e  reason: collision with root package name */
    final SparseArray f11505e = new SparseArray();

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ m(r rVar, i6.e eVar) {
        this.f11506f = rVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void a(int i8, String str) {
        b(i8, str, null);
    }

    final synchronized void b(int i8, String str, Throwable th) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            Log.d("MessengerIpcClient", "Disconnected: ".concat(String.valueOf(str)));
        }
        int i9 = this.f11501a;
        if (i9 == 0) {
            throw new IllegalStateException();
        }
        if (i9 != 1 && i9 != 2) {
            if (i9 != 3) {
                return;
            }
            this.f11501a = 4;
            return;
        }
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Unbinding service");
        }
        this.f11501a = 4;
        t6.a.b().c(r.a(this.f11506f), this);
        zzs zzsVar = new zzs(i8, str, th);
        for (p pVar : this.f11504d) {
            pVar.c(zzsVar);
        }
        this.f11504d.clear();
        for (int i10 = 0; i10 < this.f11505e.size(); i10++) {
            ((p) this.f11505e.valueAt(i10)).c(zzsVar);
        }
        this.f11505e.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void c() {
        r.e(this.f11506f).execute(new Runnable() { // from class: com.google.android.gms.cloudmessaging.h
            @Override // java.lang.Runnable
            public final void run() {
                final p pVar;
                while (true) {
                    final m mVar = m.this;
                    synchronized (mVar) {
                        if (mVar.f11501a != 2) {
                            return;
                        }
                        if (mVar.f11504d.isEmpty()) {
                            mVar.f();
                            return;
                        }
                        pVar = (p) mVar.f11504d.poll();
                        mVar.f11505e.put(pVar.f11509a, pVar);
                        r.e(mVar.f11506f).schedule(new Runnable() { // from class: com.google.android.gms.cloudmessaging.l
                            @Override // java.lang.Runnable
                            public final void run() {
                                m.this.e(pVar.f11509a);
                            }
                        }, 30L, TimeUnit.SECONDS);
                    }
                    if (Log.isLoggable("MessengerIpcClient", 3)) {
                        Log.d("MessengerIpcClient", "Sending ".concat(String.valueOf(pVar)));
                    }
                    r rVar = mVar.f11506f;
                    Messenger messenger = mVar.f11502b;
                    int i8 = pVar.f11511c;
                    Context a9 = r.a(rVar);
                    Message obtain = Message.obtain();
                    obtain.what = i8;
                    obtain.arg1 = pVar.f11509a;
                    obtain.replyTo = messenger;
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("oneWay", pVar.b());
                    bundle.putString("pkg", a9.getPackageName());
                    bundle.putBundle("data", pVar.f11512d);
                    obtain.setData(bundle);
                    try {
                        mVar.f11503c.a(obtain);
                    } catch (RemoteException e8) {
                        mVar.a(2, e8.getMessage());
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void d() {
        if (this.f11501a == 1) {
            a(1, "Timed out while binding");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void e(int i8) {
        p pVar = (p) this.f11505e.get(i8);
        if (pVar != null) {
            Log.w("MessengerIpcClient", "Timing out request: " + i8);
            this.f11505e.remove(i8);
            pVar.c(new zzs(3, "Timed out waiting for response", null));
            f();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void f() {
        if (this.f11501a == 2 && this.f11504d.isEmpty() && this.f11505e.size() == 0) {
            if (Log.isLoggable("MessengerIpcClient", 2)) {
                Log.v("MessengerIpcClient", "Finished handling requests, unbinding");
            }
            this.f11501a = 3;
            t6.a.b().c(r.a(this.f11506f), this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized boolean g(p pVar) {
        int i8 = this.f11501a;
        if (i8 != 0) {
            if (i8 == 1) {
                this.f11504d.add(pVar);
                return true;
            } else if (i8 != 2) {
                return false;
            } else {
                this.f11504d.add(pVar);
                c();
                return true;
            }
        }
        this.f11504d.add(pVar);
        n6.j.p(this.f11501a == 0);
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Starting bind to GmsCore");
        }
        this.f11501a = 1;
        Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
        intent.setPackage("com.google.android.gms");
        try {
            if (t6.a.b().a(r.a(this.f11506f), intent, this, 1)) {
                r.e(this.f11506f).schedule(new Runnable() { // from class: com.google.android.gms.cloudmessaging.i
                    @Override // java.lang.Runnable
                    public final void run() {
                        m.this.d();
                    }
                }, 30L, TimeUnit.SECONDS);
            } else {
                a(0, "Unable to bind to service");
            }
        } catch (SecurityException e8) {
            b(0, "Unable to bind to service", e8);
        }
        return true;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, final IBinder iBinder) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service connected");
        }
        r.e(this.f11506f).execute(new Runnable() { // from class: com.google.android.gms.cloudmessaging.g
            @Override // java.lang.Runnable
            public final void run() {
                m mVar = m.this;
                IBinder iBinder2 = iBinder;
                synchronized (mVar) {
                    try {
                        if (iBinder2 == null) {
                            mVar.a(0, "Null service connection");
                            return;
                        }
                        try {
                            mVar.f11503c = new n(iBinder2);
                            mVar.f11501a = 2;
                            mVar.c();
                        } catch (RemoteException e8) {
                            mVar.a(0, e8.getMessage());
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        });
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service disconnected");
        }
        r.e(this.f11506f).execute(new Runnable() { // from class: com.google.android.gms.cloudmessaging.j
            @Override // java.lang.Runnable
            public final void run() {
                m.this.a(2, "Service disconnected");
            }
        });
    }
}
