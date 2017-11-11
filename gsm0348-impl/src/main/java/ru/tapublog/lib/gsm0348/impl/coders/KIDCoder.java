package ru.tapublog.lib.gsm0348.impl.coders;

import ru.tapublog.lib.gsm0348.api.model.AlgorithmImplementation;
import ru.tapublog.lib.gsm0348.api.model.CertificationAlgorithmMode;
import ru.tapublog.lib.gsm0348.api.model.CertificationMode;
import ru.tapublog.lib.gsm0348.api.model.KID;
import ru.tapublog.lib.gsm0348.impl.CodingException;
import ru.tapublog.lib.gsm0348.impl.Util;

public class KIDCoder
{
	public static byte decode(KID kid) throws CodingException
	{
		int algImpl = 0;
		int algMode = 0;
		byte keysetID = kid.getKeysetID();

		if(keysetID  < 0 && keysetID > 0xF)
			throw new CodingException("Cannot decode KID. KID keySetID cannot be <0 and >15");

		switch (kid.getAlgorithmImplementation())
		{
			case ALGORITHM_KNOWN_BY_BOTH_ENTITIES:
				algImpl = 0;
				break;
			case DES:
			case CRC:
				algImpl = 1;
				break;
			case AES:
				algImpl = 2;
				break;
			case PROPRIETARY_IMPLEMENTATIONS:
				algImpl = 3;
				break;
		}

		switch (kid.getCertificationAlgorithmMode())
		{
			case DES_CBC:
			case AES_CMAC:
			case CRC_16:
				algMode = 0;
				break;
			case TRIPLE_DES_CBC_2_KEYS:
			case CRC_32:
				algMode = 1;
				break;
			case TRIPLE_DES_CBC_3_KEYS:
				algMode = 2;
				break;
			case RESERVED:
				algMode = 3;
				break;
		}

		byte result = (byte)(algImpl + (algMode << 2) + (keysetID << 4));

		return result;
	}

	public static KID encode(CertificationMode certificationMode, byte kid) throws CodingException
	{
		KID result = new KID();

		final int algImpl = kid & 0x3;
		final int algMode = (kid & 0xC) >> 2;
		final byte keysetID = (byte) ((kid & 0xF0) >>> 4);

		AlgorithmImplementation resultAlgImpl = null;
		CertificationAlgorithmMode resultAlgMode = null;
		switch (certificationMode){
			case RC:
        switch (algImpl) {
          case 0:
            resultAlgImpl = AlgorithmImplementation.ALGORITHM_KNOWN_BY_BOTH_ENTITIES;
            break;
          case 1:
            resultAlgImpl = AlgorithmImplementation.CRC;
            switch (algMode) {
              case 0:
                resultAlgMode = CertificationAlgorithmMode.CRC_16;
                break;
              case 1:
                resultAlgMode = CertificationAlgorithmMode.CRC_32;
                break;
              default:
                throw new CodingException("Cannot encode KID(raw=" + Util.toHex(kid) + "). No such CRC algorithm mode(raw="
                    + Integer.toHexString(algMode));
            }
            break;
          case 3:
            resultAlgImpl = AlgorithmImplementation.PROPRIETARY_IMPLEMENTATIONS;
            break;
          default:
            throw new CodingException("Cannot encode KID(raw=" + Util.toHex(kid) + "). No such RC algorithm mode(raw="
                + Integer.toHexString(algImpl));
        }
        break;
			case CC:
				switch (algImpl) {
					case 0:
						resultAlgImpl = AlgorithmImplementation.ALGORITHM_KNOWN_BY_BOTH_ENTITIES;
						break;
					case 1:
						resultAlgImpl = AlgorithmImplementation.DES;
						switch (algMode) {
							case 0:
								resultAlgMode = CertificationAlgorithmMode.DES_CBC;
								break;
							case 1:
								resultAlgMode = CertificationAlgorithmMode.TRIPLE_DES_CBC_2_KEYS;
								break;
							case 2:
								resultAlgMode = CertificationAlgorithmMode.TRIPLE_DES_CBC_3_KEYS;
								break;
							case 3:
								resultAlgMode = CertificationAlgorithmMode.RESERVED;
								break;
							default:
								throw new CodingException("Cannot encode KID(raw=" + Util.toHex(kid) + "). No such DES algorithm mode(raw="
										+ Integer.toHexString(algMode));
						}
						break;
					case 2:
						resultAlgImpl = AlgorithmImplementation.AES;
						switch (algMode) {
							case 0:
								resultAlgMode = CertificationAlgorithmMode.AES_CMAC;
								break;
							default:
								throw new CodingException("Cannot encode KID(raw=" + Util.toHex(kid) + "). No such AES algorithm mode(raw="
										+ Integer.toHexString(algMode));
						}
						break;
					case 3:
						resultAlgImpl = AlgorithmImplementation.PROPRIETARY_IMPLEMENTATIONS;
						break;
					default:
						throw new CodingException("Cannot encode KID(raw=" + Util.toHex(kid) + "). No such algorithm implemetation(raw="
								+ Integer.toHexString(algImpl));
				}
				break;
			default:
				throw new CodingException("Cannot encode KID(raw=" + Util.toHex(kid) + "). No such certificationMode(raw="
						+ certificationMode);
		}

		if(keysetID  < 0 && keysetID > 0xF) {
      throw new CodingException("Cannot encode KID(raw=" + Util.toHex(kid) + "). KID keySetID cannot be <0 and >15");
    }

		result.setAlgorithmImplementation(resultAlgImpl);
		result.setCertificationAlgorithmMode(resultAlgMode);
		result.setKeysetID(keysetID);

		return result;
	}
}
