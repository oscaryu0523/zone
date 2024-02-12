<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'

import { ref } from 'vue'

//文章分類數據模型
const categorys = ref([
    {
        "id": 3,
        "categoryName": "美食",
        "categoryAlias": "my",
        "createTime": "2023-09-02 12:06:59",
        "updateTime": "2023-09-02 12:06:59"
    }
])

//用戶搜索時選中的分類id
const categoryId = ref('')

//用戶搜索時選中的發布狀態
const state = ref('')

//文章列表數據模型
const articles = ref([
    {
        "id": '',//文章編號
        "title": "",//標題
        "content": "",//文章內容
        "coverImg": "",//圖片
        "state": "",//發表狀態
        "categoryId": '',//文章編號id
        "createTime": "",//創建時間
        "updateTime": ""//更新時間
    }
])

//分頁條數據模型
const pageNum = ref(1)//當前頁
const total = ref(20)//總條數
const pageSize = ref(5)//每頁條數

//當每頁條數發生了變化，調用此函數
const onSizeChange = (size) => {
    pageSize.value = size
    articleList()
}
//當前頁碼發生變化，調用此函數
const onCurrentChange = (num) => {
    pageNum.value = num
    articleList()
}

import { articleCategoryListService, articleListService, articleAddService, articleUpdateService, articleDeleteService, articleCategoryListAllService } from '@/api/article.js'


// 調用後端接口取得搜索列  文章分類列表  
const articleCategoryList = async () => {
    let result = await articleCategoryListAllService();
    categorys.value = result.data;
}




// 調用後端接口進行搜索列  取得篩選後的文章分類列表 

const articleList = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value ? categoryId.value : null,
        state: state.value ? state.value : null
    }
    let result = await articleListService(params);

    //數據渲染到頁面上
    total.value = result.data.total;
    articles.value = result.data.items;

    //處理數據，給數據模型擴展一個屬性categoryName,分類名稱
    for (let i = 0; i < articles.value.length; i++) {
        let article = articles.value[i];
        for (let j = 0; j < categorys.value.length; j++) {
            if (article.categoryId === categorys.value[j].id) {
                article.categoryName = categorys.value[j].categoryName;
            }
        }
    }
}





//載入頁面調用後端接口
articleCategoryList();

articleList();

import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { Plus } from '@element-plus/icons-vue'
// 控制抽屜是否顯示
const visibleDrawer = ref(false)
// 添加表單數據模型
const articleModel = ref({
    title: '',
    categoryId: '',
    coverImg: '',
    content: '',
    state: ''
})


// 導入token
import {useTokenStore} from '@/stores/token.js'
const tokenStore=useTokenStore();

// 上傳成功的回調函數
const uploadSuccess=(result)=>{
    articleModel.value.coverImg=result.data;
    console.log(result.data);
}

// 清空抽屜函數
const resetArticleModel=()=>{
    articleModel.value={
            title:'',
            categoryId:'',
            coverImg:'',
            content:'<p></p>',// 使用空的 HTML 標籤來清空內容
            state:''
        }
} 

// 調用接口文章添加
import {ElMessage,ElMessageBox} from 'element-plus'
const articleAdd=async(clickState)=>{
    // 修改狀態
    articleModel.value.state=clickState
    // 調用接口
    let result = await articleAddService(articleModel.value);
    ElMessage.success(result.message?result.message:'添加成功')
    // 關閉抽屜
    visibleDrawer.value=false;
    // 刷新當前列表
    articleList();
    // 清空抽屜內容
    resetArticleModel();
}

const title=ref('');
// 編輯文章
const articleEdit=(row)=>{
    // 打開抽屜
    visibleDrawer.value=true;
    // 修改title名
    title.value='編輯文章'
    articleModel.value={
            title:row.title,
            categoryId:row.categoryId,
            coverImg:row.coverImg,
            content:row.content,
            state:row.state,
            id:row.id
        }
}

// 更新文章
const articleUpdate=async(clickState)=>{
    // 修改發布狀態
    console.log("clickState="+clickState);
    articleModel.value.state=clickState;
    // 調用街口
    let result=await articleUpdateService(articleModel.value);
    ElMessage.success(result.message?result.message:'編輯成功');
     // 關閉抽屜
     visibleDrawer.value=false;
    // 刷新列表
    articleList();
    // 重置表單
    resetArticleModel();

}

// 刪除文章

const articleDelete=(row)=>{
  ElMessageBox.confirm(
    '您確定要刪除該文章嗎',
    '溫馨提示',
    {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  )
    .then(async() => {
    //   調用刪除接口
    let result=await articleDeleteService(row.id);
    ElMessage({
        type: 'success',
        message: '刪除成功',
      })
    //   刷新列表
    articleList();
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '刪除失敗',
      })
    })

}

</script>


<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章管理</span>
                <div class="extra">
                    <el-button type="primary" @click="visibleDrawer = true; title='添加文章'">添加文章</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表單 -->
        <el-form inline>
            <el-form-item label="文章分類：">
                <el-select placeholder="請選擇" class="el-select" v-model="categoryId">
                    <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="發布狀態：">
                <el-select placeholder="請選擇" class="el-select" v-model="state">
                    <el-option label="已發布" value="已發布"></el-option>
                    <el-option label="草稿" value="草稿"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="articleList">搜索</el-button>
                <el-button @click="categoryId = ''; state = ''">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <el-table :data="articles" style="width: 100%">
            <el-table-column label="文章標題" width="400" prop="title"></el-table-column>
            <el-table-column label="分類" prop="categoryName"></el-table-column>
            <el-table-column label="發布時間" prop="createTime"> </el-table-column>
            <el-table-column label="狀態" prop="state"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="articleEdit(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="articleDelete(row)">
                    
                    </el-button>
                </template>
            </el-table-column>
            <!-- <template #empty>
                <el-empty description="沒有數據" />
            </template> -->
        </el-table>
        <!-- 分頁條 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[1, 5, 10, 15]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />

        <!-- 抽屜 -->
        <el-drawer v-model="visibleDrawer" :title=title direction="rtl" size="50%">
            <!-- 添加文章表單 -->
            <el-form :model="articleModel" label-width="100px">
                <el-form-item label="文章標題">
                    <el-input v-model="articleModel.title" placeholder="請輸入標題"></el-input>
                </el-form-item>
                <el-form-item label="文章分類">
                    <el-select placeholder="請選擇" v-model="articleModel.categoryId">
                        <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="文章封面">

                    <!-- 
                        auto-upload:設置是否自動上傳
                        action:設置服務器接口路徑
                        name:設置上傳的文件字段名
                        header:設置上傳的請求頭
                        on-sucseess:設置上傳成功的回調函數

                     -->
                    
                    <el-upload class="avatar-uploader" :auto-upload="true" :show-file-list="false"
                    action="http://www.fusionsite.site:8081/upload"
                    name="file"
                    :headers="{'Authorization':tokenStore.token}"
                    :on-success="uploadSuccess"
                    >
                        <img v-if="articleModel.coverImg" :src="articleModel.coverImg" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon">
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item label="文章內容">
                    <div class="editor">
                        <quill-editor theme="snow" v-model:content="articleModel.content" contentType="html">
                        </quill-editor>
                    </div>
                </el-form-item> 
                <el-form-item>
                    <el-button type="primary" @click="title==='添加文章'?articleAdd('已發布'):articleUpdate('已發布')">發布</el-button>
                    <el-button type="info" @click="title==='添加文章'?articleAdd('草稿'):articleUpdate('草稿')">草稿</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>


    </el-card>
</template>


<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .el-select {
        --el-select-width: 220px;
    }
}

/* 抽屜樣式 */
.avatar-uploader {
    :deep() {
        .avatar {
            width: 300px;
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
            width: 178px;
            height: 178px;
            text-align: center;
        }
    }
}

.editor {
    width: 100%;

    :deep(.ql-editor) {
        min-height: 200px;
    }
}
</style>
