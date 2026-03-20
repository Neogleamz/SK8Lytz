package com.google.android.material.transformation;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.c0;
import com.google.android.material.transformation.FabTransformationBehavior;
import java.util.HashMap;
import java.util.Map;
import k7.a;
import l7.h;
import l7.j;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class FabTransformationSheetBehavior extends FabTransformationBehavior {

    /* renamed from: i  reason: collision with root package name */
    private Map<View, Integer> f18769i;

    public FabTransformationSheetBehavior() {
    }

    public FabTransformationSheetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void g0(View view, boolean z4) {
        int intValue;
        ViewParent parent = view.getParent();
        if (parent instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) parent;
            int childCount = coordinatorLayout.getChildCount();
            if (Build.VERSION.SDK_INT >= 16 && z4) {
                this.f18769i = new HashMap(childCount);
            }
            for (int i8 = 0; i8 < childCount; i8++) {
                View childAt = coordinatorLayout.getChildAt(i8);
                boolean z8 = (childAt.getLayoutParams() instanceof CoordinatorLayout.e) && (((CoordinatorLayout.e) childAt.getLayoutParams()).f() instanceof FabTransformationScrimBehavior);
                if (childAt != view && !z8) {
                    if (!z4) {
                        Map<View, Integer> map = this.f18769i;
                        intValue = (map != null && map.containsKey(childAt)) ? this.f18769i.get(childAt).intValue() : 4;
                    } else if (Build.VERSION.SDK_INT >= 16) {
                        this.f18769i.put(childAt, Integer.valueOf(childAt.getImportantForAccessibility()));
                    }
                    c0.E0(childAt, intValue);
                }
            }
            if (z4) {
                return;
            }
            this.f18769i = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.transformation.ExpandableTransformationBehavior, com.google.android.material.transformation.ExpandableBehavior
    public boolean H(View view, View view2, boolean z4, boolean z8) {
        g0(view2, z4);
        return super.H(view, view2, z4, z8);
    }

    @Override // com.google.android.material.transformation.FabTransformationBehavior
    protected FabTransformationBehavior.e e0(Context context, boolean z4) {
        int i8 = z4 ? a.f21045l : a.f21044k;
        FabTransformationBehavior.e eVar = new FabTransformationBehavior.e();
        eVar.f18762a = h.d(context, i8);
        eVar.f18763b = new j(17, 0.0f, 0.0f);
        return eVar;
    }
}
