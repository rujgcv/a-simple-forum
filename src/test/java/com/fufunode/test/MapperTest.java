package com.fufunode.test;

import com.fufunode.mapper.PedingMapper;
import com.fufunode.mapper.TabMapper;
import com.fufunode.pojo.entity.PedingTab;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MapperTest {

    @Autowired
    private PedingMapper pedingMapper;
    @Autowired
    private TabMapper tabMapper;

    @Test
    public void testPedingMapper(){
        System.out.println(tabMapper.verifyType(4L));
    }
}
