package y7;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.view.d;
import k7.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    private static final int[] f24479a = {16842752, b.f21047a0};

    /* renamed from: b  reason: collision with root package name */
    private static final int[] f24480b = {b.I};

    private static int a(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, f24479a);
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        int resourceId2 = obtainStyledAttributes.getResourceId(1, 0);
        obtainStyledAttributes.recycle();
        return resourceId != 0 ? resourceId : resourceId2;
    }

    private static int b(Context context, AttributeSet attributeSet, int i8, int i9) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, f24480b, i8, i9);
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        obtainStyledAttributes.recycle();
        return resourceId;
    }

    public static Context c(Context context, AttributeSet attributeSet, int i8, int i9) {
        int b9 = b(context, attributeSet, i8, i9);
        boolean z4 = (context instanceof d) && ((d) context).c() == b9;
        if (b9 == 0 || z4) {
            return context;
        }
        d dVar = new d(context, b9);
        int a9 = a(context, attributeSet);
        if (a9 != 0) {
            dVar.getTheme().applyStyle(a9, true);
        }
        return dVar;
    }
}
