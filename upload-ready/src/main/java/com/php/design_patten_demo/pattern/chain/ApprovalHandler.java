package com.php.design_patten_demo.pattern.chain;

import com.php.design_patten_demo.entity.ApprovalRequest;

public abstract class ApprovalHandler {
    protected ApprovalHandler nextHandler;

    public ApprovalHandler setNext(ApprovalHandler handler) {
        this.nextHandler = handler;
        return handler;
    }

    public abstract boolean handle(ApprovalRequest request);
}
