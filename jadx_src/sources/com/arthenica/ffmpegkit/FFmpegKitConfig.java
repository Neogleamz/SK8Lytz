package com.arthenica.ffmpegkit;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.util.SparseArray;
import com.daimajia.numberprogressbar.BuildConfig;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class FFmpegKitConfig {

    /* renamed from: a  reason: collision with root package name */
    private static final AtomicInteger f8733a;

    /* renamed from: b  reason: collision with root package name */
    private static Level f8734b;

    /* renamed from: c  reason: collision with root package name */
    private static int f8735c;

    /* renamed from: d  reason: collision with root package name */
    private static final Map<Long, u> f8736d;

    /* renamed from: e  reason: collision with root package name */
    private static final List<u> f8737e;

    /* renamed from: f  reason: collision with root package name */
    private static final Object f8738f;

    /* renamed from: g  reason: collision with root package name */
    private static int f8739g;

    /* renamed from: h  reason: collision with root package name */
    private static ExecutorService f8740h;

    /* renamed from: i  reason: collision with root package name */
    private static m f8741i;

    /* renamed from: j  reason: collision with root package name */
    private static w f8742j;

    /* renamed from: k  reason: collision with root package name */
    private static h f8743k;

    /* renamed from: l  reason: collision with root package name */
    private static k f8744l;

    /* renamed from: m  reason: collision with root package name */
    private static q f8745m;

    /* renamed from: n  reason: collision with root package name */
    private static final SparseArray<c> f8746n;

    /* renamed from: o  reason: collision with root package name */
    private static final SparseArray<c> f8747o;

    /* renamed from: p  reason: collision with root package name */
    private static LogRedirectionStrategy f8748p;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends LinkedHashMap<Long, u> {
        a() {
        }

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<Long, u> entry) {
            return size() > FFmpegKitConfig.f8735c;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static /* synthetic */ class b {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f8749a;

        /* renamed from: b  reason: collision with root package name */
        static final /* synthetic */ int[] f8750b;

        static {
            int[] iArr = new int[Level.values().length];
            f8750b = iArr;
            try {
                iArr[Level.AV_LOG_QUIET.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f8750b[Level.AV_LOG_TRACE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f8750b[Level.AV_LOG_DEBUG.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f8750b[Level.AV_LOG_INFO.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f8750b[Level.AV_LOG_WARNING.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f8750b[Level.AV_LOG_ERROR.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f8750b[Level.AV_LOG_FATAL.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f8750b[Level.AV_LOG_PANIC.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f8750b[Level.AV_LOG_STDERR.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                f8750b[Level.AV_LOG_VERBOSE.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            int[] iArr2 = new int[LogRedirectionStrategy.values().length];
            f8749a = iArr2;
            try {
                iArr2[LogRedirectionStrategy.NEVER_PRINT_LOGS.ordinal()] = 1;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                f8749a[LogRedirectionStrategy.PRINT_LOGS_WHEN_GLOBAL_CALLBACK_NOT_DEFINED.ordinal()] = 2;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                f8749a[LogRedirectionStrategy.PRINT_LOGS_WHEN_SESSION_CALLBACK_NOT_DEFINED.ordinal()] = 3;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                f8749a[LogRedirectionStrategy.PRINT_LOGS_WHEN_NO_CALLBACKS_DEFINED.ordinal()] = 4;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                f8749a[LogRedirectionStrategy.ALWAYS_PRINT_LOGS.ordinal()] = 5;
            } catch (NoSuchFieldError unused15) {
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c {

        /* renamed from: a  reason: collision with root package name */
        private final Integer f8751a;

        /* renamed from: b  reason: collision with root package name */
        private final Uri f8752b;

        /* renamed from: c  reason: collision with root package name */
        private final String f8753c;

        /* renamed from: d  reason: collision with root package name */
        private final ContentResolver f8754d;

        /* renamed from: e  reason: collision with root package name */
        private ParcelFileDescriptor f8755e;

        public c(Integer num, Uri uri, String str, ContentResolver contentResolver) {
            this.f8751a = num;
            this.f8752b = uri;
            this.f8753c = str;
            this.f8754d = contentResolver;
        }

        public ContentResolver a() {
            return this.f8754d;
        }

        public String b() {
            return this.f8753c;
        }

        public ParcelFileDescriptor c() {
            return this.f8755e;
        }

        public Integer d() {
            return this.f8751a;
        }

        public Uri e() {
            return this.f8752b;
        }

        public void f(ParcelFileDescriptor parcelFileDescriptor) {
            this.f8755e = parcelFileDescriptor;
        }
    }

    static {
        j2.a.b("com.arthenica");
        Log.i("ffmpeg-kit", "Loading ffmpeg-kit.");
        r.g(r.f());
        f8733a = new AtomicInteger(1);
        f8734b = Level.f(r.k());
        f8739g = 10;
        f8740h = Executors.newFixedThreadPool(10);
        f8735c = 10;
        f8736d = new a();
        f8737e = new LinkedList();
        f8738f = new Object();
        f8741i = null;
        f8742j = null;
        f8743k = null;
        f8744l = null;
        f8745m = null;
        f8746n = new SparseArray<>();
        f8747o = new SparseArray<>();
        f8748p = LogRedirectionStrategy.PRINT_LOGS_WHEN_NO_CALLBACKS_DEFINED;
        Log.i("ffmpeg-kit", String.format("Loaded ffmpeg-kit-%s-%s-%s-%s.", r.m(), r.c(), r.n(), r.d()));
    }

    private FFmpegKitConfig() {
    }

    public static u A() {
        synchronized (f8738f) {
            List<u> list = f8737e;
            if (list.size() > 0) {
                return list.get(list.size() - 1);
            }
            return null;
        }
    }

    public static Level B() {
        return f8734b;
    }

    public static LogRedirectionStrategy C() {
        return f8748p;
    }

    public static void D(p pVar, int i8) {
        pVar.x();
        try {
            t tVar = new t(nativeFFprobeExecute(pVar.o(), pVar.u()));
            pVar.a(tVar);
            if (tVar.b()) {
                List<l> g8 = pVar.g(i8);
                StringBuilder sb = new StringBuilder();
                int size = g8.size();
                for (int i9 = 0; i9 < size; i9++) {
                    l lVar = g8.get(i9);
                    if (lVar.a() == Level.AV_LOG_STDERR) {
                        sb.append(lVar.b());
                    }
                }
                pVar.C(o.a(sb.toString()));
            }
        } catch (Exception e8) {
            pVar.t(e8);
            Log.w("ffmpeg-kit", String.format("Get media information execute failed: %s.%s", c(pVar.u()), j2.a.a(e8)));
        }
    }

    public static q E() {
        return f8745m;
    }

    public static List<p> F() {
        LinkedList linkedList = new LinkedList();
        synchronized (f8738f) {
            for (u uVar : f8737e) {
                if (uVar.r()) {
                    linkedList.add((p) uVar);
                }
            }
        }
        return linkedList;
    }

    public static String G(Context context, Uri uri, String str) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 19) {
            Log.i("ffmpeg-kit", String.format("getSafParameter is not supported on API Level %d", Integer.valueOf(i8)));
            return BuildConfig.FLAVOR;
        }
        try {
            Cursor query = context.getContentResolver().query(uri, null, null, null, null);
            String string = (query == null || !query.moveToFirst()) ? "unknown" : query.getString(query.getColumnIndex("_display_name"));
            if (query != null) {
                query.close();
            }
            int andIncrement = f8733a.getAndIncrement();
            f8746n.put(andIncrement, new c(Integer.valueOf(andIncrement), uri, str, context.getContentResolver()));
            return "saf:" + andIncrement + "." + q(string);
        } catch (Throwable th) {
            Log.e("ffmpeg-kit", String.format("Failed to get %s column for %s.%s", "_display_name", uri.toString(), j2.a.a(th)));
            throw th;
        }
    }

    public static u H(long j8) {
        u uVar;
        synchronized (f8738f) {
            uVar = f8736d.get(Long.valueOf(j8));
        }
        return uVar;
    }

    public static int I() {
        return f8735c;
    }

    public static List<u> J() {
        LinkedList linkedList;
        synchronized (f8738f) {
            linkedList = new LinkedList(f8737e);
        }
        return linkedList;
    }

    public static List<u> K(SessionState sessionState) {
        LinkedList linkedList = new LinkedList();
        synchronized (f8738f) {
            for (u uVar : f8737e) {
                if (uVar.getState() == sessionState) {
                    linkedList.add(uVar);
                }
            }
        }
        return linkedList;
    }

    public static String L() {
        return N() ? String.format("%s-lts", getNativeVersion()) : getNativeVersion();
    }

    public static void M(Signal signal) {
        ignoreNativeSignal(signal.f());
    }

    public static boolean N() {
        return AbiDetect.isNativeLTSBuild();
    }

    public static String O(Context context) {
        String format;
        File file = new File(context.getCacheDir(), "pipes");
        if (file.exists() || file.mkdirs()) {
            String format2 = MessageFormat.format("{0}{1}{2}{3}", file, File.separator, "fk_pipe_", Integer.valueOf(f8733a.getAndIncrement()));
            h(format2);
            int registerNewNativeFFmpegPipe = registerNewNativeFFmpegPipe(format2);
            if (registerNewNativeFFmpegPipe == 0) {
                return format2;
            }
            format = String.format("Failed to register new FFmpeg pipe %s. Operation failed with rc=%d.", format2, Integer.valueOf(registerNewNativeFFmpegPipe));
        } else {
            format = String.format("Failed to create pipes directory: %s.", file.getAbsolutePath());
        }
        Log.e("ffmpeg-kit", format);
        return null;
    }

    public static int P(String str, String str2) {
        return setNativeEnvironmentVariable(str, str2);
    }

    public static void Q(Context context, String str, Map<String, String> map) {
        R(context, Collections.singletonList(str), map);
    }

    public static void R(Context context, List<String> list, Map<String, String> map) {
        int i8;
        Object obj;
        File file = new File(context.getCacheDir(), "fontconfig");
        if (!file.exists()) {
            Log.d("ffmpeg-kit", String.format("Created temporary font conf directory: %s.", Boolean.valueOf(file.mkdirs())));
        }
        File file2 = new File(file, "fonts.conf");
        if (file2.exists()) {
            Log.d("ffmpeg-kit", String.format("Deleted old temporary font configuration: %s.", Boolean.valueOf(file2.delete())));
        }
        StringBuilder sb = new StringBuilder(BuildConfig.FLAVOR);
        if (map == null || map.size() <= 0) {
            i8 = 0;
        } else {
            map.entrySet();
            i8 = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key != null && value != null && key.trim().length() > 0 && value.trim().length() > 0) {
                    sb.append("    <match target=\"pattern\">\n");
                    sb.append("        <test qual=\"any\" name=\"family\">\n");
                    sb.append(String.format("            <string>%s</string>\n", key));
                    sb.append("        </test>\n");
                    sb.append("        <edit name=\"family\" mode=\"assign\" binding=\"same\">\n");
                    sb.append(String.format("            <string>%s</string>\n", value));
                    sb.append("        </edit>\n");
                    sb.append("    </match>\n");
                    i8++;
                }
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("<?xml version=\"1.0\"?>\n");
        sb2.append("<!DOCTYPE fontconfig SYSTEM \"fonts.dtd\">\n");
        sb2.append("<fontconfig>\n");
        sb2.append("    <dir prefix=\"cwd\">.</dir>\n");
        for (String str : list) {
            sb2.append("    <dir>");
            sb2.append(str);
            sb2.append("</dir>\n");
        }
        sb2.append((CharSequence) sb);
        sb2.append("</fontconfig>\n");
        AtomicReference atomicReference = new AtomicReference();
        try {
            try {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    atomicReference.set(fileOutputStream);
                    fileOutputStream.write(sb2.toString().getBytes());
                    fileOutputStream.flush();
                    Log.d("ffmpeg-kit", String.format("Saved new temporary font configuration with %d font name mappings.", Integer.valueOf(i8)));
                    S(file.getAbsolutePath());
                    Iterator<String> it = list.iterator();
                    while (it.hasNext()) {
                        Log.d("ffmpeg-kit", String.format("Font directory %s registered successfully.", it.next()));
                    }
                } catch (IOException e8) {
                    Log.e("ffmpeg-kit", String.format("Failed to set font directory: %s.%s", Arrays.toString(list.toArray()), j2.a.a(e8)));
                    if (atomicReference.get() == null) {
                        return;
                    }
                    obj = atomicReference.get();
                }
                if (atomicReference.get() == null) {
                    return;
                }
                obj = atomicReference.get();
                ((FileOutputStream) obj).close();
            } catch (Throwable th) {
                if (atomicReference.get() != null) {
                    try {
                        ((FileOutputStream) atomicReference.get()).close();
                    } catch (IOException unused) {
                    }
                }
                throw th;
            }
        } catch (IOException unused2) {
        }
    }

    public static int S(String str) {
        return setNativeEnvironmentVariable("FONTCONFIG_PATH", str);
    }

    public static void T(Level level) {
        if (level != null) {
            f8734b = level;
            setNativeLogLevel(level.h());
        }
    }

    public static void U(LogRedirectionStrategy logRedirectionStrategy) {
        f8748p = logRedirectionStrategy;
    }

    public static void V(int i8) {
        if (i8 >= 1000) {
            throw new IllegalArgumentException("Session history size must not exceed the hard limit!");
        }
        if (i8 > 0) {
            f8735c = i8;
            i();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(u uVar) {
        synchronized (f8738f) {
            Map<Long, u> map = f8736d;
            if (!map.containsKey(Long.valueOf(uVar.o()))) {
                map.put(Long.valueOf(uVar.o()), uVar);
                f8737e.add(uVar);
                i();
            }
        }
    }

    public static String c(String[] strArr) {
        if (strArr == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (int i8 = 0; i8 < strArr.length; i8++) {
            if (i8 > 0) {
                sb.append(" ");
            }
            sb.append(strArr[i8]);
        }
        return sb.toString();
    }

    public static void d(g gVar) {
        gVar.w(f8740h.submit(new com.arthenica.ffmpegkit.b(gVar)));
    }

    private static native void disableNativeRedirection();

    public static void e(j jVar) {
        jVar.w(f8740h.submit(new com.arthenica.ffmpegkit.c(jVar)));
    }

    private static native void enableNativeRedirection();

    public static void f(p pVar, int i8) {
        pVar.w(f8740h.submit(new d(pVar, Integer.valueOf(i8))));
    }

    public static void g() {
        synchronized (f8738f) {
            f8737e.clear();
            f8736d.clear();
        }
    }

    private static native String getNativeBuildDate();

    private static native String getNativeFFmpegVersion();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int getNativeLogLevel();

    private static native String getNativeVersion();

    public static void h(String str) {
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
    }

    private static void i() {
        while (true) {
            List<u> list = f8737e;
            if (list.size() <= f8735c) {
                return;
            }
            try {
                u remove = list.remove(0);
                if (remove != null) {
                    f8736d.remove(Long.valueOf(remove.o()));
                }
            } catch (IndexOutOfBoundsException unused) {
            }
        }
    }

    private static native void ignoreNativeSignal(int i8);

    public static void j() {
        disableNativeRedirection();
    }

    public static void k(h hVar) {
        f8743k = hVar;
    }

    public static void l(k kVar) {
        f8744l = kVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x005f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:56:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void log(long r5, int r7, byte[] r8) {
        /*
            com.arthenica.ffmpegkit.Level r0 = com.arthenica.ffmpegkit.Level.f(r7)
            java.lang.String r1 = new java.lang.String
            r1.<init>(r8)
            com.arthenica.ffmpegkit.l r8 = new com.arthenica.ffmpegkit.l
            r8.<init>(r5, r0, r1)
            com.arthenica.ffmpegkit.LogRedirectionStrategy r2 = com.arthenica.ffmpegkit.FFmpegKitConfig.f8748p
            com.arthenica.ffmpegkit.Level r3 = com.arthenica.ffmpegkit.FFmpegKitConfig.f8734b
            com.arthenica.ffmpegkit.Level r4 = com.arthenica.ffmpegkit.Level.AV_LOG_QUIET
            if (r3 != r4) goto L1e
            com.arthenica.ffmpegkit.Level r3 = com.arthenica.ffmpegkit.Level.AV_LOG_STDERR
            int r3 = r3.h()
            if (r7 != r3) goto L26
        L1e:
            com.arthenica.ffmpegkit.Level r3 = com.arthenica.ffmpegkit.FFmpegKitConfig.f8734b
            int r3 = r3.h()
            if (r7 <= r3) goto L27
        L26:
            return
        L27:
            com.arthenica.ffmpegkit.u r5 = H(r5)
            r6 = 0
            r7 = 1
            java.lang.String r3 = "ffmpeg-kit"
            if (r5 == 0) goto L5a
            com.arthenica.ffmpegkit.LogRedirectionStrategy r2 = r5.k()
            r5.q(r8)
            com.arthenica.ffmpegkit.m r4 = r5.n()
            if (r4 == 0) goto L5a
            com.arthenica.ffmpegkit.m r5 = r5.n()     // Catch: java.lang.Exception -> L46
            r5.a(r8)     // Catch: java.lang.Exception -> L46
            goto L58
        L46:
            r5 = move-exception
            java.lang.Object[] r4 = new java.lang.Object[r7]
            java.lang.String r5 = j2.a.a(r5)
            r4[r6] = r5
            java.lang.String r5 = "Exception thrown inside session log callback.%s"
            java.lang.String r5 = java.lang.String.format(r5, r4)
            android.util.Log.e(r3, r5)
        L58:
            r5 = r7
            goto L5b
        L5a:
            r5 = r6
        L5b:
            com.arthenica.ffmpegkit.m r4 = com.arthenica.ffmpegkit.FFmpegKitConfig.f8741i
            if (r4 == 0) goto L76
            r4.a(r8)     // Catch: java.lang.Exception -> L63
            goto L75
        L63:
            r8 = move-exception
            java.lang.Object[] r4 = new java.lang.Object[r7]
            java.lang.String r8 = j2.a.a(r8)
            r4[r6] = r8
            java.lang.String r6 = "Exception thrown inside global log callback.%s"
            java.lang.String r6 = java.lang.String.format(r6, r4)
            android.util.Log.e(r3, r6)
        L75:
            r6 = r7
        L76:
            int[] r8 = com.arthenica.ffmpegkit.FFmpegKitConfig.b.f8749a
            int r2 = r2.ordinal()
            r8 = r8[r2]
            if (r8 == r7) goto Lb3
            r7 = 2
            if (r8 == r7) goto L92
            r7 = 3
            if (r8 == r7) goto L8f
            r7 = 4
            if (r8 == r7) goto L8a
            goto L95
        L8a:
            if (r6 != 0) goto L8e
            if (r5 == 0) goto L95
        L8e:
            return
        L8f:
            if (r5 == 0) goto L95
            return
        L92:
            if (r6 == 0) goto L95
            return
        L95:
            int[] r5 = com.arthenica.ffmpegkit.FFmpegKitConfig.b.f8750b
            int r6 = r0.ordinal()
            r5 = r5[r6]
            switch(r5) {
                case 1: goto Lb3;
                case 2: goto Lb0;
                case 3: goto Lb0;
                case 4: goto Lac;
                case 5: goto La8;
                case 6: goto La4;
                case 7: goto La4;
                case 8: goto La4;
                default: goto La0;
            }
        La0:
            android.util.Log.v(r3, r1)
            goto Lb3
        La4:
            android.util.Log.e(r3, r1)
            goto Lb3
        La8:
            android.util.Log.w(r3, r1)
            goto Lb3
        Lac:
            android.util.Log.i(r3, r1)
            goto Lb3
        Lb0:
            android.util.Log.d(r3, r1)
        Lb3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.arthenica.ffmpegkit.FFmpegKitConfig.log(long, int, byte[]):void");
    }

    public static void m(m mVar) {
        f8741i = mVar;
    }

    public static native int messagesInTransmit(long j8);

    public static void n(q qVar) {
        f8745m = qVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native void nativeFFmpegCancel(long j8);

    private static native int nativeFFmpegExecute(long j8, String[] strArr);

    static native int nativeFFprobeExecute(long j8, String[] strArr);

    public static void o() {
        enableNativeRedirection();
    }

    public static void p(w wVar) {
        f8742j = wVar;
    }

    static String q(String str) {
        try {
            return new StringTokenizer(str.lastIndexOf(".") >= 0 ? str.substring(str.lastIndexOf(".")) : str, " .").nextToken();
        } catch (Exception e8) {
            Log.w("ffmpeg-kit", String.format("Failed to extract extension from saf display name: %s.%s", str, j2.a.a(e8)));
            return "raw";
        }
    }

    public static void r(g gVar) {
        gVar.x();
        try {
            gVar.a(new t(nativeFFmpegExecute(gVar.o(), gVar.u())));
        } catch (Exception e8) {
            gVar.t(e8);
            Log.w("ffmpeg-kit", String.format("FFmpeg execute failed: %s.%s", c(gVar.u()), j2.a.a(e8)));
        }
    }

    private static native int registerNewNativeFFmpegPipe(String str);

    public static void s(j jVar) {
        jVar.x();
        try {
            jVar.a(new t(nativeFFprobeExecute(jVar.o(), jVar.u())));
        } catch (Exception e8) {
            jVar.t(e8);
            Log.w("ffmpeg-kit", String.format("FFprobe execute failed: %s.%s", c(jVar.u()), j2.a.a(e8)));
        }
    }

    private static int safClose(int i8) {
        String format;
        try {
            SparseArray<c> sparseArray = f8747o;
            c cVar = sparseArray.get(i8);
            if (cVar != null) {
                ParcelFileDescriptor c9 = cVar.c();
                if (c9 != null) {
                    sparseArray.delete(i8);
                    f8746n.delete(cVar.d().intValue());
                    c9.close();
                    return 1;
                }
                format = String.format("ParcelFileDescriptor for SAF fd %d not found.", Integer.valueOf(i8));
            } else {
                format = String.format("SAF fd %d not found.", Integer.valueOf(i8));
            }
            Log.e("ffmpeg-kit", format);
        } catch (Throwable th) {
            Log.e("ffmpeg-kit", String.format("Failed to close SAF fd: %d.%s", Integer.valueOf(i8), j2.a.a(th)));
        }
        return 0;
    }

    private static int safOpen(int i8) {
        c cVar;
        try {
            cVar = f8746n.get(i8);
        } catch (Throwable th) {
            Log.e("ffmpeg-kit", String.format("Failed to open SAF id: %d.%s", Integer.valueOf(i8), j2.a.a(th)));
        }
        if (cVar == null) {
            Log.e("ffmpeg-kit", String.format("SAF id %d not found.", Integer.valueOf(i8)));
            return 0;
        }
        ParcelFileDescriptor openFileDescriptor = cVar.a().openFileDescriptor(cVar.e(), cVar.b());
        cVar.f(openFileDescriptor);
        int fd = openFileDescriptor.getFd();
        f8747o.put(fd, cVar);
        return fd;
    }

    private static native int setNativeEnvironmentVariable(String str, String str2);

    private static native void setNativeLogLevel(int i8);

    private static void statistics(long j8, int i8, float f5, float f8, long j9, double d8, double d9, double d10) {
        v vVar = new v(j8, i8, f5, f8, j9, d8, d9, d10);
        u H = H(j8);
        if (H != null && H.c()) {
            g gVar = (g) H;
            gVar.z(vVar);
            if (gVar.E() != null) {
                try {
                    gVar.E().a(vVar);
                } catch (Exception e8) {
                    Log.e("ffmpeg-kit", String.format("Exception thrown inside session statistics callback.%s", j2.a.a(e8)));
                }
            }
        }
        w wVar = f8742j;
        if (wVar != null) {
            try {
                wVar.a(vVar);
            } catch (Exception e9) {
                Log.e("ffmpeg-kit", String.format("Exception thrown inside global statistics callback.%s", j2.a.a(e9)));
            }
        }
    }

    public static String t() {
        return getNativeBuildDate();
    }

    public static h u() {
        return f8743k;
    }

    public static List<g> v() {
        LinkedList linkedList = new LinkedList();
        synchronized (f8738f) {
            for (u uVar : f8737e) {
                if (uVar.c()) {
                    linkedList.add((g) uVar);
                }
            }
        }
        return linkedList;
    }

    public static String w() {
        return getNativeFFmpegVersion();
    }

    public static k x() {
        return f8744l;
    }

    public static List<j> y() {
        LinkedList linkedList = new LinkedList();
        synchronized (f8738f) {
            for (u uVar : f8737e) {
                if (uVar.p()) {
                    linkedList.add((j) uVar);
                }
            }
        }
        return linkedList;
    }

    public static u z() {
        synchronized (f8738f) {
            for (int size = f8737e.size() - 1; size >= 0; size--) {
                u uVar = f8737e.get(size);
                if (uVar.getState() == SessionState.COMPLETED) {
                    return uVar;
                }
            }
            return null;
        }
    }
}
