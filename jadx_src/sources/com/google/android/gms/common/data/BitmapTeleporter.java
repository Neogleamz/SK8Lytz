package com.google.android.gms.common.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.util.Log;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import n6.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class BitmapTeleporter extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Parcelable.Creator<BitmapTeleporter> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    final int f11726a;

    /* renamed from: b  reason: collision with root package name */
    ParcelFileDescriptor f11727b;

    /* renamed from: c  reason: collision with root package name */
    final int f11728c;

    /* renamed from: d  reason: collision with root package name */
    private Bitmap f11729d = null;

    /* renamed from: e  reason: collision with root package name */
    private boolean f11730e = false;

    /* renamed from: f  reason: collision with root package name */
    private File f11731f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BitmapTeleporter(int i8, ParcelFileDescriptor parcelFileDescriptor, int i9) {
        this.f11726a = i8;
        this.f11727b = parcelFileDescriptor;
        this.f11728c = i9;
    }

    private static final void t(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e8) {
            Log.w("BitmapTeleporter", "Could not close stream", e8);
        }
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        if (this.f11727b == null) {
            Bitmap bitmap = (Bitmap) j.l(this.f11729d);
            ByteBuffer allocate = ByteBuffer.allocate(bitmap.getRowBytes() * bitmap.getHeight());
            bitmap.copyPixelsToBuffer(allocate);
            byte[] array = allocate.array();
            File file = this.f11731f;
            if (file == null) {
                throw new IllegalStateException("setTempDir() must be called before writing this object to a parcel");
            }
            try {
                File createTempFile = File.createTempFile("teleporter", ".tmp", file);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(createTempFile);
                    this.f11727b = ParcelFileDescriptor.open(createTempFile, 268435456);
                    createTempFile.delete();
                    DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(fileOutputStream));
                    try {
                        try {
                            dataOutputStream.writeInt(array.length);
                            dataOutputStream.writeInt(bitmap.getWidth());
                            dataOutputStream.writeInt(bitmap.getHeight());
                            dataOutputStream.writeUTF(bitmap.getConfig().toString());
                            dataOutputStream.write(array);
                        } catch (IOException e8) {
                            throw new IllegalStateException("Could not write into unlinked file", e8);
                        }
                    } finally {
                        t(dataOutputStream);
                    }
                } catch (FileNotFoundException unused) {
                    throw new IllegalStateException("Temporary file is somehow already deleted");
                }
            } catch (IOException e9) {
                throw new IllegalStateException("Could not create temporary file", e9);
            }
        }
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11726a);
        o6.a.q(parcel, 2, this.f11727b, i8 | 1, false);
        o6.a.l(parcel, 3, this.f11728c);
        o6.a.b(parcel, a9);
        this.f11727b = null;
    }
}
