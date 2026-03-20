package b3;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.Log;
import b3.c;
import b3.d;
import com.example.blelibrary.scan.LEDNetWFDevice;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c {

    /* renamed from: d  reason: collision with root package name */
    private static final String f8005d = "c";

    /* renamed from: a  reason: collision with root package name */
    private d f8006a;

    /* renamed from: b  reason: collision with root package name */
    private b f8007b;

    /* renamed from: c  reason: collision with root package name */
    private Handler f8008c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements d.b {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Context f8009a;

        a(Context context) {
            this.f8009a = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void b(LEDNetWFDevice lEDNetWFDevice) {
            if (c.this.f8007b == null || lEDNetWFDevice == null) {
                return;
            }
            c.this.f8007b.b(lEDNetWFDevice);
        }

        @Override // b3.d.b
        public void onScanFailed(int i8) {
            c.this.f8007b.a(i8);
            c.this.g(this.f8009a);
        }

        @Override // b3.d.b
        public void onScanResult(ScanResult scanResult) {
            ScanRecord scanRecord;
            String name = scanResult.getDevice().getName();
            if (name == null) {
                return;
            }
            if ((name.contains("LEDnetWF") || name.contains("IOTWF")) && (scanRecord = scanResult.getScanRecord()) != null) {
                final LEDNetWFDevice e8 = c.this.e(name, scanResult.getDevice(), scanRecord.getBytes(), scanResult);
                c.this.f8008c.post(new Runnable() { // from class: b3.b
                    @Override // java.lang.Runnable
                    public final void run() {
                        c.a.this.b(e8);
                    }
                });
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a(int i8);

        void b(LEDNetWFDevice lEDNetWFDevice);
    }

    public c() {
        Looper myLooper = Looper.myLooper();
        Objects.requireNonNull(myLooper);
        this.f8008c = new Handler(myLooper);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LEDNetWFDevice e(String str, BluetoothDevice bluetoothDevice, byte[] bArr, ScanResult scanResult) {
        LEDNetWFDevice lEDNetWFDevice;
        String str2;
        String str3;
        if (bArr == null || bArr.length < 32) {
            return null;
        }
        Log.i(f8005d, c3.b.a(bArr, bArr.length));
        try {
            ArrayMap arrayMap = new ArrayMap();
            int i8 = 0;
            do {
                int i9 = (bArr[i8] & 255) - 1;
                if (i9 > 0) {
                    int i10 = bArr[i8 + 1] & 255;
                    int i11 = i8 + 2;
                    if ((i11 + i9) - 1 < bArr.length) {
                        byte[] bArr2 = new byte[i9];
                        System.arraycopy(bArr, i11, bArr2, 0, i9);
                        arrayMap.put(Integer.valueOf(i10), bArr2);
                    }
                } else {
                    i9 = 0;
                }
                i8 += i9 + 2;
            } while (i8 < bArr.length);
            if (arrayMap.containsKey(255) && arrayMap.containsKey(22)) {
                byte[] bArr3 = (byte[]) arrayMap.get(22);
                byte[] bArr4 = (byte[]) arrayMap.get(255);
                if (bArr4.length == 29) {
                    int i12 = ((bArr4[1] & 255) << 8) | (bArr4[2] & 255);
                    if (LEDNetWFDevice.a(i12) || i12 == 23124 || i12 == 23123) {
                        lEDNetWFDevice = new LEDNetWFDevice();
                        lEDNetWFDevice.p(str);
                        lEDNetWFDevice.x(bArr4[0] & 255);
                        lEDNetWFDevice.t(i12);
                        lEDNetWFDevice.m(bArr3[3] & 255);
                        lEDNetWFDevice.s(c3.b.a(new byte[]{bArr3[4], bArr3[5], bArr3[6], bArr3[7], bArr3[8], bArr3[9]}, 6));
                        lEDNetWFDevice.u(((bArr3[10] & 255) << 8) | (bArr3[11] & 255));
                        lEDNetWFDevice.q(bArr3[12] & 255);
                        lEDNetWFDevice.r(bArr3[13] & 255);
                        lEDNetWFDevice.o(bluetoothDevice);
                        byte[] bArr5 = new byte[26];
                        System.arraycopy(bArr4, 3, bArr5, 0, 26);
                        lEDNetWFDevice.y(bArr5);
                        lEDNetWFDevice.n(bArr3);
                        lEDNetWFDevice.v(scanResult);
                        str2 = f8005d;
                        str3 = " device " + lEDNetWFDevice.toString();
                        Log.i(str2, str3);
                        return lEDNetWFDevice;
                    }
                }
                return null;
            }
            if (arrayMap.containsKey(255)) {
                byte[] bArr6 = (byte[]) arrayMap.get(255);
                if (bArr6.length == 29) {
                    int i13 = ((bArr6[1] & 255) << 8) | (bArr6[2] & 255);
                    if (LEDNetWFDevice.a(i13) || i13 == 23124 || i13 == 23123) {
                        lEDNetWFDevice = new LEDNetWFDevice();
                        lEDNetWFDevice.p(str);
                        lEDNetWFDevice.x(bArr6[0] & 255);
                        lEDNetWFDevice.t(i13);
                        lEDNetWFDevice.m(bArr6[3] & 255);
                        lEDNetWFDevice.s(c3.b.a(new byte[]{bArr6[4], bArr6[5], bArr6[6], bArr6[7], bArr6[8], bArr6[9]}, 6));
                        lEDNetWFDevice.u(((bArr6[10] & 255) << 8) | (bArr6[11] & 255));
                        lEDNetWFDevice.q(bArr6[12] & 255);
                        lEDNetWFDevice.r(bArr6[13] & 255);
                        byte[] bArr7 = new byte[11];
                        System.arraycopy(bArr6, 16, bArr7, 0, 11);
                        lEDNetWFDevice.y(bArr7);
                        byte[] bArr8 = new byte[2];
                        System.arraycopy(bArr6, 27, bArr8, 0, 2);
                        lEDNetWFDevice.w(bArr8);
                        lEDNetWFDevice.o(bluetoothDevice);
                        lEDNetWFDevice.n(bArr6);
                        lEDNetWFDevice.v(scanResult);
                        str2 = f8005d;
                        str3 = " device " + lEDNetWFDevice.toString();
                        Log.i(str2, str3);
                        return lEDNetWFDevice;
                    }
                }
            } else if (arrayMap.containsKey(1)) {
                byte b9 = ((byte[]) arrayMap.get(1))[0];
                Log.i(f8005d, " flagValue " + ((int) b9));
            } else if (arrayMap.containsKey(9)) {
                String str4 = new String((byte[]) arrayMap.get(9));
                Log.i(f8005d, " complete local name " + str4);
            }
            return null;
        } catch (Exception e8) {
            String str5 = f8005d;
            String message = e8.getMessage();
            Objects.requireNonNull(message);
            Log.e(str5, message);
            return null;
        }
    }

    public void d(b bVar) {
        this.f8007b = bVar;
    }

    public void f(Context context) {
        d dVar = new d(new a(context));
        this.f8006a = dVar;
        dVar.b(0, context);
    }

    public void g(Context context) {
        d dVar = this.f8006a;
        if (dVar != null) {
            dVar.c(context);
        }
    }
}
