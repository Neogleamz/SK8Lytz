package n4;

import android.net.Uri;
import com.google.android.exoplayer2.w0;
import com.google.common.collect.ImmutableList;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import u4.f;
import v4.g;
import x4.h0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h implements p {

    /* renamed from: o  reason: collision with root package name */
    private static final int[] f22105o = {5, 4, 12, 8, 3, 10, 9, 11, 6, 2, 0, 1, 7, 16, 15, 14};

    /* renamed from: p  reason: collision with root package name */
    private static final a f22106p = new a(g.a);
    private static final a q = new a(f.a);

    /* renamed from: b  reason: collision with root package name */
    private boolean f22107b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f22108c;

    /* renamed from: d  reason: collision with root package name */
    private int f22109d;

    /* renamed from: e  reason: collision with root package name */
    private int f22110e;

    /* renamed from: f  reason: collision with root package name */
    private int f22111f;

    /* renamed from: g  reason: collision with root package name */
    private int f22112g;

    /* renamed from: h  reason: collision with root package name */
    private int f22113h;

    /* renamed from: i  reason: collision with root package name */
    private int f22114i;

    /* renamed from: j  reason: collision with root package name */
    private int f22115j;

    /* renamed from: l  reason: collision with root package name */
    private int f22117l;

    /* renamed from: k  reason: collision with root package name */
    private int f22116k = 1;

    /* renamed from: n  reason: collision with root package name */
    private int f22119n = 112800;

    /* renamed from: m  reason: collision with root package name */
    private ImmutableList<w0> f22118m = ImmutableList.E();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final InterfaceC0192a f22120a;

        /* renamed from: b  reason: collision with root package name */
        private final AtomicBoolean f22121b = new AtomicBoolean(false);

        /* renamed from: c  reason: collision with root package name */
        private Constructor<? extends k> f22122c;

        /* renamed from: n4.h$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public interface InterfaceC0192a {
            Constructor<? extends k> a();
        }

        public a(InterfaceC0192a interfaceC0192a) {
            this.f22120a = interfaceC0192a;
        }

        private Constructor<? extends k> b() {
            synchronized (this.f22121b) {
                if (this.f22121b.get()) {
                    return this.f22122c;
                }
                try {
                    return this.f22120a.a();
                } catch (ClassNotFoundException unused) {
                    this.f22121b.set(true);
                    return this.f22122c;
                } catch (Exception e8) {
                    throw new RuntimeException("Error instantiating extension", e8);
                }
            }
        }

        public k a(Object... objArr) {
            Constructor<? extends k> b9 = b();
            if (b9 == null) {
                return null;
            }
            try {
                return b9.newInstance(objArr);
            } catch (Exception e8) {
                throw new IllegalStateException("Unexpected error creating extractor", e8);
            }
        }
    }

    private void g(int i8, List<k> list) {
        k bVar;
        switch (i8) {
            case 0:
                bVar = new x4.b();
                break;
            case 1:
                bVar = new x4.e();
                break;
            case 2:
                bVar = new x4.h((this.f22108c ? 2 : 0) | this.f22109d | (this.f22107b ? 1 : 0));
                break;
            case 3:
                bVar = new o4.b((this.f22108c ? 2 : 0) | this.f22110e | (this.f22107b ? 1 : 0));
                break;
            case 4:
                bVar = f22106p.a(Integer.valueOf(this.f22111f));
                if (bVar == null) {
                    bVar = new q4.d(this.f22111f);
                    break;
                }
                break;
            case 5:
                bVar = new com.google.android.exoplayer2.extractor.flv.b();
                break;
            case 6:
                bVar = new t4.e(this.f22112g);
                break;
            case 7:
                bVar = new f((this.f22108c ? 2 : 0) | this.f22115j | (this.f22107b ? 1 : 0));
                break;
            case 8:
                list.add(new g(this.f22114i));
                bVar = new v4.k(this.f22113h);
                break;
            case 9:
                bVar = new w4.d();
                break;
            case 10:
                bVar = new x4.a0();
                break;
            case 11:
                bVar = new h0(this.f22116k, new b6.h0(0L), new x4.j(this.f22117l, this.f22118m), this.f22119n);
                break;
            case 12:
                bVar = new y4.b();
                break;
            case 13:
            default:
                return;
            case 14:
                bVar = new s4.a();
                break;
            case 15:
                bVar = q.a(new Object[0]);
                if (bVar == null) {
                    return;
                }
                break;
            case 16:
                bVar = new p4.b();
                break;
        }
        list.add(bVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Constructor<? extends k> h() {
        if (Boolean.TRUE.equals(Class.forName("com.google.android.exoplayer2.ext.flac.FlacLibrary").getMethod("isAvailable", new Class[0]).invoke(null, new Object[0]))) {
            return Class.forName("com.google.android.exoplayer2.ext.flac.FlacExtractor").asSubclass(k.class).getConstructor(Integer.TYPE);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Constructor<? extends k> i() {
        return Class.forName("com.google.android.exoplayer2.decoder.midi.MidiExtractor").asSubclass(k.class).getConstructor(new Class[0]);
    }

    @Override // n4.p
    public synchronized k[] b() {
        return c(Uri.EMPTY, new HashMap());
    }

    @Override // n4.p
    public synchronized k[] c(Uri uri, Map<String, List<String>> map) {
        ArrayList arrayList;
        int[] iArr = f22105o;
        arrayList = new ArrayList(iArr.length);
        int b9 = b6.j.b(map);
        if (b9 != -1) {
            g(b9, arrayList);
        }
        int c9 = b6.j.c(uri);
        if (c9 != -1 && c9 != b9) {
            g(c9, arrayList);
        }
        for (int i8 : iArr) {
            if (i8 != b9 && i8 != c9) {
                g(i8, arrayList);
            }
        }
        return (k[]) arrayList.toArray(new k[arrayList.size()]);
    }

    public synchronized h j(boolean z4) {
        this.f22108c = z4;
        return this;
    }

    public synchronized h k(boolean z4) {
        this.f22107b = z4;
        return this;
    }

    public synchronized h l(int i8) {
        this.f22115j = i8;
        return this;
    }
}
