package ru.tapublog.lib.gsm0348.impl.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.tapublog.lib.gsm0348.impl.crypto.mac.CRC16X25;
import ru.tapublog.lib.gsm0348.impl.crypto.mac.CRC32;
import ru.tapublog.lib.gsm0348.impl.crypto.mac.DESMACISO9797M1;
import ru.tapublog.lib.gsm0348.impl.crypto.params.KeyParameter;
import ru.tapublog.lib.gsm0348.impl.crypto.params.ParametersWithIV;

/**
 * This utility class is used for signature operations during GSM 03.48 packet creation and recovering. It performs redundancy check, digital signature and
 * cryptographic checksum algorithms.
 *
 * @author Victor Platov
 */
public class SignatureManager {
  public static final String DES_MAC8_ISO9797_M1 = "DES_MAC8_ISO9797_M1";
  public static final String CRC_16 = "CRC16";
  public static final String CRC_32 = "CRC32";
  private static final Logger LOGGER = LoggerFactory.getLogger(SignatureManager.class);

  private SignatureManager() {
  }

  private static Mac getMac(String algName, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
    LOGGER.debug("Creating MAC for name: {} with key length {}", algName, key.length);
    Mac mac = Mac.getInstance(algName);
    LOGGER.debug("MAC length: {}", mac.getMacLength());
    SecretKeySpec keySpec = new SecretKeySpec(key, algName);
    mac.init(keySpec);
    return mac;
  }

  private static byte[] runOwnMac(ru.tapublog.lib.gsm0348.impl.crypto.Mac mac, byte[] key, byte[] data, int size) {
    CipherParameters params = new ParametersWithIV(new KeyParameter(key), new byte[8]);
    mac.init(params);
    mac.update(data, 0, data.length);
    byte[] result = new byte[size];
    mac.doFinal(result, 0);
    return result;
  }

  public static byte[] sign(String algName, byte[] key, byte[] data)
      throws NoSuchAlgorithmException, InvalidKeyException {
    LOGGER.debug("Signing with algorithm {}. Data length: {}", algName, data.length);
    if (DES_MAC8_ISO9797_M1.equals(algName)) {
      return runOwnMac(new DESMACISO9797M1(), key, data, 8);
    }
    if (CRC_16.equals(algName)) {
      return runOwnMac(new CRC16X25(), key, data, 2);
    }
    if (CRC_32.equals(algName)) {
      return runOwnMac(new CRC32(), key, data, 4);
    }
    return doWork(algName, key, data);
  }

  public static boolean verify(String algName, byte[] key, byte[] data, byte[] signature)
      throws NoSuchAlgorithmException, InvalidKeyException {
    LOGGER.debug("Verifying with algorithm {}. Data length: {}", algName, data.length);
    return Arrays.equals(signature, sign(algName, key, data));
  }

  private static byte[] doWork(final String algName, byte[] key, byte[] data) throws InvalidKeyException, NoSuchAlgorithmException {
    final Mac mac = getMac(algName, key);
    final byte[] result = mac.doFinal(data);
    return result;
  }

  public static int signLength(final String algName) throws NoSuchAlgorithmException {
    LOGGER.debug("Creating MAC for algorithm: {}", algName);
    if (DES_MAC8_ISO9797_M1.equals(algName)) // TODO: remove this block after adding something better
    {
      final int macLength = 8;
      LOGGER.trace("MAC length: {}", macLength);
      return macLength;
    } else if (CRC_16.equals(algName)) {
      final int macLength = 2;
      LOGGER.trace("MAC length: {}", macLength);
      return macLength;
    } else if (CRC_32.equals(algName)) {
      final int macLength = 4;
      LOGGER.trace("MAC length: {}", macLength);
      return macLength;
    }
    Mac mac = Mac.getInstance(algName);
    final int macLength = mac.getMacLength();

    LOGGER.trace("MAC length: {}", macLength);
    return macLength;
  }
}
