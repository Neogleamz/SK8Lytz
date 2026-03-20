package androidx.media;

import android.media.AudioAttributes;
import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AudioAttributesImplApi26Parcelizer {
    public static AudioAttributesImplApi26 read(VersionedParcel versionedParcel) {
        AudioAttributesImplApi26 audioAttributesImplApi26 = new AudioAttributesImplApi26();
        audioAttributesImplApi26.f5957a = (AudioAttributes) versionedParcel.A(audioAttributesImplApi26.f5957a, 1);
        audioAttributesImplApi26.f5958b = versionedParcel.v(audioAttributesImplApi26.f5958b, 2);
        return audioAttributesImplApi26;
    }

    public static void write(AudioAttributesImplApi26 audioAttributesImplApi26, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.d0(audioAttributesImplApi26.f5957a, 1);
        versionedParcel.Y(audioAttributesImplApi26.f5958b, 2);
    }
}
