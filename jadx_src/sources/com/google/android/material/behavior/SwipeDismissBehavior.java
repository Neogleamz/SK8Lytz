package com.google.android.material.behavior;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.accessibility.c;
import androidx.core.view.accessibility.f;
import androidx.core.view.c0;
import w0.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class SwipeDismissBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    /* renamed from: a  reason: collision with root package name */
    w0.c f17462a;

    /* renamed from: b  reason: collision with root package name */
    c f17463b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f17464c;

    /* renamed from: e  reason: collision with root package name */
    private boolean f17466e;

    /* renamed from: d  reason: collision with root package name */
    private float f17465d = 0.0f;

    /* renamed from: f  reason: collision with root package name */
    int f17467f = 2;

    /* renamed from: g  reason: collision with root package name */
    float f17468g = 0.5f;

    /* renamed from: h  reason: collision with root package name */
    float f17469h = 0.0f;

    /* renamed from: i  reason: collision with root package name */
    float f17470i = 0.5f;

    /* renamed from: j  reason: collision with root package name */
    private final c.AbstractC0221c f17471j = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends c.AbstractC0221c {

        /* renamed from: a  reason: collision with root package name */
        private int f17472a;

        /* renamed from: b  reason: collision with root package name */
        private int f17473b = -1;

        a() {
        }

        private boolean n(View view, float f5) {
            int i8 = (f5 > 0.0f ? 1 : (f5 == 0.0f ? 0 : -1));
            if (i8 == 0) {
                return Math.abs(view.getLeft() - this.f17472a) >= Math.round(((float) view.getWidth()) * SwipeDismissBehavior.this.f17468g);
            }
            boolean z4 = c0.E(view) == 1;
            int i9 = SwipeDismissBehavior.this.f17467f;
            if (i9 == 2) {
                return true;
            }
            if (i9 == 0) {
                if (z4) {
                    if (f5 >= 0.0f) {
                        return false;
                    }
                } else if (i8 <= 0) {
                    return false;
                }
                return true;
            } else if (i9 == 1) {
                if (z4) {
                    if (i8 <= 0) {
                        return false;
                    }
                } else if (f5 >= 0.0f) {
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x001c, code lost:
            r5 = r2.f17472a;
            r3 = r3.getWidth() + r5;
         */
        /* JADX WARN: Code restructure failed: missing block: B:12:0x0026, code lost:
            if (r5 != false) goto L11;
         */
        /* JADX WARN: Code restructure failed: missing block: B:8:0x0010, code lost:
            if (r5 != false) goto L7;
         */
        /* JADX WARN: Code restructure failed: missing block: B:9:0x0012, code lost:
            r5 = r2.f17472a - r3.getWidth();
            r3 = r2.f17472a;
         */
        @Override // w0.c.AbstractC0221c
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public int a(android.view.View r3, int r4, int r5) {
            /*
                r2 = this;
                int r5 = androidx.core.view.c0.E(r3)
                r0 = 1
                if (r5 != r0) goto L9
                r5 = r0
                goto La
            L9:
                r5 = 0
            La:
                com.google.android.material.behavior.SwipeDismissBehavior r1 = com.google.android.material.behavior.SwipeDismissBehavior.this
                int r1 = r1.f17467f
                if (r1 != 0) goto L24
                if (r5 == 0) goto L1c
            L12:
                int r5 = r2.f17472a
                int r3 = r3.getWidth()
                int r5 = r5 - r3
                int r3 = r2.f17472a
                goto L37
            L1c:
                int r5 = r2.f17472a
                int r3 = r3.getWidth()
                int r3 = r3 + r5
                goto L37
            L24:
                if (r1 != r0) goto L29
                if (r5 == 0) goto L12
                goto L1c
            L29:
                int r5 = r2.f17472a
                int r0 = r3.getWidth()
                int r5 = r5 - r0
                int r0 = r2.f17472a
                int r3 = r3.getWidth()
                int r3 = r3 + r0
            L37:
                int r3 = com.google.android.material.behavior.SwipeDismissBehavior.G(r5, r4, r3)
                return r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.behavior.SwipeDismissBehavior.a.a(android.view.View, int, int):int");
        }

        @Override // w0.c.AbstractC0221c
        public int b(View view, int i8, int i9) {
            return view.getTop();
        }

        @Override // w0.c.AbstractC0221c
        public int d(View view) {
            return view.getWidth();
        }

        @Override // w0.c.AbstractC0221c
        public void i(View view, int i8) {
            this.f17473b = i8;
            this.f17472a = view.getLeft();
            ViewParent parent = view.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }

        @Override // w0.c.AbstractC0221c
        public void j(int i8) {
            c cVar = SwipeDismissBehavior.this.f17463b;
            if (cVar != null) {
                cVar.b(i8);
            }
        }

        @Override // w0.c.AbstractC0221c
        public void k(View view, int i8, int i9, int i10, int i11) {
            float width = this.f17472a + (view.getWidth() * SwipeDismissBehavior.this.f17469h);
            float width2 = this.f17472a + (view.getWidth() * SwipeDismissBehavior.this.f17470i);
            float f5 = i8;
            if (f5 <= width) {
                view.setAlpha(1.0f);
            } else if (f5 >= width2) {
                view.setAlpha(0.0f);
            } else {
                view.setAlpha(SwipeDismissBehavior.F(0.0f, 1.0f - SwipeDismissBehavior.I(width, width2, f5), 1.0f));
            }
        }

        @Override // w0.c.AbstractC0221c
        public void l(View view, float f5, float f8) {
            int i8;
            boolean z4;
            c cVar;
            this.f17473b = -1;
            int width = view.getWidth();
            if (n(view, f5)) {
                int left = view.getLeft();
                int i9 = this.f17472a;
                i8 = left < i9 ? i9 - width : i9 + width;
                z4 = true;
            } else {
                i8 = this.f17472a;
                z4 = false;
            }
            if (SwipeDismissBehavior.this.f17462a.P(i8, view.getTop())) {
                c0.l0(view, new d(view, z4));
            } else if (!z4 || (cVar = SwipeDismissBehavior.this.f17463b) == null) {
            } else {
                cVar.a(view);
            }
        }

        @Override // w0.c.AbstractC0221c
        public boolean m(View view, int i8) {
            int i9 = this.f17473b;
            return (i9 == -1 || i9 == i8) && SwipeDismissBehavior.this.E(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements f {
        b() {
        }

        @Override // androidx.core.view.accessibility.f
        public boolean a(View view, f.a aVar) {
            boolean z4 = false;
            if (SwipeDismissBehavior.this.E(view)) {
                boolean z8 = c0.E(view) == 1;
                int i8 = SwipeDismissBehavior.this.f17467f;
                if ((i8 == 0 && z8) || (i8 == 1 && !z8)) {
                    z4 = true;
                }
                int width = view.getWidth();
                if (z4) {
                    width = -width;
                }
                c0.c0(view, width);
                view.setAlpha(0.0f);
                c cVar = SwipeDismissBehavior.this.f17463b;
                if (cVar != null) {
                    cVar.a(view);
                }
                return true;
            }
            return false;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        void a(View view);

        void b(int i8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class d implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        private final View f17476a;

        /* renamed from: b  reason: collision with root package name */
        private final boolean f17477b;

        d(View view, boolean z4) {
            this.f17476a = view;
            this.f17477b = z4;
        }

        @Override // java.lang.Runnable
        public void run() {
            c cVar;
            w0.c cVar2 = SwipeDismissBehavior.this.f17462a;
            if (cVar2 != null && cVar2.n(true)) {
                c0.l0(this.f17476a, this);
            } else if (!this.f17477b || (cVar = SwipeDismissBehavior.this.f17463b) == null) {
            } else {
                cVar.a(this.f17476a);
            }
        }
    }

    static float F(float f5, float f8, float f9) {
        return Math.min(Math.max(f5, f8), f9);
    }

    static int G(int i8, int i9, int i10) {
        return Math.min(Math.max(i8, i9), i10);
    }

    private void H(ViewGroup viewGroup) {
        if (this.f17462a == null) {
            this.f17462a = this.f17466e ? w0.c.o(viewGroup, this.f17465d, this.f17471j) : w0.c.p(viewGroup, this.f17471j);
        }
    }

    static float I(float f5, float f8, float f9) {
        return (f9 - f5) / (f8 - f5);
    }

    private void N(View view) {
        c0.n0(view, 1048576);
        if (E(view)) {
            c0.p0(view, c.a.f4926y, null, new b());
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean D(CoordinatorLayout coordinatorLayout, V v8, MotionEvent motionEvent) {
        w0.c cVar = this.f17462a;
        if (cVar != null) {
            cVar.G(motionEvent);
            return true;
        }
        return false;
    }

    public boolean E(View view) {
        return true;
    }

    public void J(float f5) {
        this.f17470i = F(0.0f, f5, 1.0f);
    }

    public void K(c cVar) {
        this.f17463b = cVar;
    }

    public void L(float f5) {
        this.f17469h = F(0.0f, f5, 1.0f);
    }

    public void M(int i8) {
        this.f17467f = i8;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean k(CoordinatorLayout coordinatorLayout, V v8, MotionEvent motionEvent) {
        boolean z4 = this.f17464c;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            z4 = coordinatorLayout.F(v8, (int) motionEvent.getX(), (int) motionEvent.getY());
            this.f17464c = z4;
        } else if (actionMasked == 1 || actionMasked == 3) {
            this.f17464c = false;
        }
        if (z4) {
            H(coordinatorLayout);
            return this.f17462a.Q(motionEvent);
        }
        return false;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean l(CoordinatorLayout coordinatorLayout, V v8, int i8) {
        boolean l8 = super.l(coordinatorLayout, v8, i8);
        if (c0.C(v8) == 0) {
            c0.E0(v8, 1);
            N(v8);
        }
        return l8;
    }
}
