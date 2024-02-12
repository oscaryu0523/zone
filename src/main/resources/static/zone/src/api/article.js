import request from '@/utils/request.js'

// 文章分類列表查詢
export const articleCategoryListService=()=>{

   return request.get("/category");
}

// 文章分類列表查詢
export const articleCategoryListAllService=()=>{

   return request.get("/category/listAll");
}

// 文章分類添加
export const articleCategoryAddService=(categoryData)=>{
   return request.post("/category",categoryData)
}

// 文章分類編輯
export const articleCategoryUpdateService=(categoryData)=>{
   return request.put("/category",categoryData)
}

// 文章分類刪除
export const articleCategoryDeleteService=(id)=>{
   return request.delete('/category?id='+id)
}

// 文章列表查詢
export const articleListService=(params)=>{
   return request.get('/article/list',{params:params})
}

//文章添加
export const articleAddService=(articleData)=>{
   return request.post('/article',articleData)
}

//文章更新
export const articleUpdateService=(articleData)=>{
   return request.put('/article',articleData)
}

// 文章刪除
export const articleDeleteService=(id)=>{
   return request.delete('article?id='+id)
}

// 文章全部列表查詢
export const articleListAllService=(params)=>{
   return request.get('/article/listAll',{params:params})
}

//文章查詢
export const articleSearchService=(articleId, userId)=>{
   return request.get('/article/search',{params:{articleId:articleId,userId:userId}})
}

// 文章留言
export const articleCommentAddService=(commentData)=>{
   return request.post('/comment',commentData)
}

// 文章留言更新
export const articleCommentUpdateService=(params)=>{
   const queryParams = new URLSearchParams(params).toString();
   return request.put(`/comment?${queryParams}`);
}

// 文章留言刪除
export const articleCommentDeleteService=(id)=>{
   return request.delete('/comment?id='+id);
}

// 文章按讚
export const articleLikeService=(likeData)=>{
   return request.post('/like',likeData);
}
