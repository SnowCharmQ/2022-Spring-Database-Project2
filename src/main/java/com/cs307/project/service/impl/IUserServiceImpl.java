package com.cs307.project.service.impl;

import com.cs307.project.controller.ex.PasswordNotMatchException;
import com.cs307.project.controller.ex.UserNotFoundException;
import com.cs307.project.controller.ex.UsernameDuplicatedException;
import com.cs307.project.entity.User;
import com.cs307.project.mapper.UserMapper;
import com.cs307.project.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Service
public class IUserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void reg(User user) {
        User old = userMapper.selectByName(user.getUsername());
        if (old != null) throw new UsernameDuplicatedException("The user has been created!");
        String oldPwd = user.getPwd();
        String salt = UUID.randomUUID().toString().toUpperCase();
        String md5pwd = getMD5Pwd(oldPwd, salt);
        user.setPwd(md5pwd);
        user.setSalt(salt);
        if (user.getUsername().equals("super")) user.setSuper(true);
        user.setCanInsert(true);
        user.setCanDelete(true);
        user.setCanUpdate(true);
        user.setCanSelect(true);
        userMapper.insertUser(user);
    }

    @Override
    public User login(String username, String pwd) {
        User user = userMapper.selectByName(username);
        if (user == null) throw new UserNotFoundException("User doesn't exist!");
        String salt = user.getSalt();
        String md5pwd = getMD5Pwd(pwd, salt);
        if (!user.getPwd().equals(md5pwd)) throw new PasswordNotMatchException("The password is incorrect!");
        return user;
    }

    @Override
    public void changePwd(String username, String oldPwd, String newPwd) {
        User user = userMapper.selectByName(username);
        if (user == null) throw new UserNotFoundException("User doesn't exist!");
        String salt = user.getSalt();
        String oldMd5Pwd = getMD5Pwd(oldPwd, salt);
        if (!oldMd5Pwd.equals(user.getPwd())) throw new PasswordNotMatchException("The password is incorrect");
        String newPwd5Password = getMD5Pwd(newPwd, salt);
        userMapper.updatePwd(username, newPwd5Password);
    }

    @Override
    public List<User> select() {
        return userMapper.selectUser();
    }

    @Override
    public void manage(String data) {
        String[] users = data.split("-");
        for (String user : users) {
            String username = user.substring(0, user.length() - 4);
            char[] choice = user.substring(user.length() - 4).toCharArray();
            if (choice[0] == '1') userMapper.grantInsert(username);
            else userMapper.revokeInsert(username);
            if (choice[1] == '1') userMapper.grantDelete(username);
            else userMapper.revokeDelete(username);
            if (choice[2] == '1') userMapper.grantUpdate(username);
            else userMapper.revokeUpdate(username);
            if (choice[3] == '1') userMapper.grantSelect(username);
            else userMapper.revokeSelect(username);
        }
    }

    @Override
    public User selectUser(String username) {
        return userMapper.selectByName(username);
    }

    private String getMD5Pwd(String pwd, String salt) {
        return DigestUtils.md5DigestAsHex((salt + pwd + salt).getBytes()).toUpperCase();
    }
}
