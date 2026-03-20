package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {
    public static String a() {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        return ".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ")";
    }

    public static String b(Context context, int i8) {
        if (i8 != -1) {
            try {
                return context.getResources().getResourceEntryName(i8);
            } catch (Exception unused) {
                return "?" + i8;
            }
        }
        return "UNKNOWN";
    }

    public static String c(View view) {
        try {
            return view.getContext().getResources().getResourceEntryName(view.getId());
        } catch (Exception unused) {
            return "UNKNOWN";
        }
    }

    public static String d(MotionLayout motionLayout, int i8) {
        return i8 == -1 ? "UNDEFINED" : motionLayout.getContext().getResources().getResourceEntryName(i8);
    }

    public static void e(String str, String str2, int i8) {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        int min = Math.min(i8, stackTrace.length - 1);
        String str3 = " ";
        for (int i9 = 1; i9 <= min; i9++) {
            StackTraceElement stackTraceElement = stackTrace[i9];
            str3 = str3 + " ";
            Log.v(str, str2 + str3 + (".(" + stackTrace[i9].getFileName() + ":" + stackTrace[i9].getLineNumber() + ") " + stackTrace[i9].getMethodName()) + str3);
        }
    }
}
