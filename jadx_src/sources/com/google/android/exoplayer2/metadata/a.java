package com.google.android.exoplayer2.metadata;

import a5.b;
import a5.c;
import a5.d;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import b6.l0;
import com.google.android.exoplayer2.f;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.w0;
import i4.f0;
import i4.s;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends f implements Handler.Callback {
    private boolean A;
    private long B;
    private Metadata C;
    private long E;

    /* renamed from: p  reason: collision with root package name */
    private final b f10051p;
    private final d q;

    /* renamed from: t  reason: collision with root package name */
    private final Handler f10052t;

    /* renamed from: w  reason: collision with root package name */
    private final c f10053w;

    /* renamed from: x  reason: collision with root package name */
    private final boolean f10054x;

    /* renamed from: y  reason: collision with root package name */
    private a5.a f10055y;

    /* renamed from: z  reason: collision with root package name */
    private boolean f10056z;

    public a(d dVar, Looper looper) {
        this(dVar, looper, b.f72a);
    }

    public a(d dVar, Looper looper, b bVar) {
        this(dVar, looper, bVar, false);
    }

    public a(d dVar, Looper looper, b bVar, boolean z4) {
        super(5);
        this.q = (d) b6.a.e(dVar);
        this.f10052t = looper == null ? null : l0.v(looper, this);
        this.f10051p = (b) b6.a.e(bVar);
        this.f10054x = z4;
        this.f10053w = new c();
        this.E = -9223372036854775807L;
    }

    private void Y(Metadata metadata, List<Metadata.Entry> list) {
        for (int i8 = 0; i8 < metadata.e(); i8++) {
            w0 V = metadata.d(i8).V();
            if (V == null || !this.f10051p.a(V)) {
                list.add(metadata.d(i8));
            } else {
                a5.a b9 = this.f10051p.b(V);
                byte[] bArr = (byte[]) b6.a.e(metadata.d(i8).M0());
                this.f10053w.k();
                this.f10053w.z(bArr.length);
                ((ByteBuffer) l0.j(this.f10053w.f9512c)).put(bArr);
                this.f10053w.A();
                Metadata a9 = b9.a(this.f10053w);
                if (a9 != null) {
                    Y(a9, list);
                }
            }
        }
    }

    private long Z(long j8) {
        b6.a.f(j8 != -9223372036854775807L);
        b6.a.f(this.E != -9223372036854775807L);
        return j8 - this.E;
    }

    private void a0(Metadata metadata) {
        Handler handler = this.f10052t;
        if (handler != null) {
            handler.obtainMessage(0, metadata).sendToTarget();
        } else {
            b0(metadata);
        }
    }

    private void b0(Metadata metadata) {
        this.q.h(metadata);
    }

    private boolean c0(long j8) {
        boolean z4;
        Metadata metadata = this.C;
        if (metadata == null || (!this.f10054x && metadata.f10050b > Z(j8))) {
            z4 = false;
        } else {
            a0(this.C);
            this.C = null;
            z4 = true;
        }
        if (this.f10056z && this.C == null) {
            this.A = true;
        }
        return z4;
    }

    private void d0() {
        if (this.f10056z || this.C != null) {
            return;
        }
        this.f10053w.k();
        s J = J();
        int V = V(J, this.f10053w, 0);
        if (V != -4) {
            if (V == -5) {
                this.B = ((w0) b6.a.e(J.f20512b)).f11210t;
            }
        } else if (this.f10053w.t()) {
            this.f10056z = true;
        } else {
            c cVar = this.f10053w;
            cVar.f73j = this.B;
            cVar.A();
            Metadata a9 = ((a5.a) l0.j(this.f10055y)).a(this.f10053w);
            if (a9 != null) {
                ArrayList arrayList = new ArrayList(a9.e());
                Y(a9, arrayList);
                if (arrayList.isEmpty()) {
                    return;
                }
                this.C = new Metadata(Z(this.f10053w.f9514e), arrayList);
            }
        }
    }

    @Override // com.google.android.exoplayer2.f
    protected void O() {
        this.C = null;
        this.f10055y = null;
        this.E = -9223372036854775807L;
    }

    @Override // com.google.android.exoplayer2.f
    protected void Q(long j8, boolean z4) {
        this.C = null;
        this.f10056z = false;
        this.A = false;
    }

    @Override // com.google.android.exoplayer2.f
    protected void U(w0[] w0VarArr, long j8, long j9) {
        this.f10055y = this.f10051p.b(w0VarArr[0]);
        Metadata metadata = this.C;
        if (metadata != null) {
            this.C = metadata.c((metadata.f10050b + this.E) - j9);
        }
        this.E = j9;
    }

    @Override // i4.f0
    public int a(w0 w0Var) {
        if (this.f10051p.a(w0Var)) {
            return f0.u(w0Var.R == 0 ? 4 : 2);
        }
        return f0.u(0);
    }

    @Override // com.google.android.exoplayer2.c2
    public boolean b() {
        return this.A;
    }

    @Override // com.google.android.exoplayer2.c2
    public boolean e() {
        return true;
    }

    @Override // com.google.android.exoplayer2.c2, i4.f0
    public String getName() {
        return "MetadataRenderer";
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (message.what == 0) {
            b0((Metadata) message.obj);
            return true;
        }
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer2.c2
    public void w(long j8, long j9) {
        boolean z4 = true;
        while (z4) {
            d0();
            z4 = c0(j8);
        }
    }
}
