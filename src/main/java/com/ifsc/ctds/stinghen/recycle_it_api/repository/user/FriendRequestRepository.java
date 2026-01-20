package com.ifsc.ctds.stinghen.recycle_it_api.repository.user;

import com.ifsc.ctds.stinghen.recycle_it_api.models.user.FriendRequest;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByTarget(User target);
    List<FriendRequest> findBySender(User sender);
    boolean existsBySenderAndTarget(User sender, User target);
}
