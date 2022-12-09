package com.cinemaapp.utils;

import com.cinemaapp.server.main.ServerApp;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TripleDES {
    
    private SecretKeySpec skeySpec = null;
    private IvParameterSpec iv = null;
    public TripleDES(String key){   
        skeySpec = new SecretKeySpec(key.getBytes(), "TripleDES");
        iv = new IvParameterSpec(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 });
    }
    public String Encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec,iv);
            byte[] byteEncrypted = cipher.doFinal(data.getBytes());
            String strEncrypt = Base64.getEncoder().encodeToString(byteEncrypted);
            return strEncrypt;
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            Logger.getLogger(TripleDES.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    public String Decrypt(String data){
         try {
            Cipher cipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] byteDecrypted = cipher.doFinal(Base64.getDecoder().decode(data));
            return new String(byteDecrypted);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException e) {
             Logger.getLogger(TripleDES.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
