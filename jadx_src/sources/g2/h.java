package g2;

import android.util.Log;
import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h {
    public static float a(float f5, float f8, float f9, float f10, float f11) {
        Log.e("countValueFromRangeMin", "minFrom=" + f5 + " maxFrom=" + f8 + " minTo=" + f9 + " maxTo=" + f10 + " orgValue=" + f11);
        float f12 = f8 - f5;
        if (f12 == 0.0f) {
            return f9;
        }
        float f13 = ((f10 - f9) * 1.0f * ((f11 - f5) / f12)) + f9;
        if (f13 <= f10) {
            f10 = f13;
        }
        return f10 < f9 ? f9 : f10;
    }

    public static String b(int i8) {
        StringBuffer stringBuffer = new StringBuffer(BuildConfig.FLAVOR);
        stringBuffer.append(String.valueOf(i8 & 255));
        stringBuffer.append(".");
        stringBuffer.append(String.valueOf((65535 & i8) >>> 8));
        stringBuffer.append(".");
        stringBuffer.append(String.valueOf((i8 & 16777215) >>> 16));
        stringBuffer.append(".");
        return stringBuffer.toString();
    }

    public static String c(int i8) {
        StringBuffer stringBuffer = new StringBuffer(BuildConfig.FLAVOR);
        stringBuffer.append(String.valueOf(i8 & 255));
        stringBuffer.append(".");
        stringBuffer.append(String.valueOf((65535 & i8) >>> 8));
        stringBuffer.append(".");
        stringBuffer.append(String.valueOf((16777215 & i8) >>> 16));
        stringBuffer.append(".");
        stringBuffer.append(String.valueOf(i8 >>> 24));
        return stringBuffer.toString();
    }
}
