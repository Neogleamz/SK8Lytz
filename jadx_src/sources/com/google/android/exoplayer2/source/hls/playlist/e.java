package com.google.android.exoplayer2.source.hls.playlist;

import android.net.Uri;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.w0;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e extends n5.d {

    /* renamed from: n  reason: collision with root package name */
    public static final e f10585n = new e(BuildConfig.FLAVOR, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), null, Collections.emptyList(), false, Collections.emptyMap(), Collections.emptyList());

    /* renamed from: d  reason: collision with root package name */
    public final List<Uri> f10586d;

    /* renamed from: e  reason: collision with root package name */
    public final List<b> f10587e;

    /* renamed from: f  reason: collision with root package name */
    public final List<a> f10588f;

    /* renamed from: g  reason: collision with root package name */
    public final List<a> f10589g;

    /* renamed from: h  reason: collision with root package name */
    public final List<a> f10590h;

    /* renamed from: i  reason: collision with root package name */
    public final List<a> f10591i;

    /* renamed from: j  reason: collision with root package name */
    public final w0 f10592j;

    /* renamed from: k  reason: collision with root package name */
    public final List<w0> f10593k;

    /* renamed from: l  reason: collision with root package name */
    public final Map<String, String> f10594l;

    /* renamed from: m  reason: collision with root package name */
    public final List<DrmInitData> f10595m;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final Uri f10596a;

        /* renamed from: b  reason: collision with root package name */
        public final w0 f10597b;

        /* renamed from: c  reason: collision with root package name */
        public final String f10598c;

        /* renamed from: d  reason: collision with root package name */
        public final String f10599d;

        public a(Uri uri, w0 w0Var, String str, String str2) {
            this.f10596a = uri;
            this.f10597b = w0Var;
            this.f10598c = str;
            this.f10599d = str2;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final Uri f10600a;

        /* renamed from: b  reason: collision with root package name */
        public final w0 f10601b;

        /* renamed from: c  reason: collision with root package name */
        public final String f10602c;

        /* renamed from: d  reason: collision with root package name */
        public final String f10603d;

        /* renamed from: e  reason: collision with root package name */
        public final String f10604e;

        /* renamed from: f  reason: collision with root package name */
        public final String f10605f;

        public b(Uri uri, w0 w0Var, String str, String str2, String str3, String str4) {
            this.f10600a = uri;
            this.f10601b = w0Var;
            this.f10602c = str;
            this.f10603d = str2;
            this.f10604e = str3;
            this.f10605f = str4;
        }

        public static b b(Uri uri) {
            return new b(uri, new w0.b().U("0").M("application/x-mpegURL").G(), null, null, null, null);
        }

        public b a(w0 w0Var) {
            return new b(this.f10600a, w0Var, this.f10602c, this.f10603d, this.f10604e, this.f10605f);
        }
    }

    public e(String str, List<String> list, List<b> list2, List<a> list3, List<a> list4, List<a> list5, List<a> list6, w0 w0Var, List<w0> list7, boolean z4, Map<String, String> map, List<DrmInitData> list8) {
        super(str, list, z4);
        this.f10586d = Collections.unmodifiableList(f(list2, list3, list4, list5, list6));
        this.f10587e = Collections.unmodifiableList(list2);
        this.f10588f = Collections.unmodifiableList(list3);
        this.f10589g = Collections.unmodifiableList(list4);
        this.f10590h = Collections.unmodifiableList(list5);
        this.f10591i = Collections.unmodifiableList(list6);
        this.f10592j = w0Var;
        this.f10593k = list7 != null ? Collections.unmodifiableList(list7) : null;
        this.f10594l = Collections.unmodifiableMap(map);
        this.f10595m = Collections.unmodifiableList(list8);
    }

    private static void b(List<a> list, List<Uri> list2) {
        for (int i8 = 0; i8 < list.size(); i8++) {
            Uri uri = list.get(i8).f10596a;
            if (uri != null && !list2.contains(uri)) {
                list2.add(uri);
            }
        }
    }

    private static <T> List<T> d(List<T> list, int i8, List<StreamKey> list2) {
        ArrayList arrayList = new ArrayList(list2.size());
        for (int i9 = 0; i9 < list.size(); i9++) {
            T t8 = list.get(i9);
            int i10 = 0;
            while (true) {
                if (i10 < list2.size()) {
                    StreamKey streamKey = list2.get(i10);
                    if (streamKey.f10210b == i8 && streamKey.f10211c == i9) {
                        arrayList.add(t8);
                        break;
                    }
                    i10++;
                }
            }
        }
        return arrayList;
    }

    public static e e(String str) {
        return new e(BuildConfig.FLAVOR, Collections.emptyList(), Collections.singletonList(b.b(Uri.parse(str))), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), null, null, false, Collections.emptyMap(), Collections.emptyList());
    }

    private static List<Uri> f(List<b> list, List<a> list2, List<a> list3, List<a> list4, List<a> list5) {
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < list.size(); i8++) {
            Uri uri = list.get(i8).f10600a;
            if (!arrayList.contains(uri)) {
                arrayList.add(uri);
            }
        }
        b(list2, arrayList);
        b(list3, arrayList);
        b(list4, arrayList);
        b(list5, arrayList);
        return arrayList;
    }

    @Override // g5.b
    /* renamed from: c */
    public e a(List<StreamKey> list) {
        return new e(this.f22159a, this.f22160b, d(this.f10587e, 0, list), Collections.emptyList(), d(this.f10589g, 1, list), d(this.f10590h, 2, list), Collections.emptyList(), this.f10592j, this.f10593k, this.f22161c, this.f10594l, this.f10595m);
    }
}
