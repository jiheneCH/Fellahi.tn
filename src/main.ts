import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';

import { registerLicense } from '@syncfusion/ej2-base';

// Enregistre d'abord la licence ici AVANT le bootstrap
registerLicense('Ngo9BigBOggjHTQxAR8/V1NNaF5cXmBCe0x3QXxbf1x1ZFFMY11bQX9PIiBoS35Rc0VnWHtfdnBcRmVbWUZ3VEBU');


platformBrowserDynamic()
    .bootstrapModule(AppModule)
    .catch((err) => console.error(err));


 

