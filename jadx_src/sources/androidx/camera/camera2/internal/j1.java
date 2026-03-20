package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCharacteristics;
import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import androidx.camera.core.CameraUnavailableException;
import androidx.camera.core.InitializationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class j1 {
    private static String a(s.l0 l0Var, Integer num, List<String> list) {
        if (num != null && list.contains("0") && list.contains("1")) {
            if (num.intValue() == 1) {
                if (((Integer) l0Var.c("0").a(CameraCharacteristics.LENS_FACING)).intValue() == 1) {
                    return "1";
                }
                return null;
            } else if (num.intValue() == 0 && ((Integer) l0Var.c("1").a(CameraCharacteristics.LENS_FACING)).intValue() == 0) {
                return "0";
            } else {
                return null;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<String> b(v vVar, androidx.camera.core.t tVar) {
        String str;
        try {
            ArrayList arrayList = new ArrayList();
            List<String> asList = Arrays.asList(vVar.c().d());
            if (tVar == null) {
                for (String str2 : asList) {
                    arrayList.add(str2);
                }
                return arrayList;
            }
            try {
                str = a(vVar.c(), tVar.d(), asList);
            } catch (IllegalStateException unused) {
                str = null;
            }
            ArrayList arrayList2 = new ArrayList();
            for (String str3 : asList) {
                if (!str3.equals(str)) {
                    arrayList2.add(vVar.e(str3));
                }
            }
            Iterator<androidx.camera.core.s> it = tVar.b(arrayList2).iterator();
            while (it.hasNext()) {
                arrayList.add(((y.q) it.next()).c());
            }
            return arrayList;
        } catch (CameraAccessExceptionCompat e8) {
            throw new InitializationException(l1.a(e8));
        } catch (CameraUnavailableException e9) {
            throw new InitializationException(e9);
        }
    }
}
