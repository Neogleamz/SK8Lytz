package androidx.core.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContentInfo;
import android.view.Display;
import android.view.KeyEvent;
import android.view.OnReceiveContentListener;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.core.view.a;
import androidx.core.view.accessibility.c;
import androidx.core.view.c0;
import androidx.core.view.m0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
@SuppressLint({"PrivateConstructorForUtilityClass"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c0 {

    /* renamed from: b  reason: collision with root package name */
    private static Field f4957b;

    /* renamed from: c  reason: collision with root package name */
    private static boolean f4958c;

    /* renamed from: d  reason: collision with root package name */
    private static Field f4959d;

    /* renamed from: e  reason: collision with root package name */
    private static boolean f4960e;

    /* renamed from: f  reason: collision with root package name */
    private static WeakHashMap<View, String> f4961f;

    /* renamed from: h  reason: collision with root package name */
    private static Field f4963h;

    /* renamed from: j  reason: collision with root package name */
    private static ThreadLocal<Rect> f4965j;

    /* renamed from: a  reason: collision with root package name */
    private static final AtomicInteger f4956a = new AtomicInteger(1);

    /* renamed from: g  reason: collision with root package name */
    private static WeakHashMap<View, i0> f4962g = null;

    /* renamed from: i  reason: collision with root package name */
    private static boolean f4964i = false;

    /* renamed from: k  reason: collision with root package name */
    private static final int[] f4966k = {q0.e.f22458b, q0.e.f22460c, q0.e.f22482n, q0.e.f22492y, q0.e.B, q0.e.C, q0.e.D, q0.e.E, q0.e.F, q0.e.G, q0.e.f22462d, q0.e.f22464e, q0.e.f22466f, q0.e.f22468g, q0.e.f22470h, q0.e.f22472i, q0.e.f22474j, q0.e.f22476k, q0.e.f22478l, q0.e.f22480m, q0.e.f22483o, q0.e.f22484p, q0.e.q, q0.e.f22485r, q0.e.f22486s, q0.e.f22487t, q0.e.f22488u, q0.e.f22489v, q0.e.f22490w, q0.e.f22491x, q0.e.f22493z, q0.e.A};

    /* renamed from: l  reason: collision with root package name */
    private static final x f4967l = new x() { // from class: androidx.core.view.b0
        @Override // androidx.core.view.x
        public final c a(c cVar) {
            c a02;
            a02 = c0.a0(cVar);
            return a02;
        }
    };

    /* renamed from: m  reason: collision with root package name */
    private static final e f4968m = new e();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends f<Boolean> {
        a(int i8, Class cls, int i9) {
            super(i8, cls, i9);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.c0.f
        /* renamed from: i */
        public Boolean d(View view) {
            return Boolean.valueOf(q.d(view));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.c0.f
        /* renamed from: j */
        public void e(View view, Boolean bool) {
            q.i(view, bool.booleanValue());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.c0.f
        /* renamed from: k */
        public boolean h(Boolean bool, Boolean bool2) {
            return !a(bool, bool2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends f<CharSequence> {
        b(int i8, Class cls, int i9, int i10) {
            super(i8, cls, i9, i10);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.c0.f
        /* renamed from: i */
        public CharSequence d(View view) {
            return q.b(view);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.c0.f
        /* renamed from: j */
        public void e(View view, CharSequence charSequence) {
            q.h(view, charSequence);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.c0.f
        /* renamed from: k */
        public boolean h(CharSequence charSequence, CharSequence charSequence2) {
            return !TextUtils.equals(charSequence, charSequence2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends f<CharSequence> {
        c(int i8, Class cls, int i9, int i10) {
            super(i8, cls, i9, i10);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.c0.f
        /* renamed from: i */
        public CharSequence d(View view) {
            return s.a(view);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.c0.f
        /* renamed from: j */
        public void e(View view, CharSequence charSequence) {
            s.b(view, charSequence);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.c0.f
        /* renamed from: k */
        public boolean h(CharSequence charSequence, CharSequence charSequence2) {
            return !TextUtils.equals(charSequence, charSequence2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends f<Boolean> {
        d(int i8, Class cls, int i9) {
            super(i8, cls, i9);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.c0.f
        /* renamed from: i */
        public Boolean d(View view) {
            return Boolean.valueOf(q.c(view));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.c0.f
        /* renamed from: j */
        public void e(View view, Boolean bool) {
            q.g(view, bool.booleanValue());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.c0.f
        /* renamed from: k */
        public boolean h(Boolean bool, Boolean bool2) {
            return !a(bool, bool2);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class e implements ViewTreeObserver.OnGlobalLayoutListener, View.OnAttachStateChangeListener {

        /* renamed from: a  reason: collision with root package name */
        private final WeakHashMap<View, Boolean> f4969a = new WeakHashMap<>();

        e() {
        }

        private void b(View view, boolean z4) {
            boolean z8 = view.isShown() && view.getWindowVisibility() == 0;
            if (z4 != z8) {
                c0.b0(view, z8 ? 16 : 32);
                this.f4969a.put(view, Boolean.valueOf(z8));
            }
        }

        private void c(View view) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        private void e(View view) {
            h.o(view.getViewTreeObserver(), this);
        }

        void a(View view) {
            this.f4969a.put(view, Boolean.valueOf(view.isShown() && view.getWindowVisibility() == 0));
            view.addOnAttachStateChangeListener(this);
            if (k.b(view)) {
                c(view);
            }
        }

        void d(View view) {
            this.f4969a.remove(view);
            view.removeOnAttachStateChangeListener(this);
            e(view);
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (Build.VERSION.SDK_INT < 28) {
                for (Map.Entry<View, Boolean> entry : this.f4969a.entrySet()) {
                    b(entry.getKey(), entry.getValue().booleanValue());
                }
            }
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            c(view);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class f<T> {

        /* renamed from: a  reason: collision with root package name */
        private final int f4970a;

        /* renamed from: b  reason: collision with root package name */
        private final Class<T> f4971b;

        /* renamed from: c  reason: collision with root package name */
        private final int f4972c;

        /* renamed from: d  reason: collision with root package name */
        private final int f4973d;

        f(int i8, Class<T> cls, int i9) {
            this(i8, cls, 0, i9);
        }

        f(int i8, Class<T> cls, int i9, int i10) {
            this.f4970a = i8;
            this.f4971b = cls;
            this.f4973d = i9;
            this.f4972c = i10;
        }

        private boolean b() {
            return Build.VERSION.SDK_INT >= 19;
        }

        private boolean c() {
            return Build.VERSION.SDK_INT >= this.f4972c;
        }

        boolean a(Boolean bool, Boolean bool2) {
            return (bool != null && bool.booleanValue()) == (bool2 != null && bool2.booleanValue());
        }

        abstract T d(View view);

        abstract void e(View view, T t8);

        T f(View view) {
            if (c()) {
                return d(view);
            }
            if (b()) {
                T t8 = (T) view.getTag(this.f4970a);
                if (this.f4971b.isInstance(t8)) {
                    return t8;
                }
                return null;
            }
            return null;
        }

        void g(View view, T t8) {
            if (c()) {
                e(view, t8);
            } else if (b() && h(f(view), t8)) {
                c0.l(view);
                view.setTag(this.f4970a, t8);
                c0.b0(view, this.f4973d);
            }
        }

        abstract boolean h(T t8, T t9);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class g {
        static boolean a(View view) {
            return view.hasOnClickListeners();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h {
        static AccessibilityNodeProvider a(View view) {
            return view.getAccessibilityNodeProvider();
        }

        static boolean b(View view) {
            return view.getFitsSystemWindows();
        }

        static int c(View view) {
            return view.getImportantForAccessibility();
        }

        static int d(View view) {
            return view.getMinimumHeight();
        }

        static int e(View view) {
            return view.getMinimumWidth();
        }

        static ViewParent f(View view) {
            return view.getParentForAccessibility();
        }

        static int g(View view) {
            return view.getWindowSystemUiVisibility();
        }

        static boolean h(View view) {
            return view.hasOverlappingRendering();
        }

        static boolean i(View view) {
            return view.hasTransientState();
        }

        static boolean j(View view, int i8, Bundle bundle) {
            return view.performAccessibilityAction(i8, bundle);
        }

        static void k(View view) {
            view.postInvalidateOnAnimation();
        }

        static void l(View view, int i8, int i9, int i10, int i11) {
            view.postInvalidateOnAnimation(i8, i9, i10, i11);
        }

        static void m(View view, Runnable runnable) {
            view.postOnAnimation(runnable);
        }

        static void n(View view, Runnable runnable, long j8) {
            view.postOnAnimationDelayed(runnable, j8);
        }

        static void o(ViewTreeObserver viewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
            viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }

        static void p(View view) {
            view.requestFitSystemWindows();
        }

        static void q(View view, Drawable drawable) {
            view.setBackground(drawable);
        }

        static void r(View view, boolean z4) {
            view.setHasTransientState(z4);
        }

        static void s(View view, int i8) {
            view.setImportantForAccessibility(i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class i {
        static int a() {
            return View.generateViewId();
        }

        static Display b(View view) {
            return view.getDisplay();
        }

        static int c(View view) {
            return view.getLabelFor();
        }

        static int d(View view) {
            return view.getLayoutDirection();
        }

        static int e(View view) {
            return view.getPaddingEnd();
        }

        static int f(View view) {
            return view.getPaddingStart();
        }

        static boolean g(View view) {
            return view.isPaddingRelative();
        }

        static void h(View view, int i8) {
            view.setLabelFor(i8);
        }

        static void i(View view, Paint paint) {
            view.setLayerPaint(paint);
        }

        static void j(View view, int i8) {
            view.setLayoutDirection(i8);
        }

        static void k(View view, int i8, int i9, int i10, int i11) {
            view.setPaddingRelative(i8, i9, i10, i11);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class j {
        static Rect a(View view) {
            return view.getClipBounds();
        }

        static boolean b(View view) {
            return view.isInLayout();
        }

        static void c(View view, Rect rect) {
            view.setClipBounds(rect);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class k {
        static int a(View view) {
            return view.getAccessibilityLiveRegion();
        }

        static boolean b(View view) {
            return view.isAttachedToWindow();
        }

        static boolean c(View view) {
            return view.isLaidOut();
        }

        static boolean d(View view) {
            return view.isLayoutDirectionResolved();
        }

        static void e(ViewParent viewParent, View view, View view2, int i8) {
            viewParent.notifySubtreeAccessibilityStateChanged(view, view2, i8);
        }

        static void f(View view, int i8) {
            view.setAccessibilityLiveRegion(i8);
        }

        static void g(AccessibilityEvent accessibilityEvent, int i8) {
            accessibilityEvent.setContentChangeTypes(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class l {
        static WindowInsets a(View view, WindowInsets windowInsets) {
            return view.dispatchApplyWindowInsets(windowInsets);
        }

        static WindowInsets b(View view, WindowInsets windowInsets) {
            return view.onApplyWindowInsets(windowInsets);
        }

        static void c(View view) {
            view.requestApplyInsets();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class m {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a implements View.OnApplyWindowInsetsListener {

            /* renamed from: a  reason: collision with root package name */
            m0 f4974a = null;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ View f4975b;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ androidx.core.view.v f4976c;

            a(View view, androidx.core.view.v vVar) {
                this.f4975b = view;
                this.f4976c = vVar;
            }

            @Override // android.view.View.OnApplyWindowInsetsListener
            public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                m0 z4 = m0.z(windowInsets, view);
                int i8 = Build.VERSION.SDK_INT;
                if (i8 < 30) {
                    m.a(windowInsets, this.f4975b);
                    if (z4.equals(this.f4974a)) {
                        return this.f4976c.a(view, z4).x();
                    }
                }
                this.f4974a = z4;
                m0 a9 = this.f4976c.a(view, z4);
                if (i8 >= 30) {
                    return a9.x();
                }
                c0.q0(view);
                return a9.x();
            }
        }

        static void a(WindowInsets windowInsets, View view) {
            View.OnApplyWindowInsetsListener onApplyWindowInsetsListener = (View.OnApplyWindowInsetsListener) view.getTag(q0.e.f22473i0);
            if (onApplyWindowInsetsListener != null) {
                onApplyWindowInsetsListener.onApplyWindowInsets(view, windowInsets);
            }
        }

        static m0 b(View view, m0 m0Var, Rect rect) {
            WindowInsets x8 = m0Var.x();
            if (x8 != null) {
                return m0.z(view.computeSystemWindowInsets(x8, rect), view);
            }
            rect.setEmpty();
            return m0Var;
        }

        static boolean c(View view, float f5, float f8, boolean z4) {
            return view.dispatchNestedFling(f5, f8, z4);
        }

        static boolean d(View view, float f5, float f8) {
            return view.dispatchNestedPreFling(f5, f8);
        }

        static boolean e(View view, int i8, int i9, int[] iArr, int[] iArr2) {
            return view.dispatchNestedPreScroll(i8, i9, iArr, iArr2);
        }

        static boolean f(View view, int i8, int i9, int i10, int i11, int[] iArr) {
            return view.dispatchNestedScroll(i8, i9, i10, i11, iArr);
        }

        static ColorStateList g(View view) {
            return view.getBackgroundTintList();
        }

        static PorterDuff.Mode h(View view) {
            return view.getBackgroundTintMode();
        }

        static float i(View view) {
            return view.getElevation();
        }

        public static m0 j(View view) {
            return m0.a.a(view);
        }

        static String k(View view) {
            return view.getTransitionName();
        }

        static float l(View view) {
            return view.getTranslationZ();
        }

        static float m(View view) {
            return view.getZ();
        }

        static boolean n(View view) {
            return view.hasNestedScrollingParent();
        }

        static boolean o(View view) {
            return view.isImportantForAccessibility();
        }

        static boolean p(View view) {
            return view.isNestedScrollingEnabled();
        }

        static void q(View view, ColorStateList colorStateList) {
            view.setBackgroundTintList(colorStateList);
        }

        static void r(View view, PorterDuff.Mode mode) {
            view.setBackgroundTintMode(mode);
        }

        static void s(View view, float f5) {
            view.setElevation(f5);
        }

        static void t(View view, boolean z4) {
            view.setNestedScrollingEnabled(z4);
        }

        static void u(View view, androidx.core.view.v vVar) {
            if (Build.VERSION.SDK_INT < 30) {
                view.setTag(q0.e.f22457a0, vVar);
            }
            if (vVar == null) {
                view.setOnApplyWindowInsetsListener((View.OnApplyWindowInsetsListener) view.getTag(q0.e.f22473i0));
            } else {
                view.setOnApplyWindowInsetsListener(new a(view, vVar));
            }
        }

        static void v(View view, String str) {
            view.setTransitionName(str);
        }

        static void w(View view, float f5) {
            view.setTranslationZ(f5);
        }

        static void x(View view, float f5) {
            view.setZ(f5);
        }

        static boolean y(View view, int i8) {
            return view.startNestedScroll(i8);
        }

        static void z(View view) {
            view.stopNestedScroll();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class n {
        public static m0 a(View view) {
            WindowInsets rootWindowInsets = view.getRootWindowInsets();
            if (rootWindowInsets == null) {
                return null;
            }
            m0 y8 = m0.y(rootWindowInsets);
            y8.v(y8);
            y8.d(view.getRootView());
            return y8;
        }

        static int b(View view) {
            return view.getScrollIndicators();
        }

        static void c(View view, int i8) {
            view.setScrollIndicators(i8);
        }

        static void d(View view, int i8, int i9) {
            view.setScrollIndicators(i8, i9);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class o {
        static void a(View view) {
            view.cancelDragAndDrop();
        }

        static void b(View view) {
            view.dispatchFinishTemporaryDetach();
        }

        static void c(View view) {
            view.dispatchStartTemporaryDetach();
        }

        static void d(View view, PointerIcon pointerIcon) {
            view.setPointerIcon(pointerIcon);
        }

        static boolean e(View view, ClipData clipData, View.DragShadowBuilder dragShadowBuilder, Object obj, int i8) {
            return view.startDragAndDrop(clipData, dragShadowBuilder, obj, i8);
        }

        static void f(View view, View.DragShadowBuilder dragShadowBuilder) {
            view.updateDragShadow(dragShadowBuilder);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class p {
        static void a(View view, Collection<View> collection, int i8) {
            view.addKeyboardNavigationClusters(collection, i8);
        }

        static int b(View view) {
            return view.getImportantForAutofill();
        }

        static int c(View view) {
            return view.getNextClusterForwardId();
        }

        static boolean d(View view) {
            return view.hasExplicitFocusable();
        }

        static boolean e(View view) {
            return view.isFocusedByDefault();
        }

        static boolean f(View view) {
            return view.isImportantForAutofill();
        }

        static boolean g(View view) {
            return view.isKeyboardNavigationCluster();
        }

        static View h(View view, View view2, int i8) {
            return view.keyboardNavigationClusterSearch(view2, i8);
        }

        static boolean i(View view) {
            return view.restoreDefaultFocus();
        }

        static void j(View view, String... strArr) {
            view.setAutofillHints(strArr);
        }

        static void k(View view, boolean z4) {
            view.setFocusedByDefault(z4);
        }

        static void l(View view, int i8) {
            view.setImportantForAutofill(i8);
        }

        static void m(View view, boolean z4) {
            view.setKeyboardNavigationCluster(z4);
        }

        static void n(View view, int i8) {
            view.setNextClusterForwardId(i8);
        }

        static void o(View view, CharSequence charSequence) {
            view.setTooltipText(charSequence);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class q {
        static void a(View view, final v vVar) {
            int i8 = q0.e.f22471h0;
            k0.g gVar = (k0.g) view.getTag(i8);
            if (gVar == null) {
                gVar = new k0.g();
                view.setTag(i8, gVar);
            }
            Objects.requireNonNull(vVar);
            View.OnUnhandledKeyEventListener onUnhandledKeyEventListener = new View.OnUnhandledKeyEventListener() { // from class: androidx.core.view.d0
                @Override // android.view.View.OnUnhandledKeyEventListener
                public final boolean onUnhandledKeyEvent(View view2, KeyEvent keyEvent) {
                    return c0.v.this.onUnhandledKeyEvent(view2, keyEvent);
                }
            };
            gVar.put(vVar, onUnhandledKeyEventListener);
            view.addOnUnhandledKeyEventListener(onUnhandledKeyEventListener);
        }

        static CharSequence b(View view) {
            return view.getAccessibilityPaneTitle();
        }

        static boolean c(View view) {
            return view.isAccessibilityHeading();
        }

        static boolean d(View view) {
            return view.isScreenReaderFocusable();
        }

        static void e(View view, v vVar) {
            View.OnUnhandledKeyEventListener onUnhandledKeyEventListener;
            k0.g gVar = (k0.g) view.getTag(q0.e.f22471h0);
            if (gVar == null || (onUnhandledKeyEventListener = (View.OnUnhandledKeyEventListener) gVar.get(vVar)) == null) {
                return;
            }
            view.removeOnUnhandledKeyEventListener(onUnhandledKeyEventListener);
        }

        static <T> T f(View view, int i8) {
            return (T) view.requireViewById(i8);
        }

        static void g(View view, boolean z4) {
            view.setAccessibilityHeading(z4);
        }

        static void h(View view, CharSequence charSequence) {
            view.setAccessibilityPaneTitle(charSequence);
        }

        static void i(View view, boolean z4) {
            view.setScreenReaderFocusable(z4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class r {
        static View.AccessibilityDelegate a(View view) {
            return view.getAccessibilityDelegate();
        }

        static List<Rect> b(View view) {
            return view.getSystemGestureExclusionRects();
        }

        static void c(View view, Context context, int[] iArr, AttributeSet attributeSet, TypedArray typedArray, int i8, int i9) {
            view.saveAttributeDataForStyleable(context, iArr, attributeSet, typedArray, i8, i9);
        }

        static void d(View view, List<Rect> list) {
            view.setSystemGestureExclusionRects(list);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class s {
        static CharSequence a(View view) {
            return view.getStateDescription();
        }

        static void b(View view, CharSequence charSequence) {
            view.setStateDescription(charSequence);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class t {
        public static String[] a(View view) {
            return view.getReceiveContentMimeTypes();
        }

        public static androidx.core.view.c b(View view, androidx.core.view.c cVar) {
            ContentInfo f5 = cVar.f();
            ContentInfo performReceiveContent = view.performReceiveContent(f5);
            if (performReceiveContent == null) {
                return null;
            }
            return performReceiveContent == f5 ? cVar : androidx.core.view.c.g(performReceiveContent);
        }

        public static void c(View view, String[] strArr, androidx.core.view.w wVar) {
            if (wVar == null) {
                view.setOnReceiveContentListener(strArr, null);
            } else {
                view.setOnReceiveContentListener(strArr, new u(wVar));
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class u implements OnReceiveContentListener {

        /* renamed from: a  reason: collision with root package name */
        private final androidx.core.view.w f4977a;

        u(androidx.core.view.w wVar) {
            this.f4977a = wVar;
        }

        @Override // android.view.OnReceiveContentListener
        public ContentInfo onReceiveContent(View view, ContentInfo contentInfo) {
            androidx.core.view.c g8 = androidx.core.view.c.g(contentInfo);
            androidx.core.view.c a9 = this.f4977a.a(view, g8);
            if (a9 == null) {
                return null;
            }
            return a9 == g8 ? contentInfo : a9.f();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface v {
        boolean onUnhandledKeyEvent(View view, KeyEvent keyEvent);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class w {

        /* renamed from: d  reason: collision with root package name */
        private static final ArrayList<WeakReference<View>> f4978d = new ArrayList<>();

        /* renamed from: a  reason: collision with root package name */
        private WeakHashMap<View, Boolean> f4979a = null;

        /* renamed from: b  reason: collision with root package name */
        private SparseArray<WeakReference<View>> f4980b = null;

        /* renamed from: c  reason: collision with root package name */
        private WeakReference<KeyEvent> f4981c = null;

        w() {
        }

        static w a(View view) {
            int i8 = q0.e.f22469g0;
            w wVar = (w) view.getTag(i8);
            if (wVar == null) {
                w wVar2 = new w();
                view.setTag(i8, wVar2);
                return wVar2;
            }
            return wVar;
        }

        private View c(View view, KeyEvent keyEvent) {
            WeakHashMap<View, Boolean> weakHashMap = this.f4979a;
            if (weakHashMap != null && weakHashMap.containsKey(view)) {
                if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                        View c9 = c(viewGroup.getChildAt(childCount), keyEvent);
                        if (c9 != null) {
                            return c9;
                        }
                    }
                }
                if (e(view, keyEvent)) {
                    return view;
                }
            }
            return null;
        }

        private SparseArray<WeakReference<View>> d() {
            if (this.f4980b == null) {
                this.f4980b = new SparseArray<>();
            }
            return this.f4980b;
        }

        private boolean e(View view, KeyEvent keyEvent) {
            ArrayList arrayList = (ArrayList) view.getTag(q0.e.f22471h0);
            if (arrayList != null) {
                for (int size = arrayList.size() - 1; size >= 0; size--) {
                    if (((v) arrayList.get(size)).onUnhandledKeyEvent(view, keyEvent)) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        private void g() {
            WeakHashMap<View, Boolean> weakHashMap = this.f4979a;
            if (weakHashMap != null) {
                weakHashMap.clear();
            }
            ArrayList<WeakReference<View>> arrayList = f4978d;
            if (arrayList.isEmpty()) {
                return;
            }
            synchronized (arrayList) {
                if (this.f4979a == null) {
                    this.f4979a = new WeakHashMap<>();
                }
                for (int size = arrayList.size() - 1; size >= 0; size--) {
                    ArrayList<WeakReference<View>> arrayList2 = f4978d;
                    View view = arrayList2.get(size).get();
                    if (view == null) {
                        arrayList2.remove(size);
                    } else {
                        this.f4979a.put(view, Boolean.TRUE);
                        for (ViewParent parent = view.getParent(); parent instanceof View; parent = parent.getParent()) {
                            this.f4979a.put((View) parent, Boolean.TRUE);
                        }
                    }
                }
            }
        }

        boolean b(View view, KeyEvent keyEvent) {
            if (keyEvent.getAction() == 0) {
                g();
            }
            View c9 = c(view, keyEvent);
            if (keyEvent.getAction() == 0) {
                int keyCode = keyEvent.getKeyCode();
                if (c9 != null && !KeyEvent.isModifierKey(keyCode)) {
                    d().put(keyCode, new WeakReference<>(c9));
                }
            }
            return c9 != null;
        }

        boolean f(KeyEvent keyEvent) {
            int indexOfKey;
            WeakReference<KeyEvent> weakReference = this.f4981c;
            if (weakReference == null || weakReference.get() != keyEvent) {
                this.f4981c = new WeakReference<>(keyEvent);
                WeakReference<View> weakReference2 = null;
                SparseArray<WeakReference<View>> d8 = d();
                if (keyEvent.getAction() == 1 && (indexOfKey = d8.indexOfKey(keyEvent.getKeyCode())) >= 0) {
                    weakReference2 = d8.valueAt(indexOfKey);
                    d8.removeAt(indexOfKey);
                }
                if (weakReference2 == null) {
                    weakReference2 = d8.get(keyEvent.getKeyCode());
                }
                if (weakReference2 != null) {
                    View view = weakReference2.get();
                    if (view != null && c0.V(view)) {
                        e(view, keyEvent);
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    private static x A(View view) {
        return view instanceof x ? (x) view : f4967l;
    }

    public static void A0(View view, Rect rect) {
        if (Build.VERSION.SDK_INT >= 18) {
            j.c(view, rect);
        }
    }

    public static boolean B(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return h.b(view);
        }
        return false;
    }

    public static void B0(View view, float f5) {
        if (Build.VERSION.SDK_INT >= 21) {
            m.s(view, f5);
        }
    }

    public static int C(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return h.c(view);
        }
        return 0;
    }

    @Deprecated
    public static void C0(View view, boolean z4) {
        view.setFitsSystemWindows(z4);
    }

    @SuppressLint({"InlinedApi"})
    public static int D(View view) {
        if (Build.VERSION.SDK_INT >= 26) {
            return p.b(view);
        }
        return 0;
    }

    public static void D0(View view, boolean z4) {
        if (Build.VERSION.SDK_INT >= 16) {
            h.r(view, z4);
        }
    }

    public static int E(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return i.d(view);
        }
        return 0;
    }

    public static void E0(View view, int i8) {
        int i9 = Build.VERSION.SDK_INT;
        if (i9 < 19) {
            if (i9 < 16) {
                return;
            }
            if (i8 == 4) {
                i8 = 2;
            }
        }
        h.s(view, i8);
    }

    public static int F(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return h.d(view);
        }
        if (!f4960e) {
            try {
                Field declaredField = View.class.getDeclaredField("mMinHeight");
                f4959d = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException unused) {
            }
            f4960e = true;
        }
        Field field = f4959d;
        if (field != null) {
            try {
                return ((Integer) field.get(view)).intValue();
            } catch (Exception unused2) {
                return 0;
            }
        }
        return 0;
    }

    public static void F0(View view, int i8) {
        if (Build.VERSION.SDK_INT >= 26) {
            p.l(view, i8);
        }
    }

    public static int G(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return h.e(view);
        }
        if (!f4958c) {
            try {
                Field declaredField = View.class.getDeclaredField("mMinWidth");
                f4957b = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException unused) {
            }
            f4958c = true;
        }
        Field field = f4957b;
        if (field != null) {
            try {
                return ((Integer) field.get(view)).intValue();
            } catch (Exception unused2) {
                return 0;
            }
        }
        return 0;
    }

    public static void G0(View view, Paint paint) {
        if (Build.VERSION.SDK_INT >= 17) {
            i.i(view, paint);
            return;
        }
        view.setLayerType(view.getLayerType(), paint);
        view.invalidate();
    }

    public static String[] H(View view) {
        return Build.VERSION.SDK_INT >= 31 ? t.a(view) : (String[]) view.getTag(q0.e.f22461c0);
    }

    public static void H0(View view, int i8) {
        if (Build.VERSION.SDK_INT >= 17) {
            i.j(view, i8);
        }
    }

    public static int I(View view) {
        return Build.VERSION.SDK_INT >= 17 ? i.e(view) : view.getPaddingRight();
    }

    public static void I0(View view, androidx.core.view.v vVar) {
        if (Build.VERSION.SDK_INT >= 21) {
            m.u(view, vVar);
        }
    }

    public static int J(View view) {
        return Build.VERSION.SDK_INT >= 17 ? i.f(view) : view.getPaddingLeft();
    }

    public static void J0(View view, int i8, int i9, int i10, int i11) {
        if (Build.VERSION.SDK_INT >= 17) {
            i.k(view, i8, i9, i10, i11);
        } else {
            view.setPadding(i8, i9, i10, i11);
        }
    }

    public static ViewParent K(View view) {
        return Build.VERSION.SDK_INT >= 16 ? h.f(view) : view.getParent();
    }

    public static void K0(View view, z zVar) {
        if (Build.VERSION.SDK_INT >= 24) {
            o.d(view, (PointerIcon) (zVar != null ? zVar.a() : null));
        }
    }

    public static m0 L(View view) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 23) {
            return n.a(view);
        }
        if (i8 >= 21) {
            return m.j(view);
        }
        return null;
    }

    public static void L0(View view, boolean z4) {
        s0().g(view, Boolean.valueOf(z4));
    }

    public static CharSequence M(View view) {
        return S0().f(view);
    }

    public static void M0(View view, int i8, int i9) {
        if (Build.VERSION.SDK_INT >= 23) {
            n.d(view, i8, i9);
        }
    }

    public static String N(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return m.k(view);
        }
        WeakHashMap<View, String> weakHashMap = f4961f;
        if (weakHashMap == null) {
            return null;
        }
        return weakHashMap.get(view);
    }

    public static void N0(View view, CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 19) {
            S0().g(view, charSequence);
        }
    }

    public static float O(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return m.l(view);
        }
        return 0.0f;
    }

    public static void O0(View view, String str) {
        if (Build.VERSION.SDK_INT >= 21) {
            m.v(view, str);
            return;
        }
        if (f4961f == null) {
            f4961f = new WeakHashMap<>();
        }
        f4961f.put(view, str);
    }

    @Deprecated
    public static int P(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return h.g(view);
        }
        return 0;
    }

    public static void P0(View view, float f5) {
        if (Build.VERSION.SDK_INT >= 21) {
            m.w(view, f5);
        }
    }

    public static float Q(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return m.m(view);
        }
        return 0.0f;
    }

    private static void Q0(View view) {
        if (C(view) == 0) {
            E0(view, 1);
        }
        for (ViewParent parent = view.getParent(); parent instanceof View; parent = parent.getParent()) {
            if (C((View) parent) == 4) {
                E0(view, 2);
                return;
            }
        }
    }

    public static boolean R(View view) {
        if (Build.VERSION.SDK_INT >= 15) {
            return g.a(view);
        }
        return false;
    }

    public static void R0(View view, float f5) {
        if (Build.VERSION.SDK_INT >= 21) {
            m.x(view, f5);
        }
    }

    public static boolean S(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return h.h(view);
        }
        return true;
    }

    private static f<CharSequence> S0() {
        return new c(q0.e.f22465e0, CharSequence.class, 64, 30);
    }

    public static boolean T(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return h.i(view);
        }
        return false;
    }

    public static void T0(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            m.z(view);
        } else if (view instanceof androidx.core.view.p) {
            ((androidx.core.view.p) view).stopNestedScroll();
        }
    }

    public static boolean U(View view) {
        Boolean f5 = b().f(view);
        return f5 != null && f5.booleanValue();
    }

    private static void U0(View view) {
        float translationY = view.getTranslationY();
        view.setTranslationY(1.0f + translationY);
        view.setTranslationY(translationY);
    }

    public static boolean V(View view) {
        return Build.VERSION.SDK_INT >= 19 ? k.b(view) : view.getWindowToken() != null;
    }

    public static boolean W(View view) {
        return Build.VERSION.SDK_INT >= 19 ? k.c(view) : view.getWidth() > 0 && view.getHeight() > 0;
    }

    public static boolean X(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return m.p(view);
        }
        if (view instanceof androidx.core.view.p) {
            return ((androidx.core.view.p) view).isNestedScrollingEnabled();
        }
        return false;
    }

    public static boolean Y(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return i.g(view);
        }
        return false;
    }

    public static boolean Z(View view) {
        Boolean f5 = s0().f(view);
        return f5 != null && f5.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ androidx.core.view.c a0(androidx.core.view.c cVar) {
        return cVar;
    }

    private static f<Boolean> b() {
        return new d(q0.e.Y, Boolean.class, 28);
    }

    static void b0(View view, int i8) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) view.getContext().getSystemService("accessibility");
        if (accessibilityManager.isEnabled()) {
            boolean z4 = r(view) != null && view.isShown() && view.getWindowVisibility() == 0;
            if (q(view) != 0 || z4) {
                AccessibilityEvent obtain = AccessibilityEvent.obtain();
                obtain.setEventType(z4 ? 32 : RecognitionOptions.PDF417);
                k.g(obtain, i8);
                if (z4) {
                    obtain.getText().add(r(view));
                    Q0(view);
                }
                view.sendAccessibilityEventUnchecked(obtain);
            } else if (i8 == 32) {
                AccessibilityEvent obtain2 = AccessibilityEvent.obtain();
                view.onInitializeAccessibilityEvent(obtain2);
                obtain2.setEventType(32);
                k.g(obtain2, i8);
                obtain2.setSource(view);
                view.onPopulateAccessibilityEvent(obtain2);
                obtain2.getText().add(r(view));
                accessibilityManager.sendAccessibilityEvent(obtain2);
            } else if (view.getParent() != null) {
                try {
                    k.e(view.getParent(), view, view, i8);
                } catch (AbstractMethodError e8) {
                    Log.e("ViewCompat", view.getParent().getClass().getSimpleName() + " does not fully implement ViewParent", e8);
                }
            }
        }
    }

    public static int c(View view, CharSequence charSequence, androidx.core.view.accessibility.f fVar) {
        int t8 = t(view, charSequence);
        if (t8 != -1) {
            d(view, new c.a(t8, charSequence, fVar));
        }
        return t8;
    }

    public static void c0(View view, int i8) {
        int i9 = Build.VERSION.SDK_INT;
        if (i9 >= 23) {
            view.offsetLeftAndRight(i8);
        } else if (i9 < 21) {
            f(view, i8);
        } else {
            Rect z4 = z();
            boolean z8 = false;
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                View view2 = (View) parent;
                z4.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                z8 = !z4.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            }
            f(view, i8);
            if (z8 && z4.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View) parent).invalidate(z4);
            }
        }
    }

    private static void d(View view, c.a aVar) {
        if (Build.VERSION.SDK_INT >= 21) {
            l(view);
            o0(aVar.b(), view);
            s(view).add(aVar);
            b0(view, 0);
        }
    }

    public static void d0(View view, int i8) {
        int i9 = Build.VERSION.SDK_INT;
        if (i9 >= 23) {
            view.offsetTopAndBottom(i8);
        } else if (i9 < 21) {
            g(view, i8);
        } else {
            Rect z4 = z();
            boolean z8 = false;
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                View view2 = (View) parent;
                z4.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                z8 = !z4.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            }
            g(view, i8);
            if (z8 && z4.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View) parent).invalidate(z4);
            }
        }
    }

    public static i0 e(View view) {
        if (f4962g == null) {
            f4962g = new WeakHashMap<>();
        }
        i0 i0Var = f4962g.get(view);
        if (i0Var == null) {
            i0 i0Var2 = new i0(view);
            f4962g.put(view, i0Var2);
            return i0Var2;
        }
        return i0Var;
    }

    public static m0 e0(View view, m0 m0Var) {
        WindowInsets x8;
        if (Build.VERSION.SDK_INT >= 21 && (x8 = m0Var.x()) != null) {
            WindowInsets b9 = l.b(view, x8);
            if (!b9.equals(x8)) {
                return m0.z(b9, view);
            }
        }
        return m0Var;
    }

    private static void f(View view, int i8) {
        view.offsetLeftAndRight(i8);
        if (view.getVisibility() == 0) {
            U0(view);
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                U0((View) parent);
            }
        }
    }

    public static void f0(View view, androidx.core.view.accessibility.c cVar) {
        view.onInitializeAccessibilityNodeInfo(cVar.H0());
    }

    private static void g(View view, int i8) {
        view.offsetTopAndBottom(i8);
        if (view.getVisibility() == 0) {
            U0(view);
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                U0((View) parent);
            }
        }
    }

    private static f<CharSequence> g0() {
        return new b(q0.e.Z, CharSequence.class, 8, 28);
    }

    public static m0 h(View view, m0 m0Var, Rect rect) {
        return Build.VERSION.SDK_INT >= 21 ? m.b(view, m0Var, rect) : m0Var;
    }

    public static boolean h0(View view, int i8, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 16) {
            return h.j(view, i8, bundle);
        }
        return false;
    }

    public static m0 i(View view, m0 m0Var) {
        WindowInsets x8;
        if (Build.VERSION.SDK_INT >= 21 && (x8 = m0Var.x()) != null) {
            WindowInsets a9 = l.a(view, x8);
            if (!a9.equals(x8)) {
                return m0.z(a9, view);
            }
        }
        return m0Var;
    }

    public static androidx.core.view.c i0(View view, androidx.core.view.c cVar) {
        if (Log.isLoggable("ViewCompat", 3)) {
            Log.d("ViewCompat", "performReceiveContent: " + cVar + ", view=" + view.getClass().getSimpleName() + "[" + view.getId() + "]");
        }
        if (Build.VERSION.SDK_INT >= 31) {
            return t.b(view, cVar);
        }
        androidx.core.view.w wVar = (androidx.core.view.w) view.getTag(q0.e.f22459b0);
        if (wVar != null) {
            androidx.core.view.c a9 = wVar.a(view, cVar);
            if (a9 == null) {
                return null;
            }
            return A(view).a(a9);
        }
        return A(view).a(cVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean j(View view, KeyEvent keyEvent) {
        if (Build.VERSION.SDK_INT >= 28) {
            return false;
        }
        return w.a(view).b(view, keyEvent);
    }

    public static void j0(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            h.k(view);
        } else {
            view.postInvalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean k(View view, KeyEvent keyEvent) {
        if (Build.VERSION.SDK_INT >= 28) {
            return false;
        }
        return w.a(view).f(keyEvent);
    }

    public static void k0(View view, int i8, int i9, int i10, int i11) {
        if (Build.VERSION.SDK_INT >= 16) {
            h.l(view, i8, i9, i10, i11);
        } else {
            view.postInvalidate(i8, i9, i10, i11);
        }
    }

    static void l(View view) {
        androidx.core.view.a n8 = n(view);
        if (n8 == null) {
            n8 = new androidx.core.view.a();
        }
        t0(view, n8);
    }

    public static void l0(View view, Runnable runnable) {
        if (Build.VERSION.SDK_INT >= 16) {
            h.m(view, runnable);
        } else {
            view.postDelayed(runnable, ValueAnimator.getFrameDelay());
        }
    }

    public static int m() {
        AtomicInteger atomicInteger;
        int i8;
        int i9;
        if (Build.VERSION.SDK_INT >= 17) {
            return i.a();
        }
        do {
            atomicInteger = f4956a;
            i8 = atomicInteger.get();
            i9 = i8 + 1;
            if (i9 > 16777215) {
                i9 = 1;
            }
        } while (!atomicInteger.compareAndSet(i8, i9));
        return i8;
    }

    @SuppressLint({"LambdaLast"})
    public static void m0(View view, Runnable runnable, long j8) {
        if (Build.VERSION.SDK_INT >= 16) {
            h.n(view, runnable, j8);
        } else {
            view.postDelayed(runnable, ValueAnimator.getFrameDelay() + j8);
        }
    }

    public static androidx.core.view.a n(View view) {
        View.AccessibilityDelegate o5 = o(view);
        if (o5 == null) {
            return null;
        }
        return o5 instanceof a.C0042a ? ((a.C0042a) o5).f4899a : new androidx.core.view.a(o5);
    }

    public static void n0(View view, int i8) {
        if (Build.VERSION.SDK_INT >= 21) {
            o0(i8, view);
            b0(view, 0);
        }
    }

    private static View.AccessibilityDelegate o(View view) {
        return Build.VERSION.SDK_INT >= 29 ? r.a(view) : p(view);
    }

    private static void o0(int i8, View view) {
        List<c.a> s8 = s(view);
        for (int i9 = 0; i9 < s8.size(); i9++) {
            if (s8.get(i9).b() == i8) {
                s8.remove(i9);
                return;
            }
        }
    }

    private static View.AccessibilityDelegate p(View view) {
        if (f4964i) {
            return null;
        }
        if (f4963h == null) {
            try {
                Field declaredField = View.class.getDeclaredField("mAccessibilityDelegate");
                f4963h = declaredField;
                declaredField.setAccessible(true);
            } catch (Throwable unused) {
                f4964i = true;
                return null;
            }
        }
        try {
            Object obj = f4963h.get(view);
            if (obj instanceof View.AccessibilityDelegate) {
                return (View.AccessibilityDelegate) obj;
            }
            return null;
        } catch (Throwable unused2) {
            f4964i = true;
            return null;
        }
    }

    public static void p0(View view, c.a aVar, CharSequence charSequence, androidx.core.view.accessibility.f fVar) {
        if (fVar == null && charSequence == null) {
            n0(view, aVar.b());
        } else {
            d(view, aVar.a(charSequence, fVar));
        }
    }

    public static int q(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            return k.a(view);
        }
        return 0;
    }

    public static void q0(View view) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 20) {
            l.c(view);
        } else if (i8 >= 16) {
            h.p(view);
        }
    }

    public static CharSequence r(View view) {
        return g0().f(view);
    }

    public static void r0(View view, @SuppressLint({"ContextFirst"}) Context context, int[] iArr, AttributeSet attributeSet, TypedArray typedArray, int i8, int i9) {
        if (Build.VERSION.SDK_INT >= 29) {
            r.c(view, context, iArr, attributeSet, typedArray, i8, i9);
        }
    }

    private static List<c.a> s(View view) {
        int i8 = q0.e.W;
        ArrayList arrayList = (ArrayList) view.getTag(i8);
        if (arrayList == null) {
            ArrayList arrayList2 = new ArrayList();
            view.setTag(i8, arrayList2);
            return arrayList2;
        }
        return arrayList;
    }

    private static f<Boolean> s0() {
        return new a(q0.e.f22463d0, Boolean.class, 28);
    }

    private static int t(View view, CharSequence charSequence) {
        List<c.a> s8 = s(view);
        for (int i8 = 0; i8 < s8.size(); i8++) {
            if (TextUtils.equals(charSequence, s8.get(i8).c())) {
                return s8.get(i8).b();
            }
        }
        int i9 = -1;
        int i10 = 0;
        while (true) {
            int[] iArr = f4966k;
            if (i10 >= iArr.length || i9 != -1) {
                break;
            }
            int i11 = iArr[i10];
            boolean z4 = true;
            for (int i12 = 0; i12 < s8.size(); i12++) {
                z4 &= s8.get(i12).b() != i11;
            }
            if (z4) {
                i9 = i11;
            }
            i10++;
        }
        return i9;
    }

    public static void t0(View view, androidx.core.view.a aVar) {
        if (aVar == null && (o(view) instanceof a.C0042a)) {
            aVar = new androidx.core.view.a();
        }
        view.setAccessibilityDelegate(aVar == null ? null : aVar.d());
    }

    public static ColorStateList u(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return m.g(view);
        }
        if (view instanceof a0) {
            return ((a0) view).getSupportBackgroundTintList();
        }
        return null;
    }

    public static void u0(View view, boolean z4) {
        b().g(view, Boolean.valueOf(z4));
    }

    public static PorterDuff.Mode v(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return m.h(view);
        }
        if (view instanceof a0) {
            return ((a0) view).getSupportBackgroundTintMode();
        }
        return null;
    }

    public static void v0(View view, int i8) {
        if (Build.VERSION.SDK_INT >= 19) {
            k.f(view, i8);
        }
    }

    public static Rect w(View view) {
        if (Build.VERSION.SDK_INT >= 18) {
            return j.a(view);
        }
        return null;
    }

    public static void w0(View view, CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 19) {
            g0().g(view, charSequence);
            if (charSequence != null) {
                f4968m.a(view);
            } else {
                f4968m.d(view);
            }
        }
    }

    public static Display x(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return i.b(view);
        }
        if (V(view)) {
            return ((WindowManager) view.getContext().getSystemService("window")).getDefaultDisplay();
        }
        return null;
    }

    public static void x0(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            h.q(view, drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static float y(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return m.i(view);
        }
        return 0.0f;
    }

    public static void y0(View view, ColorStateList colorStateList) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 21) {
            if (view instanceof a0) {
                ((a0) view).setSupportBackgroundTintList(colorStateList);
                return;
            }
            return;
        }
        m.q(view, colorStateList);
        if (i8 == 21) {
            Drawable background = view.getBackground();
            boolean z4 = (m.g(view) == null && m.h(view) == null) ? false : true;
            if (background == null || !z4) {
                return;
            }
            if (background.isStateful()) {
                background.setState(view.getDrawableState());
            }
            h.q(view, background);
        }
    }

    private static Rect z() {
        if (f4965j == null) {
            f4965j = new ThreadLocal<>();
        }
        Rect rect = f4965j.get();
        if (rect == null) {
            rect = new Rect();
            f4965j.set(rect);
        }
        rect.setEmpty();
        return rect;
    }

    public static void z0(View view, PorterDuff.Mode mode) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 21) {
            if (view instanceof a0) {
                ((a0) view).setSupportBackgroundTintMode(mode);
                return;
            }
            return;
        }
        m.r(view, mode);
        if (i8 == 21) {
            Drawable background = view.getBackground();
            boolean z4 = (m.g(view) == null && m.h(view) == null) ? false : true;
            if (background == null || !z4) {
                return;
            }
            if (background.isStateful()) {
                background.setState(view.getDrawableState());
            }
            h.q(view, background);
        }
    }
}
