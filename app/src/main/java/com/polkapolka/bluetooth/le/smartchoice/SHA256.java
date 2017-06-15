package com.polkapolka.bluetooth.le.smartchoice;

/**
 * Created by crypto_lab on 2017-06-08.
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

    // String TAG = "SHA256";
    public StringBuilder Sha256_E(String input){
        StringBuilder hexSHA256hash = null;

        MessageDigest mdSHA256 = null;
        try {
            mdSHA256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] msg = hexStringToByteArray(input);
        String temp;
        String hex;

        mdSHA256.update(msg);

        byte[] sha256Hash = mdSHA256.digest();

        hexSHA256hash = new StringBuilder();
        for (byte b : sha256Hash)
        {
            String hexString = String.format("%02x", b);

            hexSHA256hash.append(hexString);
        }


        //Log.d("SHERESULT", hexSHA256hash.toString());
        return hexSHA256hash;
    }

    static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
