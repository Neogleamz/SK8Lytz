package v7;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.util.StateSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b {

    /* renamed from: a  reason: collision with root package name */
    public static final boolean f23352a;

    /* renamed from: b  reason: collision with root package name */
    private static final int[] f23353b;

    /* renamed from: c  reason: collision with root package name */
    private static final int[] f23354c;

    /* renamed from: d  reason: collision with root package name */
    private static final int[] f23355d;

    /* renamed from: e  reason: collision with root package name */
    private static final int[] f23356e;

    /* renamed from: f  reason: collision with root package name */
    private static final int[] f23357f;

    /* renamed from: g  reason: collision with root package name */
    private static final int[] f23358g;

    /* renamed from: h  reason: collision with root package name */
    private static final int[] f23359h;

    /* renamed from: i  reason: collision with root package name */
    private static final int[] f23360i;

    /* renamed from: j  reason: collision with root package name */
    private static final int[] f23361j;

    /* renamed from: k  reason: collision with root package name */
    private static final int[] f23362k;

    /* renamed from: l  reason: collision with root package name */
    static final String f23363l;

    static {
        f23352a = Build.VERSION.SDK_INT >= 21;
        f23353b = new int[]{16842919};
        f23354c = new int[]{16843623, 16842908};
        f23355d = new int[]{16842908};
        f23356e = new int[]{16843623};
        f23357f = new int[]{16842913, 16842919};
        f23358g = new int[]{16842913, 16843623, 16842908};
        f23359h = new int[]{16842913, 16842908};
        f23360i = new int[]{16842913, 16843623};
        f23361j = new int[]{16842913};
        f23362k = new int[]{16842910, 16842919};
        f23363l = b.class.getSimpleName();
    }

    private b() {
    }

    public static ColorStateList a(ColorStateList colorStateList) {
        if (f23352a) {
            return new ColorStateList(new int[][]{f23361j, StateSet.NOTHING}, new int[]{c(colorStateList, f23357f), c(colorStateList, f23353b)});
        }
        int[] iArr = f23357f;
        int[] iArr2 = f23358g;
        int[] iArr3 = f23359h;
        int[] iArr4 = f23360i;
        int[] iArr5 = f23353b;
        int[] iArr6 = f23354c;
        int[] iArr7 = f23355d;
        int[] iArr8 = f23356e;
        return new ColorStateList(new int[][]{iArr, iArr2, iArr3, iArr4, f23361j, iArr5, iArr6, iArr7, iArr8, StateSet.NOTHING}, new int[]{c(colorStateList, iArr), c(colorStateList, iArr2), c(colorStateList, iArr3), c(colorStateList, iArr4), 0, c(colorStateList, iArr5), c(colorStateList, iArr6), c(colorStateList, iArr7), c(colorStateList, iArr8), 0});
    }

    @TargetApi(21)
    private static int b(int i8) {
        return androidx.core.graphics.b.p(i8, Math.min(Color.alpha(i8) * 2, 255));
    }

    private static int c(ColorStateList colorStateList, int[] iArr) {
        int colorForState = colorStateList != null ? colorStateList.getColorForState(iArr, colorStateList.getDefaultColor()) : 0;
        return f23352a ? b(colorForState) : colorForState;
    }

    public static ColorStateList d(ColorStateList colorStateList) {
        if (colorStateList != null) {
            int i8 = Build.VERSION.SDK_INT;
            if (i8 >= 22 && i8 <= 27 && Color.alpha(colorStateList.getDefaultColor()) == 0 && Color.alpha(colorStateList.getColorForState(f23362k, 0)) != 0) {
                Log.w(f23363l, "Use a non-transparent color for the default color as it will be used to finish ripple animations.");
            }
            return colorStateList;
        }
        return ColorStateList.valueOf(0);
    }

    public static boolean e(int[] iArr) {
        boolean z4 = false;
        boolean z8 = false;
        for (int i8 : iArr) {
            if (i8 == 16842910) {
                z4 = true;
            } else if (i8 == 16842908 || i8 == 16842919 || i8 == 16843623) {
                z8 = true;
            }
        }
        return z4 && z8;
    }
}
