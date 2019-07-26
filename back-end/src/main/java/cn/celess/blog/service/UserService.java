package cn.celess.blog.service;

import cn.celess.blog.entity.User;
import cn.celess.blog.entity.model.UserModel;
import cn.celess.blog.entity.request.LoginReq;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author : xiaohai
 * @date : 2019/03/30 18:40
 */
@Service
public interface UserService {
    Boolean registration(String email, String password);

    Object login(LoginReq loginReq);

    Object logout();

    boolean delete(long id);

    String getAvatarImg(long id);

    UserModel update(String desc, String displayName);

    UserModel retrieveByID(long id);

    Object updateUserAavatarImg(InputStream is, String mime);

    UserModel getUserInfoBySession();

    String getUserRoleByEmail(String email);

    User getUserInfoByEmail(String email);

    boolean isExistOfEmail(String email);

    String getNameById(long id);//优先返回trueName  否则返回username

    Object sendResetPwdEmail(String email);

    Object sendVerifyEmail(String email);

    Object verifyEmail(String verifyId, String email);

    Object reSetPwd(String verifyId, String email, String pwd);
}
