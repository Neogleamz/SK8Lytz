package r1;

import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.os.Build;
import android.os.CancellationSignal;
import androidx.room.RoomDatabase;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c {
    public static void a(t1.b bVar) {
        ArrayList<String> arrayList = new ArrayList();
        Cursor w02 = bVar.w0("SELECT name FROM sqlite_master WHERE type = 'trigger'");
        while (w02.moveToNext()) {
            try {
                arrayList.add(w02.getString(0));
            } catch (Throwable th) {
                w02.close();
                throw th;
            }
        }
        w02.close();
        for (String str : arrayList) {
            if (str.startsWith("room_fts_content_sync_")) {
                bVar.H("DROP TRIGGER IF EXISTS " + str);
            }
        }
    }

    public static Cursor b(RoomDatabase roomDatabase, t1.e eVar, boolean z4, CancellationSignal cancellationSignal) {
        Cursor query = roomDatabase.query(eVar, cancellationSignal);
        if (z4 && (query instanceof AbstractWindowedCursor)) {
            AbstractWindowedCursor abstractWindowedCursor = (AbstractWindowedCursor) query;
            int count = abstractWindowedCursor.getCount();
            return (Build.VERSION.SDK_INT < 23 || (abstractWindowedCursor.hasWindow() ? abstractWindowedCursor.getWindow().getNumRows() : count) < count) ? b.a(abstractWindowedCursor) : query;
        }
        return query;
    }

    public static int c(File file) {
        FileChannel fileChannel = null;
        try {
            ByteBuffer allocate = ByteBuffer.allocate(4);
            FileChannel channel = new FileInputStream(file).getChannel();
            channel.tryLock(60L, 4L, true);
            channel.position(60L);
            if (channel.read(allocate) == 4) {
                allocate.rewind();
                int i8 = allocate.getInt();
                channel.close();
                return i8;
            }
            throw new IOException("Bad database header, unable to read 4 bytes at offset 60");
        } catch (Throwable th) {
            if (0 != 0) {
                fileChannel.close();
            }
            throw th;
        }
    }
}
