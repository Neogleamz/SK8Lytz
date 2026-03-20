package b3;

import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d {

    /* renamed from: a  reason: collision with root package name */
    private b f8011a;

    /* renamed from: b  reason: collision with root package name */
    private ScanCallback f8012b = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends ScanCallback {
        a() {
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onBatchScanResults(List<ScanResult> list) {
            for (ScanResult scanResult : list) {
                onScanResult(0, scanResult);
            }
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onScanFailed(int i8) {
            synchronized (d.class) {
                if (d.this.f8011a != null) {
                    d.this.f8011a.onScanFailed(i8);
                }
            }
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onScanResult(int i8, ScanResult scanResult) {
            synchronized (d.class) {
                if (d.this.f8011a != null) {
                    d.this.f8011a.onScanResult(scanResult);
                }
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void onScanFailed(int i8);

        void onScanResult(ScanResult scanResult);
    }

    public d(b bVar) {
        this.f8011a = bVar;
    }

    public boolean b(int i8, Context context) {
        synchronized (this) {
            BluetoothLeScanner b9 = b3.a.b(context);
            if (b9 != null) {
                b9.startScan((List<ScanFilter>) null, new ScanSettings.Builder().setReportDelay(i8).setScanMode(2).build(), this.f8012b);
            }
        }
        return true;
    }

    public void c(Context context) {
        synchronized (this) {
            BluetoothLeScanner b9 = b3.a.b(context);
            if (b9 != null) {
                b9.stopScan(this.f8012b);
                this.f8011a = null;
            }
        }
    }
}
