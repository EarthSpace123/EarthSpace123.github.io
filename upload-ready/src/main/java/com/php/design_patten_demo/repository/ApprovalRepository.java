package com.php.design_patten_demo.repository;

import com.php.design_patten_demo.entity.ApprovalRequest;
import com.php.design_patten_demo.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalRepository extends JpaRepository<ApprovalRequest, Long> {
    List<ApprovalRequest> findByApplicantId(Long applicantId);
    Optional<ApprovalRequest> findByRequestNo(String requestNo);
    List<ApprovalRequest> findByStatus(ApprovalStatus status);
    List<ApprovalRequest> findAllByOrderByCreatedAtDesc();
}
