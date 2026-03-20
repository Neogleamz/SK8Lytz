package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import android.os.Parcel;
import android.os.SystemClock;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q4 extends v4 {

    /* renamed from: c  reason: collision with root package name */
    private final t4 f16894c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f16895d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q4(f6 f6Var) {
        super(f6Var);
        this.f16894c = new t4(this, zza(), "google_app_measurement_local.db");
    }

    /* JADX WARN: Removed duplicated region for block: B:76:0x011c  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0121  */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r2v11 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final boolean B(int r17, byte[] r18) {
        /*
            Method dump skipped, instructions count: 308
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.q4.B(int, byte[]):boolean");
    }

    private final SQLiteDatabase I() {
        if (this.f16895d) {
            return null;
        }
        SQLiteDatabase writableDatabase = this.f16894c.getWritableDatabase();
        if (writableDatabase == null) {
            this.f16895d = true;
            return null;
        }
        return writableDatabase;
    }

    private final boolean J() {
        return zza().getDatabasePath("google_app_measurement_local.db").exists();
    }

    private static long z(SQLiteDatabase sQLiteDatabase) {
        Cursor cursor = null;
        try {
            cursor = sQLiteDatabase.query("messages", new String[]{"rowid"}, "type=?", new String[]{"3"}, null, null, "rowid desc", "1");
            if (!cursor.moveToFirst()) {
                cursor.close();
                return -1L;
            }
            long j8 = cursor.getLong(0);
            cursor.close();
            return j8;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x01ab  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x01bb  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x01d6  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x01eb  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0193 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:170:0x01dc A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:171:0x01dc A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:173:0x01dc A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable> A(int r22) {
        /*
            Method dump skipped, instructions count: 509
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.q4.A(int):java.util.List");
    }

    public final boolean C(zzac zzacVar) {
        g();
        byte[] o02 = sb.o0(zzacVar);
        if (o02.length > 131072) {
            i().G().a("Conditional user property too long for local database. Sending directly to service");
            return false;
        }
        return B(2, o02);
    }

    public final boolean D(zzbf zzbfVar) {
        Parcel obtain = Parcel.obtain();
        zzbfVar.writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        if (marshall.length > 131072) {
            i().G().a("Event is too long for local database. Sending event directly to service");
            return false;
        }
        return B(0, marshall);
    }

    public final boolean E(zzno zznoVar) {
        Parcel obtain = Parcel.obtain();
        zznoVar.writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        if (marshall.length > 131072) {
            i().G().a("User property too long for local database. Sending directly to service");
            return false;
        }
        return B(1, marshall);
    }

    public final void F() {
        int delete;
        k();
        try {
            SQLiteDatabase I = I();
            if (I == null || (delete = I.delete("messages", null, null) + 0) <= 0) {
                return;
            }
            i().I().b("Reset local analytics data. records", Integer.valueOf(delete));
        } catch (SQLiteException e8) {
            i().E().b("Error resetting local analytics data. error", e8);
        }
    }

    public final boolean G() {
        return B(3, new byte[0]);
    }

    public final boolean H() {
        int i8;
        k();
        if (!this.f16895d && J()) {
            int i9 = 5;
            for (i8 = 0; i8 < 5; i8 = i8 + 1) {
                SQLiteDatabase sQLiteDatabase = null;
                try {
                    try {
                        SQLiteDatabase I = I();
                        if (I == null) {
                            this.f16895d = true;
                            if (I != null) {
                                I.close();
                            }
                            return false;
                        }
                        I.beginTransaction();
                        I.delete("messages", "type == ?", new String[]{Integer.toString(3)});
                        I.setTransactionSuccessful();
                        I.endTransaction();
                        I.close();
                        return true;
                    } catch (SQLiteDatabaseLockedException unused) {
                        SystemClock.sleep(i9);
                        i9 += 20;
                        i8 = 0 == 0 ? i8 + 1 : 0;
                        sQLiteDatabase.close();
                    }
                } catch (SQLiteFullException e8) {
                    i().E().b("Error deleting app launch break from local database", e8);
                    this.f16895d = true;
                    if (0 == 0) {
                    }
                    sQLiteDatabase.close();
                } catch (SQLiteException e9) {
                    if (0 != 0) {
                        try {
                            if (sQLiteDatabase.inTransaction()) {
                                sQLiteDatabase.endTransaction();
                            }
                        } catch (Throwable th) {
                            if (0 != 0) {
                                sQLiteDatabase.close();
                            }
                            throw th;
                        }
                    }
                    i().E().b("Error deleting app launch break from local database", e9);
                    this.f16895d = true;
                    if (0 != 0) {
                        sQLiteDatabase.close();
                    }
                }
            }
            i().J().a("Error deleting app launch break from local database in reasonable time");
            return false;
        }
        return false;
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ e a() {
        return super.a();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ d b() {
        return super.b();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ x c() {
        return super.c();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ s4 e() {
        return super.e();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ h5 f() {
        return super.f();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ sb g() {
        return super.g();
    }

    @Override // com.google.android.gms.measurement.internal.w1, com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void h() {
        super.h();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ x4 i() {
        return super.i();
    }

    @Override // com.google.android.gms.measurement.internal.w1, com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void j() {
        super.j();
    }

    @Override // com.google.android.gms.measurement.internal.w1, com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void k() {
        super.k();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ a6 l() {
        return super.l();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ a m() {
        return super.m();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ r4 n() {
        return super.n();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ q4 o() {
        return super.o();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ h7 p() {
        return super.p();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ z8 q() {
        return super.q();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ f9 r() {
        return super.r();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ na s() {
        return super.s();
    }

    @Override // com.google.android.gms.measurement.internal.v4
    protected final boolean y() {
        return false;
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ Context zza() {
        return super.zza();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ u6.d zzb() {
        return super.zzb();
    }
}
