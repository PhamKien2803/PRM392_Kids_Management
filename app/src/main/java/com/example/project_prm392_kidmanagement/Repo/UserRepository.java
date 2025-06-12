package com.example.project_prm392_kidmanagement.Repo;
import android.content.Context;

import com.example.project_prm392_kidmanagement.DAO.UserDao;
import com.example.project_prm392_kidmanagement.Entity.User;

import java.util.List;

public class UserRepository implements IUserRepository {
    private final UserDao userDao;
    public UserRepository(Context context){
        userDao = new UserDao(context);
    }
    @Override
    public boolean register(User user) {
        return userDao.insert(user) > 0;
    }
    @Override
    public boolean login(String username, String password) {
        return userDao.validate(username, password);
    }
    @Override
    public List<User> getAllUsers() {
        return userDao.getAll();
    }
    public boolean update(User user) {
        return userDao.update(user);
    }
    public boolean delete(int userId) {
        return userDao.delete(userId);
    }
}
