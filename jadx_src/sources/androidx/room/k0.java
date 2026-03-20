package androidx.room;

import androidx.room.RoomDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k0 implements t1.f {

    /* renamed from: a  reason: collision with root package name */
    private final t1.f f7137a;

    /* renamed from: b  reason: collision with root package name */
    private final RoomDatabase.e f7138b;

    /* renamed from: c  reason: collision with root package name */
    private final String f7139c;

    /* renamed from: d  reason: collision with root package name */
    private final List<Object> f7140d = new ArrayList();

    /* renamed from: e  reason: collision with root package name */
    private final Executor f7141e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k0(t1.f fVar, RoomDatabase.e eVar, String str, Executor executor) {
        this.f7137a = fVar;
        this.f7138b = eVar;
        this.f7139c = str;
        this.f7141e = executor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void c() {
        this.f7138b.a(this.f7139c, this.f7140d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d() {
        this.f7138b.a(this.f7139c, this.f7140d);
    }

    private void f(int i8, Object obj) {
        int i9 = i8 - 1;
        if (i9 >= this.f7140d.size()) {
            for (int size = this.f7140d.size(); size <= i9; size++) {
                this.f7140d.add(null);
            }
        }
        this.f7140d.set(i9, obj);
    }

    @Override // t1.d
    public void I(int i8, String str) {
        f(i8, str);
        this.f7137a.I(i8, str);
    }

    @Override // t1.f
    public int N() {
        this.f7141e.execute(new Runnable() { // from class: androidx.room.j0
            @Override // java.lang.Runnable
            public final void run() {
                k0.this.d();
            }
        });
        return this.f7137a.N();
    }

    @Override // t1.d
    public void Q(int i8, double d8) {
        f(i8, Double.valueOf(d8));
        this.f7137a.Q(i8, d8);
    }

    @Override // t1.f
    public long W1() {
        this.f7141e.execute(new Runnable() { // from class: androidx.room.i0
            @Override // java.lang.Runnable
            public final void run() {
                k0.this.c();
            }
        });
        return this.f7137a.W1();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f7137a.close();
    }

    @Override // t1.d
    public void h0(int i8, long j8) {
        f(i8, Long.valueOf(j8));
        this.f7137a.h0(i8, j8);
    }

    @Override // t1.d
    public void o0(int i8, byte[] bArr) {
        f(i8, bArr);
        this.f7137a.o0(i8, bArr);
    }

    @Override // t1.d
    public void o1(int i8) {
        f(i8, this.f7140d.toArray());
        this.f7137a.o1(i8);
    }
}
