package com.xxx.test;

import com.xxx.common.auth.rsa.RsaUtils;

public class GenerateUtil {

    static String pub = "rsa_key.pub";
    static String pri = "rsa_key";

    public static void main(String[] args) throws Exception {
        RsaUtils.generateKey(pub,pri,"xxx",2048);
    }
}
