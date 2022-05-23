package xyz.shodown.crypto.exception;

import xyz.shodown.common.consts.Symbols;
import xyz.shodown.crypto.enums.CryptoErr;

/**
 * @ClassName: VerifySignException
 * @Description: 校验签名异常
 * @Author: wangxiang
 * @Date: 2021/5/27 14:04
 */
public class VerifySignException extends RuntimeException{

    public VerifySignException(String message) {
        super(message);
    }

    public VerifySignException(CryptoErr cryptoErr){
        String code = cryptoErr.getCode();
        String err = cryptoErr.getErrMsg();
        String msg = Symbols.LEFT_SQUARE_BRACKET+code+Symbols.RIGHT_SQUARE_BRACKET+Symbols.SPACE+err;
        throw new VerifySignException(msg);
    }

    public VerifySignException(String topic,CryptoErr cryptoErr){
        String code = cryptoErr.getCode();
        String err = cryptoErr.getErrMsg();
        String msg = Symbols.LEFT_SQUARE_BRACKET+code+Symbols.RIGHT_SQUARE_BRACKET+Symbols.SPACE+topic+Symbols.MINUS+err;
        throw new VerifySignException(msg);
    }
}
