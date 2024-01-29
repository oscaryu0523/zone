package com.example.controller;

import com.example.entity.Category;
import com.example.entity.Result;
import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public Result add(@RequestBody @Validated(Category.Add.class) Category category){
        categoryService.add(category);
        return Result.success();
    }
    @GetMapping
    public Result<List<Category>> list(){
        List<Category> list=categoryService.list();
        return Result.success(list);
    }
    @GetMapping("/detail")
    public Result<Category> detail(Integer id){
        Category category=categoryService.findById(id);
        return Result.success(category);
    }
    @PutMapping
    public Result update(@RequestBody @Validated(Category.Update.class) Category category){
        categoryService.update(category);
        return Result.success();
    }
    @DeleteMapping
    public Result delete(@RequestParam Integer id){
        categoryService.delete(id);
        return Result.success();
    }

}
