package com.php.design_patten_demo.service.impl;

import com.php.design_patten_demo.entity.ApprovalRequest;
import com.php.design_patten_demo.entity.Product;
import com.php.design_patten_demo.entity.User;
import com.php.design_patten_demo.enums.ApprovalStatus;
import com.php.design_patten_demo.enums.UserRole;
import com.php.design_patten_demo.repository.ApprovalRepository;
import com.php.design_patten_demo.service.ApprovalService;
import com.php.design_patten_demo.service.NotificationService;
import com.php.design_patten_demo.service.ProductService;
import com.php.design_patten_demo.service.UserService;
import com.php.design_patten_demo.pattern.chain.ApprovalChain;
import com.php.design_patten_demo.pattern.singleton.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ApprovalChain approvalChain;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public ApprovalRequest submitRequest(Long applicantId, Long productId, Integer quantity, String reason) {
        Product product = productService.findById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        ApprovalRequest request = new ApprovalRequest();
        request.setRequestNo(generateRequestNo());
        request.setApplicantId(applicantId);
        request.setProductId(productId);
        request.setQuantity(quantity);
        request.setAmount(product.getPrice().multiply(new BigDecimal(quantity)));
        request.setReason(reason);
        request.setStatus(ApprovalStatus.PENDING);

        ApprovalRequest savedRequest = approvalRepository.save(request);

        notificationService.createNotification(applicantId, "APPROVAL", "进货申请已提交", 
            "您的进货申请 " + savedRequest.getRequestNo() + " 已提交，等待审批", savedRequest.getId());

        return savedRequest;
    }

    @Override
    public List<ApprovalRequest> findByApplicantId(Long applicantId) {
        return approvalRepository.findByApplicantId(applicantId);
    }

    @Override
    public List<ApprovalRequest> findAll() {
        return approvalRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Optional<ApprovalRequest> findById(Long id) {
        return approvalRepository.findById(id);
    }

    @Override
    @Transactional
    public boolean approveRequest(Long requestId, Long approverId, String comment) {
        return approvalRepository.findById(requestId)
            .map(request -> {
                if (request.getStatus() != ApprovalStatus.PENDING) {
                    throw new RuntimeException("该申请已被处理");
                }

                User approver = userService.findById(approverId);
                if (approver == null) {
                    throw new RuntimeException("审批人不存在");
                }

                if (approver.getRole() == UserRole.MANAGER) {
                    throw new RuntimeException("库存经理不能通过审批，只能拒绝审批或转交老板审批");
                }

                boolean approved = approvalChain.process(request);
                if (approved) {
                    request.setStatus(ApprovalStatus.APPROVED);
                    request.setApproverId(approverId);
                    request.setApprovalComment(comment);
                    approvalRepository.save(request);

                    productService.updateStock(request.getProductId(), request.getQuantity());

                    notificationService.createNotification(request.getApplicantId(), "APPROVAL", "进货申请已通过", 
                        "您的进货申请 " + request.getRequestNo() + " 已通过审批", request.getId());

                    return true;
                }
                return false;
            })
            .orElse(false);
    }

    @Override
    @Transactional
    public boolean rejectRequest(Long requestId, Long approverId, String comment) {
        return approvalRepository.findById(requestId)
            .map(request -> {
                if (request.getStatus() != ApprovalStatus.PENDING) {
                    throw new RuntimeException("该申请已被处理");
                }

                User approver = userService.findById(approverId);
                if (approver == null) {
                    throw new RuntimeException("审批人不存在");
                }

                request.setStatus(ApprovalStatus.REJECTED);
                request.setApproverId(approverId);
                request.setApprovalComment(comment);
                approvalRepository.save(request);

                notificationService.createNotification(request.getApplicantId(), "APPROVAL", "进货申请已拒绝", 
                    "您的进货申请 " + request.getRequestNo() + " 已被拒绝。原因: " + comment, request.getId());

                return true;
            })
            .orElse(false);
    }

    @Override
    @Transactional
    public boolean cancelRequest(Long requestId, Long operatorId, String comment) {
        return approvalRepository.findById(requestId)
            .map(request -> {
                if (request.getStatus() != ApprovalStatus.APPROVED) {
                    throw new RuntimeException("只能取消已批准的申请");
                }

                request.setStatus(ApprovalStatus.CANCELLED);
                request.setApproverId(operatorId);
                request.setApprovalComment(comment);
                approvalRepository.save(request);

                notificationService.createNotification(request.getApplicantId(), "APPROVAL", "进货申请已取消", 
                    "您的进货申请 " + request.getRequestNo() + " 已被取消。原因: " + comment, request.getId());

                return true;
            })
            .orElse(false);
    }

    private String generateRequestNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "REQ" + timestamp + random;
    }
}
