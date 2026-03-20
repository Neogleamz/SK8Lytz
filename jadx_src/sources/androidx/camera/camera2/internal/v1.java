package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraDevice;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class v1 {

    /* renamed from: a  reason: collision with root package name */
    final Executor f2142a;

    /* renamed from: b  reason: collision with root package name */
    final Object f2143b = new Object();

    /* renamed from: c  reason: collision with root package name */
    final Set<n2> f2144c = new LinkedHashSet();

    /* renamed from: d  reason: collision with root package name */
    final Set<n2> f2145d = new LinkedHashSet();

    /* renamed from: e  reason: collision with root package name */
    final Set<n2> f2146e = new LinkedHashSet();

    /* renamed from: f  reason: collision with root package name */
    private final CameraDevice.StateCallback f2147f = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends CameraDevice.StateCallback {
        a() {
        }

        private void b() {
            List<n2> g8;
            synchronized (v1.this.f2143b) {
                g8 = v1.this.g();
                v1.this.f2146e.clear();
                v1.this.f2144c.clear();
                v1.this.f2145d.clear();
            }
            for (n2 n2Var : g8) {
                n2Var.d();
            }
        }

        private void c() {
            final LinkedHashSet linkedHashSet = new LinkedHashSet();
            synchronized (v1.this.f2143b) {
                linkedHashSet.addAll(v1.this.f2146e);
                linkedHashSet.addAll(v1.this.f2144c);
            }
            v1.this.f2142a.execute(new Runnable() { // from class: androidx.camera.camera2.internal.u1
                @Override // java.lang.Runnable
                public final void run() {
                    v1.b(linkedHashSet);
                }
            });
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onClosed(CameraDevice cameraDevice) {
            b();
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(CameraDevice cameraDevice) {
            c();
            b();
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(CameraDevice cameraDevice, int i8) {
            c();
            b();
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice cameraDevice) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public v1(Executor executor) {
        this.f2142a = executor;
    }

    private void a(n2 n2Var) {
        n2 next;
        Iterator<n2> it = g().iterator();
        while (it.hasNext() && (next = it.next()) != n2Var) {
            next.d();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(Set<n2> set) {
        for (n2 n2Var : set) {
            n2Var.c().p(n2Var);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CameraDevice.StateCallback c() {
        return this.f2147f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<n2> d() {
        ArrayList arrayList;
        synchronized (this.f2143b) {
            arrayList = new ArrayList(this.f2144c);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<n2> e() {
        ArrayList arrayList;
        synchronized (this.f2143b) {
            arrayList = new ArrayList(this.f2145d);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<n2> f() {
        ArrayList arrayList;
        synchronized (this.f2143b) {
            arrayList = new ArrayList(this.f2146e);
        }
        return arrayList;
    }

    List<n2> g() {
        ArrayList arrayList;
        synchronized (this.f2143b) {
            arrayList = new ArrayList();
            arrayList.addAll(d());
            arrayList.addAll(f());
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(n2 n2Var) {
        synchronized (this.f2143b) {
            this.f2144c.remove(n2Var);
            this.f2145d.remove(n2Var);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i(n2 n2Var) {
        synchronized (this.f2143b) {
            this.f2145d.add(n2Var);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j(n2 n2Var) {
        a(n2Var);
        synchronized (this.f2143b) {
            this.f2146e.remove(n2Var);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(n2 n2Var) {
        synchronized (this.f2143b) {
            this.f2144c.add(n2Var);
            this.f2146e.remove(n2Var);
        }
        a(n2Var);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(n2 n2Var) {
        synchronized (this.f2143b) {
            this.f2146e.add(n2Var);
        }
    }
}
