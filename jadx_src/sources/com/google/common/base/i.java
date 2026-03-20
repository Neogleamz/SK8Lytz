package com.google.common.base;

import com.daimajia.numberprogressbar.BuildConfig;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private final String f18824a;

        /* renamed from: b  reason: collision with root package name */
        private final C0147b f18825b;

        /* renamed from: c  reason: collision with root package name */
        private C0147b f18826c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f18827d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f18828e;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a extends C0147b {
            private a() {
                super();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: com.google.common.base.i$b$b  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class C0147b {

            /* renamed from: a  reason: collision with root package name */
            String f18829a;

            /* renamed from: b  reason: collision with root package name */
            Object f18830b;

            /* renamed from: c  reason: collision with root package name */
            C0147b f18831c;

            private C0147b() {
            }
        }

        private b(String str) {
            C0147b c0147b = new C0147b();
            this.f18825b = c0147b;
            this.f18826c = c0147b;
            this.f18827d = false;
            this.f18828e = false;
            this.f18824a = (String) l.n(str);
        }

        private C0147b e() {
            C0147b c0147b = new C0147b();
            this.f18826c.f18831c = c0147b;
            this.f18826c = c0147b;
            return c0147b;
        }

        private b f(Object obj) {
            e().f18830b = obj;
            return this;
        }

        private b g(String str, Object obj) {
            C0147b e8 = e();
            e8.f18830b = obj;
            e8.f18829a = (String) l.n(str);
            return this;
        }

        private a h() {
            a aVar = new a();
            this.f18826c.f18831c = aVar;
            this.f18826c = aVar;
            return aVar;
        }

        private b i(String str, Object obj) {
            a h8 = h();
            h8.f18830b = obj;
            h8.f18829a = (String) l.n(str);
            return this;
        }

        private static boolean k(Object obj) {
            return obj instanceof CharSequence ? ((CharSequence) obj).length() == 0 : obj instanceof Collection ? ((Collection) obj).isEmpty() : obj instanceof Map ? ((Map) obj).isEmpty() : obj instanceof Optional ? !((Optional) obj).c() : obj.getClass().isArray() && Array.getLength(obj) == 0;
        }

        public b a(String str, double d8) {
            return i(str, String.valueOf(d8));
        }

        public b b(String str, int i8) {
            return i(str, String.valueOf(i8));
        }

        public b c(String str, long j8) {
            return i(str, String.valueOf(j8));
        }

        public b d(String str, Object obj) {
            return g(str, obj);
        }

        public b j(Object obj) {
            return f(obj);
        }

        public String toString() {
            boolean z4 = this.f18827d;
            boolean z8 = this.f18828e;
            StringBuilder sb = new StringBuilder(32);
            sb.append(this.f18824a);
            sb.append('{');
            String str = BuildConfig.FLAVOR;
            for (C0147b c0147b = this.f18825b.f18831c; c0147b != null; c0147b = c0147b.f18831c) {
                Object obj = c0147b.f18830b;
                if (!(c0147b instanceof a)) {
                    if (obj == null) {
                        if (z4) {
                        }
                    } else if (z8 && k(obj)) {
                    }
                }
                sb.append(str);
                String str2 = c0147b.f18829a;
                if (str2 != null) {
                    sb.append(str2);
                    sb.append('=');
                }
                if (obj == null || !obj.getClass().isArray()) {
                    sb.append(obj);
                } else {
                    String deepToString = Arrays.deepToString(new Object[]{obj});
                    sb.append((CharSequence) deepToString, 1, deepToString.length() - 1);
                }
                str = ", ";
            }
            sb.append('}');
            return sb.toString();
        }
    }

    public static <T> T a(T t8, T t9) {
        if (t8 != null) {
            return t8;
        }
        Objects.requireNonNull(t9, "Both parameters are null");
        return t9;
    }

    public static b b(Object obj) {
        return new b(obj.getClass().getSimpleName());
    }
}
