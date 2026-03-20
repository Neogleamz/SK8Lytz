package i4;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import b6.p;
import c6.g;
import c6.v;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.audio.DefaultAudioSink;
import com.google.android.exoplayer2.c2;
import com.google.android.exoplayer2.mediacodec.h;
import com.google.android.exoplayer2.mediacodec.j;
import com.google.android.exoplayer2.mediacodec.l;
import java.util.ArrayList;
import k4.e;
import p5.m;
import p5.n;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d implements h0 {

    /* renamed from: a  reason: collision with root package name */
    private final Context f20491a;

    /* renamed from: e  reason: collision with root package name */
    private boolean f20495e;

    /* renamed from: g  reason: collision with root package name */
    private boolean f20497g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f20498h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f20499i;

    /* renamed from: b  reason: collision with root package name */
    private final h f20492b = new h();

    /* renamed from: c  reason: collision with root package name */
    private int f20493c = 0;

    /* renamed from: d  reason: collision with root package name */
    private long f20494d = 5000;

    /* renamed from: f  reason: collision with root package name */
    private l f20496f = l.f10041a;

    public d(Context context) {
        this.f20491a = context;
    }

    @Override // i4.h0
    public c2[] a(Handler handler, v vVar, com.google.android.exoplayer2.audio.b bVar, m mVar, a5.d dVar) {
        ArrayList<c2> arrayList = new ArrayList<>();
        h(this.f20491a, this.f20493c, this.f20496f, this.f20495e, handler, vVar, this.f20494d, arrayList);
        AudioSink c9 = c(this.f20491a, this.f20497g, this.f20498h, this.f20499i);
        if (c9 != null) {
            b(this.f20491a, this.f20493c, this.f20496f, this.f20495e, c9, handler, bVar, arrayList);
        }
        g(this.f20491a, mVar, handler.getLooper(), this.f20493c, arrayList);
        e(this.f20491a, dVar, handler.getLooper(), this.f20493c, arrayList);
        d(this.f20491a, this.f20493c, arrayList);
        f(this.f20491a, handler, this.f20493c, arrayList);
        return (c2[]) arrayList.toArray(new c2[0]);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(15:5|(1:7)|8|9|10|11|(2:12|13)|14|15|16|17|18|19|(5:21|22|23|24|25)|(2:27|28)) */
    /* JADX WARN: Can't wrap try/catch for region: R(8:(2:12|13)|15|16|17|18|19|(5:21|22|23|24|25)|(2:27|28)) */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0089, code lost:
        r4 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0094, code lost:
        r6 = r4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void b(android.content.Context r15, int r16, com.google.android.exoplayer2.mediacodec.l r17, boolean r18, com.google.android.exoplayer2.audio.AudioSink r19, android.os.Handler r20, com.google.android.exoplayer2.audio.b r21, java.util.ArrayList<com.google.android.exoplayer2.c2> r22) {
        /*
            Method dump skipped, instructions count: 259
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: i4.d.b(android.content.Context, int, com.google.android.exoplayer2.mediacodec.l, boolean, com.google.android.exoplayer2.audio.AudioSink, android.os.Handler, com.google.android.exoplayer2.audio.b, java.util.ArrayList):void");
    }

    protected AudioSink c(Context context, boolean z4, boolean z8, boolean z9) {
        return new DefaultAudioSink.f().g(e.c(context)).i(z4).h(z8).j(z9 ? 1 : 0).f();
    }

    protected void d(Context context, int i8, ArrayList<c2> arrayList) {
        arrayList.add(new d6.b());
    }

    protected void e(Context context, a5.d dVar, Looper looper, int i8, ArrayList<c2> arrayList) {
        arrayList.add(new com.google.android.exoplayer2.metadata.a(dVar, looper));
    }

    protected void f(Context context, Handler handler, int i8, ArrayList<c2> arrayList) {
    }

    protected void g(Context context, m mVar, Looper looper, int i8, ArrayList<c2> arrayList) {
        arrayList.add(new n(mVar, looper));
    }

    protected void h(Context context, int i8, l lVar, boolean z4, Handler handler, v vVar, long j8, ArrayList<c2> arrayList) {
        int i9;
        arrayList.add(new g(context, i(), lVar, j8, z4, handler, vVar, 50));
        if (i8 == 0) {
            return;
        }
        int size = arrayList.size();
        if (i8 == 2) {
            size--;
        }
        try {
            try {
                i9 = size + 1;
                try {
                    arrayList.add(size, (c2) Class.forName("com.google.android.exoplayer2.ext.vp9.LibvpxVideoRenderer").getConstructor(Long.TYPE, Handler.class, v.class, Integer.TYPE).newInstance(Long.valueOf(j8), handler, vVar, 50));
                    p.f("DefaultRenderersFactory", "Loaded LibvpxVideoRenderer.");
                } catch (ClassNotFoundException unused) {
                    size = i9;
                    i9 = size;
                    arrayList.add(i9, (c2) Class.forName("com.google.android.exoplayer2.ext.av1.Libgav1VideoRenderer").getConstructor(Long.TYPE, Handler.class, v.class, Integer.TYPE).newInstance(Long.valueOf(j8), handler, vVar, 50));
                    p.f("DefaultRenderersFactory", "Loaded Libgav1VideoRenderer.");
                }
            } catch (Exception e8) {
                throw new RuntimeException("Error instantiating VP9 extension", e8);
            }
        } catch (ClassNotFoundException unused2) {
        }
        try {
            arrayList.add(i9, (c2) Class.forName("com.google.android.exoplayer2.ext.av1.Libgav1VideoRenderer").getConstructor(Long.TYPE, Handler.class, v.class, Integer.TYPE).newInstance(Long.valueOf(j8), handler, vVar, 50));
            p.f("DefaultRenderersFactory", "Loaded Libgav1VideoRenderer.");
        } catch (ClassNotFoundException unused3) {
        } catch (Exception e9) {
            throw new RuntimeException("Error instantiating AV1 extension", e9);
        }
    }

    protected j.b i() {
        return this.f20492b;
    }

    public d j(boolean z4) {
        this.f20499i = z4;
        return this;
    }
}
