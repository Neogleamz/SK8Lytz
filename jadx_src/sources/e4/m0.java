package e4;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.os.SystemClock;
import android.util.Base64;
import com.google.android.datatransport.Priority;
import com.google.android.datatransport.runtime.firebase.transport.LogEventDropped;
import com.google.android.datatransport.runtime.synchronization.SynchronizationException;
import f4.a;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import w3.i;
import w3.o;
import z3.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class m0 implements e4.d, f4.a, e4.c {

    /* renamed from: f  reason: collision with root package name */
    private static final u3.c f19770f = u3.c.b("proto");

    /* renamed from: a  reason: collision with root package name */
    private final t0 f19771a;

    /* renamed from: b  reason: collision with root package name */
    private final g4.a f19772b;

    /* renamed from: c  reason: collision with root package name */
    private final g4.a f19773c;

    /* renamed from: d  reason: collision with root package name */
    private final e f19774d;

    /* renamed from: e  reason: collision with root package name */
    private final bj.a<String> f19775e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b<T, U> {
        U apply(T t8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {

        /* renamed from: a  reason: collision with root package name */
        final String f19776a;

        /* renamed from: b  reason: collision with root package name */
        final String f19777b;

        private c(String str, String str2) {
            this.f19776a = str;
            this.f19777b = str2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d<T> {
        T a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public m0(g4.a aVar, g4.a aVar2, e eVar, t0 t0Var, bj.a<String> aVar3) {
        this.f19771a = t0Var;
        this.f19772b = aVar;
        this.f19773c = aVar2;
        this.f19774d = eVar;
        this.f19775e = aVar3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer D0(long j8, SQLiteDatabase sQLiteDatabase) {
        String[] strArr = {String.valueOf(j8)};
        w1(sQLiteDatabase.rawQuery("SELECT COUNT(*), transport_name FROM events WHERE timestamp_ms < ? GROUP BY transport_name", strArr), new f0(this));
        return Integer.valueOf(sQLiteDatabase.delete("events", "timestamp_ms < ?", strArr));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object E0(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.beginTransaction();
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object F0(Throwable th) {
        throw new SynchronizationException("Timed out while trying to acquire the lock.", th);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ SQLiteDatabase G0(Throwable th) {
        throw new SynchronizationException("Timed out while trying to open db.", th);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Long H0(Cursor cursor) {
        return Long.valueOf(cursor.moveToNext() ? cursor.getLong(0) : 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ z3.e I0(long j8, Cursor cursor) {
        cursor.moveToNext();
        return z3.e.c().c(cursor.getLong(0)).b(j8).a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ z3.e K0(long j8, SQLiteDatabase sQLiteDatabase) {
        return (z3.e) w1(sQLiteDatabase.rawQuery("SELECT last_metrics_upload_ms FROM global_log_event_state LIMIT 1", new String[0]), new l(j8));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Long L0(Cursor cursor) {
        if (cursor.moveToNext()) {
            return Long.valueOf(cursor.getLong(0));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean M0(o oVar, SQLiteDatabase sQLiteDatabase) {
        Long r02 = r0(sQLiteDatabase, oVar);
        return r02 == null ? Boolean.FALSE : (Boolean) w1(j0().rawQuery("SELECT 1 FROM events WHERE context_id = ? LIMIT 1", new String[]{r02.toString()}), y.a);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ List O0(SQLiteDatabase sQLiteDatabase) {
        return (List) w1(sQLiteDatabase.rawQuery("SELECT distinct t._id, t.backend_name, t.priority, t.extras FROM transport_contexts AS t, events AS e WHERE e.context_id = t._id", new String[0]), t.a);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ List P0(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        while (cursor.moveToNext()) {
            arrayList.add(o.a().b(cursor.getString(1)).d(h4.a.b(cursor.getInt(2))).c(m1(cursor.getString(3))).a());
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ List Q0(o oVar, SQLiteDatabase sQLiteDatabase) {
        Priority[] values;
        List<k> k12 = k1(sQLiteDatabase, oVar, this.f19774d.d());
        for (Priority priority : Priority.values()) {
            if (priority != oVar.d()) {
                int d8 = this.f19774d.d() - k12.size();
                if (d8 <= 0) {
                    break;
                }
                k12.addAll(k1(sQLiteDatabase, oVar.f(priority), d8));
            }
        }
        return y0(k12, l1(sQLiteDatabase, k12));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ z3.a R0(Map map, a.C0235a c0235a, Cursor cursor) {
        while (cursor.moveToNext()) {
            String string = cursor.getString(0);
            LogEventDropped.Reason a02 = a0(cursor.getInt(1));
            long j8 = cursor.getLong(2);
            if (!map.containsKey(string)) {
                map.put(string, new ArrayList());
            }
            ((List) map.get(string)).add(LogEventDropped.c().c(a02).b(j8).a());
        }
        n1(c0235a, map);
        c0235a.e(q0());
        c0235a.d(m0());
        c0235a.c((String) this.f19775e.get());
        return c0235a.b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ z3.a S0(String str, Map map, a.C0235a c0235a, SQLiteDatabase sQLiteDatabase) {
        return (z3.a) w1(sQLiteDatabase.rawQuery(str, new String[0]), new m(this, map, c0235a));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object T0(List list, o oVar, Cursor cursor) {
        while (cursor.moveToNext()) {
            long j8 = cursor.getLong(0);
            boolean z4 = cursor.getInt(7) != 0;
            i.a k8 = w3.i.a().j(cursor.getString(1)).i(cursor.getLong(2)).k(cursor.getLong(3));
            k8.h(z4 ? new w3.h(s1(cursor.getString(4)), cursor.getBlob(5)) : new w3.h(s1(cursor.getString(4)), p1(j8)));
            if (!cursor.isNull(6)) {
                k8.g(Integer.valueOf(cursor.getInt(6)));
            }
            list.add(k.a(j8, oVar, k8.d()));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object W0(Map map, Cursor cursor) {
        while (cursor.moveToNext()) {
            long j8 = cursor.getLong(0);
            Set set = (Set) map.get(Long.valueOf(j8));
            if (set == null) {
                set = new HashSet();
                map.put(Long.valueOf(j8), set);
            }
            set.add(new c(cursor.getString(1), cursor.getString(2)));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Long X0(w3.i iVar, o oVar, SQLiteDatabase sQLiteDatabase) {
        if (t0()) {
            d(1L, LogEventDropped.Reason.CACHE_FULL, iVar.j());
            return -1L;
        }
        long d02 = d0(sQLiteDatabase, oVar);
        int e8 = this.f19774d.e();
        byte[] a9 = iVar.e().a();
        boolean z4 = a9.length <= e8;
        ContentValues contentValues = new ContentValues();
        contentValues.put("context_id", Long.valueOf(d02));
        contentValues.put("transport_name", iVar.j());
        contentValues.put("timestamp_ms", Long.valueOf(iVar.f()));
        contentValues.put("uptime_ms", Long.valueOf(iVar.k()));
        contentValues.put("payload_encoding", iVar.e().b().a());
        contentValues.put("code", iVar.d());
        contentValues.put("num_attempts", (Integer) 0);
        contentValues.put("inline", Boolean.valueOf(z4));
        contentValues.put("payload", z4 ? a9 : new byte[0]);
        long insert = sQLiteDatabase.insert("events", null, contentValues);
        if (!z4) {
            int ceil = (int) Math.ceil(a9.length / e8);
            for (int i8 = 1; i8 <= ceil; i8++) {
                byte[] copyOfRange = Arrays.copyOfRange(a9, (i8 - 1) * e8, Math.min(i8 * e8, a9.length));
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("event_id", Long.valueOf(insert));
                contentValues2.put("sequence_num", Integer.valueOf(i8));
                contentValues2.put("bytes", copyOfRange);
                sQLiteDatabase.insert("event_payloads", null, contentValues2);
            }
        }
        for (Map.Entry<String, String> entry : iVar.i().entrySet()) {
            ContentValues contentValues3 = new ContentValues();
            contentValues3.put("event_id", Long.valueOf(insert));
            contentValues3.put("name", entry.getKey());
            contentValues3.put("value", entry.getValue());
            sQLiteDatabase.insert("event_metadata", null, contentValues3);
        }
        return Long.valueOf(insert);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ byte[] Z0(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        int i8 = 0;
        while (cursor.moveToNext()) {
            byte[] blob = cursor.getBlob(0);
            arrayList.add(blob);
            i8 += blob.length;
        }
        byte[] bArr = new byte[i8];
        int i9 = 0;
        for (int i10 = 0; i10 < arrayList.size(); i10++) {
            byte[] bArr2 = (byte[]) arrayList.get(i10);
            System.arraycopy(bArr2, 0, bArr, i9, bArr2.length);
            i9 += bArr2.length;
        }
        return bArr;
    }

    private LogEventDropped.Reason a0(int i8) {
        LogEventDropped.Reason reason = LogEventDropped.Reason.REASON_UNKNOWN;
        if (i8 == reason.c()) {
            return reason;
        }
        LogEventDropped.Reason reason2 = LogEventDropped.Reason.MESSAGE_TOO_OLD;
        if (i8 == reason2.c()) {
            return reason2;
        }
        LogEventDropped.Reason reason3 = LogEventDropped.Reason.CACHE_FULL;
        if (i8 == reason3.c()) {
            return reason3;
        }
        LogEventDropped.Reason reason4 = LogEventDropped.Reason.PAYLOAD_TOO_BIG;
        if (i8 == reason4.c()) {
            return reason4;
        }
        LogEventDropped.Reason reason5 = LogEventDropped.Reason.MAX_RETRIES_REACHED;
        if (i8 == reason5.c()) {
            return reason5;
        }
        LogEventDropped.Reason reason6 = LogEventDropped.Reason.INVALID_PAYLOD;
        if (i8 == reason6.c()) {
            return reason6;
        }
        LogEventDropped.Reason reason7 = LogEventDropped.Reason.SERVER_ERROR;
        if (i8 == reason7.c()) {
            return reason7;
        }
        a4.a.b("SQLiteEventStore", "%n is not valid. No matched LogEventDropped-Reason found. Treated it as REASON_UNKNOWN", Integer.valueOf(i8));
        return reason;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object a1(Cursor cursor) {
        while (cursor.moveToNext()) {
            int i8 = cursor.getInt(0);
            d(i8, LogEventDropped.Reason.MAX_RETRIES_REACHED, cursor.getString(1));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object b1(String str, String str2, SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.compileStatement(str).execute();
        w1(sQLiteDatabase.rawQuery(str2, null), new g0(this));
        sQLiteDatabase.compileStatement("DELETE FROM events WHERE num_attempts >= 16").execute();
        return null;
    }

    private void c0(SQLiteDatabase sQLiteDatabase) {
        r1(new c0(sQLiteDatabase), b0.a);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean c1(Cursor cursor) {
        return Boolean.valueOf(cursor.getCount() > 0);
    }

    private long d0(SQLiteDatabase sQLiteDatabase, o oVar) {
        Long r02 = r0(sQLiteDatabase, oVar);
        if (r02 != null) {
            return r02.longValue();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("backend_name", oVar.b());
        contentValues.put("priority", Integer.valueOf(h4.a.a(oVar.d())));
        contentValues.put("next_request_ms", (Integer) 0);
        if (oVar.c() != null) {
            contentValues.put("extras", Base64.encodeToString(oVar.c(), 0));
        }
        return sQLiteDatabase.insert("transport_contexts", null, contentValues);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object f1(String str, LogEventDropped.Reason reason, long j8, SQLiteDatabase sQLiteDatabase) {
        if (((Boolean) w1(sQLiteDatabase.rawQuery("SELECT 1 FROM log_event_dropped WHERE log_source = ? AND reason = ?", new String[]{str, Integer.toString(reason.c())}), x.a)).booleanValue()) {
            sQLiteDatabase.execSQL("UPDATE log_event_dropped SET events_dropped_count = events_dropped_count + " + j8 + " WHERE log_source = ? AND reason = ?", new String[]{str, Integer.toString(reason.c())});
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("log_source", str);
            contentValues.put("reason", Integer.valueOf(reason.c()));
            contentValues.put("events_dropped_count", Long.valueOf(j8));
            sQLiteDatabase.insert("log_event_dropped", null, contentValues);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object g1(long j8, o oVar, SQLiteDatabase sQLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("next_request_ms", Long.valueOf(j8));
        if (sQLiteDatabase.update("transport_contexts", contentValues, "backend_name = ? and priority = ?", new String[]{oVar.b(), String.valueOf(h4.a.a(oVar.d()))}) < 1) {
            contentValues.put("backend_name", oVar.b());
            contentValues.put("priority", Integer.valueOf(h4.a.a(oVar.d())));
            sQLiteDatabase.insert("transport_contexts", null, contentValues);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object i1(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.compileStatement("DELETE FROM log_event_dropped").execute();
        sQLiteDatabase.compileStatement("UPDATE global_log_event_state SET last_metrics_upload_ms=" + this.f19772b.a()).execute();
        return null;
    }

    private List<k> k1(SQLiteDatabase sQLiteDatabase, o oVar, int i8) {
        ArrayList arrayList = new ArrayList();
        Long r02 = r0(sQLiteDatabase, oVar);
        if (r02 == null) {
            return arrayList;
        }
        w1(sQLiteDatabase.query("events", new String[]{"_id", "transport_name", "timestamp_ms", "uptime_ms", "payload_encoding", "payload", "code", "inline"}, "context_id = ?", new String[]{r02.toString()}, null, null, null, String.valueOf(i8)), new l0(this, arrayList, oVar));
        return arrayList;
    }

    private Map<Long, Set<c>> l1(SQLiteDatabase sQLiteDatabase, List<k> list) {
        HashMap hashMap = new HashMap();
        StringBuilder sb = new StringBuilder("event_id IN (");
        for (int i8 = 0; i8 < list.size(); i8++) {
            sb.append(list.get(i8).c());
            if (i8 < list.size() - 1) {
                sb.append(',');
            }
        }
        sb.append(')');
        w1(sQLiteDatabase.query("event_metadata", new String[]{"event_id", "name", "value"}, sb.toString(), null, null, null, null), new r(hashMap));
        return hashMap;
    }

    private z3.b m0() {
        return z3.b.b().b(z3.d.c().b(g0()).c(e.f19765a.f()).a()).a();
    }

    private static byte[] m1(String str) {
        if (str == null) {
            return null;
        }
        return Base64.decode(str, 0);
    }

    private long n0() {
        return j0().compileStatement("PRAGMA page_count").simpleQueryForLong();
    }

    private void n1(a.C0235a c0235a, Map<String, List<LogEventDropped>> map) {
        for (Map.Entry<String, List<LogEventDropped>> entry : map.entrySet()) {
            c0235a.a(z3.c.c().c(entry.getKey()).b(entry.getValue()).a());
        }
    }

    private long p0() {
        return j0().compileStatement("PRAGMA page_size").simpleQueryForLong();
    }

    private byte[] p1(long j8) {
        return (byte[]) w1(j0().query("event_payloads", new String[]{"bytes"}, "event_id = ?", new String[]{String.valueOf(j8)}, null, null, "sequence_num"), s.a);
    }

    private z3.e q0() {
        return (z3.e) s0(new w(this.f19772b.a()));
    }

    private Long r0(SQLiteDatabase sQLiteDatabase, o oVar) {
        StringBuilder sb = new StringBuilder("backend_name = ? and priority = ?");
        ArrayList arrayList = new ArrayList(Arrays.asList(oVar.b(), String.valueOf(h4.a.a(oVar.d()))));
        if (oVar.c() != null) {
            sb.append(" and extras = ?");
            arrayList.add(Base64.encodeToString(oVar.c(), 0));
        } else {
            sb.append(" and extras is null");
        }
        return (Long) w1(sQLiteDatabase.query("transport_contexts", new String[]{"_id"}, sb.toString(), (String[]) arrayList.toArray(new String[0]), null, null, null), v.a);
    }

    private <T> T r1(d<T> dVar, b<Throwable, T> bVar) {
        long a9 = this.f19773c.a();
        while (true) {
            try {
                return dVar.a();
            } catch (SQLiteDatabaseLockedException e8) {
                if (this.f19773c.a() >= this.f19774d.b() + a9) {
                    return bVar.apply(e8);
                }
                SystemClock.sleep(50L);
            }
        }
    }

    private static u3.c s1(String str) {
        return str == null ? f19770f : u3.c.b(str);
    }

    private boolean t0() {
        return n0() * p0() >= this.f19774d.f();
    }

    private static String u1(Iterable<k> iterable) {
        StringBuilder sb = new StringBuilder("(");
        Iterator<k> it = iterable.iterator();
        while (it.hasNext()) {
            sb.append(it.next().c());
            if (it.hasNext()) {
                sb.append(',');
            }
        }
        sb.append(')');
        return sb.toString();
    }

    static <T> T w1(Cursor cursor, b<Cursor, T> bVar) {
        try {
            return bVar.apply(cursor);
        } finally {
            cursor.close();
        }
    }

    private List<k> y0(List<k> list, Map<Long, Set<c>> map) {
        ListIterator<k> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            k next = listIterator.next();
            if (map.containsKey(Long.valueOf(next.c()))) {
                i.a l8 = next.b().l();
                for (c cVar : map.get(Long.valueOf(next.c()))) {
                    l8.c(cVar.f19776a, cVar.f19777b);
                }
                listIterator.set(k.a(next.c(), next.d(), l8.d()));
            }
        }
        return list;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object z0(Cursor cursor) {
        while (cursor.moveToNext()) {
            int i8 = cursor.getInt(0);
            d(i8, LogEventDropped.Reason.MESSAGE_TOO_OLD, cursor.getString(1));
        }
        return null;
    }

    @Override // e4.d
    public Iterable<k> B0(o oVar) {
        return (Iterable) s0(new o(this, oVar));
    }

    @Override // e4.d
    public void D(Iterable<k> iterable) {
        if (iterable.iterator().hasNext()) {
            j0().compileStatement("DELETE FROM events WHERE _id in " + u1(iterable)).execute();
        }
    }

    @Override // e4.d
    public void E(o oVar, long j8) {
        s0(new e0(j8, oVar));
    }

    @Override // e4.d
    public void Q1(Iterable<k> iterable) {
        if (iterable.iterator().hasNext()) {
            s0(new j0(this, "UPDATE events SET num_attempts = num_attempts + 1 WHERE _id in " + u1(iterable), "SELECT COUNT(*), transport_name FROM events WHERE num_attempts >= 16 GROUP BY transport_name"));
        }
    }

    @Override // e4.d
    public long T(o oVar) {
        return ((Long) w1(j0().rawQuery("SELECT next_request_ms FROM transport_contexts WHERE backend_name = ? and priority = ?", new String[]{oVar.b(), String.valueOf(h4.a.a(oVar.d()))}), u.a)).longValue();
    }

    @Override // e4.d
    public Iterable<o> W() {
        return (Iterable) s0(z.a);
    }

    @Override // e4.c
    public void a() {
        s0(new h0(this));
    }

    @Override // f4.a
    public <T> T b(a.InterfaceC0170a<T> interfaceC0170a) {
        SQLiteDatabase j02 = j0();
        c0(j02);
        try {
            T h8 = interfaceC0170a.h();
            j02.setTransactionSuccessful();
            return h8;
        } finally {
            j02.endTransaction();
        }
    }

    @Override // e4.c
    public z3.a c() {
        return (z3.a) s0(new k0(this, "SELECT log_source, reason, events_dropped_count FROM log_event_dropped", new HashMap(), z3.a.e()));
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f19771a.close();
    }

    @Override // e4.c
    public void d(long j8, LogEventDropped.Reason reason, String str) {
        s0(new q(str, reason, j8));
    }

    long g0() {
        return n0() * p0();
    }

    SQLiteDatabase j0() {
        t0 t0Var = this.f19771a;
        Objects.requireNonNull(t0Var);
        return (SQLiteDatabase) r1(new d0(t0Var), a0.a);
    }

    @Override // e4.d
    public k k0(o oVar, w3.i iVar) {
        a4.a.c("SQLiteEventStore", "Storing event with priority=%s, name=%s for destination %s", oVar.d(), iVar.j(), oVar.b());
        long longValue = ((Long) s0(new n(this, iVar, oVar))).longValue();
        if (longValue < 1) {
            return null;
        }
        return k.a(longValue, oVar, iVar);
    }

    <T> T s0(b<SQLiteDatabase, T> bVar) {
        SQLiteDatabase j02 = j0();
        j02.beginTransaction();
        try {
            T apply = bVar.apply(j02);
            j02.setTransactionSuccessful();
            return apply;
        } finally {
            j02.endTransaction();
        }
    }

    @Override // e4.d
    public boolean v1(o oVar) {
        return ((Boolean) s0(new p(this, oVar))).booleanValue();
    }

    @Override // e4.d
    public int y() {
        return ((Integer) s0(new i0(this, this.f19772b.a() - this.f19774d.c()))).intValue();
    }
}
