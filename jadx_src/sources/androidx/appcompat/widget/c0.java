package androidx.appcompat.widget;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.LinearLayoutCompat;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c0 extends HorizontalScrollView implements AdapterView.OnItemSelectedListener {

    /* renamed from: k  reason: collision with root package name */
    private static final Interpolator f1422k = new DecelerateInterpolator();

    /* renamed from: a  reason: collision with root package name */
    Runnable f1423a;

    /* renamed from: b  reason: collision with root package name */
    private c f1424b;

    /* renamed from: c  reason: collision with root package name */
    LinearLayoutCompat f1425c;

    /* renamed from: d  reason: collision with root package name */
    private Spinner f1426d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f1427e;

    /* renamed from: f  reason: collision with root package name */
    int f1428f;

    /* renamed from: g  reason: collision with root package name */
    int f1429g;

    /* renamed from: h  reason: collision with root package name */
    private int f1430h;

    /* renamed from: j  reason: collision with root package name */
    private int f1431j;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f1432a;

        a(View view) {
            this.f1432a = view;
        }

        @Override // java.lang.Runnable
        public void run() {
            c0.this.smoothScrollTo(this.f1432a.getLeft() - ((c0.this.getWidth() - this.f1432a.getWidth()) / 2), 0);
            c0.this.f1423a = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends BaseAdapter {
        b() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return c0.this.f1425c.getChildCount();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i8) {
            return ((d) c0.this.f1425c.getChildAt(i8)).b();
        }

        @Override // android.widget.Adapter
        public long getItemId(int i8) {
            return i8;
        }

        @Override // android.widget.Adapter
        public View getView(int i8, View view, ViewGroup viewGroup) {
            if (view == null) {
                return c0.this.c((ActionBar.b) getItem(i8), true);
            }
            ((d) view).a((ActionBar.b) getItem(i8));
            return view;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements View.OnClickListener {
        c() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ((d) view).b().e();
            int childCount = c0.this.f1425c.getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                View childAt = c0.this.f1425c.getChildAt(i8);
                childAt.setSelected(childAt == view);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends LinearLayout {

        /* renamed from: a  reason: collision with root package name */
        private final int[] f1436a;

        /* renamed from: b  reason: collision with root package name */
        private ActionBar.b f1437b;

        /* renamed from: c  reason: collision with root package name */
        private TextView f1438c;

        /* renamed from: d  reason: collision with root package name */
        private ImageView f1439d;

        /* renamed from: e  reason: collision with root package name */
        private View f1440e;

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public d(android.content.Context r6, androidx.appcompat.app.ActionBar.b r7, boolean r8) {
            /*
                r4 = this;
                androidx.appcompat.widget.c0.this = r5
                int r5 = g.a.f19865d
                r0 = 0
                r4.<init>(r6, r0, r5)
                r1 = 1
                int[] r1 = new int[r1]
                r2 = 16842964(0x10100d4, float:2.3694152E-38)
                r3 = 0
                r1[r3] = r2
                r4.f1436a = r1
                r4.f1437b = r7
                androidx.appcompat.widget.j0 r5 = androidx.appcompat.widget.j0.v(r6, r0, r1, r5, r3)
                boolean r6 = r5.s(r3)
                if (r6 == 0) goto L26
                android.graphics.drawable.Drawable r6 = r5.g(r3)
                r4.setBackgroundDrawable(r6)
            L26:
                r5.w()
                if (r8 == 0) goto L31
                r5 = 8388627(0x800013, float:1.175497E-38)
                r4.setGravity(r5)
            L31:
                r4.c()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.c0.d.<init>(androidx.appcompat.widget.c0, android.content.Context, androidx.appcompat.app.ActionBar$b, boolean):void");
        }

        public void a(ActionBar.b bVar) {
            this.f1437b = bVar;
            c();
        }

        public ActionBar.b b() {
            return this.f1437b;
        }

        public void c() {
            ActionBar.b bVar = this.f1437b;
            View b9 = bVar.b();
            if (b9 != null) {
                ViewParent parent = b9.getParent();
                if (parent != this) {
                    if (parent != null) {
                        ((ViewGroup) parent).removeView(b9);
                    }
                    addView(b9);
                }
                this.f1440e = b9;
                TextView textView = this.f1438c;
                if (textView != null) {
                    textView.setVisibility(8);
                }
                ImageView imageView = this.f1439d;
                if (imageView != null) {
                    imageView.setVisibility(8);
                    this.f1439d.setImageDrawable(null);
                    return;
                }
                return;
            }
            View view = this.f1440e;
            if (view != null) {
                removeView(view);
                this.f1440e = null;
            }
            Drawable c9 = bVar.c();
            CharSequence d8 = bVar.d();
            if (c9 != null) {
                if (this.f1439d == null) {
                    AppCompatImageView appCompatImageView = new AppCompatImageView(getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                    layoutParams.gravity = 16;
                    appCompatImageView.setLayoutParams(layoutParams);
                    addView(appCompatImageView, 0);
                    this.f1439d = appCompatImageView;
                }
                this.f1439d.setImageDrawable(c9);
                this.f1439d.setVisibility(0);
            } else {
                ImageView imageView2 = this.f1439d;
                if (imageView2 != null) {
                    imageView2.setVisibility(8);
                    this.f1439d.setImageDrawable(null);
                }
            }
            boolean z4 = !TextUtils.isEmpty(d8);
            if (z4) {
                if (this.f1438c == null) {
                    AppCompatTextView appCompatTextView = new AppCompatTextView(getContext(), null, g.a.f19866e);
                    appCompatTextView.setEllipsize(TextUtils.TruncateAt.END);
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
                    layoutParams2.gravity = 16;
                    appCompatTextView.setLayoutParams(layoutParams2);
                    addView(appCompatTextView);
                    this.f1438c = appCompatTextView;
                }
                this.f1438c.setText(d8);
                this.f1438c.setVisibility(0);
            } else {
                TextView textView2 = this.f1438c;
                if (textView2 != null) {
                    textView2.setVisibility(8);
                    this.f1438c.setText((CharSequence) null);
                }
            }
            ImageView imageView3 = this.f1439d;
            if (imageView3 != null) {
                imageView3.setContentDescription(bVar.a());
            }
            o0.a(this, z4 ? null : bVar.a());
        }

        @Override // android.view.View
        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName("androidx.appcompat.app.ActionBar$Tab");
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName("androidx.appcompat.app.ActionBar$Tab");
        }

        @Override // android.widget.LinearLayout, android.view.View
        public void onMeasure(int i8, int i9) {
            super.onMeasure(i8, i9);
            if (c0.this.f1428f > 0) {
                int measuredWidth = getMeasuredWidth();
                int i10 = c0.this.f1428f;
                if (measuredWidth > i10) {
                    super.onMeasure(View.MeasureSpec.makeMeasureSpec(i10, 1073741824), i9);
                }
            }
        }

        @Override // android.view.View
        public void setSelected(boolean z4) {
            boolean z8 = isSelected() != z4;
            super.setSelected(z4);
            if (z8 && z4) {
                sendAccessibilityEvent(4);
            }
        }
    }

    private Spinner b() {
        AppCompatSpinner appCompatSpinner = new AppCompatSpinner(getContext(), null, g.a.f19869h);
        appCompatSpinner.setLayoutParams(new LinearLayoutCompat.LayoutParams(-2, -1));
        appCompatSpinner.setOnItemSelectedListener(this);
        return appCompatSpinner;
    }

    private boolean d() {
        Spinner spinner = this.f1426d;
        return spinner != null && spinner.getParent() == this;
    }

    private void e() {
        if (d()) {
            return;
        }
        if (this.f1426d == null) {
            this.f1426d = b();
        }
        removeView(this.f1425c);
        addView(this.f1426d, new ViewGroup.LayoutParams(-2, -1));
        if (this.f1426d.getAdapter() == null) {
            this.f1426d.setAdapter((SpinnerAdapter) new b());
        }
        Runnable runnable = this.f1423a;
        if (runnable != null) {
            removeCallbacks(runnable);
            this.f1423a = null;
        }
        this.f1426d.setSelection(this.f1431j);
    }

    private boolean f() {
        if (d()) {
            removeView(this.f1426d);
            addView(this.f1425c, new ViewGroup.LayoutParams(-2, -1));
            setTabSelected(this.f1426d.getSelectedItemPosition());
            return false;
        }
        return false;
    }

    public void a(int i8) {
        View childAt = this.f1425c.getChildAt(i8);
        Runnable runnable = this.f1423a;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
        a aVar = new a(childAt);
        this.f1423a = aVar;
        post(aVar);
    }

    d c(ActionBar.b bVar, boolean z4) {
        d dVar = new d(getContext(), bVar, z4);
        if (z4) {
            dVar.setBackgroundDrawable(null);
            dVar.setLayoutParams(new AbsListView.LayoutParams(-1, this.f1430h));
        } else {
            dVar.setFocusable(true);
            if (this.f1424b == null) {
                this.f1424b = new c();
            }
            dVar.setOnClickListener(this.f1424b);
        }
        return dVar;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Runnable runnable = this.f1423a;
        if (runnable != null) {
            post(runnable);
        }
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        androidx.appcompat.view.a b9 = androidx.appcompat.view.a.b(getContext());
        setContentHeight(b9.f());
        this.f1429g = b9.e();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Runnable runnable = this.f1423a;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> adapterView, View view, int i8, long j8) {
        ((d) view).b().e();
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i8, int i9) {
        int i10;
        int mode = View.MeasureSpec.getMode(i8);
        boolean z4 = true;
        boolean z8 = mode == 1073741824;
        setFillViewport(z8);
        int childCount = this.f1425c.getChildCount();
        if (childCount <= 1 || !(mode == 1073741824 || mode == Integer.MIN_VALUE)) {
            i10 = -1;
        } else {
            if (childCount > 2) {
                this.f1428f = (int) (View.MeasureSpec.getSize(i8) * 0.4f);
            } else {
                this.f1428f = View.MeasureSpec.getSize(i8) / 2;
            }
            i10 = Math.min(this.f1428f, this.f1429g);
        }
        this.f1428f = i10;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.f1430h, 1073741824);
        if (z8 || !this.f1427e) {
            z4 = false;
        }
        if (z4) {
            this.f1425c.measure(0, makeMeasureSpec);
            if (this.f1425c.getMeasuredWidth() > View.MeasureSpec.getSize(i8)) {
                e();
                int measuredWidth = getMeasuredWidth();
                super.onMeasure(i8, makeMeasureSpec);
                int measuredWidth2 = getMeasuredWidth();
                if (z8 || measuredWidth == measuredWidth2) {
                }
                setTabSelected(this.f1431j);
                return;
            }
        }
        f();
        int measuredWidth3 = getMeasuredWidth();
        super.onMeasure(i8, makeMeasureSpec);
        int measuredWidth22 = getMeasuredWidth();
        if (z8) {
        }
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void setAllowCollapse(boolean z4) {
        this.f1427e = z4;
    }

    public void setContentHeight(int i8) {
        this.f1430h = i8;
        requestLayout();
    }

    public void setTabSelected(int i8) {
        this.f1431j = i8;
        int childCount = this.f1425c.getChildCount();
        int i9 = 0;
        while (i9 < childCount) {
            View childAt = this.f1425c.getChildAt(i9);
            boolean z4 = i9 == i8;
            childAt.setSelected(z4);
            if (z4) {
                a(i8);
            }
            i9++;
        }
        Spinner spinner = this.f1426d;
        if (spinner == null || i8 < 0) {
            return;
        }
        spinner.setSelection(i8);
    }
}
