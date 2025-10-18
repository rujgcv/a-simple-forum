package com.fufunode.controller;

import com.fufunode.pojo.dto.PedingTabDTO;
import com.fufunode.result.Result;
import com.fufunode.service.PedingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/peding")
public class PedingController {

    @Autowired
    private PedingService pedingService;

    //新增待审核贴吧
    @PostMapping("/add")
    public Result add(@RequestBody PedingTabDTO pedingTabDTO){
        log.info("新增待审核贴吧:{}",pedingTabDTO);
        return pedingService.add(pedingTabDTO);
    }
}
