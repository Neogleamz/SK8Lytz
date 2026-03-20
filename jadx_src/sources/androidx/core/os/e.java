package androidx.core.os;

import android.os.Build;
import android.os.CancellationSignal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: a  reason: collision with root package name */
    private boolean f4768a;

    /* renamed from: b  reason: collision with root package name */
    private b f4769b;

    /* renamed from: c  reason: collision with root package name */
    private Object f4770c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f4771d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static void a(Object obj) {
            ((CancellationSignal) obj).cancel();
        }

        static CancellationSignal b() {
            return new CancellationSignal();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a();
    }

    private void d() {
        while (this.f4771d) {
            try {
                wait();
            } catch (InterruptedException unused) {
            }
        }
    }

    public void a() {
        synchronized (this) {
            if (this.f4768a) {
                return;
            }
            this.f4768a = true;
            this.f4771d = true;
            b bVar = this.f4769b;
            Object obj = this.f4770c;
            if (bVar != null) {
                try {
                    bVar.a();
                } catch (Throwable th) {
                    synchronized (this) {
                        this.f4771d = false;
                        notifyAll();
                        throw th;
                    }
                }
            }
            if (obj != null && Build.VERSION.SDK_INT >= 16) {
                a.a(obj);
            }
            synchronized (this) {
                this.f4771d = false;
                notifyAll();
            }
        }
    }

    public boolean b() {
        boolean z4;
        synchronized (this) {
            z4 = this.f4768a;
        }
        return z4;
    }

    public void c(b bVar) {
        synchronized (this) {
            d();
            if (this.f4769b == bVar) {
                return;
            }
            this.f4769b = bVar;
            if (this.f4768a && bVar != null) {
                bVar.a();
            }
        }
    }
}
