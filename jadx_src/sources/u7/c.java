package u7;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import androidx.appcompat.widget.j0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c {
    public static ColorStateList a(Context context, TypedArray typedArray, int i8) {
        int color;
        int resourceId;
        ColorStateList a9;
        return (!typedArray.hasValue(i8) || (resourceId = typedArray.getResourceId(i8, 0)) == 0 || (a9 = h.a.a(context, resourceId)) == null) ? (Build.VERSION.SDK_INT > 15 || (color = typedArray.getColor(i8, -1)) == -1) ? typedArray.getColorStateList(i8) : ColorStateList.valueOf(color) : a9;
    }

    public static ColorStateList b(Context context, j0 j0Var, int i8) {
        int b9;
        int n8;
        ColorStateList a9;
        return (!j0Var.s(i8) || (n8 = j0Var.n(i8, 0)) == 0 || (a9 = h.a.a(context, n8)) == null) ? (Build.VERSION.SDK_INT > 15 || (b9 = j0Var.b(i8, -1)) == -1) ? j0Var.c(i8) : ColorStateList.valueOf(b9) : a9;
    }

    public static int c(Context context, TypedArray typedArray, int i8, int i9) {
        TypedValue typedValue = new TypedValue();
        if (typedArray.getValue(i8, typedValue) && typedValue.type == 2) {
            TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(new int[]{typedValue.data});
            int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(0, i9);
            obtainStyledAttributes.recycle();
            return dimensionPixelSize;
        }
        return typedArray.getDimensionPixelSize(i8, i9);
    }

    public static Drawable d(Context context, TypedArray typedArray, int i8) {
        int resourceId;
        Drawable b9;
        return (!typedArray.hasValue(i8) || (resourceId = typedArray.getResourceId(i8, 0)) == 0 || (b9 = h.a.b(context, resourceId)) == null) ? typedArray.getDrawable(i8) : b9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int e(TypedArray typedArray, int i8, int i9) {
        return typedArray.hasValue(i8) ? i8 : i9;
    }

    public static d f(Context context, TypedArray typedArray, int i8) {
        int resourceId;
        if (!typedArray.hasValue(i8) || (resourceId = typedArray.getResourceId(i8, 0)) == 0) {
            return null;
        }
        return new d(context, resourceId);
    }

    public static boolean g(Context context) {
        return context.getResources().getConfiguration().fontScale >= 1.3f;
    }

    public static boolean h(Context context) {
        return context.getResources().getConfiguration().fontScale >= 2.0f;
    }
}
