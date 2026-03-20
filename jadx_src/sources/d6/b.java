package d6;

import b6.l0;
import b6.z;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.f;
import com.google.android.exoplayer2.w0;
import i4.f0;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b extends f {

    /* renamed from: p  reason: collision with root package name */
    private final DecoderInputBuffer f19740p;
    private final z q;

    /* renamed from: t  reason: collision with root package name */
    private long f19741t;

    /* renamed from: w  reason: collision with root package name */
    private a f19742w;

    /* renamed from: x  reason: collision with root package name */
    private long f19743x;

    public b() {
        super(6);
        this.f19740p = new DecoderInputBuffer(1);
        this.q = new z();
    }

    private float[] Y(ByteBuffer byteBuffer) {
        if (byteBuffer.remaining() != 16) {
            return null;
        }
        this.q.S(byteBuffer.array(), byteBuffer.limit());
        this.q.U(byteBuffer.arrayOffset() + 4);
        float[] fArr = new float[3];
        for (int i8 = 0; i8 < 3; i8++) {
            fArr[i8] = Float.intBitsToFloat(this.q.u());
        }
        return fArr;
    }

    private void Z() {
        a aVar = this.f19742w;
        if (aVar != null) {
            aVar.c();
        }
    }

    @Override // com.google.android.exoplayer2.f
    protected void O() {
        Z();
    }

    @Override // com.google.android.exoplayer2.f
    protected void Q(long j8, boolean z4) {
        this.f19743x = Long.MIN_VALUE;
        Z();
    }

    @Override // com.google.android.exoplayer2.f
    protected void U(w0[] w0VarArr, long j8, long j9) {
        this.f19741t = j9;
    }

    @Override // i4.f0
    public int a(w0 w0Var) {
        return f0.u("application/x-camera-motion".equals(w0Var.f11207m) ? 4 : 0);
    }

    @Override // com.google.android.exoplayer2.c2
    public boolean b() {
        return i();
    }

    @Override // com.google.android.exoplayer2.c2
    public boolean e() {
        return true;
    }

    @Override // com.google.android.exoplayer2.c2, i4.f0
    public String getName() {
        return "CameraMotionRenderer";
    }

    @Override // com.google.android.exoplayer2.c2
    public void w(long j8, long j9) {
        while (!i() && this.f19743x < 100000 + j8) {
            this.f19740p.k();
            if (V(J(), this.f19740p, 0) != -4 || this.f19740p.t()) {
                return;
            }
            DecoderInputBuffer decoderInputBuffer = this.f19740p;
            this.f19743x = decoderInputBuffer.f9514e;
            if (this.f19742w != null && !decoderInputBuffer.s()) {
                this.f19740p.A();
                float[] Y = Y((ByteBuffer) l0.j(this.f19740p.f9512c));
                if (Y != null) {
                    ((a) l0.j(this.f19742w)).a(this.f19743x - this.f19741t, Y);
                }
            }
        }
    }

    @Override // com.google.android.exoplayer2.f, com.google.android.exoplayer2.z1.b
    public void x(int i8, Object obj) {
        if (i8 == 8) {
            this.f19742w = (a) obj;
        } else {
            super.x(i8, obj);
        }
    }
}
