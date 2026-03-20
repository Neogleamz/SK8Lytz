package u1;

import android.database.sqlite.SQLiteStatement;
import t1.f;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class e extends d implements f {

    /* renamed from: b  reason: collision with root package name */
    private final SQLiteStatement f22983b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(SQLiteStatement sQLiteStatement) {
        super(sQLiteStatement);
        this.f22983b = sQLiteStatement;
    }

    @Override // t1.f
    public int N() {
        return this.f22983b.executeUpdateDelete();
    }

    @Override // t1.f
    public long W1() {
        return this.f22983b.executeInsert();
    }
}
