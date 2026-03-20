package cn.bingoogolapple.qrcode.core;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class d extends AsyncTask<Void, Void, i> {

    /* renamed from: g  reason: collision with root package name */
    private static long f8557g;

    /* renamed from: a  reason: collision with root package name */
    private Camera f8558a;

    /* renamed from: b  reason: collision with root package name */
    private byte[] f8559b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f8560c;

    /* renamed from: d  reason: collision with root package name */
    private String f8561d;

    /* renamed from: e  reason: collision with root package name */
    private Bitmap f8562e;

    /* renamed from: f  reason: collision with root package name */
    private WeakReference<QRCodeView> f8563f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(Camera camera, byte[] bArr, QRCodeView qRCodeView, boolean z4) {
        this.f8558a = camera;
        this.f8559b = bArr;
        this.f8563f = new WeakReference<>(qRCodeView);
        this.f8560c = z4;
    }

    private i e(QRCodeView qRCodeView) {
        int i8;
        Exception e8;
        int i9;
        byte[] bArr = this.f8559b;
        if (bArr == null) {
            return null;
        }
        try {
            Camera.Size previewSize = this.f8558a.getParameters().getPreviewSize();
            i8 = previewSize.width;
            try {
                i9 = previewSize.height;
            } catch (Exception e9) {
                i9 = 0;
                e8 = e9;
            }
        } catch (Exception e10) {
            i8 = 0;
            e8 = e10;
            i9 = 0;
        }
        try {
            if (this.f8560c) {
                bArr = new byte[this.f8559b.length];
                for (int i10 = 0; i10 < i9; i10++) {
                    for (int i11 = 0; i11 < i8; i11++) {
                        bArr[(((i11 * i9) + i9) - i10) - 1] = this.f8559b[(i10 * i8) + i11];
                    }
                }
                i8 = i9;
                i9 = i8;
            }
            return qRCodeView.p(bArr, i8, i9, false);
        } catch (Exception e11) {
            e8 = e11;
            e8.printStackTrace();
            if (i8 != 0 && i9 != 0) {
                try {
                    a.e("识别失败重试");
                    return qRCodeView.p(bArr, i8, i9, true);
                } catch (Exception e12) {
                    e12.printStackTrace();
                    return null;
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        if (getStatus() != AsyncTask.Status.FINISHED) {
            cancel(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    /* renamed from: b */
    public i doInBackground(Void... voidArr) {
        QRCodeView qRCodeView = this.f8563f.get();
        if (qRCodeView == null) {
            return null;
        }
        String str = this.f8561d;
        if (str != null) {
            return qRCodeView.o(a.i(str));
        }
        Bitmap bitmap = this.f8562e;
        if (bitmap != null) {
            i o5 = qRCodeView.o(bitmap);
            this.f8562e = null;
            return o5;
        }
        if (a.l()) {
            a.e("两次任务执行的时间间隔：" + (System.currentTimeMillis() - f8557g));
            f8557g = System.currentTimeMillis();
        }
        long currentTimeMillis = System.currentTimeMillis();
        i e8 = e(qRCodeView);
        if (a.l()) {
            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
            if (e8 == null || TextUtils.isEmpty(e8.f8631a)) {
                a.h("识别失败时间为：" + currentTimeMillis2);
            } else {
                a.e("识别成功时间为：" + currentTimeMillis2);
            }
        }
        return e8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    /* renamed from: c */
    public void onPostExecute(i iVar) {
        QRCodeView qRCodeView = this.f8563f.get();
        if (qRCodeView == null) {
            return;
        }
        if (this.f8561d == null && this.f8562e == null) {
            qRCodeView.l(iVar);
            return;
        }
        this.f8562e = null;
        qRCodeView.k(iVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d d() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        return this;
    }

    @Override // android.os.AsyncTask
    protected void onCancelled() {
        super.onCancelled();
        this.f8563f.clear();
        this.f8562e = null;
        this.f8559b = null;
    }
}
