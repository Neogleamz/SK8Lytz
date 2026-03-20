package p5;

import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class g extends l4.h<k, l, SubtitleDecoderException> implements i {

    /* renamed from: n  reason: collision with root package name */
    private final String f22420n;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends l {
        a() {
        }

        @Override // l4.f
        public void y() {
            g.this.r(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public g(String str) {
        super(new k[2], new l[2]);
        this.f22420n = str;
        u(RecognitionOptions.UPC_E);
    }

    protected abstract h A(byte[] bArr, int i8, boolean z4);

    @Override // p5.i
    public void a(long j8) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // l4.h
    /* renamed from: w */
    public final k g() {
        return new k();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // l4.h
    /* renamed from: x */
    public final l h() {
        return new a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // l4.h
    /* renamed from: y */
    public final SubtitleDecoderException i(Throwable th) {
        return new SubtitleDecoderException("Unexpected decode error", th);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // l4.h
    /* renamed from: z */
    public final SubtitleDecoderException j(k kVar, l lVar, boolean z4) {
        try {
            ByteBuffer byteBuffer = (ByteBuffer) b6.a.e(kVar.f9512c);
            lVar.z(kVar.f9514e, A(byteBuffer.array(), byteBuffer.limit(), z4), kVar.f22423j);
            lVar.o(Integer.MIN_VALUE);
            return null;
        } catch (SubtitleDecoderException e8) {
            return e8;
        }
    }
}
