package n5;

import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.upstream.d;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c implements e {

    /* renamed from: a  reason: collision with root package name */
    private final e f22157a;

    /* renamed from: b  reason: collision with root package name */
    private final List<StreamKey> f22158b;

    public c(e eVar, List<StreamKey> list) {
        this.f22157a = eVar;
        this.f22158b = list;
    }

    @Override // n5.e
    public d.a<d> a(com.google.android.exoplayer2.source.hls.playlist.e eVar, com.google.android.exoplayer2.source.hls.playlist.d dVar) {
        return new g5.c(this.f22157a.a(eVar, dVar), this.f22158b);
    }

    @Override // n5.e
    public d.a<d> b() {
        return new g5.c(this.f22157a.b(), this.f22158b);
    }
}
