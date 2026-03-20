package androidx.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import org.xmlpull.v1.XmlPullParser;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Slide extends Visibility {

    /* renamed from: c0  reason: collision with root package name */
    private static final TimeInterpolator f7453c0 = new DecelerateInterpolator();

    /* renamed from: d0  reason: collision with root package name */
    private static final TimeInterpolator f7454d0 = new AccelerateInterpolator();

    /* renamed from: e0  reason: collision with root package name */
    private static final g f7455e0 = new a();

    /* renamed from: f0  reason: collision with root package name */
    private static final g f7456f0 = new b();

    /* renamed from: g0  reason: collision with root package name */
    private static final g f7457g0 = new c();

    /* renamed from: h0  reason: collision with root package name */
    private static final g f7458h0 = new d();

    /* renamed from: i0  reason: collision with root package name */
    private static final g f7459i0 = new e();

    /* renamed from: j0  reason: collision with root package name */
    private static final g f7460j0 = new f();

    /* renamed from: a0  reason: collision with root package name */
    private g f7461a0;

    /* renamed from: b0  reason: collision with root package name */
    private int f7462b0;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a extends h {
        a() {
            super(null);
        }

        @Override // androidx.transition.Slide.g
        public float b(ViewGroup viewGroup, View view) {
            return view.getTranslationX() - viewGroup.getWidth();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b extends h {
        b() {
            super(null);
        }

        @Override // androidx.transition.Slide.g
        public float b(ViewGroup viewGroup, View view) {
            boolean z4 = androidx.core.view.c0.E(viewGroup) == 1;
            float translationX = view.getTranslationX();
            float width = viewGroup.getWidth();
            return z4 ? translationX + width : translationX - width;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c extends i {
        c() {
            super(null);
        }

        @Override // androidx.transition.Slide.g
        public float a(ViewGroup viewGroup, View view) {
            return view.getTranslationY() - viewGroup.getHeight();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class d extends h {
        d() {
            super(null);
        }

        @Override // androidx.transition.Slide.g
        public float b(ViewGroup viewGroup, View view) {
            return view.getTranslationX() + viewGroup.getWidth();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class e extends h {
        e() {
            super(null);
        }

        @Override // androidx.transition.Slide.g
        public float b(ViewGroup viewGroup, View view) {
            boolean z4 = androidx.core.view.c0.E(viewGroup) == 1;
            float translationX = view.getTranslationX();
            float width = viewGroup.getWidth();
            return z4 ? translationX - width : translationX + width;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class f extends i {
        f() {
            super(null);
        }

        @Override // androidx.transition.Slide.g
        public float a(ViewGroup viewGroup, View view) {
            return view.getTranslationY() + viewGroup.getHeight();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface g {
        float a(ViewGroup viewGroup, View view);

        float b(ViewGroup viewGroup, View view);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static abstract class h implements g {
        private h() {
        }

        /* synthetic */ h(a aVar) {
            this();
        }

        @Override // androidx.transition.Slide.g
        public float a(ViewGroup viewGroup, View view) {
            return view.getTranslationY();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static abstract class i implements g {
        private i() {
        }

        /* synthetic */ i(a aVar) {
            this();
        }

        @Override // androidx.transition.Slide.g
        public float b(ViewGroup viewGroup, View view) {
            return view.getTranslationX();
        }
    }

    public Slide() {
        this.f7461a0 = f7460j0;
        this.f7462b0 = 80;
        w0(80);
    }

    @SuppressLint({"RestrictedApi"})
    public Slide(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f7461a0 = f7460j0;
        this.f7462b0 = 80;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, q.f7602h);
        int g8 = androidx.core.content.res.k.g(obtainStyledAttributes, (XmlPullParser) attributeSet, "slideEdge", 0, 80);
        obtainStyledAttributes.recycle();
        w0(g8);
    }

    private void o0(u uVar) {
        int[] iArr = new int[2];
        uVar.f7620b.getLocationOnScreen(iArr);
        uVar.f7619a.put("android:slide:screenPosition", iArr);
    }

    @Override // androidx.transition.Visibility, androidx.transition.Transition
    public void j(u uVar) {
        super.j(uVar);
        o0(uVar);
    }

    @Override // androidx.transition.Visibility, androidx.transition.Transition
    public void m(u uVar) {
        super.m(uVar);
        o0(uVar);
    }

    @Override // androidx.transition.Visibility
    public Animator r0(ViewGroup viewGroup, View view, u uVar, u uVar2) {
        if (uVar2 == null) {
            return null;
        }
        int[] iArr = (int[]) uVar2.f7619a.get("android:slide:screenPosition");
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        return w.a(view, uVar2, iArr[0], iArr[1], this.f7461a0.b(viewGroup, view), this.f7461a0.a(viewGroup, view), translationX, translationY, f7453c0, this);
    }

    @Override // androidx.transition.Visibility
    public Animator t0(ViewGroup viewGroup, View view, u uVar, u uVar2) {
        if (uVar == null) {
            return null;
        }
        int[] iArr = (int[]) uVar.f7619a.get("android:slide:screenPosition");
        return w.a(view, uVar, iArr[0], iArr[1], view.getTranslationX(), view.getTranslationY(), this.f7461a0.b(viewGroup, view), this.f7461a0.a(viewGroup, view), f7454d0, this);
    }

    public void w0(int i8) {
        g gVar;
        if (i8 == 3) {
            gVar = f7455e0;
        } else if (i8 == 5) {
            gVar = f7458h0;
        } else if (i8 == 48) {
            gVar = f7457g0;
        } else if (i8 == 80) {
            gVar = f7460j0;
        } else if (i8 == 8388611) {
            gVar = f7456f0;
        } else if (i8 != 8388613) {
            throw new IllegalArgumentException("Invalid slide direction");
        } else {
            gVar = f7459i0;
        }
        this.f7461a0 = gVar;
        this.f7462b0 = i8;
        x1.c cVar = new x1.c();
        cVar.j(i8);
        k0(cVar);
    }
}
