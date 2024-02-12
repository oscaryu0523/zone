<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'
const categorys = ref([
    {
        "id": '',
        "categoryName": "",
        "categoryAlias": "",
        "createTime": "",
        "updateTime": ""
    }
])

//聲明一個異步的函數 
import { articleCategoryListService,articleCategoryAddService, articleCategoryUpdateService, articleCategoryDeleteService} from '@/api/article.js'

//調用後台接口取得文章分類列表
const articleCategoryList = async () => {
    let result = await articleCategoryListService();
    categorys.value = result.data;
}

articleCategoryList();

// 控制添加分類彈窗
const dialogVisible = ref(false)

// 添加分類數據模型
const categoryModel = ref({
    categoryName: '',
    categoryAlias: ''
})
// 添加分類表單校驗
const rules = {
    categoryName: [
        { required: true, message: '請輸入分類名稱', trigger: 'blur' },
    ],
    categoryAlias: [
        { required: true, message: '請輸入分類別名', trigger: 'blur' },
    ]
}


import {ElMessage} from 'element-plus'
// 調用接口添加分類表單
const addCategory= async()=>{
    let result=await articleCategoryAddService(categoryModel.value);
    ElMessage.success(result.message ? result.message: "添加成功");
    // 刷新列表
    articleCategoryList();
    // 關閉彈窗
     dialogVisible.value = false
}

// 定義變量，控制標題的展示
const title=ref('')

// 展示編輯彈窗
const showDialog=(row)=>{
    dialogVisible.value=true;
    title.value='編輯分類';
    // 數據拷貝
    categoryModel.value.categoryName = row.categoryName;
    categoryModel.value.categoryAlias= row.categoryAlias;
    categoryModel.value.id= row.id;
}

// 調用接口編輯分類項
const updateCategory=async()=>{
    let result=await articleCategoryUpdateService(categoryModel.value);
    ElMessage.success(result.message?result.message:"編輯成功");
    // 刷新列表
    articleCategoryList();
    dialogVisible.value=false;
}

// 清空模型的數據
const clearData=()=>{
    categoryModel.value.categoryName='';
    categoryModel.value.categoryAlias='';
}

// 調用接口刪除分類項

import {ElMessageBox} from 'element-plus'

const deleteCategory=(row)=>{
    ElMessageBox.confirm(
    '您確定要刪除該分類嗎',
    '溫馨提示',
    {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  )
    .then(async() => {
        // 調用街口
        let result=await articleCategoryDeleteService(row.id);
      ElMessage({
        type: 'success',
        message: '刪除成功',
      })
        //刷新列表
      articleCategoryList();
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消刪除',
      })
    })
}

</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章分類</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible=true;title='添加分類'; clearData()">添加分類</el-button>
                </div>
            </div>
        </template>
        <el-table :data="categorys" style="width: 100%">
            <el-table-column label="序號" width="100" type="index"> </el-table-column>
            <el-table-column label="分類名稱" prop="categoryName"></el-table-column>
            <el-table-column label="分類別名" prop="categoryAlias"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteCategory(row)"></el-button>
                </template>
            </el-table-column>
            <!-- <template #empty>
                <el-empty description="沒有數據" />
            </template> -->
        </el-table>

        <!-- 添加分類彈窗 -->
        <el-dialog v-model="dialogVisible" :title="title" width="30%">
            <el-form :model="categoryModel" :rules="rules" label-width="100px" style="padding-right: 30px">
                <el-form-item label="分類名稱" prop="categoryName">
                    <el-input v-model="categoryModel.categoryName" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <el-form-item label="分類別名" prop="categoryAlias">
                    <el-input v-model="categoryModel.categoryAlias" minlength="1" maxlength="15"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="title==='添加分類'?addCategory():updateCategory()">確認</el-button>
                </span>
            </template>
        </el-dialog>

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
}
</style>
 