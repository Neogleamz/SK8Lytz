package n2;

import android.annotation.TargetApi;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.util.Log;
import android.util.SparseArray;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.libraries.barhopper.RecognitionOptions;
import io.flutter.plugin.common.i;
import io.flutter.plugin.common.j;
import io.flutter.plugin.common.l;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import yf.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h implements yf.a, j.c, l.d, zf.a {

    /* renamed from: b  reason: collision with root package name */
    private Context f21991b;

    /* renamed from: c  reason: collision with root package name */
    private j f21992c;

    /* renamed from: d  reason: collision with root package name */
    private BluetoothManager f21993d;

    /* renamed from: e  reason: collision with root package name */
    private BluetoothAdapter f21994e;

    /* renamed from: f  reason: collision with root package name */
    private a.b f21995f;

    /* renamed from: g  reason: collision with root package name */
    private zf.c f21996g;

    /* renamed from: t  reason: collision with root package name */
    private ScanCallback f22004t;

    /* renamed from: a  reason: collision with root package name */
    private f f21990a = f.DEBUG;

    /* renamed from: h  reason: collision with root package name */
    private final Map<String, BluetoothGatt> f21997h = new ConcurrentHashMap();

    /* renamed from: j  reason: collision with root package name */
    private final Map<String, Integer> f21998j = new ConcurrentHashMap();

    /* renamed from: k  reason: collision with root package name */
    private final Map<String, Boolean> f21999k = new ConcurrentHashMap();

    /* renamed from: l  reason: collision with root package name */
    private int f22000l = 1452;

    /* renamed from: m  reason: collision with root package name */
    private final Map<Integer, g> f22001m = new HashMap();

    /* renamed from: n  reason: collision with root package name */
    private final int f22002n = 1879842617;

    /* renamed from: p  reason: collision with root package name */
    private final BroadcastReceiver f22003p = new a();
    private final BroadcastReceiver q = new b();

    /* renamed from: w  reason: collision with root package name */
    private final BluetoothGattCallback f22005w = new d();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends BroadcastReceiver {
        a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null || !"android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                return;
            }
            int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
            h hVar = h.this;
            f fVar = f.DEBUG;
            hVar.g0(fVar, "[FBP-Android] OnAdapterStateChanged: " + h.v(intExtra));
            if (intExtra == 13 || intExtra == 10) {
                h.this.M(false);
            }
            HashMap hashMap = new HashMap();
            hashMap.put("adapter_state", Integer.valueOf(h.y(intExtra)));
            h.this.X("OnAdapterStateChanged", hashMap);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends BroadcastReceiver {
        b() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null || !action.equals("android.bluetooth.device.action.BOND_STATE_CHANGED")) {
                return;
            }
            int intExtra = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", Integer.MIN_VALUE);
            int intExtra2 = intent.getIntExtra("android.bluetooth.device.extra.PREVIOUS_BOND_STATE", -1);
            h.this.g0(f.DEBUG, "[FBP-Android] OnBondStateChanged: " + h.J(intExtra) + " prev: " + h.J(intExtra2));
            String address = ((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress();
            HashMap hashMap = new HashMap();
            hashMap.put("remote_id", address);
            hashMap.put("bond_state", Integer.valueOf(h.E(intExtra)));
            boolean z4 = true;
            hashMap.put("bond_failed", Boolean.valueOf(intExtra == 10 && intExtra2 == 11));
            if (intExtra != 10 || intExtra2 != 12) {
                z4 = false;
            }
            hashMap.put("bond_lost", Boolean.valueOf(z4));
            h.this.X("OnBondStateChanged", hashMap);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends ScanCallback {
        c() {
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onBatchScanResults(List<ScanResult> list) {
            super.onBatchScanResults(list);
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onScanFailed(int i8) {
            h hVar = h.this;
            f fVar = f.ERROR;
            hVar.g0(fVar, "[FBP-Android] onScanFailed: " + h.h0(i8));
            super.onScanFailed(i8);
            HashMap hashMap = new HashMap();
            hashMap.put("success", 0);
            hashMap.put("error_code", Integer.valueOf(i8));
            hashMap.put("error_string", h.h0(i8));
            HashMap hashMap2 = new HashMap();
            hashMap2.put("failed", hashMap);
            h.this.X("OnScanResponse", hashMap2);
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onScanResult(int i8, ScanResult scanResult) {
            h.this.g0(f.VERBOSE, "[FBP-Android] onScanResult");
            super.onScanResult(i8, scanResult);
            HashMap<String, Object> I = h.this.I(scanResult.getDevice(), scanResult);
            HashMap hashMap = new HashMap();
            hashMap.put("result", I);
            h.this.X("OnScanResponse", hashMap);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d extends BluetoothGattCallback {
        d() {
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            h hVar = h.this;
            f fVar = f.DEBUG;
            hVar.g0(fVar, "[FBP-Android] onCharacteristicChanged: uuid: " + h.this.i0(bluetoothGattCharacteristic.getUuid()));
            C0190h U = h.U(bluetoothGatt, bluetoothGattCharacteristic);
            HashMap hashMap = new HashMap();
            hashMap.put("remote_id", bluetoothGatt.getDevice().getAddress());
            hashMap.put("service_uuid", h.this.i0(U.f22020a));
            UUID uuid = U.f22021b;
            hashMap.put("secondary_service_uuid", uuid != null ? h.this.i0(uuid) : null);
            hashMap.put("characteristic_uuid", h.this.i0(bluetoothGattCharacteristic.getUuid()));
            hashMap.put("value", h.K(bluetoothGattCharacteristic.getValue()));
            hashMap.put("success", 1);
            hashMap.put("error_code", 0);
            hashMap.put("error_string", h.O(0));
            h.this.X("OnCharacteristicReceived", hashMap);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i8) {
            h hVar = h.this;
            f fVar = f.DEBUG;
            hVar.g0(fVar, "[FBP-Android] onCharacteristicRead: uuid: " + h.this.i0(bluetoothGattCharacteristic.getUuid()) + " status: " + i8);
            C0190h U = h.U(bluetoothGatt, bluetoothGattCharacteristic);
            HashMap hashMap = new HashMap();
            hashMap.put("remote_id", bluetoothGatt.getDevice().getAddress());
            hashMap.put("service_uuid", h.this.i0(U.f22020a));
            UUID uuid = U.f22021b;
            hashMap.put("secondary_service_uuid", uuid != null ? h.this.i0(uuid) : null);
            hashMap.put("characteristic_uuid", h.this.i0(bluetoothGattCharacteristic.getUuid()));
            hashMap.put("value", h.K(bluetoothGattCharacteristic.getValue()));
            hashMap.put("success", Integer.valueOf(i8 == 0 ? 1 : 0));
            hashMap.put("error_code", Integer.valueOf(i8));
            hashMap.put("error_string", h.O(i8));
            h.this.X("OnCharacteristicReceived", hashMap);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i8) {
            h hVar = h.this;
            f fVar = f.DEBUG;
            hVar.g0(fVar, "[FBP-Android] onCharacteristicWrite: uuid: " + h.this.i0(bluetoothGattCharacteristic.getUuid()) + " status: " + i8);
            C0190h U = h.U(bluetoothGatt, bluetoothGattCharacteristic);
            HashMap hashMap = new HashMap();
            hashMap.put("remote_id", bluetoothGatt.getDevice().getAddress());
            hashMap.put("service_uuid", h.this.i0(U.f22020a));
            UUID uuid = U.f22021b;
            hashMap.put("secondary_service_uuid", uuid != null ? h.this.i0(uuid) : null);
            hashMap.put("characteristic_uuid", h.this.i0(bluetoothGattCharacteristic.getUuid()));
            hashMap.put("success", Integer.valueOf(i8 == 0 ? 1 : 0));
            hashMap.put("error_code", Integer.valueOf(i8));
            hashMap.put("error_string", h.O(i8));
            h.this.X("OnCharacteristicWritten", hashMap);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i8, int i9) {
            h hVar = h.this;
            f fVar = f.DEBUG;
            hVar.g0(fVar, "[FBP-Android] onConnectionStateChange: status: " + i8 + " (" + h.V(i8) + ") newState: " + h.L(i9));
            if (i9 == 2 || i9 == 0) {
                String address = bluetoothGatt.getDevice().getAddress();
                if (i9 == 2) {
                    h.this.f21997h.put(address, bluetoothGatt);
                    h.this.f21998j.put(address, 23);
                }
                if (i9 == 0) {
                    h.this.f21997h.remove(address);
                    if (h.this.f21999k.get(address) == null || !((Boolean) h.this.f21999k.get(address)).booleanValue()) {
                        bluetoothGatt.close();
                    } else {
                        h.this.g0(fVar, "[FBP-Android] autoconnect is true. skipping gatt.close()");
                    }
                }
                HashMap hashMap = new HashMap();
                hashMap.put("remote_id", address);
                hashMap.put("connection_state", Integer.valueOf(h.H(i9)));
                hashMap.put("disconnect_reason_code", Integer.valueOf(i8));
                hashMap.put("disconnect_reason_string", h.V(i8));
                h.this.X("OnConnectionStateChanged", hashMap);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onDescriptorRead(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i8) {
            h hVar = h.this;
            f fVar = f.DEBUG;
            hVar.g0(fVar, "[FBP-Android] onDescriptorRead: uuid: " + h.this.i0(bluetoothGattDescriptor.getUuid()) + " status: " + i8);
            C0190h U = h.U(bluetoothGatt, bluetoothGattDescriptor.getCharacteristic());
            byte[] value = bluetoothGattDescriptor.getValue();
            HashMap hashMap = new HashMap();
            hashMap.put("remote_id", bluetoothGatt.getDevice().getAddress());
            hashMap.put("service_uuid", h.this.i0(U.f22020a));
            UUID uuid = U.f22021b;
            hashMap.put("secondary_service_uuid", uuid != null ? h.this.i0(uuid) : null);
            hashMap.put("characteristic_uuid", h.this.i0(bluetoothGattDescriptor.getCharacteristic().getUuid()));
            hashMap.put("descriptor_uuid", h.this.i0(bluetoothGattDescriptor.getUuid()));
            hashMap.put("value", h.K(value));
            hashMap.put("success", Integer.valueOf(i8 == 0 ? 1 : 0));
            hashMap.put("error_code", Integer.valueOf(i8));
            hashMap.put("error_string", h.O(i8));
            h.this.X("OnDescriptorRead", hashMap);
        }

        @TargetApi(33)
        public void onDescriptorRead(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i8, byte[] bArr) {
            h hVar = h.this;
            f fVar = f.DEBUG;
            hVar.g0(fVar, "[FBP-Android] onDescriptorRead: uuid: " + h.this.i0(bluetoothGattDescriptor.getUuid()) + " status: " + i8);
            C0190h U = h.U(bluetoothGatt, bluetoothGattDescriptor.getCharacteristic());
            HashMap hashMap = new HashMap();
            hashMap.put("remote_id", bluetoothGatt.getDevice().getAddress());
            hashMap.put("service_uuid", h.this.i0(U.f22020a));
            UUID uuid = U.f22021b;
            hashMap.put("secondary_service_uuid", uuid != null ? h.this.i0(uuid) : null);
            hashMap.put("characteristic_uuid", h.this.i0(bluetoothGattDescriptor.getCharacteristic().getUuid()));
            hashMap.put("descriptor_uuid", h.this.i0(bluetoothGattDescriptor.getUuid()));
            hashMap.put("value", h.K(bArr));
            hashMap.put("success", Integer.valueOf(i8 == 0 ? 1 : 0));
            hashMap.put("error_code", Integer.valueOf(i8));
            hashMap.put("error_string", h.O(i8));
            h.this.X("OnDescriptorRead", hashMap);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onDescriptorWrite(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i8) {
            h hVar = h.this;
            f fVar = f.DEBUG;
            hVar.g0(fVar, "[FBP-Android] onDescriptorWrite: uuid: " + h.this.i0(bluetoothGattDescriptor.getUuid()) + " status: " + i8);
            C0190h U = h.U(bluetoothGatt, bluetoothGattDescriptor.getCharacteristic());
            HashMap hashMap = new HashMap();
            hashMap.put("remote_id", bluetoothGatt.getDevice().getAddress());
            hashMap.put("service_uuid", h.this.i0(U.f22020a));
            UUID uuid = U.f22021b;
            hashMap.put("secondary_service_uuid", uuid != null ? h.this.i0(uuid) : null);
            hashMap.put("characteristic_uuid", h.this.i0(bluetoothGattDescriptor.getCharacteristic().getUuid()));
            hashMap.put("descriptor_uuid", h.this.i0(bluetoothGattDescriptor.getUuid()));
            hashMap.put("success", Integer.valueOf(i8 == 0 ? 1 : 0));
            hashMap.put("error_code", Integer.valueOf(i8));
            hashMap.put("error_string", h.O(i8));
            h.this.X("OnDescriptorWrite", hashMap);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onMtuChanged(BluetoothGatt bluetoothGatt, int i8, int i9) {
            h hVar = h.this;
            f fVar = f.DEBUG;
            hVar.g0(fVar, "[FBP-Android] onMtuChanged: mtu: " + i8 + " status: " + i9);
            String address = bluetoothGatt.getDevice().getAddress();
            h.this.f21998j.put(address, Integer.valueOf(i8));
            HashMap hashMap = new HashMap();
            hashMap.put("remote_id", address);
            hashMap.put("mtu", Integer.valueOf(i8));
            hashMap.put("success", Integer.valueOf(i9 == 0 ? 1 : 0));
            hashMap.put("error_code", Integer.valueOf(i9));
            hashMap.put("error_string", h.O(i9));
            h.this.X("OnMtuChanged", hashMap);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onReadRemoteRssi(BluetoothGatt bluetoothGatt, int i8, int i9) {
            h hVar = h.this;
            f fVar = f.DEBUG;
            hVar.g0(fVar, "[FBP-Android] onReadRemoteRssi: rssi: " + i8 + " status: " + i9);
            HashMap hashMap = new HashMap();
            hashMap.put("remote_id", bluetoothGatt.getDevice().getAddress());
            hashMap.put("rssi", Integer.valueOf(i8));
            hashMap.put("success", Integer.valueOf(i9 == 0 ? 1 : 0));
            hashMap.put("error_code", Integer.valueOf(i9));
            hashMap.put("error_string", h.O(i9));
            h.this.X("OnReadRssiResult", hashMap);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onReliableWriteCompleted(BluetoothGatt bluetoothGatt, int i8) {
            h hVar = h.this;
            f fVar = f.DEBUG;
            hVar.g0(fVar, "[FBP-Android] onReliableWriteCompleted: status: " + i8);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i8) {
            h hVar = h.this;
            f fVar = f.DEBUG;
            hVar.g0(fVar, "[FBP-Android] onServicesDiscovered: count: " + bluetoothGatt.getServices().size() + " status: " + i8);
            ArrayList arrayList = new ArrayList();
            for (BluetoothGattService bluetoothGattService : bluetoothGatt.getServices()) {
                arrayList.add(h.this.D(bluetoothGatt.getDevice(), bluetoothGattService, bluetoothGatt));
            }
            HashMap hashMap = new HashMap();
            hashMap.put("remote_id", bluetoothGatt.getDevice().getAddress());
            hashMap.put("services", arrayList);
            hashMap.put("success", Integer.valueOf(i8 == 0 ? 1 : 0));
            hashMap.put("error_code", Integer.valueOf(i8));
            hashMap.put("error_string", h.O(i8));
            h.this.X("OnDiscoverServicesResult", hashMap);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e {

        /* renamed from: a  reason: collision with root package name */
        public BluetoothGattCharacteristic f22010a;

        /* renamed from: b  reason: collision with root package name */
        public String f22011b;

        public e(BluetoothGattCharacteristic bluetoothGattCharacteristic, String str) {
            this.f22010a = bluetoothGattCharacteristic;
            this.f22011b = str;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum f {
        NONE,
        ERROR,
        WARNING,
        INFO,
        DEBUG,
        VERBOSE
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface g {
        void a(boolean z4, String str);
    }

    /* renamed from: n2.h$h  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0190h {

        /* renamed from: a  reason: collision with root package name */
        public UUID f22020a;

        /* renamed from: b  reason: collision with root package name */
        public UUID f22021b;
    }

    static int E(int i8) {
        if (i8 != 11) {
            return i8 != 12 ? 0 : 2;
        }
        return 1;
    }

    static int G(int i8) {
        if (i8 != 0) {
            return i8 != 1 ? 2 : 1;
        }
        return 0;
    }

    static int H(int i8) {
        return i8 != 2 ? 0 : 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String J(int i8) {
        switch (i8) {
            case 10:
                return "bond-none";
            case 11:
                return "bonding";
            case 12:
                return "bonded";
            default:
                return "UNKNOWN_BOND_STATE (" + i8 + ")";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String K(byte[] bArr) {
        if (bArr == null) {
            return BuildConfig.FLAVOR;
        }
        StringBuilder sb = new StringBuilder();
        int length = bArr.length;
        for (int i8 = 0; i8 < length; i8++) {
            sb.append(String.format("%02x", Byte.valueOf(bArr[i8])));
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String L(int i8) {
        if (i8 != 0) {
            if (i8 != 1) {
                if (i8 != 2) {
                    if (i8 != 3) {
                        return "UNKNOWN_CONNECTION_STATE (" + i8 + ")";
                    }
                    return "disconnecting";
                }
                return "connected";
            }
            return "connecting";
        }
        return "disconnected";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void M(boolean z4) {
        Log.d("[FBP-Android]", "[FBP-Android] disconnectAllDevices");
        for (BluetoothGatt bluetoothGatt : this.f21997h.values()) {
            if (bluetoothGatt != null) {
                String address = bluetoothGatt.getDevice().getAddress();
                Log.d("[FBP-Android]", "[FBP-Android] calling disconnect: " + address);
                bluetoothGatt.disconnect();
            }
        }
        if (z4) {
            Log.d("[FBP-Android]", "[FBP-Android] closeAllDevices");
            for (BluetoothGatt bluetoothGatt2 : this.f21997h.values()) {
                if (bluetoothGatt2 != null) {
                    String address2 = bluetoothGatt2.getDevice().getAddress();
                    Log.d("[FBP-Android]", "[FBP-Android] calling close: " + address2);
                    bluetoothGatt2.close();
                }
            }
        }
        this.f21997h.clear();
        this.f21998j.clear();
    }

    private void N(List<String> list, g gVar) {
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            if (str != null && androidx.core.content.a.a(this.f21991b, str) != 0) {
                arrayList.add(str);
            }
        }
        if (arrayList.isEmpty()) {
            gVar.a(true, null);
        } else {
            w(arrayList, gVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String O(int i8) {
        if (i8 != 0) {
            if (i8 != 13) {
                if (i8 != 15) {
                    if (i8 != 143) {
                        if (i8 != 257) {
                            if (i8 != 2) {
                                if (i8 != 3) {
                                    if (i8 != 5) {
                                        if (i8 != 6) {
                                            if (i8 != 7) {
                                                if (i8 != 8) {
                                                    return "UNKNOWN_GATT_ERROR (" + i8 + ")";
                                                }
                                                return "GATT_INSUFFICIENT_AUTHORIZATION";
                                            }
                                            return "GATT_INVALID_OFFSET";
                                        }
                                        return "GATT_REQUEST_NOT_SUPPORTED";
                                    }
                                    return "GATT_INSUFFICIENT_AUTHENTICATION";
                                }
                                return "GATT_WRITE_NOT_PERMITTED";
                            }
                            return "GATT_READ_NOT_PERMITTED";
                        }
                        return "GATT_FAILURE";
                    }
                    return "GATT_CONNECTION_CONGESTED";
                }
                return "GATT_INSUFFICIENT_ENCRYPTION";
            }
            return "GATT_INVALID_ATTRIBUTE_LENGTH";
        }
        return "GATT_SUCCESS";
    }

    private BluetoothGattCharacteristic P(String str, List<BluetoothGattCharacteristic> list) {
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : list) {
            if (i0(bluetoothGattCharacteristic.getUuid()).equals(str)) {
                return bluetoothGattCharacteristic;
            }
        }
        return null;
    }

    private BluetoothGattDescriptor Q(String str, List<BluetoothGattDescriptor> list) {
        for (BluetoothGattDescriptor bluetoothGattDescriptor : list) {
            if (i0(bluetoothGattDescriptor.getUuid()).equals(str)) {
                return bluetoothGattDescriptor;
            }
        }
        return null;
    }

    private int R(String str, int i8, boolean z4) {
        if (i8 == 1 || !z4) {
            Integer num = this.f21998j.get(str);
            if (num == null) {
                num = 23;
            }
            return Math.min(num.intValue() - 3, (int) RecognitionOptions.UPC_A);
        }
        return RecognitionOptions.UPC_A;
    }

    private ScanCallback S() {
        if (this.f22004t == null) {
            this.f22004t = new c();
        }
        return this.f22004t;
    }

    private BluetoothGattService T(String str, List<BluetoothGattService> list) {
        for (BluetoothGattService bluetoothGattService : list) {
            if (i0(bluetoothGattService.getUuid()).equals(str)) {
                return bluetoothGattService;
            }
        }
        return null;
    }

    static C0190h U(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        C0190h c0190h = new C0190h();
        BluetoothGattService service = bluetoothGattCharacteristic.getService();
        if (service.getType() == 0) {
            c0190h.f22020a = service.getUuid();
            return c0190h;
        }
        Iterator<BluetoothGattService> it = bluetoothGatt.getServices().iterator();
        loop0: while (true) {
            if (!it.hasNext()) {
                break;
            }
            BluetoothGattService next = it.next();
            for (BluetoothGattService bluetoothGattService : next.getIncludedServices()) {
                if (bluetoothGattService.getUuid().equals(service.getUuid())) {
                    c0190h.f22020a = next.getUuid();
                    c0190h.f22021b = bluetoothGattService.getUuid();
                    break loop0;
                }
            }
        }
        return c0190h;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String V(int i8) {
        if (i8 != 133) {
            if (i8 != 257) {
                switch (i8) {
                    case 0:
                        return "SUCCESS";
                    case 1:
                        return "UNKNOWN_COMMAND";
                    case 2:
                        return "UNKNOWN_CONNECTION_IDENTIFIER";
                    case 3:
                        return "HARDWARE_FAILURE";
                    case 4:
                        return "PAGE_TIMEOUT";
                    case 5:
                        return "AUTHENTICATION_FAILURE";
                    case 6:
                        return "PIN_OR_KEY_MISSING";
                    case 7:
                        return "MEMORY_FULL";
                    case 8:
                        return "CONNECTION_TIMEOUT";
                    case 9:
                        return "CONNECTION_LIMIT_EXCEEDED";
                    case 10:
                        return "MAX_NUM_OF_CONNECTIONS_EXCEEDED";
                    case 11:
                        return "CONNECTION_ALREADY_EXISTS";
                    case 12:
                        return "COMMAND_DISALLOWED";
                    case 13:
                        return "CONNECTION_REJECTED_LIMITED_RESOURCES";
                    case 14:
                        return "CONNECTION_REJECTED_SECURITY_REASONS";
                    case 15:
                        return "CONNECTION_REJECTED_UNACCEPTABLE_MAC_ADDRESS";
                    case 16:
                        return "CONNECTION_ACCEPT_TIMEOUT_EXCEEDED";
                    case 17:
                        return "UNSUPPORTED_PARAMETER_VALUE";
                    case 18:
                        return "INVALID_COMMAND_PARAMETERS";
                    case 19:
                        return "REMOTE_USER_TERMINATED_CONNECTION";
                    case 20:
                        return "REMOTE_DEVICE_TERMINATED_CONNECTION_LOW_RESOURCES";
                    case 21:
                        return "REMOTE_DEVICE_TERMINATED_CONNECTION_POWER_OFF";
                    case 22:
                        return "CONNECTION_TERMINATED_BY_LOCAL_HOST";
                    case 23:
                        return "REPEATED_ATTEMPTS";
                    case 24:
                        return "PAIRING_NOT_ALLOWED";
                    case 25:
                        return "UNKNOWN_LMP_PDU";
                    case 26:
                        return "UNSUPPORTED_REMOTE_FEATURE";
                    case 27:
                        return "SCO_OFFSET_REJECTED";
                    case 28:
                        return "SCO_INTERVAL_REJECTED";
                    case 29:
                        return "SCO_AIR_MODE_REJECTED";
                    case 30:
                        return "INVALID_LMP_OR_LL_PARAMETERS";
                    case 31:
                        return "UNSPECIFIED";
                    case 32:
                        return "UNSUPPORTED_LMP_OR_LL_PARAMETER_VALUE";
                    case 33:
                        return "ROLE_CHANGE_NOT_ALLOWED";
                    case 34:
                        return "LMP_OR_LL_RESPONSE_TIMEOUT";
                    case 35:
                        return "LMP_OR_LL_ERROR_TRANS_COLLISION";
                    case 36:
                        return "LMP_PDU_NOT_ALLOWED";
                    case 37:
                        return "ENCRYPTION_MODE_NOT_ACCEPTABLE";
                    case 38:
                        return "LINK_KEY_CANNOT_BE_EXCHANGED";
                    case 39:
                        return "REQUESTED_QOS_NOT_SUPPORTED";
                    case 40:
                        return "INSTANT_PASSED";
                    case 41:
                        return "PAIRING_WITH_UNIT_KEY_NOT_SUPPORTED";
                    case 42:
                        return "DIFFERENT_TRANSACTION_COLLISION";
                    case 43:
                        return "UNDEFINED_0x2B";
                    case 44:
                        return "QOS_UNACCEPTABLE_PARAMETER";
                    case 45:
                        return "QOS_REJECTED";
                    case 46:
                        return "CHANNEL_CLASSIFICATION_NOT_SUPPORTED";
                    case 47:
                        return "INSUFFICIENT_SECURITY";
                    case 48:
                        return "PARAMETER_OUT_OF_RANGE";
                    case 49:
                        return "UNDEFINED_0x31";
                    case 50:
                        return "ROLE_SWITCH_PENDING";
                    case 51:
                        return "UNDEFINED_0x33";
                    case 52:
                        return "RESERVED_SLOT_VIOLATION";
                    case 53:
                        return "ROLE_SWITCH_FAILED";
                    case 54:
                        return "INQUIRY_RESPONSE_TOO_LARGE";
                    case 55:
                        return "SECURE_SIMPLE_PAIRING_NOT_SUPPORTED";
                    case 56:
                        return "HOST_BUSY_PAIRING";
                    case 57:
                        return "CONNECTION_REJECTED_NO_SUITABLE_CHANNEL";
                    case 58:
                        return "CONTROLLER_BUSY";
                    case 59:
                        return "UNACCEPTABLE_CONNECTION_PARAMETERS";
                    case 60:
                        return "ADVERTISING_TIMEOUT";
                    case 61:
                        return "CONNECTION_TERMINATED_MIC_FAILURE";
                    case 62:
                        return "CONNECTION_FAILED_ESTABLISHMENT";
                    case 63:
                        return "MAC_CONNECTION_FAILED";
                    case 64:
                        return "COARSE_CLOCK_ADJUSTMENT_REJECTED";
                    case 65:
                        return "TYPE0_SUBMAP_NOT_DEFINED";
                    case 66:
                        return "UNKNOWN_ADVERTISING_IDENTIFIER";
                    case 67:
                        return "LIMIT_REACHED";
                    case 68:
                        return "OPERATION_CANCELLED_BY_HOST";
                    case 69:
                        return "PACKET_TOO_LONG";
                    default:
                        return "UNKNOWN_HCI_ERROR (" + i8 + ")";
                }
            }
            return "FAILURE_REGISTERING_CLIENT";
        }
        return "ANDROID_SPECIFIC_ERROR";
    }

    private static byte[] W(String str) {
        if (str == null) {
            return new byte[0];
        }
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        for (int i8 = 0; i8 < length; i8 += 2) {
            bArr[i8 / 2] = (byte) ((Character.digit(str.charAt(i8), 16) << 4) + Character.digit(str.charAt(i8 + 1), 16));
        }
        return bArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void X(String str, HashMap<String, Object> hashMap) {
        new Handler(Looper.getMainLooper()).post(new n2.a(this, str, hashMap));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void Y(g gVar, List list, boolean z4, String str) {
        this.f22001m.remove(Integer.valueOf(this.f22000l));
        if (z4) {
            w(list, gVar);
        } else {
            gVar.a(false, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void Z(String str, HashMap hashMap) {
        j jVar = this.f21992c;
        if (jVar != null) {
            jVar.c(str, hashMap);
            return;
        }
        Log.w("[FBP-Android]", "invokeMethodUIThread: tried to call method on closed channel: " + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void a0(j.d dVar, boolean z4, String str) {
        if (!this.f21994e.isEnabled()) {
            this.f21996g.getActivity().startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1879842617);
        }
        dVar.success(Boolean.TRUE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b0(j.d dVar, boolean z4, String str) {
        dVar.success(!this.f21994e.isEnabled() ? Boolean.TRUE : Boolean.valueOf(this.f21994e.disable()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void c0(j.d dVar, int i8, List list, List list2, boolean z4, String str) {
        if (!z4) {
            dVar.a("startScan", String.format("FlutterBluePlus requires %s permission", str), (Object) null);
            return;
        }
        BluetoothLeScanner bluetoothLeScanner = this.f21994e.getBluetoothLeScanner();
        if (bluetoothLeScanner == null) {
            dVar.a("startScan", String.format("getBluetoothLeScanner() is null. Is the Adapter on?", new Object[0]), (Object) null);
            return;
        }
        ScanSettings build = (Build.VERSION.SDK_INT >= 26 ? new ScanSettings.Builder().setPhy(255).setLegacy(false) : new ScanSettings.Builder()).setScanMode(i8).build();
        ArrayList arrayList = new ArrayList();
        for (int i9 = 0; i9 < list.size(); i9++) {
            arrayList.add(new ScanFilter.Builder().setDeviceAddress((String) list.get(i9)).build());
        }
        for (int i10 = 0; i10 < list2.size(); i10++) {
            arrayList.add(new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString((String) list2.get(i10))).build());
        }
        bluetoothLeScanner.startScan(arrayList, build, S());
        dVar.success((Object) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d0(j.d dVar, boolean z4, String str) {
        if (!z4) {
            dVar.a("getConnectedSystemDevices", String.format("FlutterBluePlus requires %s permission", str), (Object) null);
            return;
        }
        List<BluetoothDevice> connectedDevices = this.f21993d.getConnectedDevices(7);
        ArrayList arrayList = new ArrayList();
        for (BluetoothDevice bluetoothDevice : connectedDevices) {
            arrayList.add(C(bluetoothDevice));
        }
        HashMap hashMap = new HashMap();
        hashMap.put("devices", arrayList);
        dVar.success(hashMap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void e0(j.d dVar, i iVar, boolean z4, String str) {
        if (!z4) {
            dVar.a("connect", String.format("FlutterBluePlus requires %s for new connection", str), (Object) null);
            return;
        }
        HashMap hashMap = (HashMap) iVar.b();
        String str2 = (String) hashMap.get("remote_id");
        boolean z8 = ((Integer) hashMap.get("auto_connect")).intValue() != 0;
        this.f21999k.put(str2, Boolean.valueOf(z8));
        if (this.f21997h.get(str2) != null) {
            g0(f.DEBUG, "[FBP-Android] already connected");
            dVar.success(1);
            return;
        }
        BluetoothDevice remoteDevice = this.f21994e.getRemoteDevice(str2);
        if ((Build.VERSION.SDK_INT >= 23 ? remoteDevice.connectGatt(this.f21991b, z8, this.f22005w, 2) : remoteDevice.connectGatt(this.f21991b, z8, this.f22005w)) == null) {
            dVar.a("connect", String.format("device.connectGatt returned null", new Object[0]), (Object) null);
        } else {
            dVar.success(0);
        }
    }

    private e f0(BluetoothGatt bluetoothGatt, String str, String str2, String str3) {
        BluetoothGattService bluetoothGattService;
        BluetoothGattService T = T(str, bluetoothGatt.getServices());
        if (T == null) {
            return new e(null, "service not found '" + str + "'");
        }
        if (str2 == null || str2.length() <= 0) {
            bluetoothGattService = null;
        } else {
            bluetoothGattService = T(str, T.getIncludedServices());
            if (bluetoothGattService == null) {
                return new e(null, "secondaryService not found '" + str2 + "'");
            }
        }
        if (bluetoothGattService != null) {
            T = bluetoothGattService;
        }
        BluetoothGattCharacteristic P = P(str3, T.getCharacteristics());
        if (P == null) {
            return new e(null, "characteristic not found in service (chr: '" + str3 + "' svc: '" + str + "')");
        }
        return new e(P, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g0(f fVar, String str) {
        if (fVar.ordinal() <= this.f21990a.ordinal()) {
            Log.d("[FBP-Android]", str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String h0(int i8) {
        switch (i8) {
            case 1:
                return "SCAN_FAILED_ALREADY_STARTED";
            case 2:
                return "SCAN_FAILED_APPLICATION_REGISTRATION_FAILED";
            case 3:
                return "SCAN_FAILED_INTERNAL_ERROR";
            case 4:
                return "SCAN_FAILED_FEATURE_UNSUPPORTED";
            case 5:
                return "SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES";
            case 6:
                return "SCAN_FAILED_SCANNING_TOO_FREQUENTLY";
            default:
                return "UNKNOWN_SCAN_ERROR (" + i8 + ")";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String v(int i8) {
        switch (i8) {
            case 10:
                return "off";
            case 11:
                return "turningOn";
            case 12:
                return "on";
            case 13:
                return "turningOff";
            default:
                return "UNKNOWN_ADAPTER_STATE (" + i8 + ")";
        }
    }

    private void w(List<String> list, g gVar) {
        if (list.isEmpty()) {
            gVar.a(true, null);
            return;
        }
        this.f22001m.put(Integer.valueOf(this.f22000l), new n2.g(this, gVar, list));
        androidx.core.app.b.t(this.f21996g.getActivity(), new String[]{list.remove(0)}, this.f22000l);
        this.f22000l++;
    }

    private static String x(int i8) {
        if (i8 != 0) {
            if (i8 != 1) {
                if (i8 != 2) {
                    if (i8 != 3) {
                        if (i8 != 6) {
                            if (i8 != Integer.MAX_VALUE) {
                                if (i8 != 200) {
                                    if (i8 != 201) {
                                        switch (i8) {
                                            case 9:
                                                return "ERROR_PROFILE_SERVICE_NOT_BOUND";
                                            case 10:
                                                return "FEATURE_SUPPORTED";
                                            case 11:
                                                return "FEATURE_NOT_SUPPORTED";
                                            default:
                                                return "UNKNOWN_BLE_ERROR (" + i8 + ")";
                                        }
                                    }
                                    return "ERROR_GATT_WRITE_REQUEST_BUSY";
                                }
                                return "ERROR_GATT_WRITE_NOT_ALLOWED";
                            }
                            return "ERROR_UNKNOWN";
                        }
                        return "ERROR_MISSING_BLUETOOTH_CONNECT_PERMISSION";
                    }
                    return "ERROR_DEVICE_NOT_BONDED";
                }
                return "ERROR_BLUETOOTH_NOT_ALLOWED";
            }
            return "ERROR_BLUETOOTH_NOT_ENABLED";
        }
        return "SUCCESS";
    }

    static int y(int i8) {
        switch (i8) {
            case 10:
                return 6;
            case 11:
                return 3;
            case 12:
                return 4;
            case 13:
                return 5;
            default:
                return 0;
        }
    }

    HashMap<String, Object> A(BluetoothDevice bluetoothDevice, BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt bluetoothGatt) {
        C0190h U = U(bluetoothGatt, bluetoothGattCharacteristic);
        ArrayList arrayList = new ArrayList();
        for (BluetoothGattDescriptor bluetoothGattDescriptor : bluetoothGattCharacteristic.getDescriptors()) {
            arrayList.add(B(bluetoothDevice, bluetoothGattDescriptor));
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("remote_id", bluetoothDevice.getAddress());
        hashMap.put("service_uuid", i0(U.f22020a));
        UUID uuid = U.f22021b;
        hashMap.put("secondary_service_uuid", uuid != null ? i0(uuid) : null);
        hashMap.put("characteristic_uuid", i0(bluetoothGattCharacteristic.getUuid()));
        hashMap.put("descriptors", arrayList);
        hashMap.put("properties", F(bluetoothGattCharacteristic.getProperties()));
        return hashMap;
    }

    HashMap<String, Object> B(BluetoothDevice bluetoothDevice, BluetoothGattDescriptor bluetoothGattDescriptor) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("remote_id", bluetoothDevice.getAddress());
        hashMap.put("descriptor_uuid", i0(bluetoothGattDescriptor.getUuid()));
        hashMap.put("characteristic_uuid", i0(bluetoothGattDescriptor.getCharacteristic().getUuid()));
        hashMap.put("service_uuid", i0(bluetoothGattDescriptor.getCharacteristic().getService().getUuid()));
        return hashMap;
    }

    HashMap<String, Object> C(BluetoothDevice bluetoothDevice) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("remote_id", bluetoothDevice.getAddress());
        if (bluetoothDevice.getName() != null) {
            hashMap.put("local_name", bluetoothDevice.getName());
        }
        hashMap.put("type", Integer.valueOf(bluetoothDevice.getType()));
        return hashMap;
    }

    HashMap<String, Object> D(BluetoothDevice bluetoothDevice, BluetoothGattService bluetoothGattService, BluetoothGatt bluetoothGatt) {
        ArrayList arrayList = new ArrayList();
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
            arrayList.add(A(bluetoothDevice, bluetoothGattCharacteristic, bluetoothGatt));
        }
        ArrayList arrayList2 = new ArrayList();
        for (BluetoothGattService bluetoothGattService2 : bluetoothGattService.getIncludedServices()) {
            if (!bluetoothGattService2.getUuid().equals(bluetoothGattService.getUuid())) {
                arrayList2.add(D(bluetoothDevice, bluetoothGattService2, bluetoothGatt));
            }
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("remote_id", bluetoothDevice.getAddress());
        hashMap.put("service_uuid", i0(bluetoothGattService.getUuid()));
        hashMap.put("is_primary", Integer.valueOf(bluetoothGattService.getType() == 0 ? 1 : 0));
        hashMap.put("characteristics", arrayList);
        hashMap.put("included_services", arrayList2);
        return hashMap;
    }

    HashMap<String, Object> F(int i8) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("broadcast", Integer.valueOf((i8 & 1) != 0 ? 1 : 0));
        hashMap.put("read", Integer.valueOf((i8 & 2) != 0 ? 1 : 0));
        hashMap.put("write_without_response", Integer.valueOf((i8 & 4) != 0 ? 1 : 0));
        hashMap.put("write", Integer.valueOf((i8 & 8) != 0 ? 1 : 0));
        hashMap.put("notify", Integer.valueOf((i8 & 16) != 0 ? 1 : 0));
        hashMap.put("indicate", Integer.valueOf((i8 & 32) != 0 ? 1 : 0));
        hashMap.put("authenticated_signed_writes", Integer.valueOf((i8 & 64) != 0 ? 1 : 0));
        hashMap.put("extended_properties", Integer.valueOf((i8 & RecognitionOptions.ITF) != 0 ? 1 : 0));
        hashMap.put("notify_encryption_required", Integer.valueOf((i8 & RecognitionOptions.QR_CODE) != 0 ? 1 : 0));
        hashMap.put("indicate_encryption_required", Integer.valueOf((i8 & RecognitionOptions.UPC_A) != 0 ? 1 : 0));
        return hashMap;
    }

    HashMap<String, Object> I(BluetoothDevice bluetoothDevice, ScanResult scanResult) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("device", C(bluetoothDevice));
        hashMap.put("rssi", Integer.valueOf(scanResult.getRssi()));
        hashMap.put("advertisement_data", z(scanResult));
        return hashMap;
    }

    public String i0(UUID uuid) {
        String format;
        String uuid2 = uuid.toString();
        if (uuid2.length() == 4) {
            format = String.format("0000%s-0000-1000-8000-00805F9B34FB", uuid2);
        } else if (uuid2.length() != 8) {
            return uuid2.toLowerCase();
        } else {
            format = String.format("%s-0000-1000-8000-00805F9B34FB", uuid2);
        }
        return format.toLowerCase();
    }

    public void onAttachedToActivity(zf.c cVar) {
        Log.d("[FBP-Android]", "onAttachedToActivity");
        this.f21996g = cVar;
        cVar.b(this);
    }

    public void onAttachedToEngine(a.b bVar) {
        Log.d("[FBP-Android]", "onAttachedToEngine");
        this.f21995f = bVar;
        this.f21991b = (Application) bVar.a();
        j jVar = new j(bVar.b(), "flutter_blue_plus/methods");
        this.f21992c = jVar;
        jVar.e(this);
        this.f21991b.registerReceiver(this.f22003p, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
        this.f21991b.registerReceiver(this.q, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
    }

    public void onDetachedFromActivity() {
        Log.d("[FBP-Android]", "onDetachedFromActivity");
        this.f21996g.e(this);
        this.f21996g = null;
    }

    public void onDetachedFromActivityForConfigChanges() {
        Log.d("[FBP-Android]", "onDetachedFromActivityForConfigChanges");
        onDetachedFromActivity();
    }

    public void onDetachedFromEngine(a.b bVar) {
        Log.d("[FBP-Android]", "onDetachedFromEngine");
        this.f21995f = null;
        M(true);
        this.f21991b.unregisterReceiver(this.q);
        this.f21991b.unregisterReceiver(this.f22003p);
        this.f21991b = null;
        this.f21992c.e((j.c) null);
        this.f21992c = null;
        this.f21994e = null;
        this.f21993d = null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:301:0x07af  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x07ed A[Catch: Exception -> 0x0ac4, TryCatch #5 {Exception -> 0x0ac4, blocks: (B:117:0x01db, B:262:0x0692, B:265:0x06c3, B:266:0x06c8, B:269:0x06d2, B:271:0x06de, B:272:0x06fd, B:273:0x0702, B:275:0x0708, B:276:0x0710, B:277:0x0716, B:283:0x0766, B:286:0x0773, B:287:0x0778, B:290:0x0784, B:292:0x0788, B:295:0x0793, B:299:0x07a4, B:310:0x07c2, B:311:0x07ed, B:313:0x07f3, B:315:0x07fd, B:324:0x083e, B:318:0x0821, B:321:0x0830, B:296:0x0798, B:325:0x0843, B:327:0x0871, B:328:0x0879, B:330:0x0883, B:331:0x0889, B:334:0x0896, B:335:0x089b, B:338:0x08a5, B:340:0x08b9, B:341:0x08c1, B:344:0x08ce, B:345:0x08d4, B:347:0x08ea, B:349:0x08f9, B:350:0x0907, B:351:0x090c, B:353:0x091d, B:354:0x0922, B:355:0x0927, B:356:0x092c, B:358:0x093d, B:359:0x0940, B:360:0x0946, B:362:0x0952, B:364:0x095b, B:366:0x09a9, B:368:0x09b0, B:369:0x09b5, B:372:0x09bc, B:373:0x09c1, B:374:0x09d0, B:376:0x09e1, B:379:0x09e8, B:380:0x09ed, B:381:0x09f4, B:383:0x0a05, B:386:0x0a0c, B:387:0x0a11, B:391:0x0a23, B:392:0x0a37, B:394:0x0a3f, B:399:0x0a4e, B:402:0x0a59, B:403:0x0a5f, B:404:0x0a78, B:406:0x0a87, B:407:0x0a8e, B:409:0x0ab3, B:410:0x0ab8), top: B:423:0x01d4 }] */
    /* JADX WARN: Type inference failed for: r5v1, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r5v27, types: [n2.h] */
    /* JADX WARN: Type inference failed for: r5v3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onMethodCall(io.flutter.plugin.common.i r23, io.flutter.plugin.common.j.d r24) {
        /*
            Method dump skipped, instructions count: 2958
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: n2.h.onMethodCall(io.flutter.plugin.common.i, io.flutter.plugin.common.j$d):void");
    }

    public void onReattachedToActivityForConfigChanges(zf.c cVar) {
        Log.d("[FBP-Android]", "onReattachedToActivityForConfigChanges");
        onAttachedToActivity(cVar);
    }

    public boolean onRequestPermissionsResult(int i8, String[] strArr, int[] iArr) {
        g gVar = this.f22001m.get(Integer.valueOf(i8));
        if (gVar == null || iArr.length <= 0) {
            return false;
        }
        gVar.a(iArr[0] == 0, strArr[0]);
        return true;
    }

    HashMap<String, Object> z(ScanResult scanResult) {
        ScanRecord scanRecord = scanResult.getScanRecord();
        String deviceName = scanRecord != null ? scanRecord.getDeviceName() : null;
        boolean z4 = scanRecord != null && (scanRecord.getAdvertiseFlags() & 2) > 0;
        int txPowerLevel = scanRecord != null ? scanRecord.getTxPowerLevel() : Integer.MIN_VALUE;
        SparseArray<byte[]> manufacturerSpecificData = scanRecord != null ? scanRecord.getManufacturerSpecificData() : null;
        List<ParcelUuid> serviceUuids = scanRecord != null ? scanRecord.getServiceUuids() : null;
        Map<ParcelUuid, byte[]> serviceData = scanRecord != null ? scanRecord.getServiceData() : null;
        HashMap hashMap = new HashMap();
        if (manufacturerSpecificData != null) {
            for (int i8 = 0; i8 < manufacturerSpecificData.size(); i8++) {
                hashMap.put(Integer.valueOf(manufacturerSpecificData.keyAt(i8)), K(manufacturerSpecificData.valueAt(i8)));
            }
        }
        HashMap hashMap2 = new HashMap();
        if (serviceData != null) {
            for (Map.Entry<ParcelUuid, byte[]> entry : serviceData.entrySet()) {
                hashMap2.put(i0(entry.getKey().getUuid()), K(entry.getValue()));
            }
        }
        ArrayList arrayList = new ArrayList();
        if (serviceUuids != null) {
            for (ParcelUuid parcelUuid : serviceUuids) {
                arrayList.add(i0(parcelUuid.getUuid()));
            }
        }
        HashMap<String, Object> hashMap3 = new HashMap<>();
        hashMap3.put("local_name", deviceName);
        hashMap3.put("connectable", Boolean.valueOf(z4));
        hashMap3.put("tx_power_level", txPowerLevel != Integer.MIN_VALUE ? Integer.valueOf(txPowerLevel) : null);
        if (manufacturerSpecificData == null) {
            hashMap = null;
        }
        hashMap3.put("manufacturer_data", hashMap);
        if (serviceData == null) {
            hashMap2 = null;
        }
        hashMap3.put("service_data", hashMap2);
        hashMap3.put("service_uuids", serviceUuids != null ? arrayList : null);
        return hashMap3;
    }
}
