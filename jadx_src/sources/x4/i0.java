package x4;

import android.util.SparseArray;
import b6.z;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface i0 {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final String f23927a;

        /* renamed from: b  reason: collision with root package name */
        public final int f23928b;

        /* renamed from: c  reason: collision with root package name */
        public final byte[] f23929c;

        public a(String str, int i8, byte[] bArr) {
            this.f23927a = str;
            this.f23928b = i8;
            this.f23929c = bArr;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final int f23930a;

        /* renamed from: b  reason: collision with root package name */
        public final String f23931b;

        /* renamed from: c  reason: collision with root package name */
        public final List<a> f23932c;

        /* renamed from: d  reason: collision with root package name */
        public final byte[] f23933d;

        public b(int i8, String str, List<a> list, byte[] bArr) {
            this.f23930a = i8;
            this.f23931b = str;
            this.f23932c = list == null ? Collections.emptyList() : Collections.unmodifiableList(list);
            this.f23933d = bArr;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        i0 a(int i8, b bVar);

        SparseArray<i0> b();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        private final String f23934a;

        /* renamed from: b  reason: collision with root package name */
        private final int f23935b;

        /* renamed from: c  reason: collision with root package name */
        private final int f23936c;

        /* renamed from: d  reason: collision with root package name */
        private int f23937d;

        /* renamed from: e  reason: collision with root package name */
        private String f23938e;

        public d(int i8, int i9) {
            this(Integer.MIN_VALUE, i8, i9);
        }

        public d(int i8, int i9, int i10) {
            String str;
            if (i8 != Integer.MIN_VALUE) {
                str = i8 + "/";
            } else {
                str = BuildConfig.FLAVOR;
            }
            this.f23934a = str;
            this.f23935b = i9;
            this.f23936c = i10;
            this.f23937d = Integer.MIN_VALUE;
            this.f23938e = BuildConfig.FLAVOR;
        }

        private void d() {
            if (this.f23937d == Integer.MIN_VALUE) {
                throw new IllegalStateException("generateNewId() must be called before retrieving ids.");
            }
        }

        public void a() {
            int i8 = this.f23937d;
            this.f23937d = i8 == Integer.MIN_VALUE ? this.f23935b : i8 + this.f23936c;
            this.f23938e = this.f23934a + this.f23937d;
        }

        public String b() {
            d();
            return this.f23938e;
        }

        public int c() {
            d();
            return this.f23937d;
        }
    }

    void a(b6.h0 h0Var, n4.m mVar, d dVar);

    void b(z zVar, int i8);

    void c();
}
