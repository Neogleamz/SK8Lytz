package i3;

import android.content.Context;
import io.flutter.plugin.common.j;
import java.io.File;
import java.util.concurrent.Future;
import kotlin.jvm.internal.p;
import nb.i;
import org.json.JSONObject;
import yf.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c implements j.c, yf.a {

    /* renamed from: g  reason: collision with root package name */
    public static final a f20450g = new a(null);

    /* renamed from: a  reason: collision with root package name */
    private Context f20451a;

    /* renamed from: b  reason: collision with root package name */
    private j f20452b;

    /* renamed from: e  reason: collision with root package name */
    private Future<Void> f20455e;

    /* renamed from: c  reason: collision with root package name */
    private final String f20453c = "VideoCompressPlugin";

    /* renamed from: d  reason: collision with root package name */
    private final i f20454d = new i("VideoCompressPlugin");

    /* renamed from: f  reason: collision with root package name */
    private String f20456f = "video_compress";

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(kotlin.jvm.internal.i iVar) {
            this();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements db.b {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ j f20457a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ c f20458b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ Context f20459c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ String f20460d;

        /* renamed from: e  reason: collision with root package name */
        final /* synthetic */ j.d f20461e;

        /* renamed from: f  reason: collision with root package name */
        final /* synthetic */ boolean f20462f;

        /* renamed from: g  reason: collision with root package name */
        final /* synthetic */ String f20463g;

        b(j jVar, c cVar, Context context, String str, j.d dVar, boolean z4, String str2) {
            this.f20457a = jVar;
            this.f20458b = cVar;
            this.f20459c = context;
            this.f20460d = str;
            this.f20461e = dVar;
            this.f20462f = z4;
            this.f20463g = str2;
        }

        public void a(int i8) {
            this.f20457a.c("updateProgress", Double.valueOf(100.0d));
            JSONObject d8 = new i3.b(this.f20458b.a()).d(this.f20459c, this.f20460d);
            d8.put("isCancel", false);
            this.f20461e.success(d8.toString());
            if (this.f20462f) {
                new File(this.f20463g).delete();
            }
        }

        public void b(double d8) {
            this.f20457a.c("updateProgress", Double.valueOf(d8 * 100.0d));
        }

        public void c(Throwable th) {
            p.e(th, "exception");
            this.f20461e.success((Object) null);
        }

        public void d() {
            this.f20461e.success((Object) null);
        }
    }

    private final void b(Context context, io.flutter.plugin.common.c cVar) {
        j jVar = new j(cVar, this.f20456f);
        jVar.e(this);
        this.f20451a = context;
        this.f20452b = jVar;
    }

    public final String a() {
        return this.f20456f;
    }

    public void onAttachedToEngine(a.b bVar) {
        p.e(bVar, "binding");
        Context a9 = bVar.a();
        p.d(a9, "getApplicationContext(...)");
        io.flutter.plugin.common.c b9 = bVar.b();
        p.d(b9, "getBinaryMessenger(...)");
        b(a9, b9);
    }

    public void onDetachedFromEngine(a.b bVar) {
        p.e(bVar, "binding");
        j jVar = this.f20452b;
        if (jVar != null) {
            jVar.e((j.c) null);
        }
        this.f20451a = null;
        this.f20452b = null;
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0167  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x017c  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0183 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x01a0  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x01a5  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x01af  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01b5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onMethodCall(io.flutter.plugin.common.i r20, io.flutter.plugin.common.j.d r21) {
        /*
            Method dump skipped, instructions count: 760
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: i3.c.onMethodCall(io.flutter.plugin.common.i, io.flutter.plugin.common.j$d):void");
    }
}
