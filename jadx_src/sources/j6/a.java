package j6;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.google.errorprone.annotations.ResultIgnorabilityUnspecified;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import n6.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements ServiceConnection {

    /* renamed from: a  reason: collision with root package name */
    boolean f20792a = false;

    /* renamed from: b  reason: collision with root package name */
    private final BlockingQueue f20793b = new LinkedBlockingQueue();

    @ResultIgnorabilityUnspecified
    public IBinder a(long j8, TimeUnit timeUnit) {
        j.k("BlockingServiceConnection.getServiceWithTimeout() called on main thread");
        if (this.f20792a) {
            throw new IllegalStateException("Cannot call get on this connection more than once");
        }
        this.f20792a = true;
        IBinder iBinder = (IBinder) this.f20793b.poll(j8, timeUnit);
        if (iBinder != null) {
            return iBinder;
        }
        throw new TimeoutException("Timed out waiting for the service connection");
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.f20793b.add(iBinder);
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
    }
}
