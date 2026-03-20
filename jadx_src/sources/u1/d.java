package u1;

import android.database.sqlite.SQLiteProgram;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class d implements t1.d {

    /* renamed from: a  reason: collision with root package name */
    private final SQLiteProgram f22982a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(SQLiteProgram sQLiteProgram) {
        this.f22982a = sQLiteProgram;
    }

    @Override // t1.d
    public void I(int i8, String str) {
        this.f22982a.bindString(i8, str);
    }

    @Override // t1.d
    public void Q(int i8, double d8) {
        this.f22982a.bindDouble(i8, d8);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f22982a.close();
    }

    @Override // t1.d
    public void h0(int i8, long j8) {
        this.f22982a.bindLong(i8, j8);
    }

    @Override // t1.d
    public void o0(int i8, byte[] bArr) {
        this.f22982a.bindBlob(i8, bArr);
    }

    @Override // t1.d
    public void o1(int i8) {
        this.f22982a.bindNull(i8);
    }
}
