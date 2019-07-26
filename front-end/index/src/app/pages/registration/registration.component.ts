import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Router} from '@angular/router';
import {NzMessageService} from 'ng-zorro-antd';
import {Title} from '@angular/platform-browser';
import {UserService} from '../../services/user/user.service';
import {environment} from '../../../environments/environment';
import {LoginReq} from '../../class/loginReq';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {


  imgCodeUrl: string = environment.host + '/imgCode';

  // 遮罩
  show = false;
  // 输入框的验证码
  imgCode: string = '';
  email: string = null;
  password: string = null;
  rePassword: string = null;
  // 验证码验证状态
  public imgCodeStatus: boolean;

  constructor(public userService: UserService,
              private routerinfo: ActivatedRoute,
              private router: Router,
              private message: NzMessageService,
              private titleService: Title) {
    titleService.setTitle('小海博客|注册');
  }


  orginalUrl: string = null;

  ngOnInit() {
    window.scrollTo(0, 0);
    this.orginalUrl = this.routerinfo.snapshot.queryParams.url;
  }

  changeImg() {
    this.imgCodeUrl = environment.host + '/imgCode?time=' + (new Date()).getTime();
  }


  /**
   * 验证码验证
   */
  handleKeyUp() {
    if (this.imgCode.length === 4) {
      this.userService.imgCodeVerify(this.imgCode).subscribe(data => {
        if (data.code === 0) {
          this.imgCodeStatus = true;
        } else {
          this.imgCodeStatus = false;
          this.message.warning('验证码验证失败');
        }
      });
    }
  }

  /**
   * 注册的数据提交
   */
  doRegistration() {
    if (this.imgCodeStatus) {
      if (this.email == null || this.password == null || this.rePassword == null) {
        this.message.warning('用户名和密码均不能为空');
        return;
      }
      if (this.rePassword !== this.password) {
        this.message.warning('两次密码不匹配');
        return;
      }
      this.show = true;

      this.userService.registration(this.email, this.password).subscribe(data => {
        this.show = false;
        this.userService.tempUser = new LoginReq();
        if (data.code === 0) {
          this.userService.tempUser.email = this.email;
          this.userService.tempUser.password = this.password;
          // 注册成功
          this.message.success('注册成功，3秒钟后转跳登录页面.');
          setTimeout(() => {
            this.router.navigateByUrl('/login');
          }, 3000);
        } else {
          this.message.error('注册失败，原因：' + data.msg);
        }
      });
    } else {
      document.getElementById('verifyImgCode').focus();

    }
  }
}
