<script setup>
import { Plus, Upload } from '@element-plus/icons-vue'
import { ref } from 'vue'
import avatar from '@/assets/default.png'
const uploadRef = ref()
import { useTokenStore } from '@/stores/token.js';

const tokenStore=useTokenStore();



import useUserInfoStore from '@/stores/userInfo.js'
const userInfoStore=useUserInfoStore();
//用戶頭像地址
const imgUrl = ref(userInfoStore.info.userPic)

// 圖片成功上船的回掉函數
const uploadSuccess=(result)=>{
    imgUrl.value=result.data;
}

import {userUpdateAvatarService} from '@/api/user.js'
import { ElMessage } from 'element-plus';

// 調用接口更新avator
const updateAvatar=async()=>{
    let result=await userUpdateAvatarService(imgUrl.value);

    ElMessage.success(result.message?result.message:'修改成功')

    userInfoStore.info.userPic=imgUrl;
}



</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>更換頭像</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <!-- 
                        auto-upload:設置是否自動上傳
                        action:設置服務器接口路徑
                        name:設置上傳的文件字段名
                        header:設置上傳的請求頭
                        on-sucseess:設置上傳成功的回調函數

                     -->
                <el-upload 
                    ref="uploadRef"
                    class="avatar-uploader" 
                    :show-file-list="false"
                    :auto-upload="true"
                    action="http://www.fusionsite.site:8081/upload"
                    name="file"
                    :headers="{'Authorization':tokenStore.token}"
                    :on-success="uploadSuccess"
                    >
                    <img v-if="imgUrl" :src="imgUrl" class="avatar" />
                    <img v-else :src="avatar" max-width="278" />
                </el-upload>
                <br />
                <el-button type="primary" :icon="Plus" size="large" @click="uploadRef.$el.querySelector('input').click()">
                    選擇圖片
                </el-button>
                <el-button type="success" :icon="Upload" size="large" @click="updateAvatar">
                    更新頭像
                </el-button>
            </el-col>
        </el-row>
    </el-card>
</template>

<style lang="scss" scoped>
.avatar-uploader {
    :deep() {
        .avatar {
            max-width: 278px;
            max-height: 278px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 278px;
            height: 278px;
            text-align: center;
        }
    }
}
</style>

<style>
.page-container {
  min-height: 100%;
}
</style>
