package androidx.camera.core;

import android.media.Image;
import android.media.ImageReader;
import android.view.Surface;
import java.util.concurrent.Executor;
import y.g0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d implements y.g0 {

    /* renamed from: a  reason: collision with root package name */
    private final ImageReader f2290a;

    /* renamed from: b  reason: collision with root package name */
    private final Object f2291b = new Object();

    /* renamed from: c  reason: collision with root package name */
    private boolean f2292c = true;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(ImageReader imageReader) {
        this.f2290a = imageReader;
    }

    private boolean h(RuntimeException runtimeException) {
        return "ImageReaderContext is not initialized".equals(runtimeException.getMessage());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void i(g0.a aVar) {
        aVar.a(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void j(Executor executor, final g0.a aVar, ImageReader imageReader) {
        synchronized (this.f2291b) {
            if (!this.f2292c) {
                executor.execute(new Runnable() { // from class: androidx.camera.core.c
                    @Override // java.lang.Runnable
                    public final void run() {
                        d.this.i(aVar);
                    }
                });
            }
        }
    }

    @Override // y.g0
    public void a(final g0.a aVar, final Executor executor) {
        synchronized (this.f2291b) {
            this.f2292c = false;
            this.f2290a.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() { // from class: androidx.camera.core.b
                @Override // android.media.ImageReader.OnImageAvailableListener
                public final void onImageAvailable(ImageReader imageReader) {
                    d.this.j(executor, aVar, imageReader);
                }
            }, androidx.camera.core.impl.utils.k.a());
        }
    }

    @Override // y.g0
    public l1 acquireLatestImage() {
        Image image;
        synchronized (this.f2291b) {
            try {
                image = this.f2290a.acquireLatestImage();
            } catch (RuntimeException e8) {
                if (!h(e8)) {
                    throw e8;
                }
                image = null;
            }
            if (image == null) {
                return null;
            }
            return new a(image);
        }
    }

    @Override // y.g0
    public int c() {
        int imageFormat;
        synchronized (this.f2291b) {
            imageFormat = this.f2290a.getImageFormat();
        }
        return imageFormat;
    }

    @Override // y.g0
    public void close() {
        synchronized (this.f2291b) {
            this.f2290a.close();
        }
    }

    @Override // y.g0
    public void d() {
        synchronized (this.f2291b) {
            this.f2292c = true;
            this.f2290a.setOnImageAvailableListener(null, null);
        }
    }

    @Override // y.g0
    public int e() {
        int maxImages;
        synchronized (this.f2291b) {
            maxImages = this.f2290a.getMaxImages();
        }
        return maxImages;
    }

    @Override // y.g0
    public l1 f() {
        Image image;
        synchronized (this.f2291b) {
            try {
                image = this.f2290a.acquireNextImage();
            } catch (RuntimeException e8) {
                if (!h(e8)) {
                    throw e8;
                }
                image = null;
            }
            if (image == null) {
                return null;
            }
            return new a(image);
        }
    }

    @Override // y.g0
    public int getHeight() {
        int height;
        synchronized (this.f2291b) {
            height = this.f2290a.getHeight();
        }
        return height;
    }

    @Override // y.g0
    public Surface getSurface() {
        Surface surface;
        synchronized (this.f2291b) {
            surface = this.f2290a.getSurface();
        }
        return surface;
    }

    @Override // y.g0
    public int getWidth() {
        int width;
        synchronized (this.f2291b) {
            width = this.f2290a.getWidth();
        }
        return width;
    }
}
