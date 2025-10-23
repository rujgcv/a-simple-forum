package com.fufunode.service.impl;

import com.fufunode.service.VerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Value("${api.box.id}")
    private String boxId;

    @Value("${api.box.key}")
    private String boxKey;

    public static final String url1 = "http://101.35.2.25/api/xingkong/xk1get.php";
    public static final String url2 = "http://101.35.2.25/api/xingkong/xk1yz.php";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Map<String, Object> getCode() {
        try {
            String fullUrl = url1 + "?id=" + boxId + "&key=" + boxKey;

            ResponseEntity<Map> response = restTemplate.getForEntity(fullUrl, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // {"code":200,"imgurl":"...","md5key":"6ea7a5516322b25a527f4250fda5ce51"}
                return response.getBody();
            } else {
                throw new RuntimeException("图形验证码API调用失败: " + response.getStatusCode());
            }

        } catch (Exception e) {
            // 处理异常，返回默认值或抛出异常
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("code", 0);
            fallback.put("status", 500);
            return fallback;
        }
    }

    @Override
    public Boolean verifyCode(String userCode,String md5Key) {
        String fullUrl = url2 + "?id=" + boxId + "&key=" + boxKey + "&code=" + userCode + "&md5key=" + md5Key;

        ResponseEntity<Map> response = restTemplate.getForEntity(fullUrl, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Integer code = (Integer)response.getBody().get("code");
            if(code == 200) return true;
        }else {
            throw new RuntimeException("图形验证码API调用失败: " + response.getStatusCode());
        }
        return false;
    }

}
