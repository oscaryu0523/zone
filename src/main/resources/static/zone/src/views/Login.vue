<script setup>
import { User, Lock, Message } from '@element-plus/icons-vue'
import { ref } from 'vue'

//導入ElMessage  for彈窗
import { ElMessage,ElMessageBox } from 'element-plus'


// 控制註冊與登入表單的顯示，默認顯示註冊
const isRegister = ref(false)


// 定義數據模型
const registerData = ref({
    nickname: '',
    email: '',
    username: "",
    password: "",
    rePassword: "",
    remember:false
})

// 自定義的密碼校驗函數//rule 被校驗的對象//value 被校驗對象的值 //callback 不帶任何參數代表驗證通過
const checkRePassword = (rule, value, callback) => {
    // 首先檢查輸入值是否為空
    if (value === '') {
        // 如果為空，通過回調函數返回一個錯誤信息
        callback(new Error("請再次確認密碼"));
    } else if (value !== registerData.value.password) {
        // 如果輸入的密碼與原始密碼不匹配，返回一個不匹配的錯誤信息
        callback(new Error("請確保兩次輸入的密碼一樣"));
    } else {
        // 如果沒有問題，則調用回調函數並不帶任何參數表示通過驗證
        callback();
    }
}

//自訂註冊校驗規則
const rules = {
    nickname: [
        { required: true, message: '請輸入用戶暱稱', trigger: 'blur' },
        {
            pattern: /^\S{2,10}$/, message: '暱稱必須是2-10位的非空字符串', trigger: 'blur'
        }
    ],
    email: [
        { required: true, message: '請輸入用戶郵箱', trigger: 'blur' },
        { type: 'email', message: '郵箱格式不正確', trigger: 'blur' }
    ],
    username: [
        { required: true, message: '請輸入用戶名', trigger: 'blur' },//必須輸入
        { min: 5, max: 16, message: '長度為5-16位非空字符', trigger: 'blur' },//長度最小為5，最大為16
    ],
    password: [
        { required: true, message: '請輸入密碼', trigger: 'blur' },//必須輸入
        { min: 5, max: 16, message: '長度為5-16位非空字符', trigger: 'blur' },//長度最小為5，最大為16
    ],
    rePassword: [
        { validator: checkRePassword, trigger: 'blur' }
    ]
}

// 引入用戶函式
import { userRegisterService, userLoginService, checkMailService } from '@/api/user.js'
// 調用後台接口完成註冊
// 註冊函式
const register = async () => {
    let result = await userRegisterService(registerData.value)
    ElMessage.success(result.msg ? result.msg : "註冊成功");

    isRegister = false;
}

// 綁定數據(重用註冊表單的數據)
//表單數據校驗

//調用後台接口完成登錄
// 登錄函式 
import { useTokenStore } from '@/stores/token.js'
import { useRouter } from 'vue-router'
import { useLoginInfoShown } from '@/stores/logininfoshown';
const router = useRouter();
const tokenStore = useTokenStore();
const loginInfoStore = useLoginInfoShown();

// const login = async () => {
//     let result = await userLoginService(registerData.value)
//         ElMessage.success(result.msg ? result.msg : "登入成功");
//         // 存入token
//         tokenStore.setToken(result.data);
//         // 重置請先登入的提示狀態
//         loginInfoStore.resetLoginAlert();
//         // 跳轉道首頁，借助路由完成跳轉
//         router.push('/article/manage')
// }

const login = async () => {
    let result = await userLoginService(registerData.value)
    if (result && result.data) {
        ElMessage.success(result.msg ? result.msg : "登入成功");
        // 存入token
        tokenStore.setToken(result.data,registerData.value.remember);
        // 重置請先登入的提示狀態
        loginInfoStore.resetLoginAlert();
        // 跳轉道首頁，借助路由完成跳轉
        router.push('/article/manage')
    }else{
        ElMessage.error("登入失敗");
    }
}

// 定義函數用來清空數據模型的數據
const clearRegisterData = () => {
    registerData.value = {
        nickname: '',
        email: '',
        username: '',
        password: '',
        rePassword: ''
    }
    console.log("test")
}

// 點擊logo進入首頁
const front = () => {
    router.push('/');
}


// 忘記密碼彈窗
const open = () => {
  ElMessageBox.prompt('請輸入您的註冊信箱，我們將寄信給您', '忘記密碼', {
    confirmButtonText: '確定',
    cancelButtonText: '取消',
    inputPattern:
      /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
    inputErrorMessage: 'Invalid Email',
  })
    .then(async({ value }) => {
        let result=await checkMailService(value);
        // 驗證信箱是否存在
        // 如果存在就發信 並說 已發信通知
        // 如果不存在 就說 該信箱尚未註冊
      ElMessage({
        type: 'success',
        message: `已寄信到信箱`,
      })
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: result.data.message?result.data.message:'已取消',
      })
    })
}
</script>

<template>
    <el-row class="login-page">
        <el-col :span="12" class="bg" @click="front"></el-col>
        <el-col :span="6" :offset="3" class="form">
            <!-- 註冊表單 -->
            <el-form ref="form" size="large" autocomplete="off" v-if="isRegister" :model="registerData" :rules="rules">
                <el-form-item>
                </el-form-item>
                <h1>註冊</h1>
                <br>
                <!-- 暱稱 -->
                <el-form-item prop="nickname">
                    <el-input :prefix-icon="User" placeholder="請輸入暱稱" v-model="registerData.nickname"></el-input>
                </el-form-item>
                <el-form-item prop="email">
                    <el-input :prefix-icon="Message" placeholder="請輸入email" v-model="registerData.email"></el-input>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="請輸入用戶名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" type="password" placeholder="請輸入密碼"
                        v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item prop="rePassword">
                    <el-input :prefix-icon="Lock" type="password" placeholder="請再次輸入密碼"
                        v-model="registerData.rePassword"></el-input>
                </el-form-item>

                <!-- 註冊按鈕 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="register">
                        註冊
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = false; clearRegisterData()">
                        ← 返回
                    </el-link>
                </el-form-item>
            </el-form>
            <!-- 登入表單 -->
            <el-form ref="form" size="large" class="login" autocomplete="off" v-else :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>登入</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="請輸入用戶名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input name="password" :prefix-icon="Lock" type="password" placeholder="請輸入密碼"
                        v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item class="flex">
                    <div class="flex">
                        <el-checkbox v-model="registerData.remember">十天內免登入</el-checkbox>
                        <el-link type="primary" :underline="false" class="forget" @click="open">忘記密碼？</el-link>
                    </div>
                </el-form-item>
                <!-- 登入按鈕 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="login">登入</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = true; clearRegisterData()">
                        註冊 →
                    </el-link>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<style lang="scss" scoped>
/* 樣式 */
.login-page {
    height: 100vh;
    background-color: #fff;

    .bg {
        background:
        url('@/assets/logo4.png') no-repeat 50% center / 360px auto,
        url('@/assets/login_bg.jpg') no-repeat center / cover;
        border-radius: 0 20px 20px 0;


        &:hover {
            border: 2px solid #3498db;
            /* 添加邊框 */
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
            cursor: pointer;
            opacity: 0.95;
        }
    }


    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        user-select: none;

        .title {
            margin: 0 auto;
        }

        .button {
            width: 100%;
        }

        .flex {
            width: 100%;
            display: flex;
            justify-content: space-between;
        }
        .forget{
            text-align: right;
            margin-left: auto;
        }
    }
    

        
}
</style>
