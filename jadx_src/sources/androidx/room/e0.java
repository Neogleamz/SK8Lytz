package androidx.room;

import android.database.Cursor;
import android.os.CancellationSignal;
import android.util.Pair;
import androidx.room.RoomDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e0 implements t1.b {

    /* renamed from: a  reason: collision with root package name */
    private final t1.b f7110a;

    /* renamed from: b  reason: collision with root package name */
    private final RoomDatabase.e f7111b;

    /* renamed from: c  reason: collision with root package name */
    private final Executor f7112c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e0(t1.b bVar, RoomDatabase.e eVar, Executor executor) {
        this.f7110a = bVar;
        this.f7111b = eVar;
        this.f7112c = executor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void l() {
        this.f7111b.a("BEGIN EXCLUSIVE TRANSACTION", Collections.emptyList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void m() {
        this.f7111b.a("BEGIN DEFERRED TRANSACTION", Collections.emptyList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void n() {
        this.f7111b.a("END TRANSACTION", Collections.emptyList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void o(String str) {
        this.f7111b.a(str, new ArrayList(0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void p(String str) {
        this.f7111b.a(str, Collections.emptyList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void r(t1.e eVar, h0 h0Var) {
        this.f7111b.a(eVar.b(), h0Var.a());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void s(t1.e eVar, h0 h0Var) {
        this.f7111b.a(eVar.b(), h0Var.a());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void t() {
        this.f7111b.a("TRANSACTION SUCCESSFUL", Collections.emptyList());
    }

    @Override // t1.b
    public boolean A1() {
        return this.f7110a.A1();
    }

    @Override // t1.b
    public Cursor C0(final t1.e eVar, CancellationSignal cancellationSignal) {
        final h0 h0Var = new h0();
        eVar.a(h0Var);
        this.f7112c.execute(new Runnable() { // from class: androidx.room.d0
            @Override // java.lang.Runnable
            public final void run() {
                e0.this.s(eVar, h0Var);
            }
        });
        return this.f7110a.X(eVar);
    }

    @Override // t1.b
    public List<Pair<String, String>> F() {
        return this.f7110a.F();
    }

    @Override // t1.b
    public void H(final String str) {
        this.f7112c.execute(new Runnable() { // from class: androidx.room.a0
            @Override // java.lang.Runnable
            public final void run() {
                e0.this.o(str);
            }
        });
        this.f7110a.H(str);
    }

    @Override // t1.b
    public boolean I1() {
        return this.f7110a.I1();
    }

    @Override // t1.b
    public void J0() {
        this.f7112c.execute(new Runnable() { // from class: androidx.room.w
            @Override // java.lang.Runnable
            public final void run() {
                e0.this.n();
            }
        });
        this.f7110a.J0();
    }

    @Override // t1.b
    public t1.f O(String str) {
        return new k0(this.f7110a.O(str), this.f7111b, str, this.f7112c);
    }

    @Override // t1.b
    public Cursor X(final t1.e eVar) {
        final h0 h0Var = new h0();
        eVar.a(h0Var);
        this.f7112c.execute(new Runnable() { // from class: androidx.room.c0
            @Override // java.lang.Runnable
            public final void run() {
                e0.this.r(eVar, h0Var);
            }
        });
        return this.f7110a.X(eVar);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f7110a.close();
    }

    @Override // t1.b
    public void i0() {
        this.f7112c.execute(new Runnable() { // from class: androidx.room.y
            @Override // java.lang.Runnable
            public final void run() {
                e0.this.t();
            }
        });
        this.f7110a.i0();
    }

    @Override // t1.b
    public boolean isOpen() {
        return this.f7110a.isOpen();
    }

    @Override // t1.b
    public void l0() {
        this.f7112c.execute(new Runnable() { // from class: androidx.room.x
            @Override // java.lang.Runnable
            public final void run() {
                e0.this.m();
            }
        });
        this.f7110a.l0();
    }

    @Override // t1.b
    public Cursor w0(final String str) {
        this.f7112c.execute(new Runnable() { // from class: androidx.room.b0
            @Override // java.lang.Runnable
            public final void run() {
                e0.this.p(str);
            }
        });
        return this.f7110a.w0(str);
    }

    @Override // t1.b
    public void x() {
        this.f7112c.execute(new Runnable() { // from class: androidx.room.z
            @Override // java.lang.Runnable
            public final void run() {
                e0.this.l();
            }
        });
        this.f7110a.x();
    }

    @Override // t1.b
    public String y1() {
        return this.f7110a.y1();
    }
}
