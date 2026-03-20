package androidx.core.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import java.lang.ref.WeakReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i0 {

    /* renamed from: a  reason: collision with root package name */
    private final WeakReference<View> f5018a;

    /* renamed from: b  reason: collision with root package name */
    Runnable f5019b = null;

    /* renamed from: c  reason: collision with root package name */
    Runnable f5020c = null;

    /* renamed from: d  reason: collision with root package name */
    int f5021d = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ j0 f5022a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ View f5023b;

        a(j0 j0Var, View view) {
            this.f5022a = j0Var;
            this.f5023b = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f5022a.a(this.f5023b);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f5022a.b(this.f5023b);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            this.f5022a.c(this.f5023b);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b {
        static ViewPropertyAnimator a(ViewPropertyAnimator viewPropertyAnimator, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
            return viewPropertyAnimator.setUpdateListener(animatorUpdateListener);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c implements j0 {

        /* renamed from: a  reason: collision with root package name */
        i0 f5025a;

        /* renamed from: b  reason: collision with root package name */
        boolean f5026b;

        c(i0 i0Var) {
            this.f5025a = i0Var;
        }

        @Override // androidx.core.view.j0
        public void a(View view) {
            Object tag = view.getTag(2113929216);
            j0 j0Var = tag instanceof j0 ? (j0) tag : null;
            if (j0Var != null) {
                j0Var.a(view);
            }
        }

        @Override // androidx.core.view.j0
        @SuppressLint({"WrongConstant"})
        public void b(View view) {
            int i8 = this.f5025a.f5021d;
            if (i8 > -1) {
                view.setLayerType(i8, null);
                this.f5025a.f5021d = -1;
            }
            if (Build.VERSION.SDK_INT >= 16 || !this.f5026b) {
                i0 i0Var = this.f5025a;
                Runnable runnable = i0Var.f5020c;
                if (runnable != null) {
                    i0Var.f5020c = null;
                    runnable.run();
                }
                Object tag = view.getTag(2113929216);
                j0 j0Var = tag instanceof j0 ? (j0) tag : null;
                if (j0Var != null) {
                    j0Var.b(view);
                }
                this.f5026b = true;
            }
        }

        @Override // androidx.core.view.j0
        public void c(View view) {
            this.f5026b = false;
            if (this.f5025a.f5021d > -1) {
                view.setLayerType(2, null);
            }
            i0 i0Var = this.f5025a;
            Runnable runnable = i0Var.f5019b;
            if (runnable != null) {
                i0Var.f5019b = null;
                runnable.run();
            }
            Object tag = view.getTag(2113929216);
            j0 j0Var = tag instanceof j0 ? (j0) tag : null;
            if (j0Var != null) {
                j0Var.c(view);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public i0(View view) {
        this.f5018a = new WeakReference<>(view);
    }

    private void i(View view, j0 j0Var) {
        if (j0Var != null) {
            view.animate().setListener(new a(j0Var, view));
        } else {
            view.animate().setListener(null);
        }
    }

    public i0 b(float f5) {
        View view = this.f5018a.get();
        if (view != null) {
            view.animate().alpha(f5);
        }
        return this;
    }

    public void c() {
        View view = this.f5018a.get();
        if (view != null) {
            view.animate().cancel();
        }
    }

    public long d() {
        View view = this.f5018a.get();
        if (view != null) {
            return view.animate().getDuration();
        }
        return 0L;
    }

    public i0 f(long j8) {
        View view = this.f5018a.get();
        if (view != null) {
            view.animate().setDuration(j8);
        }
        return this;
    }

    public i0 g(Interpolator interpolator) {
        View view = this.f5018a.get();
        if (view != null) {
            view.animate().setInterpolator(interpolator);
        }
        return this;
    }

    public i0 h(j0 j0Var) {
        View view = this.f5018a.get();
        if (view != null) {
            if (Build.VERSION.SDK_INT < 16) {
                view.setTag(2113929216, j0Var);
                j0Var = new c(this);
            }
            i(view, j0Var);
        }
        return this;
    }

    public i0 j(long j8) {
        View view = this.f5018a.get();
        if (view != null) {
            view.animate().setStartDelay(j8);
        }
        return this;
    }

    public i0 k(final l0 l0Var) {
        final View view = this.f5018a.get();
        if (view != null && Build.VERSION.SDK_INT >= 19) {
            b.a(view.animate(), l0Var != null ? new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.core.view.h0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    l0.this.a(view);
                }
            } : null);
        }
        return this;
    }

    public void l() {
        View view = this.f5018a.get();
        if (view != null) {
            view.animate().start();
        }
    }

    public i0 m(float f5) {
        View view = this.f5018a.get();
        if (view != null) {
            view.animate().translationY(f5);
        }
        return this;
    }
}
