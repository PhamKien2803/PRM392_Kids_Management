package com.example.se1829_prm392_kindergartenms;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se1829_prm392_kindergartenms.DAO.AccountDao;
import com.example.se1829_prm392_kindergartenms.DAO.ClassDao;
import com.example.se1829_prm392_kindergartenms.DAO.ParentDao;
import com.example.se1829_prm392_kindergartenms.DAO.ScheduleDao;
import com.example.se1829_prm392_kindergartenms.DAO.ScheduleToClassDao;
import com.example.se1829_prm392_kindergartenms.DAO.StudentDao;
import com.example.se1829_prm392_kindergartenms.DAO.StudentToClassDao;
import com.example.se1829_prm392_kindergartenms.DAO.TeacherDao;
import com.example.se1829_prm392_kindergartenms.Entity.Account;
import com.example.se1829_prm392_kindergartenms.Entity.Parent;
import com.example.se1829_prm392_kindergartenms.Entity.Schedule;
import com.example.se1829_prm392_kindergartenms.Entity.SchedulesToClass;
import com.example.se1829_prm392_kindergartenms.Entity.Student;
import com.example.se1829_prm392_kindergartenms.Entity.StudentToClass;
import com.example.se1829_prm392_kindergartenms.Entity.Teacher;
import com.example.se1829_prm392_kindergartenms.Entity.Class;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "InsertSampleData";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Xóa tất cả tài khoản trước khi chèn mới
        AccountDao accountDao = new AccountDao(this);
        accountDao.deleteAll();

        insertSampleData(); // chèn lại dữ liệu
    }


    private void insertSampleData() {
        // Khởi tạo DAO
        TeacherDao teacherDao = new TeacherDao(this);
        ParentDao parentDao = new ParentDao(this);
        AccountDao accountDao = new AccountDao(this);
        ClassDao classDao = new ClassDao(this);
        ScheduleDao scheduleDao = new ScheduleDao(this);
        StudentDao studentDao = new StudentDao(this);
        ScheduleToClassDao scheduleToClassDao = new ScheduleToClassDao(this);
        StudentToClassDao studentToClassDao = new StudentToClassDao(this);

        // 1. Thêm giáo viên
        Teacher teacher = new Teacher("GV001", "Trịnh Phạm Đoan Trang", "Hà Nội", "0909123456", "1980-04-12");
        long tResult = teacherDao.insert(teacher);

        Teacher teacher2 = new Teacher("GV003", "Nguyễn Thị Khánh Vi", "Hà Nội", "0909123456", "1980-04-12");
        long tResult2 = teacherDao.insert(teacher2);

        Teacher teacher3 = new Teacher("GV004", "Nguyễn Đình Minh Quân", "Hà Nội", "0909123456", "1980-04-12");
        long tResult3 = teacherDao.insert(teacher3);

        Teacher principal = new Teacher("GV002", "Trần Long", "Hà Nội", "0989123456", "1988-04-12");
        long printResult = teacherDao.insert(principal);

        // 2. Thêm phụ huynh
        Parent parent = new Parent("PH001", "Cao Trọng Hải", "Yên Bái", "0912345678", "1985-08-30");
        long pResult = parentDao.insert(parent);

        // 3. Thêm tài khoản giáo viên
        Account teacherAccount = new Account();
        teacherAccount.setUsername("teacher");
        teacherAccount.setPassword("123456");
        teacherAccount.setEmail("nguyendinhminhquan03@gmail.com");
        teacherAccount.setRole(1); // giáo viên
        teacherAccount.setTeacherId(teacher);
        teacherAccount.setParentId(null);
        long a1 = accountDao.insert(teacherAccount);

        Account teacherAccount2 = new Account();
        teacherAccount2.setUsername("teacher2");
        teacherAccount2.setPassword("123456");
        teacherAccount2.setEmail("teacher2@kids.edu.vn");
        teacherAccount2.setRole(1); // giáo viên
        teacherAccount2.setTeacherId(teacher2);
        teacherAccount2.setParentId(null);
        long a3 = accountDao.insert(teacherAccount2);

        Account principalAccount = new Account();
        principalAccount.setUsername("principal");
        principalAccount.setPassword("123456");
        principalAccount.setEmail("principal@kids.edu.vn");
        principalAccount.setRole(0); // hiệu trường
        principalAccount.setTeacherId(principal);
        principalAccount.setParentId(null);
        long a11 = accountDao.insert(principalAccount);

        // 4. Thêm tài khoản phụ huynh
        Account parentAccount = new Account();
        parentAccount.setUsername("parent");
        parentAccount.setPassword("123456");
        parentAccount.setEmail("parent@kids.edu.vn");
        parentAccount.setRole(2); // phụ huynh
        parentAccount.setParentId(parent);
        parentAccount.setTeacherId(null);
        long a2 = accountDao.insert(parentAccount);

        // 6. Thêm thời khóa biểu
        Schedule schedule1 = new Schedule("SCH001", "Toán - Nhận biết số lượng", "08:00", "08:45", "14/07/2025");
        Schedule schedule2 = new Schedule("SCH002", "Văn - Kể chuyện bé ngoan", "09:00", "09:45", "14/07/2025");
        long s1 = scheduleDao.insert(schedule1);
        long s2 = scheduleDao.insert(schedule2);

        // 5. Thêm lớp học (sau khi có GV001)
        Class class1 = new Class();
        class1.setClassId("CL01");
        class1.setClassName("Lá 2");
        class1.setSchoolYear("2024-2025");
        class1.setTeacherId(teacher);
        class1.setScheduleId(schedule1);
        long cResult = classDao.insert(class1);

        Class class2 = new Class();
        class2.setClassId("CL02");
        class2.setClassName("Lá 3");
        class2.setSchoolYear("2024-2025");
        class2.setTeacherId(teacher2);
        class2.setScheduleId(schedule1);
        long cResult2 = classDao.insert(class2);


        // 7. Thêm học sinh
        Student student = new Student();
        student.setStudentId("STU01");
        student.setParentId(parent);
        student.setFullName("Nguyễn Minh Anh");
        student.setAddress("Hà Nội");
        student.setDob("2020-01-15");
        student.setClassId(class1);
        long stResult = studentDao.insert(student);

        SchedulesToClass schedulesToClass = new SchedulesToClass();
        schedulesToClass.setScheduleClassID("1");
        schedulesToClass.setClassId(class1);
        schedulesToClass.setScheduleId(schedule1);
        long stcResult = scheduleToClassDao.insert(schedulesToClass);

        StudentToClass studentToClass = new StudentToClass();
        studentToClass.setStudentClassID("1");
        studentToClass.setStudentId(student);
        studentToClass.setClassId(class1);
        long sutcResult = studentToClassDao.insert(studentToClass);

        // 8. Kiểm tra và thông báo
        String message = "Đã chèn dữ liệu:\n"
                + "Giáo viên: " + (tResult != -1 ? "OK" : "FAIL") + "\n"
                + "Phụ huynh: " + (pResult != -1 ? "OK" : "FAIL") + "\n"
                + "Hiệu trưởng: " + (printResult != -1 ? "OK" : "FAIL") + "\n"
                + "Account GV: " + (a1 != -1 ? "OK" : "FAIL") + "\n"
                + "Account GV2: " + (a3 != -1 ? "OK" : "FAIL") + "\n"
                + "Account PH: " + (a2 != -1 ? "OK" : "FAIL") + "\n"
                + "Account Principal: " + (a11 != -1 ? "OK" : "FAIL") + "\n"
                + "Schedule" + (s1 != -1 ? "OK" : "FAIL") + "\n"
                + "Schedule" + (s2 != -1 ? "OK" : "FAIL") + "\n"
                + "Class" + (cResult != -1 ? "OK" : "FAIL") + "\n"
                + "student" + (stResult != -1 ? "OK" : "FAIL") + "\n"
                + "studentToClass" + (sutcResult != -1 ? "OK" : "FAIL") + "\n"
                + "schedulesToClass" + (stcResult != -1 ? "OK" : "FAIL") + "\n";



        Toast.makeText(this, message, Toast.LENGTH_LONG).show();


    }
}