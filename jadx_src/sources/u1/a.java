package u1;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.os.CancellationSignal;
import android.util.Pair;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.List;
import t1.f;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class a implements t1.b {

    /* renamed from: b  reason: collision with root package name */
    private static final String[] f22963b = {BuildConfig.FLAVOR, " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};

    /* renamed from: c  reason: collision with root package name */
    private static final String[] f22964c = new String[0];

    /* renamed from: a  reason: collision with root package name */
    private final SQLiteDatabase f22965a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: u1.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class C0211a implements SQLiteDatabase.CursorFactory {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ t1.e f22966a;

        C0211a(t1.e eVar) {
            this.f22966a = eVar;
        }

        @Override // android.database.sqlite.SQLiteDatabase.CursorFactory
        public Cursor newCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery) {
            this.f22966a.a(new d(sQLiteQuery));
            return new SQLiteCursor(sQLiteCursorDriver, str, sQLiteQuery);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements SQLiteDatabase.CursorFactory {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ t1.e f22968a;

        b(t1.e eVar) {
            this.f22968a = eVar;
        }

        @Override // android.database.sqlite.SQLiteDatabase.CursorFactory
        public Cursor newCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery) {
            this.f22968a.a(new d(sQLiteQuery));
            return new SQLiteCursor(sQLiteCursorDriver, str, sQLiteQuery);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(SQLiteDatabase sQLiteDatabase) {
        this.f22965a = sQLiteDatabase;
    }

    @Override // t1.b
    public boolean A1() {
        return this.f22965a.inTransaction();
    }

    @Override // t1.b
    public Cursor C0(t1.e eVar, CancellationSignal cancellationSignal) {
        return this.f22965a.rawQueryWithFactory(new b(eVar), eVar.b(), f22964c, null, cancellationSignal);
    }

    @Override // t1.b
    public List<Pair<String, String>> F() {
        return this.f22965a.getAttachedDbs();
    }

    @Override // t1.b
    public void H(String str) {
        this.f22965a.execSQL(str);
    }

    @Override // t1.b
    public boolean I1() {
        return this.f22965a.isWriteAheadLoggingEnabled();
    }

    @Override // t1.b
    public void J0() {
        this.f22965a.endTransaction();
    }

    @Override // t1.b
    public f O(String str) {
        return new e(this.f22965a.compileStatement(str));
    }

    @Override // t1.b
    public Cursor X(t1.e eVar) {
        return this.f22965a.rawQueryWithFactory(new C0211a(eVar), eVar.b(), f22964c, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(SQLiteDatabase sQLiteDatabase) {
        return this.f22965a == sQLiteDatabase;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f22965a.close();
    }

    @Override // t1.b
    public void i0() {
        this.f22965a.setTransactionSuccessful();
    }

    @Override // t1.b
    public boolean isOpen() {
        return this.f22965a.isOpen();
    }

    @Override // t1.b
    public void l0() {
        this.f22965a.beginTransactionNonExclusive();
    }

    @Override // t1.b
    public Cursor w0(String str) {
        return X(new t1.a(str));
    }

    @Override // t1.b
    public void x() {
        this.f22965a.beginTransaction();
    }

    @Override // t1.b
    public String y1() {
        return this.f22965a.getPath();
    }
}
