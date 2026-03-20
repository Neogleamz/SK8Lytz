package com.google.android.material.button;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import androidx.core.view.accessibility.c;
import androidx.core.view.c0;
import androidx.core.view.i;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.internal.s;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeMap;
import k7.k;
import x7.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MaterialButtonToggleGroup extends LinearLayout {

    /* renamed from: l  reason: collision with root package name */
    private static final String f17606l = MaterialButtonToggleGroup.class.getSimpleName();

    /* renamed from: m  reason: collision with root package name */
    private static final int f17607m = k.D;

    /* renamed from: a  reason: collision with root package name */
    private final List<d> f17608a;

    /* renamed from: b  reason: collision with root package name */
    private final c f17609b;

    /* renamed from: c  reason: collision with root package name */
    private final f f17610c;

    /* renamed from: d  reason: collision with root package name */
    private final LinkedHashSet<e> f17611d;

    /* renamed from: e  reason: collision with root package name */
    private final Comparator<MaterialButton> f17612e;

    /* renamed from: f  reason: collision with root package name */
    private Integer[] f17613f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f17614g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f17615h;

    /* renamed from: j  reason: collision with root package name */
    private boolean f17616j;

    /* renamed from: k  reason: collision with root package name */
    private int f17617k;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Comparator<MaterialButton> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(MaterialButton materialButton, MaterialButton materialButton2) {
            int compareTo = Boolean.valueOf(materialButton.isChecked()).compareTo(Boolean.valueOf(materialButton2.isChecked()));
            if (compareTo != 0) {
                return compareTo;
            }
            int compareTo2 = Boolean.valueOf(materialButton.isPressed()).compareTo(Boolean.valueOf(materialButton2.isPressed()));
            return compareTo2 != 0 ? compareTo2 : Integer.valueOf(MaterialButtonToggleGroup.this.indexOfChild(materialButton)).compareTo(Integer.valueOf(MaterialButtonToggleGroup.this.indexOfChild(materialButton2)));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends androidx.core.view.a {
        b() {
        }

        @Override // androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            super.g(view, cVar);
            cVar.f0(c.C0043c.a(0, 1, MaterialButtonToggleGroup.this.n(view), 1, false, ((MaterialButton) view).isChecked()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements MaterialButton.a {
        private c() {
        }

        /* synthetic */ c(MaterialButtonToggleGroup materialButtonToggleGroup, a aVar) {
            this();
        }

        @Override // com.google.android.material.button.MaterialButton.a
        public void a(MaterialButton materialButton, boolean z4) {
            if (MaterialButtonToggleGroup.this.f17614g) {
                return;
            }
            if (MaterialButtonToggleGroup.this.f17615h) {
                MaterialButtonToggleGroup.this.f17617k = z4 ? materialButton.getId() : -1;
            }
            if (MaterialButtonToggleGroup.this.u(materialButton.getId(), z4)) {
                MaterialButtonToggleGroup.this.l(materialButton.getId(), materialButton.isChecked());
            }
            MaterialButtonToggleGroup.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d {

        /* renamed from: e  reason: collision with root package name */
        private static final x7.c f17621e = new x7.a(0.0f);

        /* renamed from: a  reason: collision with root package name */
        x7.c f17622a;

        /* renamed from: b  reason: collision with root package name */
        x7.c f17623b;

        /* renamed from: c  reason: collision with root package name */
        x7.c f17624c;

        /* renamed from: d  reason: collision with root package name */
        x7.c f17625d;

        d(x7.c cVar, x7.c cVar2, x7.c cVar3, x7.c cVar4) {
            this.f17622a = cVar;
            this.f17623b = cVar3;
            this.f17624c = cVar4;
            this.f17625d = cVar2;
        }

        public static d a(d dVar) {
            x7.c cVar = f17621e;
            return new d(cVar, dVar.f17625d, cVar, dVar.f17624c);
        }

        public static d b(d dVar, View view) {
            return s.h(view) ? c(dVar) : d(dVar);
        }

        public static d c(d dVar) {
            x7.c cVar = dVar.f17622a;
            x7.c cVar2 = dVar.f17625d;
            x7.c cVar3 = f17621e;
            return new d(cVar, cVar2, cVar3, cVar3);
        }

        public static d d(d dVar) {
            x7.c cVar = f17621e;
            return new d(cVar, cVar, dVar.f17623b, dVar.f17624c);
        }

        public static d e(d dVar, View view) {
            return s.h(view) ? d(dVar) : c(dVar);
        }

        public static d f(d dVar) {
            x7.c cVar = dVar.f17622a;
            x7.c cVar2 = f17621e;
            return new d(cVar, cVar2, dVar.f17623b, cVar2);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface e {
        void a(MaterialButtonToggleGroup materialButtonToggleGroup, int i8, boolean z4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f implements MaterialButton.b {
        private f() {
        }

        /* synthetic */ f(MaterialButtonToggleGroup materialButtonToggleGroup, a aVar) {
            this();
        }

        @Override // com.google.android.material.button.MaterialButton.b
        public void a(MaterialButton materialButton, boolean z4) {
            MaterialButtonToggleGroup.this.invalidate();
        }
    }

    public MaterialButtonToggleGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.C);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MaterialButtonToggleGroup(android.content.Context r7, android.util.AttributeSet r8, int r9) {
        /*
            r6 = this;
            int r4 = com.google.android.material.button.MaterialButtonToggleGroup.f17607m
            android.content.Context r7 = y7.a.c(r7, r8, r9, r4)
            r6.<init>(r7, r8, r9)
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
            r6.f17608a = r7
            com.google.android.material.button.MaterialButtonToggleGroup$c r7 = new com.google.android.material.button.MaterialButtonToggleGroup$c
            r0 = 0
            r7.<init>(r6, r0)
            r6.f17609b = r7
            com.google.android.material.button.MaterialButtonToggleGroup$f r7 = new com.google.android.material.button.MaterialButtonToggleGroup$f
            r7.<init>(r6, r0)
            r6.f17610c = r7
            java.util.LinkedHashSet r7 = new java.util.LinkedHashSet
            r7.<init>()
            r6.f17611d = r7
            com.google.android.material.button.MaterialButtonToggleGroup$a r7 = new com.google.android.material.button.MaterialButtonToggleGroup$a
            r7.<init>()
            r6.f17612e = r7
            r7 = 0
            r6.f17614g = r7
            android.content.Context r0 = r6.getContext()
            int[] r2 = k7.l.U3
            int[] r5 = new int[r7]
            r1 = r8
            r3 = r9
            android.content.res.TypedArray r8 = com.google.android.material.internal.m.h(r0, r1, r2, r3, r4, r5)
            int r9 = k7.l.X3
            boolean r9 = r8.getBoolean(r9, r7)
            r6.setSingleSelection(r9)
            int r9 = k7.l.V3
            r0 = -1
            int r9 = r8.getResourceId(r9, r0)
            r6.f17617k = r9
            int r9 = k7.l.W3
            boolean r7 = r8.getBoolean(r9, r7)
            r6.f17616j = r7
            r7 = 1
            r6.setChildrenDrawingOrderEnabled(r7)
            r8.recycle()
            androidx.core.view.c0.E0(r6, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.button.MaterialButtonToggleGroup.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private int getFirstVisibleChildIndex() {
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            if (p(i8)) {
                return i8;
            }
        }
        return -1;
    }

    private int getLastVisibleChildIndex() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            if (p(childCount)) {
                return childCount;
            }
        }
        return -1;
    }

    private int getVisibleButtonCount() {
        int i8 = 0;
        for (int i9 = 0; i9 < getChildCount(); i9++) {
            if ((getChildAt(i9) instanceof MaterialButton) && p(i9)) {
                i8++;
            }
        }
        return i8;
    }

    private void h() {
        int firstVisibleChildIndex = getFirstVisibleChildIndex();
        if (firstVisibleChildIndex == -1) {
            return;
        }
        for (int i8 = firstVisibleChildIndex + 1; i8 < getChildCount(); i8++) {
            MaterialButton m8 = m(i8);
            int min = Math.min(m8.getStrokeWidth(), m(i8 - 1).getStrokeWidth());
            LinearLayout.LayoutParams i9 = i(m8);
            if (getOrientation() == 0) {
                i.c(i9, 0);
                i.d(i9, -min);
                i9.topMargin = 0;
            } else {
                i9.bottomMargin = 0;
                i9.topMargin = -min;
                i.d(i9, 0);
            }
            m8.setLayoutParams(i9);
        }
        r(firstVisibleChildIndex);
    }

    private LinearLayout.LayoutParams i(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        return layoutParams instanceof LinearLayout.LayoutParams ? (LinearLayout.LayoutParams) layoutParams : new LinearLayout.LayoutParams(layoutParams.width, layoutParams.height);
    }

    private void j(int i8, boolean z4) {
        MaterialButton materialButton = (MaterialButton) findViewById(i8);
        if (materialButton != null) {
            materialButton.setChecked(z4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l(int i8, boolean z4) {
        Iterator<e> it = this.f17611d.iterator();
        while (it.hasNext()) {
            it.next().a(this, i8, z4);
        }
    }

    private MaterialButton m(int i8) {
        return (MaterialButton) getChildAt(i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int n(View view) {
        if (view instanceof MaterialButton) {
            int i8 = 0;
            for (int i9 = 0; i9 < getChildCount(); i9++) {
                if (getChildAt(i9) == view) {
                    return i8;
                }
                if ((getChildAt(i9) instanceof MaterialButton) && p(i9)) {
                    i8++;
                }
            }
            return -1;
        }
        return -1;
    }

    private d o(int i8, int i9, int i10) {
        d dVar = this.f17608a.get(i8);
        if (i9 == i10) {
            return dVar;
        }
        boolean z4 = getOrientation() == 0;
        if (i8 == i9) {
            return z4 ? d.e(dVar, this) : d.f(dVar);
        } else if (i8 == i10) {
            return z4 ? d.b(dVar, this) : d.a(dVar);
        } else {
            return null;
        }
    }

    private boolean p(int i8) {
        return getChildAt(i8).getVisibility() != 8;
    }

    private void r(int i8) {
        if (getChildCount() == 0 || i8 == -1) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) m(i8).getLayoutParams();
        if (getOrientation() == 1) {
            layoutParams.topMargin = 0;
            layoutParams.bottomMargin = 0;
            return;
        }
        i.c(layoutParams, 0);
        i.d(layoutParams, 0);
        layoutParams.leftMargin = 0;
        layoutParams.rightMargin = 0;
    }

    private void s(int i8, boolean z4) {
        View findViewById = findViewById(i8);
        if (findViewById instanceof MaterialButton) {
            this.f17614g = true;
            ((MaterialButton) findViewById).setChecked(z4);
            this.f17614g = false;
        }
    }

    private void setCheckedId(int i8) {
        this.f17617k = i8;
        l(i8, true);
    }

    private void setGeneratedIdIfNeeded(MaterialButton materialButton) {
        if (materialButton.getId() == -1) {
            materialButton.setId(c0.m());
        }
    }

    private void setupButtonChild(MaterialButton materialButton) {
        materialButton.setMaxLines(1);
        materialButton.setEllipsize(TextUtils.TruncateAt.END);
        materialButton.setCheckable(true);
        materialButton.a(this.f17609b);
        materialButton.setOnPressedChangeListenerInternal(this.f17610c);
        materialButton.setShouldDrawSurfaceColorStroke(true);
    }

    private static void t(m.b bVar, d dVar) {
        if (dVar == null) {
            bVar.o(0.0f);
        } else {
            bVar.F(dVar.f17622a).w(dVar.f17625d).J(dVar.f17623b).A(dVar.f17624c);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean u(int i8, boolean z4) {
        List<Integer> checkedButtonIds = getCheckedButtonIds();
        if (this.f17616j && checkedButtonIds.isEmpty()) {
            s(i8, true);
            this.f17617k = i8;
            return false;
        }
        if (z4 && this.f17615h) {
            checkedButtonIds.remove(Integer.valueOf(i8));
            for (Integer num : checkedButtonIds) {
                int intValue = num.intValue();
                s(intValue, false);
                l(intValue, false);
            }
        }
        return true;
    }

    private void v() {
        TreeMap treeMap = new TreeMap(this.f17612e);
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            treeMap.put(m(i8), Integer.valueOf(i8));
        }
        this.f17613f = (Integer[]) treeMap.values().toArray(new Integer[0]);
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i8, ViewGroup.LayoutParams layoutParams) {
        if (!(view instanceof MaterialButton)) {
            Log.e(f17606l, "Child views must be of type MaterialButton.");
            return;
        }
        super.addView(view, i8, layoutParams);
        MaterialButton materialButton = (MaterialButton) view;
        setGeneratedIdIfNeeded(materialButton);
        setupButtonChild(materialButton);
        if (materialButton.isChecked()) {
            u(materialButton.getId(), true);
            setCheckedId(materialButton.getId());
        }
        m shapeAppearanceModel = materialButton.getShapeAppearanceModel();
        this.f17608a.add(new d(shapeAppearanceModel.r(), shapeAppearanceModel.j(), shapeAppearanceModel.t(), shapeAppearanceModel.l()));
        c0.t0(materialButton, new b());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        v();
        super.dispatchDraw(canvas);
    }

    public void g(e eVar) {
        this.f17611d.add(eVar);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return MaterialButtonToggleGroup.class.getName();
    }

    public int getCheckedButtonId() {
        if (this.f17615h) {
            return this.f17617k;
        }
        return -1;
    }

    public List<Integer> getCheckedButtonIds() {
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < getChildCount(); i8++) {
            MaterialButton m8 = m(i8);
            if (m8.isChecked()) {
                arrayList.add(Integer.valueOf(m8.getId()));
            }
        }
        return arrayList;
    }

    @Override // android.view.ViewGroup
    protected int getChildDrawingOrder(int i8, int i9) {
        Integer[] numArr = this.f17613f;
        if (numArr == null || i9 >= numArr.length) {
            Log.w(f17606l, "Child order wasn't updated");
            return i9;
        }
        return numArr[i9].intValue();
    }

    public void k() {
        this.f17614g = true;
        for (int i8 = 0; i8 < getChildCount(); i8++) {
            MaterialButton m8 = m(i8);
            m8.setChecked(false);
            l(m8.getId(), false);
        }
        this.f17614g = false;
        setCheckedId(-1);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        int i8 = this.f17617k;
        if (i8 != -1) {
            j(i8, true);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        androidx.core.view.accessibility.c.I0(accessibilityNodeInfo).e0(c.b.b(1, getVisibleButtonCount(), false, q() ? 1 : 2));
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i8, int i9) {
        w();
        h();
        super.onMeasure(i8, i9);
    }

    @Override // android.view.ViewGroup
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if (view instanceof MaterialButton) {
            MaterialButton materialButton = (MaterialButton) view;
            materialButton.h(this.f17609b);
            materialButton.setOnPressedChangeListenerInternal(null);
        }
        int indexOfChild = indexOfChild(view);
        if (indexOfChild >= 0) {
            this.f17608a.remove(indexOfChild);
        }
        w();
        h();
    }

    public boolean q() {
        return this.f17615h;
    }

    public void setSelectionRequired(boolean z4) {
        this.f17616j = z4;
    }

    public void setSingleSelection(int i8) {
        setSingleSelection(getResources().getBoolean(i8));
    }

    public void setSingleSelection(boolean z4) {
        if (this.f17615h != z4) {
            this.f17615h = z4;
            k();
        }
    }

    void w() {
        int childCount = getChildCount();
        int firstVisibleChildIndex = getFirstVisibleChildIndex();
        int lastVisibleChildIndex = getLastVisibleChildIndex();
        for (int i8 = 0; i8 < childCount; i8++) {
            MaterialButton m8 = m(i8);
            if (m8.getVisibility() != 8) {
                m.b v8 = m8.getShapeAppearanceModel().v();
                t(v8, o(i8, firstVisibleChildIndex, lastVisibleChildIndex));
                m8.setShapeAppearanceModel(v8.m());
            }
        }
    }
}
