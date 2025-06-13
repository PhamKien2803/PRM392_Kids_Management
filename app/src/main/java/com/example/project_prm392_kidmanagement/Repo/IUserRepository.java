package com.example.project_prm392_kidmanagement.Repo;

import com.example.project_prm392_kidmanagement.Entity.User;
import java.util.List;

public interface IUserRepository {
    boolean register(User user);
    boolean login(String username, String password);
    List<User> getAllUsers();
}
