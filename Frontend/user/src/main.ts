import './styles/main.scss';
// import ElPagination from 'element-plus';
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { createMetaManager } from 'vue-meta';

import App from './App.vue';
import router from './router';

const app = createApp(App).use(createMetaManager()).use(createPinia()).use(router);

await router.isReady();
app.mount('#app');
