package com.php.design_patten_demo.service;

import com.php.design_patten_demo.entity.ApprovalRequest;

import java.util.List;
import java.util.Optional;

public interface ApprovalService {
    ApprovalRequest submitRequest(Long applicantId, Long productId, Integer quantity, String reason);
    List<ApprovalRequest> findByApplicantId(Long applicantId);
    List<ApprovalRequest> findAll();
    Optional<ApprovalRequest> findById(Long id);
    boolean approveRequest(Long requestId, Long approverId, String comment);
    boolean rejectRequest(Long requestId, Long approverId, String comment);
    boolean cancelRequest(Long requestId, Long operatorId, String comment);
}
