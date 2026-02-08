package com.php.design_patten_demo.pattern.chain;

import com.php.design_patten_demo.entity.ApprovalRequest;
import org.springframework.stereotype.Component;

@Component
public class ApprovalChain {
    private final ApprovalHandler handler;

    public ApprovalChain() {
        this.handler = new ManagerApprovalHandler();
        handler.setNext(new BossApprovalHandler());
    }

    public boolean process(ApprovalRequest request) {
        return handler.handle(request);
    }
}
