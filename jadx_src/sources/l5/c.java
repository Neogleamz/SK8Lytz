package l5;

import android.net.Uri;
import b6.l0;
import com.google.android.exoplayer2.offline.StreamKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c implements g5.b<c> {

    /* renamed from: a  reason: collision with root package name */
    public final long f21634a;

    /* renamed from: b  reason: collision with root package name */
    public final long f21635b;

    /* renamed from: c  reason: collision with root package name */
    public final long f21636c;

    /* renamed from: d  reason: collision with root package name */
    public final boolean f21637d;

    /* renamed from: e  reason: collision with root package name */
    public final long f21638e;

    /* renamed from: f  reason: collision with root package name */
    public final long f21639f;

    /* renamed from: g  reason: collision with root package name */
    public final long f21640g;

    /* renamed from: h  reason: collision with root package name */
    public final long f21641h;

    /* renamed from: i  reason: collision with root package name */
    public final o f21642i;

    /* renamed from: j  reason: collision with root package name */
    public final l f21643j;

    /* renamed from: k  reason: collision with root package name */
    public final Uri f21644k;

    /* renamed from: l  reason: collision with root package name */
    public final h f21645l;

    /* renamed from: m  reason: collision with root package name */
    private final List<g> f21646m;

    public c(long j8, long j9, long j10, boolean z4, long j11, long j12, long j13, long j14, h hVar, o oVar, l lVar, Uri uri, List<g> list) {
        this.f21634a = j8;
        this.f21635b = j9;
        this.f21636c = j10;
        this.f21637d = z4;
        this.f21638e = j11;
        this.f21639f = j12;
        this.f21640g = j13;
        this.f21641h = j14;
        this.f21645l = hVar;
        this.f21642i = oVar;
        this.f21644k = uri;
        this.f21643j = lVar;
        this.f21646m = list == null ? Collections.emptyList() : list;
    }

    private static ArrayList<a> c(List<a> list, LinkedList<StreamKey> linkedList) {
        StreamKey poll = linkedList.poll();
        int i8 = poll.f10209a;
        ArrayList<a> arrayList = new ArrayList<>();
        do {
            int i9 = poll.f10210b;
            a aVar = list.get(i9);
            List<j> list2 = aVar.f21626c;
            ArrayList arrayList2 = new ArrayList();
            do {
                arrayList2.add(list2.get(poll.f10211c));
                poll = linkedList.poll();
                if (poll.f10209a != i8) {
                    break;
                }
            } while (poll.f10210b == i9);
            arrayList.add(new a(aVar.f21624a, aVar.f21625b, arrayList2, aVar.f21627d, aVar.f21628e, aVar.f21629f));
        } while (poll.f10209a == i8);
        linkedList.addFirst(poll);
        return arrayList;
    }

    @Override // g5.b
    /* renamed from: b */
    public final c a(List<StreamKey> list) {
        LinkedList linkedList = new LinkedList(list);
        Collections.sort(linkedList);
        linkedList.add(new StreamKey(-1, -1, -1));
        ArrayList arrayList = new ArrayList();
        long j8 = 0;
        int i8 = 0;
        while (true) {
            if (i8 >= e()) {
                break;
            }
            if (((StreamKey) linkedList.peek()).f10209a != i8) {
                long f5 = f(i8);
                if (f5 != -9223372036854775807L) {
                    j8 += f5;
                }
            } else {
                g d8 = d(i8);
                arrayList.add(new g(d8.f21669a, d8.f21670b - j8, c(d8.f21671c, linkedList), d8.f21672d));
            }
            i8++;
        }
        long j9 = this.f21635b;
        return new c(this.f21634a, j9 != -9223372036854775807L ? j9 - j8 : -9223372036854775807L, this.f21636c, this.f21637d, this.f21638e, this.f21639f, this.f21640g, this.f21641h, this.f21645l, this.f21642i, this.f21643j, this.f21644k, arrayList);
    }

    public final g d(int i8) {
        return this.f21646m.get(i8);
    }

    public final int e() {
        return this.f21646m.size();
    }

    public final long f(int i8) {
        if (i8 == this.f21646m.size() - 1) {
            long j8 = this.f21635b;
            if (j8 == -9223372036854775807L) {
                return -9223372036854775807L;
            }
            return j8 - this.f21646m.get(i8).f21670b;
        }
        return this.f21646m.get(i8 + 1).f21670b - this.f21646m.get(i8).f21670b;
    }

    public final long g(int i8) {
        return l0.C0(f(i8));
    }
}
