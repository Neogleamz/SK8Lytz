package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import y1.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class VersionedParcel {

    /* renamed from: a  reason: collision with root package name */
    protected final k0.a<String, Method> f7735a;

    /* renamed from: b  reason: collision with root package name */
    protected final k0.a<String, Method> f7736b;

    /* renamed from: c  reason: collision with root package name */
    protected final k0.a<String, Class> f7737c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class ParcelException extends RuntimeException {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends ObjectInputStream {
        a(InputStream inputStream) {
            super(inputStream);
        }

        @Override // java.io.ObjectInputStream
        protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) {
            Class<?> cls = Class.forName(objectStreamClass.getName(), false, getClass().getClassLoader());
            return cls != null ? cls : super.resolveClass(objectStreamClass);
        }
    }

    public VersionedParcel(k0.a<String, Method> aVar, k0.a<String, Method> aVar2, k0.a<String, Class> aVar3) {
        this.f7735a = aVar;
        this.f7736b = aVar2;
        this.f7737c = aVar3;
    }

    private <T> void T(Collection<T> collection) {
        if (collection == null) {
            X(-1);
            return;
        }
        int size = collection.size();
        X(size);
        if (size > 0) {
            int e8 = e(collection.iterator().next());
            X(e8);
            switch (e8) {
                case 1:
                    for (T t8 : collection) {
                        l0(t8);
                    }
                    return;
                case 2:
                    for (T t9 : collection) {
                        c0(t9);
                    }
                    return;
                case 3:
                    for (T t10 : collection) {
                        e0(t10);
                    }
                    return;
                case 4:
                    for (T t11 : collection) {
                        g0(t11);
                    }
                    return;
                case 5:
                    for (T t12 : collection) {
                        i0(t12);
                    }
                    return;
                case 6:
                default:
                    return;
                case 7:
                    for (T t13 : collection) {
                        X(t13.intValue());
                    }
                    return;
                case 8:
                    for (T t14 : collection) {
                        V(t14.floatValue());
                    }
                    return;
            }
        }
    }

    private <T> void U(Collection<T> collection, int i8) {
        J(i8);
        T(collection);
    }

    private Class c(Class<? extends b> cls) {
        Class cls2 = this.f7737c.get(cls.getName());
        if (cls2 == null) {
            Class<?> cls3 = Class.forName(String.format("%s.%sParcelizer", cls.getPackage().getName(), cls.getSimpleName()), false, cls.getClassLoader());
            this.f7737c.put(cls.getName(), cls3);
            return cls3;
        }
        return cls2;
    }

    private Method d(String str) {
        Method method = this.f7735a.get(str);
        if (method == null) {
            System.currentTimeMillis();
            Method declaredMethod = Class.forName(str, true, VersionedParcel.class.getClassLoader()).getDeclaredMethod("read", VersionedParcel.class);
            this.f7735a.put(str, declaredMethod);
            return declaredMethod;
        }
        return method;
    }

    private <T> int e(T t8) {
        if (t8 instanceof String) {
            return 4;
        }
        if (t8 instanceof Parcelable) {
            return 2;
        }
        if (t8 instanceof b) {
            return 1;
        }
        if (t8 instanceof Serializable) {
            return 3;
        }
        if (t8 instanceof IBinder) {
            return 5;
        }
        if (t8 instanceof Integer) {
            return 7;
        }
        if (t8 instanceof Float) {
            return 8;
        }
        throw new IllegalArgumentException(t8.getClass().getName() + " cannot be VersionedParcelled");
    }

    private void e0(Serializable serializable) {
        if (serializable == null) {
            g0(null);
            return;
        }
        String name = serializable.getClass().getName();
        g0(name);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.close();
            P(byteArrayOutputStream.toByteArray());
        } catch (IOException e8) {
            throw new RuntimeException("VersionedParcelable encountered IOException writing serializable object (name = " + name + ")", e8);
        }
    }

    private Method f(Class cls) {
        Method method = this.f7736b.get(cls.getName());
        if (method == null) {
            Class c9 = c(cls);
            System.currentTimeMillis();
            Method declaredMethod = c9.getDeclaredMethod("write", cls, VersionedParcel.class);
            this.f7736b.put(cls.getName(), declaredMethod);
            return declaredMethod;
        }
        return method;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void n0(b bVar) {
        try {
            g0(c(bVar.getClass()).getName());
        } catch (ClassNotFoundException e8) {
            throw new RuntimeException(bVar.getClass().getSimpleName() + " does not have a Parcelizer", e8);
        }
    }

    private <T, S extends Collection<T>> S p(S s8) {
        int u8 = u();
        if (u8 < 0) {
            return null;
        }
        if (u8 != 0) {
            int u9 = u();
            if (u8 < 0) {
                return null;
            }
            if (u9 == 1) {
                while (u8 > 0) {
                    s8.add(H());
                    u8--;
                }
            } else if (u9 == 2) {
                while (u8 > 0) {
                    s8.add(z());
                    u8--;
                }
            } else if (u9 == 3) {
                while (u8 > 0) {
                    s8.add(B());
                    u8--;
                }
            } else if (u9 == 4) {
                while (u8 > 0) {
                    s8.add(D());
                    u8--;
                }
            } else if (u9 == 5) {
                while (u8 > 0) {
                    s8.add(F());
                    u8--;
                }
            }
        }
        return s8;
    }

    public <T extends Parcelable> T A(T t8, int i8) {
        return !q(i8) ? t8 : (T) z();
    }

    protected Serializable B() {
        String D = D();
        if (D == null) {
            return null;
        }
        try {
            return (Serializable) new a(new ByteArrayInputStream(l())).readObject();
        } catch (IOException e8) {
            throw new RuntimeException("VersionedParcelable encountered IOException reading a Serializable object (name = " + D + ")", e8);
        } catch (ClassNotFoundException e9) {
            throw new RuntimeException("VersionedParcelable encountered ClassNotFoundException reading a Serializable object (name = " + D + ")", e9);
        }
    }

    public <T> Set<T> C(Set<T> set, int i8) {
        return !q(i8) ? set : (Set) p(new k0.b());
    }

    protected abstract String D();

    public String E(String str, int i8) {
        return !q(i8) ? str : D();
    }

    protected abstract IBinder F();

    public IBinder G(IBinder iBinder, int i8) {
        return !q(i8) ? iBinder : F();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <T extends b> T H() {
        String D = D();
        if (D == null) {
            return null;
        }
        return (T) t(D, b());
    }

    public <T extends b> T I(T t8, int i8) {
        return !q(i8) ? t8 : (T) H();
    }

    protected abstract void J(int i8);

    public void K(boolean z4, boolean z8) {
    }

    protected abstract void L(boolean z4);

    public void M(boolean z4, int i8) {
        J(i8);
        L(z4);
    }

    protected abstract void N(Bundle bundle);

    public void O(Bundle bundle, int i8) {
        J(i8);
        N(bundle);
    }

    protected abstract void P(byte[] bArr);

    public void Q(byte[] bArr, int i8) {
        J(i8);
        P(bArr);
    }

    protected abstract void R(CharSequence charSequence);

    public void S(CharSequence charSequence, int i8) {
        J(i8);
        R(charSequence);
    }

    protected abstract void V(float f5);

    public void W(float f5, int i8) {
        J(i8);
        V(f5);
    }

    protected abstract void X(int i8);

    public void Y(int i8, int i9) {
        J(i9);
        X(i8);
    }

    public <T> void Z(List<T> list, int i8) {
        U(list, i8);
    }

    protected abstract void a();

    protected abstract void a0(long j8);

    protected abstract VersionedParcel b();

    public void b0(long j8, int i8) {
        J(i8);
        a0(j8);
    }

    protected abstract void c0(Parcelable parcelable);

    public void d0(Parcelable parcelable, int i8) {
        J(i8);
        c0(parcelable);
    }

    public <T> void f0(Set<T> set, int i8) {
        U(set, i8);
    }

    public boolean g() {
        return false;
    }

    protected abstract void g0(String str);

    protected abstract boolean h();

    public void h0(String str, int i8) {
        J(i8);
        g0(str);
    }

    public boolean i(boolean z4, int i8) {
        return !q(i8) ? z4 : h();
    }

    protected abstract void i0(IBinder iBinder);

    protected abstract Bundle j();

    public void j0(IBinder iBinder, int i8) {
        J(i8);
        i0(iBinder);
    }

    public Bundle k(Bundle bundle, int i8) {
        return !q(i8) ? bundle : j();
    }

    protected <T extends b> void k0(T t8, VersionedParcel versionedParcel) {
        try {
            f(t8.getClass()).invoke(null, t8, versionedParcel);
        } catch (ClassNotFoundException e8) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", e8);
        } catch (IllegalAccessException e9) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", e9);
        } catch (NoSuchMethodException e10) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", e10);
        } catch (InvocationTargetException e11) {
            if (!(e11.getCause() instanceof RuntimeException)) {
                throw new RuntimeException("VersionedParcel encountered InvocationTargetException", e11);
            }
            throw ((RuntimeException) e11.getCause());
        }
    }

    protected abstract byte[] l();

    /* JADX INFO: Access modifiers changed from: protected */
    public void l0(b bVar) {
        if (bVar == null) {
            g0(null);
            return;
        }
        n0(bVar);
        VersionedParcel b9 = b();
        k0(bVar, b9);
        b9.a();
    }

    public byte[] m(byte[] bArr, int i8) {
        return !q(i8) ? bArr : l();
    }

    public void m0(b bVar, int i8) {
        J(i8);
        l0(bVar);
    }

    protected abstract CharSequence n();

    public CharSequence o(CharSequence charSequence, int i8) {
        return !q(i8) ? charSequence : n();
    }

    protected abstract boolean q(int i8);

    protected abstract float r();

    public float s(float f5, int i8) {
        return !q(i8) ? f5 : r();
    }

    protected <T extends b> T t(String str, VersionedParcel versionedParcel) {
        try {
            return (T) d(str).invoke(null, versionedParcel);
        } catch (ClassNotFoundException e8) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", e8);
        } catch (IllegalAccessException e9) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", e9);
        } catch (NoSuchMethodException e10) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", e10);
        } catch (InvocationTargetException e11) {
            if (e11.getCause() instanceof RuntimeException) {
                throw ((RuntimeException) e11.getCause());
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", e11);
        }
    }

    protected abstract int u();

    public int v(int i8, int i9) {
        return !q(i9) ? i8 : u();
    }

    public <T> List<T> w(List<T> list, int i8) {
        return !q(i8) ? list : (List) p(new ArrayList());
    }

    protected abstract long x();

    public long y(long j8, int i8) {
        return !q(i8) ? j8 : x();
    }

    protected abstract <T extends Parcelable> T z();
}
