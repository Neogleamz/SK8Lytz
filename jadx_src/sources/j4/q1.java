package j4;

import android.util.Base64;
import b6.l0;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.source.k;
import com.google.common.base.r;
import j4.b;
import j4.s1;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q1 implements s1 {

    /* renamed from: h  reason: collision with root package name */
    public static final r<String> f20664h = p1.a;

    /* renamed from: i  reason: collision with root package name */
    private static final Random f20665i = new Random();

    /* renamed from: a  reason: collision with root package name */
    private final h2.d f20666a;

    /* renamed from: b  reason: collision with root package name */
    private final h2.b f20667b;

    /* renamed from: c  reason: collision with root package name */
    private final HashMap<String, a> f20668c;

    /* renamed from: d  reason: collision with root package name */
    private final r<String> f20669d;

    /* renamed from: e  reason: collision with root package name */
    private s1.a f20670e;

    /* renamed from: f  reason: collision with root package name */
    private h2 f20671f;

    /* renamed from: g  reason: collision with root package name */
    private String f20672g;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class a {

        /* renamed from: a  reason: collision with root package name */
        private final String f20673a;

        /* renamed from: b  reason: collision with root package name */
        private int f20674b;

        /* renamed from: c  reason: collision with root package name */
        private long f20675c;

        /* renamed from: d  reason: collision with root package name */
        private k.b f20676d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f20677e;

        /* renamed from: f  reason: collision with root package name */
        private boolean f20678f;

        public a(String str, int i8, k.b bVar) {
            this.f20673a = str;
            this.f20674b = i8;
            this.f20675c = bVar == null ? -1L : bVar.f20289d;
            if (bVar == null || !bVar.b()) {
                return;
            }
            this.f20676d = bVar;
        }

        private int l(h2 h2Var, h2 h2Var2, int i8) {
            if (i8 >= h2Var.t()) {
                if (i8 < h2Var2.t()) {
                    return i8;
                }
                return -1;
            }
            h2Var.r(i8, q1.this.f20666a);
            for (int i9 = q1.this.f20666a.q; i9 <= q1.this.f20666a.f9784t; i9++) {
                int f5 = h2Var2.f(h2Var.q(i9));
                if (f5 != -1) {
                    return h2Var2.j(f5, q1.this.f20667b).f9758c;
                }
            }
            return -1;
        }

        public boolean i(int i8, k.b bVar) {
            if (bVar == null) {
                return i8 == this.f20674b;
            }
            k.b bVar2 = this.f20676d;
            return bVar2 == null ? !bVar.b() && bVar.f20289d == this.f20675c : bVar.f20289d == bVar2.f20289d && bVar.f20287b == bVar2.f20287b && bVar.f20288c == bVar2.f20288c;
        }

        public boolean j(b.a aVar) {
            k.b bVar = aVar.f20640d;
            if (bVar == null) {
                return this.f20674b != aVar.f20639c;
            }
            long j8 = this.f20675c;
            if (j8 == -1) {
                return false;
            }
            if (bVar.f20289d > j8) {
                return true;
            }
            if (this.f20676d == null) {
                return false;
            }
            int f5 = aVar.f20638b.f(bVar.f20286a);
            int f8 = aVar.f20638b.f(this.f20676d.f20286a);
            k.b bVar2 = aVar.f20640d;
            if (bVar2.f20289d < this.f20676d.f20289d || f5 < f8) {
                return false;
            }
            if (f5 > f8) {
                return true;
            }
            boolean b9 = bVar2.b();
            k.b bVar3 = aVar.f20640d;
            if (!b9) {
                int i8 = bVar3.f20290e;
                return i8 == -1 || i8 > this.f20676d.f20287b;
            }
            int i9 = bVar3.f20287b;
            int i10 = bVar3.f20288c;
            k.b bVar4 = this.f20676d;
            int i11 = bVar4.f20287b;
            if (i9 <= i11) {
                return i9 == i11 && i10 > bVar4.f20288c;
            }
            return true;
        }

        public void k(int i8, k.b bVar) {
            if (this.f20675c == -1 && i8 == this.f20674b && bVar != null) {
                this.f20675c = bVar.f20289d;
            }
        }

        public boolean m(h2 h2Var, h2 h2Var2) {
            int l8 = l(h2Var, h2Var2, this.f20674b);
            this.f20674b = l8;
            if (l8 == -1) {
                return false;
            }
            k.b bVar = this.f20676d;
            return bVar == null || h2Var2.f(bVar.f20286a) != -1;
        }
    }

    public q1() {
        this(f20664h);
    }

    public q1(r<String> rVar) {
        this.f20669d = rVar;
        this.f20666a = new h2.d();
        this.f20667b = new h2.b();
        this.f20668c = new HashMap<>();
        this.f20671f = h2.f9745a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String k() {
        byte[] bArr = new byte[12];
        f20665i.nextBytes(bArr);
        return Base64.encodeToString(bArr, 10);
    }

    private a l(int i8, k.b bVar) {
        int i9;
        a aVar = null;
        long j8 = Long.MAX_VALUE;
        for (a aVar2 : this.f20668c.values()) {
            aVar2.k(i8, bVar);
            if (aVar2.i(i8, bVar)) {
                long j9 = aVar2.f20675c;
                if (j9 == -1 || j9 < j8) {
                    aVar = aVar2;
                    j8 = j9;
                } else if (i9 == 0 && ((a) l0.j(aVar)).f20676d != null && aVar2.f20676d != null) {
                    aVar = aVar2;
                }
            }
        }
        if (aVar == null) {
            String str = this.f20669d.get();
            a aVar3 = new a(str, i8, bVar);
            this.f20668c.put(str, aVar3);
            return aVar3;
        }
        return aVar;
    }

    private void m(b.a aVar) {
        if (aVar.f20638b.u()) {
            this.f20672g = null;
            return;
        }
        a aVar2 = this.f20668c.get(this.f20672g);
        a l8 = l(aVar.f20639c, aVar.f20640d);
        this.f20672g = l8.f20673a;
        c(aVar);
        k.b bVar = aVar.f20640d;
        if (bVar == null || !bVar.b()) {
            return;
        }
        if (aVar2 != null && aVar2.f20675c == aVar.f20640d.f20289d && aVar2.f20676d != null && aVar2.f20676d.f20287b == aVar.f20640d.f20287b && aVar2.f20676d.f20288c == aVar.f20640d.f20288c) {
            return;
        }
        k.b bVar2 = aVar.f20640d;
        this.f20670e.q(aVar, l(aVar.f20639c, new k.b(bVar2.f20286a, bVar2.f20289d)).f20673a, l8.f20673a);
    }

    @Override // j4.s1
    public synchronized String a() {
        return this.f20672g;
    }

    @Override // j4.s1
    public synchronized void b(b.a aVar) {
        s1.a aVar2;
        this.f20672g = null;
        Iterator<a> it = this.f20668c.values().iterator();
        while (it.hasNext()) {
            a next = it.next();
            it.remove();
            if (next.f20677e && (aVar2 = this.f20670e) != null) {
                aVar2.X(aVar, next.f20673a, false);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00e1 A[Catch: all -> 0x0118, TryCatch #0 {, blocks: (B:4:0x0005, B:8:0x0014, B:11:0x0025, B:13:0x0030, B:16:0x003a, B:23:0x004b, B:25:0x0057, B:26:0x005d, B:28:0x0061, B:30:0x0067, B:32:0x0080, B:34:0x00db, B:36:0x00e1, B:38:0x00f7, B:40:0x0103, B:42:0x0109), top: B:48:0x0005 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00f3  */
    @Override // j4.s1
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void c(j4.b.a r25) {
        /*
            Method dump skipped, instructions count: 283
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: j4.q1.c(j4.b$a):void");
    }

    @Override // j4.s1
    public synchronized void d(b.a aVar) {
        b6.a.e(this.f20670e);
        h2 h2Var = this.f20671f;
        this.f20671f = aVar.f20638b;
        Iterator<a> it = this.f20668c.values().iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (!next.m(h2Var, this.f20671f) || next.j(aVar)) {
                it.remove();
                if (next.f20677e) {
                    if (next.f20673a.equals(this.f20672g)) {
                        this.f20672g = null;
                    }
                    this.f20670e.X(aVar, next.f20673a, false);
                }
            }
        }
        m(aVar);
    }

    @Override // j4.s1
    public void e(s1.a aVar) {
        this.f20670e = aVar;
    }

    @Override // j4.s1
    public synchronized String f(h2 h2Var, k.b bVar) {
        return l(h2Var.l(bVar.f20286a, this.f20667b).f9758c, bVar).f20673a;
    }

    @Override // j4.s1
    public synchronized void g(b.a aVar, int i8) {
        b6.a.e(this.f20670e);
        boolean z4 = i8 == 0;
        Iterator<a> it = this.f20668c.values().iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (next.j(aVar)) {
                it.remove();
                if (next.f20677e) {
                    boolean equals = next.f20673a.equals(this.f20672g);
                    boolean z8 = z4 && equals && next.f20678f;
                    if (equals) {
                        this.f20672g = null;
                    }
                    this.f20670e.X(aVar, next.f20673a, z8);
                }
            }
        }
        m(aVar);
    }
}
