package u5;

import android.text.TextUtils;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b {

    /* renamed from: a  reason: collision with root package name */
    public final int f23033a;

    /* renamed from: b  reason: collision with root package name */
    public final int f23034b;

    /* renamed from: c  reason: collision with root package name */
    public final int f23035c;

    /* renamed from: d  reason: collision with root package name */
    public final int f23036d;

    /* renamed from: e  reason: collision with root package name */
    public final int f23037e;

    private b(int i8, int i9, int i10, int i11, int i12) {
        this.f23033a = i8;
        this.f23034b = i9;
        this.f23035c = i10;
        this.f23036d = i11;
        this.f23037e = i12;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static b a(String str) {
        char c9;
        b6.a.a(str.startsWith("Format:"));
        String[] split = TextUtils.split(str.substring(7), ",");
        int i8 = -1;
        int i9 = -1;
        int i10 = -1;
        int i11 = -1;
        for (int i12 = 0; i12 < split.length; i12++) {
            String e8 = com.google.common.base.c.e(split[i12].trim());
            e8.hashCode();
            switch (e8.hashCode()) {
                case 100571:
                    if (e8.equals("end")) {
                        c9 = 0;
                        break;
                    }
                    c9 = 65535;
                    break;
                case 3556653:
                    if (e8.equals("text")) {
                        c9 = 1;
                        break;
                    }
                    c9 = 65535;
                    break;
                case 109757538:
                    if (e8.equals("start")) {
                        c9 = 2;
                        break;
                    }
                    c9 = 65535;
                    break;
                case 109780401:
                    if (e8.equals("style")) {
                        c9 = 3;
                        break;
                    }
                    c9 = 65535;
                    break;
                default:
                    c9 = 65535;
                    break;
            }
            switch (c9) {
                case 0:
                    i9 = i12;
                    break;
                case 1:
                    i11 = i12;
                    break;
                case 2:
                    i8 = i12;
                    break;
                case 3:
                    i10 = i12;
                    break;
            }
        }
        if (i8 == -1 || i9 == -1 || i11 == -1) {
            return null;
        }
        return new b(i8, i9, i10, i11, split.length);
    }
}
