package com.minivision.camaraplat.repository;

import com.minivision.camaraplat.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yao on 2017/3/12.
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User,Long> {

    List<User> findAll();

    User findByUsernameAndPassword(String username, String password);
}
