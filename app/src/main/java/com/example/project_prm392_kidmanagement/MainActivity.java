package com.example.project_prm392_kidmanagement;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392_kidmanagement.DAO.AccountDao;
import com.example.project_prm392_kidmanagement.DAO.ParentDao;
import com.example.project_prm392_kidmanagement.DAO.TeacherDao;
import com.example.project_prm392_kidmanagement.Entity.Account;
import com.example.project_prm392_kidmanagement.Entity.Parent;
import com.example.project_prm392_kidmanagement.Entity.Teacher;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Chèn dữ liệu mẫu khi app mở lần đầu (chỉ nên dùng để test)
        insertSampleData();

        // Gợi ý: chuyển sang màn login sau khi insert
        // startActivity(new Intent(this, AccountManagerActivity.class));
        // finish();
    }

    private void insertSampleData() {
        TeacherDao teacherDao = new TeacherDao(this);
        ParentDao parentDao = new ParentDao(this);
        AccountDao accountDao = new AccountDao(this);

        // Insert giáo viên
        Teacher teacher = new Teacher("GV001", "Nguyễn Xuân Mai", "Hà Nội=", "0909123456", "1980-04-12");
        long tResult = teacherDao.insert(teacher);

        // Insert phụ huynh
        Parent parent = new Parent("PH001", "Bùi Trung Hiếu", "Yên Bái", "0912345678", "1985-08-30");
        long pResult = parentDao.insert(parent);

        // Insert account cho giáo viên
        Account teacherAccount = new Account();
        teacherAccount.setUsername("teacher");
        teacherAccount.setPassword("123456");
        teacherAccount.setEmail("teacher.mai@sakura.edu.vn");
        teacherAccount.setRole(true); // true = giáo viên
        teacherAccount.setTeacherId(teacher);
        teacherAccount.setParentId(null);
        long a1 = accountDao.insert(teacherAccount);

        // Insert account cho phụ huynh
        Account parentAccount = new Account();
        parentAccount.setUsername("parent");
        parentAccount.setPassword("123456");
        parentAccount.setEmail("parent@sakura.edu.vn");
        parentAccount.setRole(false); // false = phụ huynh
        parentAccount.setParentId(parent);
        parentAccount.setTeacherId(null);
        long a2 = accountDao.insert(parentAccount);

        // Thông báo kết quả
        String message = "Đã chèn dữ liệu:\n"
                + "Giáo viên: " + (tResult != -1 ? "OK" : "FAIL") + "\n"
                + "Phụ huynh: " + (pResult != -1 ? "OK" : "FAIL") + "\n"
                + "Account GV: " + (a1 != -1 ? "OK" : "FAIL") + "\n"
                + "Account PH: " + (a2 != -1 ? "OK" : "FAIL");

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
