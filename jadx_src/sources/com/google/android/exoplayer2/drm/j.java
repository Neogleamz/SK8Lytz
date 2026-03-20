package com.google.android.exoplayer2.drm;

import android.media.DeniedByServerException;
import android.media.MediaDrm;
import android.media.MediaDrmResetException;
import android.media.NotProvisionedException;
import b6.l0;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        public static boolean a(Throwable th) {
            return th instanceof DeniedByServerException;
        }

        public static boolean b(Throwable th) {
            return th instanceof NotProvisionedException;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {
        public static boolean a(Throwable th) {
            return th instanceof MediaDrm.MediaDrmStateException;
        }

        public static int b(Throwable th) {
            return l0.U(l0.V(((MediaDrm.MediaDrmStateException) th).getDiagnosticInfo()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {
        public static boolean a(Throwable th) {
            return th instanceof MediaDrmResetException;
        }
    }

    public static int a(Exception exc, int i8) {
        int i9 = l0.f8063a;
        if (i9 < 21 || !b.a(exc)) {
            if (i9 < 23 || !c.a(exc)) {
                if (i9 < 18 || !a.b(exc)) {
                    if (i9 < 18 || !a.a(exc)) {
                        if (exc instanceof UnsupportedDrmException) {
                            return 6001;
                        }
                        if (exc instanceof DefaultDrmSessionManager.MissingSchemeDataException) {
                            return 6003;
                        }
                        if (exc instanceof KeysExpiredException) {
                            return 6008;
                        }
                        if (i8 == 1) {
                            return 6006;
                        }
                        if (i8 == 2) {
                            return 6004;
                        }
                        if (i8 == 3) {
                            return 6002;
                        }
                        throw new IllegalArgumentException();
                    }
                    return 6007;
                }
                return 6002;
            }
            return 6006;
        }
        return b.b(exc);
    }
}
