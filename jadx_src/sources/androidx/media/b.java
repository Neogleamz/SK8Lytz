package androidx.media;

import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a {
        static int a(AudioManager audioManager, AudioFocusRequest audioFocusRequest) {
            return audioManager.abandonAudioFocusRequest(audioFocusRequest);
        }

        static int b(AudioManager audioManager, AudioFocusRequest audioFocusRequest) {
            return audioManager.requestAudioFocus(audioFocusRequest);
        }
    }

    public static int a(AudioManager audioManager, androidx.media.a aVar) {
        if (audioManager != null) {
            if (aVar != null) {
                return Build.VERSION.SDK_INT >= 26 ? a.a(audioManager, aVar.c()) : audioManager.abandonAudioFocus(aVar.e());
            }
            throw new IllegalArgumentException("AudioFocusRequestCompat must not be null");
        }
        throw new IllegalArgumentException("AudioManager must not be null");
    }

    public static int b(AudioManager audioManager, androidx.media.a aVar) {
        if (audioManager != null) {
            if (aVar != null) {
                return Build.VERSION.SDK_INT >= 26 ? a.b(audioManager, aVar.c()) : audioManager.requestAudioFocus(aVar.e(), aVar.b().a(), aVar.d());
            }
            throw new IllegalArgumentException("AudioFocusRequestCompat must not be null");
        }
        throw new IllegalArgumentException("AudioManager must not be null");
    }
}
