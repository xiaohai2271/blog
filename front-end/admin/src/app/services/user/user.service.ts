import {Injectable} from '@angular/core';
import {HttpService} from '../http.service';
import {User} from '../../classes/user';
import {of} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    userInfo: User;

    avatarHost: string = 'http://cdn.celess.cn';

    constructor(public http: HttpService) {
        this.getUserInfo();
    }

    /**
     * 获取用户信息
     */
    getUserInfo() {
        const observable = this.http.get('/user/userInfo');
        observable.subscribe((data: any) => {
            if (data.code === 0) {
                this.userInfo = data.result;
            }
        });
        return observable;
    }

    /**
     * 注销登录
     */
    logout() {
        this.http.get('/logout').subscribe((data: any) => {
            if (data.code === 0) {
                this.userInfo = null;
            }
        });
    }


    updateInfo(submitBody: { desc: string, displayName: string }) {
        const observable = this.http.put('/user/userInfo/update', submitBody, false);
        observable.subscribe(data => {
            if (data.code === 0) {
                this.userInfo.desc = submitBody.desc;
                this.userInfo.displayName = submitBody.displayName;
            }
        });
        return observable;
    }


    sendEmail() {
        return this.http.post('/sendVerifyEmail', {email: this.userInfo.email}, false);
    }
}
