package r1;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Build;
import android.util.Log;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b {
    public static Cursor a(Cursor cursor) {
        try {
            MatrixCursor matrixCursor = new MatrixCursor(cursor.getColumnNames(), cursor.getCount());
            while (cursor.moveToNext()) {
                Object[] objArr = new Object[cursor.getColumnCount()];
                for (int i8 = 0; i8 < cursor.getColumnCount(); i8++) {
                    int type = cursor.getType(i8);
                    if (type == 0) {
                        objArr[i8] = null;
                    } else if (type == 1) {
                        objArr[i8] = Long.valueOf(cursor.getLong(i8));
                    } else if (type == 2) {
                        objArr[i8] = Double.valueOf(cursor.getDouble(i8));
                    } else if (type == 3) {
                        objArr[i8] = cursor.getString(i8);
                    } else if (type != 4) {
                        throw new IllegalStateException();
                    } else {
                        objArr[i8] = cursor.getBlob(i8);
                    }
                }
                matrixCursor.addRow(objArr);
            }
            return matrixCursor;
        } finally {
            cursor.close();
        }
    }

    private static int b(Cursor cursor, String str) {
        if (Build.VERSION.SDK_INT <= 25 && str.length() != 0) {
            return c(cursor.getColumnNames(), str);
        }
        return -1;
    }

    static int c(String[] strArr, String str) {
        String str2 = "." + str;
        String str3 = "." + str + "`";
        for (int i8 = 0; i8 < strArr.length; i8++) {
            String str4 = strArr[i8];
            if (str4.length() >= str.length() + 2) {
                if (str4.endsWith(str2)) {
                    return i8;
                }
                if (str4.charAt(0) == '`' && str4.endsWith(str3)) {
                    return i8;
                }
            }
        }
        return -1;
    }

    public static int d(Cursor cursor, String str) {
        int columnIndex = cursor.getColumnIndex(str);
        if (columnIndex >= 0) {
            return columnIndex;
        }
        int columnIndex2 = cursor.getColumnIndex("`" + str + "`");
        return columnIndex2 >= 0 ? columnIndex2 : b(cursor, str);
    }

    public static int e(Cursor cursor, String str) {
        String str2;
        int d8 = d(cursor, str);
        if (d8 >= 0) {
            return d8;
        }
        try {
            str2 = Arrays.toString(cursor.getColumnNames());
        } catch (Exception e8) {
            Log.d("RoomCursorUtil", "Cannot collect column names for debug purposes", e8);
            str2 = BuildConfig.FLAVOR;
        }
        throw new IllegalArgumentException("column '" + str + "' does not exist. Available columns: " + str2);
    }
}
