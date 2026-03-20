package androidx.appcompat.widget;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ActivityChooserView extends ViewGroup {

    /* renamed from: a  reason: collision with root package name */
    final f f1108a;

    /* renamed from: b  reason: collision with root package name */
    private final g f1109b;

    /* renamed from: c  reason: collision with root package name */
    private final View f1110c;

    /* renamed from: d  reason: collision with root package name */
    private final Drawable f1111d;

    /* renamed from: e  reason: collision with root package name */
    final FrameLayout f1112e;

    /* renamed from: f  reason: collision with root package name */
    private final ImageView f1113f;

    /* renamed from: g  reason: collision with root package name */
    final FrameLayout f1114g;

    /* renamed from: h  reason: collision with root package name */
    private final ImageView f1115h;

    /* renamed from: j  reason: collision with root package name */
    private final int f1116j;

    /* renamed from: k  reason: collision with root package name */
    androidx.core.view.b f1117k;

    /* renamed from: l  reason: collision with root package name */
    final DataSetObserver f1118l;

    /* renamed from: m  reason: collision with root package name */
    private final ViewTreeObserver.OnGlobalLayoutListener f1119m;

    /* renamed from: n  reason: collision with root package name */
    private ListPopupWindow f1120n;

    /* renamed from: p  reason: collision with root package name */
    PopupWindow.OnDismissListener f1121p;
    boolean q;

    /* renamed from: t  reason: collision with root package name */
    int f1122t;

    /* renamed from: w  reason: collision with root package name */
    private boolean f1123w;

    /* renamed from: x  reason: collision with root package name */
    private int f1124x;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class InnerLayout extends LinearLayout {

        /* renamed from: a  reason: collision with root package name */
        private static final int[] f1125a = {16842964};

        public InnerLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            j0 u8 = j0.u(context, attributeSet, f1125a);
            setBackgroundDrawable(u8.g(0));
            u8.w();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends DataSetObserver {
        a() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            super.onChanged();
            ActivityChooserView.this.f1108a.notifyDataSetChanged();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            super.onInvalidated();
            ActivityChooserView.this.f1108a.notifyDataSetInvalidated();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements ViewTreeObserver.OnGlobalLayoutListener {
        b() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (ActivityChooserView.this.b()) {
                if (!ActivityChooserView.this.isShown()) {
                    ActivityChooserView.this.getListPopupWindow().dismiss();
                    return;
                }
                ActivityChooserView.this.getListPopupWindow().a();
                androidx.core.view.b bVar = ActivityChooserView.this.f1117k;
                if (bVar != null) {
                    bVar.k(true);
                }
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c extends View.AccessibilityDelegate {
        c() {
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            androidx.core.view.accessibility.c.I0(accessibilityNodeInfo).Z(true);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d extends w {
        d(View view) {
            super(view);
        }

        @Override // androidx.appcompat.widget.w
        public androidx.appcompat.view.menu.p b() {
            return ActivityChooserView.this.getListPopupWindow();
        }

        @Override // androidx.appcompat.widget.w
        protected boolean c() {
            ActivityChooserView.this.c();
            return true;
        }

        @Override // androidx.appcompat.widget.w
        protected boolean d() {
            ActivityChooserView.this.a();
            return true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class e extends DataSetObserver {
        e() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            super.onChanged();
            ActivityChooserView.this.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f extends BaseAdapter {

        /* renamed from: a  reason: collision with root package name */
        private androidx.appcompat.widget.c f1131a;

        /* renamed from: b  reason: collision with root package name */
        private int f1132b = 4;

        /* renamed from: c  reason: collision with root package name */
        private boolean f1133c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f1134d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f1135e;

        f() {
        }

        public int a() {
            throw null;
        }

        public androidx.appcompat.widget.c b() {
            return this.f1131a;
        }

        public ResolveInfo c() {
            throw null;
        }

        public int d() {
            throw null;
        }

        public boolean e() {
            return this.f1133c;
        }

        public void f(androidx.appcompat.widget.c cVar) {
            ActivityChooserView.this.f1108a.b();
            notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            throw null;
        }

        @Override // android.widget.Adapter
        public Object getItem(int i8) {
            int itemViewType = getItemViewType(i8);
            if (itemViewType == 0) {
                boolean z4 = this.f1133c;
                throw null;
            } else if (itemViewType == 1) {
                return null;
            } else {
                throw new IllegalArgumentException();
            }
        }

        @Override // android.widget.Adapter
        public long getItemId(int i8) {
            return i8;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getItemViewType(int i8) {
            return (this.f1135e && i8 == getCount() - 1) ? 1 : 0;
        }

        @Override // android.widget.Adapter
        public View getView(int i8, View view, ViewGroup viewGroup) {
            int itemViewType = getItemViewType(i8);
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    if (view == null || view.getId() != 1) {
                        View inflate = LayoutInflater.from(ActivityChooserView.this.getContext()).inflate(g.g.f19966f, viewGroup, false);
                        inflate.setId(1);
                        ((TextView) inflate.findViewById(g.f.S)).setText(ActivityChooserView.this.getContext().getString(g.h.f19983b));
                        return inflate;
                    }
                    return view;
                }
                throw new IllegalArgumentException();
            }
            if (view == null || view.getId() != g.f.f19958x) {
                view = LayoutInflater.from(ActivityChooserView.this.getContext()).inflate(g.g.f19966f, viewGroup, false);
            }
            PackageManager packageManager = ActivityChooserView.this.getContext().getPackageManager();
            ResolveInfo resolveInfo = (ResolveInfo) getItem(i8);
            ((ImageView) view.findViewById(g.f.f19956v)).setImageDrawable(resolveInfo.loadIcon(packageManager));
            ((TextView) view.findViewById(g.f.S)).setText(resolveInfo.loadLabel(packageManager));
            if (this.f1133c && i8 == 0 && this.f1134d) {
                view.setActivated(true);
            } else {
                view.setActivated(false);
            }
            return view;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getViewTypeCount() {
            return 3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class g implements AdapterView.OnItemClickListener, View.OnClickListener, View.OnLongClickListener, PopupWindow.OnDismissListener {
        g() {
        }

        private void a() {
            PopupWindow.OnDismissListener onDismissListener = ActivityChooserView.this.f1121p;
            if (onDismissListener != null) {
                onDismissListener.onDismiss();
            }
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ActivityChooserView activityChooserView = ActivityChooserView.this;
            if (view == activityChooserView.f1114g) {
                activityChooserView.a();
                ActivityChooserView.this.f1108a.c();
                ActivityChooserView.this.f1108a.b();
                throw null;
            } else if (view != activityChooserView.f1112e) {
                throw new IllegalArgumentException();
            } else {
                activityChooserView.q = false;
                activityChooserView.d(activityChooserView.f1122t);
            }
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            a();
            androidx.core.view.b bVar = ActivityChooserView.this.f1117k;
            if (bVar != null) {
                bVar.k(false);
            }
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i8, long j8) {
            int itemViewType = ((f) adapterView.getAdapter()).getItemViewType(i8);
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    throw new IllegalArgumentException();
                }
                ActivityChooserView.this.d(Integer.MAX_VALUE);
                return;
            }
            ActivityChooserView.this.a();
            ActivityChooserView activityChooserView = ActivityChooserView.this;
            if (!activityChooserView.q) {
                activityChooserView.f1108a.e();
                ActivityChooserView.this.f1108a.b();
                throw null;
            } else if (i8 <= 0) {
            } else {
                activityChooserView.f1108a.b();
                throw null;
            }
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            ActivityChooserView activityChooserView = ActivityChooserView.this;
            if (view == activityChooserView.f1114g) {
                if (activityChooserView.f1108a.getCount() > 0) {
                    ActivityChooserView activityChooserView2 = ActivityChooserView.this;
                    activityChooserView2.q = true;
                    activityChooserView2.d(activityChooserView2.f1122t);
                }
                return true;
            }
            throw new IllegalArgumentException();
        }
    }

    public ActivityChooserView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ActivityChooserView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f1118l = new a();
        this.f1119m = new b();
        this.f1122t = 4;
        int[] iArr = g.j.E;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i8, 0);
        androidx.core.view.c0.r0(this, context, iArr, attributeSet, obtainStyledAttributes, i8, 0);
        this.f1122t = obtainStyledAttributes.getInt(g.j.G, 4);
        Drawable drawable = obtainStyledAttributes.getDrawable(g.j.F);
        obtainStyledAttributes.recycle();
        LayoutInflater.from(getContext()).inflate(g.g.f19965e, (ViewGroup) this, true);
        g gVar = new g();
        this.f1109b = gVar;
        View findViewById = findViewById(g.f.f19945j);
        this.f1110c = findViewById;
        this.f1111d = findViewById.getBackground();
        FrameLayout frameLayout = (FrameLayout) findViewById(g.f.f19952r);
        this.f1114g = frameLayout;
        frameLayout.setOnClickListener(gVar);
        frameLayout.setOnLongClickListener(gVar);
        int i9 = g.f.f19957w;
        this.f1115h = (ImageView) frameLayout.findViewById(i9);
        FrameLayout frameLayout2 = (FrameLayout) findViewById(g.f.f19954t);
        frameLayout2.setOnClickListener(gVar);
        frameLayout2.setAccessibilityDelegate(new c());
        frameLayout2.setOnTouchListener(new d(frameLayout2));
        this.f1112e = frameLayout2;
        ImageView imageView = (ImageView) frameLayout2.findViewById(i9);
        this.f1113f = imageView;
        imageView.setImageDrawable(drawable);
        f fVar = new f();
        this.f1108a = fVar;
        fVar.registerDataSetObserver(new e());
        Resources resources = context.getResources();
        this.f1116j = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(g.d.f19899d));
    }

    public boolean a() {
        if (b()) {
            getListPopupWindow().dismiss();
            ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeGlobalOnLayoutListener(this.f1119m);
                return true;
            }
            return true;
        }
        return true;
    }

    public boolean b() {
        return getListPopupWindow().b();
    }

    public boolean c() {
        if (b() || !this.f1123w) {
            return false;
        }
        this.q = false;
        d(this.f1122t);
        return true;
    }

    void d(int i8) {
        this.f1108a.b();
        throw new IllegalStateException("No data model. Did you call #setDataModel?");
    }

    void e() {
        View view;
        Drawable drawable;
        if (this.f1108a.getCount() > 0) {
            this.f1112e.setEnabled(true);
        } else {
            this.f1112e.setEnabled(false);
        }
        int a9 = this.f1108a.a();
        int d8 = this.f1108a.d();
        if (a9 == 1 || (a9 > 1 && d8 > 0)) {
            this.f1114g.setVisibility(0);
            ResolveInfo c9 = this.f1108a.c();
            PackageManager packageManager = getContext().getPackageManager();
            this.f1115h.setImageDrawable(c9.loadIcon(packageManager));
            if (this.f1124x != 0) {
                this.f1114g.setContentDescription(getContext().getString(this.f1124x, c9.loadLabel(packageManager)));
            }
        } else {
            this.f1114g.setVisibility(8);
        }
        if (this.f1114g.getVisibility() == 0) {
            view = this.f1110c;
            drawable = this.f1111d;
        } else {
            view = this.f1110c;
            drawable = null;
        }
        view.setBackgroundDrawable(drawable);
    }

    public androidx.appcompat.widget.c getDataModel() {
        return this.f1108a.b();
    }

    ListPopupWindow getListPopupWindow() {
        if (this.f1120n == null) {
            ListPopupWindow listPopupWindow = new ListPopupWindow(getContext());
            this.f1120n = listPopupWindow;
            listPopupWindow.p(this.f1108a);
            this.f1120n.D(this);
            this.f1120n.J(true);
            this.f1120n.L(this.f1109b);
            this.f1120n.K(this.f1109b);
        }
        return this.f1120n;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f1108a.b();
        this.f1123w = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f1108a.b();
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.removeGlobalOnLayoutListener(this.f1119m);
        }
        if (b()) {
            a();
        }
        this.f1123w = false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        this.f1110c.layout(0, 0, i10 - i8, i11 - i9);
        if (b()) {
            return;
        }
        a();
    }

    @Override // android.view.View
    protected void onMeasure(int i8, int i9) {
        View view = this.f1110c;
        if (this.f1114g.getVisibility() != 0) {
            i9 = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i9), 1073741824);
        }
        measureChild(view, i8, i9);
        setMeasuredDimension(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    public void setActivityChooserModel(androidx.appcompat.widget.c cVar) {
        this.f1108a.f(cVar);
        if (b()) {
            a();
            c();
        }
    }

    public void setDefaultActionButtonContentDescription(int i8) {
        this.f1124x = i8;
    }

    public void setExpandActivityOverflowButtonContentDescription(int i8) {
        this.f1113f.setContentDescription(getContext().getString(i8));
    }

    public void setExpandActivityOverflowButtonDrawable(Drawable drawable) {
        this.f1113f.setImageDrawable(drawable);
    }

    public void setInitialActivityCount(int i8) {
        this.f1122t = i8;
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.f1121p = onDismissListener;
    }

    public void setProvider(androidx.core.view.b bVar) {
        this.f1117k = bVar;
    }
}
