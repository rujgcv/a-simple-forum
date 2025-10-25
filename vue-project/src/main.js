import './assets/css/main.css';
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import axios from '@/utils/request';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

const app = createApp(App);

app.config.globalProperties.axios = axios;

app.use(ElementPlus, {
  locale: zhCn,
})

app.use(router);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.mount('#app');
