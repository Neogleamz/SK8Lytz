package z5;

import android.util.Pair;
import b6.l0;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.source.k;
import i4.f0;
import i4.g0;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class t extends a0 {

    /* renamed from: c  reason: collision with root package name */
    private a f24694c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final int f24695a;

        /* renamed from: b  reason: collision with root package name */
        private final String[] f24696b;

        /* renamed from: c  reason: collision with root package name */
        private final int[] f24697c;

        /* renamed from: d  reason: collision with root package name */
        private final h5.w[] f24698d;

        /* renamed from: e  reason: collision with root package name */
        private final int[] f24699e;

        /* renamed from: f  reason: collision with root package name */
        private final int[][][] f24700f;

        /* renamed from: g  reason: collision with root package name */
        private final h5.w f24701g;

        a(String[] strArr, int[] iArr, h5.w[] wVarArr, int[] iArr2, int[][][] iArr3, h5.w wVar) {
            this.f24696b = strArr;
            this.f24697c = iArr;
            this.f24698d = wVarArr;
            this.f24700f = iArr3;
            this.f24699e = iArr2;
            this.f24701g = wVar;
            this.f24695a = iArr.length;
        }

        public int a(int i8, int i9, boolean z4) {
            int i10 = this.f24698d[i8].b(i9).f20308a;
            int[] iArr = new int[i10];
            int i11 = 0;
            for (int i12 = 0; i12 < i10; i12++) {
                int g8 = g(i8, i9, i12);
                if (g8 == 4 || (z4 && g8 == 3)) {
                    iArr[i11] = i12;
                    i11++;
                }
            }
            return b(i8, i9, Arrays.copyOf(iArr, i11));
        }

        public int b(int i8, int i9, int[] iArr) {
            int i10 = 0;
            int i11 = 16;
            String str = null;
            boolean z4 = false;
            int i12 = 0;
            while (i10 < iArr.length) {
                String str2 = this.f24698d[i8].b(i9).b(iArr[i10]).f11207m;
                int i13 = i12 + 1;
                if (i12 == 0) {
                    str = str2;
                } else {
                    z4 |= !l0.c(str, str2);
                }
                i11 = Math.min(i11, f0.t(this.f24700f[i8][i9][i10]));
                i10++;
                i12 = i13;
            }
            return z4 ? Math.min(i11, this.f24699e[i8]) : i11;
        }

        public int c(int i8, int i9, int i10) {
            return this.f24700f[i8][i9][i10];
        }

        public int d() {
            return this.f24695a;
        }

        public int e(int i8) {
            return this.f24697c[i8];
        }

        public h5.w f(int i8) {
            return this.f24698d[i8];
        }

        public int g(int i8, int i9, int i10) {
            return f0.F(c(i8, i9, i10));
        }

        public h5.w h() {
            return this.f24701g;
        }
    }

    private static int i(f0[] f0VarArr, h5.u uVar, int[] iArr, boolean z4) {
        int length = f0VarArr.length;
        boolean z8 = true;
        int i8 = 0;
        for (int i9 = 0; i9 < f0VarArr.length; i9++) {
            f0 f0Var = f0VarArr[i9];
            int i10 = 0;
            for (int i11 = 0; i11 < uVar.f20308a; i11++) {
                i10 = Math.max(i10, f0.F(f0Var.a(uVar.b(i11))));
            }
            boolean z9 = iArr[i9] == 0;
            if (i10 > i8 || (i10 == i8 && z4 && !z8 && z9)) {
                length = i9;
                z8 = z9;
                i8 = i10;
            }
        }
        return length;
    }

    private static int[] j(f0 f0Var, h5.u uVar) {
        int[] iArr = new int[uVar.f20308a];
        for (int i8 = 0; i8 < uVar.f20308a; i8++) {
            iArr[i8] = f0Var.a(uVar.b(i8));
        }
        return iArr;
    }

    private static int[] k(f0[] f0VarArr) {
        int length = f0VarArr.length;
        int[] iArr = new int[length];
        for (int i8 = 0; i8 < length; i8++) {
            iArr[i8] = f0VarArr[i8].v();
        }
        return iArr;
    }

    @Override // z5.a0
    public final void e(Object obj) {
        this.f24694c = (a) obj;
    }

    @Override // z5.a0
    public final b0 g(f0[] f0VarArr, h5.w wVar, k.b bVar, h2 h2Var) {
        int[] iArr = new int[f0VarArr.length + 1];
        int length = f0VarArr.length + 1;
        h5.u[][] uVarArr = new h5.u[length];
        int[][][] iArr2 = new int[f0VarArr.length + 1][];
        for (int i8 = 0; i8 < length; i8++) {
            int i9 = wVar.f20316a;
            uVarArr[i8] = new h5.u[i9];
            iArr2[i8] = new int[i9];
        }
        int[] k8 = k(f0VarArr);
        for (int i10 = 0; i10 < wVar.f20316a; i10++) {
            h5.u b9 = wVar.b(i10);
            int i11 = i(f0VarArr, b9, iArr, b9.f20310c == 5);
            int[] j8 = i11 == f0VarArr.length ? new int[b9.f20308a] : j(f0VarArr[i11], b9);
            int i12 = iArr[i11];
            uVarArr[i11][i12] = b9;
            iArr2[i11][i12] = j8;
            iArr[i11] = iArr[i11] + 1;
        }
        h5.w[] wVarArr = new h5.w[f0VarArr.length];
        String[] strArr = new String[f0VarArr.length];
        int[] iArr3 = new int[f0VarArr.length];
        for (int i13 = 0; i13 < f0VarArr.length; i13++) {
            int i14 = iArr[i13];
            wVarArr[i13] = new h5.w((h5.u[]) l0.H0(uVarArr[i13], i14));
            iArr2[i13] = (int[][]) l0.H0(iArr2[i13], i14);
            strArr[i13] = f0VarArr[i13].getName();
            iArr3[i13] = f0VarArr[i13].h();
        }
        a aVar = new a(strArr, iArr3, wVarArr, k8, iArr2, new h5.w((h5.u[]) l0.H0(uVarArr[f0VarArr.length], iArr[f0VarArr.length])));
        Pair<g0[], r[]> l8 = l(aVar, iArr2, k8, bVar, h2Var);
        return new b0((g0[]) l8.first, (r[]) l8.second, z.b(aVar, (u[]) l8.second), aVar);
    }

    protected abstract Pair<g0[], r[]> l(a aVar, int[][][] iArr, int[] iArr2, k.b bVar, h2 h2Var);
}
