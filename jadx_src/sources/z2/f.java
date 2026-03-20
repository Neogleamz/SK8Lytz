package z2;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import c3.b;
import c3.c;
import com.example.blelibrary.protocol.layer.LowerTransportLayerDecoder;
import com.example.blelibrary.protocol.layer.LowerTransportLayerEncoder;
import com.example.blelibrary.protocol.layer.UpperTransportLayer;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.gson.e;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f implements com.example.blelibrary.client.a {

    /* renamed from: r  reason: collision with root package name */
    private static final String f24529r = "z2.f";

    /* renamed from: b  reason: collision with root package name */
    private Context f24531b;

    /* renamed from: c  reason: collision with root package name */
    private BluetoothDevice f24532c;

    /* renamed from: e  reason: collision with root package name */
    private BluetoothGatt f24534e;

    /* renamed from: f  reason: collision with root package name */
    private BluetoothGattCharacteristic f24535f;

    /* renamed from: g  reason: collision with root package name */
    private BluetoothGattCharacteristic f24536g;

    /* renamed from: j  reason: collision with root package name */
    private y2.a f24539j;

    /* renamed from: n  reason: collision with root package name */
    private c<Boolean> f24543n;

    /* renamed from: a  reason: collision with root package name */
    private final e f24530a = new e();

    /* renamed from: d  reason: collision with root package name */
    private Handler f24533d = new Handler();

    /* renamed from: h  reason: collision with root package name */
    private int f24537h = RecognitionOptions.QR_CODE;

    /* renamed from: i  reason: collision with root package name */
    private byte f24538i = -1;

    /* renamed from: k  reason: collision with root package name */
    private Map<Integer, List<g>> f24540k = new HashMap();

    /* renamed from: l  reason: collision with root package name */
    private int f24541l = -1;

    /* renamed from: m  reason: collision with root package name */
    private final Object f24542m = new Object();

    /* renamed from: o  reason: collision with root package name */
    private boolean f24544o = false;

    /* renamed from: p  reason: collision with root package name */
    private Thread f24545p = null;
    private final BluetoothGattCallback q = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends BluetoothGattCallback {

        /* renamed from: a  reason: collision with root package name */
        private final LowerTransportLayerDecoder f24546a = new LowerTransportLayerDecoder();

        a() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void c(g gVar, UpperTransportLayer upperTransportLayer, int i8) {
            try {
                Type b9 = gVar.b();
                String str = new String(upperTransportLayer.getPayload(), StandardCharsets.UTF_8);
                String str2 = f.f24529r;
                Log.i(str2, " JSON " + str);
                gVar.a().a(i8, f.this.f24530a.m(str, b9), null);
            } catch (Throwable th) {
                th.printStackTrace();
                String str3 = f.f24529r;
                String message = th.getMessage();
                Objects.requireNonNull(message);
                Log.i(str3, message);
                gVar.a().a(i8, null, th);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void d() {
            if (f.this.f24539j != null) {
                f.this.f24539j.onConnected();
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            synchronized (f.this) {
                String str = f.f24529r;
                StringBuilder sb = new StringBuilder();
                sb.append("characteristic : ");
                UUID uuid = a3.a.f71c;
                sb.append(uuid);
                Log.i(str, sb.toString());
                if (bluetoothGattCharacteristic.getUuid().equals(uuid)) {
                    byte[] value = bluetoothGattCharacteristic.getValue();
                    String str2 = f.f24529r;
                    Log.i(str2, "characteristic : " + b.a(value, value.length));
                    UpperTransportLayer transport = this.f24546a.getTransport(value);
                    if (transport != null) {
                        String str3 = f.f24529r;
                        Log.i(str3, "layer " + transport.toString() + " hash code " + f.this);
                        byte cmdId = transport.getCmdId();
                        if (f.this.f24540k != null) {
                            List<g> list = (List) f.this.f24540k.get(Integer.valueOf(cmdId));
                            if (list == null) {
                                return;
                            }
                            for (g gVar : list) {
                                f.this.f24533d.post(new e(this, gVar, transport, cmdId));
                            }
                        }
                    }
                }
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i8) {
            String str = f.f24529r;
            Log.i(str, "write status : " + i8);
            if (bluetoothGattCharacteristic.getUuid().equals(f.this.f24535f.getUuid())) {
                f.this.f24543n.d(Boolean.TRUE);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i8, int i9) {
            String str;
            String str2;
            synchronized (f.this) {
                String str3 = f.f24529r;
                Log.i(str3, "status " + i8 + " newState " + i9);
                if (i8 == 0 && i9 == 2) {
                    f.this.f24537h = RecognitionOptions.QR_CODE;
                    if (bluetoothGatt.discoverServices()) {
                        str = f.f24529r;
                        str2 = "Discover services succeed.";
                    } else {
                        str = f.f24529r;
                        str2 = "Discover services failure.";
                    }
                    Log.i(str, str2);
                } else {
                    f.this.f24541l = i9;
                    f.this.notify();
                    f.this.y(c.a);
                }
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onMtuChanged(BluetoothGatt bluetoothGatt, int i8, int i9) {
            synchronized (f.this) {
                if (i9 == 0) {
                    f.this.f24537h = i8 - 4;
                    f.this.f24541l = 2;
                    f.this.f24533d.postDelayed(new d(this), 500L);
                } else {
                    f.this.y(c.a);
                }
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i8) {
            String str;
            String str2;
            synchronized (f.this) {
                BluetoothGattService service = bluetoothGatt.getService(a3.a.f69a);
                if (service != null) {
                    f.this.f24535f = service.getCharacteristic(a3.a.f70b);
                    f.this.f24536g = service.getCharacteristic(a3.a.f71c);
                    if (f.this.f24536g != null) {
                        bluetoothGatt.setCharacteristicNotification(f.this.f24536g, true);
                    }
                    if (bluetoothGatt.requestMtu(f.this.f24537h)) {
                        str = f.f24529r;
                        str2 = "Request mtu succeed.";
                    } else {
                        str = f.f24529r;
                        str2 = "Request mtu failure.";
                    }
                    Log.i(str, str2);
                }
            }
        }
    }

    public f(Context context, BluetoothDevice bluetoothDevice) {
        this.f24531b = context;
        this.f24532c = bluetoothDevice;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void w() {
        do {
            synchronized (this) {
                try {
                } catch (InterruptedException e8) {
                    e8.printStackTrace();
                }
                if (this.f24544o) {
                    return;
                }
                BluetoothGatt bluetoothGatt = this.f24534e;
                if (bluetoothGatt != null) {
                    bluetoothGatt.close();
                }
                Log.i(f24529r, "Connect");
                this.f24534e = Build.VERSION.SDK_INT >= 23 ? this.f24532c.connectGatt(this.f24531b, false, this.q, 2) : this.f24532c.connectGatt(this.f24531b, false, this.q);
                wait();
            }
        } while (!this.f24544o);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void x(androidx.core.util.a aVar) {
        synchronized (this) {
            y2.a aVar2 = this.f24539j;
            if (aVar2 != null) {
                aVar.accept(aVar2);
            }
        }
    }

    private void z(byte b9, Object obj, boolean z4) {
        UpperTransportLayer createUpper;
        synchronized (this) {
            if (!g()) {
                throw new RuntimeException("ble disconnected.");
            }
            if (this.f24535f != null) {
                if (z4) {
                    byte b10 = (byte) (this.f24538i + 1);
                    this.f24538i = b10;
                    createUpper = UpperTransportLayer.createUpper(false, false, b10, b9, (byte[]) obj);
                } else {
                    byte b11 = (byte) (this.f24538i + 1);
                    this.f24538i = b11;
                    createUpper = UpperTransportLayer.createUpper(false, false, b11, b9, this.f24530a.u(obj).getBytes(StandardCharsets.UTF_8));
                }
                for (byte[] bArr : LowerTransportLayerEncoder.generator(createUpper, this.f24537h)) {
                    String str = f24529r;
                    Log.i(str, "writer " + c3.a.g(bArr));
                    this.f24543n = new c<>();
                    this.f24535f.setValue(bArr);
                    if (this.f24534e.writeCharacteristic(this.f24535f)) {
                        Log.i(str, "write status true " + this.f24535f.hashCode());
                    } else {
                        c<Boolean> cVar = this.f24543n;
                        cVar.c(new RuntimeException("write status false " + this.f24535f.hashCode()));
                    }
                    this.f24543n.b(1000);
                }
            }
        }
    }

    @Override // com.example.blelibrary.client.a
    public void a(int i8, com.example.blelibrary.client.e eVar, Type type) {
        synchronized (this) {
            Map<Integer, List<g>> map = this.f24540k;
            if (map == null) {
                return;
            }
            List<g> list = map.get(Integer.valueOf(i8));
            if (list == null) {
                list = new LinkedList<>();
                this.f24540k.put(Integer.valueOf(i8), list);
            }
            list.add(new g(type, eVar));
        }
    }

    @Override // com.example.blelibrary.client.a
    public void b(int i8, byte[] bArr) {
        z((byte) i8, bArr, true);
    }

    @Override // com.example.blelibrary.client.a
    public void c(y2.a aVar) {
        synchronized (this) {
            this.f24539j = aVar;
        }
    }

    @Override // com.example.blelibrary.client.a
    public void d(int i8, Object obj) {
        z((byte) i8, obj, false);
    }

    @Override // com.example.blelibrary.client.a
    public void disconnect() {
        synchronized (this) {
            this.f24544o = true;
            if (this.f24534e != null) {
                Log.i(f24529r, "DISCONNECT");
                this.f24534e.close();
                this.f24534e.disconnect();
                this.f24534e = null;
            }
            if (this.f24539j != null) {
                this.f24539j = null;
            }
            Map<Integer, List<g>> map = this.f24540k;
            if (map != null) {
                map.clear();
                this.f24540k = null;
            }
            if (this.f24535f != null) {
                this.f24535f = null;
            }
            if (this.f24536g != null) {
                this.f24536g = null;
            }
        }
    }

    @Override // com.example.blelibrary.client.a
    public void e(int i8, com.example.blelibrary.client.e eVar) {
        synchronized (this) {
            Map<Integer, List<g>> map = this.f24540k;
            if (map == null) {
                return;
            }
            List<g> list = map.get(Integer.valueOf(i8));
            if (list == null) {
                return;
            }
            Iterator<g> it = list.iterator();
            while (it.hasNext()) {
                if (it.next().a().hashCode() == eVar.hashCode()) {
                    it.remove();
                }
            }
        }
    }

    @Override // com.example.blelibrary.client.a
    public void f(boolean z4) {
        synchronized (this) {
            if (this.f24545p != null) {
                return;
            }
            Thread thread = new Thread((Runnable) new z2.a(this));
            this.f24545p = thread;
            thread.start();
        }
    }

    @Override // com.example.blelibrary.client.a
    public boolean g() {
        return this.f24541l == 2;
    }

    public void y(androidx.core.util.a<y2.a> aVar) {
        this.f24533d.post(new b(this, aVar));
    }
}
