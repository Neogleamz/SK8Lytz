package x7;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class m {

    /* renamed from: m  reason: collision with root package name */
    public static final x7.c f24211m = new k(0.5f);

    /* renamed from: a  reason: collision with root package name */
    d f24212a;

    /* renamed from: b  reason: collision with root package name */
    d f24213b;

    /* renamed from: c  reason: collision with root package name */
    d f24214c;

    /* renamed from: d  reason: collision with root package name */
    d f24215d;

    /* renamed from: e  reason: collision with root package name */
    x7.c f24216e;

    /* renamed from: f  reason: collision with root package name */
    x7.c f24217f;

    /* renamed from: g  reason: collision with root package name */
    x7.c f24218g;

    /* renamed from: h  reason: collision with root package name */
    x7.c f24219h;

    /* renamed from: i  reason: collision with root package name */
    f f24220i;

    /* renamed from: j  reason: collision with root package name */
    f f24221j;

    /* renamed from: k  reason: collision with root package name */
    f f24222k;

    /* renamed from: l  reason: collision with root package name */
    f f24223l;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private d f24224a;

        /* renamed from: b  reason: collision with root package name */
        private d f24225b;

        /* renamed from: c  reason: collision with root package name */
        private d f24226c;

        /* renamed from: d  reason: collision with root package name */
        private d f24227d;

        /* renamed from: e  reason: collision with root package name */
        private x7.c f24228e;

        /* renamed from: f  reason: collision with root package name */
        private x7.c f24229f;

        /* renamed from: g  reason: collision with root package name */
        private x7.c f24230g;

        /* renamed from: h  reason: collision with root package name */
        private x7.c f24231h;

        /* renamed from: i  reason: collision with root package name */
        private f f24232i;

        /* renamed from: j  reason: collision with root package name */
        private f f24233j;

        /* renamed from: k  reason: collision with root package name */
        private f f24234k;

        /* renamed from: l  reason: collision with root package name */
        private f f24235l;

        public b() {
            this.f24224a = i.b();
            this.f24225b = i.b();
            this.f24226c = i.b();
            this.f24227d = i.b();
            this.f24228e = new x7.a(0.0f);
            this.f24229f = new x7.a(0.0f);
            this.f24230g = new x7.a(0.0f);
            this.f24231h = new x7.a(0.0f);
            this.f24232i = i.c();
            this.f24233j = i.c();
            this.f24234k = i.c();
            this.f24235l = i.c();
        }

        public b(m mVar) {
            this.f24224a = i.b();
            this.f24225b = i.b();
            this.f24226c = i.b();
            this.f24227d = i.b();
            this.f24228e = new x7.a(0.0f);
            this.f24229f = new x7.a(0.0f);
            this.f24230g = new x7.a(0.0f);
            this.f24231h = new x7.a(0.0f);
            this.f24232i = i.c();
            this.f24233j = i.c();
            this.f24234k = i.c();
            this.f24235l = i.c();
            this.f24224a = mVar.f24212a;
            this.f24225b = mVar.f24213b;
            this.f24226c = mVar.f24214c;
            this.f24227d = mVar.f24215d;
            this.f24228e = mVar.f24216e;
            this.f24229f = mVar.f24217f;
            this.f24230g = mVar.f24218g;
            this.f24231h = mVar.f24219h;
            this.f24232i = mVar.f24220i;
            this.f24233j = mVar.f24221j;
            this.f24234k = mVar.f24222k;
            this.f24235l = mVar.f24223l;
        }

        private static float n(d dVar) {
            if (dVar instanceof l) {
                return ((l) dVar).f24210a;
            }
            if (dVar instanceof e) {
                return ((e) dVar).f24162a;
            }
            return -1.0f;
        }

        public b A(x7.c cVar) {
            this.f24230g = cVar;
            return this;
        }

        public b B(f fVar) {
            this.f24232i = fVar;
            return this;
        }

        public b C(int i8, x7.c cVar) {
            return D(i.a(i8)).F(cVar);
        }

        public b D(d dVar) {
            this.f24224a = dVar;
            float n8 = n(dVar);
            if (n8 != -1.0f) {
                E(n8);
            }
            return this;
        }

        public b E(float f5) {
            this.f24228e = new x7.a(f5);
            return this;
        }

        public b F(x7.c cVar) {
            this.f24228e = cVar;
            return this;
        }

        public b G(int i8, x7.c cVar) {
            return H(i.a(i8)).J(cVar);
        }

        public b H(d dVar) {
            this.f24225b = dVar;
            float n8 = n(dVar);
            if (n8 != -1.0f) {
                I(n8);
            }
            return this;
        }

        public b I(float f5) {
            this.f24229f = new x7.a(f5);
            return this;
        }

        public b J(x7.c cVar) {
            this.f24229f = cVar;
            return this;
        }

        public m m() {
            return new m(this);
        }

        public b o(float f5) {
            return E(f5).I(f5).z(f5).v(f5);
        }

        public b p(x7.c cVar) {
            return F(cVar).J(cVar).A(cVar).w(cVar);
        }

        public b q(int i8, float f5) {
            return r(i.a(i8)).o(f5);
        }

        public b r(d dVar) {
            return D(dVar).H(dVar).y(dVar).u(dVar);
        }

        public b s(f fVar) {
            this.f24234k = fVar;
            return this;
        }

        public b t(int i8, x7.c cVar) {
            return u(i.a(i8)).w(cVar);
        }

        public b u(d dVar) {
            this.f24227d = dVar;
            float n8 = n(dVar);
            if (n8 != -1.0f) {
                v(n8);
            }
            return this;
        }

        public b v(float f5) {
            this.f24231h = new x7.a(f5);
            return this;
        }

        public b w(x7.c cVar) {
            this.f24231h = cVar;
            return this;
        }

        public b x(int i8, x7.c cVar) {
            return y(i.a(i8)).A(cVar);
        }

        public b y(d dVar) {
            this.f24226c = dVar;
            float n8 = n(dVar);
            if (n8 != -1.0f) {
                z(n8);
            }
            return this;
        }

        public b z(float f5) {
            this.f24230g = new x7.a(f5);
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        x7.c a(x7.c cVar);
    }

    public m() {
        this.f24212a = i.b();
        this.f24213b = i.b();
        this.f24214c = i.b();
        this.f24215d = i.b();
        this.f24216e = new x7.a(0.0f);
        this.f24217f = new x7.a(0.0f);
        this.f24218g = new x7.a(0.0f);
        this.f24219h = new x7.a(0.0f);
        this.f24220i = i.c();
        this.f24221j = i.c();
        this.f24222k = i.c();
        this.f24223l = i.c();
    }

    private m(b bVar) {
        this.f24212a = bVar.f24224a;
        this.f24213b = bVar.f24225b;
        this.f24214c = bVar.f24226c;
        this.f24215d = bVar.f24227d;
        this.f24216e = bVar.f24228e;
        this.f24217f = bVar.f24229f;
        this.f24218g = bVar.f24230g;
        this.f24219h = bVar.f24231h;
        this.f24220i = bVar.f24232i;
        this.f24221j = bVar.f24233j;
        this.f24222k = bVar.f24234k;
        this.f24223l = bVar.f24235l;
    }

    public static b a() {
        return new b();
    }

    public static b b(Context context, int i8, int i9) {
        return c(context, i8, i9, 0);
    }

    private static b c(Context context, int i8, int i9, int i10) {
        return d(context, i8, i9, new x7.a(i10));
    }

    private static b d(Context context, int i8, int i9, x7.c cVar) {
        if (i9 != 0) {
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, i8);
            i8 = i9;
            context = contextThemeWrapper;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(i8, k7.l.f21345j6);
        try {
            int i10 = obtainStyledAttributes.getInt(k7.l.f21354k6, 0);
            int i11 = obtainStyledAttributes.getInt(k7.l.f21381n6, i10);
            int i12 = obtainStyledAttributes.getInt(k7.l.f21389o6, i10);
            int i13 = obtainStyledAttributes.getInt(k7.l.f21372m6, i10);
            int i14 = obtainStyledAttributes.getInt(k7.l.f21363l6, i10);
            x7.c m8 = m(obtainStyledAttributes, k7.l.f21398p6, cVar);
            x7.c m9 = m(obtainStyledAttributes, k7.l.f21423s6, m8);
            x7.c m10 = m(obtainStyledAttributes, k7.l.f21432t6, m8);
            x7.c m11 = m(obtainStyledAttributes, k7.l.f21414r6, m8);
            return new b().C(i11, m9).G(i12, m10).x(i13, m11).t(i14, m(obtainStyledAttributes, k7.l.f21406q6, m8));
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public static b e(Context context, AttributeSet attributeSet, int i8, int i9) {
        return f(context, attributeSet, i8, i9, 0);
    }

    public static b f(Context context, AttributeSet attributeSet, int i8, int i9, int i10) {
        return g(context, attributeSet, i8, i9, new x7.a(i10));
    }

    public static b g(Context context, AttributeSet attributeSet, int i8, int i9, x7.c cVar) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, k7.l.I4, i8, i9);
        int resourceId = obtainStyledAttributes.getResourceId(k7.l.J4, 0);
        int resourceId2 = obtainStyledAttributes.getResourceId(k7.l.K4, 0);
        obtainStyledAttributes.recycle();
        return d(context, resourceId, resourceId2, cVar);
    }

    private static x7.c m(TypedArray typedArray, int i8, x7.c cVar) {
        TypedValue peekValue = typedArray.peekValue(i8);
        if (peekValue == null) {
            return cVar;
        }
        int i9 = peekValue.type;
        return i9 == 5 ? new x7.a(TypedValue.complexToDimensionPixelSize(peekValue.data, typedArray.getResources().getDisplayMetrics())) : i9 == 6 ? new k(peekValue.getFraction(1.0f, 1.0f)) : cVar;
    }

    public f h() {
        return this.f24222k;
    }

    public d i() {
        return this.f24215d;
    }

    public x7.c j() {
        return this.f24219h;
    }

    public d k() {
        return this.f24214c;
    }

    public x7.c l() {
        return this.f24218g;
    }

    public f n() {
        return this.f24223l;
    }

    public f o() {
        return this.f24221j;
    }

    public f p() {
        return this.f24220i;
    }

    public d q() {
        return this.f24212a;
    }

    public x7.c r() {
        return this.f24216e;
    }

    public d s() {
        return this.f24213b;
    }

    public x7.c t() {
        return this.f24217f;
    }

    public boolean u(RectF rectF) {
        boolean z4 = this.f24223l.getClass().equals(f.class) && this.f24221j.getClass().equals(f.class) && this.f24220i.getClass().equals(f.class) && this.f24222k.getClass().equals(f.class);
        float a9 = this.f24216e.a(rectF);
        return z4 && ((this.f24217f.a(rectF) > a9 ? 1 : (this.f24217f.a(rectF) == a9 ? 0 : -1)) == 0 && (this.f24219h.a(rectF) > a9 ? 1 : (this.f24219h.a(rectF) == a9 ? 0 : -1)) == 0 && (this.f24218g.a(rectF) > a9 ? 1 : (this.f24218g.a(rectF) == a9 ? 0 : -1)) == 0) && ((this.f24213b instanceof l) && (this.f24212a instanceof l) && (this.f24214c instanceof l) && (this.f24215d instanceof l));
    }

    public b v() {
        return new b(this);
    }

    public m w(float f5) {
        return v().o(f5).m();
    }

    public m x(x7.c cVar) {
        return v().p(cVar).m();
    }

    public m y(c cVar) {
        return v().F(cVar.a(r())).J(cVar.a(t())).w(cVar.a(j())).A(cVar.a(l())).m();
    }
}
