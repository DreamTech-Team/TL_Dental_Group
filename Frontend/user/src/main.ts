import './styles/main.scss';
// import ElPagination from 'element-plus';
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import VueGtag from 'vue-gtag';
import { createMetaManager } from 'vue-meta';

import App from './App.vue';
import router from './router';

const app = createApp(App)
  .use(createMetaManager())
  .use(createPinia())
  .use(router)
  .use(
    VueGtag,
    {
      appName: 'TLDentalCustomer',
      pageTrackerScreenviewEnabled: true,
      config: { id: import.meta.env.VITE_MEASUREMENT_ID }
    },
    router
  );

app.mount('#app');

// https://blog.stackademic.com/how-to-use-google-analytics-in-your-vue-3-options-api-with-vue-gtag-f7e217a8fa56
