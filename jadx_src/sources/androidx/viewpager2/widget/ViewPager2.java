package androidx.viewpager2.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.accessibility.c;
import androidx.core.view.accessibility.f;
import androidx.core.view.c0;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.v;
import com.google.android.libraries.barhopper.RecognitionOptions;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ViewPager2 extends ViewGroup {
    static boolean A = true;

    /* renamed from: a  reason: collision with root package name */
    private final Rect f7838a;

    /* renamed from: b  reason: collision with root package name */
    private final Rect f7839b;

    /* renamed from: c  reason: collision with root package name */
    private androidx.viewpager2.widget.b f7840c;

    /* renamed from: d  reason: collision with root package name */
    int f7841d;

    /* renamed from: e  reason: collision with root package name */
    boolean f7842e;

    /* renamed from: f  reason: collision with root package name */
    private RecyclerView.i f7843f;

    /* renamed from: g  reason: collision with root package name */
    private LinearLayoutManager f7844g;

    /* renamed from: h  reason: collision with root package name */
    private int f7845h;

    /* renamed from: j  reason: collision with root package name */
    private Parcelable f7846j;

    /* renamed from: k  reason: collision with root package name */
    RecyclerView f7847k;

    /* renamed from: l  reason: collision with root package name */
    private v f7848l;

    /* renamed from: m  reason: collision with root package name */
    androidx.viewpager2.widget.e f7849m;

    /* renamed from: n  reason: collision with root package name */
    private androidx.viewpager2.widget.b f7850n;

    /* renamed from: p  reason: collision with root package name */
    private androidx.viewpager2.widget.c f7851p;
    private androidx.viewpager2.widget.d q;

    /* renamed from: t  reason: collision with root package name */
    private RecyclerView.l f7852t;

    /* renamed from: w  reason: collision with root package name */
    private boolean f7853w;

    /* renamed from: x  reason: collision with root package name */
    private boolean f7854x;

    /* renamed from: y  reason: collision with root package name */
    private int f7855y;

    /* renamed from: z  reason: collision with root package name */
    e f7856z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        int f7857a;

        /* renamed from: b  reason: collision with root package name */
        int f7858b;

        /* renamed from: c  reason: collision with root package name */
        Parcelable f7859c;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return createFromParcel(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return Build.VERSION.SDK_INT >= 24 ? new SavedState(parcel, classLoader) : new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        SavedState(Parcel parcel) {
            super(parcel);
            a(parcel, null);
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            a(parcel, classLoader);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private void a(Parcel parcel, ClassLoader classLoader) {
            this.f7857a = parcel.readInt();
            this.f7858b = parcel.readInt();
            this.f7859c = parcel.readParcelable(classLoader);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeInt(this.f7857a);
            parcel.writeInt(this.f7858b);
            parcel.writeParcelable(this.f7859c, i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends g {
        a() {
            super(null);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.g, androidx.recyclerview.widget.RecyclerView.i
        public void a() {
            ViewPager2 viewPager2 = ViewPager2.this;
            viewPager2.f7842e = true;
            viewPager2.f7849m.l();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends i {
        b() {
        }

        @Override // androidx.viewpager2.widget.ViewPager2.i
        public void a(int i8) {
            if (i8 == 0) {
                ViewPager2.this.m();
            }
        }

        @Override // androidx.viewpager2.widget.ViewPager2.i
        public void c(int i8) {
            ViewPager2 viewPager2 = ViewPager2.this;
            if (viewPager2.f7841d != i8) {
                viewPager2.f7841d = i8;
                viewPager2.f7856z.q();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends i {
        c() {
        }

        @Override // androidx.viewpager2.widget.ViewPager2.i
        public void c(int i8) {
            ViewPager2.this.clearFocus();
            if (ViewPager2.this.hasFocus()) {
                ViewPager2.this.f7847k.requestFocus(2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements RecyclerView.p {
        d() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.p
        public void b(View view) {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.p
        public void d(View view) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            if (((ViewGroup.MarginLayoutParams) layoutParams).width != -1 || ((ViewGroup.MarginLayoutParams) layoutParams).height != -1) {
                throw new IllegalStateException("Pages must fill the whole ViewPager2 (use match_parent)");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public abstract class e {
        private e() {
        }

        /* synthetic */ e(ViewPager2 viewPager2, a aVar) {
            this();
        }

        boolean a() {
            return false;
        }

        boolean b(int i8) {
            return false;
        }

        boolean c(int i8, Bundle bundle) {
            return false;
        }

        boolean d() {
            return false;
        }

        void e(RecyclerView.g<?> gVar) {
        }

        void f(RecyclerView.g<?> gVar) {
        }

        String g() {
            throw new IllegalStateException("Not implemented.");
        }

        void h(androidx.viewpager2.widget.b bVar, RecyclerView recyclerView) {
        }

        void i(AccessibilityNodeInfo accessibilityNodeInfo) {
        }

        void j(androidx.core.view.accessibility.c cVar) {
        }

        boolean k(int i8) {
            throw new IllegalStateException("Not implemented.");
        }

        boolean l(int i8, Bundle bundle) {
            throw new IllegalStateException("Not implemented.");
        }

        void m() {
        }

        CharSequence n() {
            throw new IllegalStateException("Not implemented.");
        }

        void o(AccessibilityEvent accessibilityEvent) {
        }

        void p() {
        }

        void q() {
        }

        void r() {
        }

        void s() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f extends e {
        f() {
            super(ViewPager2.this, null);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public boolean b(int i8) {
            return (i8 == 8192 || i8 == 4096) && !ViewPager2.this.e();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public boolean d() {
            return true;
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void j(androidx.core.view.accessibility.c cVar) {
            if (ViewPager2.this.e()) {
                return;
            }
            cVar.T(c.a.f4919r);
            cVar.T(c.a.q);
            cVar.y0(false);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public boolean k(int i8) {
            if (b(i8)) {
                return false;
            }
            throw new IllegalStateException();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public CharSequence n() {
            if (d()) {
                return "androidx.viewpager.widget.ViewPager";
            }
            throw new IllegalStateException();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static abstract class g extends RecyclerView.i {
        private g() {
        }

        /* synthetic */ g(a aVar) {
            this();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.i
        public abstract void a();

        @Override // androidx.recyclerview.widget.RecyclerView.i
        public final void b(int i8, int i9) {
            a();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.i
        public final void c(int i8, int i9, Object obj) {
            a();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.i
        public final void d(int i8, int i9) {
            a();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.i
        public final void e(int i8, int i9, int i10) {
            a();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.i
        public final void f(int i8, int i9) {
            a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class h extends LinearLayoutManager {
        h(Context context) {
            super(context);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.recyclerview.widget.LinearLayoutManager
        public void N1(RecyclerView.y yVar, int[] iArr) {
            int offscreenPageLimit = ViewPager2.this.getOffscreenPageLimit();
            if (offscreenPageLimit == -1) {
                super.N1(yVar, iArr);
                return;
            }
            int pageSize = ViewPager2.this.getPageSize() * offscreenPageLimit;
            iArr[0] = pageSize;
            iArr[1] = pageSize;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.o
        public void O0(RecyclerView.u uVar, RecyclerView.y yVar, androidx.core.view.accessibility.c cVar) {
            super.O0(uVar, yVar, cVar);
            ViewPager2.this.f7856z.j(cVar);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.o
        public boolean i1(RecyclerView.u uVar, RecyclerView.y yVar, int i8, Bundle bundle) {
            return ViewPager2.this.f7856z.b(i8) ? ViewPager2.this.f7856z.k(i8) : super.i1(uVar, yVar, i8, bundle);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.o
        public boolean t1(RecyclerView recyclerView, View view, Rect rect, boolean z4, boolean z8) {
            return false;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class i {
        public void a(int i8) {
        }

        public void b(int i8, float f5, int i9) {
        }

        public void c(int i8) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class j extends e {

        /* renamed from: b  reason: collision with root package name */
        private final androidx.core.view.accessibility.f f7866b;

        /* renamed from: c  reason: collision with root package name */
        private final androidx.core.view.accessibility.f f7867c;

        /* renamed from: d  reason: collision with root package name */
        private RecyclerView.i f7868d;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements androidx.core.view.accessibility.f {
            a() {
            }

            @Override // androidx.core.view.accessibility.f
            public boolean a(View view, f.a aVar) {
                j.this.v(((ViewPager2) view).getCurrentItem() + 1);
                return true;
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class b implements androidx.core.view.accessibility.f {
            b() {
            }

            @Override // androidx.core.view.accessibility.f
            public boolean a(View view, f.a aVar) {
                j.this.v(((ViewPager2) view).getCurrentItem() - 1);
                return true;
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class c extends g {
            c() {
                super(null);
            }

            @Override // androidx.viewpager2.widget.ViewPager2.g, androidx.recyclerview.widget.RecyclerView.i
            public void a() {
                j.this.w();
            }
        }

        j() {
            super(ViewPager2.this, null);
            this.f7866b = new a();
            this.f7867c = new b();
        }

        private void t(AccessibilityNodeInfo accessibilityNodeInfo) {
            int i8;
            int i9;
            if (ViewPager2.this.getAdapter() == null) {
                i8 = 0;
                i9 = 0;
            } else if (ViewPager2.this.getOrientation() == 1) {
                i8 = ViewPager2.this.getAdapter().c();
                i9 = 0;
            } else {
                i9 = ViewPager2.this.getAdapter().c();
                i8 = 0;
            }
            androidx.core.view.accessibility.c.I0(accessibilityNodeInfo).e0(c.b.b(i8, i9, false, 0));
        }

        private void u(AccessibilityNodeInfo accessibilityNodeInfo) {
            int c9;
            RecyclerView.g adapter = ViewPager2.this.getAdapter();
            if (adapter == null || (c9 = adapter.c()) == 0 || !ViewPager2.this.e()) {
                return;
            }
            if (ViewPager2.this.f7841d > 0) {
                accessibilityNodeInfo.addAction(8192);
            }
            if (ViewPager2.this.f7841d < c9 - 1) {
                accessibilityNodeInfo.addAction(RecognitionOptions.AZTEC);
            }
            accessibilityNodeInfo.setScrollable(true);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public boolean a() {
            return true;
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public boolean c(int i8, Bundle bundle) {
            return i8 == 8192 || i8 == 4096;
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void e(RecyclerView.g<?> gVar) {
            w();
            if (gVar != null) {
                gVar.z(this.f7868d);
            }
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void f(RecyclerView.g<?> gVar) {
            if (gVar != null) {
                gVar.B(this.f7868d);
            }
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public String g() {
            if (a()) {
                return "androidx.viewpager.widget.ViewPager";
            }
            throw new IllegalStateException();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void h(androidx.viewpager2.widget.b bVar, RecyclerView recyclerView) {
            c0.E0(recyclerView, 2);
            this.f7868d = new c();
            if (c0.C(ViewPager2.this) == 0) {
                c0.E0(ViewPager2.this, 1);
            }
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void i(AccessibilityNodeInfo accessibilityNodeInfo) {
            t(accessibilityNodeInfo);
            if (Build.VERSION.SDK_INT >= 16) {
                u(accessibilityNodeInfo);
            }
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public boolean l(int i8, Bundle bundle) {
            if (c(i8, bundle)) {
                v(i8 == 8192 ? ViewPager2.this.getCurrentItem() - 1 : ViewPager2.this.getCurrentItem() + 1);
                return true;
            }
            throw new IllegalStateException();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void m() {
            w();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void o(AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.setSource(ViewPager2.this);
            accessibilityEvent.setClassName(g());
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void p() {
            w();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void q() {
            w();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void r() {
            w();
        }

        @Override // androidx.viewpager2.widget.ViewPager2.e
        public void s() {
            w();
            if (Build.VERSION.SDK_INT < 21) {
                ViewPager2.this.sendAccessibilityEvent(RecognitionOptions.PDF417);
            }
        }

        void v(int i8) {
            if (ViewPager2.this.e()) {
                ViewPager2.this.j(i8, true);
            }
        }

        void w() {
            int c9;
            ViewPager2 viewPager2 = ViewPager2.this;
            c0.n0(viewPager2, 16908360);
            c0.n0(viewPager2, 16908361);
            c0.n0(viewPager2, 16908358);
            c0.n0(viewPager2, 16908359);
            if (ViewPager2.this.getAdapter() == null || (c9 = ViewPager2.this.getAdapter().c()) == 0 || !ViewPager2.this.e()) {
                return;
            }
            if (ViewPager2.this.getOrientation() != 0) {
                if (ViewPager2.this.f7841d < c9 - 1) {
                    c0.p0(viewPager2, new c.a(16908359, null), null, this.f7866b);
                }
                if (ViewPager2.this.f7841d > 0) {
                    c0.p0(viewPager2, new c.a(16908358, null), null, this.f7867c);
                    return;
                }
                return;
            }
            boolean d8 = ViewPager2.this.d();
            int i8 = d8 ? 16908360 : 16908361;
            int i9 = d8 ? 16908361 : 16908360;
            if (ViewPager2.this.f7841d < c9 - 1) {
                c0.p0(viewPager2, new c.a(i8, null), null, this.f7866b);
            }
            if (ViewPager2.this.f7841d > 0) {
                c0.p0(viewPager2, new c.a(i9, null), null, this.f7867c);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface k {
        void a(View view, float f5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class l extends v {
        l() {
        }

        @Override // androidx.recyclerview.widget.v, androidx.recyclerview.widget.z
        public View h(RecyclerView.o oVar) {
            if (ViewPager2.this.c()) {
                return null;
            }
            return super.h(oVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class m extends RecyclerView {
        m(Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public CharSequence getAccessibilityClassName() {
            return ViewPager2.this.f7856z.d() ? ViewPager2.this.f7856z.n() : super.getAccessibilityClassName();
        }

        @Override // android.view.View
        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setFromIndex(ViewPager2.this.f7841d);
            accessibilityEvent.setToIndex(ViewPager2.this.f7841d);
            ViewPager2.this.f7856z.o(accessibilityEvent);
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return ViewPager2.this.e() && super.onInterceptTouchEvent(motionEvent);
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return ViewPager2.this.e() && super.onTouchEvent(motionEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class n implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        private final int f7874a;

        /* renamed from: b  reason: collision with root package name */
        private final RecyclerView f7875b;

        n(int i8, RecyclerView recyclerView) {
            this.f7874a = i8;
            this.f7875b = recyclerView;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f7875b.t1(this.f7874a);
        }
    }

    public ViewPager2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f7838a = new Rect();
        this.f7839b = new Rect();
        this.f7840c = new androidx.viewpager2.widget.b(3);
        this.f7842e = false;
        this.f7843f = new a();
        this.f7845h = -1;
        this.f7852t = null;
        this.f7853w = false;
        this.f7854x = true;
        this.f7855y = -1;
        b(context, attributeSet);
    }

    public ViewPager2(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f7838a = new Rect();
        this.f7839b = new Rect();
        this.f7840c = new androidx.viewpager2.widget.b(3);
        this.f7842e = false;
        this.f7843f = new a();
        this.f7845h = -1;
        this.f7852t = null;
        this.f7853w = false;
        this.f7854x = true;
        this.f7855y = -1;
        b(context, attributeSet);
    }

    private RecyclerView.p a() {
        return new d();
    }

    private void b(Context context, AttributeSet attributeSet) {
        this.f7856z = A ? new j() : new f();
        m mVar = new m(context);
        this.f7847k = mVar;
        mVar.setId(c0.m());
        this.f7847k.setDescendantFocusability(131072);
        h hVar = new h(context);
        this.f7844g = hVar;
        this.f7847k.setLayoutManager(hVar);
        this.f7847k.setScrollingTouchSlop(1);
        k(context, attributeSet);
        this.f7847k.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.f7847k.j(a());
        androidx.viewpager2.widget.e eVar = new androidx.viewpager2.widget.e(this);
        this.f7849m = eVar;
        this.f7851p = new androidx.viewpager2.widget.c(this, eVar, this.f7847k);
        l lVar = new l();
        this.f7848l = lVar;
        lVar.b(this.f7847k);
        this.f7847k.l(this.f7849m);
        androidx.viewpager2.widget.b bVar = new androidx.viewpager2.widget.b(3);
        this.f7850n = bVar;
        this.f7849m.o(bVar);
        b bVar2 = new b();
        c cVar = new c();
        this.f7850n.d(bVar2);
        this.f7850n.d(cVar);
        this.f7856z.h(this.f7850n, this.f7847k);
        this.f7850n.d(this.f7840c);
        androidx.viewpager2.widget.d dVar = new androidx.viewpager2.widget.d(this.f7844g);
        this.q = dVar;
        this.f7850n.d(dVar);
        RecyclerView recyclerView = this.f7847k;
        attachViewToParent(recyclerView, 0, recyclerView.getLayoutParams());
    }

    private void f(RecyclerView.g<?> gVar) {
        if (gVar != null) {
            gVar.z(this.f7843f);
        }
    }

    private void h() {
        RecyclerView.g adapter;
        if (this.f7845h == -1 || (adapter = getAdapter()) == null) {
            return;
        }
        Parcelable parcelable = this.f7846j;
        if (parcelable != null) {
            if (adapter instanceof androidx.viewpager2.adapter.a) {
                ((androidx.viewpager2.adapter.a) adapter).b(parcelable);
            }
            this.f7846j = null;
        }
        int max = Math.max(0, Math.min(this.f7845h, adapter.c() - 1));
        this.f7841d = max;
        this.f7845h = -1;
        this.f7847k.l1(max);
        this.f7856z.m();
    }

    private void k(Context context, AttributeSet attributeSet) {
        int[] iArr = z1.a.f24527g;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr);
        if (Build.VERSION.SDK_INT >= 29) {
            saveAttributeDataForStyleable(context, iArr, attributeSet, obtainStyledAttributes, 0, 0);
        }
        try {
            setOrientation(obtainStyledAttributes.getInt(z1.a.f24528h, 0));
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    private void l(RecyclerView.g<?> gVar) {
        if (gVar != null) {
            gVar.B(this.f7843f);
        }
    }

    public boolean c() {
        return this.f7851p.a();
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int i8) {
        return this.f7847k.canScrollHorizontally(i8);
    }

    @Override // android.view.View
    public boolean canScrollVertically(int i8) {
        return this.f7847k.canScrollVertically(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d() {
        return this.f7844g.a0() == 1;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        Parcelable parcelable = sparseArray.get(getId());
        if (parcelable instanceof SavedState) {
            int i8 = ((SavedState) parcelable).f7857a;
            sparseArray.put(this.f7847k.getId(), sparseArray.get(i8));
            sparseArray.remove(i8);
        }
        super.dispatchRestoreInstanceState(sparseArray);
        h();
    }

    public boolean e() {
        return this.f7854x;
    }

    public void g() {
        if (this.q.d() == null) {
            return;
        }
        double g8 = this.f7849m.g();
        int i8 = (int) g8;
        float f5 = (float) (g8 - i8);
        this.q.b(i8, f5, Math.round(getPageSize() * f5));
    }

    @Override // android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return this.f7856z.a() ? this.f7856z.g() : super.getAccessibilityClassName();
    }

    public RecyclerView.g getAdapter() {
        return this.f7847k.getAdapter();
    }

    public int getCurrentItem() {
        return this.f7841d;
    }

    public int getItemDecorationCount() {
        return this.f7847k.getItemDecorationCount();
    }

    public int getOffscreenPageLimit() {
        return this.f7855y;
    }

    public int getOrientation() {
        return this.f7844g.q2();
    }

    int getPageSize() {
        int height;
        int paddingBottom;
        RecyclerView recyclerView = this.f7847k;
        if (getOrientation() == 0) {
            height = recyclerView.getWidth() - recyclerView.getPaddingLeft();
            paddingBottom = recyclerView.getPaddingRight();
        } else {
            height = recyclerView.getHeight() - recyclerView.getPaddingTop();
            paddingBottom = recyclerView.getPaddingBottom();
        }
        return height - paddingBottom;
    }

    public int getScrollState() {
        return this.f7849m.h();
    }

    public void i(int i8, boolean z4) {
        if (c()) {
            throw new IllegalStateException("Cannot change current item when ViewPager2 is fake dragging");
        }
        j(i8, z4);
    }

    void j(int i8, boolean z4) {
        RecyclerView.g adapter = getAdapter();
        if (adapter == null) {
            if (this.f7845h != -1) {
                this.f7845h = Math.max(i8, 0);
            }
        } else if (adapter.c() <= 0) {
        } else {
            int min = Math.min(Math.max(i8, 0), adapter.c() - 1);
            if (min == this.f7841d && this.f7849m.j()) {
                return;
            }
            int i9 = this.f7841d;
            if (min == i9 && z4) {
                return;
            }
            double d8 = i9;
            this.f7841d = min;
            this.f7856z.q();
            if (!this.f7849m.j()) {
                d8 = this.f7849m.g();
            }
            this.f7849m.m(min, z4);
            if (!z4) {
                this.f7847k.l1(min);
                return;
            }
            double d9 = min;
            if (Math.abs(d9 - d8) <= 3.0d) {
                this.f7847k.t1(min);
                return;
            }
            this.f7847k.l1(d9 > d8 ? min - 3 : min + 3);
            RecyclerView recyclerView = this.f7847k;
            recyclerView.post(new n(min, recyclerView));
        }
    }

    void m() {
        v vVar = this.f7848l;
        if (vVar == null) {
            throw new IllegalStateException("Design assumption violated.");
        }
        View h8 = vVar.h(this.f7844g);
        if (h8 == null) {
            return;
        }
        int i02 = this.f7844g.i0(h8);
        if (i02 != this.f7841d && getScrollState() == 0) {
            this.f7850n.c(i02);
        }
        this.f7842e = false;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        this.f7856z.i(accessibilityNodeInfo);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        int measuredWidth = this.f7847k.getMeasuredWidth();
        int measuredHeight = this.f7847k.getMeasuredHeight();
        this.f7838a.left = getPaddingLeft();
        this.f7838a.right = (i10 - i8) - getPaddingRight();
        this.f7838a.top = getPaddingTop();
        this.f7838a.bottom = (i11 - i9) - getPaddingBottom();
        Gravity.apply(8388659, measuredWidth, measuredHeight, this.f7838a, this.f7839b);
        RecyclerView recyclerView = this.f7847k;
        Rect rect = this.f7839b;
        recyclerView.layout(rect.left, rect.top, rect.right, rect.bottom);
        if (this.f7842e) {
            m();
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i8, int i9) {
        measureChild(this.f7847k, i8, i9);
        int measuredWidth = this.f7847k.getMeasuredWidth();
        int measuredHeight = this.f7847k.getMeasuredHeight();
        int measuredState = this.f7847k.getMeasuredState();
        int paddingLeft = measuredWidth + getPaddingLeft() + getPaddingRight();
        int paddingTop = measuredHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(ViewGroup.resolveSizeAndState(Math.max(paddingLeft, getSuggestedMinimumWidth()), i8, measuredState), ViewGroup.resolveSizeAndState(Math.max(paddingTop, getSuggestedMinimumHeight()), i9, measuredState << 16));
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.f7845h = savedState.f7858b;
        this.f7846j = savedState.f7859c;
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f7857a = this.f7847k.getId();
        int i8 = this.f7845h;
        if (i8 == -1) {
            i8 = this.f7841d;
        }
        savedState.f7858b = i8;
        Parcelable parcelable = this.f7846j;
        if (parcelable == null) {
            RecyclerView.g adapter = this.f7847k.getAdapter();
            if (adapter instanceof androidx.viewpager2.adapter.a) {
                parcelable = ((androidx.viewpager2.adapter.a) adapter).a();
            }
            return savedState;
        }
        savedState.f7859c = parcelable;
        return savedState;
    }

    @Override // android.view.ViewGroup
    public void onViewAdded(View view) {
        throw new IllegalStateException(ViewPager2.class.getSimpleName() + " does not support direct child views");
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int i8, Bundle bundle) {
        return this.f7856z.c(i8, bundle) ? this.f7856z.l(i8, bundle) : super.performAccessibilityAction(i8, bundle);
    }

    public void setAdapter(RecyclerView.g gVar) {
        RecyclerView.g adapter = this.f7847k.getAdapter();
        this.f7856z.f(adapter);
        l(adapter);
        this.f7847k.setAdapter(gVar);
        this.f7841d = 0;
        h();
        this.f7856z.e(gVar);
        f(gVar);
    }

    public void setCurrentItem(int i8) {
        i(i8, true);
    }

    @Override // android.view.View
    public void setLayoutDirection(int i8) {
        super.setLayoutDirection(i8);
        this.f7856z.p();
    }

    public void setOffscreenPageLimit(int i8) {
        if (i8 < 1 && i8 != -1) {
            throw new IllegalArgumentException("Offscreen page limit must be OFFSCREEN_PAGE_LIMIT_DEFAULT or a number > 0");
        }
        this.f7855y = i8;
        this.f7847k.requestLayout();
    }

    public void setOrientation(int i8) {
        this.f7844g.E2(i8);
        this.f7856z.r();
    }

    public void setPageTransformer(k kVar) {
        boolean z4 = this.f7853w;
        if (kVar != null) {
            if (!z4) {
                this.f7852t = this.f7847k.getItemAnimator();
                this.f7853w = true;
            }
            this.f7847k.setItemAnimator(null);
        } else if (z4) {
            this.f7847k.setItemAnimator(this.f7852t);
            this.f7852t = null;
            this.f7853w = false;
        }
        if (kVar == this.q.d()) {
            return;
        }
        this.q.e(kVar);
        g();
    }

    public void setUserInputEnabled(boolean z4) {
        this.f7854x = z4;
        this.f7856z.s();
    }
}
