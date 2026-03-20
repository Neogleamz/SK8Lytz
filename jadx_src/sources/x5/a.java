package x5;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import b6.l0;
import b6.p;
import b6.z;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.common.base.e;
import java.nio.charset.Charset;
import java.util.List;
import p5.b;
import p5.g;
import p5.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends g {

    /* renamed from: o  reason: collision with root package name */
    private final z f24150o;

    /* renamed from: p  reason: collision with root package name */
    private final boolean f24151p;
    private final int q;

    /* renamed from: r  reason: collision with root package name */
    private final int f24152r;

    /* renamed from: s  reason: collision with root package name */
    private final String f24153s;

    /* renamed from: t  reason: collision with root package name */
    private final float f24154t;

    /* renamed from: u  reason: collision with root package name */
    private final int f24155u;

    public a(List<byte[]> list) {
        super("Tx3gDecoder");
        this.f24150o = new z();
        if (list.size() != 1 || (list.get(0).length != 48 && list.get(0).length != 53)) {
            this.q = 0;
            this.f24152r = -1;
            this.f24153s = "sans-serif";
            this.f24151p = false;
            this.f24154t = 0.85f;
            this.f24155u = -1;
            return;
        }
        byte[] bArr = list.get(0);
        this.q = bArr[24];
        this.f24152r = ((bArr[26] & 255) << 24) | ((bArr[27] & 255) << 16) | ((bArr[28] & 255) << 8) | (bArr[29] & 255);
        this.f24153s = "Serif".equals(l0.E(bArr, 43, bArr.length - 43)) ? "serif" : "sans-serif";
        int i8 = bArr[25] * 20;
        this.f24155u = i8;
        boolean z4 = (bArr[0] & 32) != 0;
        this.f24151p = z4;
        if (z4) {
            this.f24154t = l0.p(((bArr[11] & 255) | ((bArr[10] & 255) << 8)) / i8, 0.0f, 0.95f);
        } else {
            this.f24154t = 0.85f;
        }
    }

    private void B(z zVar, SpannableStringBuilder spannableStringBuilder) {
        int i8;
        C(zVar.a() >= 12);
        int N = zVar.N();
        int N2 = zVar.N();
        zVar.V(2);
        int H = zVar.H();
        zVar.V(1);
        int q = zVar.q();
        if (N2 > spannableStringBuilder.length()) {
            p.i("Tx3gDecoder", "Truncating styl end (" + N2 + ") to cueText.length() (" + spannableStringBuilder.length() + ").");
            i8 = spannableStringBuilder.length();
        } else {
            i8 = N2;
        }
        if (N < i8) {
            int i9 = i8;
            E(spannableStringBuilder, H, this.q, N, i9, 0);
            D(spannableStringBuilder, q, this.f24152r, N, i9, 0);
            return;
        }
        p.i("Tx3gDecoder", "Ignoring styl with start (" + N + ") >= end (" + i8 + ").");
    }

    private static void C(boolean z4) {
        if (!z4) {
            throw new SubtitleDecoderException("Unexpected subtitle format.");
        }
    }

    private static void D(SpannableStringBuilder spannableStringBuilder, int i8, int i9, int i10, int i11, int i12) {
        if (i8 != i9) {
            spannableStringBuilder.setSpan(new ForegroundColorSpan((i8 >>> 8) | ((i8 & 255) << 24)), i10, i11, i12 | 33);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0033  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0036  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void E(android.text.SpannableStringBuilder r5, int r6, int r7, int r8, int r9, int r10) {
        /*
            if (r6 == r7) goto L4c
            r7 = r10 | 33
            r10 = r6 & 1
            r0 = 0
            r1 = 1
            if (r10 == 0) goto Lc
            r10 = r1
            goto Ld
        Lc:
            r10 = r0
        Ld:
            r2 = r6 & 2
            if (r2 == 0) goto L13
            r2 = r1
            goto L14
        L13:
            r2 = r0
        L14:
            if (r10 == 0) goto L23
            android.text.style.StyleSpan r3 = new android.text.style.StyleSpan
            if (r2 == 0) goto L1f
            r4 = 3
            r3.<init>(r4)
            goto L2b
        L1f:
            r3.<init>(r1)
            goto L2b
        L23:
            if (r2 == 0) goto L2e
            android.text.style.StyleSpan r3 = new android.text.style.StyleSpan
            r4 = 2
            r3.<init>(r4)
        L2b:
            r5.setSpan(r3, r8, r9, r7)
        L2e:
            r6 = r6 & 4
            if (r6 == 0) goto L33
            goto L34
        L33:
            r1 = r0
        L34:
            if (r1 == 0) goto L3e
            android.text.style.UnderlineSpan r6 = new android.text.style.UnderlineSpan
            r6.<init>()
            r5.setSpan(r6, r8, r9, r7)
        L3e:
            if (r1 != 0) goto L4c
            if (r10 != 0) goto L4c
            if (r2 != 0) goto L4c
            android.text.style.StyleSpan r6 = new android.text.style.StyleSpan
            r6.<init>(r0)
            r5.setSpan(r6, r8, r9, r7)
        L4c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: x5.a.E(android.text.SpannableStringBuilder, int, int, int, int, int):void");
    }

    private static void F(SpannableStringBuilder spannableStringBuilder, String str, int i8, int i9) {
        if (str != "sans-serif") {
            spannableStringBuilder.setSpan(new TypefaceSpan(str), i8, i9, 16711713);
        }
    }

    private static String G(z zVar) {
        C(zVar.a() >= 2);
        int N = zVar.N();
        if (N == 0) {
            return BuildConfig.FLAVOR;
        }
        int f5 = zVar.f();
        Charset P = zVar.P();
        int f8 = N - (zVar.f() - f5);
        if (P == null) {
            P = e.f18817c;
        }
        return zVar.F(f8, P);
    }

    @Override // p5.g
    protected h A(byte[] bArr, int i8, boolean z4) {
        this.f24150o.S(bArr, i8);
        String G = G(this.f24150o);
        if (G.isEmpty()) {
            return b.f24156b;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(G);
        E(spannableStringBuilder, this.q, 0, 0, spannableStringBuilder.length(), 16711680);
        D(spannableStringBuilder, this.f24152r, -1, 0, spannableStringBuilder.length(), 16711680);
        F(spannableStringBuilder, this.f24153s, 0, spannableStringBuilder.length());
        float f5 = this.f24154t;
        while (this.f24150o.a() >= 8) {
            int f8 = this.f24150o.f();
            int q = this.f24150o.q();
            int q8 = this.f24150o.q();
            if (q8 == 1937013100) {
                C(this.f24150o.a() >= 2);
                int N = this.f24150o.N();
                for (int i9 = 0; i9 < N; i9++) {
                    B(this.f24150o, spannableStringBuilder);
                }
            } else if (q8 == 1952608120 && this.f24151p) {
                C(this.f24150o.a() >= 2);
                f5 = l0.p(this.f24150o.N() / this.f24155u, 0.0f, 0.95f);
            }
            this.f24150o.U(f8 + q);
        }
        return new b(new b.C0196b().o(spannableStringBuilder).h(f5, 0).i(0).a());
    }
}
