package ActElse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * booking > ActElse
 */
public class ActUtil {
    private static final Logger logger = LoggerFactory.getLogger(ActUtil.class);

    public static String encrypt(String publicKeyStr, String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // rsa_1024_public_key

        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);
//        RSAPublicKeySpec pub = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
//
//        BigInteger mod = pub.getModulus();
//        BigInteger exp = pub.getPublicExponent();

//        logger.debug("pub.getModulus() = ", mod);
//        logger.debug("pub.getPublicExponent() = ", exp);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherData = cipher.doFinal(data.getBytes());
        String dataEncrypted = new String(cipherData);

        logger.debug("data          : " + data);
        logger.debug("encrypted data: " + dataEncrypted);

        return dataEncrypted;
    }
}
