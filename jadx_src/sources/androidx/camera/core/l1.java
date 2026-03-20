package androidx.camera.core;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.media.Image;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface l1 extends AutoCloseable {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        int a();

        ByteBuffer b();

        int c();
    }

    @SuppressLint({"ArrayReturn"})
    a[] A();

    Image B1();

    void Y0(Rect rect);

    @Override // java.lang.AutoCloseable
    void close();

    i1 e1();

    int getFormat();

    int getHeight();

    int getWidth();
}
