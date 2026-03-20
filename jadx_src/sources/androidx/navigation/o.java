package androidx.navigation;

import android.os.Bundle;
import android.os.Parcelable;
import java.io.Serializable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class o<T> {

    /* renamed from: b  reason: collision with root package name */
    public static final o<Integer> f6425b = new c(false);

    /* renamed from: c  reason: collision with root package name */
    public static final o<Integer> f6426c = new d(false);

    /* renamed from: d  reason: collision with root package name */
    public static final o<int[]> f6427d = new e(true);

    /* renamed from: e  reason: collision with root package name */
    public static final o<Long> f6428e = new f(false);

    /* renamed from: f  reason: collision with root package name */
    public static final o<long[]> f6429f = new g(true);

    /* renamed from: g  reason: collision with root package name */
    public static final o<Float> f6430g = new h(false);

    /* renamed from: h  reason: collision with root package name */
    public static final o<float[]> f6431h = new i(true);

    /* renamed from: i  reason: collision with root package name */
    public static final o<Boolean> f6432i = new j(false);

    /* renamed from: j  reason: collision with root package name */
    public static final o<boolean[]> f6433j = new k(true);

    /* renamed from: k  reason: collision with root package name */
    public static final o<String> f6434k = new a(true);

    /* renamed from: l  reason: collision with root package name */
    public static final o<String[]> f6435l = new b(true);

    /* renamed from: a  reason: collision with root package name */
    private final boolean f6436a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends o<String> {
        a(boolean z4) {
            super(z4);
        }

        @Override // androidx.navigation.o
        public String c() {
            return "string";
        }

        @Override // androidx.navigation.o
        /* renamed from: j */
        public String b(Bundle bundle, String str) {
            return (String) bundle.get(str);
        }

        @Override // androidx.navigation.o
        /* renamed from: k */
        public String h(String str) {
            return str;
        }

        @Override // androidx.navigation.o
        /* renamed from: l */
        public void i(Bundle bundle, String str, String str2) {
            bundle.putString(str, str2);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends o<String[]> {
        b(boolean z4) {
            super(z4);
        }

        @Override // androidx.navigation.o
        public String c() {
            return "string[]";
        }

        @Override // androidx.navigation.o
        /* renamed from: j */
        public String[] b(Bundle bundle, String str) {
            return (String[]) bundle.get(str);
        }

        @Override // androidx.navigation.o
        /* renamed from: k */
        public String[] h(String str) {
            throw new UnsupportedOperationException("Arrays don't support default values.");
        }

        @Override // androidx.navigation.o
        /* renamed from: l */
        public void i(Bundle bundle, String str, String[] strArr) {
            bundle.putStringArray(str, strArr);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c extends o<Integer> {
        c(boolean z4) {
            super(z4);
        }

        @Override // androidx.navigation.o
        public String c() {
            return "integer";
        }

        @Override // androidx.navigation.o
        /* renamed from: j */
        public Integer b(Bundle bundle, String str) {
            return (Integer) bundle.get(str);
        }

        @Override // androidx.navigation.o
        /* renamed from: k */
        public Integer h(String str) {
            return Integer.valueOf(str.startsWith("0x") ? Integer.parseInt(str.substring(2), 16) : Integer.parseInt(str));
        }

        @Override // androidx.navigation.o
        /* renamed from: l */
        public void i(Bundle bundle, String str, Integer num) {
            bundle.putInt(str, num.intValue());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d extends o<Integer> {
        d(boolean z4) {
            super(z4);
        }

        @Override // androidx.navigation.o
        public String c() {
            return "reference";
        }

        @Override // androidx.navigation.o
        /* renamed from: j */
        public Integer b(Bundle bundle, String str) {
            return (Integer) bundle.get(str);
        }

        @Override // androidx.navigation.o
        /* renamed from: k */
        public Integer h(String str) {
            return Integer.valueOf(str.startsWith("0x") ? Integer.parseInt(str.substring(2), 16) : Integer.parseInt(str));
        }

        @Override // androidx.navigation.o
        /* renamed from: l */
        public void i(Bundle bundle, String str, Integer num) {
            bundle.putInt(str, num.intValue());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class e extends o<int[]> {
        e(boolean z4) {
            super(z4);
        }

        @Override // androidx.navigation.o
        public String c() {
            return "integer[]";
        }

        @Override // androidx.navigation.o
        /* renamed from: j */
        public int[] b(Bundle bundle, String str) {
            return (int[]) bundle.get(str);
        }

        @Override // androidx.navigation.o
        /* renamed from: k */
        public int[] h(String str) {
            throw new UnsupportedOperationException("Arrays don't support default values.");
        }

        @Override // androidx.navigation.o
        /* renamed from: l */
        public void i(Bundle bundle, String str, int[] iArr) {
            bundle.putIntArray(str, iArr);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class f extends o<Long> {
        f(boolean z4) {
            super(z4);
        }

        @Override // androidx.navigation.o
        public String c() {
            return "long";
        }

        @Override // androidx.navigation.o
        /* renamed from: j */
        public Long b(Bundle bundle, String str) {
            return (Long) bundle.get(str);
        }

        @Override // androidx.navigation.o
        /* renamed from: k */
        public Long h(String str) {
            if (str.endsWith("L")) {
                str = str.substring(0, str.length() - 1);
            }
            return Long.valueOf(str.startsWith("0x") ? Long.parseLong(str.substring(2), 16) : Long.parseLong(str));
        }

        @Override // androidx.navigation.o
        /* renamed from: l */
        public void i(Bundle bundle, String str, Long l8) {
            bundle.putLong(str, l8.longValue());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class g extends o<long[]> {
        g(boolean z4) {
            super(z4);
        }

        @Override // androidx.navigation.o
        public String c() {
            return "long[]";
        }

        @Override // androidx.navigation.o
        /* renamed from: j */
        public long[] b(Bundle bundle, String str) {
            return (long[]) bundle.get(str);
        }

        @Override // androidx.navigation.o
        /* renamed from: k */
        public long[] h(String str) {
            throw new UnsupportedOperationException("Arrays don't support default values.");
        }

        @Override // androidx.navigation.o
        /* renamed from: l */
        public void i(Bundle bundle, String str, long[] jArr) {
            bundle.putLongArray(str, jArr);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class h extends o<Float> {
        h(boolean z4) {
            super(z4);
        }

        @Override // androidx.navigation.o
        public String c() {
            return "float";
        }

        @Override // androidx.navigation.o
        /* renamed from: j */
        public Float b(Bundle bundle, String str) {
            return (Float) bundle.get(str);
        }

        @Override // androidx.navigation.o
        /* renamed from: k */
        public Float h(String str) {
            return Float.valueOf(Float.parseFloat(str));
        }

        @Override // androidx.navigation.o
        /* renamed from: l */
        public void i(Bundle bundle, String str, Float f5) {
            bundle.putFloat(str, f5.floatValue());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class i extends o<float[]> {
        i(boolean z4) {
            super(z4);
        }

        @Override // androidx.navigation.o
        public String c() {
            return "float[]";
        }

        @Override // androidx.navigation.o
        /* renamed from: j */
        public float[] b(Bundle bundle, String str) {
            return (float[]) bundle.get(str);
        }

        @Override // androidx.navigation.o
        /* renamed from: k */
        public float[] h(String str) {
            throw new UnsupportedOperationException("Arrays don't support default values.");
        }

        @Override // androidx.navigation.o
        /* renamed from: l */
        public void i(Bundle bundle, String str, float[] fArr) {
            bundle.putFloatArray(str, fArr);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class j extends o<Boolean> {
        j(boolean z4) {
            super(z4);
        }

        @Override // androidx.navigation.o
        public String c() {
            return "boolean";
        }

        @Override // androidx.navigation.o
        /* renamed from: j */
        public Boolean b(Bundle bundle, String str) {
            return (Boolean) bundle.get(str);
        }

        @Override // androidx.navigation.o
        /* renamed from: k */
        public Boolean h(String str) {
            if ("true".equals(str)) {
                return Boolean.TRUE;
            }
            if ("false".equals(str)) {
                return Boolean.FALSE;
            }
            throw new IllegalArgumentException("A boolean NavType only accepts \"true\" or \"false\" values.");
        }

        @Override // androidx.navigation.o
        /* renamed from: l */
        public void i(Bundle bundle, String str, Boolean bool) {
            bundle.putBoolean(str, bool.booleanValue());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class k extends o<boolean[]> {
        k(boolean z4) {
            super(z4);
        }

        @Override // androidx.navigation.o
        public String c() {
            return "boolean[]";
        }

        @Override // androidx.navigation.o
        /* renamed from: j */
        public boolean[] b(Bundle bundle, String str) {
            return (boolean[]) bundle.get(str);
        }

        @Override // androidx.navigation.o
        /* renamed from: k */
        public boolean[] h(String str) {
            throw new UnsupportedOperationException("Arrays don't support default values.");
        }

        @Override // androidx.navigation.o
        /* renamed from: l */
        public void i(Bundle bundle, String str, boolean[] zArr) {
            bundle.putBooleanArray(str, zArr);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class l<D extends Enum> extends p<D> {

        /* renamed from: n  reason: collision with root package name */
        private final Class<D> f6437n;

        public l(Class<D> cls) {
            super(false, cls);
            if (cls.isEnum()) {
                this.f6437n = cls;
                return;
            }
            throw new IllegalArgumentException(cls + " is not an Enum type.");
        }

        @Override // androidx.navigation.o.p, androidx.navigation.o
        public String c() {
            return this.f6437n.getName();
        }

        @Override // androidx.navigation.o.p
        /* renamed from: m */
        public D k(String str) {
            D[] enumConstants;
            for (D d8 : this.f6437n.getEnumConstants()) {
                if (d8.name().equals(str)) {
                    return d8;
                }
            }
            throw new IllegalArgumentException("Enum value " + str + " not found for type " + this.f6437n.getName() + ".");
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class m<D extends Parcelable> extends o<D[]> {

        /* renamed from: m  reason: collision with root package name */
        private final Class<D[]> f6438m;

        public m(Class<D> cls) {
            super(true);
            if (!Parcelable.class.isAssignableFrom(cls)) {
                throw new IllegalArgumentException(cls + " does not implement Parcelable.");
            }
            try {
                this.f6438m = (Class<D[]>) Class.forName("[L" + cls.getName() + ";");
            } catch (ClassNotFoundException e8) {
                throw new RuntimeException(e8);
            }
        }

        @Override // androidx.navigation.o
        public String c() {
            return this.f6438m.getName();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || m.class != obj.getClass()) {
                return false;
            }
            return this.f6438m.equals(((m) obj).f6438m);
        }

        public int hashCode() {
            return this.f6438m.hashCode();
        }

        @Override // androidx.navigation.o
        /* renamed from: j */
        public D[] b(Bundle bundle, String str) {
            return (D[]) ((Parcelable[]) bundle.get(str));
        }

        @Override // androidx.navigation.o
        /* renamed from: k */
        public D[] h(String str) {
            throw new UnsupportedOperationException("Arrays don't support default values.");
        }

        @Override // androidx.navigation.o
        /* renamed from: l */
        public void i(Bundle bundle, String str, D[] dArr) {
            this.f6438m.cast(dArr);
            bundle.putParcelableArray(str, dArr);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class n<D> extends o<D> {

        /* renamed from: m  reason: collision with root package name */
        private final Class<D> f6439m;

        public n(Class<D> cls) {
            super(true);
            if (Parcelable.class.isAssignableFrom(cls) || Serializable.class.isAssignableFrom(cls)) {
                this.f6439m = cls;
                return;
            }
            throw new IllegalArgumentException(cls + " does not implement Parcelable or Serializable.");
        }

        @Override // androidx.navigation.o
        public D b(Bundle bundle, String str) {
            return (D) bundle.get(str);
        }

        @Override // androidx.navigation.o
        public String c() {
            return this.f6439m.getName();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || n.class != obj.getClass()) {
                return false;
            }
            return this.f6439m.equals(((n) obj).f6439m);
        }

        @Override // androidx.navigation.o
        public D h(String str) {
            throw new UnsupportedOperationException("Parcelables don't support default values.");
        }

        public int hashCode() {
            return this.f6439m.hashCode();
        }

        @Override // androidx.navigation.o
        public void i(Bundle bundle, String str, D d8) {
            this.f6439m.cast(d8);
            if (d8 == null || (d8 instanceof Parcelable)) {
                bundle.putParcelable(str, (Parcelable) d8);
            } else if (d8 instanceof Serializable) {
                bundle.putSerializable(str, (Serializable) d8);
            }
        }
    }

    /* renamed from: androidx.navigation.o$o  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0070o<D extends Serializable> extends o<D[]> {

        /* renamed from: m  reason: collision with root package name */
        private final Class<D[]> f6440m;

        public C0070o(Class<D> cls) {
            super(true);
            if (!Serializable.class.isAssignableFrom(cls)) {
                throw new IllegalArgumentException(cls + " does not implement Serializable.");
            }
            try {
                this.f6440m = (Class<D[]>) Class.forName("[L" + cls.getName() + ";");
            } catch (ClassNotFoundException e8) {
                throw new RuntimeException(e8);
            }
        }

        @Override // androidx.navigation.o
        public String c() {
            return this.f6440m.getName();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || C0070o.class != obj.getClass()) {
                return false;
            }
            return this.f6440m.equals(((C0070o) obj).f6440m);
        }

        public int hashCode() {
            return this.f6440m.hashCode();
        }

        @Override // androidx.navigation.o
        /* renamed from: j */
        public D[] b(Bundle bundle, String str) {
            return (D[]) ((Serializable[]) bundle.get(str));
        }

        @Override // androidx.navigation.o
        /* renamed from: k */
        public D[] h(String str) {
            throw new UnsupportedOperationException("Arrays don't support default values.");
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.navigation.o
        /* renamed from: l */
        public void i(Bundle bundle, String str, D[] dArr) {
            this.f6440m.cast(dArr);
            bundle.putSerializable(str, dArr);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class p<D extends Serializable> extends o<D> {

        /* renamed from: m  reason: collision with root package name */
        private final Class<D> f6441m;

        public p(Class<D> cls) {
            super(true);
            if (!Serializable.class.isAssignableFrom(cls)) {
                throw new IllegalArgumentException(cls + " does not implement Serializable.");
            } else if (!cls.isEnum()) {
                this.f6441m = cls;
            } else {
                throw new IllegalArgumentException(cls + " is an Enum. You should use EnumType instead.");
            }
        }

        p(boolean z4, Class<D> cls) {
            super(z4);
            if (Serializable.class.isAssignableFrom(cls)) {
                this.f6441m = cls;
                return;
            }
            throw new IllegalArgumentException(cls + " does not implement Serializable.");
        }

        @Override // androidx.navigation.o
        public String c() {
            return this.f6441m.getName();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof p) {
                return this.f6441m.equals(((p) obj).f6441m);
            }
            return false;
        }

        public int hashCode() {
            return this.f6441m.hashCode();
        }

        @Override // androidx.navigation.o
        /* renamed from: j */
        public D b(Bundle bundle, String str) {
            return (D) bundle.get(str);
        }

        @Override // androidx.navigation.o
        /* renamed from: k */
        public D h(String str) {
            throw new UnsupportedOperationException("Serializables don't support default values.");
        }

        @Override // androidx.navigation.o
        /* renamed from: l */
        public void i(Bundle bundle, String str, D d8) {
            this.f6441m.cast(d8);
            bundle.putSerializable(str, d8);
        }
    }

    o(boolean z4) {
        this.f6436a = z4;
    }

    public static o<?> a(String str, String str2) {
        String str3;
        o<Integer> oVar = f6425b;
        if (oVar.c().equals(str)) {
            return oVar;
        }
        o oVar2 = f6427d;
        if (oVar2.c().equals(str)) {
            return oVar2;
        }
        o<Long> oVar3 = f6428e;
        if (oVar3.c().equals(str)) {
            return oVar3;
        }
        o oVar4 = f6429f;
        if (oVar4.c().equals(str)) {
            return oVar4;
        }
        o<Boolean> oVar5 = f6432i;
        if (oVar5.c().equals(str)) {
            return oVar5;
        }
        o oVar6 = f6433j;
        if (oVar6.c().equals(str)) {
            return oVar6;
        }
        o<String> oVar7 = f6434k;
        if (oVar7.c().equals(str)) {
            return oVar7;
        }
        o oVar8 = f6435l;
        if (oVar8.c().equals(str)) {
            return oVar8;
        }
        o<Float> oVar9 = f6430g;
        if (oVar9.c().equals(str)) {
            return oVar9;
        }
        o oVar10 = f6431h;
        if (oVar10.c().equals(str)) {
            return oVar10;
        }
        o<Integer> oVar11 = f6426c;
        if (oVar11.c().equals(str)) {
            return oVar11;
        }
        if (str == null || str.isEmpty()) {
            return oVar7;
        }
        try {
            if (!str.startsWith(".") || str2 == null) {
                str3 = str;
            } else {
                str3 = str2 + str;
            }
            if (str.endsWith("[]")) {
                str3 = str3.substring(0, str3.length() - 2);
                Class<?> cls = Class.forName(str3);
                if (Parcelable.class.isAssignableFrom(cls)) {
                    return new m(cls);
                }
                if (Serializable.class.isAssignableFrom(cls)) {
                    return new C0070o(cls);
                }
            } else {
                Class<?> cls2 = Class.forName(str3);
                if (Parcelable.class.isAssignableFrom(cls2)) {
                    return new n(cls2);
                }
                if (Enum.class.isAssignableFrom(cls2)) {
                    return new l(cls2);
                }
                if (Serializable.class.isAssignableFrom(cls2)) {
                    return new p(cls2);
                }
            }
            throw new IllegalArgumentException(str3 + " is not Serializable or Parcelable.");
        } catch (ClassNotFoundException e8) {
            throw new RuntimeException(e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static o d(String str) {
        try {
            try {
                try {
                    try {
                        o<Integer> oVar = f6425b;
                        oVar.h(str);
                        return oVar;
                    } catch (IllegalArgumentException unused) {
                        o<Boolean> oVar2 = f6432i;
                        oVar2.h(str);
                        return oVar2;
                    }
                } catch (IllegalArgumentException unused2) {
                    o<Float> oVar3 = f6430g;
                    oVar3.h(str);
                    return oVar3;
                }
            } catch (IllegalArgumentException unused3) {
                o<Long> oVar4 = f6428e;
                oVar4.h(str);
                return oVar4;
            }
        } catch (IllegalArgumentException unused4) {
            return f6434k;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static o e(Object obj) {
        if (obj instanceof Integer) {
            return f6425b;
        }
        if (obj instanceof int[]) {
            return f6427d;
        }
        if (obj instanceof Long) {
            return f6428e;
        }
        if (obj instanceof long[]) {
            return f6429f;
        }
        if (obj instanceof Float) {
            return f6430g;
        }
        if (obj instanceof float[]) {
            return f6431h;
        }
        if (obj instanceof Boolean) {
            return f6432i;
        }
        if (obj instanceof boolean[]) {
            return f6433j;
        }
        if ((obj instanceof String) || obj == null) {
            return f6434k;
        }
        if (obj instanceof String[]) {
            return f6435l;
        }
        if (obj.getClass().isArray() && Parcelable.class.isAssignableFrom(obj.getClass().getComponentType())) {
            return new m(obj.getClass().getComponentType());
        }
        if (obj.getClass().isArray() && Serializable.class.isAssignableFrom(obj.getClass().getComponentType())) {
            return new C0070o(obj.getClass().getComponentType());
        }
        if (obj instanceof Parcelable) {
            return new n(obj.getClass());
        }
        if (obj instanceof Enum) {
            return new l(obj.getClass());
        }
        if (obj instanceof Serializable) {
            return new p(obj.getClass());
        }
        throw new IllegalArgumentException("Object of type " + obj.getClass().getName() + " is not supported for navigation arguments.");
    }

    public abstract T b(Bundle bundle, String str);

    public abstract String c();

    public boolean f() {
        return this.f6436a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public T g(Bundle bundle, String str, String str2) {
        T h8 = h(str2);
        i(bundle, str, h8);
        return h8;
    }

    public abstract T h(String str);

    public abstract void i(Bundle bundle, String str, T t8);

    public String toString() {
        return c();
    }
}
