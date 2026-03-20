package i5;

import android.net.Uri;
import android.os.Bundle;
import b6.l0;
import com.google.android.exoplayer2.g;
import java.util.ArrayList;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c implements g {

    /* renamed from: g  reason: collision with root package name */
    public static final c f20513g = new c(null, new a[0], 0, -9223372036854775807L, 0);

    /* renamed from: h  reason: collision with root package name */
    private static final a f20514h = new a(0).i(0);

    /* renamed from: j  reason: collision with root package name */
    private static final String f20515j = l0.r0(1);

    /* renamed from: k  reason: collision with root package name */
    private static final String f20516k = l0.r0(2);

    /* renamed from: l  reason: collision with root package name */
    private static final String f20517l = l0.r0(3);

    /* renamed from: m  reason: collision with root package name */
    private static final String f20518m = l0.r0(4);

    /* renamed from: n  reason: collision with root package name */
    public static final g.a<c> f20519n = i5.a.a;

    /* renamed from: a  reason: collision with root package name */
    public final Object f20520a;

    /* renamed from: b  reason: collision with root package name */
    public final int f20521b;

    /* renamed from: c  reason: collision with root package name */
    public final long f20522c;

    /* renamed from: d  reason: collision with root package name */
    public final long f20523d;

    /* renamed from: e  reason: collision with root package name */
    public final int f20524e;

    /* renamed from: f  reason: collision with root package name */
    private final a[] f20525f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a implements g {

        /* renamed from: j  reason: collision with root package name */
        private static final String f20526j = l0.r0(0);

        /* renamed from: k  reason: collision with root package name */
        private static final String f20527k = l0.r0(1);

        /* renamed from: l  reason: collision with root package name */
        private static final String f20528l = l0.r0(2);

        /* renamed from: m  reason: collision with root package name */
        private static final String f20529m = l0.r0(3);

        /* renamed from: n  reason: collision with root package name */
        private static final String f20530n = l0.r0(4);

        /* renamed from: p  reason: collision with root package name */
        private static final String f20531p = l0.r0(5);
        private static final String q = l0.r0(6);

        /* renamed from: t  reason: collision with root package name */
        private static final String f20532t = l0.r0(7);

        /* renamed from: w  reason: collision with root package name */
        public static final g.a<a> f20533w = b.a;

        /* renamed from: a  reason: collision with root package name */
        public final long f20534a;

        /* renamed from: b  reason: collision with root package name */
        public final int f20535b;

        /* renamed from: c  reason: collision with root package name */
        public final int f20536c;

        /* renamed from: d  reason: collision with root package name */
        public final Uri[] f20537d;

        /* renamed from: e  reason: collision with root package name */
        public final int[] f20538e;

        /* renamed from: f  reason: collision with root package name */
        public final long[] f20539f;

        /* renamed from: g  reason: collision with root package name */
        public final long f20540g;

        /* renamed from: h  reason: collision with root package name */
        public final boolean f20541h;

        public a(long j8) {
            this(j8, -1, -1, new int[0], new Uri[0], new long[0], 0L, false);
        }

        private a(long j8, int i8, int i9, int[] iArr, Uri[] uriArr, long[] jArr, long j9, boolean z4) {
            b6.a.a(iArr.length == uriArr.length);
            this.f20534a = j8;
            this.f20535b = i8;
            this.f20536c = i9;
            this.f20538e = iArr;
            this.f20537d = uriArr;
            this.f20539f = jArr;
            this.f20540g = j9;
            this.f20541h = z4;
        }

        private static long[] b(long[] jArr, int i8) {
            int length = jArr.length;
            int max = Math.max(i8, length);
            long[] copyOf = Arrays.copyOf(jArr, max);
            Arrays.fill(copyOf, length, max, -9223372036854775807L);
            return copyOf;
        }

        private static int[] c(int[] iArr, int i8) {
            int length = iArr.length;
            int max = Math.max(i8, length);
            int[] copyOf = Arrays.copyOf(iArr, max);
            Arrays.fill(copyOf, length, max, 0);
            return copyOf;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static a d(Bundle bundle) {
            long j8 = bundle.getLong(f20526j);
            int i8 = bundle.getInt(f20527k);
            int i9 = bundle.getInt(f20532t);
            ArrayList parcelableArrayList = bundle.getParcelableArrayList(f20528l);
            int[] intArray = bundle.getIntArray(f20529m);
            long[] longArray = bundle.getLongArray(f20530n);
            long j9 = bundle.getLong(f20531p);
            boolean z4 = bundle.getBoolean(q);
            if (intArray == null) {
                intArray = new int[0];
            }
            return new a(j8, i8, i9, intArray, parcelableArrayList == null ? new Uri[0] : (Uri[]) parcelableArrayList.toArray(new Uri[0]), longArray == null ? new long[0] : longArray, j9, z4);
        }

        public int e() {
            return f(-1);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || a.class != obj.getClass()) {
                return false;
            }
            a aVar = (a) obj;
            return this.f20534a == aVar.f20534a && this.f20535b == aVar.f20535b && this.f20536c == aVar.f20536c && Arrays.equals(this.f20537d, aVar.f20537d) && Arrays.equals(this.f20538e, aVar.f20538e) && Arrays.equals(this.f20539f, aVar.f20539f) && this.f20540g == aVar.f20540g && this.f20541h == aVar.f20541h;
        }

        public int f(int i8) {
            int i9 = i8 + 1;
            while (true) {
                int[] iArr = this.f20538e;
                if (i9 >= iArr.length || this.f20541h || iArr[i9] == 0 || iArr[i9] == 1) {
                    break;
                }
                i9++;
            }
            return i9;
        }

        public boolean g() {
            if (this.f20535b == -1) {
                return true;
            }
            for (int i8 = 0; i8 < this.f20535b; i8++) {
                int[] iArr = this.f20538e;
                if (iArr[i8] == 0 || iArr[i8] == 1) {
                    return true;
                }
            }
            return false;
        }

        public boolean h() {
            return this.f20535b == -1 || e() < this.f20535b;
        }

        public int hashCode() {
            long j8 = this.f20534a;
            long j9 = this.f20540g;
            return (((((((((((((this.f20535b * 31) + this.f20536c) * 31) + ((int) (j8 ^ (j8 >>> 32)))) * 31) + Arrays.hashCode(this.f20537d)) * 31) + Arrays.hashCode(this.f20538e)) * 31) + Arrays.hashCode(this.f20539f)) * 31) + ((int) (j9 ^ (j9 >>> 32)))) * 31) + (this.f20541h ? 1 : 0);
        }

        public a i(int i8) {
            int[] c9 = c(this.f20538e, i8);
            long[] b9 = b(this.f20539f, i8);
            return new a(this.f20534a, i8, this.f20536c, c9, (Uri[]) Arrays.copyOf(this.f20537d, i8), b9, this.f20540g, this.f20541h);
        }
    }

    private c(Object obj, a[] aVarArr, long j8, long j9, int i8) {
        this.f20520a = obj;
        this.f20522c = j8;
        this.f20523d = j9;
        this.f20521b = aVarArr.length + i8;
        this.f20525f = aVarArr;
        this.f20524e = i8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static c b(Bundle bundle) {
        a[] aVarArr;
        ArrayList parcelableArrayList = bundle.getParcelableArrayList(f20515j);
        if (parcelableArrayList == null) {
            aVarArr = new a[0];
        } else {
            a[] aVarArr2 = new a[parcelableArrayList.size()];
            for (int i8 = 0; i8 < parcelableArrayList.size(); i8++) {
                aVarArr2[i8] = a.f20533w.a((Bundle) parcelableArrayList.get(i8));
            }
            aVarArr = aVarArr2;
        }
        String str = f20516k;
        c cVar = f20513g;
        return new c(null, aVarArr, bundle.getLong(str, cVar.f20522c), bundle.getLong(f20517l, cVar.f20523d), bundle.getInt(f20518m, cVar.f20524e));
    }

    private boolean f(long j8, long j9, int i8) {
        if (j8 == Long.MIN_VALUE) {
            return false;
        }
        long j10 = c(i8).f20534a;
        return j10 == Long.MIN_VALUE ? j9 == -9223372036854775807L || j8 < j9 : j8 < j10;
    }

    public a c(int i8) {
        int i9 = this.f20524e;
        return i8 < i9 ? f20514h : this.f20525f[i8 - i9];
    }

    public int d(long j8, long j9) {
        if (j8 != Long.MIN_VALUE) {
            if (j9 == -9223372036854775807L || j8 < j9) {
                int i8 = this.f20524e;
                while (i8 < this.f20521b && ((c(i8).f20534a != Long.MIN_VALUE && c(i8).f20534a <= j8) || !c(i8).h())) {
                    i8++;
                }
                if (i8 < this.f20521b) {
                    return i8;
                }
                return -1;
            }
            return -1;
        }
        return -1;
    }

    public int e(long j8, long j9) {
        int i8 = this.f20521b - 1;
        while (i8 >= 0 && f(j8, j9, i8)) {
            i8--;
        }
        if (i8 < 0 || !c(i8).g()) {
            return -1;
        }
        return i8;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || c.class != obj.getClass()) {
            return false;
        }
        c cVar = (c) obj;
        return l0.c(this.f20520a, cVar.f20520a) && this.f20521b == cVar.f20521b && this.f20522c == cVar.f20522c && this.f20523d == cVar.f20523d && this.f20524e == cVar.f20524e && Arrays.equals(this.f20525f, cVar.f20525f);
    }

    public int hashCode() {
        int i8 = this.f20521b * 31;
        Object obj = this.f20520a;
        return ((((((((i8 + (obj == null ? 0 : obj.hashCode())) * 31) + ((int) this.f20522c)) * 31) + ((int) this.f20523d)) * 31) + this.f20524e) * 31) + Arrays.hashCode(this.f20525f);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AdPlaybackState(adsId=");
        sb.append(this.f20520a);
        sb.append(", adResumePositionUs=");
        sb.append(this.f20522c);
        sb.append(", adGroups=[");
        for (int i8 = 0; i8 < this.f20525f.length; i8++) {
            sb.append("adGroup(timeUs=");
            sb.append(this.f20525f[i8].f20534a);
            sb.append(", ads=[");
            for (int i9 = 0; i9 < this.f20525f[i8].f20538e.length; i9++) {
                sb.append("ad(state=");
                int i10 = this.f20525f[i8].f20538e[i9];
                sb.append(i10 != 0 ? i10 != 1 ? i10 != 2 ? i10 != 3 ? i10 != 4 ? '?' : '!' : 'P' : 'S' : 'R' : '_');
                sb.append(", durationUs=");
                sb.append(this.f20525f[i8].f20539f[i9]);
                sb.append(')');
                if (i9 < this.f20525f[i8].f20538e.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("])");
            if (i8 < this.f20525f.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("])");
        return sb.toString();
    }
}
