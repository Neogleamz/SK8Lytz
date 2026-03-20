package cn.bingoogolapple.qrcode.zxing;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.core.i;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.e;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ZXingView extends QRCodeView {

    /* renamed from: w  reason: collision with root package name */
    private e f8632w;

    /* renamed from: x  reason: collision with root package name */
    private Map<DecodeHintType, Object> f8633x;

    public ZXingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ZXingView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
    }

    private boolean E(BarcodeFormat barcodeFormat) {
        return i() && barcodeFormat == BarcodeFormat.m;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.bingoogolapple.qrcode.core.QRCodeView
    public i o(Bitmap bitmap) {
        return new i(a.a(bitmap));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0078 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0079  */
    @Override // cn.bingoogolapple.qrcode.core.QRCodeView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public cn.bingoogolapple.qrcode.core.i p(byte[] r15, int r16, int r17, boolean r18) {
        /*
            Method dump skipped, instructions count: 225
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.bingoogolapple.qrcode.zxing.ZXingView.p(byte[], int, int, boolean):cn.bingoogolapple.qrcode.core.i");
    }

    @Override // cn.bingoogolapple.qrcode.core.QRCodeView
    protected void r() {
        e eVar = new e();
        this.f8632w = eVar;
        BarcodeType barcodeType = this.f8530k;
        eVar.d(barcodeType == BarcodeType.ONE_DIMENSION ? a.f8635b : barcodeType == BarcodeType.TWO_DIMENSION ? a.f8636c : barcodeType == BarcodeType.ONLY_QR_CODE ? a.f8637d : barcodeType == BarcodeType.ONLY_CODE_128 ? a.f8638e : barcodeType == BarcodeType.ONLY_EAN_13 ? a.f8639f : barcodeType == BarcodeType.HIGH_FREQUENCY ? a.f8640g : barcodeType == BarcodeType.CUSTOM ? this.f8633x : a.f8634a);
    }
}
