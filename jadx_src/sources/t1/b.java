package t1;

import android.database.Cursor;
import android.os.CancellationSignal;
import android.util.Pair;
import java.io.Closeable;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface b extends Closeable {
    boolean A1();

    Cursor C0(e eVar, CancellationSignal cancellationSignal);

    List<Pair<String, String>> F();

    void H(String str);

    boolean I1();

    void J0();

    f O(String str);

    Cursor X(e eVar);

    void i0();

    boolean isOpen();

    void l0();

    Cursor w0(String str);

    void x();

    String y1();
}
