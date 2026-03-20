package com.google.android.exoplayer2.video.spherical;

import android.opengl.Matrix;
import b6.g0;
import com.google.android.exoplayer2.util.GlUtil;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a {

    /* renamed from: a  reason: collision with root package name */
    private final float[] f11111a = new float[16];

    /* renamed from: b  reason: collision with root package name */
    private final float[] f11112b = new float[16];

    /* renamed from: c  reason: collision with root package name */
    private final g0<float[]> f11113c = new g0<>();

    /* renamed from: d  reason: collision with root package name */
    private boolean f11114d;

    public static void a(float[] fArr, float[] fArr2) {
        GlUtil.j(fArr);
        float sqrt = (float) Math.sqrt((fArr2[10] * fArr2[10]) + (fArr2[8] * fArr2[8]));
        fArr[0] = fArr2[10] / sqrt;
        fArr[2] = fArr2[8] / sqrt;
        fArr[8] = (-fArr2[8]) / sqrt;
        fArr[10] = fArr2[10] / sqrt;
    }

    private static void b(float[] fArr, float[] fArr2) {
        float f5 = fArr2[0];
        float f8 = -fArr2[1];
        float f9 = -fArr2[2];
        float length = Matrix.length(f5, f8, f9);
        if (length != 0.0f) {
            Matrix.setRotateM(fArr, 0, (float) Math.toDegrees(length), f5 / length, f8 / length, f9 / length);
        } else {
            GlUtil.j(fArr);
        }
    }

    public boolean c(float[] fArr, long j8) {
        float[] j9 = this.f11113c.j(j8);
        if (j9 == null) {
            return false;
        }
        b(this.f11112b, j9);
        if (!this.f11114d) {
            a(this.f11111a, this.f11112b);
            this.f11114d = true;
        }
        Matrix.multiplyMM(fArr, 0, this.f11111a, 0, this.f11112b, 0);
        return true;
    }

    public void d() {
        this.f11113c.c();
        this.f11114d = false;
    }

    public void e(long j8, float[] fArr) {
        this.f11113c.a(j8, fArr);
    }
}
