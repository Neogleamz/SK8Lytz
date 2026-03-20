package c5;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: a  reason: collision with root package name */
    private final ByteArrayOutputStream f8323a;

    /* renamed from: b  reason: collision with root package name */
    private final DataOutputStream f8324b;

    public b() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(RecognitionOptions.UPC_A);
        this.f8323a = byteArrayOutputStream;
        this.f8324b = new DataOutputStream(byteArrayOutputStream);
    }

    private static void b(DataOutputStream dataOutputStream, String str) {
        dataOutputStream.writeBytes(str);
        dataOutputStream.writeByte(0);
    }

    public byte[] a(EventMessage eventMessage) {
        this.f8323a.reset();
        try {
            b(this.f8324b, eventMessage.f10061a);
            String str = eventMessage.f10062b;
            if (str == null) {
                str = BuildConfig.FLAVOR;
            }
            b(this.f8324b, str);
            this.f8324b.writeLong(eventMessage.f10063c);
            this.f8324b.writeLong(eventMessage.f10064d);
            this.f8324b.write(eventMessage.f10065e);
            this.f8324b.flush();
            return this.f8323a.toByteArray();
        } catch (IOException e8) {
            throw new RuntimeException(e8);
        }
    }
}
