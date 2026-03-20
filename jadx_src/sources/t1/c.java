package t1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface c extends Closeable {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {

        /* renamed from: a  reason: collision with root package name */
        public final int f22836a;

        public a(int i8) {
            this.f22836a = i8;
        }

        private void a(String str) {
            if (str.equalsIgnoreCase(":memory:") || str.trim().length() == 0) {
                return;
            }
            Log.w("SupportSQLite", "deleting the database file: " + str);
            try {
                if (Build.VERSION.SDK_INT >= 16) {
                    SQLiteDatabase.deleteDatabase(new File(str));
                } else {
                    try {
                        if (!new File(str).delete()) {
                            Log.e("SupportSQLite", "Could not delete the database file " + str);
                        }
                    } catch (Exception e8) {
                        Log.e("SupportSQLite", "error while deleting corrupted database file", e8);
                    }
                }
            } catch (Exception e9) {
                Log.w("SupportSQLite", "delete failed: ", e9);
            }
        }

        public void b(t1.b bVar) {
        }

        public void c(t1.b bVar) {
            Log.e("SupportSQLite", "Corruption reported by sqlite on database: " + bVar.y1());
            if (!bVar.isOpen()) {
                a(bVar.y1());
                return;
            }
            List<Pair<String, String>> list = null;
            try {
                try {
                    list = bVar.F();
                } catch (SQLiteException unused) {
                }
                try {
                    bVar.close();
                } catch (IOException unused2) {
                }
            } finally {
                if (list != null) {
                    for (Pair<String, String> next : list) {
                        a((String) next.second);
                    }
                } else {
                    a(bVar.y1());
                }
            }
        }

        public abstract void d(t1.b bVar);

        public abstract void e(t1.b bVar, int i8, int i9);

        public void f(t1.b bVar) {
        }

        public abstract void g(t1.b bVar, int i8, int i9);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        public final Context f22837a;

        /* renamed from: b  reason: collision with root package name */
        public final String f22838b;

        /* renamed from: c  reason: collision with root package name */
        public final a f22839c;

        /* renamed from: d  reason: collision with root package name */
        public final boolean f22840d;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class a {

            /* renamed from: a  reason: collision with root package name */
            Context f22841a;

            /* renamed from: b  reason: collision with root package name */
            String f22842b;

            /* renamed from: c  reason: collision with root package name */
            a f22843c;

            /* renamed from: d  reason: collision with root package name */
            boolean f22844d;

            a(Context context) {
                this.f22841a = context;
            }

            public b a() {
                if (this.f22843c != null) {
                    if (this.f22841a != null) {
                        if (this.f22844d && TextUtils.isEmpty(this.f22842b)) {
                            throw new IllegalArgumentException("Must set a non-null database name to a configuration that uses the no backup directory.");
                        }
                        return new b(this.f22841a, this.f22842b, this.f22843c, this.f22844d);
                    }
                    throw new IllegalArgumentException("Must set a non-null context to create the configuration.");
                }
                throw new IllegalArgumentException("Must set a callback to create the configuration.");
            }

            public a b(a aVar) {
                this.f22843c = aVar;
                return this;
            }

            public a c(String str) {
                this.f22842b = str;
                return this;
            }
        }

        b(Context context, String str, a aVar, boolean z4) {
            this.f22837a = context;
            this.f22838b = str;
            this.f22839c = aVar;
            this.f22840d = z4;
        }

        public static a a(Context context) {
            return new a(context);
        }
    }

    /* renamed from: t1.c$c  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface InterfaceC0207c {
        c a(b bVar);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close();

    String getDatabaseName();

    void setWriteAheadLoggingEnabled(boolean z4);

    t1.b v0();
}
