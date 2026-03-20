package z5;

import a6.d;
import b6.p;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.w0;
import j5.f;
import j5.n;
import j5.o;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface r extends u {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final h5.u f24689a;

        /* renamed from: b  reason: collision with root package name */
        public final int[] f24690b;

        /* renamed from: c  reason: collision with root package name */
        public final int f24691c;

        public a(h5.u uVar, int... iArr) {
            this(uVar, iArr, 0);
        }

        public a(h5.u uVar, int[] iArr, int i8) {
            if (iArr.length == 0) {
                p.d("ETSDefinition", "Empty tracks are not allowed", new IllegalArgumentException());
            }
            this.f24689a = uVar;
            this.f24690b = iArr;
            this.f24691c = i8;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        r[] a(a[] aVarArr, d dVar, k.b bVar, h2 h2Var);
    }

    void a(long j8, long j9, long j10, List<? extends n> list, o[] oVarArr);

    int c();

    boolean d(int i8, long j8);

    boolean e(int i8, long j8);

    default void f(boolean z4) {
    }

    void g();

    void i();

    int k(long j8, List<? extends n> list);

    int m();

    w0 n();

    int o();

    default boolean p(long j8, f fVar, List<? extends n> list) {
        return false;
    }

    void q(float f5);

    Object r();

    default void s() {
    }

    default void t() {
    }
}
