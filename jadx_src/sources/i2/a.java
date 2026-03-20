package i2;

import com.daimajia.numberprogressbar.BuildConfig;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a {

    /* renamed from: f  reason: collision with root package name */
    static d f20438f;

    /* renamed from: a  reason: collision with root package name */
    static final Set<String> f20433a = Collections.synchronizedSet(new HashSet());

    /* renamed from: b  reason: collision with root package name */
    static final Set<String> f20434b = Collections.synchronizedSet(new HashSet());

    /* renamed from: c  reason: collision with root package name */
    static final Set<String> f20435c = Collections.synchronizedSet(new HashSet());

    /* renamed from: d  reason: collision with root package name */
    static final Set<String> f20436d = Collections.synchronizedSet(new HashSet());

    /* renamed from: e  reason: collision with root package name */
    static boolean f20437e = false;

    /* renamed from: g  reason: collision with root package name */
    static boolean f20439g = false;

    /* renamed from: h  reason: collision with root package name */
    static boolean f20440h = true;

    /* renamed from: i  reason: collision with root package name */
    static boolean f20441i = true;

    public static int a(StringBuilder sb, String str, int i8, StackTraceElement stackTraceElement, boolean z4, boolean z8, String str2) {
        String c9;
        if (i8 > 0) {
            if (f20438f == null) {
                throw new IllegalArgumentException("Stack trace element serializer not initialized.");
            }
            sb.append(str2);
            if (i8 == 1) {
                c9 = f20438f.b(stackTraceElement, z4, z8);
            } else {
                sb.append(String.format("%s%s ... %d more", f20438f.a(stackTraceElement), str, Integer.valueOf(i8 - 1)));
                if (z8) {
                    c9 = f20438f.c(stackTraceElement);
                }
            }
            sb.append(c9);
        }
        return 0;
    }

    public static boolean b(String str, Set<String> set) {
        return c(str, set) != null;
    }

    public static String c(String str, Set<String> set) {
        for (String str2 : set) {
            if (str.startsWith(str2)) {
                return str2;
            }
        }
        return null;
    }

    public static StackTraceElement[] d(f fVar, int i8) {
        ArrayList arrayList = new ArrayList();
        if (fVar != null) {
            e[] d8 = fVar.d();
            for (int i9 = 0; i9 < d8.length && i9 < i8; i9++) {
                arrayList.add(d8[i9].a());
            }
        }
        return (StackTraceElement[]) arrayList.toArray(new StackTraceElement[0]);
    }

    public static StackTraceElement[] e(f fVar, Set<String> set, Set<String> set2) {
        e[] d8;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (fVar != null) {
            for (e eVar : fVar.d()) {
                String className = eVar.a().getClassName();
                if (!k(className)) {
                    if (b(className, set)) {
                        arrayList.addAll(arrayList2);
                        arrayList.add(eVar.a());
                    } else if (!b(className, set2)) {
                        arrayList2.add(eVar.a());
                    }
                }
            }
        }
        if (arrayList.isEmpty()) {
            arrayList.addAll(arrayList2);
        }
        return (StackTraceElement[]) arrayList.toArray(new StackTraceElement[0]);
    }

    public static String f(f fVar) {
        return h(fVar, f20433a, f20434b, f20435c, 0, f20437e, f20439g);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x012b A[LOOP:1: B:41:0x0129->B:42:0x012b, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String g(i2.f r22, java.lang.String r23, boolean r24, boolean r25, java.util.Set<java.lang.String> r26, java.util.Set<java.lang.String> r27, java.util.Set<java.lang.String> r28, int r29, boolean r30, boolean r31, boolean r32, boolean r33) {
        /*
            Method dump skipped, instructions count: 411
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: i2.a.g(i2.f, java.lang.String, boolean, boolean, java.util.Set, java.util.Set, java.util.Set, int, boolean, boolean, boolean, boolean):java.lang.String");
    }

    public static String h(f fVar, Set<String> set, Set<String> set2, Set<String> set3, int i8, boolean z4, boolean z8) {
        return i(fVar, set, set2, set3, i8, z4, z8, f20440h);
    }

    public static String i(f fVar, Set<String> set, Set<String> set2, Set<String> set3, int i8, boolean z4, boolean z8, boolean z9) {
        return j(fVar, false, false, set, set2, set3, i8, z4, z8, z9, f20441i);
    }

    public static String j(f fVar, boolean z4, boolean z8, Set<String> set, Set<String> set2, Set<String> set3, int i8, boolean z9, boolean z10, boolean z11, boolean z12) {
        return g(fVar, BuildConfig.FLAVOR, z4, z8, set, set2, set3, i8, z9, z10, z11, z12);
    }

    public static boolean k(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String l(Class<?> cls) {
        String url;
        int lastIndexOf;
        if (cls != null) {
            try {
                ClassLoader classLoader = cls.getClassLoader();
                URL resource = classLoader.getResource(cls.getName().replace('.', '/') + ".class");
                if (resource == null || (lastIndexOf = (url = resource.toString()).lastIndexOf(33)) <= 0) {
                    return null;
                }
                String substring = url.substring(0, lastIndexOf);
                int lastIndexOf2 = substring.lastIndexOf(47);
                if (lastIndexOf2 > 0) {
                    substring = substring.substring(lastIndexOf2 + 1);
                }
                int lastIndexOf3 = substring.lastIndexOf(92);
                return lastIndexOf3 > 0 ? substring.substring(lastIndexOf3 + 1) : substring;
            } catch (Exception unused) {
                return null;
            }
        }
        return null;
    }

    public static String m(String str, String str2) {
        boolean z4 = str != null;
        boolean z8 = str2 != null;
        if (z4 || z8) {
            StringBuilder sb = new StringBuilder();
            sb.append(" [");
            if (z4) {
                sb.append(str);
            }
            if (z8) {
                if (z4) {
                    if (!str.contains(str2)) {
                        sb.append(":");
                    }
                }
                sb.append(str2);
            }
            sb.append("]");
            return sb.toString();
        }
        return BuildConfig.FLAVOR;
    }

    public static String n(String str) {
        int lastIndexOf;
        return (str != null && (lastIndexOf = str.lastIndexOf(".")) >= 0) ? str.substring(0, lastIndexOf) : BuildConfig.FLAVOR;
    }

    public static void o(String str) {
        f20433a.add(str);
    }

    public static void p(d dVar) {
        f20438f = dVar;
    }

    public static String q(c cVar, Class<?> cls, String str) {
        try {
            Package r02 = cls.getPackage();
            if (r02 != null) {
                return r02.getImplementationVersion();
            }
            Package a9 = cVar.a(cls.getClassLoader(), str);
            if (a9 != null) {
                return a9.getImplementationVersion();
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }
}
