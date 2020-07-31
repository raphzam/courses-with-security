package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerBean implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void run(String...strings){

//        SUPER USER AND ROLE
        User superUser = new User();
        superUser.setUsername("super");
        superUser.setPassword("super");
        superUser.setEnabled(true);

        Role superRole1 = new Role("super", "ROLE_SUPER");
        Role superRole2 = new Role("super", "ROLE_TEACHER");

        userRepository.save(superUser);
        roleRepository.save(superRole1);
        roleRepository.save(superRole2);

//        TEACHER USER AND ROLE

        User teacher = new User();
        teacher.setUsername("teacher");
        teacher.setPassword("teacher");
        teacher.setEnabled(true);

        Role teacherRole = new Role("teacher", "ROLE_TEACHER");

        userRepository.save(teacher);
        roleRepository.save(teacherRole);

//        STUDENT USER AND ROLE

        User student = new User();
        student.setUsername("student");
        student.setPassword("student");
        student.setEnabled(true);

        Role studentRole = new Role("student", "ROLE_STUDENT");

        userRepository.save(student);
        roleRepository.save(studentRole);

    }
}
