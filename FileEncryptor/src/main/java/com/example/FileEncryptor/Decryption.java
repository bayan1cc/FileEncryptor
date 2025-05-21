package com.example.FileEncryptor;

public class Decryption {
    public byte[] DecryptionProcess(byte[] file, byte[] key) {

        byte[] result = new byte[file.length];

        for (int i = 0; i < file.length; i++) {
            result[i] = (byte)(file[i] ^ key[i % key.length]);
        }

        return result;
    }
}
