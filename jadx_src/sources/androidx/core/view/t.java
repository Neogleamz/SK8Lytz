package androidx.core.view;

import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface t {
    boolean onNestedFling(View view, float f5, float f8, boolean z4);

    boolean onNestedPreFling(View view, float f5, float f8);

    void onNestedPreScroll(View view, int i8, int i9, int[] iArr);

    void onNestedScroll(View view, int i8, int i9, int i10, int i11);

    void onNestedScrollAccepted(View view, View view2, int i8);

    boolean onStartNestedScroll(View view, View view2, int i8);

    void onStopNestedScroll(View view);
}
