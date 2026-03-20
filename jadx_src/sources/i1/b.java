package i1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b implements Closeable {

    /* renamed from: a  reason: collision with root package name */
    private final File f20423a;

    /* renamed from: b  reason: collision with root package name */
    private final long f20424b;

    /* renamed from: c  reason: collision with root package name */
    private final File f20425c;

    /* renamed from: d  reason: collision with root package name */
    private final RandomAccessFile f20426d;

    /* renamed from: e  reason: collision with root package name */
    private final FileChannel f20427e;

    /* renamed from: f  reason: collision with root package name */
    private final FileLock f20428f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements FileFilter {
        a() {
        }

        @Override // java.io.FileFilter
        public boolean accept(File file) {
            return !file.getName().equals("MultiDex.lock");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: i1.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0176b extends File {

        /* renamed from: a  reason: collision with root package name */
        public long f20430a;

        public C0176b(File file, String str) {
            super(file, str);
            this.f20430a = -1L;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(File file, File file2) {
        Log.i("MultiDex", "MultiDexExtractor(" + file.getPath() + ", " + file2.getPath() + ")");
        this.f20423a = file;
        this.f20425c = file2;
        this.f20424b = h(file);
        File file3 = new File(file2, "MultiDex.lock");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file3, "rw");
        this.f20426d = randomAccessFile;
        try {
            FileChannel channel = randomAccessFile.getChannel();
            this.f20427e = channel;
            try {
                Log.i("MultiDex", "Blocking on lock " + file3.getPath());
                this.f20428f = channel.lock();
                Log.i("MultiDex", file3.getPath() + " locked");
            } catch (IOException e8) {
                e = e8;
                b(this.f20427e);
                throw e;
            } catch (Error e9) {
                e = e9;
                b(this.f20427e);
                throw e;
            } catch (RuntimeException e10) {
                e = e10;
                b(this.f20427e);
                throw e;
            }
        } catch (IOException | Error | RuntimeException e11) {
            b(this.f20426d);
            throw e11;
        }
    }

    private void a() {
        File[] listFiles = this.f20425c.listFiles(new a());
        if (listFiles == null) {
            Log.w("MultiDex", "Failed to list secondary dex dir content (" + this.f20425c.getPath() + ").");
            return;
        }
        for (File file : listFiles) {
            Log.i("MultiDex", "Trying to delete old file " + file.getPath() + " of size " + file.length());
            if (file.delete()) {
                Log.i("MultiDex", "Deleted old file " + file.getPath());
            } else {
                Log.w("MultiDex", "Failed to delete old file " + file.getPath());
            }
        }
    }

    private static void b(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e8) {
            Log.w("MultiDex", "Failed to close resource", e8);
        }
    }

    private static void c(ZipFile zipFile, ZipEntry zipEntry, File file, String str) {
        InputStream inputStream = zipFile.getInputStream(zipEntry);
        File createTempFile = File.createTempFile("tmp-" + str, ".zip", file.getParentFile());
        Log.i("MultiDex", "Extracting " + createTempFile.getPath());
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(createTempFile)));
            ZipEntry zipEntry2 = new ZipEntry("classes.dex");
            zipEntry2.setTime(zipEntry.getTime());
            zipOutputStream.putNextEntry(zipEntry2);
            byte[] bArr = new byte[16384];
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                zipOutputStream.write(bArr, 0, read);
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            if (!createTempFile.setReadOnly()) {
                throw new IOException("Failed to mark readonly \"" + createTempFile.getAbsolutePath() + "\" (tmp of \"" + file.getAbsolutePath() + "\")");
            }
            Log.i("MultiDex", "Renaming to " + file.getPath());
            if (createTempFile.renameTo(file)) {
                return;
            }
            throw new IOException("Failed to rename \"" + createTempFile.getAbsolutePath() + "\" to \"" + file.getAbsolutePath() + "\"");
        } finally {
            b(inputStream);
            createTempFile.delete();
        }
    }

    private static SharedPreferences d(Context context) {
        return context.getSharedPreferences("multidex.version", Build.VERSION.SDK_INT < 11 ? 0 : 4);
    }

    private static long f(File file) {
        long lastModified = file.lastModified();
        return lastModified == -1 ? lastModified - 1 : lastModified;
    }

    private static long h(File file) {
        long c9 = c.c(file);
        return c9 == -1 ? c9 - 1 : c9;
    }

    private static boolean i(Context context, File file, long j8, String str) {
        SharedPreferences d8 = d(context);
        if (d8.getLong(str + "timestamp", -1L) == f(file)) {
            if (d8.getLong(str + "crc", -1L) == j8) {
                return false;
            }
        }
        return true;
    }

    private List<C0176b> l(Context context, String str) {
        Log.i("MultiDex", "loading existing secondary dex files");
        String str2 = this.f20423a.getName() + ".classes";
        SharedPreferences d8 = d(context);
        int i8 = d8.getInt(str + "dex.number", 1);
        ArrayList arrayList = new ArrayList(i8 + (-1));
        int i9 = 2;
        while (i9 <= i8) {
            C0176b c0176b = new C0176b(this.f20425c, str2 + i9 + ".zip");
            if (!c0176b.isFile()) {
                throw new IOException("Missing extracted secondary dex file '" + c0176b.getPath() + "'");
            }
            c0176b.f20430a = h(c0176b);
            long j8 = d8.getLong(str + "dex.crc." + i9, -1L);
            long j9 = d8.getLong(str + "dex.time." + i9, -1L);
            long lastModified = c0176b.lastModified();
            if (j9 == lastModified) {
                String str3 = str2;
                SharedPreferences sharedPreferences = d8;
                if (j8 == c0176b.f20430a) {
                    arrayList.add(c0176b);
                    i9++;
                    d8 = sharedPreferences;
                    str2 = str3;
                }
            }
            throw new IOException("Invalid extracted dex: " + c0176b + " (key \"" + str + "\"), expected modification time: " + j9 + ", modification time: " + lastModified + ", expected crc: " + j8 + ", file crc: " + c0176b.f20430a);
        }
        return arrayList;
    }

    private List<C0176b> m() {
        boolean z4;
        String str = this.f20423a.getName() + ".classes";
        a();
        ArrayList arrayList = new ArrayList();
        ZipFile zipFile = new ZipFile(this.f20423a);
        try {
            ZipEntry entry = zipFile.getEntry("classes2.dex");
            int i8 = 2;
            while (entry != null) {
                C0176b c0176b = new C0176b(this.f20425c, str + i8 + ".zip");
                arrayList.add(c0176b);
                Log.i("MultiDex", "Extraction is needed for file " + c0176b);
                int i9 = 0;
                boolean z8 = false;
                while (i9 < 3 && !z8) {
                    int i10 = i9 + 1;
                    c(zipFile, entry, c0176b, str);
                    try {
                        c0176b.f20430a = h(c0176b);
                        z4 = true;
                    } catch (IOException e8) {
                        Log.w("MultiDex", "Failed to read crc from " + c0176b.getAbsolutePath(), e8);
                        z4 = false;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Extraction ");
                    sb.append(z4 ? "succeeded" : "failed");
                    sb.append(" '");
                    sb.append(c0176b.getAbsolutePath());
                    sb.append("': length ");
                    sb.append(c0176b.length());
                    sb.append(" - crc: ");
                    sb.append(c0176b.f20430a);
                    Log.i("MultiDex", sb.toString());
                    if (!z4) {
                        c0176b.delete();
                        if (c0176b.exists()) {
                            Log.w("MultiDex", "Failed to delete corrupted secondary dex '" + c0176b.getPath() + "'");
                        }
                    }
                    z8 = z4;
                    i9 = i10;
                }
                if (!z8) {
                    throw new IOException("Could not create zip file " + c0176b.getAbsolutePath() + " for secondary dex (" + i8 + ")");
                }
                i8++;
                entry = zipFile.getEntry("classes" + i8 + ".dex");
            }
            try {
                zipFile.close();
            } catch (IOException e9) {
                Log.w("MultiDex", "Failed to close resource", e9);
            }
            return arrayList;
        } catch (Throwable th) {
            try {
                zipFile.close();
            } catch (IOException e10) {
                Log.w("MultiDex", "Failed to close resource", e10);
            }
            throw th;
        }
    }

    private static void n(Context context, String str, long j8, long j9, List<C0176b> list) {
        SharedPreferences.Editor edit = d(context).edit();
        edit.putLong(str + "timestamp", j8);
        edit.putLong(str + "crc", j9);
        edit.putInt(str + "dex.number", list.size() + 1);
        int i8 = 2;
        for (C0176b c0176b : list) {
            edit.putLong(str + "dex.crc." + i8, c0176b.f20430a);
            edit.putLong(str + "dex.time." + i8, c0176b.lastModified());
            i8++;
        }
        edit.commit();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f20428f.release();
        this.f20427e.close();
        this.f20426d.close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<? extends File> j(Context context, String str, boolean z4) {
        List<C0176b> list;
        Log.i("MultiDex", "MultiDexExtractor.load(" + this.f20423a.getPath() + ", " + z4 + ", " + str + ")");
        if (this.f20428f.isValid()) {
            if (!z4 && !i(context, this.f20423a, this.f20424b, str)) {
                try {
                    list = l(context, str);
                } catch (IOException e8) {
                    Log.w("MultiDex", "Failed to reload existing extracted secondary dex files, falling back to fresh extraction", e8);
                }
                Log.i("MultiDex", "load found " + list.size() + " secondary dex files");
                return list;
            }
            Log.i("MultiDex", z4 ? "Forced extraction must be performed." : "Detected that extraction must be performed.");
            List<C0176b> m8 = m();
            n(context, str, f(this.f20423a), this.f20424b, m8);
            list = m8;
            Log.i("MultiDex", "load found " + list.size() + " secondary dex files");
            return list;
        }
        throw new IllegalStateException("MultiDexExtractor was closed");
    }
}
