package q5;

import b6.l0;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import l4.f;
import p5.h;
import p5.i;
import p5.k;
import p5.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class e implements i {

    /* renamed from: a  reason: collision with root package name */
    private final ArrayDeque<b> f22618a = new ArrayDeque<>();

    /* renamed from: b  reason: collision with root package name */
    private final ArrayDeque<l> f22619b;

    /* renamed from: c  reason: collision with root package name */
    private final PriorityQueue<b> f22620c;

    /* renamed from: d  reason: collision with root package name */
    private b f22621d;

    /* renamed from: e  reason: collision with root package name */
    private long f22622e;

    /* renamed from: f  reason: collision with root package name */
    private long f22623f;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends k implements Comparable<b> {

        /* renamed from: k  reason: collision with root package name */
        private long f22624k;

        private b() {
        }

        @Override // java.lang.Comparable
        /* renamed from: F */
        public int compareTo(b bVar) {
            if (t() != bVar.t()) {
                return t() ? 1 : -1;
            }
            long j8 = this.f9514e - bVar.f9514e;
            if (j8 == 0) {
                j8 = this.f22624k - bVar.f22624k;
                if (j8 == 0) {
                    return 0;
                }
            }
            return j8 > 0 ? 1 : -1;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class c extends l {

        /* renamed from: f  reason: collision with root package name */
        private f.a<c> f22625f;

        public c(f.a<c> aVar) {
            this.f22625f = aVar;
        }

        @Override // l4.f
        public final void y() {
            this.f22625f.a(this);
        }
    }

    public e() {
        for (int i8 = 0; i8 < 10; i8++) {
            this.f22618a.add(new b());
        }
        this.f22619b = new ArrayDeque<>();
        for (int i9 = 0; i9 < 2; i9++) {
            this.f22619b.add(new c(new d(this)));
        }
        this.f22620c = new PriorityQueue<>();
    }

    private void m(b bVar) {
        bVar.k();
        this.f22618a.add(bVar);
    }

    @Override // p5.i
    public void a(long j8) {
        this.f22622e = j8;
    }

    protected abstract h e();

    protected abstract void f(k kVar);

    @Override // l4.d
    public void flush() {
        this.f22623f = 0L;
        this.f22622e = 0L;
        while (!this.f22620c.isEmpty()) {
            m((b) l0.j(this.f22620c.poll()));
        }
        b bVar = this.f22621d;
        if (bVar != null) {
            m(bVar);
            this.f22621d = null;
        }
    }

    @Override // l4.d
    /* renamed from: g */
    public k c() {
        b6.a.f(this.f22621d == null);
        if (this.f22618a.isEmpty()) {
            return null;
        }
        b pollFirst = this.f22618a.pollFirst();
        this.f22621d = pollFirst;
        return pollFirst;
    }

    @Override // l4.d
    /* renamed from: h */
    public l b() {
        l lVar;
        if (this.f22619b.isEmpty()) {
            return null;
        }
        while (!this.f22620c.isEmpty() && ((b) l0.j(this.f22620c.peek())).f9514e <= this.f22622e) {
            b bVar = (b) l0.j(this.f22620c.poll());
            if (bVar.t()) {
                lVar = (l) l0.j(this.f22619b.pollFirst());
                lVar.j(4);
            } else {
                f(bVar);
                if (k()) {
                    h e8 = e();
                    lVar = (l) l0.j(this.f22619b.pollFirst());
                    lVar.z(bVar.f9514e, e8, Long.MAX_VALUE);
                } else {
                    m(bVar);
                }
            }
            m(bVar);
            return lVar;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final l i() {
        return this.f22619b.pollFirst();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final long j() {
        return this.f22622e;
    }

    protected abstract boolean k();

    @Override // l4.d
    /* renamed from: l */
    public void d(k kVar) {
        b6.a.a(kVar == this.f22621d);
        b bVar = (b) kVar;
        if (bVar.s()) {
            m(bVar);
        } else {
            long j8 = this.f22623f;
            this.f22623f = 1 + j8;
            bVar.f22624k = j8;
            this.f22620c.add(bVar);
        }
        this.f22621d = null;
    }

    protected void n(l lVar) {
        lVar.k();
        this.f22619b.add(lVar);
    }

    @Override // l4.d
    public void release() {
    }
}
