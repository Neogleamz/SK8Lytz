package p5;

import com.google.common.collect.ImmutableList;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f implements i {

    /* renamed from: a  reason: collision with root package name */
    private final c f22412a = new c();

    /* renamed from: b  reason: collision with root package name */
    private final k f22413b = new k();

    /* renamed from: c  reason: collision with root package name */
    private final Deque<l> f22414c = new ArrayDeque();

    /* renamed from: d  reason: collision with root package name */
    private int f22415d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f22416e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends l {
        a() {
        }

        @Override // l4.f
        public void y() {
            f.this.i(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements h {

        /* renamed from: a  reason: collision with root package name */
        private final long f22418a;

        /* renamed from: b  reason: collision with root package name */
        private final ImmutableList<p5.b> f22419b;

        public b(long j8, ImmutableList<p5.b> immutableList) {
            this.f22418a = j8;
            this.f22419b = immutableList;
        }

        @Override // p5.h
        public int c(long j8) {
            return this.f22418a > j8 ? 0 : -1;
        }

        @Override // p5.h
        public long f(int i8) {
            b6.a.a(i8 == 0);
            return this.f22418a;
        }

        @Override // p5.h
        public List<p5.b> h(long j8) {
            return j8 >= this.f22418a ? this.f22419b : ImmutableList.E();
        }

        @Override // p5.h
        public int i() {
            return 1;
        }
    }

    public f() {
        for (int i8 = 0; i8 < 2; i8++) {
            this.f22414c.addFirst(new a());
        }
        this.f22415d = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i(l lVar) {
        b6.a.f(this.f22414c.size() < 2);
        b6.a.a(!this.f22414c.contains(lVar));
        lVar.k();
        this.f22414c.addFirst(lVar);
    }

    @Override // p5.i
    public void a(long j8) {
    }

    @Override // l4.d
    /* renamed from: f */
    public k c() {
        b6.a.f(!this.f22416e);
        if (this.f22415d != 0) {
            return null;
        }
        this.f22415d = 1;
        return this.f22413b;
    }

    @Override // l4.d
    public void flush() {
        b6.a.f(!this.f22416e);
        this.f22413b.k();
        this.f22415d = 0;
    }

    @Override // l4.d
    /* renamed from: g */
    public l b() {
        b6.a.f(!this.f22416e);
        if (this.f22415d != 2 || this.f22414c.isEmpty()) {
            return null;
        }
        l removeFirst = this.f22414c.removeFirst();
        if (this.f22413b.t()) {
            removeFirst.j(4);
        } else {
            k kVar = this.f22413b;
            removeFirst.z(this.f22413b.f9514e, new b(kVar.f9514e, this.f22412a.a(((ByteBuffer) b6.a.e(kVar.f9512c)).array())), 0L);
        }
        this.f22413b.k();
        this.f22415d = 0;
        return removeFirst;
    }

    @Override // l4.d
    /* renamed from: h */
    public void d(k kVar) {
        b6.a.f(!this.f22416e);
        b6.a.f(this.f22415d == 1);
        b6.a.a(this.f22413b == kVar);
        this.f22415d = 2;
    }

    @Override // l4.d
    public void release() {
        this.f22416e = true;
    }
}
