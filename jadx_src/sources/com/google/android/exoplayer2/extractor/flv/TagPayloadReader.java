package com.google.android.exoplayer2.extractor.flv;

import b6.z;
import com.google.android.exoplayer2.ParserException;
import n4.b0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class TagPayloadReader {

    /* renamed from: a  reason: collision with root package name */
    protected final b0 f9664a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class UnsupportedFormatException extends ParserException {
        public UnsupportedFormatException(String str) {
            super(str, null, false, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public TagPayloadReader(b0 b0Var) {
        this.f9664a = b0Var;
    }

    public final boolean a(z zVar, long j8) {
        return b(zVar) && c(zVar, j8);
    }

    protected abstract boolean b(z zVar);

    protected abstract boolean c(z zVar, long j8);
}
