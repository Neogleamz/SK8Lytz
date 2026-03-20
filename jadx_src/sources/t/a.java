package t;

import android.hardware.camera2.params.InputConfiguration;
import android.os.Build;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    private final c f22810a;

    /* renamed from: t.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class C0206a implements c {

        /* renamed from: a  reason: collision with root package name */
        private final InputConfiguration f22811a;

        C0206a(Object obj) {
            this.f22811a = (InputConfiguration) obj;
        }

        @Override // t.a.c
        public Object a() {
            return this.f22811a;
        }

        public boolean equals(Object obj) {
            if (obj instanceof c) {
                return Objects.equals(this.f22811a, ((c) obj).a());
            }
            return false;
        }

        public int hashCode() {
            return this.f22811a.hashCode();
        }

        public String toString() {
            return this.f22811a.toString();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b extends C0206a {
        b(Object obj) {
            super(obj);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private interface c {
        Object a();
    }

    private a(c cVar) {
        this.f22810a = cVar;
    }

    public static a b(Object obj) {
        int i8;
        if (obj != null && (i8 = Build.VERSION.SDK_INT) >= 23) {
            return i8 >= 31 ? new a(new b(obj)) : new a(new C0206a(obj));
        }
        return null;
    }

    public Object a() {
        return this.f22810a.a();
    }

    public boolean equals(Object obj) {
        if (obj instanceof a) {
            return this.f22810a.equals(((a) obj).f22810a);
        }
        return false;
    }

    public int hashCode() {
        return this.f22810a.hashCode();
    }

    public String toString() {
        return this.f22810a.toString();
    }
}
