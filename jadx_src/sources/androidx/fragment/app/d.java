package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import androidx.core.os.e;
import androidx.fragment.app.s;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements e.b {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Fragment f5607a;

        a(Fragment fragment) {
            this.f5607a = fragment;
        }

        @Override // androidx.core.os.e.b
        public void a() {
            if (this.f5607a.n() != null) {
                View n8 = this.f5607a.n();
                this.f5607a.q1(null);
                n8.clearAnimation();
            }
            this.f5607a.s1(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements Animation.AnimationListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ ViewGroup f5608a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Fragment f5609b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ s.g f5610c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ androidx.core.os.e f5611d;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (b.this.f5609b.n() != null) {
                    b.this.f5609b.q1(null);
                    b bVar = b.this;
                    bVar.f5610c.a(bVar.f5609b, bVar.f5611d);
                }
            }
        }

        b(ViewGroup viewGroup, Fragment fragment, s.g gVar, androidx.core.os.e eVar) {
            this.f5608a = viewGroup;
            this.f5609b = fragment;
            this.f5610c = gVar;
            this.f5611d = eVar;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            this.f5608a.post(new a());
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ ViewGroup f5613a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ View f5614b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ Fragment f5615c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ s.g f5616d;

        /* renamed from: e  reason: collision with root package name */
        final /* synthetic */ androidx.core.os.e f5617e;

        c(ViewGroup viewGroup, View view, Fragment fragment, s.g gVar, androidx.core.os.e eVar) {
            this.f5613a = viewGroup;
            this.f5614b = view;
            this.f5615c = fragment;
            this.f5616d = gVar;
            this.f5617e = eVar;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f5613a.endViewTransition(this.f5614b);
            Animator o5 = this.f5615c.o();
            this.f5615c.s1(null);
            if (o5 == null || this.f5613a.indexOfChild(this.f5614b) >= 0) {
                return;
            }
            this.f5616d.a(this.f5615c, this.f5617e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.fragment.app.d$d  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0056d {

        /* renamed from: a  reason: collision with root package name */
        public final Animation f5618a;

        /* renamed from: b  reason: collision with root package name */
        public final Animator f5619b;

        C0056d(Animator animator) {
            this.f5618a = null;
            this.f5619b = animator;
            if (animator == null) {
                throw new IllegalStateException("Animator cannot be null");
            }
        }

        C0056d(Animation animation) {
            this.f5618a = animation;
            this.f5619b = null;
            if (animation == null) {
                throw new IllegalStateException("Animation cannot be null");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e extends AnimationSet implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        private final ViewGroup f5620a;

        /* renamed from: b  reason: collision with root package name */
        private final View f5621b;

        /* renamed from: c  reason: collision with root package name */
        private boolean f5622c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f5623d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f5624e;

        /* JADX INFO: Access modifiers changed from: package-private */
        public e(Animation animation, ViewGroup viewGroup, View view) {
            super(false);
            this.f5624e = true;
            this.f5620a = viewGroup;
            this.f5621b = view;
            addAnimation(animation);
            viewGroup.post(this);
        }

        @Override // android.view.animation.AnimationSet, android.view.animation.Animation
        public boolean getTransformation(long j8, Transformation transformation) {
            this.f5624e = true;
            if (this.f5622c) {
                return !this.f5623d;
            }
            if (!super.getTransformation(j8, transformation)) {
                this.f5622c = true;
                androidx.core.view.y.a(this.f5620a, this);
            }
            return true;
        }

        @Override // android.view.animation.Animation
        public boolean getTransformation(long j8, Transformation transformation, float f5) {
            this.f5624e = true;
            if (this.f5622c) {
                return !this.f5623d;
            }
            if (!super.getTransformation(j8, transformation, f5)) {
                this.f5622c = true;
                androidx.core.view.y.a(this.f5620a, this);
            }
            return true;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.f5622c || !this.f5624e) {
                this.f5620a.endViewTransition(this.f5621b);
                this.f5623d = true;
                return;
            }
            this.f5624e = false;
            this.f5620a.post(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Fragment fragment, C0056d c0056d, s.g gVar) {
        View view = fragment.T;
        ViewGroup viewGroup = fragment.R;
        viewGroup.startViewTransition(view);
        androidx.core.os.e eVar = new androidx.core.os.e();
        eVar.c(new a(fragment));
        gVar.b(fragment, eVar);
        if (c0056d.f5618a != null) {
            e eVar2 = new e(c0056d.f5618a, viewGroup, view);
            fragment.q1(fragment.T);
            eVar2.setAnimationListener(new b(viewGroup, fragment, gVar, eVar));
            fragment.T.startAnimation(eVar2);
            return;
        }
        Animator animator = c0056d.f5619b;
        fragment.s1(animator);
        animator.addListener(new c(viewGroup, view, fragment, gVar, eVar));
        animator.setTarget(fragment.T);
        animator.start();
    }

    private static int b(Fragment fragment, boolean z4, boolean z8) {
        return z8 ? z4 ? fragment.G() : fragment.H() : z4 ? fragment.r() : fragment.u();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static C0056d c(Context context, Fragment fragment, boolean z4, boolean z8) {
        int C = fragment.C();
        int b9 = b(fragment, z4, z8);
        boolean z9 = false;
        fragment.r1(0, 0, 0, 0);
        ViewGroup viewGroup = fragment.R;
        if (viewGroup != null) {
            int i8 = b1.b.f7949c;
            if (viewGroup.getTag(i8) != null) {
                fragment.R.setTag(i8, null);
            }
        }
        ViewGroup viewGroup2 = fragment.R;
        if (viewGroup2 == null || viewGroup2.getLayoutTransition() == null) {
            Animation q02 = fragment.q0(C, z4, b9);
            if (q02 != null) {
                return new C0056d(q02);
            }
            Animator r02 = fragment.r0(C, z4, b9);
            if (r02 != null) {
                return new C0056d(r02);
            }
            if (b9 == 0 && C != 0) {
                b9 = d(C, z4);
            }
            if (b9 != 0) {
                boolean equals = "anim".equals(context.getResources().getResourceTypeName(b9));
                if (equals) {
                    try {
                        Animation loadAnimation = AnimationUtils.loadAnimation(context, b9);
                        if (loadAnimation != null) {
                            return new C0056d(loadAnimation);
                        }
                        z9 = true;
                    } catch (Resources.NotFoundException e8) {
                        throw e8;
                    } catch (RuntimeException unused) {
                    }
                }
                if (!z9) {
                    try {
                        Animator loadAnimator = AnimatorInflater.loadAnimator(context, b9);
                        if (loadAnimator != null) {
                            return new C0056d(loadAnimator);
                        }
                    } catch (RuntimeException e9) {
                        if (equals) {
                            throw e9;
                        }
                        Animation loadAnimation2 = AnimationUtils.loadAnimation(context, b9);
                        if (loadAnimation2 != null) {
                            return new C0056d(loadAnimation2);
                        }
                    }
                }
            }
            return null;
        }
        return null;
    }

    private static int d(int i8, boolean z4) {
        if (i8 == 4097) {
            return z4 ? b1.a.f7945e : b1.a.f7946f;
        } else if (i8 == 4099) {
            return z4 ? b1.a.f7943c : b1.a.f7944d;
        } else if (i8 != 8194) {
            return -1;
        } else {
            return z4 ? b1.a.f7941a : b1.a.f7942b;
        }
    }
}
