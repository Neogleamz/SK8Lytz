package androidx.transition;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"ViewConstructor"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h extends ViewGroup implements e {

    /* renamed from: a  reason: collision with root package name */
    ViewGroup f7563a;

    /* renamed from: b  reason: collision with root package name */
    View f7564b;

    /* renamed from: c  reason: collision with root package name */
    final View f7565c;

    /* renamed from: d  reason: collision with root package name */
    int f7566d;

    /* renamed from: e  reason: collision with root package name */
    private Matrix f7567e;

    /* renamed from: f  reason: collision with root package name */
    private final ViewTreeObserver.OnPreDrawListener f7568f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements ViewTreeObserver.OnPreDrawListener {
        a() {
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            View view;
            androidx.core.view.c0.j0(h.this);
            h hVar = h.this;
            ViewGroup viewGroup = hVar.f7563a;
            if (viewGroup == null || (view = hVar.f7564b) == null) {
                return true;
            }
            viewGroup.endViewTransition(view);
            androidx.core.view.c0.j0(h.this.f7563a);
            h hVar2 = h.this;
            hVar2.f7563a = null;
            hVar2.f7564b = null;
            return true;
        }
    }

    h(View view) {
        super(view.getContext());
        this.f7568f = new a();
        this.f7565c = view;
        setWillNotDraw(false);
        setLayerType(2, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static h b(View view, ViewGroup viewGroup, Matrix matrix) {
        f fVar;
        if (view.getParent() instanceof ViewGroup) {
            f b9 = f.b(viewGroup);
            h e8 = e(view);
            int i8 = 0;
            if (e8 != null && (fVar = (f) e8.getParent()) != b9) {
                i8 = e8.f7566d;
                fVar.removeView(e8);
                e8 = null;
            }
            if (e8 == null) {
                if (matrix == null) {
                    matrix = new Matrix();
                    c(view, viewGroup, matrix);
                }
                e8 = new h(view);
                e8.h(matrix);
                if (b9 == null) {
                    b9 = new f(viewGroup);
                } else {
                    b9.g();
                }
                d(viewGroup, b9);
                d(viewGroup, e8);
                b9.a(e8);
                e8.f7566d = i8;
            } else if (matrix != null) {
                e8.h(matrix);
            }
            e8.f7566d++;
            return e8;
        }
        throw new IllegalArgumentException("Ghosted views must be parented by a ViewGroup");
    }

    static void c(View view, ViewGroup viewGroup, Matrix matrix) {
        ViewGroup viewGroup2 = (ViewGroup) view.getParent();
        matrix.reset();
        f0.j(viewGroup2, matrix);
        matrix.preTranslate(-viewGroup2.getScrollX(), -viewGroup2.getScrollY());
        f0.k(viewGroup, matrix);
    }

    static void d(View view, View view2) {
        f0.g(view2, view2.getLeft(), view2.getTop(), view2.getLeft() + view.getWidth(), view2.getTop() + view.getHeight());
    }

    static h e(View view) {
        return (h) view.getTag(x1.b.f23760a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void f(View view) {
        h e8 = e(view);
        if (e8 != null) {
            int i8 = e8.f7566d - 1;
            e8.f7566d = i8;
            if (i8 <= 0) {
                ((f) e8.getParent()).removeView(e8);
            }
        }
    }

    static void g(View view, h hVar) {
        view.setTag(x1.b.f23760a, hVar);
    }

    @Override // androidx.transition.e
    public void a(ViewGroup viewGroup, View view) {
        this.f7563a = viewGroup;
        this.f7564b = view;
    }

    void h(Matrix matrix) {
        this.f7567e = matrix;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        g(this.f7565c, this);
        this.f7565c.getViewTreeObserver().addOnPreDrawListener(this.f7568f);
        f0.i(this.f7565c, 4);
        if (this.f7565c.getParent() != null) {
            ((View) this.f7565c.getParent()).invalidate();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        this.f7565c.getViewTreeObserver().removeOnPreDrawListener(this.f7568f);
        f0.i(this.f7565c, 0);
        g(this.f7565c, null);
        if (this.f7565c.getParent() != null) {
            ((View) this.f7565c.getParent()).invalidate();
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        b.a(canvas, true);
        canvas.setMatrix(this.f7567e);
        f0.i(this.f7565c, 0);
        this.f7565c.invalidate();
        f0.i(this.f7565c, 4);
        drawChild(canvas, this.f7565c, getDrawingTime());
        b.a(canvas, false);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
    }

    @Override // android.view.View, androidx.transition.e
    public void setVisibility(int i8) {
        super.setVisibility(i8);
        if (e(this.f7565c) == this) {
            f0.i(this.f7565c, i8 == 0 ? 4 : 0);
        }
    }
}
