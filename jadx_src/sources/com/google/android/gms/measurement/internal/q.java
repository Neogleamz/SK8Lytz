package com.google.android.gms.measurement.internal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q {
    private static Set<String> a(SQLiteDatabase sQLiteDatabase, String str) {
        HashSet hashSet = new HashSet();
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM " + str + " LIMIT 0", null);
        try {
            Collections.addAll(hashSet, rawQuery.getColumnNames());
            return hashSet;
        } finally {
            rawQuery.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(x4 x4Var, SQLiteDatabase sQLiteDatabase) {
        if (x4Var == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        File file = new File(com.google.android.gms.internal.measurement.i1.a().h(sQLiteDatabase.getPath()));
        if (!file.setReadable(false, false)) {
            x4Var.J().a("Failed to turn off database read permission");
        }
        if (!file.setWritable(false, false)) {
            x4Var.J().a("Failed to turn off database write permission");
        }
        if (!file.setReadable(true, true)) {
            x4Var.J().a("Failed to turn on database read permission for owner");
        }
        if (file.setWritable(true, true)) {
            return;
        }
        x4Var.J().a("Failed to turn on database write permission for owner");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void c(x4 x4Var, SQLiteDatabase sQLiteDatabase, String str, String str2, String str3, String[] strArr) {
        String[] split;
        if (x4Var == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        if (!d(x4Var, sQLiteDatabase, str)) {
            sQLiteDatabase.execSQL(str2);
        }
        try {
            Set<String> a9 = a(sQLiteDatabase, str);
            for (String str4 : str3.split(",")) {
                if (!a9.remove(str4)) {
                    throw new SQLiteException("Table " + str + " is missing required column: " + str4);
                }
            }
            if (strArr != null) {
                for (int i8 = 0; i8 < strArr.length; i8 += 2) {
                    if (!a9.remove(strArr[i8])) {
                        sQLiteDatabase.execSQL(strArr[i8 + 1]);
                    }
                }
            }
            if (a9.isEmpty()) {
                return;
            }
            x4Var.J().c("Table has extra columns. table, columns", str, TextUtils.join(", ", a9));
        } catch (SQLiteException e8) {
            x4Var.E().b("Failed to verify columns on table that was just created", str);
            throw e8;
        }
    }

    private static boolean d(x4 x4Var, SQLiteDatabase sQLiteDatabase, String str) {
        if (x4Var != null) {
            Cursor cursor = null;
            try {
                try {
                    cursor = sQLiteDatabase.query("SQLITE_MASTER", new String[]{"name"}, "name=?", new String[]{str}, null, null, null);
                    boolean moveToFirst = cursor.moveToFirst();
                    cursor.close();
                    return moveToFirst;
                } catch (SQLiteException e8) {
                    x4Var.J().c("Error querying for table", str, e8);
                    if (cursor != null) {
                        cursor.close();
                    }
                    return false;
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }
        throw new IllegalArgumentException("Monitor must not be null");
    }
}
