
package com.example.project_prm392_kidmanagement;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392_kidmanagement.DAO.AccountDao;
import com.example.project_prm392_kidmanagement.DAO.ClassDao;
import com.example.project_prm392_kidmanagement.DAO.ParentDao;
import com.example.project_prm392_kidmanagement.DAO.ScheduleDao;
import com.example.project_prm392_kidmanagement.DAO.StudentDao;
import com.example.project_prm392_kidmanagement.DAO.TeacherDao;
import com.example.project_prm392_kidmanagement.Entity.Account;
import com.example.project_prm392_kidmanagement.Entity.Class;
import com.example.project_prm392_kidmanagement.Entity.Parent;
import com.example.project_prm392_kidmanagement.Entity.Schedule;
import com.example.project_prm392_kidmanagement.Entity.Student;
import com.example.project_prm392_kidmanagement.Entity.Teacher;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        insertSampleData();

        // startActivity(new Intent(this, AccountManagerActivity.class));
        // finish();
    }

    private void insertSampleData() {
        TeacherDao teacherDao = new TeacherDao(this);
        ParentDao parentDao = new ParentDao(this);
        AccountDao accountDao = new AccountDao(this);
        ClassDao classDao = new ClassDao(this);
        ScheduleDao scheduleDao = new ScheduleDao(this);
        StudentDao studentDao = new StudentDao(this);

        // 1. Insert Teacher
        Teacher teacher = new Teacher("GV001", "Nguyễn Xuân Mai", "Hà Nội", "0909123456", "1980-04-12");
        teacherDao.insert(teacher);

        // 2. Insert Parent
        Parent parent = new Parent("PH001", "Bùi Trung Hiếu", "Yên Bái", "0912345678", "1985-08-30");
        parentDao.insert(parent);

        // 3. Insert Accounts
        Account accTeacher = new Account("teacher", "123456", "teacher.mai@sakura.edu.vn", 1, "GV001", null);
        Account accParent = new Account("parent", "123456", "parent@sakura.edu.vn", 0, null, "PH001");
        accountDao.insert(accTeacher);
        accountDao.insert(accParent);

        // 4. Insert Schedule
        Schedule schedule1 = new Schedule("SCH001", "Toán - Nhận biết số lượng", "08:00", "08:45", "2025-06-17");
        Schedule schedule2 = new Schedule("SCH002", "Văn - Kể chuyện bé ngoan", "09:00", "09:45", "2025-06-17");
        scheduleDao.insert(schedule1);
        scheduleDao.insert(schedule2);

        // 5. Insert Class
        Class class1 = new Class("CLS001", "Lá 2", "2024-2025", "GV001", null);
        classDao.insert(class1);

        // 6. Insert Student
        Student student = new Student("ST001", "PH001", "Nguyễn Minh Anh", "Hà Nội", "2020-01-15", "CLS001");
        studentDao.insert(student);

        Toast.makeText(this, "Sample data inserted!", Toast.LENGTH_LONG).show();
    }
}
