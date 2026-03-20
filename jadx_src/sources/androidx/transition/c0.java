package androidx.transition;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.reflect.Method;
import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c0 implements e0 {

    /* renamed from: a  reason: collision with root package name */
    protected a f7524a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a extends ViewGroup {

        /* renamed from: f  reason: collision with root package name */
        static Method f7525f;

        /* renamed from: a  reason: collision with root package name */
        ViewGroup f7526a;

        /* renamed from: b  reason: collision with root package name */
        View f7527b;

        /* renamed from: c  reason: collision with root package name */
        ArrayList<Drawable> f7528c;

        /* renamed from: d  reason: collision with root package name */
        c0 f7529d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f7530e;

        static {
            try {
                Class cls = Integer.TYPE;
                f7525f = ViewGroup.class.getDeclaredMethod("invalidateChildInParentFast", cls, cls, Rect.class);
            } catch (NoSuchMethodException unused) {
            }
        }

        a(Context context, ViewGroup viewGroup, View view, c0 c0Var) {
            super(context);
            this.f7528c = null;
            this.f7526a = viewGroup;
            this.f7527b = view;
            setRight(viewGroup.getWidth());
            setBottom(viewGroup.getHeight());
            viewGroup.addView(this);
            this.f7529d = c0Var;
        }

        private void c() {
            if (this.f7530e) {
                throw new IllegalStateException("This overlay was disposed already. Please use a new one via ViewGroupUtils.getOverlay()");
            }
        }

        private void d() {
            if (getChildCount() == 0) {
                ArrayList<Drawable> arrayList = this.f7528c;
                if (arrayList == null || arrayList.size() == 0) {
                    this.f7530e = true;
                    this.f7526a.removeView(this);
                }
            }
        }

        private void e(int[] iArr) {
            int[] iArr2 = new int[2];
            int[] iArr3 = new int[2];
            this.f7526a.getLocationOnScreen(iArr2);
            this.f7527b.getLocationOnScreen(iArr3);
            iArr[0] = iArr3[0] - iArr2[0];
            iArr[1] = iArr3[1] - iArr2[1];
        }

        public void a(Drawable drawable) {
            c();
            if (this.f7528c == null) {
                this.f7528c = new ArrayList<>();
            }
            if (this.f7528c.contains(drawable)) {
                return;
            }
            this.f7528c.add(drawable);
            invalidate(drawable.getBounds());
            drawable.setCallback(this);
        }

        public void b(View view) {
            c();
            if (view.getParent() instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view.getParent();
                if (viewGroup != this.f7526a && viewGroup.getParent() != null && androidx.core.view.c0.V(viewGroup)) {
                    int[] iArr = new int[2];
                    int[] iArr2 = new int[2];
                    viewGroup.getLocationOnScreen(iArr);
                    this.f7526a.getLocationOnScreen(iArr2);
                    androidx.core.view.c0.c0(view, iArr[0] - iArr2[0]);
                    androidx.core.view.c0.d0(view, iArr[1] - iArr2[1]);
                }
                viewGroup.removeView(view);
                if (view.getParent() != null) {
                    viewGroup.removeView(view);
                }
            }
            super.addView(view);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            int[] iArr = new int[2];
            int[] iArr2 = new int[2];
            this.f7526a.getLocationOnScreen(iArr);
            this.f7527b.getLocationOnScreen(iArr2);
            canvas.translate(iArr2[0] - iArr[0], iArr2[1] - iArr[1]);
            canvas.clipRect(new Rect(0, 0, this.f7527b.getWidth(), this.f7527b.getHeight()));
            super.dispatchDraw(canvas);
            ArrayList<Drawable> arrayList = this.f7528c;
            int size = arrayList == null ? 0 : arrayList.size();
            for (int i8 = 0; i8 < size; i8++) {
                this.f7528c.get(i8).draw(canvas);
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        public void f(Drawable drawable) {
            ArrayList<Drawable> arrayList = this.f7528c;
            if (arrayList != null) {
                arrayList.remove(drawable);
                invalidate(drawable.getBounds());
                drawable.setCallback(null);
                d();
            }
        }

        public void g(View view) {
            super.removeView(view);
            d();
        }

        @Override // android.view.ViewGroup, android.view.ViewParent
        public ViewParent invalidateChildInParent(int[] iArr, Rect rect) {
            if (this.f7526a != null) {
                rect.offset(iArr[0], iArr[1]);
                if (!(this.f7526a instanceof ViewGroup)) {
                    invalidate(rect);
                    return null;
                }
                iArr[0] = 0;
                iArr[1] = 0;
                int[] iArr2 = new int[2];
                e(iArr2);
                rect.offset(iArr2[0], iArr2[1]);
                return super.invalidateChildInParent(iArr, rect);
            }
            return null;
        }

        @Override // android.view.View, android.graphics.drawable.Drawable.Callback
        public void invalidateDrawable(Drawable drawable) {
            invalidate(drawable.getBounds());
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        }

        @Override // android.view.View
        protected boolean verifyDrawable(Drawable drawable) {
            ArrayList<Drawable> arrayList;
            return super.verifyDrawable(drawable) || ((arrayList = this.f7528c) != null && arrayList.contains(drawable));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c0(Context context, ViewGroup viewGroup, View view) {
        this.f7524a = new a(context, viewGroup, view, this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static c0 e(View view) {
        ViewGroup f5 = f(view);
        if (f5 != null) {
            int childCount = f5.getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                View childAt = f5.getChildAt(i8);
                if (childAt instanceof a) {
                    return ((a) childAt).f7529d;
                }
            }
            return new x(f5.getContext(), f5, view);
        }
        return null;
    }

    static ViewGroup f(View view) {
        while (view != null) {
            if (view.getId() == 16908290 && (view instanceof ViewGroup)) {
                return (ViewGroup) view;
            }
            if (view.getParent() instanceof ViewGroup) {
                view = (ViewGroup) view.getParent();
            }
        }
        return null;
    }

    @Override // androidx.transition.e0
    public void a(Drawable drawable) {
        this.f7524a.a(drawable);
    }

    @Override // androidx.transition.e0
    public void b(Drawable drawable) {
        this.f7524a.f(drawable);
    }
}
