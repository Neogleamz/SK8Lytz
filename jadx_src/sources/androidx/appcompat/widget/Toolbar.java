package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.view.menu.g;
import androidx.appcompat.view.menu.m;
import androidx.appcompat.widget.ActionMenuView;
import androidx.customview.view.AbsSavedState;
import com.example.seedpoint.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Toolbar extends ViewGroup {
    private int A;
    private int B;
    private int C;
    private CharSequence E;
    private CharSequence F;
    private ColorStateList G;
    private ColorStateList H;
    private boolean K;
    private boolean L;
    private final ArrayList<View> O;
    private final ArrayList<View> P;
    private final int[] Q;
    final androidx.core.view.l R;
    private ArrayList<MenuItem> T;
    g W;

    /* renamed from: a  reason: collision with root package name */
    ActionMenuView f1356a;

    /* renamed from: a0  reason: collision with root package name */
    private final ActionMenuView.d f1357a0;

    /* renamed from: b  reason: collision with root package name */
    private TextView f1358b;

    /* renamed from: b0  reason: collision with root package name */
    private n0 f1359b0;

    /* renamed from: c  reason: collision with root package name */
    private TextView f1360c;

    /* renamed from: c0  reason: collision with root package name */
    private ActionMenuPresenter f1361c0;

    /* renamed from: d  reason: collision with root package name */
    private ImageButton f1362d;

    /* renamed from: d0  reason: collision with root package name */
    private f f1363d0;

    /* renamed from: e  reason: collision with root package name */
    private ImageView f1364e;

    /* renamed from: e0  reason: collision with root package name */
    private m.a f1365e0;

    /* renamed from: f  reason: collision with root package name */
    private Drawable f1366f;

    /* renamed from: f0  reason: collision with root package name */
    g.a f1367f0;

    /* renamed from: g  reason: collision with root package name */
    private CharSequence f1368g;

    /* renamed from: g0  reason: collision with root package name */
    private boolean f1369g0;

    /* renamed from: h  reason: collision with root package name */
    ImageButton f1370h;

    /* renamed from: h0  reason: collision with root package name */
    private OnBackInvokedCallback f1371h0;

    /* renamed from: i0  reason: collision with root package name */
    private OnBackInvokedDispatcher f1372i0;

    /* renamed from: j  reason: collision with root package name */
    View f1373j;

    /* renamed from: j0  reason: collision with root package name */
    private boolean f1374j0;

    /* renamed from: k  reason: collision with root package name */
    private Context f1375k;

    /* renamed from: k0  reason: collision with root package name */
    private final Runnable f1376k0;

    /* renamed from: l  reason: collision with root package name */
    private int f1377l;

    /* renamed from: m  reason: collision with root package name */
    private int f1378m;

    /* renamed from: n  reason: collision with root package name */
    private int f1379n;

    /* renamed from: p  reason: collision with root package name */
    int f1380p;
    private int q;

    /* renamed from: t  reason: collision with root package name */
    private int f1381t;

    /* renamed from: w  reason: collision with root package name */
    private int f1382w;

    /* renamed from: x  reason: collision with root package name */
    private int f1383x;

    /* renamed from: y  reason: collision with root package name */
    private int f1384y;

    /* renamed from: z  reason: collision with root package name */
    private b0 f1385z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LayoutParams extends ActionBar.LayoutParams {

        /* renamed from: b  reason: collision with root package name */
        int f1386b;

        public LayoutParams(int i8, int i9) {
            super(i8, i9);
            this.f1386b = 0;
            this.f469a = 8388627;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f1386b = 0;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f1386b = 0;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.f1386b = 0;
            a(marginLayoutParams);
        }

        public LayoutParams(ActionBar.LayoutParams layoutParams) {
            super(layoutParams);
            this.f1386b = 0;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ActionBar.LayoutParams) layoutParams);
            this.f1386b = 0;
            this.f1386b = layoutParams.f1386b;
        }

        void a(ViewGroup.MarginLayoutParams marginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) this).leftMargin = marginLayoutParams.leftMargin;
            ((ViewGroup.MarginLayoutParams) this).topMargin = marginLayoutParams.topMargin;
            ((ViewGroup.MarginLayoutParams) this).rightMargin = marginLayoutParams.rightMargin;
            ((ViewGroup.MarginLayoutParams) this).bottomMargin = marginLayoutParams.bottomMargin;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: c  reason: collision with root package name */
        int f1387c;

        /* renamed from: d  reason: collision with root package name */
        boolean f1388d;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f1387c = parcel.readInt();
            this.f1388d = parcel.readInt() != 0;
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeInt(this.f1387c);
            parcel.writeInt(this.f1388d ? 1 : 0);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements ActionMenuView.d {
        a() {
        }

        @Override // androidx.appcompat.widget.ActionMenuView.d
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (Toolbar.this.R.j(menuItem)) {
                return true;
            }
            g gVar = Toolbar.this.W;
            if (gVar != null) {
                return gVar.onMenuItemClick(menuItem);
            }
            return false;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Toolbar.this.Q();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements g.a {
        c() {
        }

        @Override // androidx.appcompat.view.menu.g.a
        public boolean a(androidx.appcompat.view.menu.g gVar, MenuItem menuItem) {
            g.a aVar = Toolbar.this.f1367f0;
            return aVar != null && aVar.a(gVar, menuItem);
        }

        @Override // androidx.appcompat.view.menu.g.a
        public void b(androidx.appcompat.view.menu.g gVar) {
            if (!Toolbar.this.f1356a.J()) {
                Toolbar.this.R.k(gVar);
            }
            g.a aVar = Toolbar.this.f1367f0;
            if (aVar != null) {
                aVar.b(gVar);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements View.OnClickListener {
        d() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Toolbar.this.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e {
        static OnBackInvokedDispatcher a(View view) {
            return view.findOnBackInvokedDispatcher();
        }

        static OnBackInvokedCallback b(final Runnable runnable) {
            Objects.requireNonNull(runnable);
            return new OnBackInvokedCallback() { // from class: androidx.appcompat.widget.m0
                public final void onBackInvoked() {
                    runnable.run();
                }
            };
        }

        static void c(Object obj, Object obj2) {
            ((OnBackInvokedDispatcher) obj).registerOnBackInvokedCallback(1000000, (OnBackInvokedCallback) obj2);
        }

        static void d(Object obj, Object obj2) {
            ((OnBackInvokedDispatcher) obj).unregisterOnBackInvokedCallback((OnBackInvokedCallback) obj2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f implements androidx.appcompat.view.menu.m {

        /* renamed from: a  reason: collision with root package name */
        androidx.appcompat.view.menu.g f1393a;

        /* renamed from: b  reason: collision with root package name */
        androidx.appcompat.view.menu.i f1394b;

        f() {
        }

        @Override // androidx.appcompat.view.menu.m
        public void c(androidx.appcompat.view.menu.g gVar, boolean z4) {
        }

        @Override // androidx.appcompat.view.menu.m
        public int e() {
            return 0;
        }

        @Override // androidx.appcompat.view.menu.m
        public void f(boolean z4) {
            if (this.f1394b != null) {
                androidx.appcompat.view.menu.g gVar = this.f1393a;
                boolean z8 = false;
                if (gVar != null) {
                    int size = gVar.size();
                    int i8 = 0;
                    while (true) {
                        if (i8 >= size) {
                            break;
                        } else if (this.f1393a.getItem(i8) == this.f1394b) {
                            z8 = true;
                            break;
                        } else {
                            i8++;
                        }
                    }
                }
                if (z8) {
                    return;
                }
                h(this.f1393a, this.f1394b);
            }
        }

        @Override // androidx.appcompat.view.menu.m
        public boolean g() {
            return false;
        }

        @Override // androidx.appcompat.view.menu.m
        public boolean h(androidx.appcompat.view.menu.g gVar, androidx.appcompat.view.menu.i iVar) {
            View view = Toolbar.this.f1373j;
            if (view instanceof androidx.appcompat.view.c) {
                ((androidx.appcompat.view.c) view).f();
            }
            Toolbar toolbar = Toolbar.this;
            toolbar.removeView(toolbar.f1373j);
            Toolbar toolbar2 = Toolbar.this;
            toolbar2.removeView(toolbar2.f1370h);
            Toolbar toolbar3 = Toolbar.this;
            toolbar3.f1373j = null;
            toolbar3.a();
            this.f1394b = null;
            Toolbar.this.requestLayout();
            iVar.r(false);
            Toolbar.this.R();
            return true;
        }

        @Override // androidx.appcompat.view.menu.m
        public boolean i(androidx.appcompat.view.menu.g gVar, androidx.appcompat.view.menu.i iVar) {
            Toolbar.this.g();
            ViewParent parent = Toolbar.this.f1370h.getParent();
            Toolbar toolbar = Toolbar.this;
            if (parent != toolbar) {
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(toolbar.f1370h);
                }
                Toolbar toolbar2 = Toolbar.this;
                toolbar2.addView(toolbar2.f1370h);
            }
            Toolbar.this.f1373j = iVar.getActionView();
            this.f1394b = iVar;
            ViewParent parent2 = Toolbar.this.f1373j.getParent();
            Toolbar toolbar3 = Toolbar.this;
            if (parent2 != toolbar3) {
                if (parent2 instanceof ViewGroup) {
                    ((ViewGroup) parent2).removeView(toolbar3.f1373j);
                }
                LayoutParams generateDefaultLayoutParams = Toolbar.this.generateDefaultLayoutParams();
                Toolbar toolbar4 = Toolbar.this;
                generateDefaultLayoutParams.f469a = 8388611 | (toolbar4.f1380p & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle);
                generateDefaultLayoutParams.f1386b = 2;
                toolbar4.f1373j.setLayoutParams(generateDefaultLayoutParams);
                Toolbar toolbar5 = Toolbar.this;
                toolbar5.addView(toolbar5.f1373j);
            }
            Toolbar.this.I();
            Toolbar.this.requestLayout();
            iVar.r(true);
            View view = Toolbar.this.f1373j;
            if (view instanceof androidx.appcompat.view.c) {
                ((androidx.appcompat.view.c) view).c();
            }
            Toolbar.this.R();
            return true;
        }

        @Override // androidx.appcompat.view.menu.m
        public void k(Context context, androidx.appcompat.view.menu.g gVar) {
            androidx.appcompat.view.menu.i iVar;
            androidx.appcompat.view.menu.g gVar2 = this.f1393a;
            if (gVar2 != null && (iVar = this.f1394b) != null) {
                gVar2.f(iVar);
            }
            this.f1393a = gVar;
        }

        @Override // androidx.appcompat.view.menu.m
        public void l(Parcelable parcelable) {
        }

        @Override // androidx.appcompat.view.menu.m
        public boolean n(androidx.appcompat.view.menu.r rVar) {
            return false;
        }

        @Override // androidx.appcompat.view.menu.m
        public Parcelable o() {
            return null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface g {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    public Toolbar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, g.a.R);
    }

    public Toolbar(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.C = 8388627;
        this.O = new ArrayList<>();
        this.P = new ArrayList<>();
        this.Q = new int[2];
        this.R = new androidx.core.view.l(new Runnable() { // from class: androidx.appcompat.widget.l0
            @Override // java.lang.Runnable
            public final void run() {
                Toolbar.this.y();
            }
        });
        this.T = new ArrayList<>();
        this.f1357a0 = new a();
        this.f1376k0 = new b();
        Context context2 = getContext();
        int[] iArr = g.j.f20086p3;
        j0 v8 = j0.v(context2, attributeSet, iArr, i8, 0);
        androidx.core.view.c0.r0(this, context, iArr, attributeSet, v8.r(), i8, 0);
        this.f1378m = v8.n(g.j.R3, 0);
        this.f1379n = v8.n(g.j.I3, 0);
        this.C = v8.l(g.j.f20090q3, this.C);
        this.f1380p = v8.l(g.j.f20095r3, 48);
        int e8 = v8.e(g.j.L3, 0);
        int i9 = g.j.Q3;
        e8 = v8.s(i9) ? v8.e(i9, e8) : e8;
        this.f1384y = e8;
        this.f1383x = e8;
        this.f1382w = e8;
        this.f1381t = e8;
        int e9 = v8.e(g.j.O3, -1);
        if (e9 >= 0) {
            this.f1381t = e9;
        }
        int e10 = v8.e(g.j.N3, -1);
        if (e10 >= 0) {
            this.f1382w = e10;
        }
        int e11 = v8.e(g.j.P3, -1);
        if (e11 >= 0) {
            this.f1383x = e11;
        }
        int e12 = v8.e(g.j.M3, -1);
        if (e12 >= 0) {
            this.f1384y = e12;
        }
        this.q = v8.f(g.j.C3, -1);
        int e13 = v8.e(g.j.f20130y3, Integer.MIN_VALUE);
        int e14 = v8.e(g.j.f20110u3, Integer.MIN_VALUE);
        int f5 = v8.f(g.j.f20120w3, 0);
        int f8 = v8.f(g.j.f20125x3, 0);
        h();
        this.f1385z.e(f5, f8);
        if (e13 != Integer.MIN_VALUE || e14 != Integer.MIN_VALUE) {
            this.f1385z.g(e13, e14);
        }
        this.A = v8.e(g.j.f20135z3, Integer.MIN_VALUE);
        this.B = v8.e(g.j.f20115v3, Integer.MIN_VALUE);
        this.f1366f = v8.g(g.j.f20105t3);
        this.f1368g = v8.p(g.j.f20100s3);
        CharSequence p8 = v8.p(g.j.K3);
        if (!TextUtils.isEmpty(p8)) {
            setTitle(p8);
        }
        CharSequence p9 = v8.p(g.j.H3);
        if (!TextUtils.isEmpty(p9)) {
            setSubtitle(p9);
        }
        this.f1375k = getContext();
        setPopupTheme(v8.n(g.j.G3, 0));
        Drawable g8 = v8.g(g.j.F3);
        if (g8 != null) {
            setNavigationIcon(g8);
        }
        CharSequence p10 = v8.p(g.j.E3);
        if (!TextUtils.isEmpty(p10)) {
            setNavigationContentDescription(p10);
        }
        Drawable g9 = v8.g(g.j.A3);
        if (g9 != null) {
            setLogo(g9);
        }
        CharSequence p11 = v8.p(g.j.B3);
        if (!TextUtils.isEmpty(p11)) {
            setLogoDescription(p11);
        }
        int i10 = g.j.S3;
        if (v8.s(i10)) {
            setTitleTextColor(v8.c(i10));
        }
        int i11 = g.j.J3;
        if (v8.s(i11)) {
            setSubtitleTextColor(v8.c(i11));
        }
        int i12 = g.j.D3;
        if (v8.s(i12)) {
            x(v8.n(i12, 0));
        }
        v8.w();
    }

    private int C(View view, int i8, int[] iArr, int i9) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i10 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin - iArr[0];
        int max = i8 + Math.max(0, i10);
        iArr[0] = Math.max(0, -i10);
        int q = q(view, i9);
        int measuredWidth = view.getMeasuredWidth();
        view.layout(max, q, max + measuredWidth, view.getMeasuredHeight() + q);
        return max + measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
    }

    private int D(View view, int i8, int[] iArr, int i9) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i10 = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin - iArr[1];
        int max = i8 - Math.max(0, i10);
        iArr[1] = Math.max(0, -i10);
        int q = q(view, i9);
        int measuredWidth = view.getMeasuredWidth();
        view.layout(max - measuredWidth, q, max, view.getMeasuredHeight() + q);
        return max - (measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin);
    }

    private int E(View view, int i8, int i9, int i10, int i11, int[] iArr) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int i12 = marginLayoutParams.leftMargin - iArr[0];
        int i13 = marginLayoutParams.rightMargin - iArr[1];
        int max = Math.max(0, i12) + Math.max(0, i13);
        iArr[0] = Math.max(0, -i12);
        iArr[1] = Math.max(0, -i13);
        view.measure(ViewGroup.getChildMeasureSpec(i8, getPaddingLeft() + getPaddingRight() + max + i9, marginLayoutParams.width), ViewGroup.getChildMeasureSpec(i10, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + i11, marginLayoutParams.height));
        return view.getMeasuredWidth() + max;
    }

    private void F(View view, int i8, int i9, int i10, int i11, int i12) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i8, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + i9, marginLayoutParams.width);
        int childMeasureSpec2 = ViewGroup.getChildMeasureSpec(i10, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + i11, marginLayoutParams.height);
        int mode = View.MeasureSpec.getMode(childMeasureSpec2);
        if (mode != 1073741824 && i12 >= 0) {
            if (mode != 0) {
                i12 = Math.min(View.MeasureSpec.getSize(childMeasureSpec2), i12);
            }
            childMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(i12, 1073741824);
        }
        view.measure(childMeasureSpec, childMeasureSpec2);
    }

    private void G() {
        Menu menu = getMenu();
        ArrayList<MenuItem> currentMenuItems = getCurrentMenuItems();
        this.R.h(menu, getMenuInflater());
        ArrayList<MenuItem> currentMenuItems2 = getCurrentMenuItems();
        currentMenuItems2.removeAll(currentMenuItems);
        this.T = currentMenuItems2;
    }

    private void H() {
        removeCallbacks(this.f1376k0);
        post(this.f1376k0);
    }

    private boolean O() {
        if (this.f1369g0) {
            int childCount = getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                View childAt = getChildAt(i8);
                if (P(childAt) && childAt.getMeasuredWidth() > 0 && childAt.getMeasuredHeight() > 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean P(View view) {
        return (view == null || view.getParent() != this || view.getVisibility() == 8) ? false : true;
    }

    private void b(List<View> list, int i8) {
        boolean z4 = androidx.core.view.c0.E(this) == 1;
        int childCount = getChildCount();
        int b9 = androidx.core.view.f.b(i8, androidx.core.view.c0.E(this));
        list.clear();
        if (!z4) {
            for (int i9 = 0; i9 < childCount; i9++) {
                View childAt = getChildAt(i9);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.f1386b == 0 && P(childAt) && p(layoutParams.f469a) == b9) {
                    list.add(childAt);
                }
            }
            return;
        }
        for (int i10 = childCount - 1; i10 >= 0; i10--) {
            View childAt2 = getChildAt(i10);
            LayoutParams layoutParams2 = (LayoutParams) childAt2.getLayoutParams();
            if (layoutParams2.f1386b == 0 && P(childAt2) && p(layoutParams2.f469a) == b9) {
                list.add(childAt2);
            }
        }
    }

    private void c(View view, boolean z4) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        LayoutParams generateDefaultLayoutParams = layoutParams == null ? generateDefaultLayoutParams() : !checkLayoutParams(layoutParams) ? generateLayoutParams(layoutParams) : (LayoutParams) layoutParams;
        generateDefaultLayoutParams.f1386b = 1;
        if (!z4 || this.f1373j == null) {
            addView(view, generateDefaultLayoutParams);
            return;
        }
        view.setLayoutParams(generateDefaultLayoutParams);
        this.P.add(view);
    }

    private ArrayList<MenuItem> getCurrentMenuItems() {
        ArrayList<MenuItem> arrayList = new ArrayList<>();
        Menu menu = getMenu();
        for (int i8 = 0; i8 < menu.size(); i8++) {
            arrayList.add(menu.getItem(i8));
        }
        return arrayList;
    }

    private MenuInflater getMenuInflater() {
        return new androidx.appcompat.view.g(getContext());
    }

    private void h() {
        if (this.f1385z == null) {
            this.f1385z = new b0();
        }
    }

    private void i() {
        if (this.f1364e == null) {
            this.f1364e = new AppCompatImageView(getContext());
        }
    }

    private void j() {
        k();
        if (this.f1356a.N() == null) {
            androidx.appcompat.view.menu.g gVar = (androidx.appcompat.view.menu.g) this.f1356a.getMenu();
            if (this.f1363d0 == null) {
                this.f1363d0 = new f();
            }
            this.f1356a.setExpandedActionViewsExclusive(true);
            gVar.c(this.f1363d0, this.f1375k);
            R();
        }
    }

    private void k() {
        if (this.f1356a == null) {
            ActionMenuView actionMenuView = new ActionMenuView(getContext());
            this.f1356a = actionMenuView;
            actionMenuView.setPopupTheme(this.f1377l);
            this.f1356a.setOnMenuItemClickListener(this.f1357a0);
            this.f1356a.O(this.f1365e0, new c());
            LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
            generateDefaultLayoutParams.f469a = 8388613 | (this.f1380p & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle);
            this.f1356a.setLayoutParams(generateDefaultLayoutParams);
            c(this.f1356a, false);
        }
    }

    private void l() {
        if (this.f1362d == null) {
            this.f1362d = new AppCompatImageButton(getContext(), null, g.a.Q);
            LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
            generateDefaultLayoutParams.f469a = 8388611 | (this.f1380p & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle);
            this.f1362d.setLayoutParams(generateDefaultLayoutParams);
        }
    }

    private int p(int i8) {
        int E = androidx.core.view.c0.E(this);
        int b9 = androidx.core.view.f.b(i8, E) & 7;
        return (b9 == 1 || b9 == 3 || b9 == 5) ? b9 : E == 1 ? 5 : 3;
    }

    private int q(View view, int i8) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int measuredHeight = view.getMeasuredHeight();
        int i9 = i8 > 0 ? (measuredHeight - i8) / 2 : 0;
        int r4 = r(layoutParams.f469a);
        if (r4 != 48) {
            if (r4 != 80) {
                int paddingTop = getPaddingTop();
                int paddingBottom = getPaddingBottom();
                int height = getHeight();
                int i10 = (((height - paddingTop) - paddingBottom) - measuredHeight) / 2;
                int i11 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                if (i10 < i11) {
                    i10 = i11;
                } else {
                    int i12 = (((height - paddingBottom) - measuredHeight) - i10) - paddingTop;
                    int i13 = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                    if (i12 < i13) {
                        i10 = Math.max(0, i10 - (i13 - i12));
                    }
                }
                return paddingTop + i10;
            }
            return (((getHeight() - getPaddingBottom()) - measuredHeight) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin) - i9;
        }
        return getPaddingTop() - i9;
    }

    private int r(int i8) {
        int i9 = i8 & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle;
        return (i9 == 16 || i9 == 48 || i9 == 80) ? i9 : this.C & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle;
    }

    private int s(View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return androidx.core.view.i.b(marginLayoutParams) + androidx.core.view.i.a(marginLayoutParams);
    }

    private int t(View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
    }

    private int u(List<View> list, int[] iArr) {
        int i8 = iArr[0];
        int i9 = iArr[1];
        int size = list.size();
        int i10 = 0;
        int i11 = 0;
        while (i10 < size) {
            View view = list.get(i10);
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            int i12 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin - i8;
            int i13 = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin - i9;
            int max = Math.max(0, i12);
            int max2 = Math.max(0, i13);
            int max3 = Math.max(0, -i12);
            int max4 = Math.max(0, -i13);
            i11 += max + view.getMeasuredWidth() + max2;
            i10++;
            i9 = max4;
            i8 = max3;
        }
        return i11;
    }

    private boolean z(View view) {
        return view.getParent() == this || this.P.contains(view);
    }

    public boolean A() {
        ActionMenuView actionMenuView = this.f1356a;
        return actionMenuView != null && actionMenuView.I();
    }

    public boolean B() {
        ActionMenuView actionMenuView = this.f1356a;
        return actionMenuView != null && actionMenuView.J();
    }

    void I() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = getChildAt(childCount);
            if (((LayoutParams) childAt.getLayoutParams()).f1386b != 2 && childAt != this.f1356a) {
                removeViewAt(childCount);
                this.P.add(childAt);
            }
        }
    }

    public void J(int i8, int i9) {
        h();
        this.f1385z.g(i8, i9);
    }

    public void K(androidx.appcompat.view.menu.g gVar, ActionMenuPresenter actionMenuPresenter) {
        if (gVar == null && this.f1356a == null) {
            return;
        }
        k();
        androidx.appcompat.view.menu.g N = this.f1356a.N();
        if (N == gVar) {
            return;
        }
        if (N != null) {
            N.Q(this.f1361c0);
            N.Q(this.f1363d0);
        }
        if (this.f1363d0 == null) {
            this.f1363d0 = new f();
        }
        actionMenuPresenter.K(true);
        if (gVar != null) {
            gVar.c(actionMenuPresenter, this.f1375k);
            gVar.c(this.f1363d0, this.f1375k);
        } else {
            actionMenuPresenter.k(this.f1375k, null);
            this.f1363d0.k(this.f1375k, null);
            actionMenuPresenter.f(true);
            this.f1363d0.f(true);
        }
        this.f1356a.setPopupTheme(this.f1377l);
        this.f1356a.setPresenter(actionMenuPresenter);
        this.f1361c0 = actionMenuPresenter;
        R();
    }

    public void L(m.a aVar, g.a aVar2) {
        this.f1365e0 = aVar;
        this.f1367f0 = aVar2;
        ActionMenuView actionMenuView = this.f1356a;
        if (actionMenuView != null) {
            actionMenuView.O(aVar, aVar2);
        }
    }

    public void M(Context context, int i8) {
        this.f1379n = i8;
        TextView textView = this.f1360c;
        if (textView != null) {
            textView.setTextAppearance(context, i8);
        }
    }

    public void N(Context context, int i8) {
        this.f1378m = i8;
        TextView textView = this.f1358b;
        if (textView != null) {
            textView.setTextAppearance(context, i8);
        }
    }

    public boolean Q() {
        ActionMenuView actionMenuView = this.f1356a;
        return actionMenuView != null && actionMenuView.P();
    }

    void R() {
        OnBackInvokedDispatcher onBackInvokedDispatcher;
        if (Build.VERSION.SDK_INT >= 33) {
            OnBackInvokedDispatcher a9 = e.a(this);
            boolean z4 = v() && a9 != null && androidx.core.view.c0.V(this) && this.f1374j0;
            if (z4 && this.f1372i0 == null) {
                if (this.f1371h0 == null) {
                    this.f1371h0 = e.b(new Runnable() { // from class: androidx.appcompat.widget.k0
                        @Override // java.lang.Runnable
                        public final void run() {
                            Toolbar.this.e();
                        }
                    });
                }
                e.c(a9, this.f1371h0);
            } else if (z4 || (onBackInvokedDispatcher = this.f1372i0) == null) {
                return;
            } else {
                e.d(onBackInvokedDispatcher, this.f1371h0);
                a9 = null;
            }
            this.f1372i0 = a9;
        }
    }

    void a() {
        for (int size = this.P.size() - 1; size >= 0; size--) {
            addView(this.P.get(size));
        }
        this.P.clear();
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.checkLayoutParams(layoutParams) && (layoutParams instanceof LayoutParams);
    }

    public boolean d() {
        ActionMenuView actionMenuView;
        return getVisibility() == 0 && (actionMenuView = this.f1356a) != null && actionMenuView.K();
    }

    public void e() {
        f fVar = this.f1363d0;
        androidx.appcompat.view.menu.i iVar = fVar == null ? null : fVar.f1394b;
        if (iVar != null) {
            iVar.collapseActionView();
        }
    }

    public void f() {
        ActionMenuView actionMenuView = this.f1356a;
        if (actionMenuView != null) {
            actionMenuView.B();
        }
    }

    void g() {
        if (this.f1370h == null) {
            AppCompatImageButton appCompatImageButton = new AppCompatImageButton(getContext(), null, g.a.Q);
            this.f1370h = appCompatImageButton;
            appCompatImageButton.setImageDrawable(this.f1366f);
            this.f1370h.setContentDescription(this.f1368g);
            LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
            generateDefaultLayoutParams.f469a = 8388611 | (this.f1380p & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle);
            generateDefaultLayoutParams.f1386b = 2;
            this.f1370h.setLayoutParams(generateDefaultLayoutParams);
            this.f1370h.setOnClickListener(new d());
        }
    }

    public CharSequence getCollapseContentDescription() {
        ImageButton imageButton = this.f1370h;
        if (imageButton != null) {
            return imageButton.getContentDescription();
        }
        return null;
    }

    public Drawable getCollapseIcon() {
        ImageButton imageButton = this.f1370h;
        if (imageButton != null) {
            return imageButton.getDrawable();
        }
        return null;
    }

    public int getContentInsetEnd() {
        b0 b0Var = this.f1385z;
        if (b0Var != null) {
            return b0Var.a();
        }
        return 0;
    }

    public int getContentInsetEndWithActions() {
        int i8 = this.B;
        return i8 != Integer.MIN_VALUE ? i8 : getContentInsetEnd();
    }

    public int getContentInsetLeft() {
        b0 b0Var = this.f1385z;
        if (b0Var != null) {
            return b0Var.b();
        }
        return 0;
    }

    public int getContentInsetRight() {
        b0 b0Var = this.f1385z;
        if (b0Var != null) {
            return b0Var.c();
        }
        return 0;
    }

    public int getContentInsetStart() {
        b0 b0Var = this.f1385z;
        if (b0Var != null) {
            return b0Var.d();
        }
        return 0;
    }

    public int getContentInsetStartWithNavigation() {
        int i8 = this.A;
        return i8 != Integer.MIN_VALUE ? i8 : getContentInsetStart();
    }

    public int getCurrentContentInsetEnd() {
        androidx.appcompat.view.menu.g N;
        ActionMenuView actionMenuView = this.f1356a;
        return actionMenuView != null && (N = actionMenuView.N()) != null && N.hasVisibleItems() ? Math.max(getContentInsetEnd(), Math.max(this.B, 0)) : getContentInsetEnd();
    }

    public int getCurrentContentInsetLeft() {
        return androidx.core.view.c0.E(this) == 1 ? getCurrentContentInsetEnd() : getCurrentContentInsetStart();
    }

    public int getCurrentContentInsetRight() {
        return androidx.core.view.c0.E(this) == 1 ? getCurrentContentInsetStart() : getCurrentContentInsetEnd();
    }

    public int getCurrentContentInsetStart() {
        return getNavigationIcon() != null ? Math.max(getContentInsetStart(), Math.max(this.A, 0)) : getContentInsetStart();
    }

    public Drawable getLogo() {
        ImageView imageView = this.f1364e;
        if (imageView != null) {
            return imageView.getDrawable();
        }
        return null;
    }

    public CharSequence getLogoDescription() {
        ImageView imageView = this.f1364e;
        if (imageView != null) {
            return imageView.getContentDescription();
        }
        return null;
    }

    public Menu getMenu() {
        j();
        return this.f1356a.getMenu();
    }

    View getNavButtonView() {
        return this.f1362d;
    }

    public CharSequence getNavigationContentDescription() {
        ImageButton imageButton = this.f1362d;
        if (imageButton != null) {
            return imageButton.getContentDescription();
        }
        return null;
    }

    public Drawable getNavigationIcon() {
        ImageButton imageButton = this.f1362d;
        if (imageButton != null) {
            return imageButton.getDrawable();
        }
        return null;
    }

    ActionMenuPresenter getOuterActionMenuPresenter() {
        return this.f1361c0;
    }

    public Drawable getOverflowIcon() {
        j();
        return this.f1356a.getOverflowIcon();
    }

    Context getPopupContext() {
        return this.f1375k;
    }

    public int getPopupTheme() {
        return this.f1377l;
    }

    public CharSequence getSubtitle() {
        return this.F;
    }

    final TextView getSubtitleTextView() {
        return this.f1360c;
    }

    public CharSequence getTitle() {
        return this.E;
    }

    public int getTitleMarginBottom() {
        return this.f1384y;
    }

    public int getTitleMarginEnd() {
        return this.f1382w;
    }

    public int getTitleMarginStart() {
        return this.f1381t;
    }

    public int getTitleMarginTop() {
        return this.f1383x;
    }

    final TextView getTitleTextView() {
        return this.f1358b;
    }

    public s getWrapper() {
        if (this.f1359b0 == null) {
            this.f1359b0 = new n0(this, true);
        }
        return this.f1359b0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: m */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // android.view.ViewGroup
    /* renamed from: n */
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: o */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams ? new LayoutParams((LayoutParams) layoutParams) : layoutParams instanceof ActionBar.LayoutParams ? new LayoutParams((ActionBar.LayoutParams) layoutParams) : layoutParams instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams) : new LayoutParams(layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        R();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.f1376k0);
        R();
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 9) {
            this.L = false;
        }
        if (!this.L) {
            boolean onHoverEvent = super.onHoverEvent(motionEvent);
            if (actionMasked == 9 && !onHoverEvent) {
                this.L = true;
            }
        }
        if (actionMasked == 10 || actionMasked == 3) {
            this.L = false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0295 A[LOOP:0: B:109:0x0293->B:110:0x0295, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:113:0x02b7 A[LOOP:1: B:112:0x02b5->B:113:0x02b7, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:117:0x02e1  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x02f0 A[LOOP:2: B:121:0x02ee->B:122:0x02f0, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00ad  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00c2  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00dd  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00f6  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0113  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0123  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0126  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x015e  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x019c  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01ad  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x021d  */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onLayout(boolean r20, int r21, int r22, int r23, int r24) {
        /*
            Method dump skipped, instructions count: 773
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.Toolbar.onLayout(boolean, int, int, int, int):void");
    }

    @Override // android.view.View
    protected void onMeasure(int i8, int i9) {
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int[] iArr = this.Q;
        boolean b9 = u0.b(this);
        int i17 = !b9 ? 1 : 0;
        if (P(this.f1362d)) {
            F(this.f1362d, i8, 0, i9, 0, this.q);
            i10 = this.f1362d.getMeasuredWidth() + s(this.f1362d);
            i11 = Math.max(0, this.f1362d.getMeasuredHeight() + t(this.f1362d));
            i12 = View.combineMeasuredStates(0, this.f1362d.getMeasuredState());
        } else {
            i10 = 0;
            i11 = 0;
            i12 = 0;
        }
        if (P(this.f1370h)) {
            F(this.f1370h, i8, 0, i9, 0, this.q);
            i10 = this.f1370h.getMeasuredWidth() + s(this.f1370h);
            i11 = Math.max(i11, this.f1370h.getMeasuredHeight() + t(this.f1370h));
            i12 = View.combineMeasuredStates(i12, this.f1370h.getMeasuredState());
        }
        int currentContentInsetStart = getCurrentContentInsetStart();
        int max = 0 + Math.max(currentContentInsetStart, i10);
        iArr[b9 ? 1 : 0] = Math.max(0, currentContentInsetStart - i10);
        if (P(this.f1356a)) {
            F(this.f1356a, i8, max, i9, 0, this.q);
            i13 = this.f1356a.getMeasuredWidth() + s(this.f1356a);
            i11 = Math.max(i11, this.f1356a.getMeasuredHeight() + t(this.f1356a));
            i12 = View.combineMeasuredStates(i12, this.f1356a.getMeasuredState());
        } else {
            i13 = 0;
        }
        int currentContentInsetEnd = getCurrentContentInsetEnd();
        int max2 = max + Math.max(currentContentInsetEnd, i13);
        iArr[i17] = Math.max(0, currentContentInsetEnd - i13);
        if (P(this.f1373j)) {
            max2 += E(this.f1373j, i8, max2, i9, 0, iArr);
            i11 = Math.max(i11, this.f1373j.getMeasuredHeight() + t(this.f1373j));
            i12 = View.combineMeasuredStates(i12, this.f1373j.getMeasuredState());
        }
        if (P(this.f1364e)) {
            max2 += E(this.f1364e, i8, max2, i9, 0, iArr);
            i11 = Math.max(i11, this.f1364e.getMeasuredHeight() + t(this.f1364e));
            i12 = View.combineMeasuredStates(i12, this.f1364e.getMeasuredState());
        }
        int childCount = getChildCount();
        for (int i18 = 0; i18 < childCount; i18++) {
            View childAt = getChildAt(i18);
            if (((LayoutParams) childAt.getLayoutParams()).f1386b == 0 && P(childAt)) {
                max2 += E(childAt, i8, max2, i9, 0, iArr);
                i11 = Math.max(i11, childAt.getMeasuredHeight() + t(childAt));
                i12 = View.combineMeasuredStates(i12, childAt.getMeasuredState());
            }
        }
        int i19 = this.f1383x + this.f1384y;
        int i20 = this.f1381t + this.f1382w;
        if (P(this.f1358b)) {
            E(this.f1358b, i8, max2 + i20, i9, i19, iArr);
            int measuredWidth = this.f1358b.getMeasuredWidth() + s(this.f1358b);
            i14 = this.f1358b.getMeasuredHeight() + t(this.f1358b);
            i15 = View.combineMeasuredStates(i12, this.f1358b.getMeasuredState());
            i16 = measuredWidth;
        } else {
            i14 = 0;
            i15 = i12;
            i16 = 0;
        }
        if (P(this.f1360c)) {
            i16 = Math.max(i16, E(this.f1360c, i8, max2 + i20, i9, i14 + i19, iArr));
            i14 += this.f1360c.getMeasuredHeight() + t(this.f1360c);
            i15 = View.combineMeasuredStates(i15, this.f1360c.getMeasuredState());
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max(max2 + i16 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i8, (-16777216) & i15), O() ? 0 : View.resolveSizeAndState(Math.max(Math.max(i11, i14) + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i9, i15 << 16));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        MenuItem findItem;
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.a());
        ActionMenuView actionMenuView = this.f1356a;
        androidx.appcompat.view.menu.g N = actionMenuView != null ? actionMenuView.N() : null;
        int i8 = savedState.f1387c;
        if (i8 != 0 && this.f1363d0 != null && N != null && (findItem = N.findItem(i8)) != null) {
            findItem.expandActionView();
        }
        if (savedState.f1388d) {
            H();
        }
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i8) {
        if (Build.VERSION.SDK_INT >= 17) {
            super.onRtlPropertiesChanged(i8);
        }
        h();
        this.f1385z.f(i8 == 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        androidx.appcompat.view.menu.i iVar;
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        f fVar = this.f1363d0;
        if (fVar != null && (iVar = fVar.f1394b) != null) {
            savedState.f1387c = iVar.getItemId();
        }
        savedState.f1388d = B();
        return savedState;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.K = false;
        }
        if (!this.K) {
            boolean onTouchEvent = super.onTouchEvent(motionEvent);
            if (actionMasked == 0 && !onTouchEvent) {
                this.K = true;
            }
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.K = false;
        }
        return true;
    }

    public void setBackInvokedCallbackEnabled(boolean z4) {
        if (this.f1374j0 != z4) {
            this.f1374j0 = z4;
            R();
        }
    }

    public void setCollapseContentDescription(int i8) {
        setCollapseContentDescription(i8 != 0 ? getContext().getText(i8) : null);
    }

    public void setCollapseContentDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            g();
        }
        ImageButton imageButton = this.f1370h;
        if (imageButton != null) {
            imageButton.setContentDescription(charSequence);
        }
    }

    public void setCollapseIcon(int i8) {
        setCollapseIcon(h.a.b(getContext(), i8));
    }

    public void setCollapseIcon(Drawable drawable) {
        if (drawable != null) {
            g();
            this.f1370h.setImageDrawable(drawable);
            return;
        }
        ImageButton imageButton = this.f1370h;
        if (imageButton != null) {
            imageButton.setImageDrawable(this.f1366f);
        }
    }

    public void setCollapsible(boolean z4) {
        this.f1369g0 = z4;
        requestLayout();
    }

    public void setContentInsetEndWithActions(int i8) {
        if (i8 < 0) {
            i8 = Integer.MIN_VALUE;
        }
        if (i8 != this.B) {
            this.B = i8;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public void setContentInsetStartWithNavigation(int i8) {
        if (i8 < 0) {
            i8 = Integer.MIN_VALUE;
        }
        if (i8 != this.A) {
            this.A = i8;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public void setLogo(int i8) {
        setLogo(h.a.b(getContext(), i8));
    }

    public void setLogo(Drawable drawable) {
        if (drawable != null) {
            i();
            if (!z(this.f1364e)) {
                c(this.f1364e, true);
            }
        } else {
            ImageView imageView = this.f1364e;
            if (imageView != null && z(imageView)) {
                removeView(this.f1364e);
                this.P.remove(this.f1364e);
            }
        }
        ImageView imageView2 = this.f1364e;
        if (imageView2 != null) {
            imageView2.setImageDrawable(drawable);
        }
    }

    public void setLogoDescription(int i8) {
        setLogoDescription(getContext().getText(i8));
    }

    public void setLogoDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            i();
        }
        ImageView imageView = this.f1364e;
        if (imageView != null) {
            imageView.setContentDescription(charSequence);
        }
    }

    public void setNavigationContentDescription(int i8) {
        setNavigationContentDescription(i8 != 0 ? getContext().getText(i8) : null);
    }

    public void setNavigationContentDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            l();
        }
        ImageButton imageButton = this.f1362d;
        if (imageButton != null) {
            imageButton.setContentDescription(charSequence);
            o0.a(this.f1362d, charSequence);
        }
    }

    public void setNavigationIcon(int i8) {
        setNavigationIcon(h.a.b(getContext(), i8));
    }

    public void setNavigationIcon(Drawable drawable) {
        if (drawable != null) {
            l();
            if (!z(this.f1362d)) {
                c(this.f1362d, true);
            }
        } else {
            ImageButton imageButton = this.f1362d;
            if (imageButton != null && z(imageButton)) {
                removeView(this.f1362d);
                this.P.remove(this.f1362d);
            }
        }
        ImageButton imageButton2 = this.f1362d;
        if (imageButton2 != null) {
            imageButton2.setImageDrawable(drawable);
        }
    }

    public void setNavigationOnClickListener(View.OnClickListener onClickListener) {
        l();
        this.f1362d.setOnClickListener(onClickListener);
    }

    public void setOnMenuItemClickListener(g gVar) {
        this.W = gVar;
    }

    public void setOverflowIcon(Drawable drawable) {
        j();
        this.f1356a.setOverflowIcon(drawable);
    }

    public void setPopupTheme(int i8) {
        if (this.f1377l != i8) {
            this.f1377l = i8;
            if (i8 == 0) {
                this.f1375k = getContext();
            } else {
                this.f1375k = new ContextThemeWrapper(getContext(), i8);
            }
        }
    }

    public void setSubtitle(int i8) {
        setSubtitle(getContext().getText(i8));
    }

    public void setSubtitle(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            TextView textView = this.f1360c;
            if (textView != null && z(textView)) {
                removeView(this.f1360c);
                this.P.remove(this.f1360c);
            }
        } else {
            if (this.f1360c == null) {
                Context context = getContext();
                AppCompatTextView appCompatTextView = new AppCompatTextView(context);
                this.f1360c = appCompatTextView;
                appCompatTextView.setSingleLine();
                this.f1360c.setEllipsize(TextUtils.TruncateAt.END);
                int i8 = this.f1379n;
                if (i8 != 0) {
                    this.f1360c.setTextAppearance(context, i8);
                }
                ColorStateList colorStateList = this.H;
                if (colorStateList != null) {
                    this.f1360c.setTextColor(colorStateList);
                }
            }
            if (!z(this.f1360c)) {
                c(this.f1360c, true);
            }
        }
        TextView textView2 = this.f1360c;
        if (textView2 != null) {
            textView2.setText(charSequence);
        }
        this.F = charSequence;
    }

    public void setSubtitleTextColor(int i8) {
        setSubtitleTextColor(ColorStateList.valueOf(i8));
    }

    public void setSubtitleTextColor(ColorStateList colorStateList) {
        this.H = colorStateList;
        TextView textView = this.f1360c;
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }

    public void setTitle(int i8) {
        setTitle(getContext().getText(i8));
    }

    public void setTitle(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            TextView textView = this.f1358b;
            if (textView != null && z(textView)) {
                removeView(this.f1358b);
                this.P.remove(this.f1358b);
            }
        } else {
            if (this.f1358b == null) {
                Context context = getContext();
                AppCompatTextView appCompatTextView = new AppCompatTextView(context);
                this.f1358b = appCompatTextView;
                appCompatTextView.setSingleLine();
                this.f1358b.setEllipsize(TextUtils.TruncateAt.END);
                int i8 = this.f1378m;
                if (i8 != 0) {
                    this.f1358b.setTextAppearance(context, i8);
                }
                ColorStateList colorStateList = this.G;
                if (colorStateList != null) {
                    this.f1358b.setTextColor(colorStateList);
                }
            }
            if (!z(this.f1358b)) {
                c(this.f1358b, true);
            }
        }
        TextView textView2 = this.f1358b;
        if (textView2 != null) {
            textView2.setText(charSequence);
        }
        this.E = charSequence;
    }

    public void setTitleMarginBottom(int i8) {
        this.f1384y = i8;
        requestLayout();
    }

    public void setTitleMarginEnd(int i8) {
        this.f1382w = i8;
        requestLayout();
    }

    public void setTitleMarginStart(int i8) {
        this.f1381t = i8;
        requestLayout();
    }

    public void setTitleMarginTop(int i8) {
        this.f1383x = i8;
        requestLayout();
    }

    public void setTitleTextColor(int i8) {
        setTitleTextColor(ColorStateList.valueOf(i8));
    }

    public void setTitleTextColor(ColorStateList colorStateList) {
        this.G = colorStateList;
        TextView textView = this.f1358b;
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }

    public boolean v() {
        f fVar = this.f1363d0;
        return (fVar == null || fVar.f1394b == null) ? false : true;
    }

    public boolean w() {
        ActionMenuView actionMenuView = this.f1356a;
        return actionMenuView != null && actionMenuView.H();
    }

    public void x(int i8) {
        getMenuInflater().inflate(i8, getMenu());
    }

    public void y() {
        Iterator<MenuItem> it = this.T.iterator();
        while (it.hasNext()) {
            getMenu().removeItem(it.next().getItemId());
        }
        G();
    }
}
