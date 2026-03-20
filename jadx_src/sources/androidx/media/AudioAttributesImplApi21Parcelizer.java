package androidx.media;

import android.media.AudioAttributes;
import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AudioAttributesImplApi21Parcelizer {
    public static AudioAttributesImplApi21 read(VersionedParcel versionedParcel) {
        AudioAttributesImplApi21 audioAttributesImplApi21 = new AudioAttributesImplApi21();
        audioAttributesImplApi21.f5957a = (AudioAttributes) versionedParcel.A(audioAttributesImplApi21.f5957a, 1);
        audioAttributesImplApi21.f5958b = versionedParcel.v(audioAttributesImplApi21.f5958b, 2);
        return audioAttributesImplApi21;
    }

    public static void write(AudioAttributesImplApi21 audioAttributesImplApi21, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.d0(audioAttributesImplApi21.f5957a, 1);
        versionedParcel.Y(audioAttributesImplApi21.f5958b, 2);
    }
}
