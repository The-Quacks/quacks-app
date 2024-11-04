package com.example.quacks_app;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;

/**
 * The {@code QRCodeUtil} class provides functionality for encoding, decoding, and
 * and hashing QR codes.
 */
public class QRCodeUtil {

    /**
     * Encodes a string into a QR code
     *
     * @param id the id string to be encoded
     * @param width the width of the resulting QR code
     * @param height the height of the resulting QR code
     * @return a {@code Bitmap} of the QR code. Returns {@code null} on failure
     *
     */
    public static Bitmap encode(String id, int width, int height) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(id, BarcodeFormat.QR_CODE, width, height);
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    if (matrix.get(w, h)) {
                        bitmap.setPixel(w, h, Color.BLACK);
                    } else {
                        bitmap.setPixel(w, h, Color.WHITE);
                    }
                }
            }
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Decodes a string from a QR code
     *
     * @param bitmap the QR code bitmap to be decoded
     * @return a {@code string} decoded from the QR code. Returns {@code null} on failure
     *
     */
    public static String decode(Bitmap bitmap) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            RGBLuminanceSource luminanceSource = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
            Reader reader = new QRCodeReader();
            return reader.decode(binaryBitmap).getText();
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Hashes a QR code
     *
     * @param bitmap the QR code bitmap to be hashed
     * @return a {@code string} hash of the QR code. Returns {@code null} on failure
     *
     */
    public static String hash(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(stream.toByteArray());
            String str = "";
            for (byte b : hash) {
                str += String.format("%02X", b);
            }
            return str;
        }
        catch (Exception e) {
            return null;
        }
    }
}
