package androidx.appcompat.app;

import java.util.LinkedHashSet;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class o {
    private static androidx.core.os.j a(androidx.core.os.j jVar, androidx.core.os.j jVar2) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        int i8 = 0;
        while (i8 < jVar.g() + jVar2.g()) {
            Locale d8 = i8 < jVar.g() ? jVar.d(i8) : jVar2.d(i8 - jVar.g());
            if (d8 != null) {
                linkedHashSet.add(d8);
            }
            i8++;
        }
        return androidx.core.os.j.a((Locale[]) linkedHashSet.toArray(new Locale[linkedHashSet.size()]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static androidx.core.os.j b(androidx.core.os.j jVar, androidx.core.os.j jVar2) {
        return (jVar == null || jVar.f()) ? androidx.core.os.j.e() : a(jVar, jVar2);
    }
}
