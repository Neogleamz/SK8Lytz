package b0;

import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.media.ImageWriter;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.impl.utils.ExifData;
import androidx.camera.core.internal.utils.ImageUtil;
import androidx.camera.core.l1;
import androidx.camera.core.p1;
import androidx.concurrent.futures.c;
import java.nio.ByteBuffer;
import java.util.List;
import y.f0;
import y.v;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class m implements v {

    /* renamed from: k  reason: collision with root package name */
    private static final Rect f7930k = new Rect(0, 0, 0, 0);

    /* renamed from: a  reason: collision with root package name */
    private final int f7931a;

    /* renamed from: c  reason: collision with root package name */
    private int f7933c;

    /* renamed from: g  reason: collision with root package name */
    private ImageWriter f7937g;

    /* renamed from: i  reason: collision with root package name */
    c.a<Void> f7939i;

    /* renamed from: j  reason: collision with root package name */
    private com.google.common.util.concurrent.d<Void> f7940j;

    /* renamed from: b  reason: collision with root package name */
    private final Object f7932b = new Object();

    /* renamed from: d  reason: collision with root package name */
    private int f7934d = 0;

    /* renamed from: e  reason: collision with root package name */
    private boolean f7935e = false;

    /* renamed from: f  reason: collision with root package name */
    private int f7936f = 0;

    /* renamed from: h  reason: collision with root package name */
    private Rect f7938h = f7930k;

    public m(int i8, int i9) {
        this.f7933c = i8;
        this.f7931a = i9;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object f(c.a aVar) {
        synchronized (this.f7932b) {
            this.f7939i = aVar;
        }
        return "YuvToJpegProcessor-close";
    }

    @Override // y.v
    public void a(Surface surface, int i8) {
        androidx.core.util.h.k(i8 == 256, "YuvToJpegProcessor only supports JPEG output format.");
        synchronized (this.f7932b) {
            if (this.f7935e) {
                p1.k("YuvToJpegProcessor", "Cannot set output surface. Processor is closed.");
            } else if (this.f7937g != null) {
                throw new IllegalStateException("Output surface already set.");
            } else {
                this.f7937g = c0.a.d(surface, this.f7931a, i8);
            }
        }
    }

    @Override // y.v
    public com.google.common.util.concurrent.d<Void> b() {
        com.google.common.util.concurrent.d<Void> j8;
        synchronized (this.f7932b) {
            if (this.f7935e && this.f7936f == 0) {
                j8 = a0.f.h(null);
            } else {
                if (this.f7940j == null) {
                    this.f7940j = androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: b0.l
                        @Override // androidx.concurrent.futures.c.InterfaceC0024c
                        public final Object a(c.a aVar) {
                            Object f5;
                            f5 = m.this.f(aVar);
                            return f5;
                        }
                    });
                }
                j8 = a0.f.j(this.f7940j);
            }
        }
        return j8;
    }

    @Override // y.v
    public void c(f0 f0Var) {
        ImageWriter imageWriter;
        boolean z4;
        Rect rect;
        int i8;
        int i9;
        l1 l1Var;
        Image image;
        c.a<Void> aVar;
        c.a<Void> aVar2;
        ByteBuffer buffer;
        int position;
        c.a<Void> aVar3;
        List<Integer> b9 = f0Var.b();
        boolean z8 = false;
        androidx.core.util.h.b(b9.size() == 1, "Processing image bundle have single capture id, but found " + b9.size());
        com.google.common.util.concurrent.d<l1> a9 = f0Var.a(b9.get(0).intValue());
        androidx.core.util.h.a(a9.isDone());
        synchronized (this.f7932b) {
            imageWriter = this.f7937g;
            z4 = !this.f7935e;
            rect = this.f7938h;
            if (z4) {
                this.f7936f++;
            }
            i8 = this.f7933c;
            i9 = this.f7934d;
        }
        try {
            try {
                l1Var = a9.get();
                try {
                } catch (Exception e8) {
                    e = e8;
                    image = null;
                } catch (Throwable th) {
                    th = th;
                    image = null;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e9) {
            e = e9;
            l1Var = null;
            image = null;
        } catch (Throwable th3) {
            th = th3;
            l1Var = null;
            image = null;
        }
        if (!z4) {
            p1.k("YuvToJpegProcessor", "Image enqueued for processing on closed processor.");
            l1Var.close();
            synchronized (this.f7932b) {
                if (z4) {
                    try {
                        int i10 = this.f7936f;
                        this.f7936f = i10 - 1;
                        if (i10 == 0 && this.f7935e) {
                            z8 = true;
                        }
                    } finally {
                    }
                }
                aVar3 = this.f7939i;
            }
            if (z8) {
                imageWriter.close();
                p1.a("YuvToJpegProcessor", "Closed after completion of last image processed.");
                if (aVar3 != null) {
                    aVar3.c(null);
                    return;
                }
                return;
            }
            return;
        }
        image = imageWriter.dequeueInputImage();
        try {
            l1 l1Var2 = a9.get();
            try {
                androidx.core.util.h.k(l1Var2.getFormat() == 35, "Input image is not expected YUV_420_888 image format");
                YuvImage yuvImage = new YuvImage(ImageUtil.k(l1Var2), 17, l1Var2.getWidth(), l1Var2.getHeight(), null);
                buffer = image.getPlanes()[0].getBuffer();
                position = buffer.position();
                yuvImage.compressToJpeg(rect, i8, new androidx.camera.core.impl.utils.h(new b(buffer), ExifData.b(l1Var2, i9)));
                l1Var2.close();
            } catch (Exception e10) {
                e = e10;
                l1Var = l1Var2;
            } catch (Throwable th4) {
                th = th4;
                l1Var = l1Var2;
            }
        } catch (Exception e11) {
            e = e11;
        }
        try {
            buffer.limit(buffer.position());
            buffer.position(position);
            imageWriter.queueInputImage(image);
            synchronized (this.f7932b) {
                if (z4) {
                    try {
                        int i11 = this.f7936f;
                        this.f7936f = i11 - 1;
                        if (i11 == 0 && this.f7935e) {
                            z8 = true;
                        }
                    } finally {
                    }
                }
                aVar2 = this.f7939i;
            }
        } catch (Exception e12) {
            e = e12;
            l1Var = null;
            if (z4) {
                p1.d("YuvToJpegProcessor", "Failed to process YUV -> JPEG", e);
                image = imageWriter.dequeueInputImage();
                ByteBuffer buffer2 = image.getPlanes()[0].getBuffer();
                buffer2.rewind();
                buffer2.limit(0);
                imageWriter.queueInputImage(image);
            }
            synchronized (this.f7932b) {
                if (z4) {
                    try {
                        int i12 = this.f7936f;
                        this.f7936f = i12 - 1;
                        if (i12 == 0 && this.f7935e) {
                            z8 = true;
                        }
                    } finally {
                    }
                }
                aVar2 = this.f7939i;
            }
            if (image != null) {
                image.close();
            }
            if (l1Var != null) {
                l1Var.close();
            }
            if (z8) {
                imageWriter.close();
                p1.a("YuvToJpegProcessor", "Closed after completion of last image processed.");
                if (aVar2 == null) {
                    return;
                }
                aVar2.c(null);
            }
            return;
        } catch (Throwable th5) {
            th = th5;
            l1Var = null;
            synchronized (this.f7932b) {
                if (z4) {
                    try {
                        int i13 = this.f7936f;
                        this.f7936f = i13 - 1;
                        if (i13 == 0 && this.f7935e) {
                            z8 = true;
                        }
                    } finally {
                    }
                }
                aVar = this.f7939i;
            }
            if (image != null) {
                image.close();
            }
            if (l1Var != null) {
                l1Var.close();
            }
            if (z8) {
                imageWriter.close();
                p1.a("YuvToJpegProcessor", "Closed after completion of last image processed.");
                if (aVar != null) {
                    aVar.c(null);
                }
            }
            throw th;
        }
        if (z8) {
            imageWriter.close();
            p1.a("YuvToJpegProcessor", "Closed after completion of last image processed.");
            if (aVar2 == null) {
                return;
            }
            aVar2.c(null);
        }
    }

    @Override // y.v
    public void close() {
        c.a<Void> aVar;
        synchronized (this.f7932b) {
            if (this.f7935e) {
                return;
            }
            this.f7935e = true;
            if (this.f7936f != 0 || this.f7937g == null) {
                p1.a("YuvToJpegProcessor", "close() called while processing. Will close after completion.");
                aVar = null;
            } else {
                p1.a("YuvToJpegProcessor", "No processing in progress. Closing immediately.");
                this.f7937g.close();
                aVar = this.f7939i;
            }
            if (aVar != null) {
                aVar.c(null);
            }
        }
    }

    @Override // y.v
    public void d(Size size) {
        synchronized (this.f7932b) {
            this.f7938h = new Rect(0, 0, size.getWidth(), size.getHeight());
        }
    }

    public void g(int i8) {
        synchronized (this.f7932b) {
            this.f7933c = i8;
        }
    }

    public void h(int i8) {
        synchronized (this.f7932b) {
            this.f7934d = i8;
        }
    }
}
