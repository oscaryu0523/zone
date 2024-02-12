import {createRouter, createWebHistory} from 'vue-router'

// 導入登入組件
import LoginVue from '@/views/Login.vue'
// 導入重設密碼組件
import ResetPasswordVue from '@/views/ResetPassword.vue';

// 導入首頁組件
import LayoutVue from '@/views/Layout.vue';
// 導入關於墨語閣組件
import AboutVue from '@/views/About.vue';

// 導入文章相關組件
import ArticleCategoryVue from '@/views/article/ArticleCategory.vue';
import ArticleManageVue from '@/views/article/ArticleManage.vue';
import ArticleListVue from '@/views/article/ArticleList.vue';
import ArticleDetailVue from '@/views/article/ArticleDetail.vue'
// 導入用戶相關組件
import UserAvatarVue from '@/views/user/UserAvatar.vue';
import UserInfoVue from '@/views/user/UserInfo.vue';
import UserResetPasswordVue from '@/views/user/UserResetPassword.vue';


// 定義路由關係
const routes=[
    {path:'/login', component:LoginVue},
    {path:'/reset', component:ResetPasswordVue},
    {path:'/',component:LayoutVue,redirect:'/article/articleList', children:[
        {path:'/article/category',component:ArticleCategoryVue},
        {path:'/article/manage',component:ArticleManageVue},
        {path:'/article/articleList',component:ArticleListVue},
        {path:'/article/:id', component:ArticleDetailVue},
        {path:'/user/avatar',component:UserAvatarVue},
        {path:'/user/info',component:UserInfoVue},
        {path:'/user/resetPassword',component:UserResetPasswordVue},
        {path:'/about', component:AboutVue}
    
    ]}
]

// 創建路由器
const router=createRouter({
    history:createWebHistory(),
    routes:routes
})

// 導出路由
export default router