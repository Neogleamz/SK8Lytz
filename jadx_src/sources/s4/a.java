package s4;

import b6.z;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.mp4.MotionPhotoMetadata;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import n4.k;
import n4.l;
import n4.m;
import n4.y;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements k {

    /* renamed from: b  reason: collision with root package name */
    private m f22774b;

    /* renamed from: c  reason: collision with root package name */
    private int f22775c;

    /* renamed from: d  reason: collision with root package name */
    private int f22776d;

    /* renamed from: e  reason: collision with root package name */
    private int f22777e;

    /* renamed from: g  reason: collision with root package name */
    private MotionPhotoMetadata f22779g;

    /* renamed from: h  reason: collision with root package name */
    private l f22780h;

    /* renamed from: i  reason: collision with root package name */
    private c f22781i;

    /* renamed from: j  reason: collision with root package name */
    private v4.k f22782j;

    /* renamed from: a  reason: collision with root package name */
    private final z f22773a = new z(6);

    /* renamed from: f  reason: collision with root package name */
    private long f22778f = -1;

    private void a(l lVar) {
        this.f22773a.Q(2);
        lVar.k(this.f22773a.e(), 0, 2);
        lVar.f(this.f22773a.N() - 2);
    }

    private void d() {
        h(new Metadata.Entry[0]);
        ((m) b6.a.e(this.f22774b)).o();
        this.f22774b.m(new z.b(-9223372036854775807L));
        this.f22775c = 6;
    }

    private static MotionPhotoMetadata f(String str, long j8) {
        b a9;
        if (j8 == -1 || (a9 = e.a(str)) == null) {
            return null;
        }
        return a9.a(j8);
    }

    private void h(Metadata.Entry... entryArr) {
        ((m) b6.a.e(this.f22774b)).e(RecognitionOptions.UPC_E, 4).f(new w0.b().M("image/jpeg").Z(new Metadata(entryArr)).G());
    }

    private int i(l lVar) {
        this.f22773a.Q(2);
        lVar.k(this.f22773a.e(), 0, 2);
        return this.f22773a.N();
    }

    private void j(l lVar) {
        int i8;
        this.f22773a.Q(2);
        lVar.readFully(this.f22773a.e(), 0, 2);
        int N = this.f22773a.N();
        this.f22776d = N;
        if (N == 65498) {
            if (this.f22778f == -1) {
                d();
                return;
            }
            i8 = 4;
        } else if ((N >= 65488 && N <= 65497) || N == 65281) {
            return;
        } else {
            i8 = 1;
        }
        this.f22775c = i8;
    }

    private void k(l lVar) {
        String B;
        if (this.f22776d == 65505) {
            b6.z zVar = new b6.z(this.f22777e);
            lVar.readFully(zVar.e(), 0, this.f22777e);
            if (this.f22779g == null && "http://ns.adobe.com/xap/1.0/".equals(zVar.B()) && (B = zVar.B()) != null) {
                MotionPhotoMetadata f5 = f(B, lVar.b());
                this.f22779g = f5;
                if (f5 != null) {
                    this.f22778f = f5.f10132d;
                }
            }
        } else {
            lVar.i(this.f22777e);
        }
        this.f22775c = 0;
    }

    private void l(l lVar) {
        this.f22773a.Q(2);
        lVar.readFully(this.f22773a.e(), 0, 2);
        this.f22777e = this.f22773a.N() - 2;
        this.f22775c = 2;
    }

    private void m(l lVar) {
        if (lVar.d(this.f22773a.e(), 0, 1, true)) {
            lVar.h();
            if (this.f22782j == null) {
                this.f22782j = new v4.k();
            }
            c cVar = new c(lVar, this.f22778f);
            this.f22781i = cVar;
            if (this.f22782j.g(cVar)) {
                this.f22782j.b(new d(this.f22778f, (m) b6.a.e(this.f22774b)));
                n();
                return;
            }
        }
        d();
    }

    private void n() {
        h((Metadata.Entry) b6.a.e(this.f22779g));
        this.f22775c = 5;
    }

    @Override // n4.k
    public void b(m mVar) {
        this.f22774b = mVar;
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        if (j8 == 0) {
            this.f22775c = 0;
            this.f22782j = null;
        } else if (this.f22775c == 5) {
            ((v4.k) b6.a.e(this.f22782j)).c(j8, j9);
        }
    }

    @Override // n4.k
    public int e(l lVar, y yVar) {
        int i8 = this.f22775c;
        if (i8 == 0) {
            j(lVar);
            return 0;
        } else if (i8 == 1) {
            l(lVar);
            return 0;
        } else if (i8 == 2) {
            k(lVar);
            return 0;
        } else if (i8 == 4) {
            long position = lVar.getPosition();
            long j8 = this.f22778f;
            if (position != j8) {
                yVar.f22152a = j8;
                return 1;
            }
            m(lVar);
            return 0;
        } else if (i8 != 5) {
            if (i8 == 6) {
                return -1;
            }
            throw new IllegalStateException();
        } else {
            if (this.f22781i == null || lVar != this.f22780h) {
                this.f22780h = lVar;
                this.f22781i = new c(lVar, this.f22778f);
            }
            int e8 = ((v4.k) b6.a.e(this.f22782j)).e(this.f22781i, yVar);
            if (e8 == 1) {
                yVar.f22152a += this.f22778f;
            }
            return e8;
        }
    }

    @Override // n4.k
    public boolean g(l lVar) {
        if (i(lVar) != 65496) {
            return false;
        }
        int i8 = i(lVar);
        this.f22776d = i8;
        if (i8 == 65504) {
            a(lVar);
            this.f22776d = i(lVar);
        }
        if (this.f22776d != 65505) {
            return false;
        }
        lVar.f(2);
        this.f22773a.Q(6);
        lVar.k(this.f22773a.e(), 0, 6);
        return this.f22773a.J() == 1165519206 && this.f22773a.N() == 0;
    }

    @Override // n4.k
    public void release() {
        v4.k kVar = this.f22782j;
        if (kVar != null) {
            kVar.release();
        }
    }
}
