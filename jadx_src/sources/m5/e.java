package m5;

import android.net.Uri;
import java.util.LinkedHashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class e {

    /* renamed from: a  reason: collision with root package name */
    private final LinkedHashMap<Uri, byte[]> f21850a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends LinkedHashMap<Uri, byte[]> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ int f21851a;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(e eVar, int i8, float f5, boolean z4, int i9) {
            super(i8, f5, z4);
            this.f21851a = i9;
        }

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<Uri, byte[]> entry) {
            return size() > this.f21851a;
        }
    }

    public e(int i8) {
        this.f21850a = new a(this, i8 + 1, 1.0f, false, i8);
    }

    public byte[] a(Uri uri) {
        if (uri == null) {
            return null;
        }
        return this.f21850a.get(uri);
    }

    public byte[] b(Uri uri, byte[] bArr) {
        return this.f21850a.put((Uri) b6.a.e(uri), (byte[]) b6.a.e(bArr));
    }

    public byte[] c(Uri uri) {
        return this.f21850a.remove(b6.a.e(uri));
    }
}
