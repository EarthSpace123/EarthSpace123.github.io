package com.php.design_patten_demo.controller;

import com.php.design_patten_demo.entity.ApprovalRequest;
import com.php.design_patten_demo.entity.User;
import com.php.design_patten_demo.enums.UserRole;
import com.php.design_patten_demo.service.ApprovalService;
import com.php.design_patten_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private UserService userService;

    @GetMapping("/approvals")
    public String approvalsPage(@RequestParam Long userId, Model model) {
        User user = userService.findById(userId);
        List<ApprovalRequest> requests;
        
        if (user != null && (user.getRole() == UserRole.MANAGER || user.getRole() == UserRole.BOSS)) {
            requests = approvalService.findAll();
        } else {
            requests = approvalService.findByApplicantId(userId);
        }
        
        model.addAttribute("requests", requests);
        model.addAttribute("userId", userId);
        return "approval/list";
    }

    @GetMapping("/approval/create")
    public String createApprovalPage(@RequestParam Long userId, Model model) {
        model.addAttribute("userId", userId);
        return "approval/create";
    }

    @GetMapping("/api/approvals")
    @ResponseBody
    public ResponseEntity<List<ApprovalRequest>> getApprovals(@RequestParam Long userId) {
        User user = userService.findById(userId);
        List<ApprovalRequest> requests;
        
        if (user != null && (user.getRole() == UserRole.MANAGER || user.getRole() == UserRole.BOSS)) {
            requests = approvalService.findAll();
        } else {
            requests = approvalService.findByApplicantId(userId);
        }
        
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/api/approvals/{id}")
    @ResponseBody
    public ResponseEntity<ApprovalRequest> getApprovalById(@PathVariable Long id) {
        return approvalService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/approvals")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitApproval(@RequestParam Long applicantId,
                                                               @RequestParam Long productId,
                                                               @RequestParam Integer quantity,
                                                               @RequestParam String reason) {
        try {
            ApprovalRequest request = approvalService.submitRequest(applicantId, productId, quantity, reason);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "进货申请提交成功");
            result.put("request", request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("/api/approvals/{id}/approve")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> approveRequest(@PathVariable Long id,
                                                                @RequestParam Long approverId,
                                                                @RequestParam(required = false) String comment) {
        try {
            boolean approved = approvalService.approveRequest(id, approverId, comment);
            Map<String, Object> result = new HashMap<>();
            if (approved) {
                result.put("success", true);
                result.put("message", "审批通过");
            } else {
                result.put("success", false);
                result.put("message", "审批失败");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("/api/approvals/{id}/reject")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> rejectRequest(@PathVariable Long id,
                                                               @RequestParam Long approverId,
                                                               @RequestParam String comment) {
        try {
            boolean rejected = approvalService.rejectRequest(id, approverId, comment);
            Map<String, Object> result = new HashMap<>();
            if (rejected) {
                result.put("success", true);
                result.put("message", "已拒绝");
            } else {
                result.put("success", false);
                result.put("message", "操作失败");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("/api/approvals/{id}/cancel")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cancelRequest(@PathVariable Long id,
                                                              @RequestParam Long operatorId,
                                                              @RequestParam String comment) {
        try {
            boolean cancelled = approvalService.cancelRequest(id, operatorId, comment);
            Map<String, Object> result = new HashMap<>();
            if (cancelled) {
                result.put("success", true);
                result.put("message", "已取消");
            } else {
                result.put("success", false);
                result.put("message", "操作失败");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }
}
