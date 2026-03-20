package com.google.android.exoplayer2.video;

import android.view.Surface;
import com.google.android.exoplayer2.mediacodec.MediaCodecDecoderException;
import com.google.android.exoplayer2.mediacodec.k;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MediaCodecVideoDecoderException extends MediaCodecDecoderException {

    /* renamed from: c  reason: collision with root package name */
    public final int f11061c;

    /* renamed from: d  reason: collision with root package name */
    public final boolean f11062d;

    public MediaCodecVideoDecoderException(Throwable th, k kVar, Surface surface) {
        super(th, kVar);
        this.f11061c = System.identityHashCode(surface);
        this.f11062d = surface == null || surface.isValid();
    }
}
