package t;

import android.hardware.camera2.params.OutputConfiguration;
import android.os.Build;
import android.view.Surface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: a  reason: collision with root package name */
    private final a f22812a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface a {
        void a(long j8);

        void b(Surface surface);

        String c();

        void d();

        void e(String str);

        Object f();

        Surface getSurface();
    }

    public b(int i8, Surface surface) {
        a cVar;
        int i9 = Build.VERSION.SDK_INT;
        if (i9 >= 33) {
            cVar = new f(i8, surface);
        } else if (i9 >= 28) {
            cVar = new e(i8, surface);
        } else if (i9 >= 26) {
            cVar = new d(i8, surface);
        } else if (i9 < 24) {
            this.f22812a = new g(surface);
            return;
        } else {
            cVar = new c(i8, surface);
        }
        this.f22812a = cVar;
    }

    private b(a aVar) {
        this.f22812a = aVar;
    }

    public static b h(Object obj) {
        if (obj == null) {
            return null;
        }
        int i8 = Build.VERSION.SDK_INT;
        a k8 = i8 >= 33 ? f.k((OutputConfiguration) obj) : i8 >= 28 ? e.j((OutputConfiguration) obj) : i8 >= 26 ? d.i((OutputConfiguration) obj) : i8 >= 24 ? c.h((OutputConfiguration) obj) : null;
        if (k8 == null) {
            return null;
        }
        return new b(k8);
    }

    public void a(Surface surface) {
        this.f22812a.b(surface);
    }

    public void b() {
        this.f22812a.d();
    }

    public String c() {
        return this.f22812a.c();
    }

    public Surface d() {
        return this.f22812a.getSurface();
    }

    public void e(String str) {
        this.f22812a.e(str);
    }

    public boolean equals(Object obj) {
        if (obj instanceof b) {
            return this.f22812a.equals(((b) obj).f22812a);
        }
        return false;
    }

    public void f(long j8) {
        this.f22812a.a(j8);
    }

    public Object g() {
        return this.f22812a.f();
    }

    public int hashCode() {
        return this.f22812a.hashCode();
    }
}
