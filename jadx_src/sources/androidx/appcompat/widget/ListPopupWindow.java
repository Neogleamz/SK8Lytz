package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ListPopupWindow implements androidx.appcompat.view.menu.p {
    private static Method R;
    private static Method T;
    private static Method W;
    private AdapterView.OnItemClickListener A;
    private AdapterView.OnItemSelectedListener B;
    final i C;
    private final h E;
    private final g F;
    private final e G;
    private Runnable H;
    final Handler K;
    private final Rect L;
    private Rect O;
    private boolean P;
    PopupWindow Q;

    /* renamed from: a  reason: collision with root package name */
    private Context f1243a;

    /* renamed from: b  reason: collision with root package name */
    private ListAdapter f1244b;

    /* renamed from: c  reason: collision with root package name */
    u f1245c;

    /* renamed from: d  reason: collision with root package name */
    private int f1246d;

    /* renamed from: e  reason: collision with root package name */
    private int f1247e;

    /* renamed from: f  reason: collision with root package name */
    private int f1248f;

    /* renamed from: g  reason: collision with root package name */
    private int f1249g;

    /* renamed from: h  reason: collision with root package name */
    private int f1250h;

    /* renamed from: j  reason: collision with root package name */
    private boolean f1251j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f1252k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f1253l;

    /* renamed from: m  reason: collision with root package name */
    private int f1254m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f1255n;

    /* renamed from: p  reason: collision with root package name */
    private boolean f1256p;
    int q;

    /* renamed from: t  reason: collision with root package name */
    private View f1257t;

    /* renamed from: w  reason: collision with root package name */
    private int f1258w;

    /* renamed from: x  reason: collision with root package name */
    private DataSetObserver f1259x;

    /* renamed from: y  reason: collision with root package name */
    private View f1260y;

    /* renamed from: z  reason: collision with root package name */
    private Drawable f1261z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            View t8 = ListPopupWindow.this.t();
            if (t8 == null || t8.getWindowToken() == null) {
                return;
            }
            ListPopupWindow.this.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements AdapterView.OnItemSelectedListener {
        b() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i8, long j8) {
            u uVar;
            if (i8 == -1 || (uVar = ListPopupWindow.this.f1245c) == null) {
                return;
            }
            uVar.setListSelectionHidden(false);
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {
        static int a(PopupWindow popupWindow, View view, int i8, boolean z4) {
            return popupWindow.getMaxAvailableHeight(view, i8, z4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d {
        static void a(PopupWindow popupWindow, Rect rect) {
            popupWindow.setEpicenterBounds(rect);
        }

        static void b(PopupWindow popupWindow, boolean z4) {
            popupWindow.setIsClippedToScreen(z4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e implements Runnable {
        e() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ListPopupWindow.this.r();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f extends DataSetObserver {
        f() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            if (ListPopupWindow.this.b()) {
                ListPopupWindow.this.a();
            }
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            ListPopupWindow.this.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class g implements AbsListView.OnScrollListener {
        g() {
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScroll(AbsListView absListView, int i8, int i9, int i10) {
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScrollStateChanged(AbsListView absListView, int i8) {
            if (i8 != 1 || ListPopupWindow.this.A() || ListPopupWindow.this.Q.getContentView() == null) {
                return;
            }
            ListPopupWindow listPopupWindow = ListPopupWindow.this;
            listPopupWindow.K.removeCallbacks(listPopupWindow.C);
            ListPopupWindow.this.C.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class h implements View.OnTouchListener {
        h() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            PopupWindow popupWindow;
            int action = motionEvent.getAction();
            int x8 = (int) motionEvent.getX();
            int y8 = (int) motionEvent.getY();
            if (action == 0 && (popupWindow = ListPopupWindow.this.Q) != null && popupWindow.isShowing() && x8 >= 0 && x8 < ListPopupWindow.this.Q.getWidth() && y8 >= 0 && y8 < ListPopupWindow.this.Q.getHeight()) {
                ListPopupWindow listPopupWindow = ListPopupWindow.this;
                listPopupWindow.K.postDelayed(listPopupWindow.C, 250L);
                return false;
            } else if (action == 1) {
                ListPopupWindow listPopupWindow2 = ListPopupWindow.this;
                listPopupWindow2.K.removeCallbacks(listPopupWindow2.C);
                return false;
            } else {
                return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class i implements Runnable {
        i() {
        }

        @Override // java.lang.Runnable
        public void run() {
            u uVar = ListPopupWindow.this.f1245c;
            if (uVar == null || !androidx.core.view.c0.V(uVar) || ListPopupWindow.this.f1245c.getCount() <= ListPopupWindow.this.f1245c.getChildCount()) {
                return;
            }
            int childCount = ListPopupWindow.this.f1245c.getChildCount();
            ListPopupWindow listPopupWindow = ListPopupWindow.this;
            if (childCount <= listPopupWindow.q) {
                listPopupWindow.Q.setInputMethodMode(2);
                ListPopupWindow.this.a();
            }
        }
    }

    static {
        if (Build.VERSION.SDK_INT <= 28) {
            try {
                R = PopupWindow.class.getDeclaredMethod("setClipToScreenEnabled", Boolean.TYPE);
            } catch (NoSuchMethodException unused) {
                Log.i("ListPopupWindow", "Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
            }
            try {
                W = PopupWindow.class.getDeclaredMethod("setEpicenterBounds", Rect.class);
            } catch (NoSuchMethodException unused2) {
                Log.i("ListPopupWindow", "Could not find method setEpicenterBounds(Rect) on PopupWindow. Oh well.");
            }
        }
        if (Build.VERSION.SDK_INT <= 23) {
            try {
                T = PopupWindow.class.getDeclaredMethod("getMaxAvailableHeight", View.class, Integer.TYPE, Boolean.TYPE);
            } catch (NoSuchMethodException unused3) {
                Log.i("ListPopupWindow", "Could not find method getMaxAvailableHeight(View, int, boolean) on PopupWindow. Oh well.");
            }
        }
    }

    public ListPopupWindow(Context context) {
        this(context, null, g.a.H);
    }

    public ListPopupWindow(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, g.a.H);
    }

    public ListPopupWindow(Context context, AttributeSet attributeSet, int i8) {
        this(context, attributeSet, i8, 0);
    }

    public ListPopupWindow(Context context, AttributeSet attributeSet, int i8, int i9) {
        this.f1246d = -2;
        this.f1247e = -2;
        this.f1250h = 1002;
        this.f1254m = 0;
        this.f1255n = false;
        this.f1256p = false;
        this.q = Integer.MAX_VALUE;
        this.f1258w = 0;
        this.C = new i();
        this.E = new h();
        this.F = new g();
        this.G = new e();
        this.L = new Rect();
        this.f1243a = context;
        this.K = new Handler(context.getMainLooper());
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, g.j.f20113v1, i8, i9);
        this.f1248f = obtainStyledAttributes.getDimensionPixelOffset(g.j.f20118w1, 0);
        int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(g.j.f20123x1, 0);
        this.f1249g = dimensionPixelOffset;
        if (dimensionPixelOffset != 0) {
            this.f1251j = true;
        }
        obtainStyledAttributes.recycle();
        AppCompatPopupWindow appCompatPopupWindow = new AppCompatPopupWindow(context, attributeSet, i8, i9);
        this.Q = appCompatPopupWindow;
        appCompatPopupWindow.setInputMethodMode(1);
    }

    private void C() {
        View view = this.f1257t;
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.f1257t);
            }
        }
    }

    private void N(boolean z4) {
        if (Build.VERSION.SDK_INT > 28) {
            d.b(this.Q, z4);
            return;
        }
        Method method = R;
        if (method != null) {
            try {
                method.invoke(this.Q, Boolean.valueOf(z4));
            } catch (Exception unused) {
                Log.i("ListPopupWindow", "Could not call setClipToScreenEnabled() on PopupWindow. Oh well.");
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0150  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int q() {
        /*
            Method dump skipped, instructions count: 355
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ListPopupWindow.q():int");
    }

    private int u(View view, int i8, boolean z4) {
        if (Build.VERSION.SDK_INT <= 23) {
            Method method = T;
            if (method != null) {
                try {
                    return ((Integer) method.invoke(this.Q, view, Integer.valueOf(i8), Boolean.valueOf(z4))).intValue();
                } catch (Exception unused) {
                    Log.i("ListPopupWindow", "Could not call getMaxAvailableHeightMethod(View, int, boolean) on PopupWindow. Using the public version.");
                }
            }
            return this.Q.getMaxAvailableHeight(view, i8);
        }
        return c.a(this.Q, view, i8, z4);
    }

    public boolean A() {
        return this.Q.getInputMethodMode() == 2;
    }

    public boolean B() {
        return this.P;
    }

    public void D(View view) {
        this.f1260y = view;
    }

    public void E(int i8) {
        this.Q.setAnimationStyle(i8);
    }

    public void F(int i8) {
        Drawable background = this.Q.getBackground();
        if (background == null) {
            Q(i8);
            return;
        }
        background.getPadding(this.L);
        Rect rect = this.L;
        this.f1247e = rect.left + rect.right + i8;
    }

    public void G(int i8) {
        this.f1254m = i8;
    }

    public void H(Rect rect) {
        this.O = rect != null ? new Rect(rect) : null;
    }

    public void I(int i8) {
        this.Q.setInputMethodMode(i8);
    }

    public void J(boolean z4) {
        this.P = z4;
        this.Q.setFocusable(z4);
    }

    public void K(PopupWindow.OnDismissListener onDismissListener) {
        this.Q.setOnDismissListener(onDismissListener);
    }

    public void L(AdapterView.OnItemClickListener onItemClickListener) {
        this.A = onItemClickListener;
    }

    public void M(boolean z4) {
        this.f1253l = true;
        this.f1252k = z4;
    }

    public void O(int i8) {
        this.f1258w = i8;
    }

    public void P(int i8) {
        u uVar = this.f1245c;
        if (!b() || uVar == null) {
            return;
        }
        uVar.setListSelectionHidden(false);
        uVar.setSelection(i8);
        if (uVar.getChoiceMode() != 0) {
            uVar.setItemChecked(i8, true);
        }
    }

    public void Q(int i8) {
        this.f1247e = i8;
    }

    @Override // androidx.appcompat.view.menu.p
    public void a() {
        int q = q();
        boolean A = A();
        androidx.core.widget.j.b(this.Q, this.f1250h);
        boolean z4 = true;
        if (this.Q.isShowing()) {
            if (androidx.core.view.c0.V(t())) {
                int i8 = this.f1247e;
                if (i8 == -1) {
                    i8 = -1;
                } else if (i8 == -2) {
                    i8 = t().getWidth();
                }
                int i9 = this.f1246d;
                if (i9 == -1) {
                    if (!A) {
                        q = -1;
                    }
                    if (A) {
                        this.Q.setWidth(this.f1247e == -1 ? -1 : 0);
                        this.Q.setHeight(0);
                    } else {
                        this.Q.setWidth(this.f1247e == -1 ? -1 : 0);
                        this.Q.setHeight(-1);
                    }
                } else if (i9 != -2) {
                    q = i9;
                }
                PopupWindow popupWindow = this.Q;
                if (this.f1256p || this.f1255n) {
                    z4 = false;
                }
                popupWindow.setOutsideTouchable(z4);
                this.Q.update(t(), this.f1248f, this.f1249g, i8 < 0 ? -1 : i8, q < 0 ? -1 : q);
                return;
            }
            return;
        }
        int i10 = this.f1247e;
        if (i10 == -1) {
            i10 = -1;
        } else if (i10 == -2) {
            i10 = t().getWidth();
        }
        int i11 = this.f1246d;
        if (i11 == -1) {
            q = -1;
        } else if (i11 != -2) {
            q = i11;
        }
        this.Q.setWidth(i10);
        this.Q.setHeight(q);
        N(true);
        this.Q.setOutsideTouchable((this.f1256p || this.f1255n) ? false : true);
        this.Q.setTouchInterceptor(this.E);
        if (this.f1253l) {
            androidx.core.widget.j.a(this.Q, this.f1252k);
        }
        if (Build.VERSION.SDK_INT <= 28) {
            Method method = W;
            if (method != null) {
                try {
                    method.invoke(this.Q, this.O);
                } catch (Exception e8) {
                    Log.e("ListPopupWindow", "Could not invoke setEpicenterBounds on PopupWindow", e8);
                }
            }
        } else {
            d.a(this.Q, this.O);
        }
        androidx.core.widget.j.c(this.Q, t(), this.f1248f, this.f1249g, this.f1254m);
        this.f1245c.setSelection(-1);
        if (!this.P || this.f1245c.isInTouchMode()) {
            r();
        }
        if (this.P) {
            return;
        }
        this.K.post(this.G);
    }

    @Override // androidx.appcompat.view.menu.p
    public boolean b() {
        return this.Q.isShowing();
    }

    public void c(Drawable drawable) {
        this.Q.setBackgroundDrawable(drawable);
    }

    public int d() {
        return this.f1248f;
    }

    @Override // androidx.appcompat.view.menu.p
    public void dismiss() {
        this.Q.dismiss();
        C();
        this.Q.setContentView(null);
        this.f1245c = null;
        this.K.removeCallbacks(this.C);
    }

    public void f(int i8) {
        this.f1248f = i8;
    }

    public Drawable i() {
        return this.Q.getBackground();
    }

    public void k(int i8) {
        this.f1249g = i8;
        this.f1251j = true;
    }

    @Override // androidx.appcompat.view.menu.p
    public ListView m() {
        return this.f1245c;
    }

    public int o() {
        if (this.f1251j) {
            return this.f1249g;
        }
        return 0;
    }

    public void p(ListAdapter listAdapter) {
        DataSetObserver dataSetObserver = this.f1259x;
        if (dataSetObserver == null) {
            this.f1259x = new f();
        } else {
            ListAdapter listAdapter2 = this.f1244b;
            if (listAdapter2 != null) {
                listAdapter2.unregisterDataSetObserver(dataSetObserver);
            }
        }
        this.f1244b = listAdapter;
        if (listAdapter != null) {
            listAdapter.registerDataSetObserver(this.f1259x);
        }
        u uVar = this.f1245c;
        if (uVar != null) {
            uVar.setAdapter(this.f1244b);
        }
    }

    public void r() {
        u uVar = this.f1245c;
        if (uVar != null) {
            uVar.setListSelectionHidden(true);
            uVar.requestLayout();
        }
    }

    u s(Context context, boolean z4) {
        return new u(context, z4);
    }

    public View t() {
        return this.f1260y;
    }

    public Object v() {
        if (b()) {
            return this.f1245c.getSelectedItem();
        }
        return null;
    }

    public long w() {
        if (b()) {
            return this.f1245c.getSelectedItemId();
        }
        return Long.MIN_VALUE;
    }

    public int x() {
        if (b()) {
            return this.f1245c.getSelectedItemPosition();
        }
        return -1;
    }

    public View y() {
        if (b()) {
            return this.f1245c.getSelectedView();
        }
        return null;
    }

    public int z() {
        return this.f1247e;
    }
}
