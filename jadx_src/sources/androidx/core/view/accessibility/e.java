package androidx.core.view.accessibility;

import android.os.Build;
import android.view.View;
import android.view.accessibility.AccessibilityRecord;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static int a(AccessibilityRecord accessibilityRecord) {
            return accessibilityRecord.getMaxScrollX();
        }

        static int b(AccessibilityRecord accessibilityRecord) {
            return accessibilityRecord.getMaxScrollY();
        }

        static void c(AccessibilityRecord accessibilityRecord, int i8) {
            accessibilityRecord.setMaxScrollX(i8);
        }

        static void d(AccessibilityRecord accessibilityRecord, int i8) {
            accessibilityRecord.setMaxScrollY(i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b {
        static void a(AccessibilityRecord accessibilityRecord, View view, int i8) {
            accessibilityRecord.setSource(view, i8);
        }
    }

    public static void a(AccessibilityRecord accessibilityRecord, int i8) {
        if (Build.VERSION.SDK_INT >= 15) {
            a.c(accessibilityRecord, i8);
        }
    }

    public static void b(AccessibilityRecord accessibilityRecord, int i8) {
        if (Build.VERSION.SDK_INT >= 15) {
            a.d(accessibilityRecord, i8);
        }
    }

    public static void c(AccessibilityRecord accessibilityRecord, View view, int i8) {
        if (Build.VERSION.SDK_INT >= 16) {
            b.a(accessibilityRecord, view, i8);
        }
    }
}
