package com.ifsc.ctds.stinghen.recycle_it_api.repository.user;

import com.ifsc.ctds.stinghen.recycle_it_api.models.user.FriendRequest;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    
    List<FriendRequest> findByTargetId(Long targetId);
    Page<FriendRequest> findByTargetId(Long targetId, Pageable pageable);
    
    List<FriendRequest> findBySenderId( Long senderId);
    Page<FriendRequest> findBySenderId( Long senderId, Pageable pageable);
    
    List<FriendRequest> findByTarget_Credential_Email(String targetEmail);
    Page<FriendRequest> findByTarget_Credential_Email(String targetEmail, Pageable pageable);
    
    List<FriendRequest> findBySender_Credential_Email(String senderEmail);
    Page<FriendRequest> findBySender_Credential_Email(String senderEmail, Pageable pageable);
    
    FriendRequest findByTargetIdAndSender_Credential_Email(Long targetId, String senderEmail);
    
    boolean existsByTargetId(Long targetId);
    boolean existsBySenderId(Long senderId);
    
    boolean existsByTarget_Credential_Email(String targetEmail);
    boolean existsBySender_Credential_Email(String senderEmail);
    
    boolean existsBySenderIdAndTargetId(Long senderId, Long targetId);
    boolean existsBySender_Credential_EmailAndTarget_Credential_Email(String senderEmail, String targetEmail);
}
