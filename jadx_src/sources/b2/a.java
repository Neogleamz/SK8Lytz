package b2;

import android.os.Build;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a implements b2.e {

    /* renamed from: c  reason: collision with root package name */
    private static final Set<a> f7962c = new HashSet();

    /* renamed from: a  reason: collision with root package name */
    private final String f7963a;

    /* renamed from: b  reason: collision with root package name */
    private final String f7964b;

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: b2.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0091a {

        /* renamed from: a  reason: collision with root package name */
        static final Set<String> f7965a = new HashSet(Arrays.asList(k.d().a()));
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends a {
        /* JADX INFO: Access modifiers changed from: package-private */
        public b(String str, String str2) {
            super(str, str2);
        }

        @Override // b2.a
        public boolean c() {
            return Build.VERSION.SDK_INT >= 23;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c extends a {
        /* JADX INFO: Access modifiers changed from: package-private */
        public c(String str, String str2) {
            super(str, str2);
        }

        @Override // b2.a
        public boolean c() {
            return Build.VERSION.SDK_INT >= 24;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d extends a {
        /* JADX INFO: Access modifiers changed from: package-private */
        public d(String str, String str2) {
            super(str, str2);
        }

        @Override // b2.a
        public boolean c() {
            return false;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e extends a {
        /* JADX INFO: Access modifiers changed from: package-private */
        public e(String str, String str2) {
            super(str, str2);
        }

        @Override // b2.a
        public boolean c() {
            return Build.VERSION.SDK_INT >= 26;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class f extends a {
        /* JADX INFO: Access modifiers changed from: package-private */
        public f(String str, String str2) {
            super(str, str2);
        }

        @Override // b2.a
        public boolean c() {
            return Build.VERSION.SDK_INT >= 27;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g extends a {
        /* JADX INFO: Access modifiers changed from: package-private */
        public g(String str, String str2) {
            super(str, str2);
        }

        @Override // b2.a
        public boolean c() {
            return Build.VERSION.SDK_INT >= 28;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class h extends a {
        /* JADX INFO: Access modifiers changed from: package-private */
        public h(String str, String str2) {
            super(str, str2);
        }

        @Override // b2.a
        public boolean c() {
            return Build.VERSION.SDK_INT >= 29;
        }
    }

    a(String str, String str2) {
        this.f7963a = str;
        this.f7964b = str2;
        f7962c.add(this);
    }

    public static Set<a> e() {
        return Collections.unmodifiableSet(f7962c);
    }

    @Override // b2.e
    public String a() {
        return this.f7963a;
    }

    @Override // b2.e
    public boolean b() {
        return c() || d();
    }

    public abstract boolean c();

    public boolean d() {
        return bl.a.b(C0091a.f7965a, this.f7964b);
    }
}
