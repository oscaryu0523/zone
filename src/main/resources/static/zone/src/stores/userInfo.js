import {defineStore} from 'pinia'
import {ref} from 'vue'

// 定義全域參數

const useUserInfoStore=defineStore('userInfo',()=>{
// 定義狀態相關內容
    const info =ref({})

    const setInfo = (newInfo)=>{
        info.value = newInfo
    }

    const removeInfo=()=>{
        info.value=''
    }
    return{info, setInfo, removeInfo}
},{
    persist:true
})
export default useUserInfoStore;