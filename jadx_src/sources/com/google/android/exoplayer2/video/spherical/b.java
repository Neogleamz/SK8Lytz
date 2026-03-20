package com.google.android.exoplayer2.video.spherical;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.view.Display;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b implements SensorEventListener {

    /* renamed from: a  reason: collision with root package name */
    private final float[] f11115a = new float[16];

    /* renamed from: b  reason: collision with root package name */
    private final float[] f11116b = new float[16];

    /* renamed from: c  reason: collision with root package name */
    private final float[] f11117c = new float[16];

    /* renamed from: d  reason: collision with root package name */
    private final float[] f11118d = new float[3];

    /* renamed from: e  reason: collision with root package name */
    private final Display f11119e;

    /* renamed from: f  reason: collision with root package name */
    private final a[] f11120f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f11121g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(float[] fArr, float f5);
    }

    public b(Display display, a... aVarArr) {
        this.f11119e = display;
        this.f11120f = aVarArr;
    }

    private float a(float[] fArr) {
        SensorManager.remapCoordinateSystem(fArr, 1, 131, this.f11116b);
        SensorManager.getOrientation(this.f11116b, this.f11118d);
        return this.f11118d[2];
    }

    private void b(float[] fArr, float f5) {
        for (a aVar : this.f11120f) {
            aVar.a(fArr, f5);
        }
    }

    private void c(float[] fArr) {
        if (!this.f11121g) {
            com.google.android.exoplayer2.video.spherical.a.a(this.f11117c, fArr);
            this.f11121g = true;
        }
        float[] fArr2 = this.f11116b;
        System.arraycopy(fArr, 0, fArr2, 0, fArr2.length);
        Matrix.multiplyMM(fArr, 0, this.f11116b, 0, this.f11117c, 0);
    }

    private void d(float[] fArr, int i8) {
        if (i8 != 0) {
            int i9 = 130;
            int i10 = 129;
            if (i8 == 1) {
                i9 = 2;
            } else if (i8 == 2) {
                i10 = 130;
                i9 = 129;
            } else if (i8 != 3) {
                throw new IllegalStateException();
            } else {
                i10 = 1;
            }
            float[] fArr2 = this.f11116b;
            System.arraycopy(fArr, 0, fArr2, 0, fArr2.length);
            SensorManager.remapCoordinateSystem(this.f11116b, i9, i10, fArr);
        }
    }

    private static void e(float[] fArr) {
        Matrix.rotateM(fArr, 0, 90.0f, 1.0f, 0.0f, 0.0f);
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i8) {
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        SensorManager.getRotationMatrixFromVector(this.f11115a, sensorEvent.values);
        d(this.f11115a, this.f11119e.getRotation());
        float a9 = a(this.f11115a);
        e(this.f11115a);
        c(this.f11115a);
        b(this.f11115a, a9);
    }
}
