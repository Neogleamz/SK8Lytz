package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import com.google.android.gms.internal.mlkit_vision_barcode_bundled.j2;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class p2<MessageType extends p2<MessageType, BuilderType>, BuilderType extends j2<MessageType, BuilderType>> extends y0<MessageType, BuilderType> {
    private static final Map zza = new ConcurrentHashMap();
    private int zzd = -1;
    protected i5 zzc = i5.c();

    /* JADX INFO: Access modifiers changed from: protected */
    public static void B(Class cls, p2 p2Var) {
        p2Var.A();
        zza.put(cls, p2Var);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static final boolean D(p2 p2Var, boolean z4) {
        byte byteValue = ((Byte) p2Var.H(1, null, null)).byteValue();
        if (byteValue == 1) {
            return true;
        }
        if (byteValue == 0) {
            return false;
        }
        boolean f5 = g4.a().b(p2Var.getClass()).f(p2Var);
        if (z4) {
            p2Var.H(2, true != f5 ? null : p2Var, null);
        }
        return f5;
    }

    private final int F(r4 r4Var) {
        if (r4Var == null) {
            return g4.a().b(getClass()).a(this);
        }
        return r4Var.a(this);
    }

    private static p2 G(p2 p2Var, byte[] bArr, int i8, int i9, b2 b2Var) {
        p2 o5 = p2Var.o();
        try {
            r4 b9 = g4.a().b(o5.getClass());
            b9.h(o5, bArr, 0, i9, new c1(b2Var));
            b9.c(o5);
            return o5;
        } catch (zzeo e8) {
            e8.f(o5);
            throw e8;
        } catch (zzgx e9) {
            zzeo a9 = e9.a();
            a9.f(o5);
            throw a9;
        } catch (IOException e10) {
            if (e10.getCause() instanceof zzeo) {
                throw ((zzeo) e10.getCause());
            }
            zzeo zzeoVar = new zzeo(e10);
            zzeoVar.f(o5);
            throw zzeoVar;
        } catch (IndexOutOfBoundsException unused) {
            zzeo g8 = zzeo.g();
            g8.f(o5);
            throw g8;
        }
    }

    public static o2 l(x3 x3Var, Object obj, x3 x3Var2, s2 s2Var, int i8, zzho zzhoVar, Class cls) {
        return new o2(x3Var, obj, x3Var2, new n2(null, i8, zzhoVar, false, false), cls);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static p2 n(Class cls) {
        Map map = zza;
        p2 p2Var = (p2) map.get(cls);
        if (p2Var == null) {
            try {
                Class.forName(cls.getName(), true, cls.getClassLoader());
                p2Var = (p2) map.get(cls);
            } catch (ClassNotFoundException e8) {
                throw new IllegalStateException("Class initialization cannot fail.", e8);
            }
        }
        if (p2Var == null) {
            p2Var = (p2) ((p2) s5.j(cls)).H(6, null, null);
            if (p2Var == null) {
                throw new IllegalStateException();
            }
            map.put(cls, p2Var);
        }
        return p2Var;
    }

    protected static p2 p(p2 p2Var, byte[] bArr, b2 b2Var) {
        p2 G = G(p2Var, bArr, 0, bArr.length, b2Var);
        if (G == null || G.m()) {
            return G;
        }
        zzeo a9 = new zzgx(G).a();
        a9.f(G);
        throw a9;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static u2 q() {
        return h2.g();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static u2 r(u2 u2Var) {
        int size = u2Var.size();
        return u2Var.m(size == 0 ? 10 : size + size);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static v2 s() {
        return q2.h();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static x2 t() {
        return h4.g();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static x2 w(x2 x2Var) {
        int size = x2Var.size();
        return x2Var.z(size == 0 ? 10 : size + size);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object x(Method method, Object obj, Object... objArr) {
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
    public static Object y(x3 x3Var, String str, Object[] objArr) {
        return new i4(x3Var, str, objArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void A() {
        this.zzd &= Integer.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void C(int i8) {
        this.zzd = (this.zzd & Integer.MIN_VALUE) | Integer.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean E() {
        return (this.zzd & Integer.MIN_VALUE) != 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract Object H(int i8, Object obj, Object obj2);

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.x3
    public final int b() {
        int i8;
        if (E()) {
            i8 = F(null);
            if (i8 < 0) {
                throw new IllegalStateException("serialized size must be non-negative, was " + i8);
            }
        } else {
            i8 = this.zzd & Integer.MAX_VALUE;
            if (i8 == Integer.MAX_VALUE) {
                i8 = F(null);
                if (i8 < 0) {
                    throw new IllegalStateException("serialized size must be non-negative, was " + i8);
                }
                this.zzd = (this.zzd & Integer.MIN_VALUE) | i8;
            }
        }
        return i8;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.x3
    public final void d(w1 w1Var) {
        g4.a().b(getClass()).e(this, x1.L(w1Var));
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.x3
    public final /* synthetic */ w3 e() {
        return (j2) H(5, null, null);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            return g4.a().b(getClass()).i(this, (p2) obj);
        }
        return false;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.x3
    public final /* synthetic */ w3 f() {
        j2 j2Var = (j2) H(5, null, null);
        j2Var.k(this);
        return j2Var;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y0
    final int g(r4 r4Var) {
        if (E()) {
            int F = F(r4Var);
            if (F >= 0) {
                return F;
            }
            throw new IllegalStateException("serialized size must be non-negative, was " + F);
        }
        int i8 = this.zzd & Integer.MAX_VALUE;
        if (i8 != Integer.MAX_VALUE) {
            return i8;
        }
        int F2 = F(r4Var);
        if (F2 >= 0) {
            this.zzd = (this.zzd & Integer.MIN_VALUE) | F2;
            return F2;
        }
        throw new IllegalStateException("serialized size must be non-negative, was " + F2);
    }

    final int h() {
        return g4.a().b(getClass()).b(this);
    }

    public final int hashCode() {
        if (E()) {
            return h();
        }
        int i8 = this.zzb;
        if (i8 == 0) {
            int h8 = h();
            this.zzb = h8;
            return h8;
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final j2 j() {
        return (j2) H(5, null, null);
    }

    public final j2 k() {
        j2 j2Var = (j2) H(5, null, null);
        j2Var.k(this);
        return j2Var;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y3
    public final boolean m() {
        return D(this, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final p2 o() {
        return (p2) H(4, null, null);
    }

    public final String toString() {
        return z3.a(this, super.toString());
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y3
    public final /* synthetic */ x3 v() {
        return (p2) H(6, null, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void z() {
        g4.a().b(getClass()).c(this);
        A();
    }
}
