package s5;

import android.graphics.Bitmap;
import b6.l0;
import b6.z;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.zip.Inflater;
import p5.b;
import p5.g;
import p5.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends g {

    /* renamed from: o  reason: collision with root package name */
    private final z f22797o;

    /* renamed from: p  reason: collision with root package name */
    private final z f22798p;
    private final C0205a q;

    /* renamed from: r  reason: collision with root package name */
    private Inflater f22799r;

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: s5.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0205a {

        /* renamed from: a  reason: collision with root package name */
        private final z f22800a = new z();

        /* renamed from: b  reason: collision with root package name */
        private final int[] f22801b = new int[RecognitionOptions.QR_CODE];

        /* renamed from: c  reason: collision with root package name */
        private boolean f22802c;

        /* renamed from: d  reason: collision with root package name */
        private int f22803d;

        /* renamed from: e  reason: collision with root package name */
        private int f22804e;

        /* renamed from: f  reason: collision with root package name */
        private int f22805f;

        /* renamed from: g  reason: collision with root package name */
        private int f22806g;

        /* renamed from: h  reason: collision with root package name */
        private int f22807h;

        /* renamed from: i  reason: collision with root package name */
        private int f22808i;

        /* JADX INFO: Access modifiers changed from: private */
        public void e(z zVar, int i8) {
            int K;
            if (i8 < 4) {
                return;
            }
            zVar.V(3);
            int i9 = i8 - 4;
            if ((zVar.H() & RecognitionOptions.ITF) != 0) {
                if (i9 < 7 || (K = zVar.K()) < 4) {
                    return;
                }
                this.f22807h = zVar.N();
                this.f22808i = zVar.N();
                this.f22800a.Q(K - 4);
                i9 -= 7;
            }
            int f5 = this.f22800a.f();
            int g8 = this.f22800a.g();
            if (f5 >= g8 || i9 <= 0) {
                return;
            }
            int min = Math.min(i9, g8 - f5);
            zVar.l(this.f22800a.e(), f5, min);
            this.f22800a.U(f5 + min);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void f(z zVar, int i8) {
            if (i8 < 19) {
                return;
            }
            this.f22803d = zVar.N();
            this.f22804e = zVar.N();
            zVar.V(11);
            this.f22805f = zVar.N();
            this.f22806g = zVar.N();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void g(z zVar, int i8) {
            if (i8 % 5 != 2) {
                return;
            }
            zVar.V(2);
            Arrays.fill(this.f22801b, 0);
            int i9 = i8 / 5;
            int i10 = 0;
            while (i10 < i9) {
                int H = zVar.H();
                int H2 = zVar.H();
                int H3 = zVar.H();
                int H4 = zVar.H();
                int H5 = zVar.H();
                double d8 = H2;
                double d9 = H3 - 128;
                int i11 = i10;
                double d10 = H4 - 128;
                this.f22801b[H] = l0.q((int) (d8 + (d10 * 1.772d)), 0, 255) | (l0.q((int) ((d8 - (0.34414d * d10)) - (d9 * 0.71414d)), 0, 255) << 8) | (H5 << 24) | (l0.q((int) ((1.402d * d9) + d8), 0, 255) << 16);
                i10 = i11 + 1;
            }
            this.f22802c = true;
        }

        public p5.b d() {
            int i8;
            if (this.f22803d == 0 || this.f22804e == 0 || this.f22807h == 0 || this.f22808i == 0 || this.f22800a.g() == 0 || this.f22800a.f() != this.f22800a.g() || !this.f22802c) {
                return null;
            }
            this.f22800a.U(0);
            int i9 = this.f22807h * this.f22808i;
            int[] iArr = new int[i9];
            int i10 = 0;
            while (i10 < i9) {
                int H = this.f22800a.H();
                if (H != 0) {
                    i8 = i10 + 1;
                    iArr[i10] = this.f22801b[H];
                } else {
                    int H2 = this.f22800a.H();
                    if (H2 != 0) {
                        i8 = ((H2 & 64) == 0 ? H2 & 63 : ((H2 & 63) << 8) | this.f22800a.H()) + i10;
                        Arrays.fill(iArr, i10, i8, (H2 & RecognitionOptions.ITF) == 0 ? 0 : this.f22801b[this.f22800a.H()]);
                    }
                }
                i10 = i8;
            }
            return new b.C0196b().f(Bitmap.createBitmap(iArr, this.f22807h, this.f22808i, Bitmap.Config.ARGB_8888)).k(this.f22805f / this.f22803d).l(0).h(this.f22806g / this.f22804e, 0).i(0).n(this.f22807h / this.f22803d).g(this.f22808i / this.f22804e).a();
        }

        public void h() {
            this.f22803d = 0;
            this.f22804e = 0;
            this.f22805f = 0;
            this.f22806g = 0;
            this.f22807h = 0;
            this.f22808i = 0;
            this.f22800a.Q(0);
            this.f22802c = false;
        }
    }

    public a() {
        super("PgsDecoder");
        this.f22797o = new z();
        this.f22798p = new z();
        this.q = new C0205a();
    }

    private void B(z zVar) {
        if (zVar.a() <= 0 || zVar.j() != 120) {
            return;
        }
        if (this.f22799r == null) {
            this.f22799r = new Inflater();
        }
        if (l0.q0(zVar, this.f22798p, this.f22799r)) {
            zVar.S(this.f22798p.e(), this.f22798p.g());
        }
    }

    private static p5.b C(z zVar, C0205a c0205a) {
        int g8 = zVar.g();
        int H = zVar.H();
        int N = zVar.N();
        int f5 = zVar.f() + N;
        p5.b bVar = null;
        if (f5 > g8) {
            zVar.U(g8);
            return null;
        }
        if (H != 128) {
            switch (H) {
                case 20:
                    c0205a.g(zVar, N);
                    break;
                case 21:
                    c0205a.e(zVar, N);
                    break;
                case 22:
                    c0205a.f(zVar, N);
                    break;
            }
        } else {
            bVar = c0205a.d();
            c0205a.h();
        }
        zVar.U(f5);
        return bVar;
    }

    @Override // p5.g
    protected h A(byte[] bArr, int i8, boolean z4) {
        this.f22797o.S(bArr, i8);
        B(this.f22797o);
        this.q.h();
        ArrayList arrayList = new ArrayList();
        while (this.f22797o.a() >= 3) {
            p5.b C = C(this.f22797o, this.q);
            if (C != null) {
                arrayList.add(C);
            }
        }
        return new b(Collections.unmodifiableList(arrayList));
    }
}
