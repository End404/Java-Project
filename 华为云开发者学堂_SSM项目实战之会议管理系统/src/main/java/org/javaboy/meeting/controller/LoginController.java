

//  第3章 用户注册与登陆
//        3.1 用户登陆
//    3.4 用户注册准备


package org.javaboy.meeting.controller;

import org.javaboy.meeting.model.Department;
import org.javaboy.meeting.model.Employee;
import org.javaboy.meeting.servlice.DepartmentServce;
import org.javaboy.meeting.servlice.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    EmployeeService employeeService;

    @Autowired
    DepartmentServce departmentServce;  //3.4

    @RequestMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/doLogin")
    public String doLogin(String username, String password, Model model, HttpServlet httpServlet) {
        Employee employee = employeeService.doLogin(username, password);
        if (employee == null){
            model.addAttribute("errer","用户名或密码输入错误，登陆失败");
            return "forward:/";
        } else{
            if (employee.getStatus() == 0) {
                model.addAttribute("errer","用户待审批");
                return "forward:/";
            }else if (employee.getStatus() == 2) {
                    model.addAttribute("errer","用户审批未通过");
                    return "forward:/";
            }else{
                httpServlet.setAttribute("currentuser",employee);
                return "redirect:/notifications";
            }
        }
    }

//    3.4
    @RequestMapping("/register")
    public String register(Model model) {
        List<Department> deps = departmentServce.getAllDeps();
        model.addAttribute("deps", deps);
        return "register";
    }
    @RequestMapping("/doReg")
    public String doReg(Employee employee, Model model){
        Integer result = employeeService.doReg(employee);
        if (result == 1) {
            return "redirect:/";
        }else {
            model.addAttribute("error","注册失败");
            model.addAttribute("employee", employee);
            return "forward:/register";
        }
    }
}
