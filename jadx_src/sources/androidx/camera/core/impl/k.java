package androidx.camera.core.impl;

import androidx.camera.core.impl.Config;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface k extends q {

    /* renamed from: f  reason: collision with root package name */
    public static final Config.a<Integer> f2575f = Config.a.a("camerax.core.imageInput.inputFormat", Integer.TYPE);

    default int m() {
        return ((Integer) a(f2575f)).intValue();
    }
}
