package c1;

import android.graphics.Bitmap;
import android.graphics.Rect;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: f  reason: collision with root package name */
    private static final float[] f8209f;

    /* renamed from: g  reason: collision with root package name */
    private static final FloatBuffer f8210g;

    /* renamed from: a  reason: collision with root package name */
    private final float[] f8211a;

    /* renamed from: b  reason: collision with root package name */
    private final FloatBuffer f8212b;

    /* renamed from: c  reason: collision with root package name */
    private final int f8213c;

    /* renamed from: d  reason: collision with root package name */
    private final int f8214d;

    /* renamed from: e  reason: collision with root package name */
    private e f8215e;

    static {
        float[] fArr = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
        f8209f = fArr;
        f8210g = b(fArr);
    }

    public a(e eVar, int i8, int i9) {
        float[] fArr = new float[8];
        this.f8211a = fArr;
        this.f8212b = b(fArr);
        this.f8215e = eVar;
        this.f8213c = i8;
        this.f8214d = i9;
    }

    public static FloatBuffer b(float[] fArr) {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer asFloatBuffer = allocateDirect.asFloatBuffer();
        asFloatBuffer.put(fArr);
        asFloatBuffer.position(0);
        return asFloatBuffer;
    }

    public void a(int i8, float[] fArr, Rect rect) {
        f(rect);
        this.f8215e.e(e.f8285h, f8210g, 0, 4, 2, 8, fArr, this.f8212b, i8, 8);
    }

    public int c() {
        return this.f8215e.d();
    }

    public void d(int i8, Bitmap bitmap) {
        this.f8215e.g(i8, bitmap);
    }

    public void e(boolean z4) {
        e eVar = this.f8215e;
        if (eVar != null) {
            if (z4) {
                eVar.h();
            }
            this.f8215e = null;
        }
    }

    void f(Rect rect) {
        float[] fArr = this.f8211a;
        int i8 = rect.left;
        int i9 = this.f8213c;
        fArr[0] = i8 / i9;
        int i10 = rect.bottom;
        int i11 = this.f8214d;
        fArr[1] = 1.0f - (i10 / i11);
        int i12 = rect.right;
        fArr[2] = i12 / i9;
        fArr[3] = 1.0f - (i10 / i11);
        fArr[4] = i8 / i9;
        int i13 = rect.top;
        fArr[5] = 1.0f - (i13 / i11);
        fArr[6] = i12 / i9;
        fArr[7] = 1.0f - (i13 / i11);
        this.f8212b.put(fArr);
        this.f8212b.position(0);
    }
}
