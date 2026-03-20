package androidx.camera.core;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ImageCaptureException extends Exception {

    /* renamed from: a  reason: collision with root package name */
    private final int f2210a;

    public ImageCaptureException(int i8, String str, Throwable th) {
        super(str, th);
        this.f2210a = i8;
    }

    public int a() {
        return this.f2210a;
    }
}
