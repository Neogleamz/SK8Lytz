package com.google.android.material.internal;

import android.annotation.SuppressLint;
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
public class p implements r {

    /* renamed from: a  reason: collision with root package name */
    protected a f18151a;

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"ViewConstructor", "PrivateApi"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a extends ViewGroup {

        /* renamed from: f  reason: collision with root package name */
        static Method f18152f;

        /* renamed from: a  reason: collision with root package name */
        ViewGroup f18153a;

        /* renamed from: b  reason: collision with root package name */
        View f18154b;

        /* renamed from: c  reason: collision with root package name */
        ArrayList<Drawable> f18155c;

        /* renamed from: d  reason: collision with root package name */
        p f18156d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f18157e;

        static {
            try {
                Class cls = Integer.TYPE;
                f18152f = ViewGroup.class.getDeclaredMethod("invalidateChildInParentFast", cls, cls, Rect.class);
            } catch (NoSuchMethodException unused) {
            }
        }

        a(Context context, ViewGroup viewGroup, View view, p pVar) {
            super(context);
            this.f18155c = null;
            this.f18153a = viewGroup;
            this.f18154b = view;
            setRight(viewGroup.getWidth());
            setBottom(viewGroup.getHeight());
            viewGroup.addView(this);
            this.f18156d = pVar;
        }

        private void b() {
            if (this.f18157e) {
                throw new IllegalStateException("This overlay was disposed already. Please use a new one via ViewGroupUtils.getOverlay()");
            }
        }

        private void c() {
            if (getChildCount() == 0) {
                ArrayList<Drawable> arrayList = this.f18155c;
                if (arrayList == null || arrayList.size() == 0) {
                    this.f18157e = true;
                    this.f18153a.removeView(this);
                }
            }
        }

        private void d(int[] iArr) {
            int[] iArr2 = new int[2];
            int[] iArr3 = new int[2];
            this.f18153a.getLocationOnScreen(iArr2);
            this.f18154b.getLocationOnScreen(iArr3);
            iArr[0] = iArr3[0] - iArr2[0];
            iArr[1] = iArr3[1] - iArr2[1];
        }

        public void a(Drawable drawable) {
            b();
            if (this.f18155c == null) {
                this.f18155c = new ArrayList<>();
            }
            if (this.f18155c.contains(drawable)) {
                return;
            }
            this.f18155c.add(drawable);
            invalidate(drawable.getBounds());
            drawable.setCallback(this);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            int[] iArr = new int[2];
            int[] iArr2 = new int[2];
            this.f18153a.getLocationOnScreen(iArr);
            this.f18154b.getLocationOnScreen(iArr2);
            canvas.translate(iArr2[0] - iArr[0], iArr2[1] - iArr[1]);
            canvas.clipRect(new Rect(0, 0, this.f18154b.getWidth(), this.f18154b.getHeight()));
            super.dispatchDraw(canvas);
            ArrayList<Drawable> arrayList = this.f18155c;
            int size = arrayList == null ? 0 : arrayList.size();
            for (int i8 = 0; i8 < size; i8++) {
                this.f18155c.get(i8).draw(canvas);
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        public void e(Drawable drawable) {
            ArrayList<Drawable> arrayList = this.f18155c;
            if (arrayList != null) {
                arrayList.remove(drawable);
                invalidate(drawable.getBounds());
                drawable.setCallback(null);
                c();
            }
        }

        @Override // android.view.ViewGroup, android.view.ViewParent
        public ViewParent invalidateChildInParent(int[] iArr, Rect rect) {
            if (this.f18153a != null) {
                rect.offset(iArr[0], iArr[1]);
                if (this.f18153a == null) {
                    invalidate(rect);
                    return null;
                }
                iArr[0] = 0;
                iArr[1] = 0;
                int[] iArr2 = new int[2];
                d(iArr2);
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
            return super.verifyDrawable(drawable) || ((arrayList = this.f18155c) != null && arrayList.contains(drawable));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(Context context, ViewGroup viewGroup, View view) {
        this.f18151a = new a(context, viewGroup, view, this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static p c(View view) {
        ViewGroup d8 = s.d(view);
        if (d8 != null) {
            int childCount = d8.getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                View childAt = d8.getChildAt(i8);
                if (childAt instanceof a) {
                    return ((a) childAt).f18156d;
                }
            }
            return new o(d8.getContext(), d8, view);
        }
        return null;
    }

    @Override // com.google.android.material.internal.r
    public void a(Drawable drawable) {
        this.f18151a.a(drawable);
    }

    @Override // com.google.android.material.internal.r
    public void b(Drawable drawable) {
        this.f18151a.e(drawable);
    }
}
