package com.diplabs.securecodeverifier;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {


    private static final String TAG = "TESTT";


    public String get_SHA_512_SecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] data = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public String decrypt(String encrypted_encoded_string, String KEY){

        String plain_text = "";
        try{
            byte[] encrypted_decoded_bytes = Base64.getDecoder().decode(encrypted_encoded_string);
            String encrypted_decoded_string = new String(encrypted_decoded_bytes);
            String iv_string = encrypted_decoded_string.substring(0,16); //IV is retrieved correctly.

            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOf(encrypted_decoded_bytes, 16));
            SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            plain_text = new String(cipher.doFinal(Arrays.copyOfRange(encrypted_decoded_bytes, 16, encrypted_decoded_bytes.length)));//Returns garbage characters
            return plain_text;

        }  catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
        return plain_text;
    }

}