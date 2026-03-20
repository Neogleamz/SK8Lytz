package androidx.room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.util.Log;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class u {

    /* renamed from: n  reason: collision with root package name */
    private static final String[] f7197n = {"UPDATE", "DELETE", "INSERT"};

    /* renamed from: b  reason: collision with root package name */
    final String[] f7199b;

    /* renamed from: c  reason: collision with root package name */
    private Map<String, Set<String>> f7200c;

    /* renamed from: e  reason: collision with root package name */
    final RoomDatabase f7202e;

    /* renamed from: h  reason: collision with root package name */
    volatile t1.f f7205h;

    /* renamed from: i  reason: collision with root package name */
    private b f7206i;

    /* renamed from: j  reason: collision with root package name */
    private final s f7207j;

    /* renamed from: l  reason: collision with root package name */
    private v f7209l;

    /* renamed from: d  reason: collision with root package name */
    androidx.room.a f7201d = null;

    /* renamed from: f  reason: collision with root package name */
    AtomicBoolean f7203f = new AtomicBoolean(false);

    /* renamed from: g  reason: collision with root package name */
    private volatile boolean f7204g = false;
    @SuppressLint({"RestrictedApi"})

    /* renamed from: k  reason: collision with root package name */
    final m.b<c, d> f7208k = new m.b<>();

    /* renamed from: m  reason: collision with root package name */
    Runnable f7210m = new a();

    /* renamed from: a  reason: collision with root package name */
    final HashMap<String, Integer> f7198a = new HashMap<>();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {
        a() {
        }

        private Set<Integer> a() {
            HashSet hashSet = new HashSet();
            Cursor query = u.this.f7202e.query(new t1.a("SELECT * FROM room_table_modification_log WHERE invalidated = 1;"));
            while (query.moveToNext()) {
                try {
                    hashSet.add(Integer.valueOf(query.getInt(0)));
                } catch (Throwable th) {
                    query.close();
                    throw th;
                }
            }
            query.close();
            if (!hashSet.isEmpty()) {
                u.this.f7205h.N();
            }
            return hashSet;
        }

        /* JADX WARN: Code restructure failed: missing block: B:31:0x007f, code lost:
            if (r0 != null) goto L33;
         */
        /* JADX WARN: Code restructure failed: missing block: B:32:0x0081, code lost:
            r0.b();
         */
        /* JADX WARN: Code restructure failed: missing block: B:40:0x0098, code lost:
            if (r0 == null) goto L34;
         */
        /* JADX WARN: Code restructure failed: missing block: B:42:0x009b, code lost:
            if (r1 == null) goto L53;
         */
        /* JADX WARN: Code restructure failed: missing block: B:44:0x00a1, code lost:
            if (r1.isEmpty() != false) goto L52;
         */
        /* JADX WARN: Code restructure failed: missing block: B:45:0x00a3, code lost:
            r0 = r5.f7211a.f7208k;
         */
        /* JADX WARN: Code restructure failed: missing block: B:46:0x00a7, code lost:
            monitor-enter(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:47:0x00a8, code lost:
            r2 = r5.f7211a.f7208k.iterator();
         */
        /* JADX WARN: Code restructure failed: missing block: B:49:0x00b4, code lost:
            if (r2.hasNext() == false) goto L44;
         */
        /* JADX WARN: Code restructure failed: missing block: B:50:0x00b6, code lost:
            r2.next().getValue().a(r1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:51:0x00c6, code lost:
            monitor-exit(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:56:0x00cb, code lost:
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:70:?, code lost:
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:71:?, code lost:
            return;
         */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            /*
                r5 = this;
                androidx.room.u r0 = androidx.room.u.this
                androidx.room.RoomDatabase r0 = r0.f7202e
                java.util.concurrent.locks.Lock r0 = r0.getCloseLock()
                r0.lock()
                r1 = 0
                androidx.room.u r2 = androidx.room.u.this     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                boolean r2 = r2.d()     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                if (r2 != 0) goto L21
                r0.unlock()
                androidx.room.u r0 = androidx.room.u.this
                androidx.room.a r0 = r0.f7201d
                if (r0 == 0) goto L20
                r0.b()
            L20:
                return
            L21:
                androidx.room.u r2 = androidx.room.u.this     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                java.util.concurrent.atomic.AtomicBoolean r2 = r2.f7203f     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                r3 = 1
                r4 = 0
                boolean r2 = r2.compareAndSet(r3, r4)     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                if (r2 != 0) goto L3a
                r0.unlock()
                androidx.room.u r0 = androidx.room.u.this
                androidx.room.a r0 = r0.f7201d
                if (r0 == 0) goto L39
                r0.b()
            L39:
                return
            L3a:
                androidx.room.u r2 = androidx.room.u.this     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                androidx.room.RoomDatabase r2 = r2.f7202e     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                boolean r2 = r2.inTransaction()     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                if (r2 == 0) goto L51
                r0.unlock()
                androidx.room.u r0 = androidx.room.u.this
                androidx.room.a r0 = r0.f7201d
                if (r0 == 0) goto L50
                r0.b()
            L50:
                return
            L51:
                androidx.room.u r2 = androidx.room.u.this     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                androidx.room.RoomDatabase r2 = r2.f7202e     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                boolean r3 = r2.mWriteAheadLoggingEnabled     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                if (r3 == 0) goto L74
                t1.c r2 = r2.getOpenHelper()     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                t1.b r2 = r2.v0()     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                r2.l0()     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                java.util.Set r1 = r5.a()     // Catch: java.lang.Throwable -> L6f
                r2.i0()     // Catch: java.lang.Throwable -> L6f
                r2.J0()     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                goto L78
            L6f:
                r3 = move-exception
                r2.J0()     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
                throw r3     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
            L74:
                java.util.Set r1 = r5.a()     // Catch: java.lang.Throwable -> L85 android.database.sqlite.SQLiteException -> L87 java.lang.IllegalStateException -> L89
            L78:
                r0.unlock()
                androidx.room.u r0 = androidx.room.u.this
                androidx.room.a r0 = r0.f7201d
                if (r0 == 0) goto L9b
            L81:
                r0.b()
                goto L9b
            L85:
                r1 = move-exception
                goto Lcc
            L87:
                r2 = move-exception
                goto L8a
            L89:
                r2 = move-exception
            L8a:
                java.lang.String r3 = "ROOM"
                java.lang.String r4 = "Cannot run invalidation tracker. Is the db closed?"
                android.util.Log.e(r3, r4, r2)     // Catch: java.lang.Throwable -> L85
                r0.unlock()
                androidx.room.u r0 = androidx.room.u.this
                androidx.room.a r0 = r0.f7201d
                if (r0 == 0) goto L9b
                goto L81
            L9b:
                if (r1 == 0) goto Lcb
                boolean r0 = r1.isEmpty()
                if (r0 != 0) goto Lcb
                androidx.room.u r0 = androidx.room.u.this
                m.b<androidx.room.u$c, androidx.room.u$d> r0 = r0.f7208k
                monitor-enter(r0)
                androidx.room.u r2 = androidx.room.u.this     // Catch: java.lang.Throwable -> Lc8
                m.b<androidx.room.u$c, androidx.room.u$d> r2 = r2.f7208k     // Catch: java.lang.Throwable -> Lc8
                java.util.Iterator r2 = r2.iterator()     // Catch: java.lang.Throwable -> Lc8
            Lb0:
                boolean r3 = r2.hasNext()     // Catch: java.lang.Throwable -> Lc8
                if (r3 == 0) goto Lc6
                java.lang.Object r3 = r2.next()     // Catch: java.lang.Throwable -> Lc8
                java.util.Map$Entry r3 = (java.util.Map.Entry) r3     // Catch: java.lang.Throwable -> Lc8
                java.lang.Object r3 = r3.getValue()     // Catch: java.lang.Throwable -> Lc8
                androidx.room.u$d r3 = (androidx.room.u.d) r3     // Catch: java.lang.Throwable -> Lc8
                r3.a(r1)     // Catch: java.lang.Throwable -> Lc8
                goto Lb0
            Lc6:
                monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc8
                goto Lcb
            Lc8:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc8
                throw r1
            Lcb:
                return
            Lcc:
                r0.unlock()
                androidx.room.u r0 = androidx.room.u.this
                androidx.room.a r0 = r0.f7201d
                if (r0 == 0) goto Ld8
                r0.b()
            Ld8:
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.room.u.a.run():void");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        final long[] f7212a;

        /* renamed from: b  reason: collision with root package name */
        final boolean[] f7213b;

        /* renamed from: c  reason: collision with root package name */
        final int[] f7214c;

        /* renamed from: d  reason: collision with root package name */
        boolean f7215d;

        /* renamed from: e  reason: collision with root package name */
        boolean f7216e;

        b(int i8) {
            long[] jArr = new long[i8];
            this.f7212a = jArr;
            boolean[] zArr = new boolean[i8];
            this.f7213b = zArr;
            this.f7214c = new int[i8];
            Arrays.fill(jArr, 0L);
            Arrays.fill(zArr, false);
        }

        int[] a() {
            synchronized (this) {
                if (this.f7215d && !this.f7216e) {
                    int length = this.f7212a.length;
                    int i8 = 0;
                    while (true) {
                        int i9 = 1;
                        if (i8 >= length) {
                            this.f7216e = true;
                            this.f7215d = false;
                            return this.f7214c;
                        }
                        boolean z4 = this.f7212a[i8] > 0;
                        boolean[] zArr = this.f7213b;
                        if (z4 != zArr[i8]) {
                            int[] iArr = this.f7214c;
                            if (!z4) {
                                i9 = 2;
                            }
                            iArr[i8] = i9;
                        } else {
                            this.f7214c[i8] = 0;
                        }
                        zArr[i8] = z4;
                        i8++;
                    }
                }
                return null;
            }
        }

        boolean b(int... iArr) {
            boolean z4;
            synchronized (this) {
                z4 = false;
                for (int i8 : iArr) {
                    long[] jArr = this.f7212a;
                    long j8 = jArr[i8];
                    jArr[i8] = 1 + j8;
                    if (j8 == 0) {
                        this.f7215d = true;
                        z4 = true;
                    }
                }
            }
            return z4;
        }

        boolean c(int... iArr) {
            boolean z4;
            synchronized (this) {
                z4 = false;
                for (int i8 : iArr) {
                    long[] jArr = this.f7212a;
                    long j8 = jArr[i8];
                    jArr[i8] = j8 - 1;
                    if (j8 == 1) {
                        this.f7215d = true;
                        z4 = true;
                    }
                }
            }
            return z4;
        }

        void d() {
            synchronized (this) {
                this.f7216e = false;
            }
        }

        void e() {
            synchronized (this) {
                Arrays.fill(this.f7213b, false);
                this.f7215d = true;
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class c {

        /* renamed from: a  reason: collision with root package name */
        final String[] f7217a;

        public c(String[] strArr) {
            this.f7217a = (String[]) Arrays.copyOf(strArr, strArr.length);
        }

        boolean a() {
            return false;
        }

        public abstract void b(Set<String> set);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d {

        /* renamed from: a  reason: collision with root package name */
        final int[] f7218a;

        /* renamed from: b  reason: collision with root package name */
        private final String[] f7219b;

        /* renamed from: c  reason: collision with root package name */
        final c f7220c;

        /* renamed from: d  reason: collision with root package name */
        private final Set<String> f7221d;

        d(c cVar, int[] iArr, String[] strArr) {
            Set<String> set;
            this.f7220c = cVar;
            this.f7218a = iArr;
            this.f7219b = strArr;
            if (iArr.length == 1) {
                HashSet hashSet = new HashSet();
                hashSet.add(strArr[0]);
                set = Collections.unmodifiableSet(hashSet);
            } else {
                set = null;
            }
            this.f7221d = set;
        }

        void a(Set<Integer> set) {
            int length = this.f7218a.length;
            Set<String> set2 = null;
            for (int i8 = 0; i8 < length; i8++) {
                if (set.contains(Integer.valueOf(this.f7218a[i8]))) {
                    if (length == 1) {
                        set2 = this.f7221d;
                    } else {
                        if (set2 == null) {
                            set2 = new HashSet<>(length);
                        }
                        set2.add(this.f7219b[i8]);
                    }
                }
            }
            if (set2 != null) {
                this.f7220c.b(set2);
            }
        }

        void b(String[] strArr) {
            Set<String> set = null;
            if (this.f7219b.length == 1) {
                int length = strArr.length;
                int i8 = 0;
                while (true) {
                    if (i8 >= length) {
                        break;
                    } else if (strArr[i8].equalsIgnoreCase(this.f7219b[0])) {
                        set = this.f7221d;
                        break;
                    } else {
                        i8++;
                    }
                }
            } else {
                HashSet hashSet = new HashSet();
                for (String str : strArr) {
                    String[] strArr2 = this.f7219b;
                    int length2 = strArr2.length;
                    int i9 = 0;
                    while (true) {
                        if (i9 < length2) {
                            String str2 = strArr2[i9];
                            if (str2.equalsIgnoreCase(str)) {
                                hashSet.add(str2);
                                break;
                            }
                            i9++;
                        }
                    }
                }
                if (hashSet.size() > 0) {
                    set = hashSet;
                }
            }
            if (set != null) {
                this.f7220c.b(set);
            }
        }
    }

    public u(RoomDatabase roomDatabase, Map<String, String> map, Map<String, Set<String>> map2, String... strArr) {
        this.f7202e = roomDatabase;
        this.f7206i = new b(strArr.length);
        this.f7200c = map2;
        this.f7207j = new s(roomDatabase);
        int length = strArr.length;
        this.f7199b = new String[length];
        for (int i8 = 0; i8 < length; i8++) {
            String str = strArr[i8];
            Locale locale = Locale.US;
            String lowerCase = str.toLowerCase(locale);
            this.f7198a.put(lowerCase, Integer.valueOf(i8));
            String str2 = map.get(strArr[i8]);
            if (str2 != null) {
                this.f7199b[i8] = str2.toLowerCase(locale);
            } else {
                this.f7199b[i8] = lowerCase;
            }
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Locale locale2 = Locale.US;
            String lowerCase2 = entry.getValue().toLowerCase(locale2);
            if (this.f7198a.containsKey(lowerCase2)) {
                String lowerCase3 = entry.getKey().toLowerCase(locale2);
                HashMap<String, Integer> hashMap = this.f7198a;
                hashMap.put(lowerCase3, hashMap.get(lowerCase2));
            }
        }
    }

    private static void b(StringBuilder sb, String str, String str2) {
        sb.append("`");
        sb.append("room_table_modification_trigger_");
        sb.append(str);
        sb.append("_");
        sb.append(str2);
        sb.append("`");
    }

    private static void c(t1.b bVar) {
        if (Build.VERSION.SDK_INT < 16 || !bVar.I1()) {
            bVar.x();
        } else {
            bVar.l0();
        }
    }

    private String[] j(String[] strArr) {
        HashSet hashSet = new HashSet();
        for (String str : strArr) {
            String lowerCase = str.toLowerCase(Locale.US);
            if (this.f7200c.containsKey(lowerCase)) {
                hashSet.addAll(this.f7200c.get(lowerCase));
            } else {
                hashSet.add(str);
            }
        }
        return (String[]) hashSet.toArray(new String[hashSet.size()]);
    }

    private void m(t1.b bVar, int i8) {
        String[] strArr;
        bVar.H("INSERT OR IGNORE INTO room_table_modification_log VALUES(" + i8 + ", 0)");
        String str = this.f7199b[i8];
        StringBuilder sb = new StringBuilder();
        for (String str2 : f7197n) {
            sb.setLength(0);
            sb.append("CREATE TEMP TRIGGER IF NOT EXISTS ");
            b(sb, str, str2);
            sb.append(" AFTER ");
            sb.append(str2);
            sb.append(" ON `");
            sb.append(str);
            sb.append("` BEGIN UPDATE ");
            sb.append("room_table_modification_log");
            sb.append(" SET ");
            sb.append("invalidated");
            sb.append(" = 1");
            sb.append(" WHERE ");
            sb.append("table_id");
            sb.append(" = ");
            sb.append(i8);
            sb.append(" AND ");
            sb.append("invalidated");
            sb.append(" = 0");
            sb.append("; END");
            bVar.H(sb.toString());
        }
    }

    private void o(t1.b bVar, int i8) {
        String[] strArr;
        String str = this.f7199b[i8];
        StringBuilder sb = new StringBuilder();
        for (String str2 : f7197n) {
            sb.setLength(0);
            sb.append("DROP TRIGGER IF EXISTS ");
            b(sb, str, str2);
            bVar.H(sb.toString());
        }
    }

    @SuppressLint({"RestrictedApi"})
    public void a(c cVar) {
        d n8;
        String[] j8 = j(cVar.f7217a);
        int[] iArr = new int[j8.length];
        int length = j8.length;
        for (int i8 = 0; i8 < length; i8++) {
            Integer num = this.f7198a.get(j8[i8].toLowerCase(Locale.US));
            if (num == null) {
                throw new IllegalArgumentException("There is no table with name " + j8[i8]);
            }
            iArr[i8] = num.intValue();
        }
        d dVar = new d(cVar, iArr, j8);
        synchronized (this.f7208k) {
            n8 = this.f7208k.n(cVar, dVar);
        }
        if (n8 == null && this.f7206i.b(iArr)) {
            p();
        }
    }

    boolean d() {
        if (this.f7202e.isOpen()) {
            if (!this.f7204g) {
                this.f7202e.getOpenHelper().v0();
            }
            if (this.f7204g) {
                return true;
            }
            Log.e("ROOM", "database is not initialized even though it is open");
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(t1.b bVar) {
        synchronized (this) {
            if (this.f7204g) {
                Log.e("ROOM", "Invalidation tracker is initialized twice :/.");
                return;
            }
            bVar.H("PRAGMA temp_store = MEMORY;");
            bVar.H("PRAGMA recursive_triggers='ON';");
            bVar.H("CREATE TEMP TABLE room_table_modification_log(table_id INTEGER PRIMARY KEY, invalidated INTEGER NOT NULL DEFAULT 0)");
            q(bVar);
            this.f7205h = bVar.O("UPDATE room_table_modification_log SET invalidated = 0 WHERE invalidated = 1 ");
            this.f7204g = true;
        }
    }

    public void f(String... strArr) {
        synchronized (this.f7208k) {
            Iterator<Map.Entry<c, d>> it = this.f7208k.iterator();
            while (it.hasNext()) {
                Map.Entry<c, d> next = it.next();
                if (!next.getKey().a()) {
                    next.getValue().b(strArr);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g() {
        synchronized (this) {
            this.f7204g = false;
            this.f7206i.e();
        }
    }

    public void h() {
        if (this.f7203f.compareAndSet(false, true)) {
            androidx.room.a aVar = this.f7201d;
            if (aVar != null) {
                aVar.e();
            }
            this.f7202e.getQueryExecutor().execute(this.f7210m);
        }
    }

    @SuppressLint({"RestrictedApi"})
    public void i(c cVar) {
        d p8;
        synchronized (this.f7208k) {
            p8 = this.f7208k.p(cVar);
        }
        if (p8 == null || !this.f7206i.c(p8.f7218a)) {
            return;
        }
        p();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(androidx.room.a aVar) {
        this.f7201d = aVar;
        aVar.h(new Runnable() { // from class: androidx.room.t
            @Override // java.lang.Runnable
            public final void run() {
                u.this.g();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(Context context, String str) {
        this.f7209l = new v(context, str, this, this.f7202e.getQueryExecutor());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void n() {
        v vVar = this.f7209l;
        if (vVar != null) {
            vVar.a();
            this.f7209l = null;
        }
    }

    void p() {
        if (this.f7202e.isOpen()) {
            q(this.f7202e.getOpenHelper().v0());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(t1.b bVar) {
        if (bVar.A1()) {
            return;
        }
        while (true) {
            try {
                Lock closeLock = this.f7202e.getCloseLock();
                closeLock.lock();
                try {
                    int[] a9 = this.f7206i.a();
                    if (a9 == null) {
                        return;
                    }
                    int length = a9.length;
                    c(bVar);
                    for (int i8 = 0; i8 < length; i8++) {
                        int i9 = a9[i8];
                        if (i9 == 1) {
                            m(bVar, i8);
                        } else if (i9 == 2) {
                            o(bVar, i8);
                        }
                    }
                    bVar.i0();
                    bVar.J0();
                    this.f7206i.d();
                } finally {
                    closeLock.unlock();
                }
            } catch (SQLiteException | IllegalStateException e8) {
                Log.e("ROOM", "Cannot run invalidation tracker. Is the db closed?", e8);
                return;
            }
        }
    }
}
