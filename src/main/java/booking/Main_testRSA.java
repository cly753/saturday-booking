package booking;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Main_testRSA {

//    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
//        String publicKeyStr = "-----BEGIN PUBLIC KEY-----\n" +
//                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2sSnc7tplzMayv1nY0ow\n" +
//                "XWBnrpTh7dfGCzw/WYvK0fQnrplvAsYmWayrOs03M8wKFS1lk5d7sUCTi6eggqnf\n" +
//                "Vq87oWIIiYswVSfrgjy6nnUcLBI1V23sFc4fOTsxJ6mRjug0yjekzgWj/EuJitoU\n" +
//                "NhbHwulOAqUdKHJjyccd/ydRLsv19BUKtUxCGSp53ykFs2leYadF8kv4Lz0QbsRg\n" +
//                "d9iTyY7Pq01KeNfAFFO0e97/9nsRwo95PeV8dbbZTpCKErVJYFpLtXFPaANDnUFs\n" +
//                "rDGpXY3w4IVOtlMx4H601748QtAEC0IyLZYM/yFISWL5jIAl+saspndEPUAbnCyO\n" +
//                "3QIDAQAB\n" +
//                "-----END PUBLIC KEY-----";
//
//        publicKeyStr =
//                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2sSnc7tplzMayv1nY0ow\n" +
//                "XWBnrpTh7dfGCzw/WYvK0fQnrplvAsYmWayrOs03M8wKFS1lk5d7sUCTi6eggqnf\n" +
//                "Vq87oWIIiYswVSfrgjy6nnUcLBI1V23sFc4fOTsxJ6mRjug0yjekzgWj/EuJitoU\n" +
//                "NhbHwulOAqUdKHJjyccd/ydRLsv19BUKtUxCGSp53ykFs2leYadF8kv4Lz0QbsRg\n" +
//                "d9iTyY7Pq01KeNfAFFO0e97/9nsRwo95PeV8dbbZTpCKErVJYFpLtXFPaANDnUFs\n" +
//                "rDGpXY3w4IVOtlMx4H601748QtAEC0IyLZYM/yFISWL5jIAl+saspndEPUAbnCyO\n" +
//                "3QIDAQAB\n";
//
//        publicKeyStr =
//                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2sSnc7tplzMayv1nY0ow" +
//                "XWBnrpTh7dfGCzw/WYvK0fQnrplvAsYmWayrOs03M8wKFS1lk5d7sUCTi6eggqnf" +
//                "Vq87oWIIiYswVSfrgjy6nnUcLBI1V23sFc4fOTsxJ6mRjug0yjekzgWj/EuJitoU" +
//                "NhbHwulOAqUdKHJjyccd/ydRLsv19BUKtUxCGSp53ykFs2leYadF8kv4Lz0QbsRg" +
//                "d9iTyY7Pq01KeNfAFFO0e97/9nsRwo95PeV8dbbZTpCKErVJYFpLtXFPaANDnUFs" +
//                "rDGpXY3w4IVOtlMx4H601748QtAEC0IyLZYM/yFISWL5jIAl+saspndEPUAbnCyO" +
//                "3QIDAQAB";
//
//        System.out.println("publicKeyStr.length() = " + publicKeyStr.length());
//
//        String data = "sheepx7777";
//
//        String encrypted = encrypt(publicKeyStr, data);
//
//        System.out.println("Encrypted:\n" + encrypted);
//    }

//    public static String encrypt(String publicKeyStr, String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
//
////         rsa_1024_public_key
//
//        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PublicKey publicKey = keyFactory.generatePublic(spec);
////        RSAPublicKeySpec pub = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
////
////        BigInteger mod = pub.getModulus();
////        BigInteger exp = pub.getPublicExponent();
//
////        logger.debug("pub.getModulus() = ", mod);
////        logger.debug("pub.getPublicExponent() = ", exp);
//
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        byte[] cipherData = cipher.doFinal(data.getBytes());
//        String dataEncrypted = new String(cipherData);
//
//        return dataEncrypted;
//    }
}
