package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.x8;
import com.google.android.gms.internal.measurement.x8.a;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class x8<MessageType extends x8<MessageType, BuilderType>, BuilderType extends a<MessageType, BuilderType>> extends g7<MessageType, BuilderType> {
    private static Map<Object, x8<?, ?>> zzc = new ConcurrentHashMap();
    private int zzd = -1;
    protected vb zzb = vb.k();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a<MessageType extends x8<MessageType, BuilderType>, BuilderType extends a<MessageType, BuilderType>> extends f7<MessageType, BuilderType> {

        /* renamed from: a  reason: collision with root package name */
        private final MessageType f12659a;

        /* renamed from: b  reason: collision with root package name */
        protected MessageType f12660b;

        /* JADX INFO: Access modifiers changed from: protected */
        public a(MessageType messagetype) {
            this.f12659a = messagetype;
            if (messagetype.G()) {
                throw new IllegalArgumentException("Default instance must be immutable.");
            }
            this.f12660b = (MessageType) messagetype.A();
        }

        private static <MessageType> void o(MessageType messagetype, MessageType messagetype2) {
            ua.a().c(messagetype).g(messagetype, messagetype2);
        }

        private final BuilderType w(byte[] bArr, int i8, int i9, l8 l8Var) {
            if (!this.f12660b.G()) {
                v();
            }
            try {
                ua.a().c(this.f12660b).c(this.f12660b, bArr, 0, i9, new m7(l8Var));
                return this;
            } catch (zzkb e8) {
                throw e8;
            } catch (IOException e9) {
                throw new RuntimeException("Reading from byte array should not throw IOException.", e9);
            } catch (IndexOutOfBoundsException unused) {
                throw zzkb.f();
            }
        }

        @Override // com.google.android.gms.internal.measurement.f7
        public /* synthetic */ Object clone() {
            a aVar = (a) this.f12659a.r(f.f12666e, null, null);
            aVar.f12660b = (MessageType) t();
            return aVar;
        }

        @Override // com.google.android.gms.internal.measurement.ka
        public final boolean g() {
            return x8.w(this.f12660b, false);
        }

        @Override // com.google.android.gms.internal.measurement.f7
        public final /* synthetic */ f7 k(byte[] bArr, int i8, int i9) {
            return w(bArr, 0, i9, l8.f12297c);
        }

        @Override // com.google.android.gms.internal.measurement.f7
        public final /* synthetic */ f7 l(byte[] bArr, int i8, int i9, l8 l8Var) {
            return w(bArr, 0, i9, l8Var);
        }

        public final BuilderType m(MessageType messagetype) {
            if (this.f12659a.equals(messagetype)) {
                return this;
            }
            if (!this.f12660b.G()) {
                v();
            }
            o(this.f12660b, messagetype);
            return this;
        }

        @Override // com.google.android.gms.internal.measurement.ha
        /* renamed from: q */
        public final MessageType n() {
            MessageType messagetype = (MessageType) t();
            if (messagetype.g()) {
                return messagetype;
            }
            throw new zzmu(messagetype);
        }

        @Override // com.google.android.gms.internal.measurement.ha
        /* renamed from: r */
        public MessageType t() {
            if (this.f12660b.G()) {
                this.f12660b.E();
                return this.f12660b;
            }
            return this.f12660b;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final void s() {
            if (this.f12660b.G()) {
                return;
            }
            v();
        }

        protected void v() {
            MessageType messagetype = (MessageType) this.f12659a.A();
            o(messagetype, this.f12660b);
            this.f12660b = messagetype;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class b<MessageType extends b<MessageType, BuilderType>, BuilderType> extends x8<MessageType, BuilderType> implements ka {
        protected o8<e> zzc = o8.i();

        /* JADX INFO: Access modifiers changed from: package-private */
        public final o8<e> H() {
            if (this.zzc.r()) {
                this.zzc = (o8) this.zzc.clone();
            }
            return this.zzc;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    protected static class c<T extends x8<T, ?>> extends i7<T> {

        /* renamed from: b  reason: collision with root package name */
        private final T f12661b;

        public c(T t8) {
            this.f12661b = t8;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d<ContainingType extends ia, Type> extends j8<ContainingType, Type> {
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class e implements q8<e> {
        @Override // com.google.android.gms.internal.measurement.q8
        public final zznq a() {
            throw new NoSuchMethodError();
        }

        @Override // com.google.android.gms.internal.measurement.q8
        public final boolean b() {
            throw new NoSuchMethodError();
        }

        @Override // java.lang.Comparable
        public final /* synthetic */ int compareTo(Object obj) {
            throw new NoSuchMethodError();
        }

        @Override // com.google.android.gms.internal.measurement.q8
        public final boolean d() {
            throw new NoSuchMethodError();
        }

        @Override // com.google.android.gms.internal.measurement.q8
        public final ma l(ma maVar, ma maVar2) {
            throw new NoSuchMethodError();
        }

        @Override // com.google.android.gms.internal.measurement.q8
        public final ha n(ha haVar, ia iaVar) {
            throw new NoSuchMethodError();
        }

        @Override // com.google.android.gms.internal.measurement.q8
        public final int zza() {
            throw new NoSuchMethodError();
        }

        @Override // com.google.android.gms.internal.measurement.q8
        public final zzng zzb() {
            throw new NoSuchMethodError();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum f {

        /* renamed from: a  reason: collision with root package name */
        public static final int f12662a = 1;

        /* renamed from: b  reason: collision with root package name */
        public static final int f12663b = 2;

        /* renamed from: c  reason: collision with root package name */
        public static final int f12664c = 3;

        /* renamed from: d  reason: collision with root package name */
        public static final int f12665d = 4;

        /* renamed from: e  reason: collision with root package name */
        public static final int f12666e = 5;

        /* renamed from: f  reason: collision with root package name */
        public static final int f12667f = 6;

        /* renamed from: g  reason: collision with root package name */
        public static final int f12668g = 7;

        /* renamed from: h  reason: collision with root package name */
        private static final /* synthetic */ int[] f12669h = {1, 2, 3, 4, 5, 6, 7};

        public static int[] a() {
            return (int[]) f12669h.clone();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static f9 B() {
        return y8.h();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static d9 C() {
        return t9.h();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static <E> g9<E> D() {
        return ta.i();
    }

    private final int m() {
        return ua.a().c(this).b(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T extends x8<?, ?>> T o(Class<T> cls) {
        x8<?, ?> x8Var = zzc.get(cls);
        if (x8Var == null) {
            try {
                Class.forName(cls.getName(), true, cls.getClassLoader());
                x8Var = zzc.get(cls);
            } catch (ClassNotFoundException e8) {
                throw new IllegalStateException("Class initialization cannot fail.", e8);
            }
        }
        if (x8Var == null) {
            x8Var = (T) ((x8) yb.b(cls)).r(f.f12667f, null, null);
            if (x8Var == null) {
                throw new IllegalStateException();
            }
            zzc.put(cls, x8Var);
        }
        return (T) x8Var;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static d9 p(d9 d9Var) {
        int size = d9Var.size();
        return d9Var.o(size == 0 ? 10 : size << 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static <E> g9<E> q(g9<E> g9Var) {
        int size = g9Var.size();
        return g9Var.c(size == 0 ? 10 : size << 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Object s(ia iaVar, String str, Object[] objArr) {
        return new va(iaVar, str, objArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object u(Method method, Object obj, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e8) {
            throw new RuntimeException("Couldn't use Java reflection to implement protocol message reflection.", e8);
        } catch (InvocationTargetException e9) {
            Throwable cause = e9.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            throw new RuntimeException("Unexpected exception thrown by generated accessor method.", cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static <T extends x8<?, ?>> void v(Class<T> cls, T t8) {
        t8.F();
        zzc.put(cls, t8);
    }

    protected static final <T extends x8<T, ?>> boolean w(T t8, boolean z4) {
        byte byteValue = ((Byte) t8.r(f.f12662a, null, null)).byteValue();
        if (byteValue == 1) {
            return true;
        }
        if (byteValue == 0) {
            return false;
        }
        boolean e8 = ua.a().c(t8).e(t8);
        if (z4) {
            t8.r(f.f12663b, e8 ? t8 : null, null);
        }
        return e8;
    }

    private final int x(xa<?> xaVar) {
        return xaVar == null ? ua.a().c(this).a(this) : xaVar.a(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final MessageType A() {
        return (MessageType) r(f.f12665d, null, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void E() {
        ua.a().c(this).f(this);
        F();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void F() {
        this.zzd &= Integer.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean G() {
        return (this.zzd & Integer.MIN_VALUE) != 0;
    }

    @Override // com.google.android.gms.internal.measurement.g7
    final int a(xa xaVar) {
        if (!G()) {
            if (i() != Integer.MAX_VALUE) {
                return i();
            }
            int x8 = x(xaVar);
            l(x8);
            return x8;
        }
        int x9 = x(xaVar);
        if (x9 >= 0) {
            return x9;
        }
        throw new IllegalStateException("serialized size must be non-negative, was " + x9);
    }

    @Override // com.google.android.gms.internal.measurement.ia
    public final void b(zzja zzjaVar) {
        ua.a().c(this).d(this, h8.O(zzjaVar));
    }

    @Override // com.google.android.gms.internal.measurement.ka
    public final /* synthetic */ ia c() {
        return (x8) r(f.f12667f, null, null);
    }

    @Override // com.google.android.gms.internal.measurement.ia
    public final /* synthetic */ ha d() {
        return ((a) r(f.f12666e, null, null)).m(this);
    }

    @Override // com.google.android.gms.internal.measurement.ia
    public final /* synthetic */ ha e() {
        return (a) r(f.f12666e, null, null);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            return ua.a().c(this).h(this, (x8) obj);
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.ia
    public final int f() {
        return a(null);
    }

    @Override // com.google.android.gms.internal.measurement.ka
    public final boolean g() {
        return w(this, true);
    }

    public int hashCode() {
        if (G()) {
            return m();
        }
        if (this.zza == 0) {
            this.zza = m();
        }
        return this.zza;
    }

    @Override // com.google.android.gms.internal.measurement.g7
    final int i() {
        return this.zzd & Integer.MAX_VALUE;
    }

    @Override // com.google.android.gms.internal.measurement.g7
    final void l(int i8) {
        if (i8 >= 0) {
            this.zzd = (i8 & Integer.MAX_VALUE) | (this.zzd & Integer.MIN_VALUE);
            return;
        }
        throw new IllegalStateException("serialized size must be non-negative, was " + i8);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract Object r(int i8, Object obj, Object obj2);

    public String toString() {
        return ja.a(this, super.toString());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final <MessageType extends x8<MessageType, BuilderType>, BuilderType extends a<MessageType, BuilderType>> BuilderType y() {
        return (BuilderType) r(f.f12666e, null, null);
    }

    public final BuilderType z() {
        return (BuilderType) ((a) r(f.f12666e, null, null)).m(this);
    }
}
