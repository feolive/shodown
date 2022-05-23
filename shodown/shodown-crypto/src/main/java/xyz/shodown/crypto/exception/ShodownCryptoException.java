package xyz.shodown.crypto.exception;

import xyz.shodown.common.consts.Symbols;
import xyz.shodown.crypto.enums.CryptoErr;

/**
 * @ClassName: XhyjCryptoException
 * @Description: 加解密异常
 * @Author: wangxiang
 * @Date: 2021/4/20 14:55
 */
public class ShodownCryptoException extends RuntimeException{

    public ShodownCryptoException(String message) {
        super(message);
    }

    public ShodownCryptoException(CryptoErr cryptoErr) {
        String code = cryptoErr.getCode();
        String err = cryptoErr.getErrMsg();
        String msg = Symbols.LEFT_SQUARE_BRACKET+code+Symbols.RIGHT_SQUARE_BRACKET+Symbols.SPACE+err;
        throw new ShodownCryptoException(msg);
    }

}
