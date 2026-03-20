package n6;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.concurrent.ConcurrentHashMap;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h {

    /* renamed from: b  reason: collision with root package name */
    private static final d f22183b = new d("LibraryVersion", BuildConfig.FLAVOR);

    /* renamed from: c  reason: collision with root package name */
    private static final h f22184c = new h();

    /* renamed from: a  reason: collision with root package name */
    private final ConcurrentHashMap f22185a = new ConcurrentHashMap();

    protected h() {
    }

    public static h a() {
        return f22184c;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0096  */
    /* JADX WARN: Type inference failed for: r3v11 */
    /* JADX WARN: Type inference failed for: r3v14 */
    /* JADX WARN: Type inference failed for: r3v15 */
    /* JADX WARN: Type inference failed for: r3v16 */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6, types: [java.lang.Object, java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v7, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v9 */
    @java.lang.Deprecated
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String b(java.lang.String r9) {
        /*
            r8 = this;
            java.lang.String r0 = "Failed to get app version for libraryName: "
            java.lang.String r1 = "LibraryVersion"
            java.lang.String r2 = "Please provide a valid libraryName"
            n6.j.g(r9, r2)
            java.util.concurrent.ConcurrentHashMap r2 = r8.f22185a
            boolean r2 = r2.containsKey(r9)
            if (r2 == 0) goto L1a
            java.util.concurrent.ConcurrentHashMap r0 = r8.f22185a
            java.lang.Object r9 = r0.get(r9)
            java.lang.String r9 = (java.lang.String) r9
            return r9
        L1a:
            java.util.Properties r2 = new java.util.Properties
            r2.<init>()
            r3 = 0
            java.lang.String r4 = "/%s.properties"
            r5 = 1
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch: java.lang.Throwable -> L74 java.io.IOException -> L76
            r6 = 0
            r5[r6] = r9     // Catch: java.lang.Throwable -> L74 java.io.IOException -> L76
            java.lang.String r4 = java.lang.String.format(r4, r5)     // Catch: java.lang.Throwable -> L74 java.io.IOException -> L76
            java.lang.Class<n6.h> r5 = n6.h.class
            java.io.InputStream r4 = r5.getResourceAsStream(r4)     // Catch: java.lang.Throwable -> L74 java.io.IOException -> L76
            if (r4 == 0) goto L57
            r2.load(r4)     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            java.lang.String r5 = "version"
            java.lang.String r3 = r2.getProperty(r5, r3)     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            n6.d r2 = n6.h.f22183b     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            r5.<init>()     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            r5.append(r9)     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            java.lang.String r6 = " version is "
            r5.append(r6)     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            r5.append(r3)     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            r2.e(r1, r5)     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            goto L8f
        L57:
            n6.d r2 = n6.h.f22183b     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            r5.<init>()     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            r5.append(r0)     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            r5.append(r9)     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            r2.f(r1, r5)     // Catch: java.lang.Throwable -> L6c java.io.IOException -> L6f
            goto L8f
        L6c:
            r9 = move-exception
            r3 = r4
            goto La5
        L6f:
            r2 = move-exception
            r7 = r4
            r4 = r3
            r3 = r7
            goto L78
        L74:
            r9 = move-exception
            goto La5
        L76:
            r2 = move-exception
            r4 = r3
        L78:
            n6.d r5 = n6.h.f22183b     // Catch: java.lang.Throwable -> L74
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L74
            r6.<init>()     // Catch: java.lang.Throwable -> L74
            r6.append(r0)     // Catch: java.lang.Throwable -> L74
            r6.append(r9)     // Catch: java.lang.Throwable -> L74
            java.lang.String r0 = r6.toString()     // Catch: java.lang.Throwable -> L74
            r5.d(r1, r0, r2)     // Catch: java.lang.Throwable -> L74
            r7 = r4
            r4 = r3
            r3 = r7
        L8f:
            if (r4 == 0) goto L94
            u6.j.a(r4)
        L94:
            if (r3 != 0) goto L9f
            n6.d r0 = n6.h.f22183b
            java.lang.String r2 = ".properties file is dropped during release process. Failure to read app version is expected during Google internal testing where locally-built libraries are used"
            r0.b(r1, r2)
            java.lang.String r3 = "UNKNOWN"
        L9f:
            java.util.concurrent.ConcurrentHashMap r0 = r8.f22185a
            r0.put(r9, r3)
            return r3
        La5:
            if (r3 == 0) goto Laa
            u6.j.a(r3)
        Laa:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: n6.h.b(java.lang.String):java.lang.String");
    }
}
