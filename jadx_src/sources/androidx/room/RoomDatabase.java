package androidx.room;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Looper;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import t1.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class RoomDatabase {
    private static final String DB_IMPL_SUFFIX = "_Impl";
    public static final int MAX_BIND_PARAMETER_CNT = 999;
    private boolean mAllowMainThreadQueries;
    private androidx.room.a mAutoCloser;
    @Deprecated
    protected List<b> mCallbacks;
    @Deprecated
    protected volatile t1.b mDatabase;
    private t1.c mOpenHelper;
    private Executor mQueryExecutor;
    private Executor mTransactionExecutor;
    boolean mWriteAheadLoggingEnabled;
    private final ReentrantReadWriteLock mCloseLock = new ReentrantReadWriteLock();
    private final ThreadLocal<Integer> mSuspendingTransactionId = new ThreadLocal<>();
    private final Map<String, Object> mBackingFieldMap = Collections.synchronizedMap(new HashMap());
    private final u mInvalidationTracker = createInvalidationTracker();
    private final Map<Class<?>, Object> mTypeConverters = new HashMap();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum JournalMode {
        AUTOMATIC,
        TRUNCATE,
        WRITE_AHEAD_LOGGING;

        private static boolean c(ActivityManager activityManager) {
            if (Build.VERSION.SDK_INT >= 19) {
                return activityManager.isLowRamDevice();
            }
            return false;
        }

        @SuppressLint({"NewApi"})
        JournalMode f(Context context) {
            ActivityManager activityManager;
            return this != AUTOMATIC ? this : (Build.VERSION.SDK_INT < 16 || (activityManager = (ActivityManager) context.getSystemService("activity")) == null || c(activityManager)) ? TRUNCATE : WRITE_AHEAD_LOGGING;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a<T extends RoomDatabase> {

        /* renamed from: a  reason: collision with root package name */
        private final Class<T> f7058a;

        /* renamed from: b  reason: collision with root package name */
        private final String f7059b;

        /* renamed from: c  reason: collision with root package name */
        private final Context f7060c;

        /* renamed from: d  reason: collision with root package name */
        private ArrayList<b> f7061d;

        /* renamed from: e  reason: collision with root package name */
        private d f7062e;

        /* renamed from: f  reason: collision with root package name */
        private e f7063f;

        /* renamed from: g  reason: collision with root package name */
        private Executor f7064g;

        /* renamed from: h  reason: collision with root package name */
        private List<Object> f7065h;

        /* renamed from: i  reason: collision with root package name */
        private Executor f7066i;

        /* renamed from: j  reason: collision with root package name */
        private Executor f7067j;

        /* renamed from: k  reason: collision with root package name */
        private c.InterfaceC0207c f7068k;

        /* renamed from: l  reason: collision with root package name */
        private boolean f7069l;

        /* renamed from: n  reason: collision with root package name */
        private boolean f7071n;

        /* renamed from: p  reason: collision with root package name */
        private boolean f7073p;

        /* renamed from: r  reason: collision with root package name */
        private TimeUnit f7074r;

        /* renamed from: t  reason: collision with root package name */
        private Set<Integer> f7076t;

        /* renamed from: u  reason: collision with root package name */
        private Set<Integer> f7077u;

        /* renamed from: v  reason: collision with root package name */
        private String f7078v;

        /* renamed from: w  reason: collision with root package name */
        private File f7079w;

        /* renamed from: x  reason: collision with root package name */
        private Callable<InputStream> f7080x;
        private long q = -1;

        /* renamed from: m  reason: collision with root package name */
        private JournalMode f7070m = JournalMode.AUTOMATIC;

        /* renamed from: o  reason: collision with root package name */
        private boolean f7072o = true;

        /* renamed from: s  reason: collision with root package name */
        private final c f7075s = new c();

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(Context context, Class<T> cls, String str) {
            this.f7060c = context;
            this.f7058a = cls;
            this.f7059b = str;
        }

        /* JADX WARN: Code restructure failed: missing block: B:18:0x0028, code lost:
            if (r1 != null) goto L10;
         */
        /* JADX WARN: Removed duplicated region for block: B:27:0x003d  */
        /* JADX WARN: Removed duplicated region for block: B:34:0x0067  */
        /* JADX WARN: Removed duplicated region for block: B:37:0x0074  */
        /* JADX WARN: Removed duplicated region for block: B:50:0x00a0  */
        /* JADX WARN: Removed duplicated region for block: B:73:0x011f  */
        @android.annotation.SuppressLint({"RestrictedApi"})
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public T a() {
            /*
                Method dump skipped, instructions count: 311
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.room.RoomDatabase.a.a():androidx.room.RoomDatabase");
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class b {
        public void a(t1.b bVar) {
        }

        public void b(t1.b bVar) {
        }

        public void c(t1.b bVar) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {

        /* renamed from: a  reason: collision with root package name */
        private HashMap<Integer, TreeMap<Integer, q1.a>> f7081a = new HashMap<>();

        /* JADX WARN: Removed duplicated region for block: B:31:0x0016 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:9:0x0017  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private java.util.List<q1.a> b(java.util.List<q1.a> r7, boolean r8, int r9, int r10) {
            /*
                r6 = this;
            L0:
                if (r8 == 0) goto L5
                if (r9 >= r10) goto L5a
                goto L7
            L5:
                if (r9 <= r10) goto L5a
            L7:
                java.util.HashMap<java.lang.Integer, java.util.TreeMap<java.lang.Integer, q1.a>> r0 = r6.f7081a
                java.lang.Integer r1 = java.lang.Integer.valueOf(r9)
                java.lang.Object r0 = r0.get(r1)
                java.util.TreeMap r0 = (java.util.TreeMap) r0
                r1 = 0
                if (r0 != 0) goto L17
                return r1
            L17:
                if (r8 == 0) goto L1e
                java.util.NavigableSet r2 = r0.descendingKeySet()
                goto L22
            L1e:
                java.util.Set r2 = r0.keySet()
            L22:
                java.util.Iterator r2 = r2.iterator()
            L26:
                boolean r3 = r2.hasNext()
                r4 = 1
                r5 = 0
                if (r3 == 0) goto L56
                java.lang.Object r3 = r2.next()
                java.lang.Integer r3 = (java.lang.Integer) r3
                int r3 = r3.intValue()
                if (r8 == 0) goto L40
                if (r3 > r10) goto L45
                if (r3 <= r9) goto L45
            L3e:
                r5 = r4
                goto L45
            L40:
                if (r3 < r10) goto L45
                if (r3 >= r9) goto L45
                goto L3e
            L45:
                if (r5 == 0) goto L26
                java.lang.Integer r9 = java.lang.Integer.valueOf(r3)
                java.lang.Object r9 = r0.get(r9)
                q1.a r9 = (q1.a) r9
                r7.add(r9)
                r9 = r3
                goto L57
            L56:
                r4 = r5
            L57:
                if (r4 != 0) goto L0
                return r1
            L5a:
                return r7
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.room.RoomDatabase.c.b(java.util.List, boolean, int, int):java.util.List");
        }

        public List<q1.a> a(int i8, int i9) {
            if (i8 == i9) {
                return Collections.emptyList();
            }
            return b(new ArrayList(), i9 > i8, i8, i9);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class d {
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface e {
        void a(String str, List<Object> list);
    }

    private void internalBeginTransaction() {
        assertNotMainThread();
        t1.b v02 = this.mOpenHelper.v0();
        this.mInvalidationTracker.q(v02);
        if (Build.VERSION.SDK_INT < 16 || !v02.I1()) {
            v02.x();
        } else {
            v02.l0();
        }
    }

    private void internalEndTransaction() {
        this.mOpenHelper.v0().J0();
        if (inTransaction()) {
            return;
        }
        this.mInvalidationTracker.h();
    }

    private static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object lambda$beginTransaction$0(t1.b bVar) {
        internalBeginTransaction();
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object lambda$endTransaction$1(t1.b bVar) {
        internalEndTransaction();
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> T unwrapOpenHelper(Class<T> cls, t1.c cVar) {
        if (cls.isInstance(cVar)) {
            return cVar;
        }
        if (cVar instanceof n) {
            return (T) unwrapOpenHelper(cls, ((n) cVar).a());
        }
        return null;
    }

    public void assertNotMainThread() {
        if (!this.mAllowMainThreadQueries && isMainThread()) {
            throw new IllegalStateException("Cannot access database on the main thread since it may potentially lock the UI for a long period of time.");
        }
    }

    public void assertNotSuspendingTransaction() {
        if (!inTransaction() && this.mSuspendingTransactionId.get() != null) {
            throw new IllegalStateException("Cannot access database on a different coroutine context inherited from a suspending transaction.");
        }
    }

    @Deprecated
    public void beginTransaction() {
        assertNotMainThread();
        androidx.room.a aVar = this.mAutoCloser;
        if (aVar == null) {
            internalBeginTransaction();
        } else {
            aVar.c(new n.a() { // from class: androidx.room.m0
                @Override // n.a
                public final Object apply(Object obj) {
                    Object lambda$beginTransaction$0;
                    lambda$beginTransaction$0 = RoomDatabase.this.lambda$beginTransaction$0((t1.b) obj);
                    return lambda$beginTransaction$0;
                }
            });
        }
    }

    public abstract void clearAllTables();

    public void close() {
        if (isOpen()) {
            ReentrantReadWriteLock.WriteLock writeLock = this.mCloseLock.writeLock();
            writeLock.lock();
            try {
                this.mInvalidationTracker.n();
                this.mOpenHelper.close();
            } finally {
                writeLock.unlock();
            }
        }
    }

    public t1.f compileStatement(String str) {
        assertNotMainThread();
        assertNotSuspendingTransaction();
        return this.mOpenHelper.v0().O(str);
    }

    protected abstract u createInvalidationTracker();

    protected abstract t1.c createOpenHelper(m mVar);

    @Deprecated
    public void endTransaction() {
        androidx.room.a aVar = this.mAutoCloser;
        if (aVar == null) {
            internalEndTransaction();
        } else {
            aVar.c(new n.a() { // from class: androidx.room.n0
                @Override // n.a
                public final Object apply(Object obj) {
                    Object lambda$endTransaction$1;
                    lambda$endTransaction$1 = RoomDatabase.this.lambda$endTransaction$1((t1.b) obj);
                    return lambda$endTransaction$1;
                }
            });
        }
    }

    Map<String, Object> getBackingFieldMap() {
        return this.mBackingFieldMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Lock getCloseLock() {
        return this.mCloseLock.readLock();
    }

    public u getInvalidationTracker() {
        return this.mInvalidationTracker;
    }

    public t1.c getOpenHelper() {
        return this.mOpenHelper;
    }

    public Executor getQueryExecutor() {
        return this.mQueryExecutor;
    }

    protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
        return Collections.emptyMap();
    }

    ThreadLocal<Integer> getSuspendingTransactionId() {
        return this.mSuspendingTransactionId;
    }

    public Executor getTransactionExecutor() {
        return this.mTransactionExecutor;
    }

    public <T> T getTypeConverter(Class<T> cls) {
        return (T) this.mTypeConverters.get(cls);
    }

    public boolean inTransaction() {
        return this.mOpenHelper.v0().A1();
    }

    public void init(m mVar) {
        t1.c createOpenHelper = createOpenHelper(mVar);
        this.mOpenHelper = createOpenHelper;
        r0 r0Var = (r0) unwrapOpenHelper(r0.class, createOpenHelper);
        if (r0Var != null) {
            r0Var.d(mVar);
        }
        h hVar = (h) unwrapOpenHelper(h.class, this.mOpenHelper);
        if (hVar != null) {
            androidx.room.a b9 = hVar.b();
            this.mAutoCloser = b9;
            this.mInvalidationTracker.k(b9);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            r2 = mVar.f7152i == JournalMode.WRITE_AHEAD_LOGGING;
            this.mOpenHelper.setWriteAheadLoggingEnabled(r2);
        }
        this.mCallbacks = mVar.f7148e;
        this.mQueryExecutor = mVar.f7153j;
        this.mTransactionExecutor = new u0(mVar.f7154k);
        this.mAllowMainThreadQueries = mVar.f7151h;
        this.mWriteAheadLoggingEnabled = r2;
        if (mVar.f7155l) {
            this.mInvalidationTracker.l(mVar.f7145b, mVar.f7146c);
        }
        Map<Class<?>, List<Class<?>>> requiredTypeConverters = getRequiredTypeConverters();
        BitSet bitSet = new BitSet();
        for (Map.Entry<Class<?>, List<Class<?>>> entry : requiredTypeConverters.entrySet()) {
            Class<?> key = entry.getKey();
            for (Class<?> cls : entry.getValue()) {
                int size = mVar.f7150g.size() - 1;
                while (true) {
                    if (size < 0) {
                        size = -1;
                        break;
                    } else if (cls.isAssignableFrom(mVar.f7150g.get(size).getClass())) {
                        bitSet.set(size);
                        break;
                    } else {
                        size--;
                    }
                }
                if (size < 0) {
                    throw new IllegalArgumentException("A required type converter (" + cls + ") for " + key.getCanonicalName() + " is missing in the database configuration.");
                }
                this.mTypeConverters.put(cls, mVar.f7150g.get(size));
            }
        }
        for (int size2 = mVar.f7150g.size() - 1; size2 >= 0; size2--) {
            if (!bitSet.get(size2)) {
                throw new IllegalArgumentException("Unexpected type converter " + mVar.f7150g.get(size2) + ". Annotate TypeConverter class with @ProvidedTypeConverter annotation or remove this converter from the builder.");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void internalInitInvalidationTracker(t1.b bVar) {
        this.mInvalidationTracker.e(bVar);
    }

    public boolean isOpen() {
        androidx.room.a aVar = this.mAutoCloser;
        if (aVar != null) {
            return aVar.g();
        }
        t1.b bVar = this.mDatabase;
        return bVar != null && bVar.isOpen();
    }

    public Cursor query(String str, Object[] objArr) {
        return this.mOpenHelper.v0().X(new t1.a(str, objArr));
    }

    public Cursor query(t1.e eVar) {
        return query(eVar, (CancellationSignal) null);
    }

    public Cursor query(t1.e eVar, CancellationSignal cancellationSignal) {
        assertNotMainThread();
        assertNotSuspendingTransaction();
        return (cancellationSignal == null || Build.VERSION.SDK_INT < 16) ? this.mOpenHelper.v0().X(eVar) : this.mOpenHelper.v0().C0(eVar, cancellationSignal);
    }

    public <V> V runInTransaction(Callable<V> callable) {
        beginTransaction();
        try {
            try {
                V call = callable.call();
                setTransactionSuccessful();
                endTransaction();
                return call;
            } catch (RuntimeException e8) {
                throw e8;
            } catch (Exception e9) {
                r1.e.a(e9);
                endTransaction();
                return null;
            }
        } catch (Throwable th) {
            endTransaction();
            throw th;
        }
    }

    public void runInTransaction(Runnable runnable) {
        beginTransaction();
        try {
            runnable.run();
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    @Deprecated
    public void setTransactionSuccessful() {
        this.mOpenHelper.v0().i0();
    }
}
