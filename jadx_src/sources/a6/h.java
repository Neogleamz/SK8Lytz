package a6;

import android.net.Uri;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface h extends f {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        h a();
    }

    void close();

    Uri v();

    void w(y yVar);

    long x(com.google.android.exoplayer2.upstream.a aVar);

    default Map<String, List<String>> y() {
        return Collections.emptyMap();
    }
}
