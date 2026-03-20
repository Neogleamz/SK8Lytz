package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.LocaleList;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;
import androidx.core.content.res.h;
import java.lang.ref.WeakReference;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class p {

    /* renamed from: a  reason: collision with root package name */
    private final TextView f1548a;

    /* renamed from: b  reason: collision with root package name */
    private h0 f1549b;

    /* renamed from: c  reason: collision with root package name */
    private h0 f1550c;

    /* renamed from: d  reason: collision with root package name */
    private h0 f1551d;

    /* renamed from: e  reason: collision with root package name */
    private h0 f1552e;

    /* renamed from: f  reason: collision with root package name */
    private h0 f1553f;

    /* renamed from: g  reason: collision with root package name */
    private h0 f1554g;

    /* renamed from: h  reason: collision with root package name */
    private h0 f1555h;

    /* renamed from: i  reason: collision with root package name */
    private final q f1556i;

    /* renamed from: j  reason: collision with root package name */
    private int f1557j = 0;

    /* renamed from: k  reason: collision with root package name */
    private int f1558k = -1;

    /* renamed from: l  reason: collision with root package name */
    private Typeface f1559l;

    /* renamed from: m  reason: collision with root package name */
    private boolean f1560m;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends h.f {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ int f1561a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ int f1562b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ WeakReference f1563c;

        a(int i8, int i9, WeakReference weakReference) {
            this.f1561a = i8;
            this.f1562b = i9;
            this.f1563c = weakReference;
        }

        @Override // androidx.core.content.res.h.f
        public void h(int i8) {
        }

        @Override // androidx.core.content.res.h.f
        public void i(Typeface typeface) {
            int i8;
            if (Build.VERSION.SDK_INT >= 28 && (i8 = this.f1561a) != -1) {
                typeface = g.a(typeface, i8, (this.f1562b & 2) != 0);
            }
            p.this.n(this.f1563c, typeface);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ TextView f1565a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Typeface f1566b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ int f1567c;

        b(TextView textView, Typeface typeface, int i8) {
            this.f1565a = textView;
            this.f1566b = typeface;
            this.f1567c = i8;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f1565a.setTypeface(this.f1566b, this.f1567c);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {
        static Drawable[] a(TextView textView) {
            return textView.getCompoundDrawablesRelative();
        }

        static void b(TextView textView, Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        }

        static void c(TextView textView, Locale locale) {
            textView.setTextLocale(locale);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class d {
        static Locale a(String str) {
            return Locale.forLanguageTag(str);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class e {
        static LocaleList a(String str) {
            return LocaleList.forLanguageTags(str);
        }

        static void b(TextView textView, LocaleList localeList) {
            textView.setTextLocales(localeList);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class f {
        static int a(TextView textView) {
            return textView.getAutoSizeStepGranularity();
        }

        static void b(TextView textView, int i8, int i9, int i10, int i11) {
            textView.setAutoSizeTextTypeUniformWithConfiguration(i8, i9, i10, i11);
        }

        static void c(TextView textView, int[] iArr, int i8) {
            textView.setAutoSizeTextTypeUniformWithPresetSizes(iArr, i8);
        }

        static boolean d(TextView textView, String str) {
            return textView.setFontVariationSettings(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class g {
        static Typeface a(Typeface typeface, int i8, boolean z4) {
            return Typeface.create(typeface, i8, z4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(TextView textView) {
        this.f1548a = textView;
        this.f1556i = new q(textView);
    }

    private void B(int i8, float f5) {
        this.f1556i.w(i8, f5);
    }

    private void C(Context context, j0 j0Var) {
        String o5;
        Typeface create;
        Typeface typeface;
        this.f1557j = j0Var.k(g.j.f20036f3, this.f1557j);
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 28) {
            int k8 = j0Var.k(g.j.f20061k3, -1);
            this.f1558k = k8;
            if (k8 != -1) {
                this.f1557j = (this.f1557j & 2) | 0;
            }
        }
        int i9 = g.j.f20056j3;
        if (!j0Var.s(i9) && !j0Var.s(g.j.f20066l3)) {
            int i10 = g.j.f20031e3;
            if (j0Var.s(i10)) {
                this.f1560m = false;
                int k9 = j0Var.k(i10, 1);
                if (k9 == 1) {
                    typeface = Typeface.SANS_SERIF;
                } else if (k9 == 2) {
                    typeface = Typeface.SERIF;
                } else if (k9 != 3) {
                    return;
                } else {
                    typeface = Typeface.MONOSPACE;
                }
                this.f1559l = typeface;
                return;
            }
            return;
        }
        this.f1559l = null;
        int i11 = g.j.f20066l3;
        if (j0Var.s(i11)) {
            i9 = i11;
        }
        int i12 = this.f1558k;
        int i13 = this.f1557j;
        if (!context.isRestricted()) {
            try {
                Typeface j8 = j0Var.j(i9, this.f1557j, new a(i12, i13, new WeakReference(this.f1548a)));
                if (j8 != null) {
                    if (i8 >= 28 && this.f1558k != -1) {
                        j8 = g.a(Typeface.create(j8, 0), this.f1558k, (this.f1557j & 2) != 0);
                    }
                    this.f1559l = j8;
                }
                this.f1560m = this.f1559l == null;
            } catch (Resources.NotFoundException | UnsupportedOperationException unused) {
            }
        }
        if (this.f1559l != null || (o5 = j0Var.o(i9)) == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < 28 || this.f1558k == -1) {
            create = Typeface.create(o5, this.f1557j);
        } else {
            create = g.a(Typeface.create(o5, 0), this.f1558k, (this.f1557j & 2) != 0);
        }
        this.f1559l = create;
    }

    private void a(Drawable drawable, h0 h0Var) {
        if (drawable == null || h0Var == null) {
            return;
        }
        androidx.appcompat.widget.g.i(drawable, h0Var, this.f1548a.getDrawableState());
    }

    private static h0 d(Context context, androidx.appcompat.widget.g gVar, int i8) {
        ColorStateList f5 = gVar.f(context, i8);
        if (f5 != null) {
            h0 h0Var = new h0();
            h0Var.f1501d = true;
            h0Var.f1498a = f5;
            return h0Var;
        }
        return null;
    }

    private void y(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4, Drawable drawable5, Drawable drawable6) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 17 && (drawable5 != null || drawable6 != null)) {
            Drawable[] a9 = c.a(this.f1548a);
            TextView textView = this.f1548a;
            if (drawable5 == null) {
                drawable5 = a9[0];
            }
            if (drawable2 == null) {
                drawable2 = a9[1];
            }
            if (drawable6 == null) {
                drawable6 = a9[2];
            }
            if (drawable4 == null) {
                drawable4 = a9[3];
            }
            c.b(textView, drawable5, drawable2, drawable6, drawable4);
        } else if (drawable == null && drawable2 == null && drawable3 == null && drawable4 == null) {
        } else {
            if (i8 >= 17) {
                Drawable[] a10 = c.a(this.f1548a);
                if (a10[0] != null || a10[2] != null) {
                    TextView textView2 = this.f1548a;
                    Drawable drawable7 = a10[0];
                    if (drawable2 == null) {
                        drawable2 = a10[1];
                    }
                    Drawable drawable8 = a10[2];
                    if (drawable4 == null) {
                        drawable4 = a10[3];
                    }
                    c.b(textView2, drawable7, drawable2, drawable8, drawable4);
                    return;
                }
            }
            Drawable[] compoundDrawables = this.f1548a.getCompoundDrawables();
            TextView textView3 = this.f1548a;
            if (drawable == null) {
                drawable = compoundDrawables[0];
            }
            if (drawable2 == null) {
                drawable2 = compoundDrawables[1];
            }
            if (drawable3 == null) {
                drawable3 = compoundDrawables[2];
            }
            if (drawable4 == null) {
                drawable4 = compoundDrawables[3];
            }
            textView3.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        }
    }

    private void z() {
        h0 h0Var = this.f1555h;
        this.f1549b = h0Var;
        this.f1550c = h0Var;
        this.f1551d = h0Var;
        this.f1552e = h0Var;
        this.f1553f = h0Var;
        this.f1554g = h0Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A(int i8, float f5) {
        if (u0.f1636b || l()) {
            return;
        }
        B(i8, f5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        if (this.f1549b != null || this.f1550c != null || this.f1551d != null || this.f1552e != null) {
            Drawable[] compoundDrawables = this.f1548a.getCompoundDrawables();
            a(compoundDrawables[0], this.f1549b);
            a(compoundDrawables[1], this.f1550c);
            a(compoundDrawables[2], this.f1551d);
            a(compoundDrawables[3], this.f1552e);
        }
        if (Build.VERSION.SDK_INT >= 17) {
            if (this.f1553f == null && this.f1554g == null) {
                return;
            }
            Drawable[] a9 = c.a(this.f1548a);
            a(a9[0], this.f1553f);
            a(a9[2], this.f1554g);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c() {
        this.f1556i.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int e() {
        return this.f1556i.h();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int f() {
        return this.f1556i.i();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int g() {
        return this.f1556i.j();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] h() {
        return this.f1556i.k();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int i() {
        return this.f1556i.l();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList j() {
        h0 h0Var = this.f1555h;
        if (h0Var != null) {
            return h0Var.f1498a;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PorterDuff.Mode k() {
        h0 h0Var = this.f1555h;
        if (h0Var != null) {
            return h0Var.f1499b;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean l() {
        return this.f1556i.q();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:101:0x01ce  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x01e2  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x01e9  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0218  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0229  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x026c  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0275  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x027b  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0284  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x028a  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0293  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0299  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x02a2  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x02a8  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x02b1  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x02b7  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x02cb  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x02dc  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x02ec  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0304  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x030b  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0312  */
    /* JADX WARN: Removed duplicated region for block: B:163:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0109  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0172  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x017a  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x018d  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x01b7  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01be  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x01c5 A[ADDED_TO_REGION] */
    @android.annotation.SuppressLint({"NewApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void m(android.util.AttributeSet r24, int r25) {
        /*
            Method dump skipped, instructions count: 792
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.p.m(android.util.AttributeSet, int):void");
    }

    void n(WeakReference<TextView> weakReference, Typeface typeface) {
        if (this.f1560m) {
            this.f1559l = typeface;
            TextView textView = weakReference.get();
            if (textView != null) {
                if (androidx.core.view.c0.V(textView)) {
                    textView.post(new b(textView, typeface, this.f1557j));
                } else {
                    textView.setTypeface(typeface, this.f1557j);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(boolean z4, int i8, int i9, int i10, int i11) {
        if (u0.f1636b) {
            return;
        }
        c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p() {
        b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(Context context, int i8) {
        String o5;
        ColorStateList c9;
        ColorStateList c10;
        ColorStateList c11;
        j0 t8 = j0.t(context, i8, g.j.f20019c3);
        int i9 = g.j.f20076n3;
        if (t8.s(i9)) {
            s(t8.a(i9, false));
        }
        int i10 = Build.VERSION.SDK_INT;
        if (i10 < 23) {
            int i11 = g.j.f20041g3;
            if (t8.s(i11) && (c11 = t8.c(i11)) != null) {
                this.f1548a.setTextColor(c11);
            }
            int i12 = g.j.f20051i3;
            if (t8.s(i12) && (c10 = t8.c(i12)) != null) {
                this.f1548a.setLinkTextColor(c10);
            }
            int i13 = g.j.f20046h3;
            if (t8.s(i13) && (c9 = t8.c(i13)) != null) {
                this.f1548a.setHintTextColor(c9);
            }
        }
        int i14 = g.j.f20025d3;
        if (t8.s(i14) && t8.f(i14, -1) == 0) {
            this.f1548a.setTextSize(0, 0.0f);
        }
        C(context, t8);
        if (i10 >= 26) {
            int i15 = g.j.f20071m3;
            if (t8.s(i15) && (o5 = t8.o(i15)) != null) {
                f.d(this.f1548a, o5);
            }
        }
        t8.w();
        Typeface typeface = this.f1559l;
        if (typeface != null) {
            this.f1548a.setTypeface(typeface, this.f1557j);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(TextView textView, InputConnection inputConnection, EditorInfo editorInfo) {
        if (Build.VERSION.SDK_INT >= 30 || inputConnection == null) {
            return;
        }
        u0.a.f(editorInfo, textView.getText());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(boolean z4) {
        this.f1548a.setAllCaps(z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t(int i8, int i9, int i10, int i11) {
        this.f1556i.s(i8, i9, i10, i11);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u(int[] iArr, int i8) {
        this.f1556i.t(iArr, i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v(int i8) {
        this.f1556i.u(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w(ColorStateList colorStateList) {
        if (this.f1555h == null) {
            this.f1555h = new h0();
        }
        h0 h0Var = this.f1555h;
        h0Var.f1498a = colorStateList;
        h0Var.f1501d = colorStateList != null;
        z();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x(PorterDuff.Mode mode) {
        if (this.f1555h == null) {
            this.f1555h = new h0();
        }
        h0 h0Var = this.f1555h;
        h0Var.f1499b = mode;
        h0Var.f1500c = mode != null;
        z();
    }
}
