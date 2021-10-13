package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-10-08
 */
@Api(tags = "讲师管理")
@RestController
@RequestMapping("/eduservice/edu-teacher")
public class EduTeacherController {

    //访问地址  http://localhost:8001/eduservice/edu-teacher/findAll
    //注入service
    @Autowired
    private EduTeacherService teacherService;

    //1查询表中的所有数据
    //rest风格
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher() {
        //调用service的方法来查询所有的操作
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }

    //2逻辑删除讲师的方法
    @ApiOperation(value = "根据id逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@ApiParam(name = "id", value = "讲师ID", required = true)
                           @PathVariable String id) {
        boolean flag = teacherService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //3.分页查询讲师的方法           当前页  每页显示记录数
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,
                             @PathVariable long limit) {

        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面
        teacherService.page(pageTeacher, null);

        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords();//数据list集合
//        Map map = new HashMap();
//        map.put("total",total);
//        map.put("rows",records);
//        return R.ok().data(map);

        return R.ok().data("total", total).data("rows", records);
    }

    //4条件查询带分页的方法
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current, @PathVariable long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery) {
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        if (!StringUtils.isEmpty(name)) {
            //构建条件
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }
        //调用方法实现条件查询分页
        teacherService.page(pageTeacher, wrapper);
        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords();//数据list集合
        System.out.println("git1");
        System.out.println("git3");
        System.out.println("git4");
        System.out.println("master test");
        System.out.println("hot-fix test");
        return R.ok().data("total", total).data("rows", records);
    }
}

