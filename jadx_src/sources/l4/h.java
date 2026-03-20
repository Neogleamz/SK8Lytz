package l4;

import com.google.android.exoplayer2.decoder.DecoderException;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import java.util.ArrayDeque;
import l4.f;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class h<I extends DecoderInputBuffer, O extends f, E extends DecoderException> implements d<I, O, E> {

    /* renamed from: a  reason: collision with root package name */
    private final Thread f21604a;

    /* renamed from: b  reason: collision with root package name */
    private final Object f21605b = new Object();

    /* renamed from: c  reason: collision with root package name */
    private final ArrayDeque<I> f21606c = new ArrayDeque<>();

    /* renamed from: d  reason: collision with root package name */
    private final ArrayDeque<O> f21607d = new ArrayDeque<>();

    /* renamed from: e  reason: collision with root package name */
    private final I[] f21608e;

    /* renamed from: f  reason: collision with root package name */
    private final O[] f21609f;

    /* renamed from: g  reason: collision with root package name */
    private int f21610g;

    /* renamed from: h  reason: collision with root package name */
    private int f21611h;

    /* renamed from: i  reason: collision with root package name */
    private I f21612i;

    /* renamed from: j  reason: collision with root package name */
    private E f21613j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f21614k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f21615l;

    /* renamed from: m  reason: collision with root package name */
    private int f21616m;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends Thread {
        a(String str) {
            super(str);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            h.this.t();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public h(I[] iArr, O[] oArr) {
        this.f21608e = iArr;
        this.f21610g = iArr.length;
        for (int i8 = 0; i8 < this.f21610g; i8++) {
            this.f21608e[i8] = g();
        }
        this.f21609f = oArr;
        this.f21611h = oArr.length;
        for (int i9 = 0; i9 < this.f21611h; i9++) {
            this.f21609f[i9] = h();
        }
        a aVar = new a("ExoPlayer:SimpleDecoder");
        this.f21604a = aVar;
        aVar.start();
    }

    private boolean f() {
        return !this.f21606c.isEmpty() && this.f21611h > 0;
    }

    private boolean k() {
        E i8;
        synchronized (this.f21605b) {
            while (!this.f21615l && !f()) {
                this.f21605b.wait();
            }
            if (this.f21615l) {
                return false;
            }
            I removeFirst = this.f21606c.removeFirst();
            O[] oArr = this.f21609f;
            int i9 = this.f21611h - 1;
            this.f21611h = i9;
            O o5 = oArr[i9];
            boolean z4 = this.f21614k;
            this.f21614k = false;
            if (removeFirst.t()) {
                o5.j(4);
            } else {
                if (removeFirst.s()) {
                    o5.j(Integer.MIN_VALUE);
                }
                if (removeFirst.u()) {
                    o5.j(134217728);
                }
                try {
                    i8 = j(removeFirst, o5, z4);
                } catch (OutOfMemoryError | RuntimeException e8) {
                    i8 = i(e8);
                }
                if (i8 != null) {
                    synchronized (this.f21605b) {
                        this.f21613j = i8;
                    }
                    return false;
                }
            }
            synchronized (this.f21605b) {
                if (!this.f21614k) {
                    if (o5.s()) {
                        this.f21616m++;
                    } else {
                        o5.f21598c = this.f21616m;
                        this.f21616m = 0;
                        this.f21607d.addLast(o5);
                        q(removeFirst);
                    }
                }
                o5.y();
                q(removeFirst);
            }
            return true;
        }
    }

    private void n() {
        if (f()) {
            this.f21605b.notify();
        }
    }

    private void o() {
        E e8 = this.f21613j;
        if (e8 != null) {
            throw e8;
        }
    }

    private void q(I i8) {
        i8.k();
        I[] iArr = this.f21608e;
        int i9 = this.f21610g;
        this.f21610g = i9 + 1;
        iArr[i9] = i8;
    }

    private void s(O o5) {
        o5.k();
        O[] oArr = this.f21609f;
        int i8 = this.f21611h;
        this.f21611h = i8 + 1;
        oArr[i8] = o5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void t() {
        do {
            try {
            } catch (InterruptedException e8) {
                throw new IllegalStateException(e8);
            }
        } while (k());
    }

    @Override // l4.d
    public final void flush() {
        synchronized (this.f21605b) {
            this.f21614k = true;
            this.f21616m = 0;
            I i8 = this.f21612i;
            if (i8 != null) {
                q(i8);
                this.f21612i = null;
            }
            while (!this.f21606c.isEmpty()) {
                q(this.f21606c.removeFirst());
            }
            while (!this.f21607d.isEmpty()) {
                this.f21607d.removeFirst().y();
            }
        }
    }

    protected abstract I g();

    protected abstract O h();

    protected abstract E i(Throwable th);

    protected abstract E j(I i8, O o5, boolean z4);

    @Override // l4.d
    /* renamed from: l */
    public final I c() {
        I i8;
        synchronized (this.f21605b) {
            o();
            b6.a.f(this.f21612i == null);
            int i9 = this.f21610g;
            if (i9 == 0) {
                i8 = null;
            } else {
                I[] iArr = this.f21608e;
                int i10 = i9 - 1;
                this.f21610g = i10;
                i8 = iArr[i10];
            }
            this.f21612i = i8;
        }
        return i8;
    }

    @Override // l4.d
    /* renamed from: m */
    public final O b() {
        synchronized (this.f21605b) {
            o();
            if (this.f21607d.isEmpty()) {
                return null;
            }
            return this.f21607d.removeFirst();
        }
    }

    @Override // l4.d
    /* renamed from: p */
    public final void d(I i8) {
        synchronized (this.f21605b) {
            o();
            b6.a.a(i8 == this.f21612i);
            this.f21606c.addLast(i8);
            n();
            this.f21612i = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void r(O o5) {
        synchronized (this.f21605b) {
            s(o5);
            n();
        }
    }

    @Override // l4.d
    public void release() {
        synchronized (this.f21605b) {
            this.f21615l = true;
            this.f21605b.notify();
        }
        try {
            this.f21604a.join();
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void u(int i8) {
        b6.a.f(this.f21610g == this.f21608e.length);
        for (I i9 : this.f21608e) {
            i9.z(i8);
        }
    }
}
