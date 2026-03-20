package v2;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kj.k;
import kotlin.collections.q;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
import kotlin.jvm.internal.t;
import kotlinx.serialization.SerializationException;
import mk.a;
import tj.o;
import v2.c;
import yf.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: b  reason: collision with root package name */
    private static boolean f23160b;

    /* renamed from: c  reason: collision with root package name */
    private static List<c> f23161c;

    /* renamed from: a  reason: collision with root package name */
    public static final C0215a f23159a = new C0215a(null);

    /* renamed from: d  reason: collision with root package name */
    private static final Map<e, Typeface> f23162d = new LinkedHashMap();

    /* renamed from: v2.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0215a {
        private C0215a() {
        }

        public /* synthetic */ C0215a(i iVar) {
            this();
        }

        public static /* synthetic */ Typeface c(C0215a c0215a, String str, int i8, boolean z4, boolean z8, int i9, Object obj) {
            if ((i9 & 2) != 0) {
                i8 = 400;
            }
            if ((i9 & 4) != 0) {
                z4 = false;
            }
            if ((i9 & 8) != 0) {
                z8 = false;
            }
            return c0215a.b(str, i8, z4, z8);
        }

        public final void a(a.b bVar) {
            int intValue;
            p.e(bVar, "binding");
            if (a.f23160b) {
                Log.d("FlutterFontRegistry", "Already initialized!");
                return;
            }
            AssetManager assets = bVar.a().getAssets();
            p.d(assets, "getAssets(...)");
            InputStream open = assets.open(bVar.c().a("FontManifest.json"));
            p.d(open, "open(...)");
            Charset forName = Charset.forName("utf-8");
            p.d(forName, "forName(...)");
            String c9 = k.c(new InputStreamReader(open, forName));
            try {
                a.a aVar = mk.a.d;
                hk.b b9 = hk.i.b(aVar.a(), t.m(List.class, o.c.a(t.l(c.class))));
                p.c(b9, "null cannot be cast to non-null type kotlinx.serialization.KSerializer<T of kotlinx.serialization.internal.Platform_commonKt.cast>");
                a.f23161c = (List) aVar.b(b9, c9);
            } catch (SerializationException e8) {
                Log.e("FlutterFontRegistry", e8.toString());
            }
            List<c> list = a.f23161c;
            if (list == null) {
                p.t("manifest");
                list = null;
            }
            for (c cVar : list) {
                Log.d("FlutterFontRegistry", cVar.toString());
                String str = (String) q.C(vj.e.X(cVar.a(), new String[]{"/"}, false, 0, 6, (Object) null));
                for (c.a aVar2 : cVar.b()) {
                    Log.d("FlutterFontRegistry", aVar2.toString());
                    String a9 = bVar.c().a(aVar2.a());
                    Log.e("FlutterFontRegistry", a9);
                    Typeface createFromAsset = Typeface.createFromAsset(assets, a9);
                    if (createFromAsset != null) {
                        p.b(createFromAsset);
                        if (Build.VERSION.SDK_INT >= 28) {
                            intValue = createFromAsset.getWeight();
                        } else {
                            Integer b10 = aVar2.b();
                            intValue = b10 != null ? b10.intValue() : 400;
                        }
                        a.f23162d.put(new e(str, intValue, createFromAsset.isBold(), createFromAsset.isItalic()), createFromAsset);
                    }
                }
            }
            a.f23160b = true;
        }

        public final Typeface b(String str, int i8, boolean z4, boolean z8) {
            Typeface d8 = d(str, i8, z4, z8);
            if (d8 == null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    d8 = Typeface.create(Typeface.DEFAULT, i8, z4);
                } else {
                    Typeface typeface = i8 >= 700 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT;
                    p.b(typeface);
                    d8 = b.a(b.b(typeface, i8), z4);
                }
                p.b(d8);
            }
            return d8;
        }

        public final Typeface d(String str, int i8, boolean z4, boolean z8) {
            StringBuilder sb;
            Typeface typeface;
            Typeface b9;
            if (a.f23160b) {
                int i9 = 1;
                boolean z9 = i8 >= 700;
                Typeface typeface2 = null;
                if (str != null) {
                    Map map = a.f23162d;
                    LinkedHashMap linkedHashMap = new LinkedHashMap();
                    for (Map.Entry entry : map.entrySet()) {
                        if (p.a(((e) entry.getKey()).a(), str)) {
                            linkedHashMap.put(entry.getKey(), entry.getValue());
                        }
                    }
                    if (linkedHashMap.isEmpty()) {
                        typeface = null;
                    } else {
                        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
                        for (Map.Entry entry2 : linkedHashMap.entrySet()) {
                            e eVar = (e) entry2.getKey();
                            if (eVar.b() == i8 && eVar.d() == z4) {
                                linkedHashMap2.put(entry2.getKey(), entry2.getValue());
                            }
                        }
                        if (linkedHashMap2.isEmpty()) {
                            linkedHashMap2 = new LinkedHashMap();
                            for (Map.Entry entry3 : linkedHashMap.entrySet()) {
                                e eVar2 = (e) entry3.getKey();
                                if (eVar2.c() == z9 && eVar2.d() == z4) {
                                    linkedHashMap2.put(entry3.getKey(), entry3.getValue());
                                }
                            }
                        }
                        if (linkedHashMap2.isEmpty()) {
                            linkedHashMap2 = new LinkedHashMap();
                            for (Map.Entry entry4 : linkedHashMap.entrySet()) {
                                if (((e) entry4.getKey()).d() == z4) {
                                    linkedHashMap2.put(entry4.getKey(), entry4.getValue());
                                }
                            }
                        }
                        typeface = (Typeface) q.v(linkedHashMap2.values());
                    }
                    if (typeface != null && (b9 = b.b(typeface, i8)) != null) {
                        typeface2 = b.a(b9, z4);
                    }
                }
                if (z8) {
                    if (typeface2 != null) {
                        sb = new StringBuilder();
                        sb.append("resolved font ");
                        sb.append(str);
                        sb.append(", weight = ");
                        sb.append(i8);
                        sb.append(", isItalic = ");
                        sb.append(z4);
                        sb.append('!');
                    } else {
                        sb = new StringBuilder();
                        sb.append("failed to resolve font ");
                        sb.append(str);
                        sb.append(", weight = ");
                        sb.append(i8);
                        sb.append(", isItalic = ");
                        sb.append(z4);
                        sb.append("; registered typefaces = ");
                        sb.append(a.f23162d);
                    }
                    Log.d("FlutterFontRegistry", sb.toString());
                }
                if (typeface2 == null) {
                    List h8 = q.h(new Boolean[]{Boolean.valueOf(z9), Boolean.valueOf(z4)});
                    Boolean bool = Boolean.FALSE;
                    if (p.a(h8, q.h(new Boolean[]{bool, bool}))) {
                        i9 = 0;
                    } else {
                        Boolean bool2 = Boolean.TRUE;
                        if (!p.a(h8, q.h(new Boolean[]{bool2, bool}))) {
                            i9 = p.a(h8, q.h(new Boolean[]{bool, bool2})) ? 2 : 3;
                        }
                    }
                    return Typeface.defaultFromStyle(i9);
                }
                return typeface2;
            }
            throw new IllegalStateException("FlutterFontRegistry has not been initialized!".toString());
        }
    }
}
