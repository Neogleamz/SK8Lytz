package androidx.media;

import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AudioAttributesImplBaseParcelizer {
    public static AudioAttributesImplBase read(VersionedParcel versionedParcel) {
        AudioAttributesImplBase audioAttributesImplBase = new AudioAttributesImplBase();
        audioAttributesImplBase.f5960a = versionedParcel.v(audioAttributesImplBase.f5960a, 1);
        audioAttributesImplBase.f5961b = versionedParcel.v(audioAttributesImplBase.f5961b, 2);
        audioAttributesImplBase.f5962c = versionedParcel.v(audioAttributesImplBase.f5962c, 3);
        audioAttributesImplBase.f5963d = versionedParcel.v(audioAttributesImplBase.f5963d, 4);
        return audioAttributesImplBase;
    }

    public static void write(AudioAttributesImplBase audioAttributesImplBase, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.Y(audioAttributesImplBase.f5960a, 1);
        versionedParcel.Y(audioAttributesImplBase.f5961b, 2);
        versionedParcel.Y(audioAttributesImplBase.f5962c, 3);
        versionedParcel.Y(audioAttributesImplBase.f5963d, 4);
    }
}
