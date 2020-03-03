import { Component } from '@angular/core';

//Importing the native capacitor plugins
import { Plugins } from '@capacitor/core';
const { HmsMapPlugin } = Plugins;

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {

  constructor() {}

  triggerMap() {
      HmsMapPlugin.jump2MapActivity();
    }

}
