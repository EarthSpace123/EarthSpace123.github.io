package com.php.design_patten_demo.pattern.chain;

import com.php.design_patten_demo.entity.ApprovalRequest;
import com.php.design_patten_demo.pattern.singleton.SystemConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ManagerApprovalHandler extends ApprovalHandler {
    @Override
    public boolean handle(ApprovalRequest request) {
        int maxQuantity = Integer.parseInt(SystemConfig.getInstance().get("approval.manager.limit"));
        if (request.getQuantity() <= maxQuantity) {
            log.info("库存经理不能通过审批，需要转交老板: {}", request.getRequestNo());
            if (nextHandler != null) {
                return nextHandler.handle(request);
            }
            return false;
        } else if (nextHandler != null) {
            return nextHandler.handle(request);
        }
        return false;
    }
}
