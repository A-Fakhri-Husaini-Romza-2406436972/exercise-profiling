package com.advpro.profiling.tutorial.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advpro.profiling.tutorial.model.Student;
import com.advpro.profiling.tutorial.model.StudentCourse;
import com.advpro.profiling.tutorial.repository.StudentCourseRepository;
import com.advpro.profiling.tutorial.repository.StudentRepository;

/**
 * @author muhammad.khadafi
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    public List<StudentCourse> getAllStudentsWithCourses() {
        // Load all students and student-course relations once to avoid N+1 queries.
        List<Student> students = studentRepository.findAll();
        Map<Long, Student> studentById = new HashMap<>(students.size());
        for (Student s : students) {
            studentById.put(s.getId(), s);
        }

        List<StudentCourse> allStudentCourses = studentCourseRepository.findAll();
        List<StudentCourse> result = new ArrayList<>(allStudentCourses.size());
        for (StudentCourse sc : allStudentCourses) {
            StudentCourse mapped = new StudentCourse();
            Long sid = sc.getStudent() != null ? sc.getStudent().getId() : null;
            Student fullStudent = sid != null ? studentById.get(sid) : null;
            mapped.setStudent(fullStudent != null ? fullStudent : sc.getStudent());
            mapped.setCourse(sc.getCourse());
            result.add(mapped);
        }

        return result;
    }

    public Optional<Student> findStudentWithHighestGpa() {
        // Let the database find the top student by GPA to avoid loading all rows
        return studentRepository.findTopByOrderByGpaDesc();
    }

    public String joinStudentNames() {
        // Fetch only names and join them efficiently
        List<String> names = studentRepository.findAllNames();
        if (names.isEmpty()) return "";
        return String.join(", ", names);
    }
}

