import { Component } from '@angular/core';

@Component({
    selector: 'basic-form',
    moduleId: module.id,
    templateUrl: './basic.html',
})
export class BasicComponent {
    codeArr: any = [];
    toggleCode = (name: string) => {
        if (this.codeArr.includes(name)) {
            this.codeArr = this.codeArr.filter((d: string) => d != name);
        } else {
            this.codeArr.push(name);
        }
    };

    constructor() {}
}
