package androidx.camera.core.impl;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class CameraCaptureFailure {

    /* renamed from: a  reason: collision with root package name */
    private final Reason f2425a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum Reason {
        ERROR
    }

    public CameraCaptureFailure(Reason reason) {
        this.f2425a = reason;
    }

    public Reason a() {
        return this.f2425a;
    }
}
