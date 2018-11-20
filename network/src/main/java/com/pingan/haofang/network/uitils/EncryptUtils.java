package com.pingan.haofang.network.uitils;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;


class EncryptUtils {

    private static final char[] HEX = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public EncryptUtils() {
    }

    public static byte[] des3EncodeCBC(String key, String encodeStr) {
        try {
            byte[] data = encodeStr.getBytes("UTF-8");
            Key deskey = null;
            DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            IvParameterSpec ips = new IvParameterSpec(new byte[8]);
            cipher.init(1, deskey, ips);
            cipher.init(1, deskey);
            return cipher.doFinal(data);
        } catch (Exception var8) {
            return null;
        }
    }

    private static byte[] des3EncodeEcb(String key, String encodeStr) {
        try {
            byte[] data = encodeStr.getBytes("UTF-8");
            Key deskey = null;
            DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            cipher.init(1, deskey);
            return cipher.doFinal(data);
        } catch (Exception var7) {
            return null;
        }
    }

    public static String des3EncodeEcbWithBase64(String key, String encodeStr) {
        byte[] data = des3EncodeEcb(key, encodeStr);
        return null != data && 0 != data.length ? filter(Base64.encodeToString(data, 0)) : "";
    }

    private static String filter(String str) {
        String output = "";
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < str.length(); ++i) {
            int asc = str.charAt(i);
            if (asc != 10 && asc != 13) {
                sb.append(str.subSequence(i, i + 1));
            }
        }

        output = new String(sb);
        return output;
    }

    public static String des3DecodeCBC(String key, byte[] data) {
        SecretKey deskey = null;

        try {
            DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            IvParameterSpec ips = new IvParameterSpec(new byte[8]);
            cipher.init(2, deskey, ips);
            cipher.init(2, deskey);
            byte[] bOut = cipher.doFinal(data);
            return new String(bOut, "UTF-8");
        } catch (Exception var8) {
            return "";
        }
    }

    private static String des3DecodeEcb(String key, byte[] data) {
        SecretKey deskey = null;

        try {
            DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            cipher.init(2, deskey);
            byte[] bOut = cipher.doFinal(data);
            return new String(bOut, "UTF-8");
        } catch (Exception var7) {
            return "";
        }
    }

    public static String des3DecodeEcbWithBase64(String key, String base64Str) {
        return des3DecodeEcb(key, Base64.decode(base64Str, 0));
    }

    public static String Md5(String str) {
        if (str != null && !str.trim().equals("")) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
                StringBuilder sb = new StringBuilder();

                for (byte aMd5Byte : md5Byte) {
                    sb.append(HEX[(aMd5Byte & 240) >>> 4]);
                    sb.append(HEX[aMd5Byte & 15]);
                }

                str = sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return str;
    }

    public static String Md5File(File file) {
        FileInputStream in = null;

        try {
            in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0L, file.length());
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(byteBuffer);
            return bufferToHex(messagedigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return "";
    }

    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;

        for (int l = m; l < k; ++l) {
            appendHexPair(bytes[l], stringbuffer);
        }

        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = HEX[(bt & 240) >> 4];
        char c1 = HEX[bt & 15];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static String SHA1(String s) {
        if (s != null && !s.trim().equals("")) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-1");
                digest.update(s.getBytes("UTF8"));
                byte[] messageDigest = digest.digest();
                StringBuilder sb = new StringBuilder();

                for (byte aMessageDigest : messageDigest) {
                    sb.append(HEX[(aMessageDigest & 240) >>> 4]);
                    sb.append(HEX[aMessageDigest & 15]);
                }

                s = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return s;
    }
}
