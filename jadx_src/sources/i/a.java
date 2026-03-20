package i;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.StateSet;
import androidx.appcompat.widget.z;
import androidx.core.content.res.k;
import androidx.vectordrawable.graphics.drawable.i;
import i.b;
import i.e;
import k0.h;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends i.e implements androidx.core.graphics.drawable.b {

    /* renamed from: z  reason: collision with root package name */
    private static final String f20347z = a.class.getSimpleName();
    private c q;

    /* renamed from: t  reason: collision with root package name */
    private g f20348t;

    /* renamed from: w  reason: collision with root package name */
    private int f20349w;

    /* renamed from: x  reason: collision with root package name */
    private int f20350x;

    /* renamed from: y  reason: collision with root package name */
    private boolean f20351y;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends g {

        /* renamed from: a  reason: collision with root package name */
        private final Animatable f20352a;

        b(Animatable animatable) {
            super();
            this.f20352a = animatable;
        }

        @Override // i.a.g
        public void c() {
            this.f20352a.start();
        }

        @Override // i.a.g
        public void d() {
            this.f20352a.stop();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c extends e.a {
        k0.d<Long> K;
        h<Integer> L;

        c(c cVar, a aVar, Resources resources) {
            super(cVar, aVar, resources);
            h<Integer> hVar;
            if (cVar != null) {
                this.K = cVar.K;
                hVar = cVar.L;
            } else {
                this.K = new k0.d<>();
                hVar = new h<>();
            }
            this.L = hVar;
        }

        private static long D(int i8, int i9) {
            return i9 | (i8 << 32);
        }

        int B(int[] iArr, Drawable drawable, int i8) {
            int z4 = super.z(iArr, drawable);
            this.L.l(z4, Integer.valueOf(i8));
            return z4;
        }

        int C(int i8, int i9, Drawable drawable, boolean z4) {
            int a9 = super.a(drawable);
            long D = D(i8, i9);
            long j8 = z4 ? 8589934592L : 0L;
            long j9 = a9;
            this.K.b(D, Long.valueOf(j9 | j8));
            if (z4) {
                this.K.b(D(i9, i8), Long.valueOf(4294967296L | j9 | j8));
            }
            return a9;
        }

        int E(int i8) {
            if (i8 < 0) {
                return 0;
            }
            return this.L.g(i8, 0).intValue();
        }

        int F(int[] iArr) {
            int A = super.A(iArr);
            return A >= 0 ? A : super.A(StateSet.WILD_CARD);
        }

        int G(int i8, int i9) {
            return (int) this.K.g(D(i8, i9), -1L).longValue();
        }

        boolean H(int i8, int i9) {
            return (this.K.g(D(i8, i9), -1L).longValue() & 4294967296L) != 0;
        }

        boolean I(int i8, int i9) {
            return (this.K.g(D(i8, i9), -1L).longValue() & 8589934592L) != 0;
        }

        @Override // i.e.a, android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new a(this, null);
        }

        @Override // i.e.a, android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources) {
            return new a(this, resources);
        }

        @Override // i.e.a, i.b.d
        void r() {
            this.K = this.K.clone();
            this.L = this.L.clone();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d extends g {

        /* renamed from: a  reason: collision with root package name */
        private final androidx.vectordrawable.graphics.drawable.c f20353a;

        d(androidx.vectordrawable.graphics.drawable.c cVar) {
            super();
            this.f20353a = cVar;
        }

        @Override // i.a.g
        public void c() {
            this.f20353a.start();
        }

        @Override // i.a.g
        public void d() {
            this.f20353a.stop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e extends g {

        /* renamed from: a  reason: collision with root package name */
        private final ObjectAnimator f20354a;

        /* renamed from: b  reason: collision with root package name */
        private final boolean f20355b;

        e(AnimationDrawable animationDrawable, boolean z4, boolean z8) {
            super();
            int numberOfFrames = animationDrawable.getNumberOfFrames();
            int i8 = z4 ? numberOfFrames - 1 : 0;
            int i9 = z4 ? 0 : numberOfFrames - 1;
            f fVar = new f(animationDrawable, z4);
            ObjectAnimator ofInt = ObjectAnimator.ofInt(animationDrawable, "currentIndex", i8, i9);
            if (Build.VERSION.SDK_INT >= 18) {
                j.b.a(ofInt, true);
            }
            ofInt.setDuration(fVar.a());
            ofInt.setInterpolator(fVar);
            this.f20355b = z8;
            this.f20354a = ofInt;
        }

        @Override // i.a.g
        public boolean a() {
            return this.f20355b;
        }

        @Override // i.a.g
        public void b() {
            this.f20354a.reverse();
        }

        @Override // i.a.g
        public void c() {
            this.f20354a.start();
        }

        @Override // i.a.g
        public void d() {
            this.f20354a.cancel();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class f implements TimeInterpolator {

        /* renamed from: a  reason: collision with root package name */
        private int[] f20356a;

        /* renamed from: b  reason: collision with root package name */
        private int f20357b;

        /* renamed from: c  reason: collision with root package name */
        private int f20358c;

        f(AnimationDrawable animationDrawable, boolean z4) {
            b(animationDrawable, z4);
        }

        int a() {
            return this.f20358c;
        }

        int b(AnimationDrawable animationDrawable, boolean z4) {
            int numberOfFrames = animationDrawable.getNumberOfFrames();
            this.f20357b = numberOfFrames;
            int[] iArr = this.f20356a;
            if (iArr == null || iArr.length < numberOfFrames) {
                this.f20356a = new int[numberOfFrames];
            }
            int[] iArr2 = this.f20356a;
            int i8 = 0;
            for (int i9 = 0; i9 < numberOfFrames; i9++) {
                int duration = animationDrawable.getDuration(z4 ? (numberOfFrames - i9) - 1 : i9);
                iArr2[i9] = duration;
                i8 += duration;
            }
            this.f20358c = i8;
            return i8;
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f5) {
            int i8 = (int) ((f5 * this.f20358c) + 0.5f);
            int i9 = this.f20357b;
            int[] iArr = this.f20356a;
            int i10 = 0;
            while (i10 < i9 && i8 >= iArr[i10]) {
                i8 -= iArr[i10];
                i10++;
            }
            return (i10 / i9) + (i10 < i9 ? i8 / this.f20358c : 0.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class g {
        private g() {
        }

        public boolean a() {
            return false;
        }

        public void b() {
        }

        public abstract void c();

        public abstract void d();
    }

    public a() {
        this(null, null);
    }

    a(c cVar, Resources resources) {
        super(null);
        this.f20349w = -1;
        this.f20350x = -1;
        h(new c(cVar, this, resources));
        onStateChange(getState());
        jumpToCurrentState();
    }

    public static a m(Context context, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        String name = xmlPullParser.getName();
        if (name.equals("animated-selector")) {
            a aVar = new a();
            aVar.n(context, resources, xmlPullParser, attributeSet, theme);
            return aVar;
        }
        throw new XmlPullParserException(xmlPullParser.getPositionDescription() + ": invalid animated-selector tag " + name);
    }

    private void o(Context context, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        int depth = xmlPullParser.getDepth() + 1;
        while (true) {
            int next = xmlPullParser.next();
            if (next == 1) {
                return;
            }
            int depth2 = xmlPullParser.getDepth();
            if (depth2 < depth && next == 3) {
                return;
            }
            if (next == 2 && depth2 <= depth) {
                if (xmlPullParser.getName().equals("item")) {
                    q(context, resources, xmlPullParser, attributeSet, theme);
                } else if (xmlPullParser.getName().equals("transition")) {
                    r(context, resources, xmlPullParser, attributeSet, theme);
                }
            }
        }
    }

    private void p() {
        onStateChange(getState());
    }

    private int q(Context context, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        int next;
        TypedArray k8 = k.k(resources, theme, attributeSet, j.e.f20578h);
        int resourceId = k8.getResourceId(j.e.f20579i, 0);
        int resourceId2 = k8.getResourceId(j.e.f20580j, -1);
        Drawable j8 = resourceId2 > 0 ? z.h().j(context, resourceId2) : null;
        k8.recycle();
        int[] k9 = k(attributeSet);
        if (j8 == null) {
            do {
                next = xmlPullParser.next();
            } while (next == 4);
            if (next != 2) {
                throw new XmlPullParserException(xmlPullParser.getPositionDescription() + ": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
            }
            j8 = xmlPullParser.getName().equals("vector") ? i.c(resources, xmlPullParser, attributeSet, theme) : Build.VERSION.SDK_INT >= 21 ? j.c.a(resources, xmlPullParser, attributeSet, theme) : Drawable.createFromXmlInner(resources, xmlPullParser, attributeSet);
        }
        if (j8 != null) {
            return this.q.B(k9, j8, resourceId);
        }
        throw new XmlPullParserException(xmlPullParser.getPositionDescription() + ": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
    }

    private int r(Context context, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        int next;
        TypedArray k8 = k.k(resources, theme, attributeSet, j.e.f20581k);
        int resourceId = k8.getResourceId(j.e.f20584n, -1);
        int resourceId2 = k8.getResourceId(j.e.f20583m, -1);
        int resourceId3 = k8.getResourceId(j.e.f20582l, -1);
        Drawable j8 = resourceId3 > 0 ? z.h().j(context, resourceId3) : null;
        boolean z4 = k8.getBoolean(j.e.f20585o, false);
        k8.recycle();
        if (j8 == null) {
            do {
                next = xmlPullParser.next();
            } while (next == 4);
            if (next != 2) {
                throw new XmlPullParserException(xmlPullParser.getPositionDescription() + ": <transition> tag requires a 'drawable' attribute or child tag defining a drawable");
            }
            j8 = xmlPullParser.getName().equals("animated-vector") ? androidx.vectordrawable.graphics.drawable.c.a(context, resources, xmlPullParser, attributeSet, theme) : Build.VERSION.SDK_INT >= 21 ? j.c.a(resources, xmlPullParser, attributeSet, theme) : Drawable.createFromXmlInner(resources, xmlPullParser, attributeSet);
        }
        if (j8 == null) {
            throw new XmlPullParserException(xmlPullParser.getPositionDescription() + ": <transition> tag requires a 'drawable' attribute or child tag defining a drawable");
        } else if (resourceId == -1 || resourceId2 == -1) {
            throw new XmlPullParserException(xmlPullParser.getPositionDescription() + ": <transition> tag requires 'fromId' & 'toId' attributes");
        } else {
            return this.q.C(resourceId, resourceId2, j8, z4);
        }
    }

    private boolean s(int i8) {
        int c9;
        int G;
        g bVar;
        g gVar = this.f20348t;
        if (gVar == null) {
            c9 = c();
        } else if (i8 == this.f20349w) {
            return true;
        } else {
            if (i8 == this.f20350x && gVar.a()) {
                gVar.b();
                this.f20349w = this.f20350x;
                this.f20350x = i8;
                return true;
            }
            c9 = this.f20349w;
            gVar.d();
        }
        this.f20348t = null;
        this.f20350x = -1;
        this.f20349w = -1;
        c cVar = this.q;
        int E = cVar.E(c9);
        int E2 = cVar.E(i8);
        if (E2 == 0 || E == 0 || (G = cVar.G(E, E2)) < 0) {
            return false;
        }
        boolean I = cVar.I(E, E2);
        g(G);
        Drawable current = getCurrent();
        if (current instanceof AnimationDrawable) {
            bVar = new e((AnimationDrawable) current, cVar.H(E, E2), I);
        } else if (!(current instanceof androidx.vectordrawable.graphics.drawable.c)) {
            if (current instanceof Animatable) {
                bVar = new b((Animatable) current);
            }
            return false;
        } else {
            bVar = new d((androidx.vectordrawable.graphics.drawable.c) current);
        }
        bVar.c();
        this.f20348t = bVar;
        this.f20350x = c9;
        this.f20349w = i8;
        return true;
    }

    private void t(TypedArray typedArray) {
        c cVar = this.q;
        if (Build.VERSION.SDK_INT >= 21) {
            cVar.f20376d |= j.c.b(typedArray);
        }
        cVar.x(typedArray.getBoolean(j.e.f20574d, cVar.f20381i));
        cVar.t(typedArray.getBoolean(j.e.f20575e, cVar.f20384l));
        cVar.u(typedArray.getInt(j.e.f20576f, cVar.A));
        cVar.v(typedArray.getInt(j.e.f20577g, cVar.B));
        setDither(typedArray.getBoolean(j.e.f20572b, cVar.f20395x));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // i.e, i.b
    public void h(b.d dVar) {
        super.h(dVar);
        if (dVar instanceof c) {
            this.q = (c) dVar;
        }
    }

    @Override // i.e, android.graphics.drawable.Drawable
    public boolean isStateful() {
        return true;
    }

    @Override // i.b, android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        super.jumpToCurrentState();
        g gVar = this.f20348t;
        if (gVar != null) {
            gVar.d();
            this.f20348t = null;
            g(this.f20349w);
            this.f20349w = -1;
            this.f20350x = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // i.e
    /* renamed from: l */
    public c j() {
        return new c(this.q, this, null);
    }

    @Override // i.e, i.b, android.graphics.drawable.Drawable
    public Drawable mutate() {
        if (!this.f20351y && super.mutate() == this) {
            this.q.r();
            this.f20351y = true;
        }
        return this;
    }

    public void n(Context context, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        TypedArray k8 = k.k(resources, theme, attributeSet, j.e.f20571a);
        setVisible(k8.getBoolean(j.e.f20573c, true), true);
        t(k8);
        i(resources);
        k8.recycle();
        o(context, resources, xmlPullParser, attributeSet, theme);
        p();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // i.e, i.b, android.graphics.drawable.Drawable
    public boolean onStateChange(int[] iArr) {
        int F = this.q.F(iArr);
        boolean z4 = F != c() && (s(F) || g(F));
        Drawable current = getCurrent();
        return current != null ? z4 | current.setState(iArr) : z4;
    }

    @Override // i.b, android.graphics.drawable.Drawable
    public boolean setVisible(boolean z4, boolean z8) {
        boolean visible = super.setVisible(z4, z8);
        g gVar = this.f20348t;
        if (gVar != null && (visible || z8)) {
            if (z4) {
                gVar.c();
            } else {
                jumpToCurrentState();
            }
        }
        return visible;
    }
}
