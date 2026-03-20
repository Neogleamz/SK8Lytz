package r5;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.SparseArray;
import b6.l0;
import b6.p;
import b6.y;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p5.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b {

    /* renamed from: h  reason: collision with root package name */
    private static final byte[] f22671h = {0, 7, 8, 15};

    /* renamed from: i  reason: collision with root package name */
    private static final byte[] f22672i = {0, 119, -120, -1};

    /* renamed from: j  reason: collision with root package name */
    private static final byte[] f22673j = {0, 17, 34, 51, 68, 85, 102, 119, -120, -103, -86, -69, -52, -35, -18, -1};

    /* renamed from: a  reason: collision with root package name */
    private final Paint f22674a;

    /* renamed from: b  reason: collision with root package name */
    private final Paint f22675b;

    /* renamed from: c  reason: collision with root package name */
    private final Canvas f22676c;

    /* renamed from: d  reason: collision with root package name */
    private final C0204b f22677d;

    /* renamed from: e  reason: collision with root package name */
    private final a f22678e;

    /* renamed from: f  reason: collision with root package name */
    private final h f22679f;

    /* renamed from: g  reason: collision with root package name */
    private Bitmap f22680g;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final int f22681a;

        /* renamed from: b  reason: collision with root package name */
        public final int[] f22682b;

        /* renamed from: c  reason: collision with root package name */
        public final int[] f22683c;

        /* renamed from: d  reason: collision with root package name */
        public final int[] f22684d;

        public a(int i8, int[] iArr, int[] iArr2, int[] iArr3) {
            this.f22681a = i8;
            this.f22682b = iArr;
            this.f22683c = iArr2;
            this.f22684d = iArr3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: r5.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0204b {

        /* renamed from: a  reason: collision with root package name */
        public final int f22685a;

        /* renamed from: b  reason: collision with root package name */
        public final int f22686b;

        /* renamed from: c  reason: collision with root package name */
        public final int f22687c;

        /* renamed from: d  reason: collision with root package name */
        public final int f22688d;

        /* renamed from: e  reason: collision with root package name */
        public final int f22689e;

        /* renamed from: f  reason: collision with root package name */
        public final int f22690f;

        public C0204b(int i8, int i9, int i10, int i11, int i12, int i13) {
            this.f22685a = i8;
            this.f22686b = i9;
            this.f22687c = i10;
            this.f22688d = i11;
            this.f22689e = i12;
            this.f22690f = i13;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: a  reason: collision with root package name */
        public final int f22691a;

        /* renamed from: b  reason: collision with root package name */
        public final boolean f22692b;

        /* renamed from: c  reason: collision with root package name */
        public final byte[] f22693c;

        /* renamed from: d  reason: collision with root package name */
        public final byte[] f22694d;

        public c(int i8, boolean z4, byte[] bArr, byte[] bArr2) {
            this.f22691a = i8;
            this.f22692b = z4;
            this.f22693c = bArr;
            this.f22694d = bArr2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        public final int f22695a;

        /* renamed from: b  reason: collision with root package name */
        public final int f22696b;

        /* renamed from: c  reason: collision with root package name */
        public final int f22697c;

        /* renamed from: d  reason: collision with root package name */
        public final SparseArray<e> f22698d;

        public d(int i8, int i9, int i10, SparseArray<e> sparseArray) {
            this.f22695a = i8;
            this.f22696b = i9;
            this.f22697c = i10;
            this.f22698d = sparseArray;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: a  reason: collision with root package name */
        public final int f22699a;

        /* renamed from: b  reason: collision with root package name */
        public final int f22700b;

        public e(int i8, int i9) {
            this.f22699a = i8;
            this.f22700b = i9;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class f {

        /* renamed from: a  reason: collision with root package name */
        public final int f22701a;

        /* renamed from: b  reason: collision with root package name */
        public final boolean f22702b;

        /* renamed from: c  reason: collision with root package name */
        public final int f22703c;

        /* renamed from: d  reason: collision with root package name */
        public final int f22704d;

        /* renamed from: e  reason: collision with root package name */
        public final int f22705e;

        /* renamed from: f  reason: collision with root package name */
        public final int f22706f;

        /* renamed from: g  reason: collision with root package name */
        public final int f22707g;

        /* renamed from: h  reason: collision with root package name */
        public final int f22708h;

        /* renamed from: i  reason: collision with root package name */
        public final int f22709i;

        /* renamed from: j  reason: collision with root package name */
        public final int f22710j;

        /* renamed from: k  reason: collision with root package name */
        public final SparseArray<g> f22711k;

        public f(int i8, boolean z4, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, SparseArray<g> sparseArray) {
            this.f22701a = i8;
            this.f22702b = z4;
            this.f22703c = i9;
            this.f22704d = i10;
            this.f22705e = i11;
            this.f22706f = i12;
            this.f22707g = i13;
            this.f22708h = i14;
            this.f22709i = i15;
            this.f22710j = i16;
            this.f22711k = sparseArray;
        }

        public void a(f fVar) {
            SparseArray<g> sparseArray = fVar.f22711k;
            for (int i8 = 0; i8 < sparseArray.size(); i8++) {
                this.f22711k.put(sparseArray.keyAt(i8), sparseArray.valueAt(i8));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g {

        /* renamed from: a  reason: collision with root package name */
        public final int f22712a;

        /* renamed from: b  reason: collision with root package name */
        public final int f22713b;

        /* renamed from: c  reason: collision with root package name */
        public final int f22714c;

        /* renamed from: d  reason: collision with root package name */
        public final int f22715d;

        /* renamed from: e  reason: collision with root package name */
        public final int f22716e;

        /* renamed from: f  reason: collision with root package name */
        public final int f22717f;

        public g(int i8, int i9, int i10, int i11, int i12, int i13) {
            this.f22712a = i8;
            this.f22713b = i9;
            this.f22714c = i10;
            this.f22715d = i11;
            this.f22716e = i12;
            this.f22717f = i13;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class h {

        /* renamed from: a  reason: collision with root package name */
        public final int f22718a;

        /* renamed from: b  reason: collision with root package name */
        public final int f22719b;

        /* renamed from: c  reason: collision with root package name */
        public final SparseArray<f> f22720c = new SparseArray<>();

        /* renamed from: d  reason: collision with root package name */
        public final SparseArray<a> f22721d = new SparseArray<>();

        /* renamed from: e  reason: collision with root package name */
        public final SparseArray<c> f22722e = new SparseArray<>();

        /* renamed from: f  reason: collision with root package name */
        public final SparseArray<a> f22723f = new SparseArray<>();

        /* renamed from: g  reason: collision with root package name */
        public final SparseArray<c> f22724g = new SparseArray<>();

        /* renamed from: h  reason: collision with root package name */
        public C0204b f22725h;

        /* renamed from: i  reason: collision with root package name */
        public d f22726i;

        public h(int i8, int i9) {
            this.f22718a = i8;
            this.f22719b = i9;
        }

        public void a() {
            this.f22720c.clear();
            this.f22721d.clear();
            this.f22722e.clear();
            this.f22723f.clear();
            this.f22724g.clear();
            this.f22725h = null;
            this.f22726i = null;
        }
    }

    public b(int i8, int i9) {
        Paint paint = new Paint();
        this.f22674a = paint;
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        paint.setPathEffect(null);
        Paint paint2 = new Paint();
        this.f22675b = paint2;
        paint2.setStyle(Paint.Style.FILL);
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        paint2.setPathEffect(null);
        this.f22676c = new Canvas();
        this.f22677d = new C0204b(719, 575, 0, 719, 0, 575);
        this.f22678e = new a(0, c(), d(), e());
        this.f22679f = new h(i8, i9);
    }

    private static byte[] a(int i8, int i9, y yVar) {
        byte[] bArr = new byte[i8];
        for (int i10 = 0; i10 < i8; i10++) {
            bArr[i10] = (byte) yVar.h(i9);
        }
        return bArr;
    }

    private static int[] c() {
        return new int[]{0, -1, -16777216, -8421505};
    }

    private static int[] d() {
        int[] iArr = new int[16];
        iArr[0] = 0;
        for (int i8 = 1; i8 < 16; i8++) {
            if (i8 < 8) {
                iArr[i8] = f(255, (i8 & 1) != 0 ? 255 : 0, (i8 & 2) != 0 ? 255 : 0, (i8 & 4) != 0 ? 255 : 0);
            } else {
                iArr[i8] = f(255, (i8 & 1) != 0 ? 127 : 0, (i8 & 2) != 0 ? 127 : 0, (i8 & 4) == 0 ? 0 : 127);
            }
        }
        return iArr;
    }

    private static int[] e() {
        int[] iArr = new int[RecognitionOptions.QR_CODE];
        iArr[0] = 0;
        for (int i8 = 0; i8 < 256; i8++) {
            if (i8 < 8) {
                iArr[i8] = f(63, (i8 & 1) != 0 ? 255 : 0, (i8 & 2) != 0 ? 255 : 0, (i8 & 4) == 0 ? 0 : 255);
            } else {
                int i9 = i8 & 136;
                if (i9 == 0) {
                    iArr[i8] = f(255, ((i8 & 1) != 0 ? 85 : 0) + ((i8 & 16) != 0 ? 170 : 0), ((i8 & 2) != 0 ? 85 : 0) + ((i8 & 32) != 0 ? 170 : 0), ((i8 & 4) == 0 ? 0 : 85) + ((i8 & 64) == 0 ? 0 : 170));
                } else if (i9 == 8) {
                    iArr[i8] = f(127, ((i8 & 1) != 0 ? 85 : 0) + ((i8 & 16) != 0 ? 170 : 0), ((i8 & 2) != 0 ? 85 : 0) + ((i8 & 32) != 0 ? 170 : 0), ((i8 & 4) == 0 ? 0 : 85) + ((i8 & 64) == 0 ? 0 : 170));
                } else if (i9 == 128) {
                    iArr[i8] = f(255, ((i8 & 1) != 0 ? 43 : 0) + 127 + ((i8 & 16) != 0 ? 85 : 0), ((i8 & 2) != 0 ? 43 : 0) + 127 + ((i8 & 32) != 0 ? 85 : 0), ((i8 & 4) == 0 ? 0 : 43) + 127 + ((i8 & 64) == 0 ? 0 : 85));
                } else if (i9 == 136) {
                    iArr[i8] = f(255, ((i8 & 1) != 0 ? 43 : 0) + ((i8 & 16) != 0 ? 85 : 0), ((i8 & 2) != 0 ? 43 : 0) + ((i8 & 32) != 0 ? 85 : 0), ((i8 & 4) == 0 ? 0 : 43) + ((i8 & 64) == 0 ? 0 : 85));
                }
            }
        }
        return iArr;
    }

    private static int f(int i8, int i9, int i10, int i11) {
        return (i8 << 24) | (i9 << 16) | (i10 << 8) | i11;
    }

    private static int g(y yVar, int[] iArr, byte[] bArr, int i8, int i9, Paint paint, Canvas canvas) {
        boolean z4;
        int i10;
        int h8;
        int i11 = i8;
        boolean z8 = false;
        while (true) {
            byte h9 = yVar.h(2);
            if (h9 != 0) {
                z4 = z8;
                i10 = 1;
            } else {
                if (yVar.g()) {
                    h8 = yVar.h(3) + 3;
                } else {
                    if (yVar.g()) {
                        z4 = z8;
                        i10 = 1;
                    } else {
                        int h10 = yVar.h(2);
                        if (h10 == 0) {
                            z4 = true;
                        } else if (h10 == 1) {
                            z4 = z8;
                            i10 = 2;
                        } else if (h10 == 2) {
                            h8 = yVar.h(4) + 12;
                        } else if (h10 != 3) {
                            z4 = z8;
                        } else {
                            h8 = yVar.h(8) + 29;
                        }
                        h9 = 0;
                        i10 = 0;
                    }
                    h9 = 0;
                }
                z4 = z8;
                i10 = h8;
                h9 = yVar.h(2);
            }
            if (i10 != 0 && paint != null) {
                if (bArr != null) {
                    h9 = bArr[h9];
                }
                paint.setColor(iArr[h9]);
                canvas.drawRect(i11, i9, i11 + i10, i9 + 1, paint);
            }
            i11 += i10;
            if (z4) {
                return i11;
            }
            z8 = z4;
        }
    }

    private static int h(y yVar, int[] iArr, byte[] bArr, int i8, int i9, Paint paint, Canvas canvas) {
        boolean z4;
        int i10;
        int h8;
        int i11 = i8;
        boolean z8 = false;
        while (true) {
            byte h9 = yVar.h(4);
            int i12 = 2;
            if (h9 != 0) {
                z4 = z8;
                i10 = 1;
            } else if (yVar.g()) {
                if (yVar.g()) {
                    int h10 = yVar.h(2);
                    if (h10 != 0) {
                        if (h10 != 1) {
                            if (h10 == 2) {
                                h8 = yVar.h(4) + 9;
                            } else if (h10 != 3) {
                                z4 = z8;
                                h9 = 0;
                                i10 = 0;
                            } else {
                                h8 = yVar.h(8) + 25;
                            }
                        }
                        z4 = z8;
                        i10 = i12;
                        h9 = 0;
                    } else {
                        z4 = z8;
                        i10 = 1;
                        h9 = 0;
                    }
                } else {
                    h8 = yVar.h(2) + 4;
                }
                h9 = yVar.h(4);
                z4 = z8;
                i10 = h8;
            } else {
                int h11 = yVar.h(3);
                if (h11 != 0) {
                    i12 = h11 + 2;
                    z4 = z8;
                    i10 = i12;
                    h9 = 0;
                } else {
                    z4 = true;
                    h9 = 0;
                    i10 = 0;
                }
            }
            if (i10 != 0 && paint != null) {
                if (bArr != null) {
                    h9 = bArr[h9];
                }
                paint.setColor(iArr[h9]);
                canvas.drawRect(i11, i9, i11 + i10, i9 + 1, paint);
            }
            i11 += i10;
            if (z4) {
                return i11;
            }
            z8 = z4;
        }
    }

    private static int i(y yVar, int[] iArr, byte[] bArr, int i8, int i9, Paint paint, Canvas canvas) {
        boolean z4;
        int h8;
        int i10 = i8;
        boolean z8 = false;
        while (true) {
            byte h9 = yVar.h(8);
            if (h9 != 0) {
                z4 = z8;
                h8 = 1;
            } else if (yVar.g()) {
                z4 = z8;
                h8 = yVar.h(7);
                h9 = yVar.h(8);
            } else {
                int h10 = yVar.h(7);
                if (h10 != 0) {
                    z4 = z8;
                    h8 = h10;
                    h9 = 0;
                } else {
                    z4 = true;
                    h9 = 0;
                    h8 = 0;
                }
            }
            if (h8 != 0 && paint != null) {
                if (bArr != null) {
                    h9 = bArr[h9];
                }
                paint.setColor(iArr[h9]);
                canvas.drawRect(i10, i9, i10 + h8, i9 + 1, paint);
            }
            i10 += h8;
            if (z4) {
                return i10;
            }
            z8 = z4;
        }
    }

    private static void j(byte[] bArr, int[] iArr, int i8, int i9, int i10, Paint paint, Canvas canvas) {
        byte[] bArr2;
        byte[] bArr3;
        byte[] bArr4;
        y yVar = new y(bArr);
        int i11 = i9;
        int i12 = i10;
        byte[] bArr5 = null;
        byte[] bArr6 = null;
        byte[] bArr7 = null;
        while (yVar.b() != 0) {
            int h8 = yVar.h(8);
            if (h8 != 240) {
                switch (h8) {
                    case 16:
                        if (i8 != 3) {
                            if (i8 != 2) {
                                bArr2 = null;
                                i11 = g(yVar, iArr, bArr2, i11, i12, paint, canvas);
                                yVar.c();
                                break;
                            } else {
                                bArr3 = bArr7 == null ? f22671h : bArr7;
                            }
                        } else {
                            bArr3 = bArr5 == null ? f22672i : bArr5;
                        }
                        bArr2 = bArr3;
                        i11 = g(yVar, iArr, bArr2, i11, i12, paint, canvas);
                        yVar.c();
                    case 17:
                        if (i8 == 3) {
                            bArr4 = bArr6 == null ? f22673j : bArr6;
                        } else {
                            bArr4 = null;
                        }
                        i11 = h(yVar, iArr, bArr4, i11, i12, paint, canvas);
                        yVar.c();
                        break;
                    case 18:
                        i11 = i(yVar, iArr, null, i11, i12, paint, canvas);
                        break;
                    default:
                        switch (h8) {
                            case 32:
                                bArr7 = a(4, 4, yVar);
                                continue;
                            case 33:
                                bArr5 = a(4, 8, yVar);
                                continue;
                            case 34:
                                bArr6 = a(16, 8, yVar);
                                continue;
                        }
                }
            } else {
                i12 += 2;
                i11 = i9;
            }
        }
    }

    private static void k(c cVar, a aVar, int i8, int i9, int i10, Paint paint, Canvas canvas) {
        int[] iArr = i8 == 3 ? aVar.f22684d : i8 == 2 ? aVar.f22683c : aVar.f22682b;
        j(cVar.f22693c, iArr, i8, i9, i10, paint, canvas);
        j(cVar.f22694d, iArr, i8, i9, i10 + 1, paint, canvas);
    }

    private static a l(y yVar, int i8) {
        int h8;
        int i9;
        int h9;
        int h10;
        int i10;
        int i11 = 8;
        int h11 = yVar.h(8);
        yVar.r(8);
        int i12 = 2;
        int i13 = i8 - 2;
        int[] c9 = c();
        int[] d8 = d();
        int[] e8 = e();
        while (i13 > 0) {
            int h12 = yVar.h(i11);
            int h13 = yVar.h(i11);
            int i14 = i13 - 2;
            int[] iArr = (h13 & RecognitionOptions.ITF) != 0 ? c9 : (h13 & 64) != 0 ? d8 : e8;
            if ((h13 & 1) != 0) {
                h10 = yVar.h(i11);
                i10 = yVar.h(i11);
                h8 = yVar.h(i11);
                h9 = yVar.h(i11);
                i9 = i14 - 4;
            } else {
                int h14 = yVar.h(4) << 4;
                h8 = yVar.h(4) << 4;
                i9 = i14 - 2;
                h9 = yVar.h(i12) << 6;
                h10 = yVar.h(6) << i12;
                i10 = h14;
            }
            if (h10 == 0) {
                h9 = 255;
                i10 = 0;
                h8 = 0;
            }
            double d9 = h10;
            double d10 = i10 - 128;
            double d11 = h8 - 128;
            iArr[h12] = f((byte) (255 - (h9 & 255)), l0.q((int) (d9 + (1.402d * d10)), 0, 255), l0.q((int) ((d9 - (0.34414d * d11)) - (d10 * 0.71414d)), 0, 255), l0.q((int) (d9 + (d11 * 1.772d)), 0, 255));
            i13 = i9;
            h11 = h11;
            i11 = 8;
            i12 = 2;
        }
        return new a(h11, c9, d8, e8);
    }

    private static C0204b m(y yVar) {
        int i8;
        int i9;
        int i10;
        int i11;
        yVar.r(4);
        boolean g8 = yVar.g();
        yVar.r(3);
        int h8 = yVar.h(16);
        int h9 = yVar.h(16);
        if (g8) {
            int h10 = yVar.h(16);
            int h11 = yVar.h(16);
            int h12 = yVar.h(16);
            i11 = yVar.h(16);
            i10 = h11;
            i9 = h12;
            i8 = h10;
        } else {
            i8 = 0;
            i9 = 0;
            i10 = h8;
            i11 = h9;
        }
        return new C0204b(h8, h9, i8, i10, i9, i11);
    }

    private static c n(y yVar) {
        byte[] bArr;
        int h8 = yVar.h(16);
        yVar.r(4);
        int h9 = yVar.h(2);
        boolean g8 = yVar.g();
        yVar.r(1);
        byte[] bArr2 = l0.f8068f;
        if (h9 == 1) {
            yVar.r(yVar.h(8) * 16);
        } else if (h9 == 0) {
            int h10 = yVar.h(16);
            int h11 = yVar.h(16);
            if (h10 > 0) {
                bArr2 = new byte[h10];
                yVar.k(bArr2, 0, h10);
            }
            if (h11 > 0) {
                bArr = new byte[h11];
                yVar.k(bArr, 0, h11);
                return new c(h8, g8, bArr2, bArr);
            }
        }
        bArr = bArr2;
        return new c(h8, g8, bArr2, bArr);
    }

    private static d o(y yVar, int i8) {
        int h8 = yVar.h(8);
        int h9 = yVar.h(4);
        int h10 = yVar.h(2);
        yVar.r(2);
        int i9 = i8 - 2;
        SparseArray sparseArray = new SparseArray();
        while (i9 > 0) {
            int h11 = yVar.h(8);
            yVar.r(8);
            i9 -= 6;
            sparseArray.put(h11, new e(yVar.h(16), yVar.h(16)));
        }
        return new d(h8, h9, h10, sparseArray);
    }

    private static f p(y yVar, int i8) {
        int h8;
        int h9;
        int h10 = yVar.h(8);
        yVar.r(4);
        boolean g8 = yVar.g();
        yVar.r(3);
        int i9 = 16;
        int h11 = yVar.h(16);
        int h12 = yVar.h(16);
        int h13 = yVar.h(3);
        int h14 = yVar.h(3);
        int i10 = 2;
        yVar.r(2);
        int h15 = yVar.h(8);
        int h16 = yVar.h(8);
        int h17 = yVar.h(4);
        int h18 = yVar.h(2);
        yVar.r(2);
        int i11 = i8 - 10;
        SparseArray sparseArray = new SparseArray();
        while (i11 > 0) {
            int h19 = yVar.h(i9);
            int h20 = yVar.h(i10);
            int h21 = yVar.h(i10);
            int h22 = yVar.h(12);
            int i12 = h18;
            yVar.r(4);
            int h23 = yVar.h(12);
            i11 -= 6;
            if (h20 == 1 || h20 == 2) {
                i11 -= 2;
                h8 = yVar.h(8);
                h9 = yVar.h(8);
            } else {
                h8 = 0;
                h9 = 0;
            }
            sparseArray.put(h19, new g(h20, h21, h22, h23, h8, h9));
            h18 = i12;
            i10 = 2;
            i9 = 16;
        }
        return new f(h10, g8, h11, h12, h13, h14, h15, h16, h17, h18, sparseArray);
    }

    private static void q(y yVar, h hVar) {
        f fVar;
        SparseArray sparseArray;
        a aVar;
        int i8;
        a aVar2;
        c cVar;
        int h8 = yVar.h(8);
        int h9 = yVar.h(16);
        int h10 = yVar.h(16);
        int d8 = yVar.d() + h10;
        if (h10 * 8 > yVar.b()) {
            p.i("DvbParser", "Data field length exceeds limit");
            yVar.r(yVar.b());
            return;
        }
        switch (h8) {
            case 16:
                if (h9 == hVar.f22718a) {
                    d dVar = hVar.f22726i;
                    d o5 = o(yVar, h10);
                    if (o5.f22697c == 0) {
                        if (dVar != null && dVar.f22696b != o5.f22696b) {
                            hVar.f22726i = o5;
                            break;
                        }
                    } else {
                        hVar.f22726i = o5;
                        hVar.f22720c.clear();
                        hVar.f22721d.clear();
                        hVar.f22722e.clear();
                        break;
                    }
                }
                break;
            case 17:
                d dVar2 = hVar.f22726i;
                if (h9 == hVar.f22718a && dVar2 != null) {
                    f p8 = p(yVar, h10);
                    if (dVar2.f22697c == 0 && (fVar = hVar.f22720c.get(p8.f22701a)) != null) {
                        p8.a(fVar);
                    }
                    hVar.f22720c.put(p8.f22701a, p8);
                    break;
                }
                break;
            case 18:
                if (h9 == hVar.f22718a) {
                    a l8 = l(yVar, h10);
                    sparseArray = hVar.f22721d;
                    aVar = l8;
                } else if (h9 == hVar.f22719b) {
                    a l9 = l(yVar, h10);
                    sparseArray = hVar.f22723f;
                    aVar = l9;
                }
                i8 = aVar.f22681a;
                aVar2 = aVar;
                sparseArray.put(i8, aVar2);
                break;
            case 19:
                if (h9 == hVar.f22718a) {
                    c n8 = n(yVar);
                    sparseArray = hVar.f22722e;
                    cVar = n8;
                } else if (h9 == hVar.f22719b) {
                    c n9 = n(yVar);
                    sparseArray = hVar.f22724g;
                    cVar = n9;
                }
                i8 = cVar.f22691a;
                aVar2 = cVar;
                sparseArray.put(i8, aVar2);
                break;
            case 20:
                if (h9 == hVar.f22718a) {
                    hVar.f22725h = m(yVar);
                    break;
                }
                break;
        }
        yVar.s(d8 - yVar.d());
    }

    public List<p5.b> b(byte[] bArr, int i8) {
        int i9;
        SparseArray<g> sparseArray;
        y yVar = new y(bArr, i8);
        while (yVar.b() >= 48 && yVar.h(8) == 15) {
            q(yVar, this.f22679f);
        }
        h hVar = this.f22679f;
        d dVar = hVar.f22726i;
        if (dVar == null) {
            return Collections.emptyList();
        }
        C0204b c0204b = hVar.f22725h;
        if (c0204b == null) {
            c0204b = this.f22677d;
        }
        Bitmap bitmap = this.f22680g;
        if (bitmap == null || c0204b.f22685a + 1 != bitmap.getWidth() || c0204b.f22686b + 1 != this.f22680g.getHeight()) {
            Bitmap createBitmap = Bitmap.createBitmap(c0204b.f22685a + 1, c0204b.f22686b + 1, Bitmap.Config.ARGB_8888);
            this.f22680g = createBitmap;
            this.f22676c.setBitmap(createBitmap);
        }
        ArrayList arrayList = new ArrayList();
        SparseArray<e> sparseArray2 = dVar.f22698d;
        for (int i10 = 0; i10 < sparseArray2.size(); i10++) {
            this.f22676c.save();
            e valueAt = sparseArray2.valueAt(i10);
            f fVar = this.f22679f.f22720c.get(sparseArray2.keyAt(i10));
            int i11 = valueAt.f22699a + c0204b.f22687c;
            int i12 = valueAt.f22700b + c0204b.f22689e;
            this.f22676c.clipRect(i11, i12, Math.min(fVar.f22703c + i11, c0204b.f22688d), Math.min(fVar.f22704d + i12, c0204b.f22690f));
            a aVar = this.f22679f.f22721d.get(fVar.f22707g);
            if (aVar == null && (aVar = this.f22679f.f22723f.get(fVar.f22707g)) == null) {
                aVar = this.f22678e;
            }
            SparseArray<g> sparseArray3 = fVar.f22711k;
            int i13 = 0;
            while (i13 < sparseArray3.size()) {
                int keyAt = sparseArray3.keyAt(i13);
                g valueAt2 = sparseArray3.valueAt(i13);
                c cVar = this.f22679f.f22722e.get(keyAt);
                c cVar2 = cVar == null ? this.f22679f.f22724g.get(keyAt) : cVar;
                if (cVar2 != null) {
                    i9 = i13;
                    sparseArray = sparseArray3;
                    k(cVar2, aVar, fVar.f22706f, valueAt2.f22714c + i11, i12 + valueAt2.f22715d, cVar2.f22692b ? null : this.f22674a, this.f22676c);
                } else {
                    i9 = i13;
                    sparseArray = sparseArray3;
                }
                i13 = i9 + 1;
                sparseArray3 = sparseArray;
            }
            if (fVar.f22702b) {
                int i14 = fVar.f22706f;
                this.f22675b.setColor(i14 == 3 ? aVar.f22684d[fVar.f22708h] : i14 == 2 ? aVar.f22683c[fVar.f22709i] : aVar.f22682b[fVar.f22710j]);
                this.f22676c.drawRect(i11, i12, fVar.f22703c + i11, fVar.f22704d + i12, this.f22675b);
            }
            arrayList.add(new b.C0196b().f(Bitmap.createBitmap(this.f22680g, i11, i12, fVar.f22703c, fVar.f22704d)).k(i11 / c0204b.f22685a).l(0).h(i12 / c0204b.f22686b, 0).i(0).n(fVar.f22703c / c0204b.f22685a).g(fVar.f22704d / c0204b.f22686b).a());
            this.f22676c.drawColor(0, PorterDuff.Mode.CLEAR);
            this.f22676c.restore();
        }
        return Collections.unmodifiableList(arrayList);
    }

    public void r() {
        this.f22679f.a();
    }
}
