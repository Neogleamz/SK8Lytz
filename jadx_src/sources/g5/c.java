package g5;

import android.net.Uri;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.upstream.d;
import g5.b;
import java.io.InputStream;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c<T extends b<T>> implements d.a<T> {

    /* renamed from: a  reason: collision with root package name */
    private final d.a<? extends T> f20196a;

    /* renamed from: b  reason: collision with root package name */
    private final List<StreamKey> f20197b;

    public c(d.a<? extends T> aVar, List<StreamKey> list) {
        this.f20196a = aVar;
        this.f20197b = list;
    }

    @Override // com.google.android.exoplayer2.upstream.d.a
    /* renamed from: b */
    public T a(Uri uri, InputStream inputStream) {
        T a9 = this.f20196a.a(uri, inputStream);
        List<StreamKey> list = this.f20197b;
        return (list == null || list.isEmpty()) ? a9 : (T) a9.a(this.f20197b);
    }
}
