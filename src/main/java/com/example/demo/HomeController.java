package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;

@Controller
public class HomeController {

    //    PUBLIC VIEWS
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CourseRepository courseRepository;

    @RequestMapping("/")
    public String loadHomePage(Model model) {
        model.addAttribute("catalog", courseRepository.findAll());
        return "home";
    }

    @GetMapping("/newstudent")
    public String loadStudentForm(Model model) {
        model.addAttribute("student", new User());
        return "studentform";
    }

    @PostMapping("/processstudent")
    public String processStudent(@ModelAttribute User student, Model model) {
        userRepository.save(student);
        Role studentRole = new Role(student.getUsername(), "ROLE_STUDENT");
        roleRepository.save(studentRole);

        model.addAttribute("message", "Your student account has been created. An instructor or administrator " +
                "must authorize your account before you can login and register for classes. For now, you may continue " +
                "to browse the course catalog.");
        return "studentconfirmation";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }

    //    SUPER VIEWS
    @GetMapping("/super")
    public String loadSuperPortal() {
        return "superportal";
    }

    @GetMapping("/newteacher")
    public String loadTeacherForm(Model model) {
        model.addAttribute("teacher", new User());
        return "teacherform";
    }

    @PostMapping("/processteacher")
    public String processTeacher(@ModelAttribute User teacher, Model model) {

        teacher.setEnabled(true);
        userRepository.save(teacher);

        Role teacherRole = new Role(teacher.getUsername(), "ROLE_TEACHER");
        roleRepository.save(teacherRole);

        model.addAttribute("message", "New staff profile created. " +
                "Provide instructor with their login credentials");

        return "teacherconfirmation";
    }

    //    TEACHER VIEWS
    @GetMapping("/teacherportal")
    public String loadTeacherPortal(Model model, Principal principal) {
        model.addAttribute("students", roleRepository.findAllByRole("ROLE_STUDENT"));

//        ArrayList<String> usernames = new ArrayList<>();
//
////        for (Role role : roleRepository.findAll()){
////            if (role.getRole().equals("ROLE_STUDENT")){
////                usernames.add(role.getUsername());
////            }
////        }
////
////        for (String student : usernames){
////            if(!userRepository.findByUsername(student).isEnabled()){
////
////            }
////        }

        //STUDENTS THAT REGISTERED BUT ARE NOT ENABLED
        model.addAttribute("pendingStudents", userRepository.findAllByEnabledFalse());

        //FIND CURRENT LOGGED IN TEACHER'S COURSES
        model.addAttribute("myCourses", courseRepository.findByInstructor(principal.getName()));

        //ALL COURSES
        model.addAttribute("catalog", courseRepository.findAll());
        return "teacherportal";
    }

    @RequestMapping("/teacherportal/togglestudent/{id}")
    public String toggleStudentAccount(@PathVariable("id") long studentID, Model model) {

        User student;
        student = userRepository.findById(studentID).get();

        Boolean enabled = student.isEnabled();
        student.setEnabled(!enabled);
        userRepository.save(student);
        return "redirect:/teacherportal";
    }

    @GetMapping("/newcourse")
    public String newCourse(Model model) {
        model.addAttribute("course", new Course());
        return "courseform";
    }

    @PostMapping("/processcourse")
    public String processCourse(@ModelAttribute Course course, Model model, Principal principal) {

        course.setInstructor(principal.getName()); //set Course instructor to currently logged in user (ROLE_TEACHER)
        courseRepository.save(course);

        return "redirect:/teacherportal";
    }

    @RequestMapping("/updatecourse/{id}")
    public String updateCourse(@PathVariable("id") long id, Model model){
        model.addAttribute("course", courseRepository.findById(id));
        return "courseform";
    }

    @RequestMapping("/deletecourse/{id}")
    public String delete(@PathVariable("id") long id, Model model){
        courseRepository.deleteById(id);
        return "redirect:/teacherportal";
    }

    //    STUDENT VIEWS
    @GetMapping("/studentportal")
    public String loadStudentPortal(Principal principal, Model model) {

        User student = userRepository.findByUsername(principal.getName());
        model.addAttribute("myCourses", student.getCourses());

        return "studentportal";
    }

    @RequestMapping("/enroll/{id}")
    public String enrollCourse(@PathVariable("id") long courseID, Model model, Principal principal){
        //find the course
        Course course = courseRepository.findById(courseID).get();

        //find current logged in user and save course to their set
        User student = userRepository.findByUsername(principal.getName());
        student.enrollCourse(course);

        //save changes to user repository
        userRepository.save(student);

        return "redirect:/";
    }

}
