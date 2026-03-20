package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z {

    /* renamed from: i  reason: collision with root package name */
    private static z f1652i;

    /* renamed from: a  reason: collision with root package name */
    private WeakHashMap<Context, k0.h<ColorStateList>> f1654a;

    /* renamed from: b  reason: collision with root package name */
    private k0.g<String, e> f1655b;

    /* renamed from: c  reason: collision with root package name */
    private k0.h<String> f1656c;

    /* renamed from: d  reason: collision with root package name */
    private final WeakHashMap<Context, k0.d<WeakReference<Drawable.ConstantState>>> f1657d = new WeakHashMap<>(0);

    /* renamed from: e  reason: collision with root package name */
    private TypedValue f1658e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f1659f;

    /* renamed from: g  reason: collision with root package name */
    private f f1660g;

    /* renamed from: h  reason: collision with root package name */
    private static final PorterDuff.Mode f1651h = PorterDuff.Mode.SRC_IN;

    /* renamed from: j  reason: collision with root package name */
    private static final c f1653j = new c(6);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a implements e {
        a() {
        }

        @Override // androidx.appcompat.widget.z.e
        public Drawable a(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
            try {
                return i.a.m(context, context.getResources(), xmlPullParser, attributeSet, theme);
            } catch (Exception e8) {
                Log.e("AsldcInflateDelegate", "Exception while inflating <animated-selector>", e8);
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b implements e {
        b() {
        }

        @Override // androidx.appcompat.widget.z.e
        public Drawable a(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
            try {
                return androidx.vectordrawable.graphics.drawable.c.a(context, context.getResources(), xmlPullParser, attributeSet, theme);
            } catch (Exception e8) {
                Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", e8);
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c extends k0.e<Integer, PorterDuffColorFilter> {
        public c(int i8) {
            super(i8);
        }

        private static int j(int i8, PorterDuff.Mode mode) {
            return ((i8 + 31) * 31) + mode.hashCode();
        }

        PorterDuffColorFilter k(int i8, PorterDuff.Mode mode) {
            return c(Integer.valueOf(j(i8, mode)));
        }

        PorterDuffColorFilter l(int i8, PorterDuff.Mode mode, PorterDuffColorFilter porterDuffColorFilter) {
            return d(Integer.valueOf(j(i8, mode)), porterDuffColorFilter);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d implements e {
        d() {
        }

        @Override // androidx.appcompat.widget.z.e
        public Drawable a(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
            String classAttribute = attributeSet.getClassAttribute();
            if (classAttribute != null) {
                try {
                    Drawable drawable = (Drawable) d.class.getClassLoader().loadClass(classAttribute).asSubclass(Drawable.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                    if (Build.VERSION.SDK_INT >= 21) {
                        j.c.c(drawable, context.getResources(), xmlPullParser, attributeSet, theme);
                    } else {
                        drawable.inflate(context.getResources(), xmlPullParser, attributeSet);
                    }
                    return drawable;
                } catch (Exception e8) {
                    Log.e("DrawableDelegate", "Exception while inflating <drawable>", e8);
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface e {
        Drawable a(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f {
        boolean a(Context context, int i8, Drawable drawable);

        PorterDuff.Mode b(int i8);

        Drawable c(z zVar, Context context, int i8);

        ColorStateList d(Context context, int i8);

        boolean e(Context context, int i8, Drawable drawable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class g implements e {
        g() {
        }

        @Override // androidx.appcompat.widget.z.e
        public Drawable a(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
            try {
                return androidx.vectordrawable.graphics.drawable.i.c(context.getResources(), xmlPullParser, attributeSet, theme);
            } catch (Exception e8) {
                Log.e("VdcInflateDelegate", "Exception while inflating <vector>", e8);
                return null;
            }
        }
    }

    private void a(String str, e eVar) {
        if (this.f1655b == null) {
            this.f1655b = new k0.g<>();
        }
        this.f1655b.put(str, eVar);
    }

    private synchronized boolean b(Context context, long j8, Drawable drawable) {
        boolean z4;
        Drawable.ConstantState constantState = drawable.getConstantState();
        if (constantState != null) {
            k0.d<WeakReference<Drawable.ConstantState>> dVar = this.f1657d.get(context);
            if (dVar == null) {
                dVar = new k0.d<>();
                this.f1657d.put(context, dVar);
            }
            dVar.l(j8, new WeakReference<>(constantState));
            z4 = true;
        } else {
            z4 = false;
        }
        return z4;
    }

    private void c(Context context, int i8, ColorStateList colorStateList) {
        if (this.f1654a == null) {
            this.f1654a = new WeakHashMap<>();
        }
        k0.h<ColorStateList> hVar = this.f1654a.get(context);
        if (hVar == null) {
            hVar = new k0.h<>();
            this.f1654a.put(context, hVar);
        }
        hVar.b(i8, colorStateList);
    }

    private void d(Context context) {
        if (this.f1659f) {
            return;
        }
        this.f1659f = true;
        Drawable j8 = j(context, j.d.f20570a);
        if (j8 == null || !q(j8)) {
            this.f1659f = false;
            throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
        }
    }

    private static long e(TypedValue typedValue) {
        return (typedValue.assetCookie << 32) | typedValue.data;
    }

    private Drawable f(Context context, int i8) {
        if (this.f1658e == null) {
            this.f1658e = new TypedValue();
        }
        TypedValue typedValue = this.f1658e;
        context.getResources().getValue(i8, typedValue, true);
        long e8 = e(typedValue);
        Drawable i9 = i(context, e8);
        if (i9 != null) {
            return i9;
        }
        f fVar = this.f1660g;
        Drawable c9 = fVar == null ? null : fVar.c(this, context, i8);
        if (c9 != null) {
            c9.setChangingConfigurations(typedValue.changingConfigurations);
            b(context, e8, c9);
        }
        return c9;
    }

    private static PorterDuffColorFilter g(ColorStateList colorStateList, PorterDuff.Mode mode, int[] iArr) {
        if (colorStateList == null || mode == null) {
            return null;
        }
        return l(colorStateList.getColorForState(iArr, 0), mode);
    }

    public static synchronized z h() {
        z zVar;
        synchronized (z.class) {
            if (f1652i == null) {
                z zVar2 = new z();
                f1652i = zVar2;
                p(zVar2);
            }
            zVar = f1652i;
        }
        return zVar;
    }

    private synchronized Drawable i(Context context, long j8) {
        k0.d<WeakReference<Drawable.ConstantState>> dVar = this.f1657d.get(context);
        if (dVar == null) {
            return null;
        }
        WeakReference<Drawable.ConstantState> f5 = dVar.f(j8);
        if (f5 != null) {
            Drawable.ConstantState constantState = f5.get();
            if (constantState != null) {
                return constantState.newDrawable(context.getResources());
            }
            dVar.m(j8);
        }
        return null;
    }

    public static synchronized PorterDuffColorFilter l(int i8, PorterDuff.Mode mode) {
        PorterDuffColorFilter k8;
        synchronized (z.class) {
            c cVar = f1653j;
            k8 = cVar.k(i8, mode);
            if (k8 == null) {
                k8 = new PorterDuffColorFilter(i8, mode);
                cVar.l(i8, mode, k8);
            }
        }
        return k8;
    }

    private ColorStateList n(Context context, int i8) {
        k0.h<ColorStateList> hVar;
        WeakHashMap<Context, k0.h<ColorStateList>> weakHashMap = this.f1654a;
        if (weakHashMap == null || (hVar = weakHashMap.get(context)) == null) {
            return null;
        }
        return hVar.f(i8);
    }

    private static void p(z zVar) {
        if (Build.VERSION.SDK_INT < 24) {
            zVar.a("vector", new g());
            zVar.a("animated-vector", new b());
            zVar.a("animated-selector", new a());
            zVar.a("drawable", new d());
        }
    }

    private static boolean q(Drawable drawable) {
        return (drawable instanceof androidx.vectordrawable.graphics.drawable.i) || "android.graphics.drawable.VectorDrawable".equals(drawable.getClass().getName());
    }

    private Drawable r(Context context, int i8) {
        int next;
        k0.g<String, e> gVar = this.f1655b;
        if (gVar == null || gVar.isEmpty()) {
            return null;
        }
        k0.h<String> hVar = this.f1656c;
        if (hVar != null) {
            String f5 = hVar.f(i8);
            if ("appcompat_skip_skip".equals(f5) || (f5 != null && this.f1655b.get(f5) == null)) {
                return null;
            }
        } else {
            this.f1656c = new k0.h<>();
        }
        if (this.f1658e == null) {
            this.f1658e = new TypedValue();
        }
        TypedValue typedValue = this.f1658e;
        Resources resources = context.getResources();
        resources.getValue(i8, typedValue, true);
        long e8 = e(typedValue);
        Drawable i9 = i(context, e8);
        if (i9 != null) {
            return i9;
        }
        CharSequence charSequence = typedValue.string;
        if (charSequence != null && charSequence.toString().endsWith(".xml")) {
            try {
                XmlResourceParser xml = resources.getXml(i8);
                AttributeSet asAttributeSet = Xml.asAttributeSet(xml);
                while (true) {
                    next = xml.next();
                    if (next == 2 || next == 1) {
                        break;
                    }
                }
                if (next != 2) {
                    throw new XmlPullParserException("No start tag found");
                }
                String name = xml.getName();
                this.f1656c.b(i8, name);
                e eVar = this.f1655b.get(name);
                if (eVar != null) {
                    i9 = eVar.a(context, xml, asAttributeSet, context.getTheme());
                }
                if (i9 != null) {
                    i9.setChangingConfigurations(typedValue.changingConfigurations);
                    b(context, e8, i9);
                }
            } catch (Exception e9) {
                Log.e("ResourceManagerInternal", "Exception while inflating drawable", e9);
            }
        }
        if (i9 == null) {
            this.f1656c.b(i8, "appcompat_skip_skip");
        }
        return i9;
    }

    private Drawable v(Context context, int i8, boolean z4, Drawable drawable) {
        ColorStateList m8 = m(context, i8);
        if (m8 == null) {
            f fVar = this.f1660g;
            if ((fVar == null || !fVar.e(context, i8, drawable)) && !x(context, i8, drawable) && z4) {
                return null;
            }
            return drawable;
        }
        if (t.a(drawable)) {
            drawable = drawable.mutate();
        }
        Drawable r4 = androidx.core.graphics.drawable.a.r(drawable);
        androidx.core.graphics.drawable.a.o(r4, m8);
        PorterDuff.Mode o5 = o(i8);
        if (o5 != null) {
            androidx.core.graphics.drawable.a.p(r4, o5);
            return r4;
        }
        return r4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void w(Drawable drawable, h0 h0Var, int[] iArr) {
        int[] state = drawable.getState();
        if (t.a(drawable)) {
            if (!(drawable.mutate() == drawable)) {
                Log.d("ResourceManagerInternal", "Mutated drawable is not the same instance as the input.");
                return;
            }
        }
        if ((drawable instanceof LayerDrawable) && drawable.isStateful()) {
            drawable.setState(new int[0]);
            drawable.setState(state);
        }
        boolean z4 = h0Var.f1501d;
        if (z4 || h0Var.f1500c) {
            drawable.setColorFilter(g(z4 ? h0Var.f1498a : null, h0Var.f1500c ? h0Var.f1499b : f1651h, iArr));
        } else {
            drawable.clearColorFilter();
        }
        if (Build.VERSION.SDK_INT <= 23) {
            drawable.invalidateSelf();
        }
    }

    public synchronized Drawable j(Context context, int i8) {
        return k(context, i8, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Drawable k(Context context, int i8, boolean z4) {
        Drawable r4;
        d(context);
        r4 = r(context, i8);
        if (r4 == null) {
            r4 = f(context, i8);
        }
        if (r4 == null) {
            r4 = androidx.core.content.a.f(context, i8);
        }
        if (r4 != null) {
            r4 = v(context, i8, z4, r4);
        }
        if (r4 != null) {
            t.b(r4);
        }
        return r4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ColorStateList m(Context context, int i8) {
        ColorStateList n8;
        n8 = n(context, i8);
        if (n8 == null) {
            f fVar = this.f1660g;
            n8 = fVar == null ? null : fVar.d(context, i8);
            if (n8 != null) {
                c(context, i8, n8);
            }
        }
        return n8;
    }

    PorterDuff.Mode o(int i8) {
        f fVar = this.f1660g;
        if (fVar == null) {
            return null;
        }
        return fVar.b(i8);
    }

    public synchronized void s(Context context) {
        k0.d<WeakReference<Drawable.ConstantState>> dVar = this.f1657d.get(context);
        if (dVar != null) {
            dVar.c();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Drawable t(Context context, t0 t0Var, int i8) {
        Drawable r4 = r(context, i8);
        if (r4 == null) {
            r4 = t0Var.a(i8);
        }
        if (r4 != null) {
            return v(context, i8, false, r4);
        }
        return null;
    }

    public synchronized void u(f fVar) {
        this.f1660g = fVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean x(Context context, int i8, Drawable drawable) {
        f fVar = this.f1660g;
        return fVar != null && fVar.a(context, i8, drawable);
    }
}
