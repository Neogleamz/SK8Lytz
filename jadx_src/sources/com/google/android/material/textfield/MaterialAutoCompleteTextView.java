package com.google.android.material.textfield;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.Filterable;
import android.widget.ListAdapter;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.ListPopupWindow;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.material.internal.m;
import k7.k;
import k7.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MaterialAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    /* renamed from: e  reason: collision with root package name */
    private final ListPopupWindow f18549e;

    /* renamed from: f  reason: collision with root package name */
    private final AccessibilityManager f18550f;

    /* renamed from: g  reason: collision with root package name */
    private final Rect f18551g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements AdapterView.OnItemClickListener {
        a() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i8, long j8) {
            MaterialAutoCompleteTextView materialAutoCompleteTextView = MaterialAutoCompleteTextView.this;
            MaterialAutoCompleteTextView.this.f(i8 < 0 ? materialAutoCompleteTextView.f18549e.v() : materialAutoCompleteTextView.getAdapter().getItem(i8));
            AdapterView.OnItemClickListener onItemClickListener = MaterialAutoCompleteTextView.this.getOnItemClickListener();
            if (onItemClickListener != null) {
                if (view == null || i8 < 0) {
                    view = MaterialAutoCompleteTextView.this.f18549e.y();
                    i8 = MaterialAutoCompleteTextView.this.f18549e.x();
                    j8 = MaterialAutoCompleteTextView.this.f18549e.w();
                }
                onItemClickListener.onItemClick(MaterialAutoCompleteTextView.this.f18549e.m(), view, i8, j8);
            }
            MaterialAutoCompleteTextView.this.f18549e.dismiss();
        }
    }

    public MaterialAutoCompleteTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.f21048b);
    }

    public MaterialAutoCompleteTextView(Context context, AttributeSet attributeSet, int i8) {
        super(y7.a.c(context, attributeSet, i8, 0), attributeSet, i8);
        this.f18551g = new Rect();
        Context context2 = getContext();
        TypedArray h8 = m.h(context2, attributeSet, l.f21474y3, i8, k.f21236g, new int[0]);
        int i9 = l.f21483z3;
        if (h8.hasValue(i9) && h8.getInt(i9, 0) == 0) {
            setKeyListener(null);
        }
        this.f18550f = (AccessibilityManager) context2.getSystemService("accessibility");
        ListPopupWindow listPopupWindow = new ListPopupWindow(context2);
        this.f18549e = listPopupWindow;
        listPopupWindow.J(true);
        listPopupWindow.D(this);
        listPopupWindow.I(2);
        listPopupWindow.p(getAdapter());
        listPopupWindow.L(new a());
        h8.recycle();
    }

    private TextInputLayout d() {
        for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof TextInputLayout) {
                return (TextInputLayout) parent;
            }
        }
        return null;
    }

    private int e() {
        ListAdapter adapter = getAdapter();
        TextInputLayout d8 = d();
        int i8 = 0;
        if (adapter == null || d8 == null) {
            return 0;
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
        int min = Math.min(adapter.getCount(), Math.max(0, this.f18549e.x()) + 15);
        View view = null;
        int i9 = 0;
        for (int max = Math.max(0, min - 15); max < min; max++) {
            int itemViewType = adapter.getItemViewType(max);
            if (itemViewType != i8) {
                view = null;
                i8 = itemViewType;
            }
            view = adapter.getView(max, view, d8);
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
            }
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            i9 = Math.max(i9, view.getMeasuredWidth());
        }
        Drawable i10 = this.f18549e.i();
        if (i10 != null) {
            i10.getPadding(this.f18551g);
            Rect rect = this.f18551g;
            i9 += rect.left + rect.right;
        }
        return i9 + d8.getEndIconView().getMeasuredWidth();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public <T extends ListAdapter & Filterable> void f(Object obj) {
        if (Build.VERSION.SDK_INT >= 17) {
            setText(convertSelectionToString(obj), false);
            return;
        }
        ListAdapter adapter = getAdapter();
        setAdapter(null);
        setText(convertSelectionToString(obj));
        setAdapter(adapter);
    }

    @Override // android.widget.TextView
    public CharSequence getHint() {
        TextInputLayout d8 = d();
        return (d8 == null || !d8.O()) ? super.getHint() : d8.getHint();
    }

    @Override // android.widget.AutoCompleteTextView, android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        TextInputLayout d8 = d();
        if (d8 != null && d8.O() && super.getHint() == null && com.google.android.material.internal.d.c()) {
            setHint(BuildConfig.FLAVOR);
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i8, int i9) {
        super.onMeasure(i8, i9);
        if (View.MeasureSpec.getMode(i8) == Integer.MIN_VALUE) {
            setMeasuredDimension(Math.min(Math.max(getMeasuredWidth(), e()), View.MeasureSpec.getSize(i8)), getMeasuredHeight());
        }
    }

    @Override // android.widget.AutoCompleteTextView
    public <T extends ListAdapter & Filterable> void setAdapter(T t8) {
        super.setAdapter(t8);
        this.f18549e.p(getAdapter());
    }

    @Override // android.widget.AutoCompleteTextView
    public void showDropDown() {
        AccessibilityManager accessibilityManager = this.f18550f;
        if (accessibilityManager == null || !accessibilityManager.isTouchExplorationEnabled()) {
            super.showDropDown();
        } else {
            this.f18549e.a();
        }
    }
}
