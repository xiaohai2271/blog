import {Injectable} from '@angular/core';
import {HttpService} from '../http.service';
import {Category} from '../../classes/category';

@Injectable({
    providedIn: 'root'
})
export class CategoryService {

    constructor(public http: HttpService) {
    }

    categories: Category[];

    getAllCategory() {
        const observable = this.http.get('/categories');
        observable.subscribe((data) => {
                if (data.code === 0) {
                    this.categories = data.result;
                }
            }
        );
        return observable;
    }

    update(submitBody: { id: number, name: string }) {
        const observable = this.http.put('/admin/category/update', submitBody, false);
        observable.subscribe(data => {
            if (data.code === 0) {
                // tslint:disable-next-line:prefer-for-of
                for (let i = 0; i < this.categories.length; i++) {
                    if (this.categories[i].id === submitBody.id) {
                        this.categories[i].name = submitBody.name;
                    }
                }
            }
        });
        return observable;
    }

    create(nameStr: string) {
        const observable = this.http.post('/admin/category/create', {name: nameStr}, false);
        observable.subscribe(data => {
            if (data.code === 0) {
                this.categories.push(data.result);
                console.log(this.categories);
            }
        });
        return observable;
    }

    delete(id: number) {
        const observable = this.http.delete(`/admin/category/del?id=${id}`);
        observable.subscribe(data => {
            if (data.code === 0) {
                // tslint:disable-next-line:prefer-for-of
                for (let i = 0; i < this.categories.length; i++) {
                    if (this.categories[i].id === id) {
                        this.categories.splice(i, 1);
                    }
                }
            }
        });
        return observable;
    }

}
