package b5;

import a5.c;
import a5.e;
import b6.y;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.dvbsi.AppInfoTable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends e {
    private static Metadata c(y yVar) {
        yVar.r(12);
        int d8 = (yVar.d() + yVar.h(12)) - 4;
        yVar.r(44);
        yVar.s(yVar.h(12));
        yVar.r(16);
        ArrayList arrayList = new ArrayList();
        while (true) {
            String str = null;
            if (yVar.d() >= d8) {
                break;
            }
            yVar.r(48);
            int h8 = yVar.h(8);
            yVar.r(4);
            int d9 = yVar.d() + yVar.h(12);
            String str2 = null;
            while (yVar.d() < d9) {
                int h9 = yVar.h(8);
                int h10 = yVar.h(8);
                int d10 = yVar.d() + h10;
                if (h9 == 2) {
                    int h11 = yVar.h(16);
                    yVar.r(8);
                    if (h11 != 3) {
                    }
                    while (yVar.d() < d10) {
                        str = yVar.l(yVar.h(8), com.google.common.base.e.f18815a);
                        int h12 = yVar.h(8);
                        for (int i8 = 0; i8 < h12; i8++) {
                            yVar.s(yVar.h(8));
                        }
                    }
                } else if (h9 == 21) {
                    str2 = yVar.l(h10, com.google.common.base.e.f18815a);
                }
                yVar.p(d10 * 8);
            }
            yVar.p(d9 * 8);
            if (str != null && str2 != null) {
                arrayList.add(new AppInfoTable(h8, str + str2));
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return new Metadata(arrayList);
    }

    @Override // a5.e
    protected Metadata b(c cVar, ByteBuffer byteBuffer) {
        if (byteBuffer.get() == 116) {
            return c(new y(byteBuffer.array(), byteBuffer.limit()));
        }
        return null;
    }
}
