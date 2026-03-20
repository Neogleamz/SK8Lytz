package k0;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e<K, V> {

    /* renamed from: a  reason: collision with root package name */
    private final LinkedHashMap<K, V> f20874a;

    /* renamed from: b  reason: collision with root package name */
    private int f20875b;

    /* renamed from: c  reason: collision with root package name */
    private int f20876c;

    /* renamed from: d  reason: collision with root package name */
    private int f20877d;

    /* renamed from: e  reason: collision with root package name */
    private int f20878e;

    /* renamed from: f  reason: collision with root package name */
    private int f20879f;

    /* renamed from: g  reason: collision with root package name */
    private int f20880g;

    /* renamed from: h  reason: collision with root package name */
    private int f20881h;

    public e(int i8) {
        if (i8 <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.f20876c = i8;
        this.f20874a = new LinkedHashMap<>(0, 0.75f, true);
    }

    private int f(K k8, V v8) {
        int g8 = g(k8, v8);
        if (g8 >= 0) {
            return g8;
        }
        throw new IllegalStateException("Negative size: " + k8 + "=" + v8);
    }

    protected V a(K k8) {
        return null;
    }

    protected void b(boolean z4, K k8, V v8, V v9) {
    }

    public final V c(K k8) {
        V put;
        Objects.requireNonNull(k8, "key == null");
        synchronized (this) {
            V v8 = this.f20874a.get(k8);
            if (v8 != null) {
                this.f20880g++;
                return v8;
            }
            this.f20881h++;
            V a9 = a(k8);
            if (a9 == null) {
                return null;
            }
            synchronized (this) {
                this.f20878e++;
                put = this.f20874a.put(k8, a9);
                if (put != null) {
                    this.f20874a.put(k8, put);
                } else {
                    this.f20875b += f(k8, a9);
                }
            }
            if (put != null) {
                b(false, k8, a9, put);
                return put;
            }
            i(this.f20876c);
            return a9;
        }
    }

    public final V d(K k8, V v8) {
        V put;
        if (k8 == null || v8 == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            this.f20877d++;
            this.f20875b += f(k8, v8);
            put = this.f20874a.put(k8, v8);
            if (put != null) {
                this.f20875b -= f(k8, put);
            }
        }
        if (put != null) {
            b(false, k8, put, v8);
        }
        i(this.f20876c);
        return put;
    }

    public final V e(K k8) {
        V remove;
        Objects.requireNonNull(k8, "key == null");
        synchronized (this) {
            remove = this.f20874a.remove(k8);
            if (remove != null) {
                this.f20875b -= f(k8, remove);
            }
        }
        if (remove != null) {
            b(false, k8, remove, null);
        }
        return remove;
    }

    protected int g(K k8, V v8) {
        return 1;
    }

    public final synchronized Map<K, V> h() {
        return new LinkedHashMap(this.f20874a);
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0070, code lost:
        throw new java.lang.IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void i(int r5) {
        /*
            r4 = this;
        L0:
            monitor-enter(r4)
            int r0 = r4.f20875b     // Catch: java.lang.Throwable -> L71
            if (r0 < 0) goto L52
            java.util.LinkedHashMap<K, V> r0 = r4.f20874a     // Catch: java.lang.Throwable -> L71
            boolean r0 = r0.isEmpty()     // Catch: java.lang.Throwable -> L71
            if (r0 == 0) goto L11
            int r0 = r4.f20875b     // Catch: java.lang.Throwable -> L71
            if (r0 != 0) goto L52
        L11:
            int r0 = r4.f20875b     // Catch: java.lang.Throwable -> L71
            if (r0 <= r5) goto L50
            java.util.LinkedHashMap<K, V> r0 = r4.f20874a     // Catch: java.lang.Throwable -> L71
            boolean r0 = r0.isEmpty()     // Catch: java.lang.Throwable -> L71
            if (r0 == 0) goto L1e
            goto L50
        L1e:
            java.util.LinkedHashMap<K, V> r0 = r4.f20874a     // Catch: java.lang.Throwable -> L71
            java.util.Set r0 = r0.entrySet()     // Catch: java.lang.Throwable -> L71
            java.util.Iterator r0 = r0.iterator()     // Catch: java.lang.Throwable -> L71
            java.lang.Object r0 = r0.next()     // Catch: java.lang.Throwable -> L71
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0     // Catch: java.lang.Throwable -> L71
            java.lang.Object r1 = r0.getKey()     // Catch: java.lang.Throwable -> L71
            java.lang.Object r0 = r0.getValue()     // Catch: java.lang.Throwable -> L71
            java.util.LinkedHashMap<K, V> r2 = r4.f20874a     // Catch: java.lang.Throwable -> L71
            r2.remove(r1)     // Catch: java.lang.Throwable -> L71
            int r2 = r4.f20875b     // Catch: java.lang.Throwable -> L71
            int r3 = r4.f(r1, r0)     // Catch: java.lang.Throwable -> L71
            int r2 = r2 - r3
            r4.f20875b = r2     // Catch: java.lang.Throwable -> L71
            int r2 = r4.f20879f     // Catch: java.lang.Throwable -> L71
            r3 = 1
            int r2 = r2 + r3
            r4.f20879f = r2     // Catch: java.lang.Throwable -> L71
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L71
            r2 = 0
            r4.b(r3, r1, r0, r2)
            goto L0
        L50:
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L71
            return
        L52:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException     // Catch: java.lang.Throwable -> L71
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L71
            r0.<init>()     // Catch: java.lang.Throwable -> L71
            java.lang.Class r1 = r4.getClass()     // Catch: java.lang.Throwable -> L71
            java.lang.String r1 = r1.getName()     // Catch: java.lang.Throwable -> L71
            r0.append(r1)     // Catch: java.lang.Throwable -> L71
            java.lang.String r1 = ".sizeOf() is reporting inconsistent results!"
            r0.append(r1)     // Catch: java.lang.Throwable -> L71
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L71
            r5.<init>(r0)     // Catch: java.lang.Throwable -> L71
            throw r5     // Catch: java.lang.Throwable -> L71
        L71:
            r5 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L71
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: k0.e.i(int):void");
    }

    public final synchronized String toString() {
        int i8;
        int i9;
        i8 = this.f20880g;
        i9 = this.f20881h + i8;
        return String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", Integer.valueOf(this.f20876c), Integer.valueOf(this.f20880g), Integer.valueOf(this.f20881h), Integer.valueOf(i9 != 0 ? (i8 * 100) / i9 : 0));
    }
}
