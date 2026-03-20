package com.google.android.gms.measurement.internal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.TextUtils;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.gd;
import com.google.android.gms.internal.measurement.md;
import com.google.android.gms.internal.measurement.nd;
import com.google.android.gms.internal.measurement.t3;
import com.google.android.gms.internal.measurement.uc;
import com.google.android.gms.internal.measurement.v3;
import com.google.android.gms.internal.measurement.w4;
import com.google.android.gms.internal.measurement.wf;
import com.google.android.gms.internal.measurement.ye;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l extends bb {

    /* renamed from: f  reason: collision with root package name */
    private static final String[] f16738f = {"last_bundled_timestamp", "ALTER TABLE events ADD COLUMN last_bundled_timestamp INTEGER;", "last_bundled_day", "ALTER TABLE events ADD COLUMN last_bundled_day INTEGER;", "last_sampled_complex_event_id", "ALTER TABLE events ADD COLUMN last_sampled_complex_event_id INTEGER;", "last_sampling_rate", "ALTER TABLE events ADD COLUMN last_sampling_rate INTEGER;", "last_exempt_from_sampling", "ALTER TABLE events ADD COLUMN last_exempt_from_sampling INTEGER;", "current_session_count", "ALTER TABLE events ADD COLUMN current_session_count INTEGER;"};

    /* renamed from: g  reason: collision with root package name */
    private static final String[] f16739g = {"origin", "ALTER TABLE user_attributes ADD COLUMN origin TEXT;"};

    /* renamed from: h  reason: collision with root package name */
    private static final String[] f16740h = {"app_version", "ALTER TABLE apps ADD COLUMN app_version TEXT;", "app_store", "ALTER TABLE apps ADD COLUMN app_store TEXT;", "gmp_version", "ALTER TABLE apps ADD COLUMN gmp_version INTEGER;", "dev_cert_hash", "ALTER TABLE apps ADD COLUMN dev_cert_hash INTEGER;", "measurement_enabled", "ALTER TABLE apps ADD COLUMN measurement_enabled INTEGER;", "last_bundle_start_timestamp", "ALTER TABLE apps ADD COLUMN last_bundle_start_timestamp INTEGER;", "day", "ALTER TABLE apps ADD COLUMN day INTEGER;", "daily_public_events_count", "ALTER TABLE apps ADD COLUMN daily_public_events_count INTEGER;", "daily_events_count", "ALTER TABLE apps ADD COLUMN daily_events_count INTEGER;", "daily_conversions_count", "ALTER TABLE apps ADD COLUMN daily_conversions_count INTEGER;", "remote_config", "ALTER TABLE apps ADD COLUMN remote_config BLOB;", "config_fetched_time", "ALTER TABLE apps ADD COLUMN config_fetched_time INTEGER;", "failed_config_fetch_time", "ALTER TABLE apps ADD COLUMN failed_config_fetch_time INTEGER;", "app_version_int", "ALTER TABLE apps ADD COLUMN app_version_int INTEGER;", "firebase_instance_id", "ALTER TABLE apps ADD COLUMN firebase_instance_id TEXT;", "daily_error_events_count", "ALTER TABLE apps ADD COLUMN daily_error_events_count INTEGER;", "daily_realtime_events_count", "ALTER TABLE apps ADD COLUMN daily_realtime_events_count INTEGER;", "health_monitor_sample", "ALTER TABLE apps ADD COLUMN health_monitor_sample TEXT;", "android_id", "ALTER TABLE apps ADD COLUMN android_id INTEGER;", "adid_reporting_enabled", "ALTER TABLE apps ADD COLUMN adid_reporting_enabled INTEGER;", "ssaid_reporting_enabled", "ALTER TABLE apps ADD COLUMN ssaid_reporting_enabled INTEGER;", "admob_app_id", "ALTER TABLE apps ADD COLUMN admob_app_id TEXT;", "linked_admob_app_id", "ALTER TABLE apps ADD COLUMN linked_admob_app_id TEXT;", "dynamite_version", "ALTER TABLE apps ADD COLUMN dynamite_version INTEGER;", "safelisted_events", "ALTER TABLE apps ADD COLUMN safelisted_events TEXT;", "ga_app_id", "ALTER TABLE apps ADD COLUMN ga_app_id TEXT;", "config_last_modified_time", "ALTER TABLE apps ADD COLUMN config_last_modified_time TEXT;", "e_tag", "ALTER TABLE apps ADD COLUMN e_tag TEXT;", "session_stitching_token", "ALTER TABLE apps ADD COLUMN session_stitching_token TEXT;", "sgtm_upload_enabled", "ALTER TABLE apps ADD COLUMN sgtm_upload_enabled INTEGER;", "target_os_version", "ALTER TABLE apps ADD COLUMN target_os_version INTEGER;", "session_stitching_token_hash", "ALTER TABLE apps ADD COLUMN session_stitching_token_hash INTEGER;", "ad_services_version", "ALTER TABLE apps ADD COLUMN ad_services_version INTEGER;", "unmatched_first_open_without_ad_id", "ALTER TABLE apps ADD COLUMN unmatched_first_open_without_ad_id INTEGER;", "npa_metadata_value", "ALTER TABLE apps ADD COLUMN npa_metadata_value INTEGER;", "attribution_eligibility_status", "ALTER TABLE apps ADD COLUMN attribution_eligibility_status INTEGER;", "sgtm_preview_key", "ALTER TABLE apps ADD COLUMN sgtm_preview_key TEXT;", "dma_consent_state", "ALTER TABLE apps ADD COLUMN dma_consent_state INTEGER;", "daily_realtime_dcu_count", "ALTER TABLE apps ADD COLUMN daily_realtime_dcu_count INTEGER;", "bundle_delivery_index", "ALTER TABLE apps ADD COLUMN bundle_delivery_index INTEGER;", "serialized_npa_metadata", "ALTER TABLE apps ADD COLUMN serialized_npa_metadata TEXT;"};

    /* renamed from: i  reason: collision with root package name */
    private static final String[] f16741i = {"realtime", "ALTER TABLE raw_events ADD COLUMN realtime INTEGER;"};

    /* renamed from: j  reason: collision with root package name */
    private static final String[] f16742j = {"has_realtime", "ALTER TABLE queue ADD COLUMN has_realtime INTEGER;", "retry_count", "ALTER TABLE queue ADD COLUMN retry_count INTEGER;"};

    /* renamed from: k  reason: collision with root package name */
    private static final String[] f16743k = {"session_scoped", "ALTER TABLE event_filters ADD COLUMN session_scoped BOOLEAN;"};

    /* renamed from: l  reason: collision with root package name */
    private static final String[] f16744l = {"session_scoped", "ALTER TABLE property_filters ADD COLUMN session_scoped BOOLEAN;"};

    /* renamed from: m  reason: collision with root package name */
    private static final String[] f16745m = {"previous_install_count", "ALTER TABLE app2 ADD COLUMN previous_install_count INTEGER;"};

    /* renamed from: n  reason: collision with root package name */
    private static final String[] f16746n = {"consent_source", "ALTER TABLE consent_settings ADD COLUMN consent_source INTEGER;", "dma_consent_settings", "ALTER TABLE consent_settings ADD COLUMN dma_consent_settings TEXT;"};

    /* renamed from: o  reason: collision with root package name */
    private static final String[] f16747o = {"idempotent", "CREATE INDEX IF NOT EXISTS trigger_uris_index ON trigger_uris (app_id);"};

    /* renamed from: d  reason: collision with root package name */
    private final r f16748d;

    /* renamed from: e  reason: collision with root package name */
    private final wa f16749e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l(gb gbVar) {
        super(gbVar);
        this.f16749e = new wa(zzb());
        this.f16748d = new r(this, zza(), "google_app_measurement.db");
    }

    private final long E(String str, String[] strArr, long j8) {
        Cursor cursor = null;
        try {
            try {
                cursor = z().rawQuery(str, strArr);
                if (!cursor.moveToFirst()) {
                    cursor.close();
                    return j8;
                }
                long j9 = cursor.getLong(0);
                cursor.close();
                return j9;
            } catch (SQLiteException e8) {
                i().E().c("Database error", str, e8);
                throw e8;
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    private final Object K(Cursor cursor, int i8) {
        int type = cursor.getType(i8);
        if (type == 0) {
            i().E().a("Loaded invalid null value from database");
            return null;
        } else if (type != 1) {
            if (type != 2) {
                if (type != 3) {
                    if (type != 4) {
                        i().E().b("Loaded invalid unknown value type, ignoring it", Integer.valueOf(type));
                        return null;
                    }
                    i().E().a("Loaded invalid blob type value, ignoring it");
                    return null;
                }
                return cursor.getString(i8);
            }
            return Double.valueOf(cursor.getDouble(i8));
        } else {
            return Long.valueOf(cursor.getLong(i8));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0045  */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r0v2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final <T> T L(java.lang.String r3, java.lang.String[] r4, com.google.android.gms.measurement.internal.o<T> r5) {
        /*
            r2 = this;
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r2.z()     // Catch: java.lang.Throwable -> L2a android.database.sqlite.SQLiteException -> L2c
            android.database.Cursor r3 = r1.rawQuery(r3, r4)     // Catch: java.lang.Throwable -> L2a android.database.sqlite.SQLiteException -> L2c
            boolean r4 = r3.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L28 java.lang.Throwable -> L41
            if (r4 != 0) goto L20
            com.google.android.gms.measurement.internal.x4 r4 = r2.i()     // Catch: android.database.sqlite.SQLiteException -> L28 java.lang.Throwable -> L41
            com.google.android.gms.measurement.internal.z4 r4 = r4.I()     // Catch: android.database.sqlite.SQLiteException -> L28 java.lang.Throwable -> L41
            java.lang.String r5 = "No data found"
            r4.a(r5)     // Catch: android.database.sqlite.SQLiteException -> L28 java.lang.Throwable -> L41
            r3.close()
            return r0
        L20:
            java.lang.Object r4 = r5.a(r3)     // Catch: android.database.sqlite.SQLiteException -> L28 java.lang.Throwable -> L41
            r3.close()
            return r4
        L28:
            r4 = move-exception
            goto L2e
        L2a:
            r4 = move-exception
            goto L43
        L2c:
            r4 = move-exception
            r3 = r0
        L2e:
            com.google.android.gms.measurement.internal.x4 r5 = r2.i()     // Catch: java.lang.Throwable -> L41
            com.google.android.gms.measurement.internal.z4 r5 = r5.E()     // Catch: java.lang.Throwable -> L41
            java.lang.String r1 = "Error querying database."
            r5.b(r1, r4)     // Catch: java.lang.Throwable -> L41
            if (r3 == 0) goto L40
            r3.close()
        L40:
            return r0
        L41:
            r4 = move-exception
            r0 = r3
        L43:
            if (r0 == 0) goto L48
            r0.close()
        L48:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.l.L(java.lang.String, java.lang.String[], com.google.android.gms.measurement.internal.o):java.lang.Object");
    }

    private final String N(String str, String[] strArr, String str2) {
        Cursor cursor = null;
        try {
            try {
                cursor = z().rawQuery(str, strArr);
                if (!cursor.moveToFirst()) {
                    cursor.close();
                    return str2;
                }
                String string = cursor.getString(0);
                cursor.close();
                return string;
            } catch (SQLiteException e8) {
                i().E().c("Database error", str, e8);
                throw e8;
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    private static void R(ContentValues contentValues, String str, Object obj) {
        n6.j.f(str);
        n6.j.l(obj);
        if (obj instanceof String) {
            contentValues.put(str, (String) obj);
        } else if (obj instanceof Long) {
            contentValues.put(str, (Long) obj);
        } else if (!(obj instanceof Double)) {
            throw new IllegalArgumentException("Invalid value type");
        } else {
            contentValues.put(str, (Double) obj);
        }
    }

    private final void W(String str, String str2, ContentValues contentValues) {
        try {
            SQLiteDatabase z4 = z();
            String asString = contentValues.getAsString(str2);
            if (asString == null) {
                i().F().b("Value of the primary key is not set.", x4.t(str2));
                return;
            }
            if (z4.update(str, contentValues, str2 + " = ?", new String[]{asString}) == 0 && z4.insertWithOnConflict(str, null, contentValues, 5) == -1) {
                i().E().c("Failed to insert/update table (got -1). key", x4.t(str), x4.t(str2));
            }
        } catch (SQLiteException e8) {
            i().E().d("Error storing into table. key", x4.t(str), x4.t(str2), e8);
        }
    }

    private final boolean d0(String str, int i8, com.google.android.gms.internal.measurement.t3 t3Var) {
        s();
        k();
        n6.j.f(str);
        n6.j.l(t3Var);
        if (t3Var.Q().isEmpty()) {
            i().J().d("Event filter had no event name. Audience definition ignored. appId, audienceId, filterId", x4.t(str), Integer.valueOf(i8), String.valueOf(t3Var.W() ? Integer.valueOf(t3Var.M()) : null));
            return false;
        }
        byte[] k8 = t3Var.k();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", str);
        contentValues.put("audience_id", Integer.valueOf(i8));
        contentValues.put("filter_id", t3Var.W() ? Integer.valueOf(t3Var.M()) : null);
        contentValues.put("event_name", t3Var.Q());
        contentValues.put("session_scoped", t3Var.X() ? Boolean.valueOf(t3Var.U()) : null);
        contentValues.put("data", k8);
        try {
            if (z().insertWithOnConflict("event_filters", null, contentValues, 5) == -1) {
                i().E().b("Failed to insert event filter (got -1). appId", x4.t(str));
                return true;
            }
            return true;
        } catch (SQLiteException e8) {
            i().E().c("Error storing event filter. appId", x4.t(str), e8);
            return false;
        }
    }

    private final boolean e0(String str, int i8, com.google.android.gms.internal.measurement.v3 v3Var) {
        s();
        k();
        n6.j.f(str);
        n6.j.l(v3Var);
        if (v3Var.M().isEmpty()) {
            i().J().d("Property filter had no property name. Audience definition ignored. appId, audienceId, filterId", x4.t(str), Integer.valueOf(i8), String.valueOf(v3Var.Q() ? Integer.valueOf(v3Var.m()) : null));
            return false;
        }
        byte[] k8 = v3Var.k();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", str);
        contentValues.put("audience_id", Integer.valueOf(i8));
        contentValues.put("filter_id", v3Var.Q() ? Integer.valueOf(v3Var.m()) : null);
        contentValues.put("property_name", v3Var.M());
        contentValues.put("session_scoped", v3Var.R() ? Boolean.valueOf(v3Var.P()) : null);
        contentValues.put("data", k8);
        try {
            if (z().insertWithOnConflict("property_filters", null, contentValues, 5) == -1) {
                i().E().b("Failed to insert property filter (got -1). appId", x4.t(str));
                return false;
            }
            return true;
        } catch (SQLiteException e8) {
            i().E().c("Error storing property filter. appId", x4.t(str), e8);
            return false;
        }
    }

    private final boolean s0() {
        return zza().getDatabasePath("google_app_measurement.db").exists();
    }

    private final long v0(String str, String[] strArr) {
        Cursor cursor = null;
        try {
            try {
                Cursor rawQuery = z().rawQuery(str, strArr);
                if (rawQuery.moveToFirst()) {
                    long j8 = rawQuery.getLong(0);
                    rawQuery.close();
                    return j8;
                }
                throw new SQLiteException("Database returned empty set");
            } catch (SQLiteException e8) {
                i().E().c("Database error", str, e8);
                throw e8;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    private final boolean x0(String str, List<Integer> list) {
        n6.j.f(str);
        s();
        k();
        SQLiteDatabase z4 = z();
        try {
            long v02 = v0("select count(1) from audience_filter_values where app_id=?", new String[]{str});
            int max = Math.max(0, Math.min(2000, a().t(str, c0.H)));
            if (v02 <= max) {
                return false;
            }
            ArrayList arrayList = new ArrayList();
            for (int i8 = 0; i8 < list.size(); i8++) {
                Integer num = list.get(i8);
                if (num == null) {
                    return false;
                }
                arrayList.add(Integer.toString(num.intValue()));
            }
            StringBuilder sb = new StringBuilder("audience_id in (select audience_id from audience_filter_values where app_id=? and audience_id not in ");
            sb.append("(" + TextUtils.join(",", arrayList) + ")");
            sb.append(" order by rowid desc limit -1 offset ?)");
            return z4.delete("audience_filter_values", sb.toString(), new String[]{str, Integer.toString(max)}) > 0;
        } catch (SQLiteException e8) {
            i().E().c("Database error querying filters. appId", x4.t(str), e8);
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:23:0x003d  */
    /* JADX WARN: Type inference failed for: r0v0, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v4, types: [android.database.Cursor] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.String A() {
        /*
            r6 = this;
            android.database.sqlite.SQLiteDatabase r0 = r6.z()
            r1 = 0
            java.lang.String r2 = "select app_id from queue order by has_realtime desc, rowid asc limit 1;"
            android.database.Cursor r0 = r0.rawQuery(r2, r1)     // Catch: java.lang.Throwable -> L20 android.database.sqlite.SQLiteException -> L25
            boolean r2 = r0.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L1e java.lang.Throwable -> L3a
            if (r2 == 0) goto L1a
            r2 = 0
            java.lang.String r1 = r0.getString(r2)     // Catch: android.database.sqlite.SQLiteException -> L1e java.lang.Throwable -> L3a
            r0.close()
            return r1
        L1a:
            r0.close()
            return r1
        L1e:
            r2 = move-exception
            goto L27
        L20:
            r0 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
            goto L3b
        L25:
            r2 = move-exception
            r0 = r1
        L27:
            com.google.android.gms.measurement.internal.x4 r3 = r6.i()     // Catch: java.lang.Throwable -> L3a
            com.google.android.gms.measurement.internal.z4 r3 = r3.E()     // Catch: java.lang.Throwable -> L3a
            java.lang.String r4 = "Database error getting next bundle app id"
            r3.b(r4, r2)     // Catch: java.lang.Throwable -> L3a
            if (r0 == 0) goto L39
            r0.close()
        L39:
            return r1
        L3a:
            r1 = move-exception
        L3b:
            if (r0 == 0) goto L40
            r0.close()
        L40:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.l.A():java.lang.String");
    }

    /* JADX WARN: Not initialized variable reg: 1, insn: 0x0083: MOVE  (r0 I:??[OBJECT, ARRAY]) = (r1 I:??[OBJECT, ARRAY]), block:B:29:0x0083 */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0086  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.os.Bundle A0(java.lang.String r6) {
        /*
            r5 = this;
            r5.k()
            r5.s()
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r5.z()     // Catch: java.lang.Throwable -> L6b android.database.sqlite.SQLiteException -> L6d
            java.lang.String r2 = "select parameters from default_event_params where app_id=?"
            r3 = 1
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch: java.lang.Throwable -> L6b android.database.sqlite.SQLiteException -> L6d
            r4 = 0
            r3[r4] = r6     // Catch: java.lang.Throwable -> L6b android.database.sqlite.SQLiteException -> L6d
            android.database.Cursor r1 = r1.rawQuery(r2, r3)     // Catch: java.lang.Throwable -> L6b android.database.sqlite.SQLiteException -> L6d
            boolean r2 = r1.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            if (r2 != 0) goto L2e
            com.google.android.gms.measurement.internal.x4 r6 = r5.i()     // Catch: android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            com.google.android.gms.measurement.internal.z4 r6 = r6.I()     // Catch: android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            java.lang.String r2 = "Default event parameters not found"
            r6.a(r2)     // Catch: android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            r1.close()
            return r0
        L2e:
            byte[] r2 = r1.getBlob(r4)     // Catch: android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            com.google.android.gms.internal.measurement.r4$a r3 = com.google.android.gms.internal.measurement.r4.b0()     // Catch: java.io.IOException -> L53 android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            com.google.android.gms.internal.measurement.ha r2 = com.google.android.gms.measurement.internal.nb.E(r3, r2)     // Catch: java.io.IOException -> L53 android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            com.google.android.gms.internal.measurement.r4$a r2 = (com.google.android.gms.internal.measurement.r4.a) r2     // Catch: java.io.IOException -> L53 android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            com.google.android.gms.internal.measurement.ia r2 = r2.n()     // Catch: java.io.IOException -> L53 android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            com.google.android.gms.internal.measurement.x8 r2 = (com.google.android.gms.internal.measurement.x8) r2     // Catch: java.io.IOException -> L53 android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            com.google.android.gms.internal.measurement.r4 r2 = (com.google.android.gms.internal.measurement.r4) r2     // Catch: java.io.IOException -> L53 android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            r5.m()     // Catch: android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            java.util.List r6 = r2.e0()     // Catch: android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            android.os.Bundle r6 = com.google.android.gms.measurement.internal.nb.z(r6)     // Catch: android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            r1.close()
            return r6
        L53:
            r2 = move-exception
            com.google.android.gms.measurement.internal.x4 r3 = r5.i()     // Catch: android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            com.google.android.gms.measurement.internal.z4 r3 = r3.E()     // Catch: android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            java.lang.String r4 = "Failed to retrieve default event parameters. appId"
            java.lang.Object r6 = com.google.android.gms.measurement.internal.x4.t(r6)     // Catch: android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            r3.c(r4, r6, r2)     // Catch: android.database.sqlite.SQLiteException -> L69 java.lang.Throwable -> L82
            r1.close()
            return r0
        L69:
            r6 = move-exception
            goto L6f
        L6b:
            r6 = move-exception
            goto L84
        L6d:
            r6 = move-exception
            r1 = r0
        L6f:
            com.google.android.gms.measurement.internal.x4 r2 = r5.i()     // Catch: java.lang.Throwable -> L82
            com.google.android.gms.measurement.internal.z4 r2 = r2.E()     // Catch: java.lang.Throwable -> L82
            java.lang.String r3 = "Error selecting default event parameters"
            r2.b(r3, r6)     // Catch: java.lang.Throwable -> L82
            if (r1 == 0) goto L81
            r1.close()
        L81:
            return r0
        L82:
            r6 = move-exception
            r0 = r1
        L84:
            if (r0 == 0) goto L89
            r0.close()
        L89:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.l.A0(java.lang.String):android.os.Bundle");
    }

    public final int B(String str, String str2) {
        n6.j.f(str);
        n6.j.f(str2);
        k();
        s();
        try {
            return z().delete("conditional_properties", "app_id=? and name=?", new String[]{str, str2});
        } catch (SQLiteException e8) {
            i().E().d("Error deleting conditional property", x4.t(str), e().g(str2), e8);
            return 0;
        }
    }

    /* JADX WARN: Not initialized variable reg: 14, insn: 0x0147: MOVE  (r18 I:??[OBJECT, ARRAY]) = (r14 I:??[OBJECT, ARRAY]), block:B:64:0x0147 */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0142  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.google.android.gms.measurement.internal.z B0(java.lang.String r26, java.lang.String r27) {
        /*
            Method dump skipped, instructions count: 335
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.l.B0(java.lang.String, java.lang.String):com.google.android.gms.measurement.internal.z");
    }

    public final long C(com.google.android.gms.internal.measurement.v4 v4Var) {
        k();
        s();
        n6.j.l(v4Var);
        n6.j.f(v4Var.I3());
        byte[] k8 = v4Var.k();
        long y8 = m().y(k8);
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", v4Var.I3());
        contentValues.put("metadata_fingerprint", Long.valueOf(y8));
        contentValues.put("metadata", k8);
        try {
            z().insertWithOnConflict("raw_events_metadata", null, contentValues, 4);
            return y8;
        } catch (SQLiteException e8) {
            i().E().c("Error storing raw event metadata. appId", x4.t(v4Var.I3()), e8);
            throw e8;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x02f2 A[Catch: SQLiteException -> 0x0307, all -> 0x0330, TRY_LEAVE, TryCatch #0 {all -> 0x0330, blocks: (B:10:0x0085, B:12:0x00e6, B:17:0x00f0, B:21:0x013a, B:23:0x0169, B:28:0x0173, B:32:0x018e, B:34:0x0199, B:35:0x01ab, B:37:0x01b1, B:39:0x01bd, B:41:0x01c9, B:42:0x01d2, B:44:0x01d8, B:46:0x01e4, B:48:0x01ed, B:50:0x01f5, B:54:0x01fe, B:56:0x0216, B:57:0x021f, B:59:0x0237, B:61:0x0243, B:62:0x0255, B:64:0x025b, B:66:0x0267, B:68:0x026f, B:72:0x0278, B:73:0x027b, B:75:0x0281, B:77:0x028d, B:85:0x02a3, B:80:0x0297, B:84:0x029f, B:86:0x02a6, B:88:0x02ac, B:90:0x02b8, B:91:0x02ca, B:93:0x02d0, B:95:0x02dc, B:98:0x02e6, B:99:0x02e9, B:101:0x02f2, B:31:0x018a, B:20:0x0135, B:114:0x0319), top: B:123:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0334  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0131  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0135 A[Catch: SQLiteException -> 0x0307, all -> 0x0330, TryCatch #0 {all -> 0x0330, blocks: (B:10:0x0085, B:12:0x00e6, B:17:0x00f0, B:21:0x013a, B:23:0x0169, B:28:0x0173, B:32:0x018e, B:34:0x0199, B:35:0x01ab, B:37:0x01b1, B:39:0x01bd, B:41:0x01c9, B:42:0x01d2, B:44:0x01d8, B:46:0x01e4, B:48:0x01ed, B:50:0x01f5, B:54:0x01fe, B:56:0x0216, B:57:0x021f, B:59:0x0237, B:61:0x0243, B:62:0x0255, B:64:0x025b, B:66:0x0267, B:68:0x026f, B:72:0x0278, B:73:0x027b, B:75:0x0281, B:77:0x028d, B:85:0x02a3, B:80:0x0297, B:84:0x029f, B:86:0x02a6, B:88:0x02ac, B:90:0x02b8, B:91:0x02ca, B:93:0x02d0, B:95:0x02dc, B:98:0x02e6, B:99:0x02e9, B:101:0x02f2, B:31:0x018a, B:20:0x0135, B:114:0x0319), top: B:123:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0187  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x018a A[Catch: SQLiteException -> 0x0307, all -> 0x0330, TryCatch #0 {all -> 0x0330, blocks: (B:10:0x0085, B:12:0x00e6, B:17:0x00f0, B:21:0x013a, B:23:0x0169, B:28:0x0173, B:32:0x018e, B:34:0x0199, B:35:0x01ab, B:37:0x01b1, B:39:0x01bd, B:41:0x01c9, B:42:0x01d2, B:44:0x01d8, B:46:0x01e4, B:48:0x01ed, B:50:0x01f5, B:54:0x01fe, B:56:0x0216, B:57:0x021f, B:59:0x0237, B:61:0x0243, B:62:0x0255, B:64:0x025b, B:66:0x0267, B:68:0x026f, B:72:0x0278, B:73:0x027b, B:75:0x0281, B:77:0x028d, B:85:0x02a3, B:80:0x0297, B:84:0x029f, B:86:0x02a6, B:88:0x02ac, B:90:0x02b8, B:91:0x02ca, B:93:0x02d0, B:95:0x02dc, B:98:0x02e6, B:99:0x02e9, B:101:0x02f2, B:31:0x018a, B:20:0x0135, B:114:0x0319), top: B:123:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0199 A[Catch: SQLiteException -> 0x0307, all -> 0x0330, TryCatch #0 {all -> 0x0330, blocks: (B:10:0x0085, B:12:0x00e6, B:17:0x00f0, B:21:0x013a, B:23:0x0169, B:28:0x0173, B:32:0x018e, B:34:0x0199, B:35:0x01ab, B:37:0x01b1, B:39:0x01bd, B:41:0x01c9, B:42:0x01d2, B:44:0x01d8, B:46:0x01e4, B:48:0x01ed, B:50:0x01f5, B:54:0x01fe, B:56:0x0216, B:57:0x021f, B:59:0x0237, B:61:0x0243, B:62:0x0255, B:64:0x025b, B:66:0x0267, B:68:0x026f, B:72:0x0278, B:73:0x027b, B:75:0x0281, B:77:0x028d, B:85:0x02a3, B:80:0x0297, B:84:0x029f, B:86:0x02a6, B:88:0x02ac, B:90:0x02b8, B:91:0x02ca, B:93:0x02d0, B:95:0x02dc, B:98:0x02e6, B:99:0x02e9, B:101:0x02f2, B:31:0x018a, B:20:0x0135, B:114:0x0319), top: B:123:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x01b1 A[Catch: SQLiteException -> 0x0307, all -> 0x0330, TryCatch #0 {all -> 0x0330, blocks: (B:10:0x0085, B:12:0x00e6, B:17:0x00f0, B:21:0x013a, B:23:0x0169, B:28:0x0173, B:32:0x018e, B:34:0x0199, B:35:0x01ab, B:37:0x01b1, B:39:0x01bd, B:41:0x01c9, B:42:0x01d2, B:44:0x01d8, B:46:0x01e4, B:48:0x01ed, B:50:0x01f5, B:54:0x01fe, B:56:0x0216, B:57:0x021f, B:59:0x0237, B:61:0x0243, B:62:0x0255, B:64:0x025b, B:66:0x0267, B:68:0x026f, B:72:0x0278, B:73:0x027b, B:75:0x0281, B:77:0x028d, B:85:0x02a3, B:80:0x0297, B:84:0x029f, B:86:0x02a6, B:88:0x02ac, B:90:0x02b8, B:91:0x02ca, B:93:0x02d0, B:95:0x02dc, B:98:0x02e6, B:99:0x02e9, B:101:0x02f2, B:31:0x018a, B:20:0x0135, B:114:0x0319), top: B:123:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x01d8 A[Catch: SQLiteException -> 0x0307, all -> 0x0330, TryCatch #0 {all -> 0x0330, blocks: (B:10:0x0085, B:12:0x00e6, B:17:0x00f0, B:21:0x013a, B:23:0x0169, B:28:0x0173, B:32:0x018e, B:34:0x0199, B:35:0x01ab, B:37:0x01b1, B:39:0x01bd, B:41:0x01c9, B:42:0x01d2, B:44:0x01d8, B:46:0x01e4, B:48:0x01ed, B:50:0x01f5, B:54:0x01fe, B:56:0x0216, B:57:0x021f, B:59:0x0237, B:61:0x0243, B:62:0x0255, B:64:0x025b, B:66:0x0267, B:68:0x026f, B:72:0x0278, B:73:0x027b, B:75:0x0281, B:77:0x028d, B:85:0x02a3, B:80:0x0297, B:84:0x029f, B:86:0x02a6, B:88:0x02ac, B:90:0x02b8, B:91:0x02ca, B:93:0x02d0, B:95:0x02dc, B:98:0x02e6, B:99:0x02e9, B:101:0x02f2, B:31:0x018a, B:20:0x0135, B:114:0x0319), top: B:123:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x01ed A[Catch: SQLiteException -> 0x0307, all -> 0x0330, TryCatch #0 {all -> 0x0330, blocks: (B:10:0x0085, B:12:0x00e6, B:17:0x00f0, B:21:0x013a, B:23:0x0169, B:28:0x0173, B:32:0x018e, B:34:0x0199, B:35:0x01ab, B:37:0x01b1, B:39:0x01bd, B:41:0x01c9, B:42:0x01d2, B:44:0x01d8, B:46:0x01e4, B:48:0x01ed, B:50:0x01f5, B:54:0x01fe, B:56:0x0216, B:57:0x021f, B:59:0x0237, B:61:0x0243, B:62:0x0255, B:64:0x025b, B:66:0x0267, B:68:0x026f, B:72:0x0278, B:73:0x027b, B:75:0x0281, B:77:0x028d, B:85:0x02a3, B:80:0x0297, B:84:0x029f, B:86:0x02a6, B:88:0x02ac, B:90:0x02b8, B:91:0x02ca, B:93:0x02d0, B:95:0x02dc, B:98:0x02e6, B:99:0x02e9, B:101:0x02f2, B:31:0x018a, B:20:0x0135, B:114:0x0319), top: B:123:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0237 A[Catch: SQLiteException -> 0x0307, all -> 0x0330, TryCatch #0 {all -> 0x0330, blocks: (B:10:0x0085, B:12:0x00e6, B:17:0x00f0, B:21:0x013a, B:23:0x0169, B:28:0x0173, B:32:0x018e, B:34:0x0199, B:35:0x01ab, B:37:0x01b1, B:39:0x01bd, B:41:0x01c9, B:42:0x01d2, B:44:0x01d8, B:46:0x01e4, B:48:0x01ed, B:50:0x01f5, B:54:0x01fe, B:56:0x0216, B:57:0x021f, B:59:0x0237, B:61:0x0243, B:62:0x0255, B:64:0x025b, B:66:0x0267, B:68:0x026f, B:72:0x0278, B:73:0x027b, B:75:0x0281, B:77:0x028d, B:85:0x02a3, B:80:0x0297, B:84:0x029f, B:86:0x02a6, B:88:0x02ac, B:90:0x02b8, B:91:0x02ca, B:93:0x02d0, B:95:0x02dc, B:98:0x02e6, B:99:0x02e9, B:101:0x02f2, B:31:0x018a, B:20:0x0135, B:114:0x0319), top: B:123:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x025b A[Catch: SQLiteException -> 0x0307, all -> 0x0330, TryCatch #0 {all -> 0x0330, blocks: (B:10:0x0085, B:12:0x00e6, B:17:0x00f0, B:21:0x013a, B:23:0x0169, B:28:0x0173, B:32:0x018e, B:34:0x0199, B:35:0x01ab, B:37:0x01b1, B:39:0x01bd, B:41:0x01c9, B:42:0x01d2, B:44:0x01d8, B:46:0x01e4, B:48:0x01ed, B:50:0x01f5, B:54:0x01fe, B:56:0x0216, B:57:0x021f, B:59:0x0237, B:61:0x0243, B:62:0x0255, B:64:0x025b, B:66:0x0267, B:68:0x026f, B:72:0x0278, B:73:0x027b, B:75:0x0281, B:77:0x028d, B:85:0x02a3, B:80:0x0297, B:84:0x029f, B:86:0x02a6, B:88:0x02ac, B:90:0x02b8, B:91:0x02ca, B:93:0x02d0, B:95:0x02dc, B:98:0x02e6, B:99:0x02e9, B:101:0x02f2, B:31:0x018a, B:20:0x0135, B:114:0x0319), top: B:123:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0281 A[Catch: SQLiteException -> 0x0307, all -> 0x0330, TryCatch #0 {all -> 0x0330, blocks: (B:10:0x0085, B:12:0x00e6, B:17:0x00f0, B:21:0x013a, B:23:0x0169, B:28:0x0173, B:32:0x018e, B:34:0x0199, B:35:0x01ab, B:37:0x01b1, B:39:0x01bd, B:41:0x01c9, B:42:0x01d2, B:44:0x01d8, B:46:0x01e4, B:48:0x01ed, B:50:0x01f5, B:54:0x01fe, B:56:0x0216, B:57:0x021f, B:59:0x0237, B:61:0x0243, B:62:0x0255, B:64:0x025b, B:66:0x0267, B:68:0x026f, B:72:0x0278, B:73:0x027b, B:75:0x0281, B:77:0x028d, B:85:0x02a3, B:80:0x0297, B:84:0x029f, B:86:0x02a6, B:88:0x02ac, B:90:0x02b8, B:91:0x02ca, B:93:0x02d0, B:95:0x02dc, B:98:0x02e6, B:99:0x02e9, B:101:0x02f2, B:31:0x018a, B:20:0x0135, B:114:0x0319), top: B:123:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0295  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0297 A[Catch: SQLiteException -> 0x0307, all -> 0x0330, TryCatch #0 {all -> 0x0330, blocks: (B:10:0x0085, B:12:0x00e6, B:17:0x00f0, B:21:0x013a, B:23:0x0169, B:28:0x0173, B:32:0x018e, B:34:0x0199, B:35:0x01ab, B:37:0x01b1, B:39:0x01bd, B:41:0x01c9, B:42:0x01d2, B:44:0x01d8, B:46:0x01e4, B:48:0x01ed, B:50:0x01f5, B:54:0x01fe, B:56:0x0216, B:57:0x021f, B:59:0x0237, B:61:0x0243, B:62:0x0255, B:64:0x025b, B:66:0x0267, B:68:0x026f, B:72:0x0278, B:73:0x027b, B:75:0x0281, B:77:0x028d, B:85:0x02a3, B:80:0x0297, B:84:0x029f, B:86:0x02a6, B:88:0x02ac, B:90:0x02b8, B:91:0x02ca, B:93:0x02d0, B:95:0x02dc, B:98:0x02e6, B:99:0x02e9, B:101:0x02f2, B:31:0x018a, B:20:0x0135, B:114:0x0319), top: B:123:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x02ac A[Catch: SQLiteException -> 0x0307, all -> 0x0330, TryCatch #0 {all -> 0x0330, blocks: (B:10:0x0085, B:12:0x00e6, B:17:0x00f0, B:21:0x013a, B:23:0x0169, B:28:0x0173, B:32:0x018e, B:34:0x0199, B:35:0x01ab, B:37:0x01b1, B:39:0x01bd, B:41:0x01c9, B:42:0x01d2, B:44:0x01d8, B:46:0x01e4, B:48:0x01ed, B:50:0x01f5, B:54:0x01fe, B:56:0x0216, B:57:0x021f, B:59:0x0237, B:61:0x0243, B:62:0x0255, B:64:0x025b, B:66:0x0267, B:68:0x026f, B:72:0x0278, B:73:0x027b, B:75:0x0281, B:77:0x028d, B:85:0x02a3, B:80:0x0297, B:84:0x029f, B:86:0x02a6, B:88:0x02ac, B:90:0x02b8, B:91:0x02ca, B:93:0x02d0, B:95:0x02dc, B:98:0x02e6, B:99:0x02e9, B:101:0x02f2, B:31:0x018a, B:20:0x0135, B:114:0x0319), top: B:123:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x02d0 A[Catch: SQLiteException -> 0x0307, all -> 0x0330, TryCatch #0 {all -> 0x0330, blocks: (B:10:0x0085, B:12:0x00e6, B:17:0x00f0, B:21:0x013a, B:23:0x0169, B:28:0x0173, B:32:0x018e, B:34:0x0199, B:35:0x01ab, B:37:0x01b1, B:39:0x01bd, B:41:0x01c9, B:42:0x01d2, B:44:0x01d8, B:46:0x01e4, B:48:0x01ed, B:50:0x01f5, B:54:0x01fe, B:56:0x0216, B:57:0x021f, B:59:0x0237, B:61:0x0243, B:62:0x0255, B:64:0x025b, B:66:0x0267, B:68:0x026f, B:72:0x0278, B:73:0x027b, B:75:0x0281, B:77:0x028d, B:85:0x02a3, B:80:0x0297, B:84:0x029f, B:86:0x02a6, B:88:0x02ac, B:90:0x02b8, B:91:0x02ca, B:93:0x02d0, B:95:0x02dc, B:98:0x02e6, B:99:0x02e9, B:101:0x02f2, B:31:0x018a, B:20:0x0135, B:114:0x0319), top: B:123:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x02e4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.google.android.gms.measurement.internal.y3 C0(java.lang.String r47) {
        /*
            Method dump skipped, instructions count: 824
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.l.C0(java.lang.String):com.google.android.gms.measurement.internal.y3");
    }

    public final long D(String str) {
        n6.j.f(str);
        k();
        s();
        try {
            return z().delete("raw_events", "rowid in (select rowid from raw_events where app_id=? order by rowid desc limit -1 offset ?)", new String[]{str, String.valueOf(Math.max(0, Math.min(1000000, a().t(str, c0.f16404r))))});
        } catch (SQLiteException e8) {
            i().E().c("Error deleting over the limit events. appId", x4.t(str), e8);
            return 0L;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00ab  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.google.android.gms.measurement.internal.pb D0(java.lang.String r19, java.lang.String r20) {
        /*
            r18 = this;
            r8 = r20
            n6.j.f(r19)
            n6.j.f(r20)
            r18.k()
            r18.s()
            r9 = 0
            android.database.sqlite.SQLiteDatabase r10 = r18.z()     // Catch: java.lang.Throwable -> L80 android.database.sqlite.SQLiteException -> L84
            java.lang.String r11 = "user_attributes"
            java.lang.String r0 = "set_timestamp"
            java.lang.String r1 = "value"
            java.lang.String r2 = "origin"
            java.lang.String[] r12 = new java.lang.String[]{r0, r1, r2}     // Catch: java.lang.Throwable -> L80 android.database.sqlite.SQLiteException -> L84
            java.lang.String r13 = "app_id=? and name=?"
            r0 = 2
            java.lang.String[] r14 = new java.lang.String[r0]     // Catch: java.lang.Throwable -> L80 android.database.sqlite.SQLiteException -> L84
            r1 = 0
            r14[r1] = r19     // Catch: java.lang.Throwable -> L80 android.database.sqlite.SQLiteException -> L84
            r2 = 1
            r14[r2] = r8     // Catch: java.lang.Throwable -> L80 android.database.sqlite.SQLiteException -> L84
            r15 = 0
            r16 = 0
            r17 = 0
            android.database.Cursor r10 = r10.query(r11, r12, r13, r14, r15, r16, r17)     // Catch: java.lang.Throwable -> L80 android.database.sqlite.SQLiteException -> L84
            boolean r3 = r10.moveToFirst()     // Catch: java.lang.Throwable -> L78 android.database.sqlite.SQLiteException -> L7c
            if (r3 != 0) goto L3d
            r10.close()
            return r9
        L3d:
            long r5 = r10.getLong(r1)     // Catch: java.lang.Throwable -> L78 android.database.sqlite.SQLiteException -> L7c
            r11 = r18
            java.lang.Object r7 = r11.K(r10, r2)     // Catch: android.database.sqlite.SQLiteException -> L76 java.lang.Throwable -> La7
            if (r7 != 0) goto L4d
            r10.close()
            return r9
        L4d:
            java.lang.String r3 = r10.getString(r0)     // Catch: android.database.sqlite.SQLiteException -> L76 java.lang.Throwable -> La7
            com.google.android.gms.measurement.internal.pb r0 = new com.google.android.gms.measurement.internal.pb     // Catch: android.database.sqlite.SQLiteException -> L76 java.lang.Throwable -> La7
            r1 = r0
            r2 = r19
            r4 = r20
            r1.<init>(r2, r3, r4, r5, r7)     // Catch: android.database.sqlite.SQLiteException -> L76 java.lang.Throwable -> La7
            boolean r1 = r10.moveToNext()     // Catch: android.database.sqlite.SQLiteException -> L76 java.lang.Throwable -> La7
            if (r1 == 0) goto L72
            com.google.android.gms.measurement.internal.x4 r1 = r18.i()     // Catch: android.database.sqlite.SQLiteException -> L76 java.lang.Throwable -> La7
            com.google.android.gms.measurement.internal.z4 r1 = r1.E()     // Catch: android.database.sqlite.SQLiteException -> L76 java.lang.Throwable -> La7
            java.lang.String r2 = "Got multiple records for user property, expected one. appId"
            java.lang.Object r3 = com.google.android.gms.measurement.internal.x4.t(r19)     // Catch: android.database.sqlite.SQLiteException -> L76 java.lang.Throwable -> La7
            r1.b(r2, r3)     // Catch: android.database.sqlite.SQLiteException -> L76 java.lang.Throwable -> La7
        L72:
            r10.close()
            return r0
        L76:
            r0 = move-exception
            goto L88
        L78:
            r0 = move-exception
            r11 = r18
            goto La8
        L7c:
            r0 = move-exception
            r11 = r18
            goto L88
        L80:
            r0 = move-exception
            r11 = r18
            goto La9
        L84:
            r0 = move-exception
            r11 = r18
            r10 = r9
        L88:
            com.google.android.gms.measurement.internal.x4 r1 = r18.i()     // Catch: java.lang.Throwable -> La7
            com.google.android.gms.measurement.internal.z4 r1 = r1.E()     // Catch: java.lang.Throwable -> La7
            java.lang.String r2 = "Error querying user property. appId"
            java.lang.Object r3 = com.google.android.gms.measurement.internal.x4.t(r19)     // Catch: java.lang.Throwable -> La7
            com.google.android.gms.measurement.internal.s4 r4 = r18.e()     // Catch: java.lang.Throwable -> La7
            java.lang.String r4 = r4.g(r8)     // Catch: java.lang.Throwable -> La7
            r1.d(r2, r3, r4, r0)     // Catch: java.lang.Throwable -> La7
            if (r10 == 0) goto La6
            r10.close()
        La6:
            return r9
        La7:
            r0 = move-exception
        La8:
            r9 = r10
        La9:
            if (r9 == 0) goto Lae
            r9.close()
        Lae:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.l.D0(java.lang.String, java.lang.String):com.google.android.gms.measurement.internal.pb");
    }

    /* JADX WARN: Not initialized variable reg: 1, insn: 0x0084: MOVE  (r0 I:??[OBJECT, ARRAY]) = (r1 I:??[OBJECT, ARRAY]), block:B:28:0x0084 */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0087  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.google.android.gms.measurement.internal.n E0(java.lang.String r12) {
        /*
            r11 = this;
            n6.j.f(r12)
            r11.k()
            r11.s()
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r11.z()     // Catch: java.lang.Throwable -> L68 android.database.sqlite.SQLiteException -> L6a
            java.lang.String r2 = "apps"
            java.lang.String r3 = "remote_config"
            java.lang.String r4 = "config_last_modified_time"
            java.lang.String r5 = "e_tag"
            java.lang.String[] r3 = new java.lang.String[]{r3, r4, r5}     // Catch: java.lang.Throwable -> L68 android.database.sqlite.SQLiteException -> L6a
            java.lang.String r4 = "app_id=?"
            r9 = 1
            java.lang.String[] r5 = new java.lang.String[r9]     // Catch: java.lang.Throwable -> L68 android.database.sqlite.SQLiteException -> L6a
            r10 = 0
            r5[r10] = r12     // Catch: java.lang.Throwable -> L68 android.database.sqlite.SQLiteException -> L6a
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r1 = r1.query(r2, r3, r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L68 android.database.sqlite.SQLiteException -> L6a
            boolean r2 = r1.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L66 java.lang.Throwable -> L83
            if (r2 != 0) goto L33
            r1.close()
            return r0
        L33:
            byte[] r2 = r1.getBlob(r10)     // Catch: android.database.sqlite.SQLiteException -> L66 java.lang.Throwable -> L83
            java.lang.String r3 = r1.getString(r9)     // Catch: android.database.sqlite.SQLiteException -> L66 java.lang.Throwable -> L83
            r4 = 2
            java.lang.String r4 = r1.getString(r4)     // Catch: android.database.sqlite.SQLiteException -> L66 java.lang.Throwable -> L83
            boolean r5 = r1.moveToNext()     // Catch: android.database.sqlite.SQLiteException -> L66 java.lang.Throwable -> L83
            if (r5 == 0) goto L57
            com.google.android.gms.measurement.internal.x4 r5 = r11.i()     // Catch: android.database.sqlite.SQLiteException -> L66 java.lang.Throwable -> L83
            com.google.android.gms.measurement.internal.z4 r5 = r5.E()     // Catch: android.database.sqlite.SQLiteException -> L66 java.lang.Throwable -> L83
            java.lang.String r6 = "Got multiple records for app config, expected one. appId"
            java.lang.Object r7 = com.google.android.gms.measurement.internal.x4.t(r12)     // Catch: android.database.sqlite.SQLiteException -> L66 java.lang.Throwable -> L83
            r5.b(r6, r7)     // Catch: android.database.sqlite.SQLiteException -> L66 java.lang.Throwable -> L83
        L57:
            if (r2 != 0) goto L5d
            r1.close()
            return r0
        L5d:
            com.google.android.gms.measurement.internal.n r5 = new com.google.android.gms.measurement.internal.n     // Catch: android.database.sqlite.SQLiteException -> L66 java.lang.Throwable -> L83
            r5.<init>(r2, r3, r4)     // Catch: android.database.sqlite.SQLiteException -> L66 java.lang.Throwable -> L83
            r1.close()
            return r5
        L66:
            r2 = move-exception
            goto L6c
        L68:
            r12 = move-exception
            goto L85
        L6a:
            r2 = move-exception
            r1 = r0
        L6c:
            com.google.android.gms.measurement.internal.x4 r3 = r11.i()     // Catch: java.lang.Throwable -> L83
            com.google.android.gms.measurement.internal.z4 r3 = r3.E()     // Catch: java.lang.Throwable -> L83
            java.lang.String r4 = "Error querying remote config. appId"
            java.lang.Object r12 = com.google.android.gms.measurement.internal.x4.t(r12)     // Catch: java.lang.Throwable -> L83
            r3.c(r4, r12, r2)     // Catch: java.lang.Throwable -> L83
            if (r1 == 0) goto L82
            r1.close()
        L82:
            return r0
        L83:
            r12 = move-exception
            r0 = r1
        L85:
            if (r0 == 0) goto L8a
            r0.close()
        L8a:
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.l.E0(java.lang.String):com.google.android.gms.measurement.internal.n");
    }

    /* JADX WARN: Not initialized variable reg: 1, insn: 0x008b: MOVE  (r0 I:??[OBJECT, ARRAY]) = (r1 I:??[OBJECT, ARRAY]), block:B:29:0x008b */
    /* JADX WARN: Removed duplicated region for block: B:31:0x008e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.util.Pair<com.google.android.gms.internal.measurement.r4, java.lang.Long> F(java.lang.String r8, java.lang.Long r9) {
        /*
            r7 = this;
            r7.k()
            r7.s()
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r7.z()     // Catch: java.lang.Throwable -> L73 android.database.sqlite.SQLiteException -> L75
            java.lang.String r2 = "select main_event, children_to_process from main_event_params where app_id=? and event_id=?"
            r3 = 2
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch: java.lang.Throwable -> L73 android.database.sqlite.SQLiteException -> L75
            r4 = 0
            r3[r4] = r8     // Catch: java.lang.Throwable -> L73 android.database.sqlite.SQLiteException -> L75
            java.lang.String r5 = java.lang.String.valueOf(r9)     // Catch: java.lang.Throwable -> L73 android.database.sqlite.SQLiteException -> L75
            r6 = 1
            r3[r6] = r5     // Catch: java.lang.Throwable -> L73 android.database.sqlite.SQLiteException -> L75
            android.database.Cursor r1 = r1.rawQuery(r2, r3)     // Catch: java.lang.Throwable -> L73 android.database.sqlite.SQLiteException -> L75
            boolean r2 = r1.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            if (r2 != 0) goto L35
            com.google.android.gms.measurement.internal.x4 r8 = r7.i()     // Catch: android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            com.google.android.gms.measurement.internal.z4 r8 = r8.I()     // Catch: android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            java.lang.String r9 = "Main event not found"
            r8.a(r9)     // Catch: android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            r1.close()
            return r0
        L35:
            byte[] r2 = r1.getBlob(r4)     // Catch: android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            long r3 = r1.getLong(r6)     // Catch: android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            java.lang.Long r3 = java.lang.Long.valueOf(r3)     // Catch: android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            com.google.android.gms.internal.measurement.r4$a r4 = com.google.android.gms.internal.measurement.r4.b0()     // Catch: java.io.IOException -> L5b android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            com.google.android.gms.internal.measurement.ha r2 = com.google.android.gms.measurement.internal.nb.E(r4, r2)     // Catch: java.io.IOException -> L5b android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            com.google.android.gms.internal.measurement.r4$a r2 = (com.google.android.gms.internal.measurement.r4.a) r2     // Catch: java.io.IOException -> L5b android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            com.google.android.gms.internal.measurement.ia r2 = r2.n()     // Catch: java.io.IOException -> L5b android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            com.google.android.gms.internal.measurement.x8 r2 = (com.google.android.gms.internal.measurement.x8) r2     // Catch: java.io.IOException -> L5b android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            com.google.android.gms.internal.measurement.r4 r2 = (com.google.android.gms.internal.measurement.r4) r2     // Catch: java.io.IOException -> L5b android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            android.util.Pair r8 = android.util.Pair.create(r2, r3)     // Catch: android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            r1.close()
            return r8
        L5b:
            r2 = move-exception
            com.google.android.gms.measurement.internal.x4 r3 = r7.i()     // Catch: android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            com.google.android.gms.measurement.internal.z4 r3 = r3.E()     // Catch: android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            java.lang.String r4 = "Failed to merge main event. appId, eventId"
            java.lang.Object r8 = com.google.android.gms.measurement.internal.x4.t(r8)     // Catch: android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            r3.d(r4, r8, r9, r2)     // Catch: android.database.sqlite.SQLiteException -> L71 java.lang.Throwable -> L8a
            r1.close()
            return r0
        L71:
            r8 = move-exception
            goto L77
        L73:
            r8 = move-exception
            goto L8c
        L75:
            r8 = move-exception
            r1 = r0
        L77:
            com.google.android.gms.measurement.internal.x4 r9 = r7.i()     // Catch: java.lang.Throwable -> L8a
            com.google.android.gms.measurement.internal.z4 r9 = r9.E()     // Catch: java.lang.Throwable -> L8a
            java.lang.String r2 = "Error selecting main event"
            r9.b(r2, r8)     // Catch: java.lang.Throwable -> L8a
            if (r1 == 0) goto L89
            r1.close()
        L89:
            return r0
        L8a:
            r8 = move-exception
            r0 = r1
        L8c:
            if (r0 == 0) goto L91
            r0.close()
        L91:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.l.F(java.lang.String, java.lang.Long):android.util.Pair");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Map<Integer, List<com.google.android.gms.internal.measurement.t3>> F0(String str, String str2) {
        s();
        k();
        n6.j.f(str);
        n6.j.f(str2);
        k0.a aVar = new k0.a();
        Cursor cursor = null;
        try {
            try {
                Cursor query = z().query("event_filters", new String[]{"audience_id", "data"}, "app_id=? AND event_name=?", new String[]{str, str2}, null, null, null);
                if (!query.moveToFirst()) {
                    Map<Integer, List<com.google.android.gms.internal.measurement.t3>> emptyMap = Collections.emptyMap();
                    query.close();
                    return emptyMap;
                }
                do {
                    try {
                        com.google.android.gms.internal.measurement.t3 t3Var = (com.google.android.gms.internal.measurement.t3) ((com.google.android.gms.internal.measurement.x8) ((t3.a) nb.E(com.google.android.gms.internal.measurement.t3.N(), query.getBlob(1))).n());
                        int i8 = query.getInt(0);
                        List list = (List) aVar.get(Integer.valueOf(i8));
                        if (list == null) {
                            list = new ArrayList();
                            aVar.put(Integer.valueOf(i8), list);
                        }
                        list.add(t3Var);
                    } catch (IOException e8) {
                        i().E().c("Failed to merge filter. appId", x4.t(str), e8);
                    }
                } while (query.moveToNext());
                query.close();
                return aVar;
            } catch (SQLiteException e9) {
                i().E().c("Database error querying filters. appId", x4.t(str), e9);
                Map<Integer, List<com.google.android.gms.internal.measurement.t3>> emptyMap2 = Collections.emptyMap();
                if (0 != 0) {
                    cursor.close();
                }
                return emptyMap2;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public final m G(long j8, String str, long j9, boolean z4, boolean z8, boolean z9, boolean z10, boolean z11, boolean z12) {
        n6.j.f(str);
        k();
        s();
        String[] strArr = {str};
        m mVar = new m();
        Cursor cursor = null;
        try {
            try {
                SQLiteDatabase z13 = z();
                Cursor query = z13.query("apps", new String[]{"day", "daily_events_count", "daily_public_events_count", "daily_conversions_count", "daily_error_events_count", "daily_realtime_events_count", "daily_realtime_dcu_count"}, "app_id=?", new String[]{str}, null, null, null);
                if (!query.moveToFirst()) {
                    i().J().b("Not updating daily counts, app is not known. appId", x4.t(str));
                    query.close();
                    return mVar;
                }
                if (query.getLong(0) == j8) {
                    mVar.f16773b = query.getLong(1);
                    mVar.f16772a = query.getLong(2);
                    mVar.f16774c = query.getLong(3);
                    mVar.f16775d = query.getLong(4);
                    mVar.f16776e = query.getLong(5);
                    if (md.a() && a().r(c0.W0)) {
                        mVar.f16777f = query.getLong(6);
                    }
                }
                if (z4) {
                    mVar.f16773b += j9;
                }
                if (z8) {
                    mVar.f16772a += j9;
                }
                if (z9) {
                    mVar.f16774c += j9;
                }
                if (z10) {
                    mVar.f16775d += j9;
                }
                if (z11) {
                    mVar.f16776e += j9;
                }
                if (md.a() && a().r(c0.W0) && z12) {
                    mVar.f16777f += j9;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("day", Long.valueOf(j8));
                contentValues.put("daily_public_events_count", Long.valueOf(mVar.f16772a));
                contentValues.put("daily_events_count", Long.valueOf(mVar.f16773b));
                contentValues.put("daily_conversions_count", Long.valueOf(mVar.f16774c));
                contentValues.put("daily_error_events_count", Long.valueOf(mVar.f16775d));
                contentValues.put("daily_realtime_events_count", Long.valueOf(mVar.f16776e));
                if (md.a() && a().r(c0.W0)) {
                    contentValues.put("daily_realtime_dcu_count", Long.valueOf(mVar.f16777f));
                }
                z13.update("apps", contentValues, "app_id=?", strArr);
                query.close();
                return mVar;
            } catch (SQLiteException e8) {
                i().E().c("Error updating daily counts. appId", x4.t(str), e8);
                if (0 != 0) {
                    cursor.close();
                }
                return mVar;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public final v G0(String str) {
        if (md.a() && a().r(c0.S0)) {
            n6.j.l(str);
            k();
            s();
            return v.d(N("select dma_consent_settings from consent_settings where app_id=? limit 1;", new String[]{str}, BuildConfig.FLAVOR));
        }
        return v.f17035f;
    }

    public final m H(long j8, String str, boolean z4, boolean z8, boolean z9, boolean z10, boolean z11, boolean z12) {
        return G(j8, str, 1L, false, false, z9, false, z11, z12);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Map<Integer, List<com.google.android.gms.internal.measurement.v3>> H0(String str, String str2) {
        s();
        k();
        n6.j.f(str);
        n6.j.f(str2);
        k0.a aVar = new k0.a();
        Cursor cursor = null;
        try {
            try {
                Cursor query = z().query("property_filters", new String[]{"audience_id", "data"}, "app_id=? AND property_name=?", new String[]{str, str2}, null, null, null);
                if (!query.moveToFirst()) {
                    Map<Integer, List<com.google.android.gms.internal.measurement.v3>> emptyMap = Collections.emptyMap();
                    query.close();
                    return emptyMap;
                }
                do {
                    try {
                        com.google.android.gms.internal.measurement.v3 v3Var = (com.google.android.gms.internal.measurement.v3) ((com.google.android.gms.internal.measurement.x8) ((v3.a) nb.E(com.google.android.gms.internal.measurement.v3.K(), query.getBlob(1))).n());
                        int i8 = query.getInt(0);
                        List list = (List) aVar.get(Integer.valueOf(i8));
                        if (list == null) {
                            list = new ArrayList();
                            aVar.put(Integer.valueOf(i8), list);
                        }
                        list.add(v3Var);
                    } catch (IOException e8) {
                        i().E().c("Failed to merge filter", x4.t(str), e8);
                    }
                } while (query.moveToNext());
                query.close();
                return aVar;
            } catch (SQLiteException e9) {
                i().E().c("Database error querying filters. appId", x4.t(str), e9);
                Map<Integer, List<com.google.android.gms.internal.measurement.v3>> emptyMap2 = Collections.emptyMap();
                if (0 != 0) {
                    cursor.close();
                }
                return emptyMap2;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public final zziq I0(String str) {
        n6.j.l(str);
        k();
        s();
        if (md.a() && a().r(c0.S0)) {
            zziq zziqVar = (zziq) L("select consent_state, consent_source from consent_settings where app_id=? limit 1;", new String[]{str}, new o() { // from class: com.google.android.gms.measurement.internal.k
                @Override // com.google.android.gms.measurement.internal.o
                public final Object a(Cursor cursor) {
                    zziq i8;
                    i8 = zziq.i(cursor.getString(0), cursor.getInt(1));
                    return i8;
                }
            });
            return zziqVar == null ? zziq.f17272c : zziqVar;
        }
        return zziq.q(N("select consent_state from consent_settings where app_id=? limit 1;", new String[]{str}, "G1"));
    }

    public final void J0(String str, String str2) {
        n6.j.f(str);
        n6.j.f(str2);
        k();
        s();
        try {
            z().delete("user_attributes", "app_id=? and name=?", new String[]{str, str2});
        } catch (SQLiteException e8) {
            i().E().d("Error deleting user property. appId", x4.t(str), e().g(str2), e8);
        }
    }

    public final List<zzmv> K0(String str) {
        n6.j.f(str);
        k();
        s();
        ArrayList arrayList = new ArrayList();
        Cursor cursor = null;
        try {
            try {
                cursor = z().query("trigger_uris", new String[]{"trigger_uri", "timestamp_millis", "source"}, "app_id=?", new String[]{str}, null, null, "rowid", null);
                if (!cursor.moveToFirst()) {
                    cursor.close();
                    return arrayList;
                }
                do {
                    String string = cursor.getString(0);
                    if (string == null) {
                        string = BuildConfig.FLAVOR;
                    }
                    arrayList.add(new zzmv(string, cursor.getLong(1), cursor.getInt(2)));
                } while (cursor.moveToNext());
                cursor.close();
                return arrayList;
            } catch (SQLiteException e8) {
                i().E().c("Error querying trigger uris. appId", x4.t(str), e8);
                List<zzmv> emptyList = Collections.emptyList();
                if (cursor != null) {
                    cursor.close();
                }
                return emptyList;
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public final List<pb> L0(String str) {
        n6.j.f(str);
        k();
        s();
        ArrayList arrayList = new ArrayList();
        Cursor cursor = null;
        try {
            try {
                cursor = z().query("user_attributes", new String[]{"name", "origin", "set_timestamp", "value"}, "app_id=?", new String[]{str}, null, null, "rowid", "1000");
                if (!cursor.moveToFirst()) {
                    cursor.close();
                    return arrayList;
                }
                do {
                    String string = cursor.getString(0);
                    String string2 = cursor.getString(1);
                    if (string2 == null) {
                        string2 = BuildConfig.FLAVOR;
                    }
                    String str2 = string2;
                    long j8 = cursor.getLong(2);
                    Object K = K(cursor, 3);
                    if (K == null) {
                        i().E().b("Read invalid user property value, ignoring it. appId", x4.t(str));
                    } else {
                        arrayList.add(new pb(str, str2, string, j8, K));
                    }
                } while (cursor.moveToNext());
                cursor.close();
                return arrayList;
            } catch (SQLiteException e8) {
                i().E().c("Error querying user properties. appId", x4.t(str), e8);
                List<pb> emptyList = Collections.emptyList();
                if (cursor != null) {
                    cursor.close();
                }
                return emptyList;
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0057  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.String M(long r5) {
        /*
            r4 = this;
            r4.k()
            r4.s()
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r4.z()     // Catch: java.lang.Throwable -> L3c android.database.sqlite.SQLiteException -> L3e
            java.lang.String r2 = "select app_id from apps where app_id in (select distinct app_id from raw_events) and config_fetched_time < ? order by failed_config_fetch_time limit 1;"
            r3 = 1
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch: java.lang.Throwable -> L3c android.database.sqlite.SQLiteException -> L3e
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch: java.lang.Throwable -> L3c android.database.sqlite.SQLiteException -> L3e
            r6 = 0
            r3[r6] = r5     // Catch: java.lang.Throwable -> L3c android.database.sqlite.SQLiteException -> L3e
            android.database.Cursor r5 = r1.rawQuery(r2, r3)     // Catch: java.lang.Throwable -> L3c android.database.sqlite.SQLiteException -> L3e
            boolean r1 = r5.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L3a java.lang.Throwable -> L53
            if (r1 != 0) goto L32
            com.google.android.gms.measurement.internal.x4 r6 = r4.i()     // Catch: android.database.sqlite.SQLiteException -> L3a java.lang.Throwable -> L53
            com.google.android.gms.measurement.internal.z4 r6 = r6.I()     // Catch: android.database.sqlite.SQLiteException -> L3a java.lang.Throwable -> L53
            java.lang.String r1 = "No expired configs for apps with pending events"
            r6.a(r1)     // Catch: android.database.sqlite.SQLiteException -> L3a java.lang.Throwable -> L53
            r5.close()
            return r0
        L32:
            java.lang.String r6 = r5.getString(r6)     // Catch: android.database.sqlite.SQLiteException -> L3a java.lang.Throwable -> L53
            r5.close()
            return r6
        L3a:
            r6 = move-exception
            goto L40
        L3c:
            r6 = move-exception
            goto L55
        L3e:
            r6 = move-exception
            r5 = r0
        L40:
            com.google.android.gms.measurement.internal.x4 r1 = r4.i()     // Catch: java.lang.Throwable -> L53
            com.google.android.gms.measurement.internal.z4 r1 = r1.E()     // Catch: java.lang.Throwable -> L53
            java.lang.String r2 = "Error selecting expired configs"
            r1.b(r2, r6)     // Catch: java.lang.Throwable -> L53
            if (r5 == 0) goto L52
            r5.close()
        L52:
            return r0
        L53:
            r6 = move-exception
            r0 = r5
        L55:
            if (r0 == 0) goto L5a
            r0.close()
        L5a:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.l.M(long):java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Map<Integer, com.google.android.gms.internal.measurement.w4> M0(String str) {
        s();
        k();
        n6.j.f(str);
        Cursor cursor = null;
        try {
            try {
                Cursor query = z().query("audience_filter_values", new String[]{"audience_id", "current_results"}, "app_id=?", new String[]{str}, null, null, null);
                if (!query.moveToFirst()) {
                    Map<Integer, com.google.android.gms.internal.measurement.w4> emptyMap = Collections.emptyMap();
                    query.close();
                    return emptyMap;
                }
                k0.a aVar = new k0.a();
                do {
                    int i8 = query.getInt(0);
                    try {
                        aVar.put(Integer.valueOf(i8), (com.google.android.gms.internal.measurement.w4) ((com.google.android.gms.internal.measurement.x8) ((w4.a) nb.E(com.google.android.gms.internal.measurement.w4.W(), query.getBlob(1))).n()));
                    } catch (IOException e8) {
                        i().E().d("Failed to merge filter results. appId, audienceId, error", x4.t(str), Integer.valueOf(i8), e8);
                    }
                } while (query.moveToNext());
                query.close();
                return aVar;
            } catch (SQLiteException e9) {
                i().E().c("Database error querying filter results. appId", x4.t(str), e9);
                Map<Integer, com.google.android.gms.internal.measurement.w4> emptyMap2 = Collections.emptyMap();
                if (0 != 0) {
                    cursor.close();
                }
                return emptyMap2;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Map<Integer, List<com.google.android.gms.internal.measurement.t3>> N0(String str) {
        n6.j.f(str);
        k0.a aVar = new k0.a();
        Cursor cursor = null;
        try {
            try {
                Cursor query = z().query("event_filters", new String[]{"audience_id", "data"}, "app_id=?", new String[]{str}, null, null, null);
                if (!query.moveToFirst()) {
                    Map<Integer, List<com.google.android.gms.internal.measurement.t3>> emptyMap = Collections.emptyMap();
                    query.close();
                    return emptyMap;
                }
                do {
                    try {
                        com.google.android.gms.internal.measurement.t3 t3Var = (com.google.android.gms.internal.measurement.t3) ((com.google.android.gms.internal.measurement.x8) ((t3.a) nb.E(com.google.android.gms.internal.measurement.t3.N(), query.getBlob(1))).n());
                        if (t3Var.V()) {
                            int i8 = query.getInt(0);
                            List list = (List) aVar.get(Integer.valueOf(i8));
                            if (list == null) {
                                list = new ArrayList();
                                aVar.put(Integer.valueOf(i8), list);
                            }
                            list.add(t3Var);
                        }
                    } catch (IOException e8) {
                        i().E().c("Failed to merge filter. appId", x4.t(str), e8);
                    }
                } while (query.moveToNext());
                query.close();
                return aVar;
            } catch (SQLiteException e9) {
                i().E().c("Database error querying filters. appId", x4.t(str), e9);
                Map<Integer, List<com.google.android.gms.internal.measurement.t3>> emptyMap2 = Collections.emptyMap();
                if (0 != 0) {
                    cursor.close();
                }
                return emptyMap2;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:90:0x017f A[EDGE_INSN: B:90:0x017f->B:73:0x017f ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<android.util.Pair<com.google.android.gms.internal.measurement.v4, java.lang.Long>> O(java.lang.String r19, int r20, int r21) {
        /*
            Method dump skipped, instructions count: 423
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.l.O(java.lang.String, int, int):java.util.List");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Map<Integer, List<Integer>> O0(String str) {
        s();
        k();
        n6.j.f(str);
        k0.a aVar = new k0.a();
        Cursor cursor = null;
        try {
            try {
                Cursor rawQuery = z().rawQuery("select audience_id, filter_id from event_filters where app_id = ? and session_scoped = 1 UNION select audience_id, filter_id from property_filters where app_id = ? and session_scoped = 1;", new String[]{str, str});
                if (!rawQuery.moveToFirst()) {
                    Map<Integer, List<Integer>> emptyMap = Collections.emptyMap();
                    rawQuery.close();
                    return emptyMap;
                }
                do {
                    int i8 = rawQuery.getInt(0);
                    List list = (List) aVar.get(Integer.valueOf(i8));
                    if (list == null) {
                        list = new ArrayList();
                        aVar.put(Integer.valueOf(i8), list);
                    }
                    list.add(Integer.valueOf(rawQuery.getInt(1)));
                } while (rawQuery.moveToNext());
                rawQuery.close();
                return aVar;
            } catch (SQLiteException e8) {
                i().E().c("Database error querying scoped filters. appId", x4.t(str), e8);
                Map<Integer, List<Integer>> emptyMap2 = Collections.emptyMap();
                if (0 != 0) {
                    cursor.close();
                }
                return emptyMap2;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public final List<zzac> P(String str, String str2, String str3) {
        n6.j.f(str);
        k();
        s();
        ArrayList arrayList = new ArrayList(3);
        arrayList.add(str);
        StringBuilder sb = new StringBuilder("app_id=?");
        if (!TextUtils.isEmpty(str2)) {
            arrayList.add(str2);
            sb.append(" and origin=?");
        }
        if (!TextUtils.isEmpty(str3)) {
            arrayList.add(str3 + "*");
            sb.append(" and name glob ?");
        }
        return Q(sb.toString(), (String[]) arrayList.toArray(new String[arrayList.size()]));
    }

    public final void P0() {
        s();
        z().beginTransaction();
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0050, code lost:
        i().E().b("Read more than the max allowed conditional properties, ignoring extra", 1000);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<com.google.android.gms.measurement.internal.zzac> Q(java.lang.String r27, java.lang.String[] r28) {
        /*
            Method dump skipped, instructions count: 290
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.l.Q(java.lang.String, java.lang.String[]):java.util.List");
    }

    public final void Q0() {
        s();
        z().endTransaction();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void R0() {
        int delete;
        k();
        s();
        if (s0()) {
            long a9 = q().f16683e.a();
            long b9 = zzb().b();
            if (Math.abs(b9 - a9) > c0.A.a(null).longValue()) {
                q().f16683e.b(b9);
                k();
                s();
                if (!s0() || (delete = z().delete("queue", "abs(bundle_end_timestamp - ?) > cast(? as integer)", new String[]{String.valueOf(zzb().a()), String.valueOf(e.M())})) <= 0) {
                    return;
                }
                i().I().b("Deleted stale rows. rowsDeleted", Integer.valueOf(delete));
            }
        }
    }

    public final void S(z zVar) {
        n6.j.l(zVar);
        k();
        s();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zVar.f17206a);
        contentValues.put("name", zVar.f17207b);
        contentValues.put("lifetime_count", Long.valueOf(zVar.f17208c));
        contentValues.put("current_bundle_count", Long.valueOf(zVar.f17209d));
        contentValues.put("last_fire_timestamp", Long.valueOf(zVar.f17211f));
        contentValues.put("last_bundled_timestamp", Long.valueOf(zVar.f17212g));
        contentValues.put("last_bundled_day", zVar.f17213h);
        contentValues.put("last_sampled_complex_event_id", zVar.f17214i);
        contentValues.put("last_sampling_rate", zVar.f17215j);
        contentValues.put("current_session_count", Long.valueOf(zVar.f17210e));
        Boolean bool = zVar.f17216k;
        contentValues.put("last_exempt_from_sampling", (bool == null || !bool.booleanValue()) ? null : 1L);
        try {
            if (z().insertWithOnConflict("events", null, contentValues, 5) == -1) {
                i().E().b("Failed to insert/update event aggregates (got -1). appId", x4.t(zVar.f17206a));
            }
        } catch (SQLiteException e8) {
            i().E().c("Error storing event aggregates. appId", x4.t(zVar.f17206a), e8);
        }
    }

    public final void S0() {
        s();
        z().setTransactionSuccessful();
    }

    public final void T(y3 y3Var) {
        n6.j.l(y3Var);
        k();
        s();
        String h8 = y3Var.h();
        n6.j.l(h8);
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", h8);
        contentValues.put("app_instance_id", y3Var.i());
        contentValues.put("gmp_app_id", y3Var.m());
        contentValues.put("resettable_device_id_hash", y3Var.o());
        contentValues.put("last_bundle_index", Long.valueOf(y3Var.z0()));
        contentValues.put("last_bundle_start_timestamp", Long.valueOf(y3Var.B0()));
        contentValues.put("last_bundle_end_timestamp", Long.valueOf(y3Var.x0()));
        contentValues.put("app_version", y3Var.k());
        contentValues.put("app_store", y3Var.j());
        contentValues.put("gmp_version", Long.valueOf(y3Var.t0()));
        contentValues.put("dev_cert_hash", Long.valueOf(y3Var.n0()));
        contentValues.put("measurement_enabled", Boolean.valueOf(y3Var.w()));
        contentValues.put("day", Long.valueOf(y3Var.l0()));
        contentValues.put("daily_public_events_count", Long.valueOf(y3Var.g0()));
        contentValues.put("daily_events_count", Long.valueOf(y3Var.d0()));
        contentValues.put("daily_conversions_count", Long.valueOf(y3Var.X()));
        contentValues.put("config_fetched_time", Long.valueOf(y3Var.U()));
        contentValues.put("failed_config_fetch_time", Long.valueOf(y3Var.r0()));
        contentValues.put("app_version_int", Long.valueOf(y3Var.O()));
        contentValues.put("firebase_instance_id", y3Var.l());
        contentValues.put("daily_error_events_count", Long.valueOf(y3Var.a0()));
        contentValues.put("daily_realtime_events_count", Long.valueOf(y3Var.j0()));
        contentValues.put("health_monitor_sample", y3Var.n());
        contentValues.put("android_id", Long.valueOf(y3Var.K()));
        contentValues.put("adid_reporting_enabled", Boolean.valueOf(y3Var.v()));
        contentValues.put("admob_app_id", y3Var.F0());
        contentValues.put("dynamite_version", Long.valueOf(y3Var.p0()));
        contentValues.put("session_stitching_token", y3Var.q());
        contentValues.put("sgtm_upload_enabled", Boolean.valueOf(y3Var.y()));
        contentValues.put("target_os_version", Long.valueOf(y3Var.D0()));
        contentValues.put("session_stitching_token_hash", Long.valueOf(y3Var.C0()));
        if (ye.a() && a().B(h8, c0.J0)) {
            contentValues.put("ad_services_version", Integer.valueOf(y3Var.a()));
            contentValues.put("attribution_eligibility_status", Long.valueOf(y3Var.R()));
        }
        if (gd.a() && a().B(h8, c0.X0)) {
            contentValues.put("unmatched_first_open_without_ad_id", Boolean.valueOf(y3Var.z()));
        }
        if (md.a() && a().B(h8, c0.S0)) {
            contentValues.put("npa_metadata_value", y3Var.E0());
        }
        if (wf.a() && a().B(h8, c0.f16415w0)) {
            g();
            if (sb.F0(h8)) {
                contentValues.put("bundle_delivery_index", Long.valueOf(y3Var.v0()));
            }
        }
        if (wf.a() && a().B(h8, c0.f16417x0)) {
            contentValues.put("sgtm_preview_key", y3Var.r());
        }
        if (md.a() && a().B(h8, c0.W0)) {
            contentValues.put("dma_consent_state", Integer.valueOf(y3Var.F()));
            contentValues.put("daily_realtime_dcu_count", Integer.valueOf(y3Var.A()));
        }
        if (uc.a() && a().B(h8, c0.f16374e1)) {
            contentValues.put("serialized_npa_metadata", y3Var.p());
        }
        List<String> s8 = y3Var.s();
        if (s8 != null) {
            if (s8.isEmpty()) {
                i().J().b("Safelisted events should not be an empty list. appId", h8);
            } else {
                contentValues.put("safelisted_events", TextUtils.join(",", s8));
            }
        }
        if (nd.a() && a().r(c0.f16403q0) && !contentValues.containsKey("safelisted_events")) {
            contentValues.put("safelisted_events", (String) null);
        }
        try {
            SQLiteDatabase z4 = z();
            if (z4.update("apps", contentValues, "app_id = ?", new String[]{h8}) == 0 && z4.insertWithOnConflict("apps", null, contentValues, 5) == -1) {
                i().E().b("Failed to insert/update app (got -1). appId", x4.t(h8));
            }
        } catch (SQLiteException e8) {
            i().E().c("Error storing app. appId", x4.t(h8), e8);
        }
    }

    public final boolean T0() {
        return v0("select count(1) > 0 from raw_events", null) != 0;
    }

    public final void U(String str, v vVar) {
        if (md.a() && a().r(c0.S0)) {
            n6.j.l(str);
            n6.j.l(vVar);
            k();
            s();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("dma_consent_settings", vVar.j());
            W("consent_settings", "app_id", contentValues);
        }
    }

    public final boolean U0() {
        return v0("select count(1) > 0 from queue where has_realtime = 1", null) != 0;
    }

    public final void V(String str, zziq zziqVar) {
        n6.j.l(str);
        n6.j.l(zziqVar);
        k();
        s();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", str);
        contentValues.put("consent_state", zziqVar.z());
        if (md.a() && a().r(c0.S0)) {
            contentValues.put("consent_source", Integer.valueOf(zziqVar.b()));
            W("consent_settings", "app_id", contentValues);
            return;
        }
        try {
            if (z().insertWithOnConflict("consent_settings", null, contentValues, 5) == -1) {
                i().E().b("Failed to insert/update consent setting (got -1). appId", x4.t(str));
            }
        } catch (SQLiteException e8) {
            i().E().c("Error storing consent setting. appId, error", x4.t(str), e8);
        }
    }

    public final boolean V0() {
        return v0("select count(1) > 0 from raw_events where realtime = 1", null) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0164, code lost:
        r8.c(r10, r11, r9);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void X(java.lang.String r18, java.util.List<com.google.android.gms.internal.measurement.s3> r19) {
        /*
            Method dump skipped, instructions count: 571
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.l.X(java.lang.String, java.util.List):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void Y(List<Long> list) {
        k();
        s();
        n6.j.l(list);
        n6.j.n(list.size());
        if (s0()) {
            String str = "(" + TextUtils.join(",", list) + ")";
            if (v0("SELECT COUNT(1) FROM queue WHERE rowid IN " + str + " AND retry_count =  2147483647 LIMIT 1", null) > 0) {
                i().J().a("The number of upload retries exceeds the limit. Will remain unchanged.");
            }
            try {
                z().execSQL("UPDATE queue SET retry_count = IFNULL(retry_count, 0) + 1 WHERE rowid IN " + str + " AND (retry_count IS NULL OR retry_count < 2147483647)");
            } catch (SQLiteException e8) {
                i().E().b("Error incrementing retry count. error", e8);
            }
        }
    }

    public final boolean Z(com.google.android.gms.internal.measurement.v4 v4Var, boolean z4) {
        z4 E;
        Object t8;
        String str;
        ContentValues contentValues;
        k();
        s();
        n6.j.l(v4Var);
        n6.j.f(v4Var.I3());
        n6.j.p(v4Var.W0());
        R0();
        long a9 = zzb().a();
        if (v4Var.b3() < a9 - e.M() || v4Var.b3() > e.M() + a9) {
            i().J().d("Storing bundle outside of the max uploading time span. appId, now, timestamp", x4.t(v4Var.I3()), Long.valueOf(a9), Long.valueOf(v4Var.b3()));
        }
        try {
            byte[] h02 = m().h0(v4Var.k());
            i().I().b("Saving bundle, size", Integer.valueOf(h02.length));
            contentValues = new ContentValues();
            contentValues.put("app_id", v4Var.I3());
            contentValues.put("bundle_end_timestamp", Long.valueOf(v4Var.b3()));
            contentValues.put("data", h02);
            contentValues.put("has_realtime", Integer.valueOf(z4 ? 1 : 0));
            if (v4Var.d1()) {
                contentValues.put("retry_count", Integer.valueOf(v4Var.h2()));
            }
        } catch (IOException e8) {
            e = e8;
            E = i().E();
            t8 = x4.t(v4Var.I3());
            str = "Data loss. Failed to serialize bundle. appId";
        }
        try {
            if (z().insert("queue", null, contentValues) == -1) {
                i().E().b("Failed to insert bundle (got -1). appId", x4.t(v4Var.I3()));
                return false;
            }
            return true;
        } catch (SQLiteException e9) {
            e = e9;
            E = i().E();
            t8 = x4.t(v4Var.I3());
            str = "Error storing bundle. appId";
            E.c(str, t8, e);
            return false;
        }
    }

    public final boolean a0(zzac zzacVar) {
        n6.j.l(zzacVar);
        k();
        s();
        String str = zzacVar.f17250a;
        n6.j.l(str);
        if (D0(str, zzacVar.f17252c.f17308b) != null || v0("SELECT COUNT(1) FROM conditional_properties WHERE app_id=?", new String[]{str}) < 1000) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("origin", zzacVar.f17251b);
            contentValues.put("name", zzacVar.f17252c.f17308b);
            R(contentValues, "value", n6.j.l(zzacVar.f17252c.t()));
            contentValues.put("active", Boolean.valueOf(zzacVar.f17254e));
            contentValues.put("trigger_event_name", zzacVar.f17255f);
            contentValues.put("trigger_timeout", Long.valueOf(zzacVar.f17257h));
            g();
            contentValues.put("timed_out_event", sb.o0(zzacVar.f17256g));
            contentValues.put("creation_timestamp", Long.valueOf(zzacVar.f17253d));
            g();
            contentValues.put("triggered_event", sb.o0(zzacVar.f17258j));
            contentValues.put("triggered_timestamp", Long.valueOf(zzacVar.f17252c.f17309c));
            contentValues.put("time_to_live", Long.valueOf(zzacVar.f17259k));
            g();
            contentValues.put("expired_event", sb.o0(zzacVar.f17260l));
            try {
                if (z().insertWithOnConflict("conditional_properties", null, contentValues, 5) == -1) {
                    i().E().b("Failed to insert/update conditional user property (got -1)", x4.t(str));
                }
            } catch (SQLiteException e8) {
                i().E().c("Error storing conditional user property", x4.t(str), e8);
            }
            return true;
        }
        return false;
    }

    public final boolean b0(w wVar, long j8, boolean z4) {
        k();
        s();
        n6.j.l(wVar);
        n6.j.f(wVar.f17056a);
        byte[] k8 = m().C(wVar).k();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", wVar.f17056a);
        contentValues.put("name", wVar.f17057b);
        contentValues.put("timestamp", Long.valueOf(wVar.f17059d));
        contentValues.put("metadata_fingerprint", Long.valueOf(j8));
        contentValues.put("data", k8);
        contentValues.put("realtime", Integer.valueOf(z4 ? 1 : 0));
        try {
            if (z().insert("raw_events", null, contentValues) == -1) {
                i().E().b("Failed to insert raw event (got -1). appId", x4.t(wVar.f17056a));
                return false;
            }
            return true;
        } catch (SQLiteException e8) {
            i().E().c("Error storing raw event. appId", x4.t(wVar.f17056a), e8);
            return false;
        }
    }

    public final boolean c0(pb pbVar) {
        n6.j.l(pbVar);
        k();
        s();
        if (D0(pbVar.f16885a, pbVar.f16887c) == null) {
            if (sb.J0(pbVar.f16887c)) {
                if (v0("select count(1) from user_attributes where app_id=? and name not like '!_%' escape '!'", new String[]{pbVar.f16885a}) >= a().o(pbVar.f16885a, c0.I, 25, 100)) {
                    return false;
                }
            } else if (!"_npa".equals(pbVar.f16887c) && v0("select count(1) from user_attributes where app_id=? and origin=? AND name like '!_%' escape '!'", new String[]{pbVar.f16885a, pbVar.f16886b}) >= 25) {
                return false;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", pbVar.f16885a);
        contentValues.put("origin", pbVar.f16886b);
        contentValues.put("name", pbVar.f16887c);
        contentValues.put("set_timestamp", Long.valueOf(pbVar.f16888d));
        R(contentValues, "value", pbVar.f16889e);
        try {
            if (z().insertWithOnConflict("user_attributes", null, contentValues, 5) == -1) {
                i().E().b("Failed to insert/update user property (got -1). appId", x4.t(pbVar.f16885a));
            }
        } catch (SQLiteException e8) {
            i().E().c("Error storing user property. appId", x4.t(pbVar.f16885a), e8);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean f0(String str, Bundle bundle) {
        k();
        s();
        byte[] k8 = m().C(new w(this.f16485a, BuildConfig.FLAVOR, str, "dep", 0L, 0L, bundle)).k();
        i().I().c("Saving default event parameters, appId, data size", e().c(str), Integer.valueOf(k8.length));
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", str);
        contentValues.put("parameters", k8);
        try {
            if (z().insertWithOnConflict("default_event_params", null, contentValues, 5) == -1) {
                i().E().b("Failed to insert default event parameters (got -1). appId", x4.t(str));
                return false;
            }
            return true;
        } catch (SQLiteException e8) {
            i().E().c("Error storing default event parameters. appId", x4.t(str), e8);
            return false;
        }
    }

    public final boolean g0(String str, zzmv zzmvVar) {
        k();
        s();
        n6.j.l(zzmvVar);
        n6.j.f(str);
        long a9 = zzb().a();
        if (zzmvVar.f17286b < a9 - e.M() || zzmvVar.f17286b > e.M() + a9) {
            i().J().d("Storing trigger URI outside of the max retention time span. appId, now, timestamp", x4.t(str), Long.valueOf(a9), Long.valueOf(zzmvVar.f17286b));
        }
        i().I().a("Saving trigger URI");
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", str);
        contentValues.put("trigger_uri", zzmvVar.f17285a);
        contentValues.put("source", Integer.valueOf(zzmvVar.f17287c));
        contentValues.put("timestamp_millis", Long.valueOf(zzmvVar.f17286b));
        try {
            if (z().insert("trigger_uris", null, contentValues) == -1) {
                i().E().b("Failed to insert trigger URI (got -1). appId", x4.t(str));
                return false;
            }
            return true;
        } catch (SQLiteException e8) {
            i().E().c("Error storing trigger URI. appId", x4.t(str), e8);
            return false;
        }
    }

    public final boolean h0(String str, Long l8, long j8, com.google.android.gms.internal.measurement.r4 r4Var) {
        k();
        s();
        n6.j.l(r4Var);
        n6.j.f(str);
        n6.j.l(l8);
        byte[] k8 = r4Var.k();
        i().I().c("Saving complex main event, appId, data size", e().c(str), Integer.valueOf(k8.length));
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", str);
        contentValues.put("event_id", l8);
        contentValues.put("children_to_process", Long.valueOf(j8));
        contentValues.put("main_event", k8);
        try {
            if (z().insertWithOnConflict("main_event_params", null, contentValues, 5) == -1) {
                i().E().b("Failed to insert complex main event (got -1). appId", x4.t(str));
                return false;
            }
            return true;
        } catch (SQLiteException e8) {
            i().E().c("Error storing complex main event. appId", x4.t(str), e8);
            return false;
        }
    }

    public final long t0(String str) {
        n6.j.f(str);
        k();
        s();
        return E("select first_open_count from app2 where app_id=?", new String[]{str}, -1L);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final long u0(String str, String str2) {
        long E;
        n6.j.f(str);
        n6.j.f(str2);
        k();
        s();
        SQLiteDatabase z4 = z();
        z4.beginTransaction();
        long j8 = 0;
        try {
            try {
                E = E("select " + str2 + " from app2 where app_id=?", new String[]{str}, -1L);
                if (E == -1) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("app_id", str);
                    contentValues.put("first_open_count", (Integer) 0);
                    contentValues.put("previous_install_count", (Integer) 0);
                    if (z4.insertWithOnConflict("app2", null, contentValues, 5) == -1) {
                        i().E().c("Failed to insert column (got -1). appId", x4.t(str), str2);
                        return -1L;
                    }
                    E = 0;
                }
            } catch (SQLiteException e8) {
                e = e8;
            }
            try {
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("app_id", str);
                contentValues2.put(str2, Long.valueOf(1 + E));
                if (z4.update("app2", contentValues2, "app_id = ?", new String[]{str}) == 0) {
                    i().E().c("Failed to update column (got 0). appId", x4.t(str), str2);
                    return -1L;
                }
                z4.setTransactionSuccessful();
                return E;
            } catch (SQLiteException e9) {
                e = e9;
                j8 = E;
                i().E().d("Error inserting column. appId", x4.t(str), str2, e);
                z4.endTransaction();
                return j8;
            }
        } finally {
            z4.endTransaction();
        }
    }

    @Override // com.google.android.gms.measurement.internal.bb
    protected final boolean v() {
        return false;
    }

    public final long w() {
        Cursor cursor = null;
        try {
            try {
                cursor = z().rawQuery("select rowid from raw_events order by rowid desc limit 1;", null);
                if (!cursor.moveToFirst()) {
                    cursor.close();
                    return -1L;
                }
                long j8 = cursor.getLong(0);
                cursor.close();
                return j8;
            } catch (SQLiteException e8) {
                i().E().b("Error querying raw events", e8);
                if (cursor != null) {
                    cursor.close();
                }
                return -1L;
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0097, code lost:
        i().E().b("Read more than the max allowed user properties, ignoring excess", 1000);
     */
    /* JADX WARN: Removed duplicated region for block: B:46:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0122  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<com.google.android.gms.measurement.internal.pb> w0(java.lang.String r23, java.lang.String r24, java.lang.String r25) {
        /*
            Method dump skipped, instructions count: 294
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.l.w0(java.lang.String, java.lang.String, java.lang.String):java.util.List");
    }

    public final long x() {
        return E("select max(bundle_end_timestamp) from queue", null, 0L);
    }

    public final long y() {
        return E("select max(timestamp) from raw_events", null, 0L);
    }

    public final long y0(String str) {
        n6.j.f(str);
        return E("select count(1) from events where app_id=? and name not like '!_%' escape '!'", new String[]{str}, 0L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final SQLiteDatabase z() {
        k();
        try {
            return this.f16748d.getWritableDatabase();
        } catch (SQLiteException e8) {
            i().J().b("Error opening database", e8);
            throw e8;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0123  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.google.android.gms.measurement.internal.zzac z0(java.lang.String r30, java.lang.String r31) {
        /*
            Method dump skipped, instructions count: 295
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.l.z0(java.lang.String, java.lang.String):com.google.android.gms.measurement.internal.zzac");
    }
}
