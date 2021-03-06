package com.sy.controller;

import com.sy.entity.Student;
import com.sy.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.ws.Response;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @author chris
 */
@Controller
@RequestMapping(value = "/student")
public class StudentController extends BaseController {

    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/toIndex")
    public String toIndex() {
        return "admin/html/index";
    }

    @RequestMapping(value = "/toTable")
    public String toTable() {
        return "admin/html/table-student";
    }

    @RequestMapping(value = "/loginCheck")
    public String loginCheck(@Valid Student student, Model model) {
        System.out.println("ִ执行 loginCheck");
        System.out.println(student.toString());
        model.addAttribute("student", student);
        return studentService.loginCheck(student.getsName(), student.getsPwd(), student.getsBirth());
    }

    @RequestMapping(value = "/addStudent", method = RequestMethod.POST)
    public ModelAndView addStudent(@Valid @ModelAttribute("student") Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getFieldError().getField());
            return new ModelAndView("redirect:/student/toIndex");
        }
        boolean result = studentService.addStudent(student.getsName(), student.getsNickname(), student.getsPwd(), student.getsEmail(), student.getsBirth());
        if (result) {
            return new ModelAndView("redirect:/student/students");
        } else {
            return new ModelAndView("redirect:/student/toIndex");
        }
    }

    @RequestMapping(value = "/updateStudent", method = RequestMethod.POST)
    public ModelAndView updateStudent(@Valid @ModelAttribute("student") Student student) {
        boolean result = studentService.updateStudent(student.getsId(), student.getsName(), student.getsNickname(), student.getsPwd(), student.getsEmail(), student.getsUpdateDate(), student.getsBirth());
        if (result) {
            return new ModelAndView("redirect:/student/students");
        } else {
            return new ModelAndView("redirect:/student/toIndex");
        }
    }

    @RequestMapping(value = "queryStudent", method = RequestMethod.GET)
    public void queryStudent(@Valid @ModelAttribute("sId") int sId, Model model, HttpServletResponse response) throws IOException {
        String student = studentService.queryStudent(sId);
        if (student != null) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter wirte = response.getWriter();
                wirte.print(student);
                wirte.flush();
                wirte.close();
            }
        }

    @RequestMapping(value = "deleteStudent", method = RequestMethod.GET)
    public ModelAndView deleteStudent(@ModelAttribute("sId") int sId) {
        boolean result = studentService.deleteStudent(sId);
        if (result) {
            return new ModelAndView("redirect:/student/students");
        } else {
            return new ModelAndView("redirect:/student/toIndex");
        }
    }

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public String showMyProduct(HttpServletRequest request, Model model) {
        this.studentService.showStudentsByPage(request, model);
        return "/admin/html/table-student";
    }

}
