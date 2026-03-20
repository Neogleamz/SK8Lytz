package p5;

import android.os.Bundle;
import android.os.Parcel;
import b6.a;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {
    public ImmutableList<b> a(byte[] bArr) {
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(bArr, 0, bArr.length);
        obtain.setDataPosition(0);
        Bundle readBundle = obtain.readBundle(Bundle.class.getClassLoader());
        obtain.recycle();
        return b6.c.b(b.X, (ArrayList) a.e(readBundle.getParcelableArrayList("c")));
    }
}
