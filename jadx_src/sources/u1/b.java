package u1;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import java.io.File;
import t1.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class b implements t1.c {

    /* renamed from: a  reason: collision with root package name */
    private final Context f22970a;

    /* renamed from: b  reason: collision with root package name */
    private final String f22971b;

    /* renamed from: c  reason: collision with root package name */
    private final c.a f22972c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean f22973d;

    /* renamed from: e  reason: collision with root package name */
    private final Object f22974e = new Object();

    /* renamed from: f  reason: collision with root package name */
    private a f22975f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f22976g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a extends SQLiteOpenHelper {

        /* renamed from: a  reason: collision with root package name */
        final u1.a[] f22977a;

        /* renamed from: b  reason: collision with root package name */
        final c.a f22978b;

        /* renamed from: c  reason: collision with root package name */
        private boolean f22979c;

        /* renamed from: u1.b$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class C0212a implements DatabaseErrorHandler {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ c.a f22980a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ u1.a[] f22981b;

            C0212a(c.a aVar, u1.a[] aVarArr) {
                this.f22980a = aVar;
                this.f22981b = aVarArr;
            }

            @Override // android.database.DatabaseErrorHandler
            public void onCorruption(SQLiteDatabase sQLiteDatabase) {
                this.f22980a.c(a.b(this.f22981b, sQLiteDatabase));
            }
        }

        a(Context context, String str, u1.a[] aVarArr, c.a aVar) {
            super(context, str, null, aVar.f22836a, new C0212a(aVar, aVarArr));
            this.f22978b = aVar;
            this.f22977a = aVarArr;
        }

        static u1.a b(u1.a[] aVarArr, SQLiteDatabase sQLiteDatabase) {
            u1.a aVar = aVarArr[0];
            if (aVar == null || !aVar.a(sQLiteDatabase)) {
                aVarArr[0] = new u1.a(sQLiteDatabase);
            }
            return aVarArr[0];
        }

        u1.a a(SQLiteDatabase sQLiteDatabase) {
            return b(this.f22977a, sQLiteDatabase);
        }

        synchronized t1.b c() {
            this.f22979c = false;
            SQLiteDatabase writableDatabase = super.getWritableDatabase();
            if (!this.f22979c) {
                return a(writableDatabase);
            }
            close();
            return c();
        }

        @Override // android.database.sqlite.SQLiteOpenHelper, java.lang.AutoCloseable
        public synchronized void close() {
            super.close();
            this.f22977a[0] = null;
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onConfigure(SQLiteDatabase sQLiteDatabase) {
            this.f22978b.b(a(sQLiteDatabase));
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            this.f22978b.d(a(sQLiteDatabase));
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i8, int i9) {
            this.f22979c = true;
            this.f22978b.e(a(sQLiteDatabase), i8, i9);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onOpen(SQLiteDatabase sQLiteDatabase) {
            if (this.f22979c) {
                return;
            }
            this.f22978b.f(a(sQLiteDatabase));
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i8, int i9) {
            this.f22979c = true;
            this.f22978b.g(a(sQLiteDatabase), i8, i9);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(Context context, String str, c.a aVar, boolean z4) {
        this.f22970a = context;
        this.f22971b = str;
        this.f22972c = aVar;
        this.f22973d = z4;
    }

    private a a() {
        a aVar;
        synchronized (this.f22974e) {
            if (this.f22975f == null) {
                u1.a[] aVarArr = new u1.a[1];
                int i8 = Build.VERSION.SDK_INT;
                if (i8 < 23 || this.f22971b == null || !this.f22973d) {
                    this.f22975f = new a(this.f22970a, this.f22971b, aVarArr, this.f22972c);
                } else {
                    this.f22975f = new a(this.f22970a, new File(this.f22970a.getNoBackupFilesDir(), this.f22971b).getAbsolutePath(), aVarArr, this.f22972c);
                }
                if (i8 >= 16) {
                    this.f22975f.setWriteAheadLoggingEnabled(this.f22976g);
                }
            }
            aVar = this.f22975f;
        }
        return aVar;
    }

    @Override // t1.c, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        a().close();
    }

    @Override // t1.c
    public String getDatabaseName() {
        return this.f22971b;
    }

    @Override // t1.c
    public void setWriteAheadLoggingEnabled(boolean z4) {
        synchronized (this.f22974e) {
            a aVar = this.f22975f;
            if (aVar != null) {
                aVar.setWriteAheadLoggingEnabled(z4);
            }
            this.f22976g = z4;
        }
    }

    @Override // t1.c
    public t1.b v0() {
        return a().c();
    }
}
