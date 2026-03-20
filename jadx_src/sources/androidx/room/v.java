package androidx.room;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import androidx.room.q;
import androidx.room.r;
import androidx.room.u;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class v {

    /* renamed from: a  reason: collision with root package name */
    final Context f7227a;

    /* renamed from: b  reason: collision with root package name */
    final String f7228b;

    /* renamed from: c  reason: collision with root package name */
    int f7229c;

    /* renamed from: d  reason: collision with root package name */
    final u f7230d;

    /* renamed from: e  reason: collision with root package name */
    final u.c f7231e;

    /* renamed from: f  reason: collision with root package name */
    r f7232f;

    /* renamed from: g  reason: collision with root package name */
    final Executor f7233g;

    /* renamed from: h  reason: collision with root package name */
    final q f7234h = new a();

    /* renamed from: i  reason: collision with root package name */
    final AtomicBoolean f7235i = new AtomicBoolean(false);

    /* renamed from: j  reason: collision with root package name */
    final ServiceConnection f7236j;

    /* renamed from: k  reason: collision with root package name */
    final Runnable f7237k;

    /* renamed from: l  reason: collision with root package name */
    final Runnable f7238l;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends q.a {

        /* renamed from: androidx.room.v$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class RunnableC0078a implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ String[] f7240a;

            RunnableC0078a(String[] strArr) {
                this.f7240a = strArr;
            }

            @Override // java.lang.Runnable
            public void run() {
                v.this.f7230d.f(this.f7240a);
            }
        }

        a() {
        }

        @Override // androidx.room.q
        public void t(String[] strArr) {
            v.this.f7233g.execute(new RunnableC0078a(strArr));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements ServiceConnection {
        b() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            v.this.f7232f = r.a.d(iBinder);
            v vVar = v.this;
            vVar.f7233g.execute(vVar.f7237k);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            v vVar = v.this;
            vVar.f7233g.execute(vVar.f7238l);
            v.this.f7232f = null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                v vVar = v.this;
                r rVar = vVar.f7232f;
                if (rVar != null) {
                    vVar.f7229c = rVar.U(vVar.f7234h, vVar.f7228b);
                    v vVar2 = v.this;
                    vVar2.f7230d.a(vVar2.f7231e);
                }
            } catch (RemoteException e8) {
                Log.w("ROOM", "Cannot register multi-instance invalidation callback", e8);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d implements Runnable {
        d() {
        }

        @Override // java.lang.Runnable
        public void run() {
            v vVar = v.this;
            vVar.f7230d.i(vVar.f7231e);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class e extends u.c {
        e(String[] strArr) {
            super(strArr);
        }

        @Override // androidx.room.u.c
        boolean a() {
            return true;
        }

        @Override // androidx.room.u.c
        public void b(Set<String> set) {
            if (v.this.f7235i.get()) {
                return;
            }
            try {
                v vVar = v.this;
                r rVar = vVar.f7232f;
                if (rVar != null) {
                    rVar.N1(vVar.f7229c, (String[]) set.toArray(new String[0]));
                }
            } catch (RemoteException e8) {
                Log.w("ROOM", "Cannot broadcast invalidation", e8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public v(Context context, String str, u uVar, Executor executor) {
        b bVar = new b();
        this.f7236j = bVar;
        this.f7237k = new c();
        this.f7238l = new d();
        Context applicationContext = context.getApplicationContext();
        this.f7227a = applicationContext;
        this.f7228b = str;
        this.f7230d = uVar;
        this.f7233g = executor;
        this.f7231e = new e((String[]) uVar.f7198a.keySet().toArray(new String[0]));
        applicationContext.bindService(new Intent(applicationContext, MultiInstanceInvalidationService.class), bVar, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        if (this.f7235i.compareAndSet(false, true)) {
            this.f7230d.i(this.f7231e);
            try {
                r rVar = this.f7232f;
                if (rVar != null) {
                    rVar.V1(this.f7234h, this.f7229c);
                }
            } catch (RemoteException e8) {
                Log.w("ROOM", "Cannot unregister multi-instance invalidation callback", e8);
            }
            this.f7227a.unbindService(this.f7236j);
        }
    }
}
