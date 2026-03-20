package m4;

import b6.l0;
import java.util.UUID;
import l4.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l implements b {

    /* renamed from: d  reason: collision with root package name */
    public static final boolean f21834d;

    /* renamed from: a  reason: collision with root package name */
    public final UUID f21835a;

    /* renamed from: b  reason: collision with root package name */
    public final byte[] f21836b;

    /* renamed from: c  reason: collision with root package name */
    public final boolean f21837c;

    static {
        boolean z4;
        if ("Amazon".equals(l0.f8065c)) {
            String str = l0.f8066d;
            if ("AFTM".equals(str) || "AFTB".equals(str)) {
                z4 = true;
                f21834d = z4;
            }
        }
        z4 = false;
        f21834d = z4;
    }

    public l(UUID uuid, byte[] bArr, boolean z4) {
        this.f21835a = uuid;
        this.f21836b = bArr;
        this.f21837c = z4;
    }
}
