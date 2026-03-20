package androidx.room;

import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class t0 {
    private final RoomDatabase mDatabase;
    private final AtomicBoolean mLock = new AtomicBoolean(false);
    private volatile t1.f mStmt;

    public t0(RoomDatabase roomDatabase) {
        this.mDatabase = roomDatabase;
    }

    private t1.f createNewStatement() {
        return this.mDatabase.compileStatement(createQuery());
    }

    private t1.f getStmt(boolean z4) {
        if (z4) {
            if (this.mStmt == null) {
                this.mStmt = createNewStatement();
            }
            return this.mStmt;
        }
        return createNewStatement();
    }

    public t1.f acquire() {
        assertNotMainThread();
        return getStmt(this.mLock.compareAndSet(false, true));
    }

    protected void assertNotMainThread() {
        this.mDatabase.assertNotMainThread();
    }

    protected abstract String createQuery();

    public void release(t1.f fVar) {
        if (fVar == this.mStmt) {
            this.mLock.set(false);
        }
    }
}
