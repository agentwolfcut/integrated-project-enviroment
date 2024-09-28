import './assets/main.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import ToastPlugin from 'vue-toast-notification';
import 'vue-toast-notification/dist/theme-bootstrap.css';

// import moment from 'moment-timezone';
// moment.tz.setDefault('Asia/Jakarta');

const app = createApp(App)
const pinia = createPinia()

app.use(ToastPlugin);
app.use(pinia)
app.use(router)
app.mount('#app')