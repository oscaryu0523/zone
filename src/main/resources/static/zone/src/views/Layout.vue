<script setup>
import {
    List,
    Management,
    Promotion,
    UserFilled,
    User,
    Crop,
    EditPen,
    SwitchButton,
    CaretBottom
} from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'


// 獲取用戶詳細信息
import { userInfoService } from '@/api/user.js'
import useUserInfoStore from '@/stores/userInfo.js'
import { useLoginInfoShown } from '@/stores/logininfoshown';
import { useTokenStore } from '@/stores/token.js'

const tokenStore = useTokenStore();
const userInfoStore = useUserInfoStore();
const loginInfoStore = useLoginInfoShown();

const getUserInfo = async () => {

    // 如果有登入才要取得資料
    if(!tokenStore.isTokenExpired() && tokenStore.token){
        let result = await userInfoService();
        // 數據存儲到pinia中
        userInfoStore.setInfo(result.data);
    }else{
        console.log("Token is expired or not present.");
        return;
    }
}
// 載入時自動獲取用戶詳細信息


getUserInfo();

import { ElMessage,ElMessageBox } from 'element-plus';
import { useRouter } from 'vue-router'

const router = useRouter()



// 選項被點擊後調用的函數
const handleCommand = (command) => {

    // 判斷指令
    if (command === 'login') {
        router.push("/login");
        return;
    }
    if (command === 'logout') {
        // 刪除文章

        ElMessageBox.confirm(
            '您確定要登出',
            '溫馨提示',
            {
                confirmButtonText: 'OK',
                cancelButtonText: 'Cancel',
                type: 'warning',
            }
        )
            .then(() => {
                // 清除token及個人信息
                tokenStore.removeToken();
                userInfoStore.removeInfo();
                // 跳轉跳轉至登入頁面
                router.push("/login")
                ElMessage({
                    type: 'success',
                    message: '已登出',
                })

            })
            .catch(() => {
                ElMessage({
                    type: 'info',
                    message: '取消登出',
                })
            })

    } else {
        // 根據子路由的切換進行內容展示
        router.push("/user/" + command)
    }

}
const front=()=>{
    router.push('/');
}

</script>

<template>
    <!-- element-plus中的容器 -->
    <el-container class="layout-container">
        <!-- 左側菜單 -->
        <el-aside width="200px">
            <div class="el-aside__logo" @click="front"></div>
            <!-- element-plus的選單標籤 -->
            <el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff" router>
                <el-menu-item index="/about">
                    <el-icon>
                        <List />
                    </el-icon>
                    <span>關於墨語閣</span>
                </el-menu-item>
                <el-menu-item index="/article/articleList">
                    <el-icon>
                        <List />
                    </el-icon>
                    <span>文章列表</span>
                </el-menu-item>
                <el-sub-menu v-if="tokenStore.token">
                    <template #title>
                        <el-icon>
                            <UserFilled />
                        </el-icon>
                        <span>個人中心</span>
                    </template>
                    <el-menu-item index="/user/info">
                        <el-icon>
                            <User />
                        </el-icon>
                        <span>基本資料</span>
                    </el-menu-item>
                    <el-menu-item index="/user/avatar">
                        <el-icon>
                            <Crop />
                        </el-icon>
                        <span>更換頭像</span>
                    </el-menu-item>
                    <el-menu-item index="/user/resetPassword">
                        <el-icon>
                            <EditPen />
                        </el-icon>
                        <span>重置密碼</span>
                    </el-menu-item>
                </el-sub-menu>
                <el-menu-item index="/article/category">
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>文章分類</span>
                </el-menu-item>
                <el-menu-item index="/article/manage">
                    <el-icon>
                        <Promotion />
                    </el-icon>
                    <span>文章管理</span>
                </el-menu-item>
                
            </el-menu>
        </el-aside>
        <!-- 右側主區域 -->
        <el-container>
            <!-- 頭部區域 -->
            <el-header>
                <div>用戶：
                    <strong v-if="tokenStore.token">{{ userInfoStore.info.nickname }}</strong>
                    <strong v-else>遊客</strong>
                </div>
                <!-- 下拉菜單 -->
                <!-- command，選項，選項被點擊後，在事件函數上聲明一個參數，接收選項對應的指令 -->
                <el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="el-dropdown__box">
                        <el-avatar :src="userInfoStore.info.userPic ? userInfoStore.info.userPic : avatar" />
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu v-if="tokenStore.token">
                            <el-dropdown-item command="info" :icon="User">基本資料</el-dropdown-item>
                            <el-dropdown-item command="avatar" :icon="Crop">更換頭像</el-dropdown-item>
                            <el-dropdown-item command="resetPassword" :icon="EditPen">重置密碼</el-dropdown-item>
                            <el-dropdown-item command="logout" :icon="SwitchButton">登出</el-dropdown-item>
                        </el-dropdown-menu>

                        <el-dropdown-menu v-else>
                            <el-dropdown-item command="login" :icon="SwitchButton">登入</el-dropdown-item>
                        </el-dropdown-menu>

                    </template>
                </el-dropdown>
            </el-header>
            <!-- 中間區域 -->
            <el-main>
                <!-- <div style="width: 1290px; height: 570px;border: 1px solid red;">
                    內容展示區
                </div> -->
                <!-- 根據子路由的切換進行內容展示 -->
                <router-view></router-view>
            </el-main>
            <!-- 底部區域 -->
            <el-footer>墨語閣 ©2024 由 余銘修 創建</el-footer>
        </el-container>
    </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
    height: 100vh;

    .el-aside {
        background-color: #232323;

        &__logo {
            height: 120px;
            background: url('@/assets/logo4.png') no-repeat center / 120px auto;
            &:hover{
                cursor: pointer;
                transform: scale(1.05);
            }
        }

        .el-menu {
            border-right: none;
        }
    }

    .el-header {
        background-color: #fff;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-dropdown__box {
            display: flex;
            align-items: center;
            &:hover{
                cursor: pointer;
                transform: scale(1.05);
            }

            .el-icon {
                color: #999;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
    }
}
</style>
