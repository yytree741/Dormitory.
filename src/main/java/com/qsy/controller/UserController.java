package com.qsy.controller;

import com.github.pagehelper.PageInfo;
import com.qsy.entity.Student;
import com.qsy.entity.User;
import com.qsy.service.StudentService;
import com.qsy.service.UserService;
import com.qsy.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 596183363@qq.com
 * @Description:
 * @date 2020/11/1720:07
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;

    @PostMapping("create")
    public Result create(@RequestBody User user){
        int flag = userService.create(user);
        if(flag>0){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    @GetMapping("delete")
    public Result delete(String ids){
        int flag = userService.delete(ids);
        if(flag>0){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    @PostMapping("update")
    public Result update(@RequestBody User user){
        int flag = userService.updateSelective(user);
        if(flag>0){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    @GetMapping("detail")
    public User detail(Integer id){
        return userService.detail(id);
    }

    @GetMapping("info")
    public Result info(HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        return Result.ok(userService.detail(user.getId()));
    }

    @PostMapping("pwd")
    public Result pwd(@RequestBody Map<String,String> map,HttpServletRequest request){
        String spassword = map.get("spassword");
        String npassword = map.get("npassword");
        //用户修改
        if(request.getAttribute("user") != null){
            User user = (User)request.getAttribute("user");
            User entity = userService.detail(user.getId());
            if(entity.getPassword().equals(spassword)){
                User param = new User();
                param.setId(entity.getId());
                param.setPassword(npassword);
                userService.updatePwd(param);
                return Result.ok("修改密码成功");
            }else{
                return Result.fail("原密码错误");
            }
        }
        //学生修改
        if(request.getAttribute("student") != null){
            Student student = (Student)request.getAttribute("student");
            Student entity = studentService.detail(student.getId());
            if(entity.getPassword().equals(spassword)){
                Student param = new Student();
                param.setId(entity.getId());
                param.setPassword(npassword);
                studentService.updateSelective(param);
                return Result.ok("修改密码成功");
            }else{
                return Result.fail("原密码错误");
            }
        }
        return Result.ok();
    }

    @PostMapping("query")
    public Map<String,Object> query(@RequestBody User user){
        PageInfo<User> pageInfo = userService.query(user);
        return Result.ok(pageInfo);
    }

}
