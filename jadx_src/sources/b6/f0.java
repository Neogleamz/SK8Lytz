package b6;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import b6.l;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f0 implements l {

    /* renamed from: b  reason: collision with root package name */
    private static final List<b> f8041b = new ArrayList(50);

    /* renamed from: a  reason: collision with root package name */
    private final Handler f8042a;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements l.a {

        /* renamed from: a  reason: collision with root package name */
        private Message f8043a;

        /* renamed from: b  reason: collision with root package name */
        private f0 f8044b;

        private b() {
        }

        private void b() {
            this.f8043a = null;
            this.f8044b = null;
            f0.o(this);
        }

        @Override // b6.l.a
        public void a() {
            ((Message) b6.a.e(this.f8043a)).sendToTarget();
            b();
        }

        public boolean c(Handler handler) {
            boolean sendMessageAtFrontOfQueue = handler.sendMessageAtFrontOfQueue((Message) b6.a.e(this.f8043a));
            b();
            return sendMessageAtFrontOfQueue;
        }

        public b d(Message message, f0 f0Var) {
            this.f8043a = message;
            this.f8044b = f0Var;
            return this;
        }
    }

    public f0(Handler handler) {
        this.f8042a = handler;
    }

    private static b n() {
        b bVar;
        List<b> list = f8041b;
        synchronized (list) {
            bVar = list.isEmpty() ? new b() : list.remove(list.size() - 1);
        }
        return bVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void o(b bVar) {
        List<b> list = f8041b;
        synchronized (list) {
            if (list.size() < 50) {
                list.add(bVar);
            }
        }
    }

    @Override // b6.l
    public l.a a(int i8, int i9, int i10) {
        return n().d(this.f8042a.obtainMessage(i8, i9, i10), this);
    }

    @Override // b6.l
    public boolean b(Runnable runnable) {
        return this.f8042a.post(runnable);
    }

    @Override // b6.l
    public l.a c(int i8) {
        return n().d(this.f8042a.obtainMessage(i8), this);
    }

    @Override // b6.l
    public boolean d(int i8) {
        return this.f8042a.hasMessages(i8);
    }

    @Override // b6.l
    public boolean e(int i8) {
        return this.f8042a.sendEmptyMessage(i8);
    }

    @Override // b6.l
    public l.a f(int i8, int i9, int i10, Object obj) {
        return n().d(this.f8042a.obtainMessage(i8, i9, i10, obj), this);
    }

    @Override // b6.l
    public boolean g(int i8, long j8) {
        return this.f8042a.sendEmptyMessageAtTime(i8, j8);
    }

    @Override // b6.l
    public boolean h(l.a aVar) {
        return ((b) aVar).c(this.f8042a);
    }

    @Override // b6.l
    public void i(int i8) {
        this.f8042a.removeMessages(i8);
    }

    @Override // b6.l
    public l.a j(int i8, Object obj) {
        return n().d(this.f8042a.obtainMessage(i8, obj), this);
    }

    @Override // b6.l
    public void k(Object obj) {
        this.f8042a.removeCallbacksAndMessages(obj);
    }

    @Override // b6.l
    public Looper l() {
        return this.f8042a.getLooper();
    }
}
