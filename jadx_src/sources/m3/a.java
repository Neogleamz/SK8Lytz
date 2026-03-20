package m3;

import java.io.ByteArrayInputStream;
import java.io.File;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    public static final a f21831a = new a();

    private a() {
    }

    private final int a(byte[] bArr) {
        return new androidx.exifinterface.media.a(new ByteArrayInputStream(bArr)).r();
    }

    public final int b(File file) {
        p.e(file, "file");
        try {
            return new androidx.exifinterface.media.a(file.getAbsolutePath()).r();
        } catch (Exception unused) {
            return 0;
        }
    }

    public final int c(byte[] bArr) {
        p.e(bArr, "_bytes");
        try {
            return a(bArr);
        } catch (Exception unused) {
            return 0;
        }
    }
}
