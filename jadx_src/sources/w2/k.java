package w2;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import c3.b;
import com.daimajia.numberprogressbar.BuildConfig;
import com.example.blelibrary.DisconnectException;
import com.example.blelibrary.protocol.ConfigureParams;
import com.example.blelibrary.protocol.OldBLERouterWifiInfo;
import com.example.blelibrary.protocol.VersionResponse;
import com.example.blelibrary.protocol.WifiState;
import com.example.blelibrary.protocol.standard.Request;
import com.example.blelibrary.protocol.standard.Response;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.gson.e;
import gi.d;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class k extends BluetoothGattCallback {
    private static final String C = k.class.getName();
    private static e D = new e();
    private static Handler E;

    /* renamed from: a  reason: collision with root package name */
    private Context f23420a;

    /* renamed from: b  reason: collision with root package name */
    private BluetoothDevice f23421b;

    /* renamed from: e  reason: collision with root package name */
    private BluetoothGatt f23424e;

    /* renamed from: f  reason: collision with root package name */
    private byte f23425f;

    /* renamed from: m  reason: collision with root package name */
    private a f23432m;

    /* renamed from: p  reason: collision with root package name */
    private ExecutorService f23435p;

    /* renamed from: r  reason: collision with root package name */
    private VersionResponse f23436r;

    /* renamed from: s  reason: collision with root package name */
    private WifiState f23437s;

    /* renamed from: t  reason: collision with root package name */
    public int f23438t;

    /* renamed from: u  reason: collision with root package name */
    private String f23439u;

    /* renamed from: c  reason: collision with root package name */
    private BluetoothGattCharacteristic f23422c = null;

    /* renamed from: d  reason: collision with root package name */
    private BluetoothGattCharacteristic f23423d = null;

    /* renamed from: g  reason: collision with root package name */
    private int f23426g = 0;

    /* renamed from: h  reason: collision with root package name */
    private int f23427h = RecognitionOptions.QR_CODE;

    /* renamed from: i  reason: collision with root package name */
    private boolean f23428i = false;

    /* renamed from: j  reason: collision with root package name */
    private byte f23429j = 0;

    /* renamed from: k  reason: collision with root package name */
    private boolean f23430k = false;

    /* renamed from: l  reason: collision with root package name */
    private boolean f23431l = false;

    /* renamed from: n  reason: collision with root package name */
    private String f23433n = BuildConfig.FLAVOR;

    /* renamed from: o  reason: collision with root package name */
    private String f23434o = BuildConfig.FLAVOR;
    private List<OldBLERouterWifiInfo> q = new LinkedList();

    /* renamed from: v  reason: collision with root package name */
    private boolean f23440v = true;

    /* renamed from: w  reason: collision with root package name */
    private boolean f23441w = true;

    /* renamed from: x  reason: collision with root package name */
    private boolean f23442x = true;

    /* renamed from: y  reason: collision with root package name */
    private boolean f23443y = false;

    /* renamed from: z  reason: collision with root package name */
    private boolean f23444z = false;
    private final Object A = new Object();
    private final Object B = new Object();

    static {
        Looper myLooper = Looper.myLooper();
        Objects.requireNonNull(myLooper);
        E = new Handler(myLooper);
    }

    public k(Context context, BluetoothDevice bluetoothDevice, int i8) {
        this.f23420a = context;
        this.f23421b = bluetoothDevice;
        this.f23438t = i8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void A(gi.e eVar) {
        try {
            this.q.clear();
            E(9, null);
            S(2500L);
            Log.i(C, "get wifi list.");
            N();
            while (!this.f23431l) {
                this.f23434o = "wifiListData";
                E(11, null);
            }
            eVar.onNext(this.q);
        } catch (Throwable th) {
            eVar.onError(th);
        }
        eVar.onComplete();
    }

    private byte[] B(int i8, boolean z4, int i9, byte[] bArr, int i10) {
        int i11;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte a9 = c3.a.a((byte) 0, 0);
        byte d8 = c3.a.d(c3.a.a(c3.a.d(c3.a.a(c3.a.a(c3.a.d(i10 <= bArr.length ? c3.a.a(a9, 1) : c3.a.d(a9, 1), 2), 3), 5), 4), 6), 7);
        byteArrayOutputStream.write(d8);
        Log.e("ceshiwu", String.valueOf(c3.a.b(d8, 4)));
        byteArrayOutputStream.write(this.f23425f);
        if (i8 == 0) {
            byteArrayOutputStream.write(i8 & 255);
            byteArrayOutputStream.write(i8 >> 8);
            byteArrayOutputStream.write(i10 & 255);
            byteArrayOutputStream.write(i10 >> 8);
            byteArrayOutputStream.write(bArr.length + 9 + 2);
            byteArrayOutputStream.write(1);
            byteArrayOutputStream.write(1);
            byteArrayOutputStream.write(1);
            byteArrayOutputStream.write(1);
            byteArrayOutputStream.write(1);
            byteArrayOutputStream.write(2);
            byteArrayOutputStream.write(2);
            byteArrayOutputStream.write(2);
            byteArrayOutputStream.write(2);
        } else {
            if (z4) {
                byteArrayOutputStream.write(i8 & 255);
                i11 = i8 >> 8;
            } else {
                int e8 = c3.a.e(i8, 15);
                byteArrayOutputStream.write(i8 & 255);
                i11 = e8 >> 8;
            }
            byteArrayOutputStream.write(i11);
            byteArrayOutputStream.write(bArr.length + 3);
            byteArrayOutputStream.write(1);
        }
        byteArrayOutputStream.write(bArr.length);
        Log.e("type...", String.valueOf(i9));
        byteArrayOutputStream.write(i9);
        byteArrayOutputStream.write(bArr, 0, bArr.length);
        Log.e("ceshi", String.valueOf(byteArrayOutputStream));
        return byteArrayOutputStream.toByteArray();
    }

    private void C() {
        synchronized (this) {
            E.post(new j(this));
        }
    }

    private void D() {
        synchronized (this) {
            E.post(new h(this));
        }
    }

    private void E(int i8, byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            H(i8, "1234".getBytes());
        } else {
            F(i8, bArr);
        }
    }

    private void F(int i8, byte[] bArr) {
        if (bArr == null) {
            return;
        }
        String str = C;
        Log.e(str, "data length : " + bArr.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i9 = 0;
        while (true) {
            int i10 = this.f23427h;
            byte[] bArr2 = new byte[i9 == 0 ? i10 - 19 : i10 - 9];
            int read = byteArrayInputStream.read(bArr2, 0, bArr2.length);
            String str2 = C;
            Log.e(str2, "data buf length : " + read);
            if (read == -1) {
                return;
            }
            Log.e("postOS.write(dat", "postOS.write(dat");
            byteArrayOutputStream.write(bArr2, 0, read);
            boolean z4 = byteArrayInputStream.available() > 0;
            byte[] B = B(i9, z4, i8, byteArrayOutputStream.toByteArray(), bArr.length);
            byteArrayOutputStream.reset();
            U(B);
            Log.e(str2, "frag:::" + z4);
            if (!z4) {
                return;
            }
            i9++;
        }
    }

    private <Q> void G(Request<Q> request) {
        try {
            this.f23439u = null;
            this.f23434o = "custom_response";
            E(19, D.u(request).getBytes(StandardCharsets.UTF_8));
        } catch (InterruptedException e8) {
            String str = C;
            String message = e8.getMessage();
            Objects.requireNonNull(message);
            Log.e(str, message);
            throw new RuntimeException(e8);
        }
    }

    private void H(int i8, byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(c3.a.a(c3.a.a(c3.a.a(c3.a.a(c3.a.a(c3.a.a(c3.a.a(c3.a.a((byte) 0, 7), 6), 5), 4), 2), 3), 1), 0));
        byteArrayOutputStream.write(this.f23425f);
        int e8 = c3.a.e(0, 15);
        byteArrayOutputStream.write((byte) (e8 & 255));
        byteArrayOutputStream.write((byte) (e8 >> 8));
        byteArrayOutputStream.write(4);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(14);
        byteArrayOutputStream.write(1);
        byteArrayOutputStream.write(1);
        byteArrayOutputStream.write(1);
        byteArrayOutputStream.write(1);
        byteArrayOutputStream.write(1);
        byteArrayOutputStream.write(2);
        byteArrayOutputStream.write(2);
        byteArrayOutputStream.write(2);
        byteArrayOutputStream.write(2);
        byteArrayOutputStream.write(i8);
        byteArrayOutputStream.write(bArr, 0, bArr.length);
        U(byteArrayOutputStream.toByteArray());
    }

    private void N() {
        this.f23430k = false;
        this.f23428i = false;
        this.f23431l = false;
    }

    private void O(byte[] bArr) {
        this.f23430k = false;
        bArr[0] = c3.a.d(bArr[0], 7);
        String str = C;
        Log.e(str, b.a(bArr, bArr.length) + " seq : " + ((int) this.f23425f) + " last seq : " + ((int) this.f23429j) + " READ_ACK : " + this.f23428i);
        p(bArr);
        boolean readCharacteristic = this.f23424e.readCharacteristic(this.f23423d);
        StringBuilder sb = new StringBuilder();
        sb.append("ReadGattCharacteristic status : ");
        sb.append(readCharacteristic);
        Log.e(str, sb.toString());
    }

    private void Q(VersionResponse versionResponse) {
        this.f23436r = versionResponse;
    }

    private void R(WifiState wifiState) {
        this.f23437s = wifiState;
    }

    public static byte[] T(byte[] bArr, int i8, int i9) {
        byte[] bArr2 = new byte[i9];
        for (int i10 = i8; i10 < i8 + i9; i10++) {
            bArr2[i10 - i8] = bArr[i10];
        }
        return bArr2;
    }

    private void U(byte[] bArr) {
        if (!r()) {
            throw new DisconnectException("Device have been disconnected.");
        }
        p(bArr);
        o(bArr);
    }

    private void j(OldBLERouterWifiInfo oldBLERouterWifiInfo) {
        this.q.add(oldBLERouterWifiInfo);
    }

    private void k() {
        String str = C;
        Log.i(str, "DISCONNECT : " + hashCode());
        synchronized (this.A) {
            if (this.f23421b != null) {
                if (this.f23444z) {
                    this.f23443y = true;
                } else {
                    M();
                    synchronized (this.B) {
                        this.B.notifyAll();
                    }
                    ExecutorService executorService = this.f23435p;
                    if (executorService != null) {
                        executorService.shutdown();
                    }
                    BluetoothGatt bluetoothGatt = this.f23424e;
                    if (bluetoothGatt != null) {
                        bluetoothGatt.disconnect();
                        this.f23424e = null;
                    }
                    if (this.f23422c != null) {
                        this.f23422c = null;
                    }
                    if (this.f23423d != null) {
                        this.f23423d = null;
                    }
                    if (this.f23432m != null) {
                        this.f23432m = null;
                    }
                    if (this.f23421b != null) {
                        this.f23421b = null;
                    }
                    this.f23441w = true;
                    this.f23440v = true;
                    this.f23426g = 0;
                    this.f23442x = false;
                    this.A.notifyAll();
                }
            }
        }
    }

    private void o(byte[] bArr) {
        byte b9;
        if (!r()) {
            throw new DisconnectException("Device had disconnected.");
        }
        int b10 = c3.a.b(bArr[0], 3);
        N();
        if (b10 != 0) {
            return;
        }
        while (true) {
            if (this.f23430k) {
                O(bArr);
            } else {
                this.f23424e.readCharacteristic(this.f23423d);
            }
            if (!this.f23428i || this.f23425f != this.f23429j || this.f23430k) {
                synchronized (this) {
                    wait(500L);
                    String str = C;
                    Log.i(str, "status " + this.f23428i + " seq : " + ((int) this.f23425f) + " receiveSeq : " + ((int) this.f23429j) + " REWRITE : " + this.f23430k);
                }
            }
            if (!r()) {
                throw new DisconnectException("Device had disconnected.");
            }
            if (this.f23428i && (b9 = this.f23425f) == this.f23429j && !this.f23430k) {
                this.f23425f = (byte) (b9 + 1);
                return;
            }
        }
    }

    private void p(byte[] bArr) {
        synchronized (this) {
            String str = C;
            Log.e(str, "gattWrite data are : " + Arrays.toString(bArr));
            Log.e(str, "gattWrite data and length" + b.a(bArr, bArr.length) + ": length" + bArr.length);
            this.f23422c.setValue(bArr);
            Log.e(str, "gattWrite setValue data ok");
            if ((this.f23422c.getProperties() & 8) == 0 && (this.f23422c.getProperties() & 4) == 0) {
                Log.e(str, "1 error");
            }
            if (this.f23422c.getValue() == null) {
                Log.e(str, "2 error");
            }
            if (this.f23422c.getService() == null) {
                Log.e(str, "3 error");
            }
            boolean writeCharacteristic = this.f23424e.writeCharacteristic(this.f23422c);
            Log.i(str, "GattWrite write characteristic status : " + writeCharacteristic);
            wait();
        }
    }

    private WifiState q() {
        this.f23437s = null;
        this.f23434o = "wifiState";
        E(13, null);
        return this.f23437s;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void s(ConfigureParams configureParams, gi.e eVar) {
        String str;
        String str2;
        try {
            S(500L);
            E(2, configureParams.getSsid());
            S(10L);
            E(3, configureParams.getPassword().getBytes());
            S(10L);
            E(3, null);
            while (true) {
                WifiState q = q();
                if (q == null) {
                    throw new RuntimeException("Wifi state is null.");
                }
                int state = q.getState();
                if (4 == state || 1 == state || 8 == state || 9 == state || 5 == state) {
                    break;
                }
                if (3 == state) {
                    str = "Wifi getting ip...";
                    str2 = "wifi status : " + q.toString();
                } else if (2 != state) {
                    throw new RuntimeException("Unknown state code " + state);
                } else {
                    str = "Wifi connecting...";
                    str2 = "wifi status : " + q.toString();
                }
                Log.e(str, str2);
                S(2000L);
            }
        } finally {
            try {
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void t() {
        synchronized (this.A) {
            do {
                try {
                    M();
                    String str = C;
                    Log.i(str, "CONNECT : " + this.f23427h + " mGatt : " + this.f23424e + " " + hashCode());
                    BluetoothGatt bluetoothGatt = this.f23424e;
                    if (bluetoothGatt != null) {
                        bluetoothGatt.close();
                    }
                    BluetoothDevice bluetoothDevice = this.f23421b;
                    if (bluetoothDevice != null) {
                        this.f23424e = Build.VERSION.SDK_INT >= 23 ? bluetoothDevice.connectGatt(this.f23420a, false, this, 2) : bluetoothDevice.connectGatt(this.f23420a, false, this);
                    }
                    this.f23444z = true;
                    this.A.wait();
                } catch (Exception e8) {
                    String str2 = C;
                    Log.i(str2, e8.getMessage() + BuildConfig.FLAVOR);
                }
            } while (this.f23442x);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void u() {
        synchronized (this) {
            a aVar = this.f23432m;
            if (aVar != null) {
                aVar.onConnected();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void v() {
        synchronized (this) {
            a aVar = this.f23432m;
            if (aVar != null) {
                aVar.onDisconnect();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void w() {
        synchronized (this) {
            a aVar = this.f23432m;
            if (aVar != null) {
                aVar.a();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void x(Request request, gi.e eVar) {
        G(request);
        eVar.onComplete();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void y(Request request, com.google.gson.reflect.a aVar, gi.e eVar) {
        String str;
        try {
            G(request);
            String str2 = C;
            Log.i(str2, "postCustom before : " + this.f23439u);
            if (this.f23439u == null) {
                synchronized (this.B) {
                    this.B.wait();
                }
            }
            Response response = null;
            if (this.f23439u != null) {
                Log.i(str2, "postCustom after : " + this.f23439u);
                response = (Response) D.m(this.f23439u, aVar.getType());
                str = "response : " + response.toString();
            } else {
                str = "JSON : is null";
            }
            Log.i(str2, str);
            eVar.onNext(response);
        } catch (Throwable th) {
            eVar.onError(th);
        }
        eVar.onComplete();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void z(gi.e eVar) {
        try {
            E(4, null);
            eVar.onNext(Boolean.TRUE);
        } catch (Throwable th) {
            eVar.onError(th);
        }
        eVar.onComplete();
    }

    public <T> d<Void> I(Request<T> request) {
        String str = C;
        Log.i(str, "publish : " + request.getCmdId());
        return c3.d.a(new e(this, request));
    }

    public <T, Q> d<Response<T>> J(Request<Q> request, int i8, com.google.gson.reflect.a<Response<T>> aVar) {
        return c3.d.a(new f(this, request, aVar));
    }

    public d<Boolean> K() {
        return c3.d.a(new c(this));
    }

    public d<List<OldBLERouterWifiInfo>> L() {
        return c3.d.a(new b(this));
    }

    public void M() {
        Log.i(C, "RESET");
        this.f23427h = RecognitionOptions.QR_CODE;
        this.f23433n = BuildConfig.FLAVOR;
        this.f23434o = BuildConfig.FLAVOR;
        this.f23425f = (byte) 0;
        this.f23429j = (byte) 0;
        N();
    }

    public void P(a aVar) {
        this.f23432m = aVar;
    }

    public void S(long j8) {
        try {
            Thread.sleep(j8);
        } catch (InterruptedException unused) {
            Log.w(C, "sleep: interrupted");
            Thread.currentThread().interrupt();
        }
    }

    public d<WifiState> l(ConfigureParams configureParams) {
        return c3.d.a(new d(this, configureParams));
    }

    public void m() {
        synchronized (this) {
            if (this.f23441w) {
                this.f23435p = Executors.newSingleThreadExecutor();
                new Thread((Runnable) new i(this)).start();
                this.f23441w = true;
            }
        }
    }

    public void n() {
        synchronized (this) {
            k();
            notifyAll();
        }
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super.onCharacteristicChanged(bluetoothGatt, bluetoothGattCharacteristic);
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i8) {
        String str;
        byte b9;
        String str2 = C;
        Log.e(str2, "Read status is:" + i8);
        synchronized (this) {
            if (i8 == 0) {
                byte[] value = bluetoothGattCharacteristic.getValue();
                if (value != null && value.length != 0) {
                    Log.e(str2, "receive length : " + value.length);
                    Log.e(str2, "get result are : " + Arrays.toString(value));
                    Log.e(str2, "get result are " + new String(value));
                    this.f23429j = value[1];
                    Log.e(str2, String.valueOf((int) this.f23429j) + " push seq : " + ((int) this.f23425f));
                    int i9 = ((value[3] << 8) | value[2]) & 32767;
                    if (i9 == 0) {
                        b9 = value[17];
                        if (c3.a.f(Byte.valueOf(value[6])).intValue() < 11) {
                            Log.e(str2, "get error bec dateLength");
                            this.f23430k = true;
                            notify();
                            return;
                        }
                    } else {
                        b9 = value[7];
                        if (c3.a.f(Byte.valueOf(value[6])).intValue() < 1) {
                            Log.e(str2, "get error bec dateLength");
                            this.f23430k = true;
                            notify();
                            return;
                        }
                    }
                    Log.e(str2, String.valueOf((int) b9));
                    if (b9 != 0) {
                        if (b9 != 1 && b9 != 2) {
                            this.f23430k = true;
                            this.f23428i = false;
                            Log.e(str2, "analyze data error.");
                            notify();
                            return;
                        }
                        S(200L);
                        Log.e(str2, "SUBTYPE_NOT_READY");
                        this.f23430k = true;
                        this.f23428i = false;
                        notify();
                        return;
                    }
                    this.f23428i = true;
                    this.f23430k = false;
                    Log.e(str2, "SUBTYPE_ACK_SUCCESS");
                    String str3 = i9 == 0 ? new String(T(value, 18, c3.a.f(Byte.valueOf(value[16])).intValue())) : new String(T(value, 8, c3.a.f(Byte.valueOf(value[6])).intValue()));
                    Log.e(str2, "DATA STRING : " + str3);
                    if (str3.length() == 0) {
                        this.f23431l = true;
                        str = "not read data : true";
                    } else {
                        Log.e("allResult", this.f23433n);
                        int c9 = c3.a.c(value[2] | (value[3] << 8), 15);
                        if (c9 == 1 && this.f23425f == this.f23429j) {
                            this.f23433n += str3;
                            Log.e("get date", "stop start next -> date");
                            Log.i(str2, " resultTypeKey " + this.f23434o);
                            String str4 = this.f23434o;
                            char c10 = 65535;
                            switch (str4.hashCode()) {
                                case -766907998:
                                    if (str4.equals("deviceVersion")) {
                                        c10 = 1;
                                        break;
                                    }
                                    break;
                                case 515752861:
                                    if (str4.equals("wifiListData")) {
                                        c10 = 2;
                                        break;
                                    }
                                    break;
                                case 521996623:
                                    if (str4.equals("custom_response")) {
                                        c10 = 3;
                                        break;
                                    }
                                    break;
                                case 1390103164:
                                    if (str4.equals("wifiState")) {
                                        c10 = 0;
                                        break;
                                    }
                                    break;
                            }
                            if (c10 == 0) {
                                R((WifiState) D.l(this.f23433n, WifiState.class));
                            } else if (c10 == 1) {
                                VersionResponse versionResponse = new VersionResponse();
                                versionResponse.setVersionValues(this.f23433n);
                                Q(versionResponse);
                            } else if (c10 == 2) {
                                OldBLERouterWifiInfo oldBLERouterWifiInfo = (OldBLERouterWifiInfo) D.l(this.f23433n, OldBLERouterWifiInfo.class);
                                if (oldBLERouterWifiInfo.getSsid() != null && !oldBLERouterWifiInfo.getSsid().isEmpty()) {
                                    j(oldBLERouterWifiInfo);
                                    Log.i(str2, "wifi info : " + oldBLERouterWifiInfo.toString());
                                }
                            } else if (c10 == 3) {
                                synchronized (this.B) {
                                    this.f23439u = this.f23433n;
                                    this.B.notify();
                                }
                            }
                            this.f23434o = BuildConfig.FLAVOR;
                            this.f23433n = BuildConfig.FLAVOR;
                            str2 = C;
                            str = "allResult : " + this.f23433n;
                        } else if (this.f23425f == this.f23429j && c9 == 0) {
                            this.f23433n += str3;
                            this.f23424e.readCharacteristic(this.f23423d);
                            Log.i(str2, "Not the last one...");
                        } else {
                            this.f23430k = true;
                            this.f23428i = false;
                            str = ((int) this.f23425f) + " req " + ((int) this.f23429j) + " receiveSeq " + c9 + " code ";
                        }
                    }
                    Log.e(str2, str);
                }
                str = "data==null";
                Log.e(str2, str);
            }
            notify();
        }
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i8) {
        String str = C;
        Log.i(str, "onCharacteristicWrite : " + i8);
        if (bluetoothGattCharacteristic.equals(this.f23422c)) {
            synchronized (this) {
                notify();
            }
            return;
        }
        Log.i(str, "onCharacteristicWrite : " + this.f23422c.toString());
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i8, int i9) {
        String str = C;
        Log.i(str, "gatt status : " + i8 + " newState : " + i9);
        synchronized (this) {
            this.f23444z = false;
            if (this.f23443y) {
                k();
                notifyAll();
            } else if (i8 == 0 && i9 == 2) {
                bluetoothGatt.requestConnectionPriority(1);
                bluetoothGatt.discoverServices();
                C();
            } else {
                this.f23426g = i9;
                synchronized (this.A) {
                    this.A.notifyAll();
                }
                D();
            }
        }
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onMtuChanged(BluetoothGatt bluetoothGatt, int i8, int i9) {
        if (i9 == 0) {
            this.f23426g = 2;
            this.f23427h = i8 - 4;
            String str = C;
            Log.i(str, "Mtu change : " + this.f23427h);
            synchronized (this) {
                Log.i(str, "NOTIFY : prepareCompleted");
                E.post(new g(this));
            }
            if (this.f23440v) {
                this.f23440v = false;
            }
        }
    }

    @Override // android.bluetooth.BluetoothGattCallback
    public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i8) {
        String str;
        String str2;
        synchronized (this) {
            if (i8 == 0) {
                BluetoothGattService service = bluetoothGatt.getService(a3.a.f69a);
                if (service != null) {
                    this.f23422c = service.getCharacteristic(a3.a.f70b);
                    BluetoothGattCharacteristic characteristic = service.getCharacteristic(a3.a.f71c);
                    this.f23423d = characteristic;
                    if (characteristic != null) {
                        bluetoothGatt.setCharacteristicNotification(characteristic, true);
                    }
                    if (bluetoothGatt.requestMtu(this.f23427h)) {
                        str = C;
                        str2 = "Request mtu succeed.";
                    } else {
                        str = C;
                        str2 = "Request mtu failure.";
                    }
                    Log.e(str, str2);
                }
            }
        }
    }

    public boolean r() {
        return this.f23426g == 2;
    }
}
