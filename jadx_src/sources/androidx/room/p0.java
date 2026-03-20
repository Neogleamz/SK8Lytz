package androidx.room;

import android.database.Cursor;
import java.util.List;
import t1.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class p0 extends c.a {

    /* renamed from: b  reason: collision with root package name */
    private m f7163b;

    /* renamed from: c  reason: collision with root package name */
    private final a f7164c;

    /* renamed from: d  reason: collision with root package name */
    private final String f7165d;

    /* renamed from: e  reason: collision with root package name */
    private final String f7166e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {
        public final int version;

        public a(int i8) {
            this.version = i8;
        }

        protected abstract void createAllTables(t1.b bVar);

        protected abstract void dropAllTables(t1.b bVar);

        protected abstract void onCreate(t1.b bVar);

        protected abstract void onOpen(t1.b bVar);

        protected void onPostMigrate(t1.b bVar) {
        }

        protected void onPreMigrate(t1.b bVar) {
        }

        protected b onValidateSchema(t1.b bVar) {
            validateMigration(bVar);
            return new b(true, null);
        }

        @Deprecated
        protected void validateMigration(t1.b bVar) {
            throw new UnsupportedOperationException("validateMigration is deprecated");
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        public final boolean f7167a;

        /* renamed from: b  reason: collision with root package name */
        public final String f7168b;

        public b(boolean z4, String str) {
            this.f7167a = z4;
            this.f7168b = str;
        }
    }

    public p0(m mVar, a aVar, String str, String str2) {
        super(aVar.version);
        this.f7163b = mVar;
        this.f7164c = aVar;
        this.f7165d = str;
        this.f7166e = str2;
    }

    private void h(t1.b bVar) {
        if (!k(bVar)) {
            b onValidateSchema = this.f7164c.onValidateSchema(bVar);
            if (onValidateSchema.f7167a) {
                this.f7164c.onPostMigrate(bVar);
                l(bVar);
                return;
            }
            throw new IllegalStateException("Pre-packaged database has an invalid schema: " + onValidateSchema.f7168b);
        }
        Cursor X = bVar.X(new t1.a("SELECT identity_hash FROM room_master_table WHERE id = 42 LIMIT 1"));
        try {
            String string = X.moveToFirst() ? X.getString(0) : null;
            X.close();
            if (!this.f7165d.equals(string) && !this.f7166e.equals(string)) {
                throw new IllegalStateException("Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number.");
            }
        } catch (Throwable th) {
            X.close();
            throw th;
        }
    }

    private void i(t1.b bVar) {
        bVar.H("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
    }

    private static boolean j(t1.b bVar) {
        Cursor w02 = bVar.w0("SELECT count(*) FROM sqlite_master WHERE name != 'android_metadata'");
        try {
            boolean z4 = false;
            if (w02.moveToFirst()) {
                if (w02.getInt(0) == 0) {
                    z4 = true;
                }
            }
            return z4;
        } finally {
            w02.close();
        }
    }

    private static boolean k(t1.b bVar) {
        Cursor w02 = bVar.w0("SELECT 1 FROM sqlite_master WHERE type = 'table' AND name='room_master_table'");
        try {
            boolean z4 = false;
            if (w02.moveToFirst()) {
                if (w02.getInt(0) != 0) {
                    z4 = true;
                }
            }
            return z4;
        } finally {
            w02.close();
        }
    }

    private void l(t1.b bVar) {
        i(bVar);
        bVar.H(o0.a(this.f7165d));
    }

    @Override // t1.c.a
    public void b(t1.b bVar) {
        super.b(bVar);
    }

    @Override // t1.c.a
    public void d(t1.b bVar) {
        boolean j8 = j(bVar);
        this.f7164c.createAllTables(bVar);
        if (!j8) {
            b onValidateSchema = this.f7164c.onValidateSchema(bVar);
            if (!onValidateSchema.f7167a) {
                throw new IllegalStateException("Pre-packaged database has an invalid schema: " + onValidateSchema.f7168b);
            }
        }
        l(bVar);
        this.f7164c.onCreate(bVar);
    }

    @Override // t1.c.a
    public void e(t1.b bVar, int i8, int i9) {
        g(bVar, i8, i9);
    }

    @Override // t1.c.a
    public void f(t1.b bVar) {
        super.f(bVar);
        h(bVar);
        this.f7164c.onOpen(bVar);
        this.f7163b = null;
    }

    @Override // t1.c.a
    public void g(t1.b bVar, int i8, int i9) {
        boolean z4;
        List<q1.a> a9;
        m mVar = this.f7163b;
        if (mVar == null || (a9 = mVar.f7147d.a(i8, i9)) == null) {
            z4 = false;
        } else {
            this.f7164c.onPreMigrate(bVar);
            for (q1.a aVar : a9) {
                aVar.a(bVar);
            }
            b onValidateSchema = this.f7164c.onValidateSchema(bVar);
            if (!onValidateSchema.f7167a) {
                throw new IllegalStateException("Migration didn't properly handle: " + onValidateSchema.f7168b);
            }
            this.f7164c.onPostMigrate(bVar);
            l(bVar);
            z4 = true;
        }
        if (z4) {
            return;
        }
        m mVar2 = this.f7163b;
        if (mVar2 != null && !mVar2.a(i8, i9)) {
            this.f7164c.dropAllTables(bVar);
            this.f7164c.createAllTables(bVar);
            return;
        }
        throw new IllegalStateException("A migration from " + i8 + " to " + i9 + " was required but not found. Please provide the necessary Migration path via RoomDatabase.Builder.addMigration(Migration ...) or allow for destructive migrations via one of the RoomDatabase.Builder.fallbackToDestructiveMigration* methods.");
    }
}
