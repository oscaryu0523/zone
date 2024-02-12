import './assets/main.scss'

import { createApp } from 'vue'//導入vue
import ElementPlus from 'element-plus'//導入element-plus
import 'element-plus/dist/index.css'//導入element-plus的樣式
import router from '@/router'//導入路由
import {createPinia} from 'pinia'//導入狀態管理庫
import { createPersistedState } from 'pinia-persistedstate-plugin'//導入持久化插件 
import locale from 'element-plus/dist/locale/zh-tw.js'//導入繁體中文組件


import App from './App.vue'//導入App.vue

const app=createApp(App);//創建一個應用實例

const pinia= createPinia();

const persist=createPersistedState();

pinia.use(persist)//pinia使用持久化

app.use(pinia)//使用狀態管理庫

app.use(router);//使用路由

app.use(ElementPlus, {locale});//使用elementplus

app.mount('#app')//控制html元素
