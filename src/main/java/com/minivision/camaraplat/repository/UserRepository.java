package com.minivision.camaraplat.repository;

import com.minivision.camaraplat.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.List;
public interface UserRepository extends PagingAndSortingRepository<User,Long> {

    List<User> findAll();

    User findByUsernameAndPassword(String username, String password);
}
