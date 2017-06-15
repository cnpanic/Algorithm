package com.polkapolka.bluetooth.le.smartchoice;

/**
 * Created by crypto_lab on 2017-06-08.
 */
import kr.re.nsr.crypto.BlockCipher;
import kr.re.nsr.crypto.BlockCipherMode;
import kr.re.nsr.crypto.padding.PKCS5Padding;

public class LEA {
    private BlockCipherMode lea_cipher = null;
    String TAG = "LEA";

    public byte[] ENC_LEA(byte[] plain, byte[] key)
    {


        lea_cipher = new kr.re.nsr.crypto.symm.LEA.ECB();
        lea_cipher.init(BlockCipher.Mode.ENCRYPT,key);
        lea_cipher.setPadding(new PKCS5Padding(16));
        byte ct1[] = lea_cipher.doFinal(plain);
        return ct1;
    }

    public byte[] DEC_LEA(byte[] key, byte[] cipher) {

        lea_cipher = new kr.re.nsr.crypto.symm.LEA.ECB();
        lea_cipher.init(BlockCipher.Mode.DECRYPT, key);
        lea_cipher.setPadding(new PKCS5Padding(16));
        final byte en1[] = lea_cipher.doFinal(cipher);
        return en1;
    }
}
