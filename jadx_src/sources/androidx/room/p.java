package androidx.room;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class p<T> extends t0 {
    public p(RoomDatabase roomDatabase) {
        super(roomDatabase);
    }

    protected abstract void bind(t1.f fVar, T t8);

    public final void insert(Iterable<? extends T> iterable) {
        t1.f acquire = acquire();
        try {
            for (T t8 : iterable) {
                bind(acquire, t8);
                acquire.W1();
            }
        } finally {
            release(acquire);
        }
    }

    public final void insert(T t8) {
        t1.f acquire = acquire();
        try {
            bind(acquire, t8);
            acquire.W1();
        } finally {
            release(acquire);
        }
    }

    public final void insert(T[] tArr) {
        t1.f acquire = acquire();
        try {
            for (T t8 : tArr) {
                bind(acquire, t8);
                acquire.W1();
            }
        } finally {
            release(acquire);
        }
    }

    public final long insertAndReturnId(T t8) {
        t1.f acquire = acquire();
        try {
            bind(acquire, t8);
            return acquire.W1();
        } finally {
            release(acquire);
        }
    }

    public final long[] insertAndReturnIdsArray(Collection<? extends T> collection) {
        t1.f acquire = acquire();
        try {
            long[] jArr = new long[collection.size()];
            int i8 = 0;
            for (T t8 : collection) {
                bind(acquire, t8);
                jArr[i8] = acquire.W1();
                i8++;
            }
            return jArr;
        } finally {
            release(acquire);
        }
    }

    public final long[] insertAndReturnIdsArray(T[] tArr) {
        t1.f acquire = acquire();
        try {
            long[] jArr = new long[tArr.length];
            int i8 = 0;
            for (T t8 : tArr) {
                bind(acquire, t8);
                jArr[i8] = acquire.W1();
                i8++;
            }
            return jArr;
        } finally {
            release(acquire);
        }
    }

    public final Long[] insertAndReturnIdsArrayBox(Collection<? extends T> collection) {
        t1.f acquire = acquire();
        try {
            Long[] lArr = new Long[collection.size()];
            int i8 = 0;
            for (T t8 : collection) {
                bind(acquire, t8);
                lArr[i8] = Long.valueOf(acquire.W1());
                i8++;
            }
            return lArr;
        } finally {
            release(acquire);
        }
    }

    public final Long[] insertAndReturnIdsArrayBox(T[] tArr) {
        t1.f acquire = acquire();
        try {
            Long[] lArr = new Long[tArr.length];
            int i8 = 0;
            for (T t8 : tArr) {
                bind(acquire, t8);
                lArr[i8] = Long.valueOf(acquire.W1());
                i8++;
            }
            return lArr;
        } finally {
            release(acquire);
        }
    }

    public final List<Long> insertAndReturnIdsList(Collection<? extends T> collection) {
        t1.f acquire = acquire();
        try {
            ArrayList arrayList = new ArrayList(collection.size());
            int i8 = 0;
            for (T t8 : collection) {
                bind(acquire, t8);
                arrayList.add(i8, Long.valueOf(acquire.W1()));
                i8++;
            }
            return arrayList;
        } finally {
            release(acquire);
        }
    }

    public final List<Long> insertAndReturnIdsList(T[] tArr) {
        t1.f acquire = acquire();
        try {
            ArrayList arrayList = new ArrayList(tArr.length);
            int i8 = 0;
            for (T t8 : tArr) {
                bind(acquire, t8);
                arrayList.add(i8, Long.valueOf(acquire.W1()));
                i8++;
            }
            return arrayList;
        } finally {
            release(acquire);
        }
    }
}
