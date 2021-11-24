package com.xxx.common.auth.rsa;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@Component
public class JwtProperties {

    private static String pubKeyPath = "/rsa_key.pub";
    private static String priKeyPath = "/rsa_key";

    private PublicKey publicKey;
    private PrivateKey privateKey;

    private TokenPojo cookie = new TokenPojo();



    @Data
    public class TokenPojo{
        private Integer expire;
        private Integer refTime;
        private String tokenName;
    }

    /**
     * 指定初始化方法
     * @throws Exception
     */
    @PostConstruct
    public void initMethod() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

}