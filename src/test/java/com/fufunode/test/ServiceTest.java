package com.fufunode.test;

import com.fufunode.service.VerifyCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Test
    public void testVerifyCode(){
        System.out.println(verifyCodeService.getCode());
        System.out.println((verifyCodeService.getCode().get("md5key")));
    }
}
