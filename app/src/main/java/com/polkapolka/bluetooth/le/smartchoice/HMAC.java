package com.polkapolka.bluetooth.le.smartchoice;

import java.util.Arrays;

/**
 * Created by crypto_lab on 2017-06-08.
 */

import static com.polkapolka.bluetooth.le.smartchoice.HMAC.MAC_ALGORITHM.MAC_SHA256;

public class HMAC {public enum AUTH_ALGORITHM {AUTH_PRESENT_ECB_SHA256, AUTH_LEA128_ECB_SHA256}

    public enum MAC_ALGORITHM {MAC_SHA256}

    public enum CRYPTO_ALGORITHM {CRYPTO_LEA128_ECB, CRYPTO_LEA192_ECB, CRYPTO_PRESENT_ECB, CRYPTO_LEA256_ECB}

    public enum CRYPTO_CIPHERMODE {ECB}

    public boolean cryptoSupport;
    public CRYPTO_ALGORITHM type;


    public final int AUTH_BOARD_ADDR_LENGTH = 40;
    public final int AUTH_RAND_LENGTH = 4;
    public final int AUTH_SRES_LENGTH = 4;
    public final int AUTH_MSG_LENGTH = (AUTH_BOARD_ADDR_LENGTH + AUTH_RAND_LENGTH);
    public final int MAC_SHA256_MACSIZE = 32;
    public final int MAC_SHA256_BLOCKSIZE = 64;
    public final int HMAC_BLOCKSIZE = 64;

    public final int IPAD = 0x36;
    public final int OPAD = 0x5C;
    public final int CRYPTO_PRESENT_KEYLENGTH = 10;
    public final int CRYPTO_LEA128_KEYLENGTH = 16;
    public final int CRYPTO_LEA192_KEYLENGTH = 24;
    public final int CRYPTO_LEA256_KEYLENGTH = 32;

    public final int CRYPTO_PRESENT_BLOCKSIZE = 8;
    public final int CRYPTO_LEA_BLOCKSIZE = 16;

    public final int CRYPTO_PRESENT_ROUNDS = 31;
    public final int CRYPTO_LEA128_ROUNDS = 24;
    public final int CRYPTO_LEA192_ROUNDS = 28;
    public final int CRYPTO_LEA256_ROUNDS = 32;

    public final int CRYPTO_LEA_ROUNDKEY_LENGTH = 6;

    SHA256 SHA = new SHA256();

    public MAC_ALGORITHM getMacAlgorithm(AUTH_ALGORITHM type) {
        MAC_ALGORITHM algorithm = null;
        switch (type) {
            case AUTH_PRESENT_ECB_SHA256:
            case AUTH_LEA128_ECB_SHA256:
                algorithm = MAC_SHA256;
                break;
        }

        return algorithm;
    }

    public byte[] mac(MAC_ALGORITHM macAlgorithm, byte[] msg){
        String byteToStr = null;
        String strMsg = null;
        byte[] result = null;
        int i;

        switch (macAlgorithm) {
            case MAC_SHA256: {
                byteToStr = Integer.toString(((msg[0] & 0xFF) + 0x100), 16).substring(1);
                strMsg = byteToStr;

                for (i = 1; i < msg.length; i++) {
                    byteToStr = Integer.toString(((msg[i] & 0xFF) + 0x100), 16).substring(1);
                    strMsg = strMsg + byteToStr;
                }
                //Log.d("text", strMsg);

                StringBuilder ctx;
                ctx = SHA.Sha256_E(strMsg);

                result = new byte[ctx.toString().length()];
                result = hexStringToByteArray(ctx.toString());
                break;
            }
        }

//        for (i = 0; i < result.length; i++) {
//            Log.d("Mac", String.format("%02x", result[i]));
//
//        }

        return result;
    }

    public int getMacAlgorithmMacSize(MAC_ALGORITHM type) {
        int macSize = 0;

        switch (type) {
            case MAC_SHA256: {
                macSize = MAC_SHA256_MACSIZE;
                break;
            }
        }
        return macSize;
    }

    public byte[] hmac(MAC_ALGORITHM macAlgorithm, byte[] key, byte[] msg){

        byte[] hmacSum;
        byte[] buffer= new byte[HMAC_BLOCKSIZE];

//        for (int i = 0; i < key.length; i++) {
//            Log.d("DONGGUK", String.format("key %02x", key[i]));
//        }
//
//        for (int i = 0; i < msg.length; i++) {
//            Log.d("DONGGUK", String.format("msg %02x", msg[i]));
//        }

        buffer = Arrays.copyOf(buffer, HMAC_BLOCKSIZE);

        if (key.length > HMAC_BLOCKSIZE) {
            buffer = mac(macAlgorithm, msg);
        } else {
            for (int i = 0; i < key.length; i++) {
                buffer[i] = key[i];
            }
        }

        for (int i = 0; i < HMAC_BLOCKSIZE; i++) {
            buffer[i] ^= IPAD;
        }

        int bufferLen = buffer.length;
        buffer = Arrays.copyOf(buffer, buffer.length + msg.length);
        System.arraycopy(msg, 0, buffer, bufferLen, msg.length);

        hmacSum = mac(macAlgorithm, buffer);

        for (int i = 0; i < HMAC_BLOCKSIZE; i++) {
            buffer[i] ^= IPAD ^ OPAD;
        }

        buffer = Arrays.copyOf(buffer, HMAC_BLOCKSIZE + hmacSum.length);
        System.arraycopy(hmacSum, 0, buffer, HMAC_BLOCKSIZE, hmacSum.length);

        hmacSum = mac(macAlgorithm, buffer);

//        for (int i = 0; i < hmacSum.length; i++) {
//            Log.d("DONGGUK", String.format("hmac %02x", hmacSum[i]));
//        }

        return hmacSum;
    }


    public byte[] req_auth(AUTH_ALGORITHM type, byte[] preSharedKey, byte[] Uid, byte[] Randnum){
        int i;

        byte[] BD_ADDR = new byte[Uid.length];
        for (i = 0; i < Uid.length; i++) {
            BD_ADDR[i] = Uid[i];
        }
        //48bits 보드나, 핸드폰의 식별명에서 6bytes 따옴
        byte[] AU_RAND = new byte[AUTH_RAND_LENGTH]; //120bits //검증자에게서 받아옴

        byte[] AU_MSG = new byte[Uid.length + AUTH_RAND_LENGTH];

        for (i = 0; i < Uid.length + AUTH_RAND_LENGTH; i++) {
            AU_MSG[i] = (byte) 0x00;
        }

        for (i = 0; i < AUTH_RAND_LENGTH; i++) {
            AU_RAND[i] = Randnum[i];
        }


        byte[] hmacSum;
        MAC_ALGORITHM macType = getMacAlgorithm(type);

        byte[] SRES = new byte[AUTH_SRES_LENGTH];

        for (i = 0; i < AUTH_SRES_LENGTH; i++) {
            SRES[i] = (byte) 0x00;
        }

        hmacSum = new byte[getMacAlgorithmMacSize(macType)];

        for (i = 0; i < Uid.length; i++) {
            AU_MSG[i] = BD_ADDR[i];
        }
        for (i = 0; i < AUTH_RAND_LENGTH; i++) {
            AU_MSG[i + Uid.length] = AU_RAND[i];
        }

        hmacSum = hmac(macType, preSharedKey, AU_MSG);

        for (i = 0; i < AUTH_SRES_LENGTH; i++) {
            SRES[i] = hmacSum[i];
        }

        return SRES;
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

