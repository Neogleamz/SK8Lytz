package androidx.camera.camera2.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d implements y.g {

    /* renamed from: a  reason: collision with root package name */
    private final boolean f1768a;

    /* renamed from: b  reason: collision with root package name */
    private final int f1769b;

    /* renamed from: c  reason: collision with root package name */
    private final v.c f1770c;

    public d(String str, s.y yVar) {
        boolean z4;
        int i8;
        try {
            i8 = Integer.parseInt(str);
            z4 = true;
        } catch (NumberFormatException unused) {
            androidx.camera.core.p1.k("Camera2CamcorderProfileProvider", "Camera id is not an integer: " + str + ", unable to create CamcorderProfileProvider");
            z4 = false;
            i8 = -1;
        }
        this.f1768a = z4;
        this.f1769b = i8;
        this.f1770c = new v.c((u.e) u.g.a(str, yVar).b(u.e.class));
    }
}
