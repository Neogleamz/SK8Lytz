package com.google.android.exoplayer2.source.smoothstreaming.manifest;

import android.net.Uri;
import b6.j0;
import b6.l0;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.w0;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import v4.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements g5.b<a> {

    /* renamed from: a  reason: collision with root package name */
    public final int f10764a;

    /* renamed from: b  reason: collision with root package name */
    public final int f10765b;

    /* renamed from: c  reason: collision with root package name */
    public final int f10766c;

    /* renamed from: d  reason: collision with root package name */
    public final boolean f10767d;

    /* renamed from: e  reason: collision with root package name */
    public final C0115a f10768e;

    /* renamed from: f  reason: collision with root package name */
    public final b[] f10769f;

    /* renamed from: g  reason: collision with root package name */
    public final long f10770g;

    /* renamed from: h  reason: collision with root package name */
    public final long f10771h;

    /* renamed from: com.google.android.exoplayer2.source.smoothstreaming.manifest.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0115a {

        /* renamed from: a  reason: collision with root package name */
        public final UUID f10772a;

        /* renamed from: b  reason: collision with root package name */
        public final byte[] f10773b;

        /* renamed from: c  reason: collision with root package name */
        public final p[] f10774c;

        public C0115a(UUID uuid, byte[] bArr, p[] pVarArr) {
            this.f10772a = uuid;
            this.f10773b = bArr;
            this.f10774c = pVarArr;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        public final int f10775a;

        /* renamed from: b  reason: collision with root package name */
        public final String f10776b;

        /* renamed from: c  reason: collision with root package name */
        public final long f10777c;

        /* renamed from: d  reason: collision with root package name */
        public final String f10778d;

        /* renamed from: e  reason: collision with root package name */
        public final int f10779e;

        /* renamed from: f  reason: collision with root package name */
        public final int f10780f;

        /* renamed from: g  reason: collision with root package name */
        public final int f10781g;

        /* renamed from: h  reason: collision with root package name */
        public final int f10782h;

        /* renamed from: i  reason: collision with root package name */
        public final String f10783i;

        /* renamed from: j  reason: collision with root package name */
        public final w0[] f10784j;

        /* renamed from: k  reason: collision with root package name */
        public final int f10785k;

        /* renamed from: l  reason: collision with root package name */
        private final String f10786l;

        /* renamed from: m  reason: collision with root package name */
        private final String f10787m;

        /* renamed from: n  reason: collision with root package name */
        private final List<Long> f10788n;

        /* renamed from: o  reason: collision with root package name */
        private final long[] f10789o;

        /* renamed from: p  reason: collision with root package name */
        private final long f10790p;

        public b(String str, String str2, int i8, String str3, long j8, String str4, int i9, int i10, int i11, int i12, String str5, w0[] w0VarArr, List<Long> list, long j9) {
            this(str, str2, i8, str3, j8, str4, i9, i10, i11, i12, str5, w0VarArr, list, l0.P0(list, 1000000L, j8), l0.O0(j9, 1000000L, j8));
        }

        private b(String str, String str2, int i8, String str3, long j8, String str4, int i9, int i10, int i11, int i12, String str5, w0[] w0VarArr, List<Long> list, long[] jArr, long j9) {
            this.f10786l = str;
            this.f10787m = str2;
            this.f10775a = i8;
            this.f10776b = str3;
            this.f10777c = j8;
            this.f10778d = str4;
            this.f10779e = i9;
            this.f10780f = i10;
            this.f10781g = i11;
            this.f10782h = i12;
            this.f10783i = str5;
            this.f10784j = w0VarArr;
            this.f10788n = list;
            this.f10789o = jArr;
            this.f10790p = j9;
            this.f10785k = list.size();
        }

        public Uri a(int i8, int i9) {
            b6.a.f(this.f10784j != null);
            b6.a.f(this.f10788n != null);
            b6.a.f(i9 < this.f10788n.size());
            String num = Integer.toString(this.f10784j[i8].f11203h);
            String l8 = this.f10788n.get(i9).toString();
            return j0.e(this.f10786l, this.f10787m.replace("{bitrate}", num).replace("{Bitrate}", num).replace("{start time}", l8).replace("{start_time}", l8));
        }

        public b b(w0[] w0VarArr) {
            return new b(this.f10786l, this.f10787m, this.f10775a, this.f10776b, this.f10777c, this.f10778d, this.f10779e, this.f10780f, this.f10781g, this.f10782h, this.f10783i, w0VarArr, this.f10788n, this.f10789o, this.f10790p);
        }

        public long c(int i8) {
            if (i8 == this.f10785k - 1) {
                return this.f10790p;
            }
            long[] jArr = this.f10789o;
            return jArr[i8 + 1] - jArr[i8];
        }

        public int d(long j8) {
            return l0.i(this.f10789o, j8, true, true);
        }

        public long e(int i8) {
            return this.f10789o[i8];
        }
    }

    private a(int i8, int i9, long j8, long j9, int i10, boolean z4, C0115a c0115a, b[] bVarArr) {
        this.f10764a = i8;
        this.f10765b = i9;
        this.f10770g = j8;
        this.f10771h = j9;
        this.f10766c = i10;
        this.f10767d = z4;
        this.f10768e = c0115a;
        this.f10769f = bVarArr;
    }

    public a(int i8, int i9, long j8, long j9, long j10, int i10, boolean z4, C0115a c0115a, b[] bVarArr) {
        this(i8, i9, j9 == 0 ? -9223372036854775807L : l0.O0(j9, 1000000L, j8), j10 != 0 ? l0.O0(j10, 1000000L, j8) : -9223372036854775807L, i10, z4, c0115a, bVarArr);
    }

    @Override // g5.b
    /* renamed from: b */
    public final a a(List<StreamKey> list) {
        ArrayList arrayList = new ArrayList(list);
        Collections.sort(arrayList);
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        b bVar = null;
        int i8 = 0;
        while (i8 < arrayList.size()) {
            StreamKey streamKey = (StreamKey) arrayList.get(i8);
            b bVar2 = this.f10769f[streamKey.f10210b];
            if (bVar2 != bVar && bVar != null) {
                arrayList2.add(bVar.b((w0[]) arrayList3.toArray(new w0[0])));
                arrayList3.clear();
            }
            arrayList3.add(bVar2.f10784j[streamKey.f10211c]);
            i8++;
            bVar = bVar2;
        }
        if (bVar != null) {
            arrayList2.add(bVar.b((w0[]) arrayList3.toArray(new w0[0])));
        }
        return new a(this.f10764a, this.f10765b, this.f10770g, this.f10771h, this.f10766c, this.f10767d, this.f10768e, (b[]) arrayList2.toArray(new b[0]));
    }
}
