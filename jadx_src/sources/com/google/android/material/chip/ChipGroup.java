package com.google.android.material.chip;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import androidx.core.view.accessibility.c;
import androidx.core.view.c0;
import com.google.android.material.internal.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import k7.k;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ChipGroup extends FlowLayout {

    /* renamed from: p  reason: collision with root package name */
    private static final int f17696p = k.f21250v;

    /* renamed from: e  reason: collision with root package name */
    private int f17697e;

    /* renamed from: f  reason: collision with root package name */
    private int f17698f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f17699g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f17700h;

    /* renamed from: j  reason: collision with root package name */
    private c f17701j;

    /* renamed from: k  reason: collision with root package name */
    private final b f17702k;

    /* renamed from: l  reason: collision with root package name */
    private d f17703l;

    /* renamed from: m  reason: collision with root package name */
    private int f17704m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f17705n;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public LayoutParams(int i8, int i9) {
            super(i8, i9);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class b implements CompoundButton.OnCheckedChangeListener {
        private b() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z4) {
            if (ChipGroup.this.f17705n) {
                return;
            }
            if (ChipGroup.this.getCheckedChipIds().isEmpty() && ChipGroup.this.f17700h) {
                ChipGroup.this.r(compoundButton.getId(), true);
                ChipGroup.this.q(compoundButton.getId(), false);
                return;
            }
            int id = compoundButton.getId();
            if (!z4) {
                if (ChipGroup.this.f17704m == id) {
                    ChipGroup.this.setCheckedId(-1);
                    return;
                }
                return;
            }
            if (ChipGroup.this.f17704m != -1 && ChipGroup.this.f17704m != id && ChipGroup.this.f17699g) {
                ChipGroup chipGroup = ChipGroup.this;
                chipGroup.r(chipGroup.f17704m, false);
            }
            ChipGroup.this.setCheckedId(id);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        void a(ChipGroup chipGroup, int i8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class d implements ViewGroup.OnHierarchyChangeListener {

        /* renamed from: a  reason: collision with root package name */
        private ViewGroup.OnHierarchyChangeListener f17707a;

        private d() {
        }

        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public void onChildViewAdded(View view, View view2) {
            if (view == ChipGroup.this && (view2 instanceof Chip)) {
                if (view2.getId() == -1) {
                    view2.setId(c0.m());
                }
                Chip chip = (Chip) view2;
                if (chip.isChecked()) {
                    ((ChipGroup) view).m(chip.getId());
                }
                chip.setOnCheckedChangeListenerInternal(ChipGroup.this.f17702k);
            }
            ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener = this.f17707a;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewAdded(view, view2);
            }
        }

        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public void onChildViewRemoved(View view, View view2) {
            if (view == ChipGroup.this && (view2 instanceof Chip)) {
                ((Chip) view2).setOnCheckedChangeListenerInternal(null);
            }
            ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener = this.f17707a;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewRemoved(view, view2);
            }
        }
    }

    public ChipGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.f21057i);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ChipGroup(android.content.Context r8, android.util.AttributeSet r9, int r10) {
        /*
            r7 = this;
            int r4 = com.google.android.material.chip.ChipGroup.f17696p
            android.content.Context r8 = y7.a.c(r8, r9, r10, r4)
            r7.<init>(r8, r9, r10)
            com.google.android.material.chip.ChipGroup$b r8 = new com.google.android.material.chip.ChipGroup$b
            r0 = 0
            r8.<init>()
            r7.f17702k = r8
            com.google.android.material.chip.ChipGroup$d r8 = new com.google.android.material.chip.ChipGroup$d
            r8.<init>()
            r7.f17703l = r8
            r8 = -1
            r7.f17704m = r8
            r6 = 0
            r7.f17705n = r6
            android.content.Context r0 = r7.getContext()
            int[] r2 = k7.l.f21418s1
            int[] r5 = new int[r6]
            r1 = r9
            r3 = r10
            android.content.res.TypedArray r9 = com.google.android.material.internal.m.h(r0, r1, r2, r3, r4, r5)
            int r10 = k7.l.f21436u1
            int r10 = r9.getDimensionPixelOffset(r10, r6)
            int r0 = k7.l.f21445v1
            int r0 = r9.getDimensionPixelOffset(r0, r10)
            r7.setChipSpacingHorizontal(r0)
            int r0 = k7.l.f21454w1
            int r10 = r9.getDimensionPixelOffset(r0, r10)
            r7.setChipSpacingVertical(r10)
            int r10 = k7.l.f21472y1
            boolean r10 = r9.getBoolean(r10, r6)
            r7.setSingleLine(r10)
            int r10 = k7.l.f21481z1
            boolean r10 = r9.getBoolean(r10, r6)
            r7.setSingleSelection(r10)
            int r10 = k7.l.f21463x1
            boolean r10 = r9.getBoolean(r10, r6)
            r7.setSelectionRequired(r10)
            int r10 = k7.l.f21427t1
            int r10 = r9.getResourceId(r10, r8)
            if (r10 == r8) goto L69
            r7.f17704m = r10
        L69:
            r9.recycle()
            com.google.android.material.chip.ChipGroup$d r8 = r7.f17703l
            super.setOnHierarchyChangeListener(r8)
            r8 = 1
            androidx.core.view.c0.E0(r7, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.chip.ChipGroup.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private int getChipCount() {
        int i8 = 0;
        for (int i9 = 0; i9 < getChildCount(); i9++) {
            if (getChildAt(i9) instanceof Chip) {
                i8++;
            }
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q(int i8, boolean z4) {
        this.f17704m = i8;
        c cVar = this.f17701j;
        if (cVar != null && this.f17699g && z4) {
            cVar.a(this, i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r(int i8, boolean z4) {
        View findViewById = findViewById(i8);
        if (findViewById instanceof Chip) {
            this.f17705n = true;
            ((Chip) findViewById).setChecked(z4);
            this.f17705n = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCheckedId(int i8) {
        q(i8, true);
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i8, ViewGroup.LayoutParams layoutParams) {
        if (view instanceof Chip) {
            Chip chip = (Chip) view;
            if (chip.isChecked()) {
                int i9 = this.f17704m;
                if (i9 != -1 && this.f17699g) {
                    r(i9, false);
                }
                setCheckedId(chip.getId());
            }
        }
        super.addView(view, i8, layoutParams);
    }

    @Override // com.google.android.material.internal.FlowLayout
    public boolean c() {
        return super.c();
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.checkLayoutParams(layoutParams) && (layoutParams instanceof LayoutParams);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public int getCheckedChipId() {
        if (this.f17699g) {
            return this.f17704m;
        }
        return -1;
    }

    public List<Integer> getCheckedChipIds() {
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < getChildCount(); i8++) {
            View childAt = getChildAt(i8);
            if ((childAt instanceof Chip) && ((Chip) childAt).isChecked()) {
                arrayList.add(Integer.valueOf(childAt.getId()));
                if (this.f17699g) {
                    return arrayList;
                }
            }
        }
        return arrayList;
    }

    public int getChipSpacingHorizontal() {
        return this.f17697e;
    }

    public int getChipSpacingVertical() {
        return this.f17698f;
    }

    public void m(int i8) {
        int i9 = this.f17704m;
        if (i8 == i9) {
            return;
        }
        if (i9 != -1 && this.f17699g) {
            r(i9, false);
        }
        if (i8 != -1) {
            r(i8, true);
        }
        setCheckedId(i8);
    }

    public void n() {
        this.f17705n = true;
        for (int i8 = 0; i8 < getChildCount(); i8++) {
            View childAt = getChildAt(i8);
            if (childAt instanceof Chip) {
                ((Chip) childAt).setChecked(false);
            }
        }
        this.f17705n = false;
        setCheckedId(-1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int o(View view) {
        if (view instanceof Chip) {
            int i8 = 0;
            for (int i9 = 0; i9 < getChildCount(); i9++) {
                if (getChildAt(i9) instanceof Chip) {
                    if (((Chip) getChildAt(i9)) == view) {
                        return i8;
                    }
                    i8++;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        int i8 = this.f17704m;
        if (i8 != -1) {
            r(i8, true);
            setCheckedId(this.f17704m);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        androidx.core.view.accessibility.c.I0(accessibilityNodeInfo).e0(c.b.b(getRowCount(), c() ? getChipCount() : -1, false, p() ? 1 : 2));
    }

    public boolean p() {
        return this.f17699g;
    }

    public void setChipSpacing(int i8) {
        setChipSpacingHorizontal(i8);
        setChipSpacingVertical(i8);
    }

    public void setChipSpacingHorizontal(int i8) {
        if (this.f17697e != i8) {
            this.f17697e = i8;
            setItemSpacing(i8);
            requestLayout();
        }
    }

    public void setChipSpacingHorizontalResource(int i8) {
        setChipSpacingHorizontal(getResources().getDimensionPixelOffset(i8));
    }

    public void setChipSpacingResource(int i8) {
        setChipSpacing(getResources().getDimensionPixelOffset(i8));
    }

    public void setChipSpacingVertical(int i8) {
        if (this.f17698f != i8) {
            this.f17698f = i8;
            setLineSpacing(i8);
            requestLayout();
        }
    }

    public void setChipSpacingVerticalResource(int i8) {
        setChipSpacingVertical(getResources().getDimensionPixelOffset(i8));
    }

    @Deprecated
    public void setDividerDrawableHorizontal(Drawable drawable) {
        throw new UnsupportedOperationException("Changing divider drawables have no effect. ChipGroup do not use divider drawables as spacing.");
    }

    @Deprecated
    public void setDividerDrawableVertical(Drawable drawable) {
        throw new UnsupportedOperationException("Changing divider drawables have no effect. ChipGroup do not use divider drawables as spacing.");
    }

    @Deprecated
    public void setFlexWrap(int i8) {
        throw new UnsupportedOperationException("Changing flex wrap not allowed. ChipGroup exposes a singleLine attribute instead.");
    }

    public void setOnCheckedChangeListener(c cVar) {
        this.f17701j = cVar;
    }

    @Override // android.view.ViewGroup
    public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener) {
        this.f17703l.f17707a = onHierarchyChangeListener;
    }

    public void setSelectionRequired(boolean z4) {
        this.f17700h = z4;
    }

    @Deprecated
    public void setShowDividerHorizontal(int i8) {
        throw new UnsupportedOperationException("Changing divider modes has no effect. ChipGroup do not use divider drawables as spacing.");
    }

    @Deprecated
    public void setShowDividerVertical(int i8) {
        throw new UnsupportedOperationException("Changing divider modes has no effect. ChipGroup do not use divider drawables as spacing.");
    }

    public void setSingleLine(int i8) {
        setSingleLine(getResources().getBoolean(i8));
    }

    @Override // com.google.android.material.internal.FlowLayout
    public void setSingleLine(boolean z4) {
        super.setSingleLine(z4);
    }

    public void setSingleSelection(int i8) {
        setSingleSelection(getResources().getBoolean(i8));
    }

    public void setSingleSelection(boolean z4) {
        if (this.f17699g != z4) {
            this.f17699g = z4;
            n();
        }
    }
}
