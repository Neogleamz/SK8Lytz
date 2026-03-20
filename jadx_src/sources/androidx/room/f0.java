package androidx.room;

import androidx.room.RoomDatabase;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f0 implements t1.c, n {

    /* renamed from: a  reason: collision with root package name */
    private final t1.c f7114a;

    /* renamed from: b  reason: collision with root package name */
    private final RoomDatabase.e f7115b;

    /* renamed from: c  reason: collision with root package name */
    private final Executor f7116c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f0(t1.c cVar, RoomDatabase.e eVar, Executor executor) {
        this.f7114a = cVar;
        this.f7115b = eVar;
        this.f7116c = executor;
    }

    @Override // androidx.room.n
    public t1.c a() {
        return this.f7114a;
    }

    @Override // t1.c, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f7114a.close();
    }

    @Override // t1.c
    public String getDatabaseName() {
        return this.f7114a.getDatabaseName();
    }

    @Override // t1.c
    public void setWriteAheadLoggingEnabled(boolean z4) {
        this.f7114a.setWriteAheadLoggingEnabled(z4);
    }

    @Override // t1.c
    public t1.b v0() {
        return new e0(this.f7114a.v0(), this.f7115b, this.f7116c);
    }
}
