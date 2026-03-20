package g2;

import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {
    public static String a(String str) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] bytes = str.getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            byte[] digest = messageDigest.digest();
            char[] cArr2 = new char[digest.length * 2];
            int i8 = 0;
            for (byte b9 : digest) {
                int i9 = i8 + 1;
                cArr2[i8] = cArr[(b9 >>> 4) & 15];
                i8 = i9 + 1;
                cArr2[i9] = cArr[b9 & 15];
            }
            return new String(cArr2);
        } catch (Exception e8) {
            e8.printStackTrace();
            return null;
        }
    }

    public static byte[] b(byte[] bArr, byte[] bArr2) {
        Key d8 = d(bArr2);
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        cipher.init(1, d8);
        return cipher.doFinal(bArr);
    }

    public static String c(File file) {
        if (!file.isFile()) {
            return null;
        }
        byte[] bArr = new byte[RecognitionOptions.UPC_E];
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            FileInputStream fileInputStream = new FileInputStream(file);
            while (true) {
                int read = fileInputStream.read(bArr, 0, RecognitionOptions.UPC_E);
                if (read == -1) {
                    fileInputStream.close();
                    return c.e(messageDigest.digest());
                }
                messageDigest.update(bArr, 0, read);
            }
        } catch (Exception e8) {
            e8.printStackTrace();
            return null;
        }
    }

    public static Key d(byte[] bArr) {
        return new SecretKeySpec(bArr, "AES");
    }
}
