package m5;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.text.TextUtils;
import b6.h0;
import b6.t;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.hls.HlsTrackMetadataEntry;
import com.google.android.exoplayer2.w0;
import j4.t1;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d implements h {

    /* renamed from: d  reason: collision with root package name */
    private static final int[] f21847d = {8, 13, 11, 2, 0, 1, 7};

    /* renamed from: b  reason: collision with root package name */
    private final int f21848b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f21849c;

    public d() {
        this(0, true);
    }

    public d(int i8, boolean z4) {
        this.f21848b = i8;
        this.f21849c = z4;
    }

    private static void b(int i8, List<Integer> list) {
        if (com.google.common.primitives.g.h(f21847d, i8) == -1 || list.contains(Integer.valueOf(i8))) {
            return;
        }
        list.add(Integer.valueOf(i8));
    }

    @SuppressLint({"SwitchIntDef"})
    private n4.k d(int i8, w0 w0Var, List<w0> list, h0 h0Var) {
        if (i8 != 0) {
            if (i8 != 1) {
                if (i8 != 2) {
                    if (i8 != 7) {
                        if (i8 != 8) {
                            if (i8 != 11) {
                                if (i8 != 13) {
                                    return null;
                                }
                                return new r(w0Var.f11198c, h0Var);
                            }
                            return f(this.f21848b, this.f21849c, w0Var, list, h0Var);
                        }
                        return e(h0Var, w0Var, list);
                    }
                    return new u4.f(0, 0L);
                }
                return new x4.h();
            }
            return new x4.e();
        }
        return new x4.b();
    }

    private static v4.g e(h0 h0Var, w0 w0Var, List<w0> list) {
        int i8 = g(w0Var) ? 4 : 0;
        if (list == null) {
            list = Collections.emptyList();
        }
        return new v4.g(i8, h0Var, null, list);
    }

    private static x4.h0 f(int i8, boolean z4, w0 w0Var, List<w0> list, h0 h0Var) {
        int i9 = i8 | 16;
        if (list != null) {
            i9 |= 32;
        } else {
            list = z4 ? Collections.singletonList(new w0.b().g0("application/cea-608").G()) : Collections.emptyList();
        }
        String str = w0Var.f11204j;
        if (!TextUtils.isEmpty(str)) {
            if (!t.b(str, "audio/mp4a-latm")) {
                i9 |= 2;
            }
            if (!t.b(str, "video/avc")) {
                i9 |= 4;
            }
        }
        return new x4.h0(2, h0Var, new x4.j(i9, list));
    }

    private static boolean g(w0 w0Var) {
        Metadata.Entry d8;
        Metadata metadata = w0Var.f11205k;
        if (metadata == null) {
            return false;
        }
        for (int i8 = 0; i8 < metadata.e(); i8++) {
            if (metadata.d(i8) instanceof HlsTrackMetadataEntry) {
                return !((HlsTrackMetadataEntry) d8).f10473c.isEmpty();
            }
        }
        return false;
    }

    private static boolean h(n4.k kVar, n4.l lVar) {
        try {
            boolean g8 = kVar.g(lVar);
            lVar.h();
            return g8;
        } catch (EOFException unused) {
            lVar.h();
            return false;
        } catch (Throwable th) {
            lVar.h();
            throw th;
        }
    }

    @Override // m5.h
    /* renamed from: c */
    public b a(Uri uri, w0 w0Var, List<w0> list, h0 h0Var, Map<String, List<String>> map, n4.l lVar, t1 t1Var) {
        int a9 = b6.j.a(w0Var.f11207m);
        int b9 = b6.j.b(map);
        int c9 = b6.j.c(uri);
        int[] iArr = f21847d;
        ArrayList arrayList = new ArrayList(iArr.length);
        b(a9, arrayList);
        b(b9, arrayList);
        b(c9, arrayList);
        for (int i8 : iArr) {
            b(i8, arrayList);
        }
        n4.k kVar = null;
        lVar.h();
        for (int i9 = 0; i9 < arrayList.size(); i9++) {
            int intValue = ((Integer) arrayList.get(i9)).intValue();
            n4.k kVar2 = (n4.k) b6.a.e(d(intValue, w0Var, list, h0Var));
            if (h(kVar2, lVar)) {
                return new b(kVar2, w0Var, h0Var);
            }
            if (kVar == null && (intValue == a9 || intValue == b9 || intValue == c9 || intValue == 11)) {
                kVar = kVar2;
            }
        }
        return new b((n4.k) b6.a.e(kVar), w0Var, h0Var);
    }
}
