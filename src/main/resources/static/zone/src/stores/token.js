// 定義store
import { defineStore } from 'pinia'
import { ref } from 'vue'


// 第一個參數:名字(唯一性)
// 第二個參數:函數，函數的內部可以定義狀態的所有內容

// 返回值:函數
// 在pinia中定義的響應式數據都不需要.value
export const useTokenStore = defineStore('token', () => {
    // 定義狀態的內容

    // 1.響應式變量
    const token = ref('')
    const tokenExpiry = ref('');


    // 2.定義一個函數，修改token的值 
    // const setToken = (newToken) => {
    //     token.value = newToken
    //     // 令牌有效期1小時
    //     const expiryTime = new Date().getTime() + 1000 * 60 * 60;
    //     console.log('expiryTime='+expiryTime.toString());
    //     tokenExpiry.value = expiryTime.toString();
    //     // 存在在localStorage中
    //     localStorage.setItem('token_expiry', tokenExpiry.value);
    // }

    const setToken = (newToken, remember = false) => {
        token.value = newToken;
        // 如果用戶選擇了“記住我”，設置較長的過期時間或不設置過期
        const expiryTime = remember ? new Date().getTime() + (1000 * 60 * 60 * 24 * 10) : new Date().getTime() + (1000 * 60 * 60 * 6); // 10天或1小时
        tokenExpiry.value = expiryTime.toString();
        localStorage.setItem('token_expiry', expiryTime.toString());
        console.log(expiryTime.toString());
        localStorage.setItem('token', newToken);
    };

    // 3.函數，移除token的值
    const removeToken = () => {
        token.value = ''
        tokenExpiry.value = '';
        localStorage.removeItem('token_expiry');
    }
    // 4.查察token是否過期
    const isTokenExpired = () => {
        const expiry = tokenExpiry.value || localStorage.getItem('token_expiry');
        return !expiry || Date.now() > parseInt(expiry);
    };


    return {
        token, setToken, removeToken, isTokenExpired
    }
}, {
    persist: true//持久化存儲

});