<script setup>
import { ref } from 'vue'
import useUserInfoStore from '@/stores/userInfo.js'
const userInfoStroe=useUserInfoStore();
const userInfo = ref({...userInfoStroe.info})

import { ElMessage } from 'element-plus';


// 輸入校驗規則
const rules = {
    nickname: [
        { required: true, message: '請輸入用戶暱稱', trigger: 'blur' },
        {
            pattern: /^\S{2,10}$/,
            message: '暱稱必須是2-10位的非空字符串',
            trigger: 'blur'
        }
    ],
    email: [
        { required: true, message: '請輸入用戶郵箱', trigger: 'blur' },
        { type: 'email', message: '郵箱格式不正確', trigger: 'blur' }
    ]
}


import {userInfoUpdateService} from '@/api/user.js'

// 更新個人信息調用接口
const updateUserInfo=async()=>{
    // 調用接口
    let result=await userInfoUpdateService(userInfo.value);
    ElMessage.success(result.message?result.message:'修改成功')

    // 修改pinia資訊
    userInfoStroe.setInfo(userInfo.value)
}



</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>基本資料</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="userInfo" :rules="rules" label-width="100px" size="large" style="height:100%;">
                    <el-form-item label="登錄名稱">
                        <el-input v-model="userInfo.username" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="用戶暱稱" prop="nickname">
                        <el-input v-model="userInfo.nickname"></el-input>
                    </el-form-item>
                    <el-form-item label="用戶郵箱" prop="email">
                        <el-input v-model="userInfo.email"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updateUserInfo">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>


<style>
.page-container {
  min-height: 100%;
}
</style>
