package h2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.arthenica.ffmpegkit.AbiDetect;
import com.arthenica.ffmpegkit.FFmpegKitConfig;
import com.arthenica.ffmpegkit.Level;
import com.arthenica.ffmpegkit.LogRedirectionStrategy;
import com.arthenica.ffmpegkit.SessionState;
import com.arthenica.ffmpegkit.Signal;
import com.arthenica.ffmpegkit.f;
import com.arthenica.ffmpegkit.g;
import com.arthenica.ffmpegkit.i;
import com.arthenica.ffmpegkit.p;
import com.arthenica.ffmpegkit.s;
import com.arthenica.ffmpegkit.t;
import com.arthenica.ffmpegkit.u;
import com.arthenica.ffmpegkit.v;
import com.google.android.gms.dynamite.descriptors.com.google.mlkit.dynamite.barcode.ModuleDescriptor;
import io.flutter.plugin.common.d;
import io.flutter.plugin.common.j;
import io.flutter.plugin.common.l;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import yf.a;
import zf.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class k implements yf.a, zf.a, j.c, d.d, l.a {

    /* renamed from: d  reason: collision with root package name */
    private j f20241d;

    /* renamed from: e  reason: collision with root package name */
    private d f20242e;

    /* renamed from: f  reason: collision with root package name */
    private j.d f20243f;

    /* renamed from: g  reason: collision with root package name */
    private Context f20244g;

    /* renamed from: h  reason: collision with root package name */
    private Activity f20245h;

    /* renamed from: j  reason: collision with root package name */
    private a.b f20246j;

    /* renamed from: k  reason: collision with root package name */
    private c f20247k;

    /* renamed from: l  reason: collision with root package name */
    private d.b f20248l;

    /* renamed from: a  reason: collision with root package name */
    private final AtomicBoolean f20238a = new AtomicBoolean(false);

    /* renamed from: b  reason: collision with root package name */
    private final AtomicBoolean f20239b = new AtomicBoolean(false);

    /* renamed from: c  reason: collision with root package name */
    private final ExecutorService f20240c = Executors.newFixedThreadPool(10);

    /* renamed from: m  reason: collision with root package name */
    private final e f20249m = new e();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f20250a;

        static {
            int[] iArr = new int[LogRedirectionStrategy.values().length];
            f20250a = iArr;
            try {
                iArr[LogRedirectionStrategy.ALWAYS_PRINT_LOGS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f20250a[LogRedirectionStrategy.PRINT_LOGS_WHEN_NO_CALLBACKS_DEFINED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f20250a[LogRedirectionStrategy.PRINT_LOGS_WHEN_GLOBAL_CALLBACK_NOT_DEFINED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f20250a[LogRedirectionStrategy.PRINT_LOGS_WHEN_SESSION_CALLBACK_NOT_DEFINED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f20250a[LogRedirectionStrategy.NEVER_PRINT_LOGS.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public k() {
        Log.d("ffmpeg-kit-flutter", String.format("FFmpegKitFlutterPlugin created %s.", this));
    }

    protected static int C0(Level level) {
        if (level == null) {
            level = Level.AV_LOG_TRACE;
        }
        return level.h();
    }

    protected static int D0(LogRedirectionStrategy logRedirectionStrategy) {
        int i8 = a.f20250a[logRedirectionStrategy.ordinal()];
        if (i8 != 1) {
            if (i8 != 2) {
                if (i8 != 3) {
                    return i8 != 4 ? 4 : 3;
                }
                return 2;
            }
            return 1;
        }
        return 0;
    }

    protected static List<Object> E0(JSONArray jSONArray) {
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < jSONArray.length(); i8++) {
            Object opt = jSONArray.opt(i8);
            if (opt != null) {
                if (opt instanceof JSONArray) {
                    opt = E0((JSONArray) opt);
                } else if (opt instanceof JSONObject) {
                    opt = M0((JSONObject) opt);
                }
                arrayList.add(opt);
            }
        }
        return arrayList;
    }

    protected static List<Map<String, Object>> F0(List<com.arthenica.ffmpegkit.l> list) {
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < list.size(); i8++) {
            arrayList.add(I0(list.get(i8)));
        }
        return arrayList;
    }

    protected static LogRedirectionStrategy G0(int i8) {
        return i8 != 0 ? i8 != 1 ? i8 != 2 ? i8 != 3 ? LogRedirectionStrategy.NEVER_PRINT_LOGS : LogRedirectionStrategy.PRINT_LOGS_WHEN_SESSION_CALLBACK_NOT_DEFINED : LogRedirectionStrategy.PRINT_LOGS_WHEN_GLOBAL_CALLBACK_NOT_DEFINED : LogRedirectionStrategy.PRINT_LOGS_WHEN_NO_CALLBACKS_DEFINED : LogRedirectionStrategy.ALWAYS_PRINT_LOGS;
    }

    protected static long H0(Date date) {
        if (date != null) {
            return date.getTime();
        }
        return 0L;
    }

    protected static Map<String, Object> I0(com.arthenica.ffmpegkit.l lVar) {
        HashMap hashMap = new HashMap();
        hashMap.put("sessionId", Long.valueOf(lVar.c()));
        hashMap.put("level", Integer.valueOf(C0(lVar.a())));
        hashMap.put("message", lVar.b());
        return hashMap;
    }

    protected static Map<String, Object> J0(com.arthenica.ffmpegkit.n nVar) {
        JSONObject a9;
        if (nVar != null) {
            HashMap hashMap = new HashMap();
            return (nVar.a() == null || (a9 = nVar.a()) == null) ? hashMap : M0(a9);
        }
        return null;
    }

    protected static Map<String, Object> K0(u uVar) {
        int i8;
        if (uVar == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("sessionId", Long.valueOf(uVar.o()));
        hashMap.put("createTime", Long.valueOf(H0(uVar.i())));
        hashMap.put("startTime", Long.valueOf(H0(uVar.e())));
        hashMap.put("command", uVar.h());
        if (uVar.c()) {
            i8 = 1;
        } else if (!uVar.p()) {
            if (uVar.r()) {
                com.arthenica.ffmpegkit.n B = ((p) uVar).B();
                if (B != null) {
                    hashMap.put("mediaInformation", J0(B));
                }
                i8 = 3;
            }
            return hashMap;
        } else {
            i8 = 2;
        }
        hashMap.put("type", Integer.valueOf(i8));
        return hashMap;
    }

    protected static Map<String, Object> L0(v vVar) {
        HashMap hashMap = new HashMap();
        if (vVar != null) {
            hashMap.put("sessionId", Long.valueOf(vVar.b()));
            hashMap.put("videoFrameNumber", Integer.valueOf(vVar.g()));
            hashMap.put("videoFps", Float.valueOf(vVar.f()));
            hashMap.put("videoQuality", Float.valueOf(vVar.h()));
            hashMap.put("size", Integer.valueOf((int) (vVar.c() < 2147483647L ? vVar.c() : vVar.c() % 2147483647L)));
            hashMap.put("time", Double.valueOf(vVar.e()));
            hashMap.put("bitrate", Double.valueOf(vVar.a()));
            hashMap.put("speed", Double.valueOf(vVar.d()));
        }
        return hashMap;
    }

    protected static Map<String, Object> M0(JSONObject jSONObject) {
        HashMap hashMap = new HashMap();
        if (jSONObject != null) {
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                Object opt = jSONObject.opt(next);
                if (opt != null) {
                    if (opt instanceof JSONArray) {
                        opt = E0((JSONArray) opt);
                    } else if (opt instanceof JSONObject) {
                        opt = M0((JSONObject) opt);
                    }
                    hashMap.put(next, opt);
                }
            }
        }
        return hashMap;
    }

    protected static List<Map<String, Object>> N0(List<? extends u> list) {
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < list.size(); i8++) {
            arrayList.add(K0(list.get(i8)));
        }
        return arrayList;
    }

    protected static SessionState O0(int i8) {
        return i8 != 0 ? i8 != 1 ? i8 != 2 ? SessionState.COMPLETED : SessionState.FAILED : SessionState.RUNNING : SessionState.CREATED;
    }

    protected static List<Map<String, Object>> P0(List<v> list) {
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < list.size(); i8++) {
            arrayList.add(L0(list.get(i8)));
        }
        return arrayList;
    }

    protected static boolean k0(Integer num) {
        return num != null && num.intValue() >= 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void l0(com.arthenica.ffmpegkit.l lVar) {
        if (this.f20238a.get()) {
            A(lVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void m0(v vVar) {
        if (this.f20239b.get()) {
            C(vVar);
        }
    }

    protected void A(com.arthenica.ffmpegkit.l lVar) {
        HashMap hashMap = new HashMap();
        hashMap.put("FFmpegKitLogCallbackEvent", I0(lVar));
        this.f20249m.l(this.f20248l, hashMap);
    }

    protected void A0(Integer num, j.d dVar) {
        FFmpegKitConfig.U(G0(num.intValue()));
        this.f20249m.m(dVar, null);
    }

    protected void B(u uVar) {
        HashMap hashMap = new HashMap();
        hashMap.put("FFmpegKitCompleteCallbackEvent", K0(uVar));
        this.f20249m.l(this.f20248l, hashMap);
    }

    protected void B0(Integer num, j.d dVar) {
        FFmpegKitConfig.V(num.intValue());
        this.f20249m.m(dVar, null);
    }

    protected void C(v vVar) {
        HashMap hashMap = new HashMap();
        hashMap.put("FFmpegKitStatisticsCallbackEvent", L0(vVar));
        this.f20249m.l(this.f20248l, hashMap);
    }

    protected void D() {
        this.f20238a.compareAndSet(false, true);
    }

    protected void E(j.d dVar) {
        D();
        this.f20249m.m(dVar, null);
    }

    protected void F(j.d dVar) {
        D();
        G();
        FFmpegKitConfig.o();
        this.f20249m.m(dVar, null);
    }

    protected void G() {
        this.f20239b.compareAndSet(false, true);
    }

    protected void H(j.d dVar) {
        G();
        this.f20249m.m(dVar, null);
    }

    protected void I(List<String> list, j.d dVar) {
        this.f20249m.m(dVar, K0(g.A((String[]) list.toArray(new String[0]), null, null, null, LogRedirectionStrategy.NEVER_PRINT_LOGS)));
    }

    protected void J(Integer num, j.d dVar) {
        e eVar;
        String str;
        String str2;
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            eVar = this.f20249m;
            str = "SESSION_NOT_FOUND";
            str2 = "Session not found.";
        } else if (H.c()) {
            this.f20240c.submit(new l((g) H, this.f20249m, dVar));
            return;
        } else {
            eVar = this.f20249m;
            str = "NOT_FFMPEG_SESSION";
            str2 = "A session is found but it does not have the correct type.";
        }
        eVar.e(dVar, str, str2);
    }

    protected void K(Integer num, Integer num2, j.d dVar) {
        e eVar;
        String str;
        String str2;
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            eVar = this.f20249m;
            str = "SESSION_NOT_FOUND";
            str2 = "Session not found.";
        } else if (H.c()) {
            this.f20249m.m(dVar, P0(((g) H).B(k0(num2) ? num2.intValue() : 5000)));
            return;
        } else {
            eVar = this.f20249m;
            str = "NOT_FFMPEG_SESSION";
            str2 = "A session is found but it does not have the correct type.";
        }
        eVar.e(dVar, str, str2);
    }

    protected void L(Integer num, j.d dVar) {
        e eVar;
        String str;
        String str2;
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            eVar = this.f20249m;
            str = "SESSION_NOT_FOUND";
            str2 = "Session not found.";
        } else if (H.c()) {
            this.f20249m.m(dVar, P0(((g) H).D()));
            return;
        } else {
            eVar = this.f20249m;
            str = "NOT_FFMPEG_SESSION";
            str2 = "A session is found but it does not have the correct type.";
        }
        eVar.e(dVar, str, str2);
    }

    protected void M(List<String> list, j.d dVar) {
        this.f20249m.m(dVar, K0(com.arthenica.ffmpegkit.j.z((String[]) list.toArray(new String[0]), null, null, LogRedirectionStrategy.NEVER_PRINT_LOGS)));
    }

    protected void N(Integer num, j.d dVar) {
        e eVar;
        String str;
        String str2;
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            eVar = this.f20249m;
            str = "SESSION_NOT_FOUND";
            str2 = "Session not found.";
        } else if (H.p()) {
            this.f20240c.submit(new m((com.arthenica.ffmpegkit.j) H, this.f20249m, dVar));
            return;
        } else {
            eVar = this.f20249m;
            str = "NOT_FFPROBE_SESSION";
            str2 = "A session is found but it does not have the correct type.";
        }
        eVar.e(dVar, str, str2);
    }

    protected void O(j.d dVar) {
        this.f20249m.m(dVar, AbiDetect.a());
    }

    protected void P(j.d dVar) {
        this.f20249m.m(dVar, FFmpegKitConfig.t());
    }

    protected void Q(j.d dVar) {
        this.f20249m.m(dVar, s.a());
    }

    protected void Q0() {
        S0();
        R0();
        c cVar = this.f20247k;
        if (cVar != null) {
            cVar.d(this);
        }
        this.f20244g = null;
        this.f20245h = null;
        this.f20247k = null;
        Log.d("ffmpeg-kit-flutter", "FFmpegKitFlutterPlugin uninitialized.");
    }

    protected void R(j.d dVar) {
        this.f20249m.m(dVar, N0(f.c()));
    }

    protected void R0() {
        d dVar = this.f20242e;
        if (dVar == null) {
            Log.i("ffmpeg-kit-flutter", "FFmpegKitFlutterPlugin event channel was already uninitialised.");
            return;
        }
        dVar.d((d.d) null);
        this.f20242e = null;
    }

    protected void S(j.d dVar) {
        this.f20249m.m(dVar, FFmpegKitConfig.w());
    }

    protected void S0() {
        j jVar = this.f20241d;
        if (jVar == null) {
            Log.i("ffmpeg-kit-flutter", "FFmpegKitFlutterPlugin method channel was already uninitialised.");
            return;
        }
        jVar.e((j.c) null);
        this.f20241d = null;
    }

    protected void T(j.d dVar) {
        this.f20249m.m(dVar, N0(i.a()));
    }

    protected void T0(String str, String str2, j.d dVar) {
        this.f20240c.submit(new o(str, str2, this.f20249m, dVar));
    }

    protected void U(j.d dVar) {
        this.f20249m.m(dVar, K0(FFmpegKitConfig.z()));
    }

    protected void V(j.d dVar) {
        this.f20249m.m(dVar, K0(FFmpegKitConfig.A()));
    }

    protected void W(j.d dVar) {
        this.f20249m.m(dVar, Integer.valueOf(C0(FFmpegKitConfig.B())));
    }

    protected void X(j.d dVar) {
        this.f20249m.m(dVar, Integer.valueOf(D0(FFmpegKitConfig.C())));
    }

    protected void Y(Integer num, j.d dVar) {
        e eVar;
        String str;
        String str2;
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            eVar = this.f20249m;
            str = "SESSION_NOT_FOUND";
            str2 = "Session not found.";
        } else if (H.r()) {
            this.f20249m.m(dVar, J0(((p) H).B()));
            return;
        } else {
            eVar = this.f20249m;
            str = "NOT_MEDIA_INFORMATION_SESSION";
            str2 = "A session is found but it does not have the correct type.";
        }
        eVar.e(dVar, str, str2);
    }

    protected void Z(j.d dVar) {
        this.f20249m.m(dVar, N0(i.b()));
    }

    protected void a0(j.d dVar) {
        this.f20249m.m(dVar, s.b());
    }

    public void b(Object obj, d.b bVar) {
        this.f20248l = bVar;
        Log.d("ffmpeg-kit-flutter", String.format("FFmpegKitFlutterPlugin %s started listening to events on %s.", this, bVar));
    }

    protected void b0(j.d dVar) {
        this.f20249m.m(dVar, "android");
    }

    protected void c0(String str, String str2, j.d dVar) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 19) {
            Log.i("ffmpeg-kit-flutter", String.format(Locale.getDefault(), "getSafParameter is not supported on API Level %d", Integer.valueOf(i8)));
            this.f20249m.e(dVar, "GET_SAF_PARAMETER_FAILED", String.format(Locale.getDefault(), "getSafParameter is not supported on API Level %d", Integer.valueOf(i8)));
        } else if (this.f20244g == null) {
            Log.w("ffmpeg-kit-flutter", String.format("Cannot getSafParameter using parameters uriString: %s, openMode: %s. Context is null.", str, str2));
            this.f20249m.e(dVar, "INVALID_CONTEXT", "Context is null.");
        } else {
            Uri parse = Uri.parse(str);
            if (parse == null) {
                Log.w("ffmpeg-kit-flutter", String.format("Cannot getSafParameter using parameters uriString: %s, openMode: %s. Uri string cannot be parsed.", str, str2));
                this.f20249m.e(dVar, "GET_SAF_PARAMETER_FAILED", "Uri string cannot be parsed.");
                return;
            }
            String G = FFmpegKitConfig.G(this.f20244g, parse, str2);
            Log.d("ffmpeg-kit-flutter", String.format("getSafParameter using parameters uriString: %s, openMode: %s completed with saf parameter: %s.", str, str2, G));
            this.f20249m.m(dVar, G);
        }
    }

    public void d(Object obj) {
        this.f20248l = null;
        Log.d("ffmpeg-kit-flutter", "FFmpegKitFlutterPlugin stopped listening to events.");
    }

    protected void d0(Integer num, j.d dVar) {
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            this.f20249m.e(dVar, "SESSION_NOT_FOUND", "Session not found.");
        } else {
            this.f20249m.m(dVar, K0(H));
        }
    }

    protected void e(Integer num, Integer num2, j.d dVar) {
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            this.f20249m.e(dVar, "SESSION_NOT_FOUND", "Session not found.");
        } else {
            this.f20249m.m(dVar, F0(H.g(k0(num2) ? num2.intValue() : 5000)));
        }
    }

    protected void e0(j.d dVar) {
        this.f20249m.m(dVar, Integer.valueOf(FFmpegKitConfig.I()));
    }

    protected void f(Integer num, Integer num2, j.d dVar) {
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            this.f20249m.e(dVar, "SESSION_NOT_FOUND", "Session not found.");
        } else {
            this.f20249m.m(dVar, H.d(k0(num2) ? num2.intValue() : 5000));
        }
    }

    protected void f0(j.d dVar) {
        this.f20249m.m(dVar, N0(FFmpegKitConfig.J()));
    }

    protected void g(Integer num, j.d dVar) {
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            this.f20249m.e(dVar, "SESSION_NOT_FOUND", "Session not found.");
        } else {
            this.f20249m.m(dVar, Long.valueOf(H.b()));
        }
    }

    protected void g0(Integer num, j.d dVar) {
        this.f20249m.m(dVar, N0(FFmpegKitConfig.K(O0(num.intValue()))));
    }

    protected void h0(Integer num, j.d dVar) {
        int intValue = num.intValue();
        Signal signal = Signal.SIGINT;
        if (intValue != signal.ordinal()) {
            int intValue2 = num.intValue();
            signal = Signal.SIGQUIT;
            if (intValue2 != signal.ordinal()) {
                int intValue3 = num.intValue();
                signal = Signal.SIGPIPE;
                if (intValue3 != signal.ordinal()) {
                    int intValue4 = num.intValue();
                    signal = Signal.SIGTERM;
                    if (intValue4 != signal.ordinal()) {
                        int intValue5 = num.intValue();
                        signal = Signal.SIGXCPU;
                        if (intValue5 != signal.ordinal()) {
                            signal = null;
                        }
                    }
                }
            }
        }
        if (signal == null) {
            this.f20249m.e(dVar, "INVALID_SIGNAL", "Signal value not supported.");
            return;
        }
        FFmpegKitConfig.M(signal);
        this.f20249m.m(dVar, null);
    }

    protected void i(Integer num, j.d dVar) {
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            this.f20249m.e(dVar, "SESSION_NOT_FOUND", "Session not found.");
            return;
        }
        Date l8 = H.l();
        if (l8 == null) {
            this.f20249m.m(dVar, null);
        } else {
            this.f20249m.m(dVar, Long.valueOf(l8.getTime()));
        }
    }

    protected void i0(io.flutter.plugin.common.c cVar, Context context, Activity activity, l.c cVar2, c cVar3) {
        s0();
        if (this.f20241d == null) {
            j jVar = new j(cVar, "flutter.arthenica.com/ffmpeg_kit");
            this.f20241d = jVar;
            jVar.e(this);
        } else {
            Log.i("ffmpeg-kit-flutter", "FFmpegKitFlutterPlugin method channel was already initialised.");
        }
        if (this.f20242e == null) {
            d dVar = new d(cVar, "flutter.arthenica.com/ffmpeg_kit_event");
            this.f20242e = dVar;
            dVar.d(this);
        } else {
            Log.i("ffmpeg-kit-flutter", "FFmpegKitFlutterPlugin event channel was already initialised.");
        }
        this.f20244g = context;
        this.f20245h = activity;
        if (cVar2 != null) {
            cVar2.a(this);
        } else {
            cVar3.a(this);
        }
        Log.d("ffmpeg-kit-flutter", String.format("FFmpegKitFlutterPlugin %s initialised with context %s and activity %s.", this, context, activity));
    }

    protected void j(Integer num, j.d dVar) {
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            this.f20249m.e(dVar, "SESSION_NOT_FOUND", "Session not found.");
        } else {
            this.f20249m.m(dVar, H.j());
        }
    }

    protected void j0(j.d dVar) {
        this.f20249m.m(dVar, Boolean.valueOf(FFmpegKitConfig.N()));
    }

    protected void k(Integer num, j.d dVar) {
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            this.f20249m.e(dVar, "SESSION_NOT_FOUND", "Session not found.");
            return;
        }
        this.f20249m.m(dVar, F0(H.m()));
    }

    protected void l(Integer num, j.d dVar) {
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            this.f20249m.e(dVar, "SESSION_NOT_FOUND", "Session not found.");
            return;
        }
        t s8 = H.s();
        if (s8 == null) {
            this.f20249m.m(dVar, null);
        } else {
            this.f20249m.m(dVar, Integer.valueOf(s8.a()));
        }
    }

    protected void m(Integer num, j.d dVar) {
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            this.f20249m.e(dVar, "SESSION_NOT_FOUND", "Session not found.");
        } else {
            this.f20249m.m(dVar, Integer.valueOf(H.getState().ordinal()));
        }
    }

    protected void n(Integer num, j.d dVar) {
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            this.f20249m.e(dVar, "SESSION_NOT_FOUND", "Session not found.");
        } else {
            this.f20249m.m(dVar, Boolean.valueOf(H.f()));
        }
    }

    protected void n0(String str, j.d dVar) {
        try {
            this.f20249m.m(dVar, J0(com.arthenica.ffmpegkit.o.a(str)));
        } catch (JSONException e8) {
            Log.i("ffmpeg-kit-flutter", "Parsing MediaInformation failed.", e8);
            this.f20249m.m(dVar, null);
        }
    }

    protected void o(Integer num, j.d dVar) {
        e eVar;
        String str;
        String str2;
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            eVar = this.f20249m;
            str = "SESSION_NOT_FOUND";
            str2 = "Session not found.";
        } else if (H.c()) {
            FFmpegKitConfig.d((g) H);
            this.f20249m.m(dVar, null);
            return;
        } else {
            eVar = this.f20249m;
            str = "NOT_FFMPEG_SESSION";
            str2 = "A session is found but it does not have the correct type.";
        }
        eVar.e(dVar, str, str2);
    }

    protected void o0(String str, j.d dVar) {
        try {
            this.f20249m.m(dVar, J0(com.arthenica.ffmpegkit.o.a(str)));
        } catch (JSONException e8) {
            Log.i("ffmpeg-kit-flutter", "Parsing MediaInformation failed.", e8);
            this.f20249m.e(dVar, "PARSE_FAILED", "Parsing MediaInformation failed with JSON error.");
        }
    }

    public boolean onActivityResult(int i8, int i9, Intent intent) {
        Object[] objArr = new Object[3];
        objArr[0] = Integer.valueOf(i8);
        objArr[1] = Integer.valueOf(i9);
        objArr[2] = intent == null ? null : intent.toString();
        Log.d("ffmpeg-kit-flutter", String.format("selectDocument completed with requestCode: %d, resultCode: %d, data: %s.", objArr));
        if (i8 != 10000 && i8 != 20000) {
            Log.i("ffmpeg-kit-flutter", String.format("FFmpegKitFlutterPlugin ignored unsupported activity result for requestCode: %d.", Integer.valueOf(i8)));
            return false;
        }
        if (i9 != -1) {
            this.f20249m.e(this.f20243f, "SELECT_CANCELLED", String.valueOf(i9));
        } else if (intent == null) {
            this.f20249m.m(this.f20243f, null);
        } else {
            Uri data = intent.getData();
            this.f20249m.m(this.f20243f, data != null ? data.toString() : null);
        }
        return true;
    }

    public void onAttachedToActivity(c cVar) {
        Log.d("ffmpeg-kit-flutter", String.format("FFmpegKitFlutterPlugin %s attached to activity %s.", this, cVar.getActivity()));
        i0(this.f20246j.b(), this.f20246j.a(), cVar.getActivity(), null, cVar);
    }

    public void onAttachedToEngine(a.b bVar) {
        this.f20246j = bVar;
    }

    public void onDetachedFromActivity() {
        Q0();
        Log.d("ffmpeg-kit-flutter", "FFmpegKitFlutterPlugin detached from activity.");
    }

    public void onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity();
    }

    public void onDetachedFromEngine(a.b bVar) {
        this.f20246j = null;
    }

    public void onMethodCall(io.flutter.plugin.common.i iVar, j.d dVar) {
        e eVar;
        String str;
        String str2;
        Integer num = (Integer) iVar.a("sessionId");
        Integer num2 = (Integer) iVar.a("waitTimeout");
        List<String> list = (List) iVar.a("arguments");
        String str3 = (String) iVar.a("ffprobeJsonOutput");
        Boolean bool = (Boolean) iVar.a("writable");
        String str4 = iVar.a;
        str4.hashCode();
        char c9 = 65535;
        switch (str4.hashCode()) {
            case -2120516313:
                if (str4.equals("getSafParameter")) {
                    c9 = 0;
                    break;
                }
                break;
            case -2103441263:
                if (str4.equals("ffmpegSession")) {
                    c9 = 1;
                    break;
                }
                break;
            case -1912785202:
                if (str4.equals("mediaInformationSession")) {
                    c9 = 2;
                    break;
                }
                break;
            case -1866655603:
                if (str4.equals("isLTSBuild")) {
                    c9 = 3;
                    break;
                }
                break;
            case -1743798884:
                if (str4.equals("setFontDirectory")) {
                    c9 = 4;
                    break;
                }
                break;
            case -1722024362:
                if (str4.equals("abstractSessionGetDuration")) {
                    c9 = 5;
                    break;
                }
                break;
            case -1653941728:
                if (str4.equals("asyncFFmpegSessionExecute")) {
                    c9 = 6;
                    break;
                }
                break;
            case -1411074938:
                if (str4.equals("getBuildDate")) {
                    c9 = 7;
                    break;
                }
                break;
            case -1389627233:
                if (str4.equals("ffmpegSessionGetAllStatistics")) {
                    c9 = '\b';
                    break;
                }
                break;
            case -1367724422:
                if (str4.equals("cancel")) {
                    c9 = '\t';
                    break;
                }
                break;
            case -1273119136:
                if (str4.equals("getSession")) {
                    c9 = '\n';
                    break;
                }
                break;
            case -1236521429:
                if (str4.equals("disableStatistics")) {
                    c9 = 11;
                    break;
                }
                break;
            case -1232550904:
                if (str4.equals("ffmpegSessionGetStatistics")) {
                    c9 = '\f';
                    break;
                }
                break;
            case -1219192049:
                if (str4.equals("abstractSessionGetState")) {
                    c9 = '\r';
                    break;
                }
                break;
            case -1197813889:
                if (str4.equals("abstractSessionGetReturnCode")) {
                    c9 = 14;
                    break;
                }
                break;
            case -1149109195:
                if (str4.equals("getSessionHistorySize")) {
                    c9 = 15;
                    break;
                }
                break;
            case -1066083862:
                if (str4.equals("getLastSession")) {
                    c9 = 16;
                    break;
                }
                break;
            case -1007401687:
                if (str4.equals("enableRedirection")) {
                    c9 = 17;
                    break;
                }
                break;
            case -1004092829:
                if (str4.equals("asyncMediaInformationSessionExecute")) {
                    c9 = 18;
                    break;
                }
                break;
            case -986804548:
                if (str4.equals("cancelSession")) {
                    c9 = 19;
                    break;
                }
                break;
            case -873593625:
                if (str4.equals("getSessionsByState")) {
                    c9 = 20;
                    break;
                }
                break;
            case -811987437:
                if (str4.equals("getSessions")) {
                    c9 = 21;
                    break;
                }
                break;
            case -395332803:
                if (str4.equals("getFFmpegVersion")) {
                    c9 = 22;
                    break;
                }
                break;
            case -393893135:
                if (str4.equals("abstractSessionGetAllLogsAsString")) {
                    c9 = 23;
                    break;
                }
                break;
            case -342383127:
                if (str4.equals("getPlatform")) {
                    c9 = 24;
                    break;
                }
                break;
            case -329192698:
                if (str4.equals("enableStatistics")) {
                    c9 = 25;
                    break;
                }
                break;
            case -309915358:
                if (str4.equals("setLogLevel")) {
                    c9 = 26;
                    break;
                }
                break;
            case -275249448:
                if (str4.equals("getFFmpegSessions")) {
                    c9 = 27;
                    break;
                }
                break;
            case -221335530:
                if (str4.equals("getLogLevel")) {
                    c9 = 28;
                    break;
                }
                break;
            case -134939106:
                if (str4.equals("getMediaInformation")) {
                    c9 = 29;
                    break;
                }
                break;
            case -75679540:
                if (str4.equals("getArch")) {
                    c9 = 30;
                    break;
                }
                break;
            case 39238969:
                if (str4.equals("thereAreAsynchronousMessagesInTransmit")) {
                    c9 = 31;
                    break;
                }
                break;
            case 97596186:
                if (str4.equals("ignoreSignal")) {
                    c9 = ' ';
                    break;
                }
                break;
            case 134287517:
                if (str4.equals("abstractSessionGetFailStackTrace")) {
                    c9 = '!';
                    break;
                }
                break;
            case 179624467:
                if (str4.equals("asyncFFprobeSessionExecute")) {
                    c9 = '\"';
                    break;
                }
                break;
            case 265484683:
                if (str4.equals("closeFFmpegPipe")) {
                    c9 = '#';
                    break;
                }
                break;
            case 268490427:
                if (str4.equals("getPackageName")) {
                    c9 = '$';
                    break;
                }
                break;
            case 616732055:
                if (str4.equals("getFFprobeSessions")) {
                    c9 = '%';
                    break;
                }
                break;
            case 666848778:
                if (str4.equals("clearSessions")) {
                    c9 = '&';
                    break;
                }
                break;
            case 754414928:
                if (str4.equals("registerNewFFmpegPipe")) {
                    c9 = '\'';
                    break;
                }
                break;
            case 898447750:
                if (str4.equals("ffprobeSession")) {
                    c9 = '(';
                    break;
                }
                break;
            case 930178724:
                if (str4.equals("disableRedirection")) {
                    c9 = ')';
                    break;
                }
                break;
            case 1038283172:
                if (str4.equals("ffmpegSessionExecute")) {
                    c9 = '*';
                    break;
                }
                break;
            case 1068836721:
                if (str4.equals("abstractSessionGetLogs")) {
                    c9 = '+';
                    break;
                }
                break;
            case 1120963409:
                if (str4.equals("getLogRedirectionStrategy")) {
                    c9 = ',';
                    break;
                }
                break;
            case 1172412742:
                if (str4.equals("abstractSessionGetEndTime")) {
                    c9 = '-';
                    break;
                }
                break;
            case 1215775213:
                if (str4.equals("setEnvironmentVariable")) {
                    c9 = '.';
                    break;
                }
                break;
            case 1294348535:
                if (str4.equals("getLastCompletedSession")) {
                    c9 = '/';
                    break;
                }
                break;
            case 1353099447:
                if (str4.equals("disableLogs")) {
                    c9 = '0';
                    break;
                }
                break;
            case 1387101761:
                if (str4.equals("setSessionHistorySize")) {
                    c9 = '1';
                    break;
                }
                break;
            case 1435234184:
                if (str4.equals("writeToPipe")) {
                    c9 = '2';
                    break;
                }
                break;
            case 1453176007:
                if (str4.equals("mediaInformationSessionExecute")) {
                    c9 = '3';
                    break;
                }
                break;
            case 1466586152:
                if (str4.equals("setFontconfigConfigurationPath")) {
                    c9 = '4';
                    break;
                }
                break;
            case 1555761752:
                if (str4.equals("getExternalLibraries")) {
                    c9 = '5';
                    break;
                }
                break;
            case 1566113121:
                if (str4.equals("messagesInTransmit")) {
                    c9 = '6';
                    break;
                }
                break;
            case 1639331035:
                if (str4.equals("getMediaInformationSessions")) {
                    c9 = '7';
                    break;
                }
                break;
            case 1714653353:
                if (str4.equals("mediaInformationJsonParserFromWithError")) {
                    c9 = '8';
                    break;
                }
                break;
            case 1755559002:
                if (str4.equals("setFontDirectoryList")) {
                    c9 = '9';
                    break;
                }
                break;
            case 1814015543:
                if (str4.equals("selectDocument")) {
                    c9 = ':';
                    break;
                }
                break;
            case 1867262446:
                if (str4.equals("abstractSessionGetAllLogs")) {
                    c9 = ';';
                    break;
                }
                break;
            case 1893000658:
                if (str4.equals("enableLogs")) {
                    c9 = '<';
                    break;
                }
                break;
            case 1945437241:
                if (str4.equals("mediaInformationJsonParserFrom")) {
                    c9 = '=';
                    break;
                }
                break;
            case 1964255069:
                if (str4.equals("setLogRedirectionStrategy")) {
                    c9 = '>';
                    break;
                }
                break;
            case 2034217743:
                if (str4.equals("ffprobeSessionExecute")) {
                    c9 = '?';
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                String str5 = (String) iVar.a("uri");
                String str6 = (String) iVar.a("openMode");
                if (str5 != null && str6 != null) {
                    c0(str5, str6, dVar);
                    return;
                }
                eVar = this.f20249m;
                if (str5 != null) {
                    str = "INVALID_OPEN_MODE";
                    str2 = "Invalid openMode value.";
                } else {
                    str = "INVALID_URI";
                    str2 = "Invalid uri value.";
                }
                eVar.e(dVar, str, str2);
                return;
            case 1:
                if (list != null) {
                    I(list, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_ARGUMENTS", "Invalid arguments array.");
                return;
            case 2:
                if (list != null) {
                    p0(list, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_ARGUMENTS", "Invalid arguments array.");
                return;
            case 3:
                j0(dVar);
                return;
            case 4:
                String str7 = (String) iVar.a("fontDirectory");
                Map<String, String> map = (Map) iVar.a("fontNameMap");
                if (str7 != null) {
                    w0(str7, map, dVar);
                    return;
                }
                eVar = this.f20249m;
                str = "INVALID_FONT_DIRECTORY";
                str2 = "Invalid font directory.";
                eVar.e(dVar, str, str2);
                return;
            case 5:
                if (num != null) {
                    g(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case 6:
                if (num != null) {
                    o(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case 7:
                P(dVar);
                return;
            case '\b':
                if (num != null) {
                    K(num, num2, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case '\t':
                r(dVar);
                return;
            case '\n':
                if (num != null) {
                    d0(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case 11:
                z(dVar);
                return;
            case '\f':
                if (num != null) {
                    L(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case '\r':
                if (num != null) {
                    m(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case 14:
                if (num != null) {
                    l(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case 15:
                e0(dVar);
                return;
            case 16:
                V(dVar);
                return;
            case 17:
                F(dVar);
                return;
            case 18:
                if (num != null) {
                    q(num, num2, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case 19:
                if (num != null) {
                    s(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case 20:
                Integer num3 = (Integer) iVar.a("state");
                if (num3 != null) {
                    g0(num3, dVar);
                    return;
                }
                eVar = this.f20249m;
                str = "INVALID_SESSION_STATE";
                str2 = "Invalid session state value.";
                eVar.e(dVar, str, str2);
                return;
            case 21:
                f0(dVar);
                return;
            case 22:
                S(dVar);
                return;
            case 23:
                if (num != null) {
                    f(num, num2, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case 24:
                b0(dVar);
                return;
            case 25:
                H(dVar);
                return;
            case 26:
                Integer num4 = (Integer) iVar.a("level");
                if (num4 != null) {
                    z0(num4, dVar);
                    return;
                }
                eVar = this.f20249m;
                str = "INVALID_LEVEL";
                str2 = "Invalid level value.";
                eVar.e(dVar, str, str2);
                return;
            case 27:
                R(dVar);
                return;
            case 28:
                W(dVar);
                return;
            case 29:
                if (num != null) {
                    Y(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case 30:
                O(dVar);
                return;
            case 31:
                if (num != null) {
                    n(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case ' ':
                Integer num5 = (Integer) iVar.a("signal");
                if (num5 != null) {
                    h0(num5, dVar);
                    return;
                }
                eVar = this.f20249m;
                str = "INVALID_SIGNAL";
                str2 = "Invalid signal value.";
                eVar.e(dVar, str, str2);
                return;
            case '!':
                if (num != null) {
                    j(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case '\"':
                if (num != null) {
                    p(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case '#':
                String str8 = (String) iVar.a("ffmpegPipePath");
                if (str8 != null) {
                    u(str8, dVar);
                    return;
                }
                eVar = this.f20249m;
                str = "INVALID_PIPE_PATH";
                str2 = "Invalid ffmpeg pipe path.";
                eVar.e(dVar, str, str2);
                return;
            case '$':
                a0(dVar);
                return;
            case '%':
                T(dVar);
                return;
            case '&':
                t(dVar);
                return;
            case '\'':
                t0(dVar);
                return;
            case '(':
                if (list != null) {
                    M(list, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_ARGUMENTS", "Invalid arguments array.");
                return;
            case ')':
                x(dVar);
                return;
            case '*':
                if (num != null) {
                    J(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case '+':
                if (num != null) {
                    k(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case ',':
                X(dVar);
                return;
            case '-':
                if (num != null) {
                    i(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case '.':
                String str9 = (String) iVar.a("variableName");
                String str10 = (String) iVar.a("variableValue");
                if (str9 != null && str10 != null) {
                    v0(str9, str10, dVar);
                    return;
                }
                if (str10 != null) {
                    eVar = this.f20249m;
                    str = "INVALID_NAME";
                    str2 = "Invalid environment variable name.";
                } else {
                    eVar = this.f20249m;
                    str = "INVALID_VALUE";
                    str2 = "Invalid environment variable value.";
                }
                eVar.e(dVar, str, str2);
                return;
            case '/':
                U(dVar);
                return;
            case '0':
                w(dVar);
                return;
            case '1':
                Integer num6 = (Integer) iVar.a("sessionHistorySize");
                if (num6 != null) {
                    B0(num6, dVar);
                    return;
                }
                eVar = this.f20249m;
                str = "INVALID_SIZE";
                str2 = "Invalid session history size value.";
                eVar.e(dVar, str, str2);
                return;
            case '2':
                String str11 = (String) iVar.a("input");
                String str12 = (String) iVar.a("pipe");
                if (str11 != null && str12 != null) {
                    T0(str11, str12, dVar);
                    return;
                }
                if (str12 != null) {
                    eVar = this.f20249m;
                    str = "INVALID_INPUT";
                    str2 = "Invalid input value.";
                } else {
                    eVar = this.f20249m;
                    str = "INVALID_PIPE";
                    str2 = "Invalid pipe value.";
                }
                eVar.e(dVar, str, str2);
                return;
            case '3':
                if (num != null) {
                    q0(num, num2, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case '4':
                String str13 = (String) iVar.a("path");
                if (str13 != null) {
                    y0(str13, dVar);
                    return;
                }
                eVar = this.f20249m;
                str = "INVALID_PATH";
                str2 = "Invalid path.";
                eVar.e(dVar, str, str2);
                return;
            case '5':
                Q(dVar);
                return;
            case '6':
                if (num != null) {
                    r0(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case '7':
                Z(dVar);
                return;
            case '8':
                if (str3 != null) {
                    o0(str3, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_FFPROBE_JSON_OUTPUT", "Invalid ffprobe json output.");
                return;
            case '9':
                List<String> list2 = (List) iVar.a("fontDirectoryList");
                Map<String, String> map2 = (Map) iVar.a("fontNameMap");
                if (list2 != null) {
                    x0(list2, map2, dVar);
                    return;
                }
                eVar = this.f20249m;
                str = "INVALID_FONT_DIRECTORY_LIST";
                str2 = "Invalid font directory list.";
                eVar.e(dVar, str, str2);
                return;
            case ':':
                String str14 = (String) iVar.a("title");
                String str15 = (String) iVar.a("type");
                List list3 = (List) iVar.a("extraTypes");
                String[] strArr = list3 != null ? (String[]) list3.toArray(new String[0]) : null;
                if (bool != null) {
                    u0(bool, str14, str15, strArr, dVar);
                    return;
                }
                eVar = this.f20249m;
                str = "INVALID_WRITABLE";
                str2 = "Invalid writable value.";
                eVar.e(dVar, str, str2);
                return;
            case ';':
                if (num != null) {
                    e(num, num2, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            case '<':
                E(dVar);
                return;
            case '=':
                if (str3 != null) {
                    n0(str3, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_FFPROBE_JSON_OUTPUT", "Invalid ffprobe json output.");
                return;
            case '>':
                Integer num7 = (Integer) iVar.a("strategy");
                if (num7 != null) {
                    A0(num7, dVar);
                    return;
                }
                eVar = this.f20249m;
                str = "INVALID_LOG_REDIRECTION_STRATEGY";
                str2 = "Invalid log redirection strategy value.";
                eVar.e(dVar, str, str2);
                return;
            case '?':
                if (num != null) {
                    N(num, dVar);
                    return;
                }
                this.f20249m.e(dVar, "INVALID_SESSION", "Invalid session id.");
                return;
            default:
                this.f20249m.k(dVar);
                return;
        }
    }

    public void onReattachedToActivityForConfigChanges(c cVar) {
        onAttachedToActivity(cVar);
    }

    protected void p(Integer num, j.d dVar) {
        e eVar;
        String str;
        String str2;
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            eVar = this.f20249m;
            str = "SESSION_NOT_FOUND";
            str2 = "Session not found.";
        } else if (H.p()) {
            FFmpegKitConfig.e((com.arthenica.ffmpegkit.j) H);
            this.f20249m.m(dVar, null);
            return;
        } else {
            eVar = this.f20249m;
            str = "NOT_FFPROBE_SESSION";
            str2 = "A session is found but it does not have the correct type.";
        }
        eVar.e(dVar, str, str2);
    }

    protected void p0(List<String> list, j.d dVar) {
        this.f20249m.m(dVar, K0(p.z((String[]) list.toArray(new String[0]), null, null)));
    }

    protected void q(Integer num, Integer num2, j.d dVar) {
        e eVar;
        String str;
        String str2;
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            eVar = this.f20249m;
            str = "SESSION_NOT_FOUND";
            str2 = "Session not found.";
        } else if (H.r()) {
            FFmpegKitConfig.f((p) H, k0(num2) ? num2.intValue() : 5000);
            this.f20249m.m(dVar, null);
            return;
        } else {
            eVar = this.f20249m;
            str = "NOT_MEDIA_INFORMATION_SESSION";
            str2 = "A session is found but it does not have the correct type.";
        }
        eVar.e(dVar, str, str2);
    }

    protected void q0(Integer num, Integer num2, j.d dVar) {
        e eVar;
        String str;
        String str2;
        u H = FFmpegKitConfig.H(num.longValue());
        if (H == null) {
            eVar = this.f20249m;
            str = "SESSION_NOT_FOUND";
            str2 = "Session not found.";
        } else if (H.r()) {
            this.f20240c.submit(new n((p) H, k0(num2) ? num2.intValue() : 5000, this.f20249m, dVar));
            return;
        } else {
            eVar = this.f20249m;
            str = "NOT_MEDIA_INFORMATION_SESSION";
            str2 = "A session is found but it does not have the correct type.";
        }
        eVar.e(dVar, str, str2);
    }

    protected void r(j.d dVar) {
        f.a();
        this.f20249m.m(dVar, null);
    }

    protected void r0(Integer num, j.d dVar) {
        this.f20249m.m(dVar, Integer.valueOf(FFmpegKitConfig.messagesInTransmit(num.longValue())));
    }

    protected void s(Integer num, j.d dVar) {
        f.b(num.longValue());
        this.f20249m.m(dVar, null);
    }

    protected void s0() {
        FFmpegKitConfig.k(new f(this));
        FFmpegKitConfig.l(new g(this));
        FFmpegKitConfig.n(new i(this));
        FFmpegKitConfig.m(new h(this));
        FFmpegKitConfig.p(new j(this));
    }

    protected void t(j.d dVar) {
        FFmpegKitConfig.g();
        this.f20249m.m(dVar, null);
    }

    protected void t0(j.d dVar) {
        Context context = this.f20244g;
        if (context != null) {
            this.f20249m.m(dVar, FFmpegKitConfig.O(context));
            return;
        }
        Log.w("ffmpeg-kit-flutter", "Cannot registerNewFFmpegPipe. Context is null.");
        this.f20249m.e(dVar, "INVALID_CONTEXT", "Context is null.");
    }

    protected void u(String str, j.d dVar) {
        FFmpegKitConfig.h(str);
        this.f20249m.m(dVar, null);
    }

    protected void u0(Boolean bool, String str, String str2, String[] strArr, j.d dVar) {
        Intent intent;
        e eVar;
        String str3;
        String str4;
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 19) {
            Log.i("ffmpeg-kit-flutter", String.format(Locale.getDefault(), "selectDocument is not supported on API Level %d", Integer.valueOf(i8)));
            this.f20249m.e(dVar, "SELECT_FAILED", String.format(Locale.getDefault(), "selectDocument is not supported on API Level %d", Integer.valueOf(i8)));
            return;
        }
        if (bool.booleanValue()) {
            intent = new Intent("android.intent.action.CREATE_DOCUMENT");
            intent.addFlags(3);
        } else {
            intent = new Intent("android.intent.action.GET_CONTENT");
            intent.addCategory("android.intent.category.OPENABLE");
            intent.addFlags(1);
        }
        if (str2 != null) {
            intent.setType(str2);
        } else {
            intent.setType("*/*");
        }
        if (str != null) {
            intent.putExtra("android.intent.extra.TITLE", str);
        }
        if (strArr != null) {
            intent.putExtra("android.intent.extra.MIME_TYPES", strArr);
        }
        if (this.f20244g != null) {
            Activity activity = this.f20245h;
            if (activity != null) {
                try {
                    this.f20243f = dVar;
                    activity.startActivityForResult(intent, bool.booleanValue() ? 20000 : ModuleDescriptor.MODULE_VERSION);
                    return;
                } catch (Exception e8) {
                    Object[] objArr = new Object[4];
                    objArr[0] = bool;
                    objArr[1] = str2;
                    objArr[2] = str;
                    objArr[3] = strArr != null ? Arrays.toString(strArr) : null;
                    Log.i("ffmpeg-kit-flutter", String.format("Failed to selectDocument using parameters writable: %s, type: %s, title: %s and extra types: %s!", objArr), e8);
                    this.f20249m.e(dVar, "SELECT_FAILED", e8.getMessage());
                    return;
                }
            }
            Object[] objArr2 = new Object[4];
            objArr2[0] = bool;
            objArr2[1] = str2;
            objArr2[2] = str;
            objArr2[3] = strArr != null ? Arrays.toString(strArr) : null;
            Log.w("ffmpeg-kit-flutter", String.format("Cannot selectDocument using parameters writable: %s, type: %s, title: %s and extra types: %s. Activity is null.", objArr2));
            eVar = this.f20249m;
            str3 = "INVALID_ACTIVITY";
            str4 = "Activity is null.";
        } else {
            Object[] objArr3 = new Object[4];
            objArr3[0] = bool;
            objArr3[1] = str2;
            objArr3[2] = str;
            objArr3[3] = strArr != null ? Arrays.toString(strArr) : null;
            Log.w("ffmpeg-kit-flutter", String.format("Cannot selectDocument using parameters writable: %s, type: %s, title: %s and extra types: %s. Context is null.", objArr3));
            eVar = this.f20249m;
            str3 = "INVALID_CONTEXT";
            str4 = "Context is null.";
        }
        eVar.e(dVar, str3, str4);
    }

    protected void v() {
        this.f20238a.compareAndSet(true, false);
    }

    protected void v0(String str, String str2, j.d dVar) {
        FFmpegKitConfig.P(str, str2);
        this.f20249m.m(dVar, null);
    }

    protected void w(j.d dVar) {
        v();
        this.f20249m.m(dVar, null);
    }

    protected void w0(String str, Map<String, String> map, j.d dVar) {
        Context context = this.f20244g;
        if (context != null) {
            FFmpegKitConfig.Q(context, str, map);
            this.f20249m.m(dVar, null);
            return;
        }
        Log.w("ffmpeg-kit-flutter", "Cannot setFontDirectory. Context is null.");
        this.f20249m.e(dVar, "INVALID_CONTEXT", "Context is null.");
    }

    protected void x(j.d dVar) {
        FFmpegKitConfig.j();
        this.f20249m.m(dVar, null);
    }

    protected void x0(List<String> list, Map<String, String> map, j.d dVar) {
        Context context = this.f20244g;
        if (context != null) {
            FFmpegKitConfig.R(context, list, map);
            this.f20249m.m(dVar, null);
            return;
        }
        Log.w("ffmpeg-kit-flutter", "Cannot setFontDirectoryList. Context is null.");
        this.f20249m.e(dVar, "INVALID_CONTEXT", "Context is null.");
    }

    protected void y() {
        this.f20239b.compareAndSet(true, false);
    }

    protected void y0(String str, j.d dVar) {
        FFmpegKitConfig.S(str);
        this.f20249m.m(dVar, null);
    }

    protected void z(j.d dVar) {
        y();
        this.f20249m.m(dVar, null);
    }

    protected void z0(Integer num, j.d dVar) {
        FFmpegKitConfig.T(Level.f(num.intValue()));
        this.f20249m.m(dVar, null);
    }
}
