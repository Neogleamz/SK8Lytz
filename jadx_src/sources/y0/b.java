package y0;

import android.os.Looper;
import android.util.AndroidRuntimeException;
import android.view.View;
import androidx.core.view.c0;
import java.util.ArrayList;
import y0.a;
import y0.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b<T extends b<T>> implements a.b {

    /* renamed from: m  reason: collision with root package name */
    public static final r f24335m = new f("translationX");

    /* renamed from: n  reason: collision with root package name */
    public static final r f24336n = new g("translationY");

    /* renamed from: o  reason: collision with root package name */
    public static final r f24337o = new h("translationZ");

    /* renamed from: p  reason: collision with root package name */
    public static final r f24338p = new i("scaleX");
    public static final r q = new j("scaleY");

    /* renamed from: r  reason: collision with root package name */
    public static final r f24339r = new k("rotation");

    /* renamed from: s  reason: collision with root package name */
    public static final r f24340s = new l("rotationX");

    /* renamed from: t  reason: collision with root package name */
    public static final r f24341t = new m("rotationY");

    /* renamed from: u  reason: collision with root package name */
    public static final r f24342u = new n("x");

    /* renamed from: v  reason: collision with root package name */
    public static final r f24343v = new a("y");

    /* renamed from: w  reason: collision with root package name */
    public static final r f24344w = new C0231b("z");

    /* renamed from: x  reason: collision with root package name */
    public static final r f24345x = new c("alpha");

    /* renamed from: y  reason: collision with root package name */
    public static final r f24346y = new d("scrollX");

    /* renamed from: z  reason: collision with root package name */
    public static final r f24347z = new e("scrollY");

    /* renamed from: d  reason: collision with root package name */
    final Object f24351d;

    /* renamed from: e  reason: collision with root package name */
    final y0.c f24352e;

    /* renamed from: j  reason: collision with root package name */
    private float f24357j;

    /* renamed from: a  reason: collision with root package name */
    float f24348a = 0.0f;

    /* renamed from: b  reason: collision with root package name */
    float f24349b = Float.MAX_VALUE;

    /* renamed from: c  reason: collision with root package name */
    boolean f24350c = false;

    /* renamed from: f  reason: collision with root package name */
    boolean f24353f = false;

    /* renamed from: g  reason: collision with root package name */
    float f24354g = Float.MAX_VALUE;

    /* renamed from: h  reason: collision with root package name */
    float f24355h = -Float.MAX_VALUE;

    /* renamed from: i  reason: collision with root package name */
    private long f24356i = 0;

    /* renamed from: k  reason: collision with root package name */
    private final ArrayList<p> f24358k = new ArrayList<>();

    /* renamed from: l  reason: collision with root package name */
    private final ArrayList<q> f24359l = new ArrayList<>();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a extends r {
        a(String str) {
            super(str, null);
        }

        @Override // y0.c
        /* renamed from: c */
        public float a(View view) {
            return view.getY();
        }

        @Override // y0.c
        /* renamed from: d */
        public void b(View view, float f5) {
            view.setY(f5);
        }
    }

    /* renamed from: y0.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class C0231b extends r {
        C0231b(String str) {
            super(str, null);
        }

        @Override // y0.c
        /* renamed from: c */
        public float a(View view) {
            return c0.Q(view);
        }

        @Override // y0.c
        /* renamed from: d */
        public void b(View view, float f5) {
            c0.R0(view, f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c extends r {
        c(String str) {
            super(str, null);
        }

        @Override // y0.c
        /* renamed from: c */
        public float a(View view) {
            return view.getAlpha();
        }

        @Override // y0.c
        /* renamed from: d */
        public void b(View view, float f5) {
            view.setAlpha(f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class d extends r {
        d(String str) {
            super(str, null);
        }

        @Override // y0.c
        /* renamed from: c */
        public float a(View view) {
            return view.getScrollX();
        }

        @Override // y0.c
        /* renamed from: d */
        public void b(View view, float f5) {
            view.setScrollX((int) f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class e extends r {
        e(String str) {
            super(str, null);
        }

        @Override // y0.c
        /* renamed from: c */
        public float a(View view) {
            return view.getScrollY();
        }

        @Override // y0.c
        /* renamed from: d */
        public void b(View view, float f5) {
            view.setScrollY((int) f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class f extends r {
        f(String str) {
            super(str, null);
        }

        @Override // y0.c
        /* renamed from: c */
        public float a(View view) {
            return view.getTranslationX();
        }

        @Override // y0.c
        /* renamed from: d */
        public void b(View view, float f5) {
            view.setTranslationX(f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class g extends r {
        g(String str) {
            super(str, null);
        }

        @Override // y0.c
        /* renamed from: c */
        public float a(View view) {
            return view.getTranslationY();
        }

        @Override // y0.c
        /* renamed from: d */
        public void b(View view, float f5) {
            view.setTranslationY(f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class h extends r {
        h(String str) {
            super(str, null);
        }

        @Override // y0.c
        /* renamed from: c */
        public float a(View view) {
            return c0.O(view);
        }

        @Override // y0.c
        /* renamed from: d */
        public void b(View view, float f5) {
            c0.P0(view, f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class i extends r {
        i(String str) {
            super(str, null);
        }

        @Override // y0.c
        /* renamed from: c */
        public float a(View view) {
            return view.getScaleX();
        }

        @Override // y0.c
        /* renamed from: d */
        public void b(View view, float f5) {
            view.setScaleX(f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class j extends r {
        j(String str) {
            super(str, null);
        }

        @Override // y0.c
        /* renamed from: c */
        public float a(View view) {
            return view.getScaleY();
        }

        @Override // y0.c
        /* renamed from: d */
        public void b(View view, float f5) {
            view.setScaleY(f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class k extends r {
        k(String str) {
            super(str, null);
        }

        @Override // y0.c
        /* renamed from: c */
        public float a(View view) {
            return view.getRotation();
        }

        @Override // y0.c
        /* renamed from: d */
        public void b(View view, float f5) {
            view.setRotation(f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class l extends r {
        l(String str) {
            super(str, null);
        }

        @Override // y0.c
        /* renamed from: c */
        public float a(View view) {
            return view.getRotationX();
        }

        @Override // y0.c
        /* renamed from: d */
        public void b(View view, float f5) {
            view.setRotationX(f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class m extends r {
        m(String str) {
            super(str, null);
        }

        @Override // y0.c
        /* renamed from: c */
        public float a(View view) {
            return view.getRotationY();
        }

        @Override // y0.c
        /* renamed from: d */
        public void b(View view, float f5) {
            view.setRotationY(f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class n extends r {
        n(String str) {
            super(str, null);
        }

        @Override // y0.c
        /* renamed from: c */
        public float a(View view) {
            return view.getX();
        }

        @Override // y0.c
        /* renamed from: d */
        public void b(View view, float f5) {
            view.setX(f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class o {

        /* renamed from: a  reason: collision with root package name */
        float f24360a;

        /* renamed from: b  reason: collision with root package name */
        float f24361b;
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface p {
        void a(b bVar, boolean z4, float f5, float f8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface q {
        void a(b bVar, float f5, float f8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class r extends y0.c<View> {
        private r(String str) {
            super(str);
        }

        /* synthetic */ r(String str, f fVar) {
            this(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <K> b(K k8, y0.c<K> cVar) {
        float f5;
        this.f24351d = k8;
        this.f24352e = cVar;
        if (cVar == f24339r || cVar == f24340s || cVar == f24341t) {
            f5 = 0.1f;
        } else if (cVar == f24345x || cVar == f24338p || cVar == q) {
            this.f24357j = 0.00390625f;
            return;
        } else {
            f5 = 1.0f;
        }
        this.f24357j = f5;
    }

    private void c(boolean z4) {
        this.f24353f = false;
        y0.a.d().g(this);
        this.f24356i = 0L;
        this.f24350c = false;
        for (int i8 = 0; i8 < this.f24358k.size(); i8++) {
            if (this.f24358k.get(i8) != null) {
                this.f24358k.get(i8).a(this, z4, this.f24349b, this.f24348a);
            }
        }
        g(this.f24358k);
    }

    private float d() {
        return this.f24352e.a(this.f24351d);
    }

    private static <T> void g(ArrayList<T> arrayList) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            if (arrayList.get(size) == null) {
                arrayList.remove(size);
            }
        }
    }

    private void k() {
        if (this.f24353f) {
            return;
        }
        this.f24353f = true;
        if (!this.f24350c) {
            this.f24349b = d();
        }
        float f5 = this.f24349b;
        if (f5 > this.f24354g || f5 < this.f24355h) {
            throw new IllegalArgumentException("Starting value need to be in between min value and max value");
        }
        y0.a.d().a(this, 0L);
    }

    @Override // y0.a.b
    public boolean a(long j8) {
        long j9 = this.f24356i;
        if (j9 == 0) {
            this.f24356i = j8;
            h(this.f24349b);
            return false;
        }
        this.f24356i = j8;
        boolean l8 = l(j8 - j9);
        float min = Math.min(this.f24349b, this.f24354g);
        this.f24349b = min;
        float max = Math.max(min, this.f24355h);
        this.f24349b = max;
        h(max);
        if (l8) {
            c(false);
        }
        return l8;
    }

    public void b() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AndroidRuntimeException("Animations may only be canceled on the main thread");
        }
        if (this.f24353f) {
            c(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float e() {
        return this.f24357j * 0.75f;
    }

    public boolean f() {
        return this.f24353f;
    }

    void h(float f5) {
        this.f24352e.b(this.f24351d, f5);
        for (int i8 = 0; i8 < this.f24359l.size(); i8++) {
            if (this.f24359l.get(i8) != null) {
                this.f24359l.get(i8).a(this, this.f24349b, this.f24348a);
            }
        }
        g(this.f24359l);
    }

    public T i(float f5) {
        this.f24349b = f5;
        this.f24350c = true;
        return this;
    }

    public void j() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AndroidRuntimeException("Animations may only be started on the main thread");
        }
        if (this.f24353f) {
            return;
        }
        k();
    }

    abstract boolean l(long j8);
}
