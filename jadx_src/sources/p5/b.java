package p5;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import b6.l0;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b implements com.google.android.exoplayer2.g {

    /* renamed from: a  reason: collision with root package name */
    public final CharSequence f22374a;

    /* renamed from: b  reason: collision with root package name */
    public final Layout.Alignment f22375b;

    /* renamed from: c  reason: collision with root package name */
    public final Layout.Alignment f22376c;

    /* renamed from: d  reason: collision with root package name */
    public final Bitmap f22377d;

    /* renamed from: e  reason: collision with root package name */
    public final float f22378e;

    /* renamed from: f  reason: collision with root package name */
    public final int f22379f;

    /* renamed from: g  reason: collision with root package name */
    public final int f22380g;

    /* renamed from: h  reason: collision with root package name */
    public final float f22381h;

    /* renamed from: j  reason: collision with root package name */
    public final int f22382j;

    /* renamed from: k  reason: collision with root package name */
    public final float f22383k;

    /* renamed from: l  reason: collision with root package name */
    public final float f22384l;

    /* renamed from: m  reason: collision with root package name */
    public final boolean f22385m;

    /* renamed from: n  reason: collision with root package name */
    public final int f22386n;

    /* renamed from: p  reason: collision with root package name */
    public final int f22387p;
    public final float q;

    /* renamed from: t  reason: collision with root package name */
    public final int f22388t;

    /* renamed from: w  reason: collision with root package name */
    public final float f22389w;

    /* renamed from: x  reason: collision with root package name */
    public static final b f22371x = new C0196b().o(BuildConfig.FLAVOR).a();

    /* renamed from: y  reason: collision with root package name */
    private static final String f22372y = l0.r0(0);

    /* renamed from: z  reason: collision with root package name */
    private static final String f22373z = l0.r0(1);
    private static final String A = l0.r0(2);
    private static final String B = l0.r0(3);
    private static final String C = l0.r0(4);
    private static final String E = l0.r0(5);
    private static final String F = l0.r0(6);
    private static final String G = l0.r0(7);
    private static final String H = l0.r0(8);
    private static final String K = l0.r0(9);
    private static final String L = l0.r0(10);
    private static final String O = l0.r0(11);
    private static final String P = l0.r0(12);
    private static final String Q = l0.r0(13);
    private static final String R = l0.r0(14);
    private static final String T = l0.r0(15);
    private static final String W = l0.r0(16);
    public static final g.a<b> X = p5.a.a;

    /* renamed from: p5.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0196b {

        /* renamed from: a  reason: collision with root package name */
        private CharSequence f22390a;

        /* renamed from: b  reason: collision with root package name */
        private Bitmap f22391b;

        /* renamed from: c  reason: collision with root package name */
        private Layout.Alignment f22392c;

        /* renamed from: d  reason: collision with root package name */
        private Layout.Alignment f22393d;

        /* renamed from: e  reason: collision with root package name */
        private float f22394e;

        /* renamed from: f  reason: collision with root package name */
        private int f22395f;

        /* renamed from: g  reason: collision with root package name */
        private int f22396g;

        /* renamed from: h  reason: collision with root package name */
        private float f22397h;

        /* renamed from: i  reason: collision with root package name */
        private int f22398i;

        /* renamed from: j  reason: collision with root package name */
        private int f22399j;

        /* renamed from: k  reason: collision with root package name */
        private float f22400k;

        /* renamed from: l  reason: collision with root package name */
        private float f22401l;

        /* renamed from: m  reason: collision with root package name */
        private float f22402m;

        /* renamed from: n  reason: collision with root package name */
        private boolean f22403n;

        /* renamed from: o  reason: collision with root package name */
        private int f22404o;

        /* renamed from: p  reason: collision with root package name */
        private int f22405p;
        private float q;

        public C0196b() {
            this.f22390a = null;
            this.f22391b = null;
            this.f22392c = null;
            this.f22393d = null;
            this.f22394e = -3.4028235E38f;
            this.f22395f = Integer.MIN_VALUE;
            this.f22396g = Integer.MIN_VALUE;
            this.f22397h = -3.4028235E38f;
            this.f22398i = Integer.MIN_VALUE;
            this.f22399j = Integer.MIN_VALUE;
            this.f22400k = -3.4028235E38f;
            this.f22401l = -3.4028235E38f;
            this.f22402m = -3.4028235E38f;
            this.f22403n = false;
            this.f22404o = -16777216;
            this.f22405p = Integer.MIN_VALUE;
        }

        private C0196b(b bVar) {
            this.f22390a = bVar.f22374a;
            this.f22391b = bVar.f22377d;
            this.f22392c = bVar.f22375b;
            this.f22393d = bVar.f22376c;
            this.f22394e = bVar.f22378e;
            this.f22395f = bVar.f22379f;
            this.f22396g = bVar.f22380g;
            this.f22397h = bVar.f22381h;
            this.f22398i = bVar.f22382j;
            this.f22399j = bVar.f22387p;
            this.f22400k = bVar.q;
            this.f22401l = bVar.f22383k;
            this.f22402m = bVar.f22384l;
            this.f22403n = bVar.f22385m;
            this.f22404o = bVar.f22386n;
            this.f22405p = bVar.f22388t;
            this.q = bVar.f22389w;
        }

        public b a() {
            return new b(this.f22390a, this.f22392c, this.f22393d, this.f22391b, this.f22394e, this.f22395f, this.f22396g, this.f22397h, this.f22398i, this.f22399j, this.f22400k, this.f22401l, this.f22402m, this.f22403n, this.f22404o, this.f22405p, this.q);
        }

        public C0196b b() {
            this.f22403n = false;
            return this;
        }

        public int c() {
            return this.f22396g;
        }

        public int d() {
            return this.f22398i;
        }

        public CharSequence e() {
            return this.f22390a;
        }

        public C0196b f(Bitmap bitmap) {
            this.f22391b = bitmap;
            return this;
        }

        public C0196b g(float f5) {
            this.f22402m = f5;
            return this;
        }

        public C0196b h(float f5, int i8) {
            this.f22394e = f5;
            this.f22395f = i8;
            return this;
        }

        public C0196b i(int i8) {
            this.f22396g = i8;
            return this;
        }

        public C0196b j(Layout.Alignment alignment) {
            this.f22393d = alignment;
            return this;
        }

        public C0196b k(float f5) {
            this.f22397h = f5;
            return this;
        }

        public C0196b l(int i8) {
            this.f22398i = i8;
            return this;
        }

        public C0196b m(float f5) {
            this.q = f5;
            return this;
        }

        public C0196b n(float f5) {
            this.f22401l = f5;
            return this;
        }

        public C0196b o(CharSequence charSequence) {
            this.f22390a = charSequence;
            return this;
        }

        public C0196b p(Layout.Alignment alignment) {
            this.f22392c = alignment;
            return this;
        }

        public C0196b q(float f5, int i8) {
            this.f22400k = f5;
            this.f22399j = i8;
            return this;
        }

        public C0196b r(int i8) {
            this.f22405p = i8;
            return this;
        }

        public C0196b s(int i8) {
            this.f22404o = i8;
            this.f22403n = true;
            return this;
        }
    }

    private b(CharSequence charSequence, Layout.Alignment alignment, Layout.Alignment alignment2, Bitmap bitmap, float f5, int i8, int i9, float f8, int i10, int i11, float f9, float f10, float f11, boolean z4, int i12, int i13, float f12) {
        if (charSequence == null) {
            b6.a.e(bitmap);
        } else {
            b6.a.a(bitmap == null);
        }
        this.f22374a = charSequence instanceof Spanned ? SpannedString.valueOf(charSequence) : charSequence != null ? charSequence.toString() : null;
        this.f22375b = alignment;
        this.f22376c = alignment2;
        this.f22377d = bitmap;
        this.f22378e = f5;
        this.f22379f = i8;
        this.f22380g = i9;
        this.f22381h = f8;
        this.f22382j = i10;
        this.f22383k = f10;
        this.f22384l = f11;
        this.f22385m = z4;
        this.f22386n = i12;
        this.f22387p = i11;
        this.q = f9;
        this.f22388t = i13;
        this.f22389w = f12;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final b c(Bundle bundle) {
        C0196b c0196b = new C0196b();
        CharSequence charSequence = bundle.getCharSequence(f22372y);
        if (charSequence != null) {
            c0196b.o(charSequence);
        }
        Layout.Alignment alignment = (Layout.Alignment) bundle.getSerializable(f22373z);
        if (alignment != null) {
            c0196b.p(alignment);
        }
        Layout.Alignment alignment2 = (Layout.Alignment) bundle.getSerializable(A);
        if (alignment2 != null) {
            c0196b.j(alignment2);
        }
        Bitmap bitmap = (Bitmap) bundle.getParcelable(B);
        if (bitmap != null) {
            c0196b.f(bitmap);
        }
        String str = C;
        if (bundle.containsKey(str)) {
            String str2 = E;
            if (bundle.containsKey(str2)) {
                c0196b.h(bundle.getFloat(str), bundle.getInt(str2));
            }
        }
        String str3 = F;
        if (bundle.containsKey(str3)) {
            c0196b.i(bundle.getInt(str3));
        }
        String str4 = G;
        if (bundle.containsKey(str4)) {
            c0196b.k(bundle.getFloat(str4));
        }
        String str5 = H;
        if (bundle.containsKey(str5)) {
            c0196b.l(bundle.getInt(str5));
        }
        String str6 = L;
        if (bundle.containsKey(str6)) {
            String str7 = K;
            if (bundle.containsKey(str7)) {
                c0196b.q(bundle.getFloat(str6), bundle.getInt(str7));
            }
        }
        String str8 = O;
        if (bundle.containsKey(str8)) {
            c0196b.n(bundle.getFloat(str8));
        }
        String str9 = P;
        if (bundle.containsKey(str9)) {
            c0196b.g(bundle.getFloat(str9));
        }
        String str10 = Q;
        if (bundle.containsKey(str10)) {
            c0196b.s(bundle.getInt(str10));
        }
        if (!bundle.getBoolean(R, false)) {
            c0196b.b();
        }
        String str11 = T;
        if (bundle.containsKey(str11)) {
            c0196b.r(bundle.getInt(str11));
        }
        String str12 = W;
        if (bundle.containsKey(str12)) {
            c0196b.m(bundle.getFloat(str12));
        }
        return c0196b.a();
    }

    public C0196b b() {
        return new C0196b();
    }

    public boolean equals(Object obj) {
        Bitmap bitmap;
        Bitmap bitmap2;
        if (this == obj) {
            return true;
        }
        if (obj == null || b.class != obj.getClass()) {
            return false;
        }
        b bVar = (b) obj;
        return TextUtils.equals(this.f22374a, bVar.f22374a) && this.f22375b == bVar.f22375b && this.f22376c == bVar.f22376c && ((bitmap = this.f22377d) != null ? !((bitmap2 = bVar.f22377d) == null || !bitmap.sameAs(bitmap2)) : bVar.f22377d == null) && this.f22378e == bVar.f22378e && this.f22379f == bVar.f22379f && this.f22380g == bVar.f22380g && this.f22381h == bVar.f22381h && this.f22382j == bVar.f22382j && this.f22383k == bVar.f22383k && this.f22384l == bVar.f22384l && this.f22385m == bVar.f22385m && this.f22386n == bVar.f22386n && this.f22387p == bVar.f22387p && this.q == bVar.q && this.f22388t == bVar.f22388t && this.f22389w == bVar.f22389w;
    }

    public int hashCode() {
        return com.google.common.base.k.b(this.f22374a, this.f22375b, this.f22376c, this.f22377d, Float.valueOf(this.f22378e), Integer.valueOf(this.f22379f), Integer.valueOf(this.f22380g), Float.valueOf(this.f22381h), Integer.valueOf(this.f22382j), Float.valueOf(this.f22383k), Float.valueOf(this.f22384l), Boolean.valueOf(this.f22385m), Integer.valueOf(this.f22386n), Integer.valueOf(this.f22387p), Float.valueOf(this.q), Integer.valueOf(this.f22388t), Float.valueOf(this.f22389w));
    }
}
