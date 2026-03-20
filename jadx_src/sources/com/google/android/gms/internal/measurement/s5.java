package com.google.android.gms.internal.measurement;

import android.content.ContentResolver;
import android.database.Cursor;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s5 implements u5 {
    @Override // com.google.android.gms.internal.measurement.u5
    public final String a(ContentResolver contentResolver, String str) {
        Cursor query = contentResolver.query(m5.f12338a, null, null, new String[]{str}, null);
        try {
            if (query != null) {
                if (!query.moveToFirst()) {
                    query.close();
                    return null;
                }
                String string = query.getString(1);
                query.close();
                return string;
            }
            throw new zzgq("Failed to connect to GservicesProvider");
        } catch (Throwable th) {
            if (query != null) {
                try {
                    query.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    @Override // com.google.android.gms.internal.measurement.u5
    public final <T extends Map<String, String>> T b(ContentResolver contentResolver, String[] strArr, v5<T> v5Var) {
        Cursor query = contentResolver.query(m5.f12339b, null, null, strArr, null);
        try {
            if (query != null) {
                T c9 = v5Var.c(query.getCount());
                while (query.moveToNext()) {
                    c9.put(query.getString(0), query.getString(1));
                }
                query.close();
                return c9;
            }
            throw new zzgq("Failed to connect to GservicesProvider");
        } catch (Throwable th) {
            if (query != null) {
                try {
                    query.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }
}
