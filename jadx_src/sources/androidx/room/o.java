package androidx.room;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class o<T> extends t0 {
    public o(RoomDatabase roomDatabase) {
        super(roomDatabase);
    }

    protected abstract void bind(t1.f fVar, T t8);

    @Override // androidx.room.t0
    protected abstract String createQuery();

    public final int handle(T t8) {
        t1.f acquire = acquire();
        try {
            bind(acquire, t8);
            return acquire.N();
        } finally {
            release(acquire);
        }
    }

    public final int handleMultiple(Iterable<? extends T> iterable) {
        t1.f acquire = acquire();
        int i8 = 0;
        try {
            for (T t8 : iterable) {
                bind(acquire, t8);
                i8 += acquire.N();
            }
            return i8;
        } finally {
            release(acquire);
        }
    }

    public final int handleMultiple(T[] tArr) {
        t1.f acquire = acquire();
        try {
            int i8 = 0;
            for (T t8 : tArr) {
                bind(acquire, t8);
                i8 += acquire.N();
            }
            return i8;
        } finally {
            release(acquire);
        }
    }
}
