package n4;

import a6.f;
import com.google.android.exoplayer2.w0;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface b0 {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final int f22048a;

        /* renamed from: b  reason: collision with root package name */
        public final byte[] f22049b;

        /* renamed from: c  reason: collision with root package name */
        public final int f22050c;

        /* renamed from: d  reason: collision with root package name */
        public final int f22051d;

        public a(int i8, byte[] bArr, int i9, int i10) {
            this.f22048a = i8;
            this.f22049b = bArr;
            this.f22050c = i9;
            this.f22051d = i10;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || a.class != obj.getClass()) {
                return false;
            }
            a aVar = (a) obj;
            return this.f22048a == aVar.f22048a && this.f22050c == aVar.f22050c && this.f22051d == aVar.f22051d && Arrays.equals(this.f22049b, aVar.f22049b);
        }

        public int hashCode() {
            return (((((this.f22048a * 31) + Arrays.hashCode(this.f22049b)) * 31) + this.f22050c) * 31) + this.f22051d;
        }
    }

    int a(f fVar, int i8, boolean z4, int i9);

    default void b(b6.z zVar, int i8) {
        e(zVar, i8, 0);
    }

    default int c(f fVar, int i8, boolean z4) {
        return a(fVar, i8, z4, 0);
    }

    void d(long j8, int i8, int i9, int i10, a aVar);

    void e(b6.z zVar, int i8, int i9);

    void f(w0 w0Var);
}
