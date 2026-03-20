package q5;

import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import b6.l0;
import b6.p;
import b6.z;
import com.example.seedpoint.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p5.b;
import p5.h;
import p5.k;
import p5.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends e {

    /* renamed from: h  reason: collision with root package name */
    private final int f22549h;

    /* renamed from: i  reason: collision with root package name */
    private final int f22550i;

    /* renamed from: j  reason: collision with root package name */
    private final int f22551j;

    /* renamed from: k  reason: collision with root package name */
    private final long f22552k;

    /* renamed from: n  reason: collision with root package name */
    private List<b> f22555n;

    /* renamed from: o  reason: collision with root package name */
    private List<b> f22556o;

    /* renamed from: p  reason: collision with root package name */
    private int f22557p;
    private int q;

    /* renamed from: r  reason: collision with root package name */
    private boolean f22558r;

    /* renamed from: s  reason: collision with root package name */
    private boolean f22559s;

    /* renamed from: t  reason: collision with root package name */
    private byte f22560t;

    /* renamed from: u  reason: collision with root package name */
    private byte f22561u;

    /* renamed from: w  reason: collision with root package name */
    private boolean f22563w;

    /* renamed from: x  reason: collision with root package name */
    private long f22564x;

    /* renamed from: y  reason: collision with root package name */
    private static final int[] f22546y = {11, 1, 3, 12, 14, 5, 7, 9};

    /* renamed from: z  reason: collision with root package name */
    private static final int[] f22547z = {0, 4, 8, 12, 16, 20, 24, 28};
    private static final int[] A = {-1, -16711936, -16776961, -16711681, -65536, -256, -65281};
    private static final int[] B = {32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 225, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 233, 93, 237, 243, 250, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, R.styleable.AppCompatTheme_textAppearanceSmallPopupMenu, R.styleable.AppCompatTheme_textColorAlertDialogListItem, R.styleable.AppCompatTheme_textColorSearchUrl, R.styleable.AppCompatTheme_toolbarNavigationButtonStyle, R.styleable.AppCompatTheme_toolbarStyle, R.styleable.AppCompatTheme_tooltipForegroundColor, R.styleable.AppCompatTheme_tooltipFrameBackground, 116, 117, 118, 119, 120, 121, 122, 231, 247, 209, 241, 9632};
    private static final int[] C = {174, 176, 189, 191, 8482, 162, 163, 9834, 224, 32, 232, 226, 234, 238, 244, 251};
    private static final int[] D = {193, 201, 211, 218, 220, 252, 8216, 161, 42, 39, 8212, 169, 8480, 8226, 8220, 8221, 192, 194, 199, 200, 202, 203, 235, 206, 207, 239, 212, 217, 249, 219, 171, 187};
    private static final int[] E = {195, 227, 205, 204, 236, 210, 242, 213, 245, 123, 125, 92, 94, 95, 124, 126, 196, 228, 214, 246, 223, 165, 164, 9474, 197, 229, 216, 248, 9484, 9488, 9492, 9496};
    private static final boolean[] F = {false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false};

    /* renamed from: g  reason: collision with root package name */
    private final z f22548g = new z();

    /* renamed from: l  reason: collision with root package name */
    private final ArrayList<C0198a> f22553l = new ArrayList<>();

    /* renamed from: m  reason: collision with root package name */
    private C0198a f22554m = new C0198a(0, 4);

    /* renamed from: v  reason: collision with root package name */
    private int f22562v = 0;

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: q5.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0198a {

        /* renamed from: a  reason: collision with root package name */
        private final List<C0199a> f22565a = new ArrayList();

        /* renamed from: b  reason: collision with root package name */
        private final List<SpannableString> f22566b = new ArrayList();

        /* renamed from: c  reason: collision with root package name */
        private final StringBuilder f22567c = new StringBuilder();

        /* renamed from: d  reason: collision with root package name */
        private int f22568d;

        /* renamed from: e  reason: collision with root package name */
        private int f22569e;

        /* renamed from: f  reason: collision with root package name */
        private int f22570f;

        /* renamed from: g  reason: collision with root package name */
        private int f22571g;

        /* renamed from: h  reason: collision with root package name */
        private int f22572h;

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: q5.a$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class C0199a {

            /* renamed from: a  reason: collision with root package name */
            public final int f22573a;

            /* renamed from: b  reason: collision with root package name */
            public final boolean f22574b;

            /* renamed from: c  reason: collision with root package name */
            public int f22575c;

            public C0199a(int i8, boolean z4, int i9) {
                this.f22573a = i8;
                this.f22574b = z4;
                this.f22575c = i9;
            }
        }

        public C0198a(int i8, int i9) {
            j(i8);
            this.f22572h = i9;
        }

        private SpannableString h() {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.f22567c);
            int length = spannableStringBuilder.length();
            int i8 = 0;
            int i9 = 0;
            boolean z4 = false;
            int i10 = -1;
            int i11 = -1;
            int i12 = -1;
            int i13 = -1;
            while (i8 < this.f22565a.size()) {
                C0199a c0199a = this.f22565a.get(i8);
                boolean z8 = c0199a.f22574b;
                int i14 = c0199a.f22573a;
                if (i14 != 8) {
                    boolean z9 = i14 == 7;
                    if (i14 != 7) {
                        i13 = a.A[i14];
                    }
                    z4 = z9;
                }
                int i15 = c0199a.f22575c;
                i8++;
                if (i15 != (i8 < this.f22565a.size() ? this.f22565a.get(i8).f22575c : length)) {
                    if (i10 != -1 && !z8) {
                        q(spannableStringBuilder, i10, i15);
                        i10 = -1;
                    } else if (i10 == -1 && z8) {
                        i10 = i15;
                    }
                    if (i11 != -1 && !z4) {
                        o(spannableStringBuilder, i11, i15);
                        i11 = -1;
                    } else if (i11 == -1 && z4) {
                        i11 = i15;
                    }
                    if (i13 != i12) {
                        n(spannableStringBuilder, i9, i15, i12);
                        i12 = i13;
                        i9 = i15;
                    }
                }
            }
            if (i10 != -1 && i10 != length) {
                q(spannableStringBuilder, i10, length);
            }
            if (i11 != -1 && i11 != length) {
                o(spannableStringBuilder, i11, length);
            }
            if (i9 != length) {
                n(spannableStringBuilder, i9, length, i12);
            }
            return new SpannableString(spannableStringBuilder);
        }

        private static void n(SpannableStringBuilder spannableStringBuilder, int i8, int i9, int i10) {
            if (i10 == -1) {
                return;
            }
            spannableStringBuilder.setSpan(new ForegroundColorSpan(i10), i8, i9, 33);
        }

        private static void o(SpannableStringBuilder spannableStringBuilder, int i8, int i9) {
            spannableStringBuilder.setSpan(new StyleSpan(2), i8, i9, 33);
        }

        private static void q(SpannableStringBuilder spannableStringBuilder, int i8, int i9) {
            spannableStringBuilder.setSpan(new UnderlineSpan(), i8, i9, 33);
        }

        public void e(char c9) {
            if (this.f22567c.length() < 32) {
                this.f22567c.append(c9);
            }
        }

        public void f() {
            int length = this.f22567c.length();
            if (length > 0) {
                this.f22567c.delete(length - 1, length);
                for (int size = this.f22565a.size() - 1; size >= 0; size--) {
                    C0199a c0199a = this.f22565a.get(size);
                    int i8 = c0199a.f22575c;
                    if (i8 != length) {
                        return;
                    }
                    c0199a.f22575c = i8 - 1;
                }
            }
        }

        public b g(int i8) {
            float f5;
            int i9 = this.f22569e + this.f22570f;
            int i10 = 32 - i9;
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            for (int i11 = 0; i11 < this.f22566b.size(); i11++) {
                spannableStringBuilder.append(l0.Z0(this.f22566b.get(i11), i10));
                spannableStringBuilder.append('\n');
            }
            spannableStringBuilder.append(l0.Z0(h(), i10));
            if (spannableStringBuilder.length() == 0) {
                return null;
            }
            int length = i10 - spannableStringBuilder.length();
            int i12 = i9 - length;
            if (i8 == Integer.MIN_VALUE) {
                i8 = (this.f22571g != 2 || (Math.abs(i12) >= 3 && length >= 0)) ? (this.f22571g != 2 || i12 <= 0) ? 0 : 2 : 1;
            }
            if (i8 != 1) {
                if (i8 == 2) {
                    i9 = 32 - length;
                }
                f5 = ((i9 / 32.0f) * 0.8f) + 0.1f;
            } else {
                f5 = 0.5f;
            }
            int i13 = this.f22568d;
            if (i13 > 7) {
                i13 = (i13 - 15) - 2;
            } else if (this.f22571g == 1) {
                i13 -= this.f22572h - 1;
            }
            return new b.C0196b().o(spannableStringBuilder).p(Layout.Alignment.ALIGN_NORMAL).h(i13, 1).k(f5).l(i8).a();
        }

        public boolean i() {
            return this.f22565a.isEmpty() && this.f22566b.isEmpty() && this.f22567c.length() == 0;
        }

        public void j(int i8) {
            this.f22571g = i8;
            this.f22565a.clear();
            this.f22566b.clear();
            this.f22567c.setLength(0);
            this.f22568d = 15;
            this.f22569e = 0;
            this.f22570f = 0;
        }

        public void k() {
            this.f22566b.add(h());
            this.f22567c.setLength(0);
            this.f22565a.clear();
            int min = Math.min(this.f22572h, this.f22568d);
            while (this.f22566b.size() >= min) {
                this.f22566b.remove(0);
            }
        }

        public void l(int i8) {
            this.f22571g = i8;
        }

        public void m(int i8) {
            this.f22572h = i8;
        }

        public void p(int i8, boolean z4) {
            this.f22565a.add(new C0199a(i8, z4, this.f22567c.length()));
        }
    }

    public a(String str, int i8, long j8) {
        this.f22552k = j8 > 0 ? j8 * 1000 : -9223372036854775807L;
        this.f22549h = "application/x-mp4-cea-608".equals(str) ? 2 : 3;
        if (i8 != 1) {
            if (i8 == 2) {
                this.f22551j = 1;
                this.f22550i = 0;
                M(0);
                L();
                this.f22563w = true;
                this.f22564x = -9223372036854775807L;
            }
            if (i8 == 3) {
                this.f22551j = 0;
            } else if (i8 != 4) {
                p.i("Cea608Decoder", "Invalid channel. Defaulting to CC1.");
            } else {
                this.f22551j = 1;
            }
            this.f22550i = 1;
            M(0);
            L();
            this.f22563w = true;
            this.f22564x = -9223372036854775807L;
        }
        this.f22551j = 0;
        this.f22550i = 0;
        M(0);
        L();
        this.f22563w = true;
        this.f22564x = -9223372036854775807L;
    }

    private static boolean A(byte b9, byte b10) {
        return (b9 & 246) == 18 && (b10 & 224) == 32;
    }

    private static boolean B(byte b9, byte b10) {
        return (b9 & 247) == 17 && (b10 & 240) == 32;
    }

    private static boolean C(byte b9, byte b10) {
        return (b9 & 246) == 20 && (b10 & 240) == 32;
    }

    private static boolean D(byte b9, byte b10) {
        return (b9 & 240) == 16 && (b10 & 192) == 64;
    }

    private static boolean E(byte b9) {
        return (b9 & 240) == 16;
    }

    private boolean F(boolean z4, byte b9, byte b10) {
        if (!z4 || !E(b9)) {
            this.f22559s = false;
        } else if (this.f22559s && this.f22560t == b9 && this.f22561u == b10) {
            this.f22559s = false;
            return true;
        } else {
            this.f22559s = true;
            this.f22560t = b9;
            this.f22561u = b10;
        }
        return false;
    }

    private static boolean G(byte b9) {
        return (b9 & 246) == 20;
    }

    private static boolean H(byte b9, byte b10) {
        return (b9 & 247) == 17 && (b10 & 240) == 48;
    }

    private static boolean I(byte b9, byte b10) {
        return (b9 & 247) == 23 && b10 >= 33 && b10 <= 35;
    }

    private static boolean J(byte b9) {
        return 1 <= b9 && b9 <= 15;
    }

    private void K(byte b9, byte b10) {
        if (!J(b9)) {
            if (G(b9)) {
                if (b10 != 32 && b10 != 47) {
                    switch (b10) {
                        case 37:
                        case 38:
                        case 39:
                            break;
                        default:
                            switch (b10) {
                                case 41:
                                    break;
                                case 42:
                                case 43:
                                    break;
                                default:
                                    return;
                            }
                    }
                }
                this.f22563w = true;
                return;
            }
            return;
        }
        this.f22563w = false;
    }

    private void L() {
        this.f22554m.j(this.f22557p);
        this.f22553l.clear();
        this.f22553l.add(this.f22554m);
    }

    private void M(int i8) {
        int i9 = this.f22557p;
        if (i9 == i8) {
            return;
        }
        this.f22557p = i8;
        if (i8 == 3) {
            for (int i10 = 0; i10 < this.f22553l.size(); i10++) {
                this.f22553l.get(i10).l(i8);
            }
            return;
        }
        L();
        if (i9 == 3 || i8 == 1 || i8 == 0) {
            this.f22555n = Collections.emptyList();
        }
    }

    private void N(int i8) {
        this.q = i8;
        this.f22554m.m(i8);
    }

    private boolean O() {
        return (this.f22552k == -9223372036854775807L || this.f22564x == -9223372036854775807L || j() - this.f22564x < this.f22552k) ? false : true;
    }

    private boolean P(byte b9) {
        if (z(b9)) {
            this.f22562v = q(b9);
        }
        return this.f22562v == this.f22551j;
    }

    private static char p(byte b9) {
        return (char) B[(b9 & Byte.MAX_VALUE) - 32];
    }

    private static int q(byte b9) {
        return (b9 >> 3) & 1;
    }

    private List<b> r() {
        int size = this.f22553l.size();
        ArrayList arrayList = new ArrayList(size);
        int i8 = 2;
        for (int i9 = 0; i9 < size; i9++) {
            b g8 = this.f22553l.get(i9).g(Integer.MIN_VALUE);
            arrayList.add(g8);
            if (g8 != null) {
                i8 = Math.min(i8, g8.f22382j);
            }
        }
        ArrayList arrayList2 = new ArrayList(size);
        for (int i10 = 0; i10 < size; i10++) {
            b bVar = (b) arrayList.get(i10);
            if (bVar != null) {
                if (bVar.f22382j != i8) {
                    bVar = (b) b6.a.e(this.f22553l.get(i10).g(i8));
                }
                arrayList2.add(bVar);
            }
        }
        return arrayList2;
    }

    private static char s(byte b9) {
        return (char) D[b9 & 31];
    }

    private static char t(byte b9) {
        return (char) E[b9 & 31];
    }

    private static char u(byte b9, byte b10) {
        return (b9 & 1) == 0 ? s(b10) : t(b10);
    }

    private static char v(byte b9) {
        return (char) C[b9 & 15];
    }

    private void w(byte b9) {
        this.f22554m.e(' ');
        this.f22554m.p((b9 >> 1) & 7, (b9 & 1) == 1);
    }

    private void x(byte b9) {
        if (b9 == 32) {
            M(2);
        } else if (b9 == 41) {
            M(3);
        } else {
            switch (b9) {
                case 37:
                    M(1);
                    N(2);
                    return;
                case 38:
                    M(1);
                    N(3);
                    return;
                case 39:
                    M(1);
                    N(4);
                    return;
                default:
                    int i8 = this.f22557p;
                    if (i8 == 0) {
                        return;
                    }
                    if (b9 == 33) {
                        this.f22554m.f();
                        return;
                    }
                    switch (b9) {
                        case 44:
                            this.f22555n = Collections.emptyList();
                            int i9 = this.f22557p;
                            if (i9 != 1 && i9 != 3) {
                                return;
                            }
                            break;
                        case 45:
                            if (i8 != 1 || this.f22554m.i()) {
                                return;
                            }
                            this.f22554m.k();
                            return;
                        case 46:
                            break;
                        case 47:
                            this.f22555n = r();
                            break;
                        default:
                            return;
                    }
                    L();
                    return;
            }
        }
    }

    private void y(byte b9, byte b10) {
        int i8 = f22546y[b9 & 7];
        if ((b10 & 32) != 0) {
            i8++;
        }
        if (i8 != this.f22554m.f22568d) {
            if (this.f22557p != 1 && !this.f22554m.i()) {
                C0198a c0198a = new C0198a(this.f22557p, this.q);
                this.f22554m = c0198a;
                this.f22553l.add(c0198a);
            }
            this.f22554m.f22568d = i8;
        }
        boolean z4 = (b10 & 16) == 16;
        boolean z8 = (b10 & 1) == 1;
        int i9 = (b10 >> 1) & 7;
        this.f22554m.p(z4 ? 8 : i9, z8);
        if (z4) {
            this.f22554m.f22569e = f22547z[i9];
        }
    }

    private static boolean z(byte b9) {
        return (b9 & 224) == 0;
    }

    @Override // q5.e, p5.i
    public /* bridge */ /* synthetic */ void a(long j8) {
        super.a(j8);
    }

    @Override // q5.e
    protected h e() {
        List<b> list = this.f22555n;
        this.f22556o = list;
        return new f((List) b6.a.e(list));
    }

    /* JADX WARN: Removed duplicated region for block: B:77:0x006d A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0018 A[SYNTHETIC] */
    @Override // q5.e
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void f(p5.k r10) {
        /*
            Method dump skipped, instructions count: 261
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: q5.a.f(p5.k):void");
    }

    @Override // q5.e, l4.d
    public void flush() {
        super.flush();
        this.f22555n = null;
        this.f22556o = null;
        M(0);
        N(4);
        L();
        this.f22558r = false;
        this.f22559s = false;
        this.f22560t = (byte) 0;
        this.f22561u = (byte) 0;
        this.f22562v = 0;
        this.f22563w = true;
        this.f22564x = -9223372036854775807L;
    }

    @Override // q5.e
    public /* bridge */ /* synthetic */ k g() {
        return super.c();
    }

    @Override // q5.e, l4.d
    /* renamed from: h */
    public l b() {
        l i8;
        l b9 = super.b();
        if (b9 != null) {
            return b9;
        }
        if (!O() || (i8 = i()) == null) {
            return null;
        }
        this.f22555n = Collections.emptyList();
        this.f22564x = -9223372036854775807L;
        i8.z(j(), e(), Long.MAX_VALUE);
        return i8;
    }

    @Override // q5.e
    protected boolean k() {
        return this.f22555n != this.f22556o;
    }

    @Override // q5.e
    public /* bridge */ /* synthetic */ void l(k kVar) {
        super.d(kVar);
    }

    @Override // q5.e, l4.d
    public void release() {
    }
}
