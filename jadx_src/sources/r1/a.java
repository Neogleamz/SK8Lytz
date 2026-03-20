package r1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: e  reason: collision with root package name */
    private static final Map<String, Lock> f22642e = new HashMap();

    /* renamed from: a  reason: collision with root package name */
    private final File f22643a;

    /* renamed from: b  reason: collision with root package name */
    private final Lock f22644b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f22645c;

    /* renamed from: d  reason: collision with root package name */
    private FileChannel f22646d;

    public a(String str, File file, boolean z4) {
        File file2 = new File(file, str + ".lck");
        this.f22643a = file2;
        this.f22644b = a(file2.getAbsolutePath());
        this.f22645c = z4;
    }

    private static Lock a(String str) {
        Lock lock;
        Map<String, Lock> map = f22642e;
        synchronized (map) {
            lock = map.get(str);
            if (lock == null) {
                lock = new ReentrantLock();
                map.put(str, lock);
            }
        }
        return lock;
    }

    public void b() {
        this.f22644b.lock();
        if (this.f22645c) {
            try {
                FileChannel channel = new FileOutputStream(this.f22643a).getChannel();
                this.f22646d = channel;
                channel.lock();
            } catch (IOException e8) {
                throw new IllegalStateException("Unable to grab copy lock.", e8);
            }
        }
    }

    public void c() {
        FileChannel fileChannel = this.f22646d;
        if (fileChannel != null) {
            try {
                fileChannel.close();
            } catch (IOException unused) {
            }
        }
        this.f22644b.unlock();
    }
}
