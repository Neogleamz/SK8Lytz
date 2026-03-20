package b3;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.os.Build;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {
    private static BluetoothAdapter a(Context context) {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService("bluetooth");
        if (bluetoothManager == null) {
            return null;
        }
        return bluetoothManager.getAdapter();
    }

    public static BluetoothLeScanner b(Context context) {
        synchronized (a.class) {
            BluetoothAdapter a9 = a(context);
            if (a9 == null) {
                return null;
            }
            if (Build.VERSION.SDK_INT >= 21) {
                return a9.getBluetoothLeScanner();
            }
            return null;
        }
    }
}
