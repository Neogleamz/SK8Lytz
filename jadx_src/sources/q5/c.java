package q5;

import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import b6.p;
import b6.y;
import b6.z;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import p5.b;
import p5.h;
import p5.k;
import p5.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c extends e {

    /* renamed from: g  reason: collision with root package name */
    private final z f22576g = new z();

    /* renamed from: h  reason: collision with root package name */
    private final y f22577h = new y();

    /* renamed from: i  reason: collision with root package name */
    private int f22578i = -1;

    /* renamed from: j  reason: collision with root package name */
    private final boolean f22579j;

    /* renamed from: k  reason: collision with root package name */
    private final int f22580k;

    /* renamed from: l  reason: collision with root package name */
    private final b[] f22581l;

    /* renamed from: m  reason: collision with root package name */
    private b f22582m;

    /* renamed from: n  reason: collision with root package name */
    private List<p5.b> f22583n;

    /* renamed from: o  reason: collision with root package name */
    private List<p5.b> f22584o;

    /* renamed from: p  reason: collision with root package name */
    private C0200c f22585p;
    private int q;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: c  reason: collision with root package name */
        private static final Comparator<a> f22586c = q5.b.a;

        /* renamed from: a  reason: collision with root package name */
        public final p5.b f22587a;

        /* renamed from: b  reason: collision with root package name */
        public final int f22588b;

        public a(CharSequence charSequence, Layout.Alignment alignment, float f5, int i8, int i9, float f8, int i10, float f9, boolean z4, int i11, int i12) {
            b.C0196b n8 = new b.C0196b().o(charSequence).p(alignment).h(f5, i8).i(i9).k(f8).l(i10).n(f9);
            if (z4) {
                n8.s(i11);
            }
            this.f22587a = n8.a();
            this.f22588b = i12;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ int c(a aVar, a aVar2) {
            return Integer.compare(aVar2.f22588b, aVar.f22588b);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {
        private static final int[] A;
        private static final int[] B;
        private static final boolean[] C;
        private static final int[] D;
        private static final int[] E;
        private static final int[] F;
        private static final int[] G;

        /* renamed from: w  reason: collision with root package name */
        public static final int f22589w = h(2, 2, 2, 0);

        /* renamed from: x  reason: collision with root package name */
        public static final int f22590x;

        /* renamed from: y  reason: collision with root package name */
        public static final int f22591y;

        /* renamed from: z  reason: collision with root package name */
        private static final int[] f22592z;

        /* renamed from: a  reason: collision with root package name */
        private final List<SpannableString> f22593a = new ArrayList();

        /* renamed from: b  reason: collision with root package name */
        private final SpannableStringBuilder f22594b = new SpannableStringBuilder();

        /* renamed from: c  reason: collision with root package name */
        private boolean f22595c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f22596d;

        /* renamed from: e  reason: collision with root package name */
        private int f22597e;

        /* renamed from: f  reason: collision with root package name */
        private boolean f22598f;

        /* renamed from: g  reason: collision with root package name */
        private int f22599g;

        /* renamed from: h  reason: collision with root package name */
        private int f22600h;

        /* renamed from: i  reason: collision with root package name */
        private int f22601i;

        /* renamed from: j  reason: collision with root package name */
        private int f22602j;

        /* renamed from: k  reason: collision with root package name */
        private boolean f22603k;

        /* renamed from: l  reason: collision with root package name */
        private int f22604l;

        /* renamed from: m  reason: collision with root package name */
        private int f22605m;

        /* renamed from: n  reason: collision with root package name */
        private int f22606n;

        /* renamed from: o  reason: collision with root package name */
        private int f22607o;

        /* renamed from: p  reason: collision with root package name */
        private int f22608p;
        private int q;

        /* renamed from: r  reason: collision with root package name */
        private int f22609r;

        /* renamed from: s  reason: collision with root package name */
        private int f22610s;

        /* renamed from: t  reason: collision with root package name */
        private int f22611t;

        /* renamed from: u  reason: collision with root package name */
        private int f22612u;

        /* renamed from: v  reason: collision with root package name */
        private int f22613v;

        static {
            int h8 = h(0, 0, 0, 0);
            f22590x = h8;
            int h9 = h(0, 0, 0, 3);
            f22591y = h9;
            f22592z = new int[]{0, 0, 0, 0, 0, 2, 0};
            A = new int[]{0, 0, 0, 0, 0, 0, 2};
            B = new int[]{3, 3, 3, 3, 3, 3, 1};
            C = new boolean[]{false, false, false, true, true, true, false};
            D = new int[]{h8, h9, h8, h8, h9, h8, h8};
            E = new int[]{0, 1, 2, 3, 4, 3, 4};
            F = new int[]{0, 0, 0, 0, 0, 3, 3};
            G = new int[]{h8, h8, h8, h8, h8, h9, h9};
        }

        public b() {
            l();
        }

        public static int g(int i8, int i9, int i10) {
            return h(i8, i9, i10, 0);
        }

        /* JADX WARN: Removed duplicated region for block: B:14:0x0024  */
        /* JADX WARN: Removed duplicated region for block: B:15:0x0026  */
        /* JADX WARN: Removed duplicated region for block: B:17:0x0029  */
        /* JADX WARN: Removed duplicated region for block: B:18:0x002b  */
        /* JADX WARN: Removed duplicated region for block: B:20:0x002e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public static int h(int r4, int r5, int r6, int r7) {
            /*
                r0 = 0
                r1 = 4
                b6.a.c(r4, r0, r1)
                b6.a.c(r5, r0, r1)
                b6.a.c(r6, r0, r1)
                b6.a.c(r7, r0, r1)
                r1 = 1
                r2 = 255(0xff, float:3.57E-43)
                if (r7 == 0) goto L21
                if (r7 == r1) goto L21
                r3 = 2
                if (r7 == r3) goto L1e
                r3 = 3
                if (r7 == r3) goto L1c
                goto L21
            L1c:
                r7 = r0
                goto L22
            L1e:
                r7 = 127(0x7f, float:1.78E-43)
                goto L22
            L21:
                r7 = r2
            L22:
                if (r4 <= r1) goto L26
                r4 = r2
                goto L27
            L26:
                r4 = r0
            L27:
                if (r5 <= r1) goto L2b
                r5 = r2
                goto L2c
            L2b:
                r5 = r0
            L2c:
                if (r6 <= r1) goto L2f
                r0 = r2
            L2f:
                int r4 = android.graphics.Color.argb(r7, r4, r5, r0)
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: q5.c.b.h(int, int, int, int):int");
        }

        public void a(char c9) {
            if (c9 != '\n') {
                this.f22594b.append(c9);
                return;
            }
            this.f22593a.add(d());
            this.f22594b.clear();
            if (this.f22608p != -1) {
                this.f22608p = 0;
            }
            if (this.q != -1) {
                this.q = 0;
            }
            if (this.f22609r != -1) {
                this.f22609r = 0;
            }
            if (this.f22611t != -1) {
                this.f22611t = 0;
            }
            while (true) {
                if ((!this.f22603k || this.f22593a.size() < this.f22602j) && this.f22593a.size() < 15) {
                    return;
                }
                this.f22593a.remove(0);
            }
        }

        public void b() {
            int length = this.f22594b.length();
            if (length > 0) {
                this.f22594b.delete(length - 1, length);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x0065  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0070  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x008f  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x0091  */
        /* JADX WARN: Removed duplicated region for block: B:34:0x009c  */
        /* JADX WARN: Removed duplicated region for block: B:35:0x009e  */
        /* JADX WARN: Removed duplicated region for block: B:41:0x00aa  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public q5.c.a c() {
            /*
                Method dump skipped, instructions count: 195
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: q5.c.b.c():q5.c$a");
        }

        public SpannableString d() {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.f22594b);
            int length = spannableStringBuilder.length();
            if (length > 0) {
                if (this.f22608p != -1) {
                    spannableStringBuilder.setSpan(new StyleSpan(2), this.f22608p, length, 33);
                }
                if (this.q != -1) {
                    spannableStringBuilder.setSpan(new UnderlineSpan(), this.q, length, 33);
                }
                if (this.f22609r != -1) {
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(this.f22610s), this.f22609r, length, 33);
                }
                if (this.f22611t != -1) {
                    spannableStringBuilder.setSpan(new BackgroundColorSpan(this.f22612u), this.f22611t, length, 33);
                }
            }
            return new SpannableString(spannableStringBuilder);
        }

        public void e() {
            this.f22593a.clear();
            this.f22594b.clear();
            this.f22608p = -1;
            this.q = -1;
            this.f22609r = -1;
            this.f22611t = -1;
            this.f22613v = 0;
        }

        public void f(boolean z4, boolean z8, boolean z9, int i8, boolean z10, int i9, int i10, int i11, int i12, int i13, int i14, int i15) {
            this.f22595c = true;
            this.f22596d = z4;
            this.f22603k = z8;
            this.f22597e = i8;
            this.f22598f = z10;
            this.f22599g = i9;
            this.f22600h = i10;
            this.f22601i = i13;
            int i16 = i11 + 1;
            if (this.f22602j != i16) {
                this.f22602j = i16;
                while (true) {
                    if ((!z8 || this.f22593a.size() < this.f22602j) && this.f22593a.size() < 15) {
                        break;
                    }
                    this.f22593a.remove(0);
                }
            }
            if (i14 != 0 && this.f22605m != i14) {
                this.f22605m = i14;
                int i17 = i14 - 1;
                q(D[i17], f22591y, C[i17], 0, A[i17], B[i17], f22592z[i17]);
            }
            if (i15 == 0 || this.f22606n == i15) {
                return;
            }
            this.f22606n = i15;
            int i18 = i15 - 1;
            m(0, 1, 1, false, false, F[i18], E[i18]);
            n(f22589w, G[i18], f22590x);
        }

        public boolean i() {
            return this.f22595c;
        }

        public boolean j() {
            return !i() || (this.f22593a.isEmpty() && this.f22594b.length() == 0);
        }

        public boolean k() {
            return this.f22596d;
        }

        public void l() {
            e();
            this.f22595c = false;
            this.f22596d = false;
            this.f22597e = 4;
            this.f22598f = false;
            this.f22599g = 0;
            this.f22600h = 0;
            this.f22601i = 0;
            this.f22602j = 15;
            this.f22603k = true;
            this.f22604l = 0;
            this.f22605m = 0;
            this.f22606n = 0;
            int i8 = f22590x;
            this.f22607o = i8;
            this.f22610s = f22589w;
            this.f22612u = i8;
        }

        public void m(int i8, int i9, int i10, boolean z4, boolean z8, int i11, int i12) {
            if (this.f22608p != -1) {
                if (!z4) {
                    this.f22594b.setSpan(new StyleSpan(2), this.f22608p, this.f22594b.length(), 33);
                    this.f22608p = -1;
                }
            } else if (z4) {
                this.f22608p = this.f22594b.length();
            }
            if (this.q == -1) {
                if (z8) {
                    this.q = this.f22594b.length();
                }
            } else if (z8) {
            } else {
                this.f22594b.setSpan(new UnderlineSpan(), this.q, this.f22594b.length(), 33);
                this.q = -1;
            }
        }

        public void n(int i8, int i9, int i10) {
            if (this.f22609r != -1 && this.f22610s != i8) {
                this.f22594b.setSpan(new ForegroundColorSpan(this.f22610s), this.f22609r, this.f22594b.length(), 33);
            }
            if (i8 != f22589w) {
                this.f22609r = this.f22594b.length();
                this.f22610s = i8;
            }
            if (this.f22611t != -1 && this.f22612u != i9) {
                this.f22594b.setSpan(new BackgroundColorSpan(this.f22612u), this.f22611t, this.f22594b.length(), 33);
            }
            if (i9 != f22590x) {
                this.f22611t = this.f22594b.length();
                this.f22612u = i9;
            }
        }

        public void o(int i8, int i9) {
            if (this.f22613v != i8) {
                a('\n');
            }
            this.f22613v = i8;
        }

        public void p(boolean z4) {
            this.f22596d = z4;
        }

        public void q(int i8, int i9, boolean z4, int i10, int i11, int i12, int i13) {
            this.f22607o = i8;
            this.f22604l = i13;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: q5.c$c  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0200c {

        /* renamed from: a  reason: collision with root package name */
        public final int f22614a;

        /* renamed from: b  reason: collision with root package name */
        public final int f22615b;

        /* renamed from: c  reason: collision with root package name */
        public final byte[] f22616c;

        /* renamed from: d  reason: collision with root package name */
        int f22617d = 0;

        public C0200c(int i8, int i9) {
            this.f22614a = i8;
            this.f22615b = i9;
            this.f22616c = new byte[(i9 * 2) - 1];
        }
    }

    public c(int i8, List<byte[]> list) {
        boolean z4 = true;
        this.f22580k = i8 == -1 ? 1 : i8;
        this.f22579j = (list == null || !b6.e.h(list)) ? false : z4;
        this.f22581l = new b[8];
        for (int i9 = 0; i9 < 8; i9++) {
            this.f22581l[i9] = new b();
        }
        this.f22582m = this.f22581l[0];
    }

    private void A() {
        int h8 = b.h(this.f22577h.h(2), this.f22577h.h(2), this.f22577h.h(2), this.f22577h.h(2));
        int h9 = b.h(this.f22577h.h(2), this.f22577h.h(2), this.f22577h.h(2), this.f22577h.h(2));
        this.f22577h.r(2);
        this.f22582m.n(h8, h9, b.g(this.f22577h.h(2), this.f22577h.h(2), this.f22577h.h(2)));
    }

    private void B() {
        this.f22577h.r(4);
        int h8 = this.f22577h.h(4);
        this.f22577h.r(2);
        this.f22582m.o(h8, this.f22577h.h(6));
    }

    private void C() {
        int h8 = b.h(this.f22577h.h(2), this.f22577h.h(2), this.f22577h.h(2), this.f22577h.h(2));
        int h9 = this.f22577h.h(2);
        int g8 = b.g(this.f22577h.h(2), this.f22577h.h(2), this.f22577h.h(2));
        if (this.f22577h.g()) {
            h9 |= 4;
        }
        boolean g9 = this.f22577h.g();
        int h10 = this.f22577h.h(2);
        int h11 = this.f22577h.h(2);
        int h12 = this.f22577h.h(2);
        this.f22577h.r(8);
        this.f22582m.q(h8, g8, g9, h9, h10, h11, h12);
    }

    private void D() {
        StringBuilder sb;
        String str;
        C0200c c0200c = this.f22585p;
        if (c0200c.f22617d != (c0200c.f22615b * 2) - 1) {
            p.b("Cea708Decoder", "DtvCcPacket ended prematurely; size is " + ((this.f22585p.f22615b * 2) - 1) + ", but current index is " + this.f22585p.f22617d + " (sequence number " + this.f22585p.f22614a + ");");
        }
        boolean z4 = false;
        y yVar = this.f22577h;
        C0200c c0200c2 = this.f22585p;
        yVar.o(c0200c2.f22616c, c0200c2.f22617d);
        while (true) {
            if (this.f22577h.b() <= 0) {
                break;
            }
            int h8 = this.f22577h.h(3);
            int h9 = this.f22577h.h(5);
            if (h8 == 7) {
                this.f22577h.r(2);
                h8 = this.f22577h.h(6);
                if (h8 < 7) {
                    p.i("Cea708Decoder", "Invalid extended service number: " + h8);
                }
            }
            if (h9 == 0) {
                if (h8 != 0) {
                    p.i("Cea708Decoder", "serviceNumber is non-zero (" + h8 + ") when blockSize is 0");
                }
            } else if (h8 != this.f22580k) {
                this.f22577h.s(h9);
            } else {
                int e8 = this.f22577h.e() + (h9 * 8);
                while (this.f22577h.e() < e8) {
                    int h10 = this.f22577h.h(8);
                    if (h10 == 16) {
                        h10 = this.f22577h.h(8);
                        if (h10 <= 31) {
                            s(h10);
                        } else {
                            if (h10 <= 127) {
                                x(h10);
                            } else if (h10 <= 159) {
                                t(h10);
                            } else if (h10 <= 255) {
                                y(h10);
                            } else {
                                sb = new StringBuilder();
                                str = "Invalid extended command: ";
                                sb.append(str);
                                sb.append(h10);
                                p.i("Cea708Decoder", sb.toString());
                            }
                            z4 = true;
                        }
                    } else if (h10 <= 31) {
                        q(h10);
                    } else {
                        if (h10 <= 127) {
                            v(h10);
                        } else if (h10 <= 159) {
                            r(h10);
                        } else if (h10 <= 255) {
                            w(h10);
                        } else {
                            sb = new StringBuilder();
                            str = "Invalid base command: ";
                            sb.append(str);
                            sb.append(h10);
                            p.i("Cea708Decoder", sb.toString());
                        }
                        z4 = true;
                    }
                }
            }
        }
        if (z4) {
            this.f22583n = p();
        }
    }

    private void E() {
        for (int i8 = 0; i8 < 8; i8++) {
            this.f22581l[i8].l();
        }
    }

    private void o() {
        if (this.f22585p == null) {
            return;
        }
        D();
        this.f22585p = null;
    }

    private List<p5.b> p() {
        a c9;
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < 8; i8++) {
            if (!this.f22581l[i8].j() && this.f22581l[i8].k() && (c9 = this.f22581l[i8].c()) != null) {
                arrayList.add(c9);
            }
        }
        Collections.sort(arrayList, a.f22586c);
        ArrayList arrayList2 = new ArrayList(arrayList.size());
        for (int i9 = 0; i9 < arrayList.size(); i9++) {
            arrayList2.add(((a) arrayList.get(i9)).f22587a);
        }
        return Collections.unmodifiableList(arrayList2);
    }

    private void q(int i8) {
        y yVar;
        if (i8 != 0) {
            if (i8 == 3) {
                this.f22583n = p();
                return;
            }
            int i9 = 8;
            if (i8 == 8) {
                this.f22582m.b();
                return;
            }
            switch (i8) {
                case 12:
                    E();
                    return;
                case 13:
                    this.f22582m.a('\n');
                    return;
                case 14:
                    return;
                default:
                    if (i8 >= 17 && i8 <= 23) {
                        p.i("Cea708Decoder", "Currently unsupported COMMAND_EXT1 Command: " + i8);
                        yVar = this.f22577h;
                    } else if (i8 < 24 || i8 > 31) {
                        p.i("Cea708Decoder", "Invalid C0 command: " + i8);
                        return;
                    } else {
                        p.i("Cea708Decoder", "Currently unsupported COMMAND_P16 Command: " + i8);
                        yVar = this.f22577h;
                        i9 = 16;
                    }
                    yVar.r(i9);
                    return;
            }
        }
    }

    private void r(int i8) {
        b bVar;
        b bVar2;
        y yVar;
        int i9 = 16;
        int i10 = 1;
        switch (i8) {
            case RecognitionOptions.ITF /* 128 */:
            case 129:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
                int i11 = i8 - 128;
                if (this.q != i11) {
                    this.q = i11;
                    bVar = this.f22581l[i11];
                    this.f22582m = bVar;
                    return;
                }
                return;
            case 136:
                while (i10 <= 8) {
                    if (this.f22577h.g()) {
                        this.f22581l[8 - i10].e();
                    }
                    i10++;
                }
                return;
            case 137:
                for (int i12 = 1; i12 <= 8; i12++) {
                    if (this.f22577h.g()) {
                        this.f22581l[8 - i12].p(true);
                    }
                }
                return;
            case 138:
                while (i10 <= 8) {
                    if (this.f22577h.g()) {
                        this.f22581l[8 - i10].p(false);
                    }
                    i10++;
                }
                return;
            case 139:
                for (int i13 = 1; i13 <= 8; i13++) {
                    if (this.f22577h.g()) {
                        this.f22581l[8 - i13].p(!bVar2.k());
                    }
                }
                return;
            case 140:
                while (i10 <= 8) {
                    if (this.f22577h.g()) {
                        this.f22581l[8 - i10].l();
                    }
                    i10++;
                }
                return;
            case 141:
                this.f22577h.r(8);
                return;
            case 142:
                return;
            case 143:
                E();
                return;
            case 144:
                if (this.f22582m.i()) {
                    z();
                    return;
                }
                yVar = this.f22577h;
                yVar.r(i9);
                return;
            case 145:
                if (this.f22582m.i()) {
                    A();
                    return;
                }
                yVar = this.f22577h;
                i9 = 24;
                yVar.r(i9);
                return;
            case 146:
                if (this.f22582m.i()) {
                    B();
                    return;
                }
                yVar = this.f22577h;
                yVar.r(i9);
                return;
            case 147:
            case 148:
            case 149:
            case 150:
            default:
                p.i("Cea708Decoder", "Invalid C1 command: " + i8);
                return;
            case 151:
                if (this.f22582m.i()) {
                    C();
                    return;
                }
                yVar = this.f22577h;
                i9 = 32;
                yVar.r(i9);
                return;
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
                int i14 = i8 - 152;
                u(i14);
                if (this.q != i14) {
                    this.q = i14;
                    bVar = this.f22581l[i14];
                    this.f22582m = bVar;
                    return;
                }
                return;
        }
    }

    private void s(int i8) {
        y yVar;
        int i9;
        if (i8 <= 7) {
            return;
        }
        if (i8 <= 15) {
            yVar = this.f22577h;
            i9 = 8;
        } else if (i8 <= 23) {
            yVar = this.f22577h;
            i9 = 16;
        } else if (i8 > 31) {
            return;
        } else {
            yVar = this.f22577h;
            i9 = 24;
        }
        yVar.r(i9);
    }

    private void t(int i8) {
        y yVar;
        int i9;
        if (i8 <= 135) {
            yVar = this.f22577h;
            i9 = 32;
        } else if (i8 > 143) {
            if (i8 <= 159) {
                this.f22577h.r(2);
                this.f22577h.r(this.f22577h.h(6) * 8);
                return;
            }
            return;
        } else {
            yVar = this.f22577h;
            i9 = 40;
        }
        yVar.r(i9);
    }

    private void u(int i8) {
        b bVar = this.f22581l[i8];
        this.f22577h.r(2);
        boolean g8 = this.f22577h.g();
        boolean g9 = this.f22577h.g();
        boolean g10 = this.f22577h.g();
        int h8 = this.f22577h.h(3);
        boolean g11 = this.f22577h.g();
        int h9 = this.f22577h.h(7);
        int h10 = this.f22577h.h(8);
        int h11 = this.f22577h.h(4);
        int h12 = this.f22577h.h(4);
        this.f22577h.r(2);
        int h13 = this.f22577h.h(6);
        this.f22577h.r(2);
        bVar.f(g8, g9, g10, h8, g11, h9, h10, h12, h13, h11, this.f22577h.h(3), this.f22577h.h(3));
    }

    private void v(int i8) {
        if (i8 == 127) {
            this.f22582m.a((char) 9835);
        } else {
            this.f22582m.a((char) (i8 & 255));
        }
    }

    private void w(int i8) {
        this.f22582m.a((char) (i8 & 255));
    }

    private void x(int i8) {
        b bVar;
        char c9 = ' ';
        if (i8 == 32) {
            bVar = this.f22582m;
        } else if (i8 == 33) {
            bVar = this.f22582m;
            c9 = 160;
        } else if (i8 == 37) {
            bVar = this.f22582m;
            c9 = 8230;
        } else if (i8 == 42) {
            bVar = this.f22582m;
            c9 = 352;
        } else if (i8 == 44) {
            bVar = this.f22582m;
            c9 = 338;
        } else if (i8 == 63) {
            bVar = this.f22582m;
            c9 = 376;
        } else if (i8 == 57) {
            bVar = this.f22582m;
            c9 = 8482;
        } else if (i8 == 58) {
            bVar = this.f22582m;
            c9 = 353;
        } else if (i8 == 60) {
            bVar = this.f22582m;
            c9 = 339;
        } else if (i8 != 61) {
            switch (i8) {
                case 48:
                    bVar = this.f22582m;
                    c9 = 9608;
                    break;
                case 49:
                    bVar = this.f22582m;
                    c9 = 8216;
                    break;
                case 50:
                    bVar = this.f22582m;
                    c9 = 8217;
                    break;
                case 51:
                    bVar = this.f22582m;
                    c9 = 8220;
                    break;
                case 52:
                    bVar = this.f22582m;
                    c9 = 8221;
                    break;
                case 53:
                    bVar = this.f22582m;
                    c9 = 8226;
                    break;
                default:
                    switch (i8) {
                        case 118:
                            bVar = this.f22582m;
                            c9 = 8539;
                            break;
                        case 119:
                            bVar = this.f22582m;
                            c9 = 8540;
                            break;
                        case 120:
                            bVar = this.f22582m;
                            c9 = 8541;
                            break;
                        case 121:
                            bVar = this.f22582m;
                            c9 = 8542;
                            break;
                        case 122:
                            bVar = this.f22582m;
                            c9 = 9474;
                            break;
                        case 123:
                            bVar = this.f22582m;
                            c9 = 9488;
                            break;
                        case 124:
                            bVar = this.f22582m;
                            c9 = 9492;
                            break;
                        case 125:
                            bVar = this.f22582m;
                            c9 = 9472;
                            break;
                        case 126:
                            bVar = this.f22582m;
                            c9 = 9496;
                            break;
                        case 127:
                            bVar = this.f22582m;
                            c9 = 9484;
                            break;
                        default:
                            p.i("Cea708Decoder", "Invalid G2 character: " + i8);
                            return;
                    }
            }
        } else {
            bVar = this.f22582m;
            c9 = 8480;
        }
        bVar.a(c9);
    }

    private void y(int i8) {
        b bVar;
        char c9;
        if (i8 == 160) {
            bVar = this.f22582m;
            c9 = 13252;
        } else {
            p.i("Cea708Decoder", "Invalid G3 character: " + i8);
            bVar = this.f22582m;
            c9 = '_';
        }
        bVar.a(c9);
    }

    private void z() {
        this.f22582m.m(this.f22577h.h(4), this.f22577h.h(2), this.f22577h.h(2), this.f22577h.g(), this.f22577h.g(), this.f22577h.h(3), this.f22577h.h(3));
    }

    @Override // q5.e, p5.i
    public /* bridge */ /* synthetic */ void a(long j8) {
        super.a(j8);
    }

    @Override // q5.e
    protected h e() {
        List<p5.b> list = this.f22583n;
        this.f22584o = list;
        return new f((List) b6.a.e(list));
    }

    @Override // q5.e
    protected void f(k kVar) {
        ByteBuffer byteBuffer = (ByteBuffer) b6.a.e(kVar.f9512c);
        this.f22576g.S(byteBuffer.array(), byteBuffer.limit());
        while (this.f22576g.a() >= 3) {
            int H = this.f22576g.H() & 7;
            int i8 = H & 3;
            boolean z4 = (H & 4) == 4;
            byte H2 = (byte) this.f22576g.H();
            byte H3 = (byte) this.f22576g.H();
            if (i8 == 2 || i8 == 3) {
                if (z4) {
                    if (i8 == 3) {
                        o();
                        int i9 = (H2 & 192) >> 6;
                        int i10 = this.f22578i;
                        if (i10 != -1 && i9 != (i10 + 1) % 4) {
                            E();
                            p.i("Cea708Decoder", "Sequence number discontinuity. previous=" + this.f22578i + " current=" + i9);
                        }
                        this.f22578i = i9;
                        int i11 = H2 & 63;
                        if (i11 == 0) {
                            i11 = 64;
                        }
                        C0200c c0200c = new C0200c(i9, i11);
                        this.f22585p = c0200c;
                        byte[] bArr = c0200c.f22616c;
                        int i12 = c0200c.f22617d;
                        c0200c.f22617d = i12 + 1;
                        bArr[i12] = H3;
                    } else {
                        b6.a.a(i8 == 2);
                        C0200c c0200c2 = this.f22585p;
                        if (c0200c2 == null) {
                            p.c("Cea708Decoder", "Encountered DTVCC_PACKET_DATA before DTVCC_PACKET_START");
                        } else {
                            byte[] bArr2 = c0200c2.f22616c;
                            int i13 = c0200c2.f22617d;
                            int i14 = i13 + 1;
                            c0200c2.f22617d = i14;
                            bArr2[i13] = H2;
                            c0200c2.f22617d = i14 + 1;
                            bArr2[i14] = H3;
                        }
                    }
                    C0200c c0200c3 = this.f22585p;
                    if (c0200c3.f22617d == (c0200c3.f22615b * 2) - 1) {
                        o();
                    }
                }
            }
        }
    }

    @Override // q5.e, l4.d
    public void flush() {
        super.flush();
        this.f22583n = null;
        this.f22584o = null;
        this.q = 0;
        this.f22582m = this.f22581l[0];
        E();
        this.f22585p = null;
    }

    @Override // q5.e
    public /* bridge */ /* synthetic */ k g() {
        return super.c();
    }

    @Override // q5.e
    public /* bridge */ /* synthetic */ l h() {
        return super.b();
    }

    @Override // q5.e
    protected boolean k() {
        return this.f22583n != this.f22584o;
    }

    @Override // q5.e
    public /* bridge */ /* synthetic */ void l(k kVar) {
        super.d(kVar);
    }

    @Override // q5.e, l4.d
    public /* bridge */ /* synthetic */ void release() {
        super.release();
    }
}
