package com.pradeep.smart_parking_api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pradeep.smart_parking_api.model.Users;
@Repository
public interface UsersRepository extends JpaRepository<Users,Long>{
    Users findByEmail(String email);
}
