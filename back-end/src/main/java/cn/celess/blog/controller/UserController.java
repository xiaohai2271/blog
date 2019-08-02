package cn.celess.blog.controller;

import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.request.LoginReq;
import cn.celess.blog.service.UserService;
import cn.celess.blog.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author : xiaohai
 * @date : 2019/03/30 20:37
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;



    @PostMapping("/login")
    public Response login(@RequestBody LoginReq loginReq) {
        return ResponseUtil.success(userService.login(loginReq));
    }

    @PostMapping("/registration")
    public Response registration(@RequestParam("email") String email,
                                 @RequestParam("password") String password) {
        return ResponseUtil.success(userService.registration(email, password));
    }

    @GetMapping("/logout")
    public Response logout() {
        return ResponseUtil.success(userService.logout());
    }

    @PutMapping("/user/userInfo/update")
    public Response updateInfo(String desc, String displayName) {
        return ResponseUtil.success(userService.update(desc, displayName));
    }

    @DeleteMapping("/user/userInfo/del/{id}")
    public Response delUser(@PathVariable("id") long id) {
        return ResponseUtil.success(userService.delete(id));
    }

    @GetMapping("/user/userInfo")
    public Response getUserInfo() {
        return ResponseUtil.success(userService.getUserInfoBySession());
    }

    /**
     * 更新头像
     *
     * @param file file
     * @return
     * @throws IOException
     */
    @PostMapping("/user/imgUpload")
    @ResponseBody
    public Response upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseUtil.failure("上传失败，请选择文件");
        }
        String fileName = file.getOriginalFilename();
        String mime = fileName.substring(fileName.lastIndexOf("."));
        if (".png".equals(mime.toLowerCase()) || ".jpg".equals(mime.toLowerCase()) ||
                ".jpeg".equals(mime.toLowerCase()) || ".bmp".equals(mime.toLowerCase())) {
            return (Response) userService.updateUserAavatarImg(file.getInputStream(), mime);
        }
        return ResponseUtil.failure("请上传图片文件");
    }

    @PostMapping("/sendResetPwdEmail")
    public Response sendResetPwdEmail(@RequestParam("email") String email) {
        return ResponseUtil.success(userService.sendResetPwdEmail(email));
    }

    @PostMapping("/sendVerifyEmail")
    public Response sendVerifyEmail(@RequestParam("email") String email) {
        return ResponseUtil.success(userService.sendVerifyEmail(email));
    }


    @PostMapping("/emailVerify")
    public Response emailVerify(@RequestParam("verifyId") String verifyId,
                                @RequestParam("email") String mail) {
        return ResponseUtil.success(userService.verifyEmail(verifyId, mail));
    }

    @PostMapping("/resetPwd")
    public Response resetPwd(@RequestParam("verifyId") String verifyId,
                             @RequestParam("email") String email,
                             @RequestParam("pwd") String pwd) {
        return ResponseUtil.success(userService.reSetPwd(verifyId, email, pwd));
    }


}
