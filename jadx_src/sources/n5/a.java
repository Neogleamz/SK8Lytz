package n5;

import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser;
import com.google.android.exoplayer2.upstream.d;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements e {
    @Override // n5.e
    public d.a<d> a(com.google.android.exoplayer2.source.hls.playlist.e eVar, com.google.android.exoplayer2.source.hls.playlist.d dVar) {
        return new HlsPlaylistParser(eVar, dVar);
    }

    @Override // n5.e
    public d.a<d> b() {
        return new HlsPlaylistParser();
    }
}
