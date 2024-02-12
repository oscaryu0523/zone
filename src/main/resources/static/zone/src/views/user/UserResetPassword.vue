<script setup>
import { ref } from 'vue'
import {ElMessage} from 'element-plus'

const pwdModel = ref({
  password: '',
  newPassword: '',
  rePassword: ''
})


// 密碼校驗規則
const rules = {
  password: [
    { required: true, message: '請輸入密碼', trigger: 'blur' },
    { min: 5, max: 16, message: '密碼長度必須是6-15位非空字符串', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '請輸入新密碼', trigger: 'blur' },
    { min: 5, max: 16, message: '密碼長度必須是6-15位非空字符串', trigger: 'blur' }
  ],
  rePassword: [
    { required: true, message: '請再次確認新密碼', trigger: 'blur' },
    { min: 5, max: 16, message: '密碼長度必須是6-15位非空字符串', trigger: 'blur' }
  ]
}


import {userUpdatePasswordService} from '@/api/user.js'
import router from '@/router/index.js'
// 調用接口更新密碼
const updatePassword=async()=>{
  let result = await userUpdatePasswordService(pwdModel.value);
  ElMessage.success(result.message?result.message:"更新成功")

  router.push('/login');
}

// 重置
const reset=()=>{
  pwdModel.value={
    password:'',
    newPassword: '',
    rePassword: ''
  }
}

</script>


<template>
  <el-card class="page-container">
    <!-- 表頭 -->
    <template #header>
      <div class="header">
        <span>重置密碼</span>
      </div>
    </template>
    <!-- 輸入欄位 -->
    <el-row>
      <el-col :span="12">
        <el-form size="large" label-width="100px" :rules="rules" :model="pwdModel">
          <el-form-item label="原密碼" prop="password">
            <el-input v-model="pwdModel.password" type="password"></el-input>
          </el-form-item>
          <el-form-item label="新密碼" prop="newPassword">
            <el-input v-model="pwdModel.newPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item label="確認新密碼" prop="rePassword">
            <el-input v-model="pwdModel.rePassword" type="password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="updatePassword">修改密碼</el-button>
            <el-button @click="reset">重置</el-button>
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
  


  