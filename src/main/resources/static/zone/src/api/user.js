//導入request.js請求工具
import request from '@/utils/request.js'

// 提供調用"註冊接口"的函數
export const userRegisterService = (registerData)=>{
    // 借助於UrlSearchParams完成傳遞
    const params=new URLSearchParams()

    for(let key in registerData){
        params.append(key, registerData[key])
    }

    return request.post('/user/register',params)
}

// 提供調用"登錄接口"的函數
export const userLoginService = (loginData)=>{
    // 借助於UrlSearchParams完成傳遞
    const params=new URLSearchParams()

    for(let key in loginData){
        params.append(key, loginData[key])
    }

    return request.post('/user/login',params)
}

// 提供調用"獲取用戶詳細信息"接口
export const userInfoService=()=>{
    return request.get('/user/userInfo')
}

// 提供'更新個人信息的'接口
export const userInfoUpdateService=(userInfoData)=>{
    return request.put('/user/update',userInfoData)
}

// 提供'更新頭像'的接口
export const userUpdateAvatarService=(avatarUrl)=>{
    return request.patch('/user/updateAvatar?avatarUrl='+avatarUrl)
}

// 提供'更新密碼'的接口
export const userUpdatePasswordService=(pwdData)=>{
    return request.patch('/user/updatePwd',{
        old_pwd:pwdData.password,
        new_pwd:pwdData.newPassword,
        re_pwd:pwdData.rePassword
    })
}

// 提供'忘記密碼的檢查mail' 的接口
export const checkMailService=(email)=>{
    return request.get('/user/checkEmail?email='+email)
}

// 提供'使用token取得email'的接口
export const getEmailService=(token)=>{
    return request.get('/user/getEmail?token='+token)
}

// 提供'未登錄 重置密碼'的接口
export const resetPasswordService=(pwdData)=>{
    console.log("token="+pwdData.token);
    console.log("new_pwd="+pwdData.newPassword)
    return request.patch('/user/resetPwd',{
        token:pwdData.token,
        email:pwdData.email,
        new_pwd:pwdData.newPassword,
        re_pwd:pwdData.rePassword
    })
}
