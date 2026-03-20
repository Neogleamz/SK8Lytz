package v4;

import b6.z;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class a {

    /* renamed from: a  reason: collision with root package name */
    public final int f23173a;

    /* renamed from: v4.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class C0216a extends a {

        /* renamed from: b  reason: collision with root package name */
        public final long f23174b;

        /* renamed from: c  reason: collision with root package name */
        public final List<b> f23175c;

        /* renamed from: d  reason: collision with root package name */
        public final List<C0216a> f23176d;

        public C0216a(int i8, long j8) {
            super(i8);
            this.f23174b = j8;
            this.f23175c = new ArrayList();
            this.f23176d = new ArrayList();
        }

        public void d(C0216a c0216a) {
            this.f23176d.add(c0216a);
        }

        public void e(b bVar) {
            this.f23175c.add(bVar);
        }

        public C0216a f(int i8) {
            int size = this.f23176d.size();
            for (int i9 = 0; i9 < size; i9++) {
                C0216a c0216a = this.f23176d.get(i9);
                if (c0216a.f23173a == i8) {
                    return c0216a;
                }
            }
            return null;
        }

        public b g(int i8) {
            int size = this.f23175c.size();
            for (int i9 = 0; i9 < size; i9++) {
                b bVar = this.f23175c.get(i9);
                if (bVar.f23173a == i8) {
                    return bVar;
                }
            }
            return null;
        }

        @Override // v4.a
        public String toString() {
            return a.a(this.f23173a) + " leaves: " + Arrays.toString(this.f23175c.toArray()) + " containers: " + Arrays.toString(this.f23176d.toArray());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class b extends a {

        /* renamed from: b  reason: collision with root package name */
        public final z f23177b;

        public b(int i8, z zVar) {
            super(i8);
            this.f23177b = zVar;
        }
    }

    public a(int i8) {
        this.f23173a = i8;
    }

    public static String a(int i8) {
        return BuildConfig.FLAVOR + ((char) ((i8 >> 24) & 255)) + ((char) ((i8 >> 16) & 255)) + ((char) ((i8 >> 8) & 255)) + ((char) (i8 & 255));
    }

    public static int b(int i8) {
        return i8 & 16777215;
    }

    public static int c(int i8) {
        return (i8 >> 24) & 255;
    }

    public String toString() {
        return a(this.f23173a);
    }
}
