package com.php.design_patten_demo.pattern.chain;

import com.php.design_patten_demo.entity.ApprovalRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BossApprovalHandler extends ApprovalHandler {
    @Override
    public boolean handle(ApprovalRequest request) {
        log.info("老板审批通过: {}", request.getRequestNo());
        return true;
    }
}
