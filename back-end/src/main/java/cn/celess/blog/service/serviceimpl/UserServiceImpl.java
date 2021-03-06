package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.User;
import cn.celess.blog.entity.model.QiniuResponse;
import cn.celess.blog.entity.model.UserModel;
import cn.celess.blog.entity.request.LoginReq;
import cn.celess.blog.entity.request.UserReq;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.UserMapper;
import cn.celess.blog.service.MailService;
import cn.celess.blog.service.QiniuService;
import cn.celess.blog.service.UserService;
import cn.celess.blog.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.beans.Transient;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author : xiaohai
 * @date : 2019/03/30 18:41
 */
@Service
public class UserServiceImpl implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserMapper userMapper;
    @Autowired
    HttpServletRequest request;
    @Autowired
    MailService mailService;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    RedisUserUtil redisUserUtil;

    @Override
    @Transient
    public Boolean registration(String email, String password) {
        if (password.length() < 6 || password.length() > 16) {
            throw new MyException(ResponseEnum.PASSWORD_TOO_SHORT_OR_LONG);
        }
        if (!RegexUtil.emailMatch(email)) {
            throw new MyException(ResponseEnum.PARAMETERS_EMAIL_ERROR);
        }
        if (!RegexUtil.pwdMatch(password)) {
            throw new MyException(ResponseEnum.PARAMETERS_PWD_ERROR);
        }
        //验证码验证状态
        Boolean verifyStatus = (Boolean) request.getSession().getAttribute("verImgCodeStatus");
        if (verifyStatus == null || !verifyStatus) {
            throw new MyException(ResponseEnum.IMG_CODE_DIDNOTVERIFY);
        }
        if (userMapper.existsByEmail(email)) {
            throw new MyException(ResponseEnum.USERNAME_HAS_EXIST);
        }
        boolean b = userMapper.addUser(email, MD5Util.getMD5(password)) == 1;
        if (b) {
            String verifyId = UUID.randomUUID().toString().replaceAll("-", "");
            redisUtil.setEx(email + "-verify", verifyId, 2, TimeUnit.DAYS);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("邮箱验证");
            mailMessage.setText("欢迎注册小海博客,点击下面链接进行邮箱验证:\n https://www.celess.cn/emailVerify?email=" + email + "&verifyId=" +
                    verifyId + "\n该链接两日内有效,若失效了,请登录后台进行重新激活。");
            mailService.send(mailMessage);
        }
        return b;
    }

    @Override
    public UserModel login(LoginReq loginReq) {
        if (loginReq == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        if (!RegexUtil.emailMatch(loginReq.getEmail())) {
            throw new MyException(ResponseEnum.PARAMETERS_EMAIL_ERROR);
        }
        if (!RegexUtil.pwdMatch(loginReq.getPassword())) {
            throw new MyException(ResponseEnum.PARAMETERS_PWD_ERROR);
        }
        //获取redis缓存中登录失败次数
        String s = redisUtil.get(loginReq.getEmail() + "-passwordWrongTime");
        if (s != null) {
            if (Integer.parseInt(s) == 5) {
                throw new MyException(ResponseEnum.LOGIN_LATER, loginReq.getEmail());
            }
        }
        User user = null;
        user = userMapper.findByEmail(loginReq.getEmail());
        String token = null;
        // 密码比对
        if (user == null) {
            // 用户不存在
            throw new MyException(ResponseEnum.USER_NOT_EXIST);
        }
        if (user.getPwd().equals(MD5Util.getMD5(loginReq.getPassword()))) {
            logger.info("====> {}  进行权限认证  状态：登录成功 <====", loginReq.getEmail());
            userMapper.updateLoginTime(loginReq.getEmail(), new Date());
            redisUtil.delete(loginReq.getEmail() + "-passwordWrongTime");
            // redis 标记
            redisUtil.setEx(loginReq.getEmail() + "-login", JSONObject.fromObject(user).toString(),
                    (loginReq.getIsRememberMe() ? JwtUtil.EXPIRATION_LONG_TIME : JwtUtil.EXPIRATION_SHORT_TIME), TimeUnit.MILLISECONDS);
            token = jwtUtil.generateToken(user, loginReq.getIsRememberMe());
        } else {
            logger.info("====> {}  进行权限认证  状态：登录失败 <====", loginReq.getEmail());
            request.getSession().removeAttribute("code");
            //登录失败
            //设置登录失败的缓存
            if (s == null) {
                redisUtil.setEx(loginReq.getEmail() + "-passwordWrongTime", "1", 2, TimeUnit.HOURS);
                s = "0";
            }
            int count = Integer.parseInt(s);
            //登录次数++
            count++;
            //更新登录失败的次数
            redisUtil.setEx(loginReq.getEmail() + "-passwordWrongTime", count + "", 2, TimeUnit.HOURS);
            throw new MyException(ResponseEnum.LOGIN_FAILURE);
        }
        UserModel trans = trans(user);
        trans.setToken(token);
        return trans;

    }

    @Override
    public Object logout() {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return "注销登录成功";
        }
        String email = jwtUtil.getUsernameFromToken(token);
        if (redisUtil.hasKey(email + "-login")) {
            redisUtil.delete(email + "-login");
        }
        return "注销登录成功";
    }

    @Override
    public UserModel update(String desc, String displayName) {
        User user = redisUserUtil.get(request);
        user.setDesc(desc);
        user.setDisplayName(displayName);

        userMapper.updateInfo(desc, displayName, user.getId());
        redisUserUtil.set(user);
        return trans(user);
    }

    @Override
    public String getUserRoleByEmail(String email) {
        String role = userMapper.getRoleByEmail(email);
        if (role == null) {
            throw new MyException(ResponseEnum.USER_NOT_EXIST);
        }
        return role;
    }

    @Override
    public User getUserInfoByEmail(String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new MyException(ResponseEnum.USER_NOT_EXIST);
        }
        return user;
    }

    @Override
    public String getAvatarImg(long id) {
        return userMapper.getAvatarImgUrlById(id);
    }

    @Override
    public Object updateUserAavatarImg(InputStream is, String mime) {
        User user = redisUserUtil.get(request);
        QiniuResponse upload = qiniuService.uploadFile(is, user.getEmail() + "_" + user.getId() + mime.toLowerCase());
        user.setAvatarImgUrl(upload.key);
        userMapper.updateAvatarImgUrl(upload.key, user.getId());
        redisUserUtil.set(user);
        return ResponseUtil.success(user.getAvatarImgUrl());
    }

    @Override
    public UserModel getUserInfoBySession() {
        User user = redisUserUtil.get(request);
        return trans(user);
    }

    @Override
    public boolean isExistOfEmail(String email) {
        return userMapper.existsByEmail(email);
    }

    @Override
    public String getNameById(long id) {
        String name = userMapper.getDisPlayName(id);
        if (name == null) {
            name = userMapper.getEmail(id);
            if (name == null) {
                throw new MyException(ResponseEnum.USER_NOT_EXIST);
            }
        }
        return name;
    }

    /**
     * 找回密码
     */
    @Override
    public Object sendResetPwdEmail(String email) {
        if (!RegexUtil.emailMatch(email)) {
            throw new MyException(ResponseEnum.PARAMETERS_EMAIL_ERROR);
        }

        User user = userMapper.findByEmail(email);
        if (user == null) {
            return "发送成功！";
        }

        if (!user.getEmailStatus()) {
            throw new MyException(ResponseEnum.USEREMAIL_NOT_VERIFY);
        }

        String verifyId = UUID.randomUUID().toString().replaceAll("-", "");

        redisUtil.setEx(user.getEmail() + "-resetPwd", verifyId, 2, TimeUnit.DAYS);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("密码重置");
        mailMessage.setText("点击下面链接进行重置密码:\n https://www.celess.cn/resetPwd?email=" + email + "&verifyId=" + verifyId);

        mailService.send(mailMessage);
        return "发送成功!";
    }

    //TODO
    @Override
    public Object sendVerifyEmail(String email) {
        if (!RegexUtil.emailMatch(email)) {
            throw new MyException(ResponseEnum.PARAMETERS_EMAIL_ERROR);
        }

        User user = userMapper.findByEmail(email);
        if (user == null) {
            return "发送成功！";
        }

        if (user.getEmailStatus()) {
            return "已经验证过了！";
        }

        String verifyId = UUID.randomUUID().toString().replaceAll("-", "");

        redisUtil.setEx(user.getEmail() + "-verify", verifyId, 2, TimeUnit.DAYS);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("邮箱验证");
        mailMessage.setText("点击下面链接进行邮箱验证:\n https://www.celess.cn/emailVerify?email=" + email + "&verifyId=" + verifyId);
        mailService.send(mailMessage);
        return "发送成功!";
    }

    @Override
    public Object verifyEmail(String verifyId, String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new MyException(ResponseEnum.FAILURE);
        }
        if (user.getEmailStatus()) {
            throw new MyException(ResponseEnum.FAILURE.getCode(), "邮箱已验证过了");
        }
        String verifyIdFromCache = redisUtil.get(user.getEmail() + "-verify");
        if (verifyIdFromCache == null) {
            throw new MyException(ResponseEnum.FAILURE.getCode(), "验证链接无效");
        }
        if (verifyIdFromCache.equals(verifyId)) {
            userMapper.updateEmailStatus(email, true);
            redisUtil.delete(user.getEmail() + "-verify");
            user.setEmailStatus(true);
            redisUserUtil.set(user);
            return "验证成功";
        } else {
            throw new MyException(ResponseEnum.FAILURE);
        }
    }

    @Override
    public Object reSetPwd(String verifyId, String email, String pwd) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new MyException(ResponseEnum.USER_NOT_EXIST);
        }
        if (!RegexUtil.pwdMatch(pwd)) {
            throw new MyException(ResponseEnum.PARAMETERS_PWD_ERROR);
        }
        if (!user.getEmailStatus()) {
            throw new MyException(ResponseEnum.USEREMAIL_NOT_VERIFY);
        }
        String resetPwdIdFromCache = redisUtil.get(user.getEmail() + "-resetPwd");
        if (resetPwdIdFromCache == null) {
            throw new MyException(ResponseEnum.FAILURE.getCode(), "请先获取重置密码的邮件");
        }
        if (resetPwdIdFromCache.equals(verifyId)) {
            if (MD5Util.getMD5(pwd).equals(user.getPwd())) {
                throw new MyException(ResponseEnum.PWD_SAME);
            }
            userMapper.updatePwd(email, MD5Util.getMD5(pwd));
            redisUtil.delete(user.getEmail() + "-resetPwd");
            return "验证成功";
        } else {
            throw new MyException(ResponseEnum.FAILURE.getCode(), "标识码不一致");
        }
    }

    @Override
    public Object deleteUser(Integer[] id) {
        JSONArray status = new JSONArray();
        if (id == null || id.length == 0) {
            return null;
        }
        for (Integer integer : id) {
            String role = userMapper.getRoleById(integer);
            int deleteResult = 0;
            JSONObject deleteStatus = new JSONObject();
            deleteStatus.put("id", integer);
            // 管理员账户不可删
            if ("admin".equals(role)) {
                deleteStatus.put("msg", "用户为管理员，不可删除");
                deleteStatus.put("status", false);
                status.add(deleteStatus);
                logger.info("删除用户id为{}的用户，删除状态{}, 原因：用户为管理员，不可删除", integer, false);
                continue;
            }
            // 非管理员账户
            deleteResult = userMapper.delete(integer);
            deleteStatus.put("status", deleteResult == 1);
            logger.info("删除用户id为{}的用户，删除状态{}", integer, deleteResult == 1);
            if (deleteResult == 0) {
                deleteStatus.put("msg", "用户不存在");
            }
            status.add(deleteStatus);
        }
        return status;
    }

    @Override
    public PageInfo<UserModel> getUserList(Integer page, Integer count) {
        PageHelper.startPage(page, count);
        List<User> all = userMapper.findAll();
        PageInfo pageInfo = PageInfo.of(all);
        List<UserModel> modelList = new ArrayList<>();
        all.forEach(user -> modelList.add(trans(user)));
        pageInfo.setList(modelList);
        return pageInfo;
    }

    @Override
    public UserModel adminUpdate(UserReq userReq) {
        if (userReq == null || userReq.getId() == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        User user = userMapper.findById(userReq.getId());
        // 设置数据
        if (userReq.getDesc() != null) {
            user.setDesc(userReq.getDesc());
        }
        if (userReq.getDisplayName() != null) {
            user.setDisplayName(userReq.getDisplayName());
        }
        if (userReq.getEmailStatus() != null) {
            user.setEmailStatus(userReq.getEmailStatus());
        }
        if (userReq.getPwd() != null) {
            if (userReq.getPwd().length() < 6 || userReq.getPwd().length() > 16) {
                throw new MyException(ResponseEnum.PASSWORD_TOO_SHORT_OR_LONG);
            }
            if (!RegexUtil.pwdMatch(userReq.getPwd())) {
                throw new MyException(ResponseEnum.PARAMETERS_PWD_ERROR);
            }
            user.setPwd(MD5Util.getMD5(userReq.getPwd()));
        }
        if (userReq.getRole() != null) {
            // TODO:用enum存放角色分类
            if ("user".equals(userReq.getRole()) || "admin".equals(userReq.getRole())) {
                user.setRole(userReq.getRole());
            } else {
                throw new MyException(ResponseEnum.PARAMETERS_ERROR);
            }
        }
        if (userReq.getEmail() != null) {
            if (!RegexUtil.emailMatch(userReq.getEmail())) {
                throw new MyException(ResponseEnum.PARAMETERS_EMAIL_ERROR);
            }
            // TODO :: 邮件提醒
            user.setEmail(userReq.getEmail());
        }
        // 数据写入
        int updateResult = userMapper.update(user);
        if (updateResult == 0) {
            throw new MyException(ResponseEnum.FAILURE);
        }
        if (redisUserUtil.get(request).getId().equals(userReq.getId())) {
            redisUserUtil.set(user);
        }
        logger.info("修改了用户 [id={}] 的用户的资料", userReq.getId());
        return trans(user);
    }

    @Override
    public boolean getStatusOfEmail(String email) {
        return userMapper.existsByEmail(email);
    }

    private UserModel trans(User u) {
        UserModel user = new UserModel();
        user.setId(u.getId());
        user.setAvatarImgUrl(u.getAvatarImgUrl() == null ? null : "http://cdn.celess.cn/" + u.getAvatarImgUrl());
        user.setEmail(u.getEmail());
        user.setDesc(u.getDesc());
        user.setDisplayName(u.getDisplayName() == null ? u.getEmail() : u.getDisplayName());
        user.setEmailStatus(u.getEmailStatus());
        user.setRecentlyLandedDate(DateFormatUtil.get(u.getRecentlyLandedDate()));
        user.setRole(u.getRole());
        return user;
    }
}
