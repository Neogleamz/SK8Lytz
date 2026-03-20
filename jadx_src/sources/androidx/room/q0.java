package androidx.room;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class q0 implements t1.e, t1.d {

    /* renamed from: j  reason: collision with root package name */
    static final TreeMap<Integer, q0> f7171j = new TreeMap<>();

    /* renamed from: a  reason: collision with root package name */
    private volatile String f7172a;

    /* renamed from: b  reason: collision with root package name */
    final long[] f7173b;

    /* renamed from: c  reason: collision with root package name */
    final double[] f7174c;

    /* renamed from: d  reason: collision with root package name */
    final String[] f7175d;

    /* renamed from: e  reason: collision with root package name */
    final byte[][] f7176e;

    /* renamed from: f  reason: collision with root package name */
    private final int[] f7177f;

    /* renamed from: g  reason: collision with root package name */
    final int f7178g;

    /* renamed from: h  reason: collision with root package name */
    int f7179h;

    private q0(int i8) {
        this.f7178g = i8;
        int i9 = i8 + 1;
        this.f7177f = new int[i9];
        this.f7173b = new long[i9];
        this.f7174c = new double[i9];
        this.f7175d = new String[i9];
        this.f7176e = new byte[i9];
    }

    public static q0 c(String str, int i8) {
        TreeMap<Integer, q0> treeMap = f7171j;
        synchronized (treeMap) {
            Map.Entry<Integer, q0> ceilingEntry = treeMap.ceilingEntry(Integer.valueOf(i8));
            if (ceilingEntry == null) {
                q0 q0Var = new q0(i8);
                q0Var.d(str, i8);
                return q0Var;
            }
            treeMap.remove(ceilingEntry.getKey());
            q0 value = ceilingEntry.getValue();
            value.d(str, i8);
            return value;
        }
    }

    private static void f() {
        TreeMap<Integer, q0> treeMap = f7171j;
        if (treeMap.size() <= 15) {
            return;
        }
        int size = treeMap.size() - 10;
        Iterator<Integer> it = treeMap.descendingKeySet().iterator();
        while (true) {
            int i8 = size - 1;
            if (size <= 0) {
                return;
            }
            it.next();
            it.remove();
            size = i8;
        }
    }

    @Override // t1.d
    public void I(int i8, String str) {
        this.f7177f[i8] = 4;
        this.f7175d[i8] = str;
    }

    @Override // t1.d
    public void Q(int i8, double d8) {
        this.f7177f[i8] = 3;
        this.f7174c[i8] = d8;
    }

    @Override // t1.e
    public void a(t1.d dVar) {
        for (int i8 = 1; i8 <= this.f7179h; i8++) {
            int i9 = this.f7177f[i8];
            if (i9 == 1) {
                dVar.o1(i8);
            } else if (i9 == 2) {
                dVar.h0(i8, this.f7173b[i8]);
            } else if (i9 == 3) {
                dVar.Q(i8, this.f7174c[i8]);
            } else if (i9 == 4) {
                dVar.I(i8, this.f7175d[i8]);
            } else if (i9 == 5) {
                dVar.o0(i8, this.f7176e[i8]);
            }
        }
    }

    @Override // t1.e
    public String b() {
        return this.f7172a;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    void d(String str, int i8) {
        this.f7172a = str;
        this.f7179h = i8;
    }

    public void h() {
        TreeMap<Integer, q0> treeMap = f7171j;
        synchronized (treeMap) {
            treeMap.put(Integer.valueOf(this.f7178g), this);
            f();
        }
    }

    @Override // t1.d
    public void h0(int i8, long j8) {
        this.f7177f[i8] = 2;
        this.f7173b[i8] = j8;
    }

    @Override // t1.d
    public void o0(int i8, byte[] bArr) {
        this.f7177f[i8] = 5;
        this.f7176e[i8] = bArr;
    }

    @Override // t1.d
    public void o1(int i8) {
        this.f7177f[i8] = 1;
    }
}
