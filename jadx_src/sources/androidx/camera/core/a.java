package androidx.camera.core;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.Image;
import androidx.camera.core.l1;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a implements l1 {

    /* renamed from: a  reason: collision with root package name */
    private final Image f2224a;

    /* renamed from: b  reason: collision with root package name */
    private final C0016a[] f2225b;

    /* renamed from: c  reason: collision with root package name */
    private final i1 f2226c;

    /* renamed from: androidx.camera.core.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class C0016a implements l1.a {

        /* renamed from: a  reason: collision with root package name */
        private final Image.Plane f2227a;

        C0016a(Image.Plane plane) {
            this.f2227a = plane;
        }

        @Override // androidx.camera.core.l1.a
        public int a() {
            return this.f2227a.getRowStride();
        }

        @Override // androidx.camera.core.l1.a
        public ByteBuffer b() {
            return this.f2227a.getBuffer();
        }

        @Override // androidx.camera.core.l1.a
        public int c() {
            return this.f2227a.getPixelStride();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(Image image) {
        this.f2224a = image;
        Image.Plane[] planes = image.getPlanes();
        if (planes != null) {
            this.f2225b = new C0016a[planes.length];
            for (int i8 = 0; i8 < planes.length; i8++) {
                this.f2225b[i8] = new C0016a(planes[i8]);
            }
        } else {
            this.f2225b = new C0016a[0];
        }
        this.f2226c = o1.f(y.a1.a(), image.getTimestamp(), 0, new Matrix());
    }

    @Override // androidx.camera.core.l1
    public l1.a[] A() {
        return this.f2225b;
    }

    @Override // androidx.camera.core.l1
    public Image B1() {
        return this.f2224a;
    }

    @Override // androidx.camera.core.l1
    public void Y0(Rect rect) {
        this.f2224a.setCropRect(rect);
    }

    @Override // androidx.camera.core.l1, java.lang.AutoCloseable
    public void close() {
        this.f2224a.close();
    }

    @Override // androidx.camera.core.l1
    public i1 e1() {
        return this.f2226c;
    }

    @Override // androidx.camera.core.l1
    public int getFormat() {
        return this.f2224a.getFormat();
    }

    @Override // androidx.camera.core.l1
    public int getHeight() {
        return this.f2224a.getHeight();
    }

    @Override // androidx.camera.core.l1
    public int getWidth() {
        return this.f2224a.getWidth();
    }
}
