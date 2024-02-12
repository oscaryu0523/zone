//定制請求的實例

//導入axios  npm install axios   for異步請求
import axios from 'axios';

//導入ElMessage  for彈窗
import { ElMessage } from 'element-plus'

import { useLoginInfoShown } from '@/stores/logininfoshown';

//定義一個變量,紀錄公共的前缀  ,  baseURL
// const baseURL = 'http://localhost:8080';
// const baseURL = '/api';
const baseURL = import.meta.env.PROD ? 'http://www.fusionsite.site:8081' : '/api'; // 生产环境URL需根据实际情况调整

const instance = axios.create({baseURL})


import { useTokenStore } from '@/stores/token.js';
//添加請求攔截器
instance.interceptors.request.use(
    (config)=>{
        //請求前的回調
        //添加token
        const tokenStore=useTokenStore();
        //判斷有沒有token
        if(tokenStore.token){
            config.headers.Authorization=tokenStore.token
        }
        return config;
    },
    (err)=>{
        // 請求錯誤的回調
        Promise.reject(err);
    }
)


// 導入路由
import router from '@/router/index.js'




//添加響應攔截器
instance.interceptors.response.use(
    result=>{
        // 判斷業務狀態碼
        if(result.data.code===0){
            return result.data;
        }
        // 操作失敗
        // alert(result.data.message?result.data.message:"服務異常")
        ElMessage.error(result.data.message?result.data.message:"服務異常");
        // 異步操作的狀態碼轉換為失敗
        return Promise.reject(result.data);
    }, 
    err=>{
        // 判斷響應狀態碼，如果為401，則為未登錄，提示請登錄，並跳轉到登錄頁面
        const loginInfoStore = useLoginInfoShown();
        if(err.response.status===401){
            if(!loginInfoStore.isLoginAlertShown){
                ElMessage.error("請先登入")
                loginInfoStore.showLoginAlert();
            }
            router.push('/login')
            loginInfoStore.resetLoginAlert();
        }else{
            ElMessage.error('服務異常')
        }
        return Promise.reject(err);//異步的狀態轉化成失敗的狀態
    }
)

export default instance;