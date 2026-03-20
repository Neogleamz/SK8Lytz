package androidx.concurrent.futures;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a<V> implements com.google.common.util.concurrent.d<V> {

    /* renamed from: d  reason: collision with root package name */
    static final boolean f3112d = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));

    /* renamed from: e  reason: collision with root package name */
    private static final Logger f3113e = Logger.getLogger(a.class.getName());

    /* renamed from: f  reason: collision with root package name */
    static final b f3114f;

    /* renamed from: g  reason: collision with root package name */
    private static final Object f3115g;

    /* renamed from: a  reason: collision with root package name */
    volatile Object f3116a;

    /* renamed from: b  reason: collision with root package name */
    volatile e f3117b;

    /* renamed from: c  reason: collision with root package name */
    volatile i f3118c;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class b {
        private b() {
        }

        abstract boolean a(a<?> aVar, e eVar, e eVar2);

        abstract boolean b(a<?> aVar, Object obj, Object obj2);

        abstract boolean c(a<?> aVar, i iVar, i iVar2);

        abstract void d(i iVar, i iVar2);

        abstract void e(i iVar, Thread thread);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: c  reason: collision with root package name */
        static final c f3119c;

        /* renamed from: d  reason: collision with root package name */
        static final c f3120d;

        /* renamed from: a  reason: collision with root package name */
        final boolean f3121a;

        /* renamed from: b  reason: collision with root package name */
        final Throwable f3122b;

        static {
            if (a.f3112d) {
                f3120d = null;
                f3119c = null;
                return;
            }
            f3120d = new c(false, null);
            f3119c = new c(true, null);
        }

        c(boolean z4, Throwable th) {
            this.f3121a = z4;
            this.f3122b = th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: b  reason: collision with root package name */
        static final d f3123b = new d(new C0023a("Failure occurred while trying to finish a future."));

        /* renamed from: a  reason: collision with root package name */
        final Throwable f3124a;

        /* renamed from: androidx.concurrent.futures.a$d$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class C0023a extends Throwable {
            C0023a(String str) {
                super(str);
            }

            @Override // java.lang.Throwable
            public synchronized Throwable fillInStackTrace() {
                return this;
            }
        }

        d(Throwable th) {
            this.f3124a = (Throwable) a.j(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: d  reason: collision with root package name */
        static final e f3125d = new e(null, null);

        /* renamed from: a  reason: collision with root package name */
        final Runnable f3126a;

        /* renamed from: b  reason: collision with root package name */
        final Executor f3127b;

        /* renamed from: c  reason: collision with root package name */
        e f3128c;

        e(Runnable runnable, Executor executor) {
            this.f3126a = runnable;
            this.f3127b = executor;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class f extends b {

        /* renamed from: a  reason: collision with root package name */
        final AtomicReferenceFieldUpdater<i, Thread> f3129a;

        /* renamed from: b  reason: collision with root package name */
        final AtomicReferenceFieldUpdater<i, i> f3130b;

        /* renamed from: c  reason: collision with root package name */
        final AtomicReferenceFieldUpdater<a, i> f3131c;

        /* renamed from: d  reason: collision with root package name */
        final AtomicReferenceFieldUpdater<a, e> f3132d;

        /* renamed from: e  reason: collision with root package name */
        final AtomicReferenceFieldUpdater<a, Object> f3133e;

        f(AtomicReferenceFieldUpdater<i, Thread> atomicReferenceFieldUpdater, AtomicReferenceFieldUpdater<i, i> atomicReferenceFieldUpdater2, AtomicReferenceFieldUpdater<a, i> atomicReferenceFieldUpdater3, AtomicReferenceFieldUpdater<a, e> atomicReferenceFieldUpdater4, AtomicReferenceFieldUpdater<a, Object> atomicReferenceFieldUpdater5) {
            super();
            this.f3129a = atomicReferenceFieldUpdater;
            this.f3130b = atomicReferenceFieldUpdater2;
            this.f3131c = atomicReferenceFieldUpdater3;
            this.f3132d = atomicReferenceFieldUpdater4;
            this.f3133e = atomicReferenceFieldUpdater5;
        }

        @Override // androidx.concurrent.futures.a.b
        boolean a(a<?> aVar, e eVar, e eVar2) {
            return androidx.concurrent.futures.b.a(this.f3132d, aVar, eVar, eVar2);
        }

        @Override // androidx.concurrent.futures.a.b
        boolean b(a<?> aVar, Object obj, Object obj2) {
            return androidx.concurrent.futures.b.a(this.f3133e, aVar, obj, obj2);
        }

        @Override // androidx.concurrent.futures.a.b
        boolean c(a<?> aVar, i iVar, i iVar2) {
            return androidx.concurrent.futures.b.a(this.f3131c, aVar, iVar, iVar2);
        }

        @Override // androidx.concurrent.futures.a.b
        void d(i iVar, i iVar2) {
            this.f3130b.lazySet(iVar, iVar2);
        }

        @Override // androidx.concurrent.futures.a.b
        void e(i iVar, Thread thread) {
            this.f3129a.lazySet(iVar, thread);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g<V> implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final a<V> f3134a;

        /* renamed from: b  reason: collision with root package name */
        final com.google.common.util.concurrent.d<? extends V> f3135b;

        @Override // java.lang.Runnable
        public void run() {
            if (this.f3134a.f3116a != this) {
                return;
            }
            if (a.f3114f.b(this.f3134a, this, a.s(this.f3135b))) {
                a.o(this.f3134a);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class h extends b {
        h() {
            super();
        }

        @Override // androidx.concurrent.futures.a.b
        boolean a(a<?> aVar, e eVar, e eVar2) {
            synchronized (aVar) {
                if (aVar.f3117b == eVar) {
                    aVar.f3117b = eVar2;
                    return true;
                }
                return false;
            }
        }

        @Override // androidx.concurrent.futures.a.b
        boolean b(a<?> aVar, Object obj, Object obj2) {
            synchronized (aVar) {
                if (aVar.f3116a == obj) {
                    aVar.f3116a = obj2;
                    return true;
                }
                return false;
            }
        }

        @Override // androidx.concurrent.futures.a.b
        boolean c(a<?> aVar, i iVar, i iVar2) {
            synchronized (aVar) {
                if (aVar.f3118c == iVar) {
                    aVar.f3118c = iVar2;
                    return true;
                }
                return false;
            }
        }

        @Override // androidx.concurrent.futures.a.b
        void d(i iVar, i iVar2) {
            iVar.f3138b = iVar2;
        }

        @Override // androidx.concurrent.futures.a.b
        void e(i iVar, Thread thread) {
            iVar.f3137a = thread;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class i {

        /* renamed from: c  reason: collision with root package name */
        static final i f3136c = new i(false);

        /* renamed from: a  reason: collision with root package name */
        volatile Thread f3137a;

        /* renamed from: b  reason: collision with root package name */
        volatile i f3138b;

        i() {
            a.f3114f.e(this, Thread.currentThread());
        }

        i(boolean z4) {
        }

        void a(i iVar) {
            a.f3114f.d(this, iVar);
        }

        void b() {
            Thread thread = this.f3137a;
            if (thread != null) {
                this.f3137a = null;
                LockSupport.unpark(thread);
            }
        }
    }

    static {
        b hVar;
        try {
            hVar = new f(AtomicReferenceFieldUpdater.newUpdater(i.class, Thread.class, "a"), AtomicReferenceFieldUpdater.newUpdater(i.class, i.class, "b"), AtomicReferenceFieldUpdater.newUpdater(a.class, i.class, "c"), AtomicReferenceFieldUpdater.newUpdater(a.class, e.class, "b"), AtomicReferenceFieldUpdater.newUpdater(a.class, Object.class, "a"));
            th = null;
        } catch (Throwable th) {
            th = th;
            hVar = new h();
        }
        f3114f = hVar;
        if (th != null) {
            f3113e.log(Level.SEVERE, "SafeAtomicHelper is broken!", th);
        }
        f3115g = new Object();
    }

    private String A(Object obj) {
        return obj == this ? "this future" : String.valueOf(obj);
    }

    private void f(StringBuilder sb) {
        String str = "]";
        try {
            Object t8 = t(this);
            sb.append("SUCCESS, result=[");
            sb.append(A(t8));
            sb.append("]");
        } catch (CancellationException unused) {
            str = "CANCELLED";
            sb.append(str);
        } catch (RuntimeException e8) {
            sb.append("UNKNOWN, cause=[");
            sb.append(e8.getClass());
            str = " thrown from get()]";
            sb.append(str);
        } catch (ExecutionException e9) {
            sb.append("FAILURE, cause=[");
            sb.append(e9.getCause());
            sb.append(str);
        }
    }

    private static CancellationException i(String str, Throwable th) {
        CancellationException cancellationException = new CancellationException(str);
        cancellationException.initCause(th);
        return cancellationException;
    }

    static <T> T j(T t8) {
        Objects.requireNonNull(t8);
        return t8;
    }

    private e k(e eVar) {
        e eVar2;
        do {
            eVar2 = this.f3117b;
        } while (!f3114f.a(this, eVar2, e.f3125d));
        e eVar3 = eVar;
        e eVar4 = eVar2;
        while (eVar4 != null) {
            e eVar5 = eVar4.f3128c;
            eVar4.f3128c = eVar3;
            eVar3 = eVar4;
            eVar4 = eVar5;
        }
        return eVar3;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0, types: [androidx.concurrent.futures.a$b] */
    /* JADX WARN: Type inference failed for: r4v0, types: [androidx.concurrent.futures.a<?>] */
    /* JADX WARN: Type inference failed for: r4v1, types: [androidx.concurrent.futures.a] */
    /* JADX WARN: Type inference failed for: r4v6, types: [androidx.concurrent.futures.a<V>, androidx.concurrent.futures.a] */
    static void o(a<?> aVar) {
        e eVar = null;
        while (true) {
            aVar.w();
            aVar.h();
            e k8 = aVar.k(eVar);
            while (k8 != null) {
                eVar = k8.f3128c;
                Runnable runnable = k8.f3126a;
                if (runnable instanceof g) {
                    g gVar = (g) runnable;
                    aVar = gVar.f3134a;
                    if (aVar.f3116a == gVar) {
                        if (f3114f.b(aVar, gVar, s(gVar.f3135b))) {
                            break;
                        }
                    } else {
                        continue;
                    }
                } else {
                    q(runnable, k8.f3127b);
                }
                k8 = eVar;
            }
            return;
        }
    }

    private static void q(Runnable runnable, Executor executor) {
        try {
            executor.execute(runnable);
        } catch (RuntimeException e8) {
            Logger logger = f3113e;
            Level level = Level.SEVERE;
            logger.log(level, "RuntimeException while executing runnable " + runnable + " with executor " + executor, (Throwable) e8);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private V r(Object obj) {
        if (obj instanceof c) {
            throw i("Task was cancelled.", ((c) obj).f3122b);
        }
        if (obj instanceof d) {
            throw new ExecutionException(((d) obj).f3124a);
        }
        if (obj == f3115g) {
            return null;
        }
        return obj;
    }

    static Object s(com.google.common.util.concurrent.d<?> dVar) {
        if (dVar instanceof a) {
            Object obj = ((a) dVar).f3116a;
            if (obj instanceof c) {
                c cVar = (c) obj;
                return cVar.f3121a ? cVar.f3122b != null ? new c(false, cVar.f3122b) : c.f3120d : obj;
            }
            return obj;
        }
        boolean isCancelled = dVar.isCancelled();
        if ((!f3112d) && isCancelled) {
            return c.f3120d;
        }
        try {
            Object t8 = t(dVar);
            return t8 == null ? f3115g : t8;
        } catch (CancellationException e8) {
            if (isCancelled) {
                return new c(false, e8);
            }
            return new d(new IllegalArgumentException("get() threw CancellationException, despite reporting isCancelled() == false: " + dVar, e8));
        } catch (ExecutionException e9) {
            return new d(e9.getCause());
        } catch (Throwable th) {
            return new d(th);
        }
    }

    static <V> V t(Future<V> future) {
        V v8;
        boolean z4 = false;
        while (true) {
            try {
                v8 = future.get();
                break;
            } catch (InterruptedException unused) {
                z4 = true;
            } catch (Throwable th) {
                if (z4) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (z4) {
            Thread.currentThread().interrupt();
        }
        return v8;
    }

    private void w() {
        i iVar;
        do {
            iVar = this.f3118c;
        } while (!f3114f.c(this, iVar, i.f3136c));
        while (iVar != null) {
            iVar.b();
            iVar = iVar.f3138b;
        }
    }

    private void x(i iVar) {
        iVar.f3137a = null;
        while (true) {
            i iVar2 = this.f3118c;
            if (iVar2 == i.f3136c) {
                return;
            }
            i iVar3 = null;
            while (iVar2 != null) {
                i iVar4 = iVar2.f3138b;
                if (iVar2.f3137a != null) {
                    iVar3 = iVar2;
                } else if (iVar3 != null) {
                    iVar3.f3138b = iVar4;
                    if (iVar3.f3137a == null) {
                        break;
                    }
                } else if (!f3114f.c(this, iVar2, iVar4)) {
                    break;
                }
                iVar2 = iVar4;
            }
            return;
        }
    }

    protected final boolean B() {
        Object obj = this.f3116a;
        return (obj instanceof c) && ((c) obj).f3121a;
    }

    @Override // com.google.common.util.concurrent.d
    public final void c(Runnable runnable, Executor executor) {
        j(runnable);
        j(executor);
        e eVar = this.f3117b;
        if (eVar == e.f3125d) {
            q(runnable, executor);
        }
        e eVar2 = new e(runnable, executor);
        do {
            eVar2.f3128c = eVar;
            if (f3114f.a(this, eVar, eVar2)) {
                return;
            }
            eVar = this.f3117b;
        } while (eVar != e.f3125d);
        q(runnable, executor);
    }

    @Override // java.util.concurrent.Future
    public final boolean cancel(boolean z4) {
        Object obj = this.f3116a;
        if (!(obj == null) && !(obj instanceof g)) {
            return false;
        }
        c cVar = f3112d ? new c(z4, new CancellationException("Future.cancel() was called.")) : z4 ? c.f3119c : c.f3120d;
        a<V> aVar = this;
        boolean z8 = false;
        while (true) {
            if (f3114f.b(aVar, obj, cVar)) {
                if (z4) {
                    aVar.u();
                }
                o(aVar);
                if (!(obj instanceof g)) {
                    return true;
                }
                com.google.common.util.concurrent.d<? extends V> dVar = ((g) obj).f3135b;
                if (!(dVar instanceof a)) {
                    dVar.cancel(z4);
                    return true;
                }
                aVar = (a) dVar;
                obj = aVar.f3116a;
                if (!(obj == null) && !(obj instanceof g)) {
                    return true;
                }
                z8 = true;
            } else {
                obj = aVar.f3116a;
                if (!(obj instanceof g)) {
                    return z8;
                }
            }
        }
    }

    @Override // java.util.concurrent.Future
    public final V get() {
        Object obj;
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object obj2 = this.f3116a;
        if ((obj2 != null) && (!(obj2 instanceof g))) {
            return r(obj2);
        }
        i iVar = this.f3118c;
        if (iVar != i.f3136c) {
            i iVar2 = new i();
            do {
                iVar2.a(iVar);
                if (f3114f.c(this, iVar, iVar2)) {
                    do {
                        LockSupport.park(this);
                        if (Thread.interrupted()) {
                            x(iVar2);
                            throw new InterruptedException();
                        }
                        obj = this.f3116a;
                    } while (!((obj != null) & (!(obj instanceof g))));
                    return r(obj);
                }
                iVar = this.f3118c;
            } while (iVar != i.f3136c);
            return r(this.f3116a);
        }
        return r(this.f3116a);
    }

    @Override // java.util.concurrent.Future
    public final V get(long j8, TimeUnit timeUnit) {
        Locale locale;
        long nanos = timeUnit.toNanos(j8);
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object obj = this.f3116a;
        if ((obj != null) && (!(obj instanceof g))) {
            return r(obj);
        }
        long nanoTime = nanos > 0 ? System.nanoTime() + nanos : 0L;
        if (nanos >= 1000) {
            i iVar = this.f3118c;
            if (iVar != i.f3136c) {
                i iVar2 = new i();
                do {
                    iVar2.a(iVar);
                    if (f3114f.c(this, iVar, iVar2)) {
                        do {
                            LockSupport.parkNanos(this, nanos);
                            if (Thread.interrupted()) {
                                x(iVar2);
                                throw new InterruptedException();
                            }
                            Object obj2 = this.f3116a;
                            if ((obj2 != null) && (!(obj2 instanceof g))) {
                                return r(obj2);
                            }
                            nanos = nanoTime - System.nanoTime();
                        } while (nanos >= 1000);
                        x(iVar2);
                    } else {
                        iVar = this.f3118c;
                    }
                } while (iVar != i.f3136c);
                return r(this.f3116a);
            }
            return r(this.f3116a);
        }
        while (nanos > 0) {
            Object obj3 = this.f3116a;
            if ((obj3 != null) && (!(obj3 instanceof g))) {
                return r(obj3);
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            nanos = nanoTime - System.nanoTime();
        }
        String aVar = toString();
        String lowerCase = timeUnit.toString().toLowerCase(Locale.ROOT);
        String str = "Waited " + j8 + " " + timeUnit.toString().toLowerCase(locale);
        if (nanos + 1000 < 0) {
            String str2 = str + " (plus ";
            long j9 = -nanos;
            long convert = timeUnit.convert(j9, TimeUnit.NANOSECONDS);
            long nanos2 = j9 - timeUnit.toNanos(convert);
            int i8 = (convert > 0L ? 1 : (convert == 0L ? 0 : -1));
            boolean z4 = i8 == 0 || nanos2 > 1000;
            if (i8 > 0) {
                String str3 = str2 + convert + " " + lowerCase;
                if (z4) {
                    str3 = str3 + ",";
                }
                str2 = str3 + " ";
            }
            if (z4) {
                str2 = str2 + nanos2 + " nanoseconds ";
            }
            str = str2 + "delay)";
        }
        if (isDone()) {
            throw new TimeoutException(str + " but future completed as timeout expired");
        }
        throw new TimeoutException(str + " for " + aVar);
    }

    protected void h() {
    }

    @Override // java.util.concurrent.Future
    public final boolean isCancelled() {
        return this.f3116a instanceof c;
    }

    @Override // java.util.concurrent.Future
    public final boolean isDone() {
        Object obj = this.f3116a;
        return (!(obj instanceof g)) & (obj != null);
    }

    public String toString() {
        String str;
        String str2;
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("[status=");
        if (!isCancelled()) {
            if (!isDone()) {
                try {
                    str = v();
                } catch (RuntimeException e8) {
                    str = "Exception thrown from implementation: " + e8.getClass();
                }
                if (str != null && !str.isEmpty()) {
                    sb.append("PENDING, info=[");
                    sb.append(str);
                    sb.append("]");
                    sb.append("]");
                    return sb.toString();
                }
                str2 = isDone() ? "PENDING" : "PENDING";
            }
            f(sb);
            sb.append("]");
            return sb.toString();
        }
        str2 = "CANCELLED";
        sb.append(str2);
        sb.append("]");
        return sb.toString();
    }

    protected void u() {
    }

    protected String v() {
        Object obj = this.f3116a;
        if (obj instanceof g) {
            return "setFuture=[" + A(((g) obj).f3135b) + "]";
        } else if (this instanceof ScheduledFuture) {
            return "remaining delay=[" + ((ScheduledFuture) this).getDelay(TimeUnit.MILLISECONDS) + " ms]";
        } else {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean y(V v8) {
        if (v8 == null) {
            v8 = (V) f3115g;
        }
        if (f3114f.b(this, null, v8)) {
            o(this);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean z(Throwable th) {
        if (f3114f.b(this, null, new d((Throwable) j(th)))) {
            o(this);
            return true;
        }
        return false;
    }
}
