package com.rakib.studentportal;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rakib.studentportal.model.Payment;
import com.rakib.studentportal.model.Student;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StudentPayment.db";
    private static final int DATABASE_VERSION = 1;

    // Student table
    private static final String TABLE_STUDENTS = "students";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD = "password";

    // Payment table
    private static final String TABLE_PAYMENTS = "payments";
    private static final String COLUMN_PAYMENT_ID = "payment_id";
    private static final String COLUMN_STUDENT_ID = "student_id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStudentTable = "CREATE TABLE " + TABLE_STUDENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT UNIQUE,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_PASSWORD + " TEXT"
                + ")";

        String createPaymentTable = "CREATE TABLE " + TABLE_PAYMENTS + "("
                + COLUMN_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_STUDENT_ID + " INTEGER,"
                + COLUMN_AMOUNT + " REAL,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_STATUS + " TEXT DEFAULT 'Pending',"
                + COLUMN_DATE + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COLUMN_ID + ")"
                + ")";

        db.execSQL(createStudentTable);
        db.execSQL(createPaymentTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
        onCreate(db);
    }

    // Student CRUD operations
    public boolean addStudent(String name, String email, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_STUDENTS, null, values);
        return result != -1;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS, null);

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setId(cursor.getInt(0));
                student.setName(cursor.getString(1));
                student.setEmail(cursor.getString(2));
                student.setPhone(cursor.getString(3));
                students.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return students;
    }

    public boolean updateStudent(int id, String name, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);

        int result = db.update(TABLE_STUDENTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_STUDENTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Student getStudentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Student student = new Student();
            student.setId(cursor.getInt(0));
            student.setName(cursor.getString(1));
            student.setEmail(cursor.getString(2));
            student.setPhone(cursor.getString(3));
            cursor.close();
            return student;
        }
        return null;
    }

    // Student login
    public Student loginStudent(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, null,
                COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{email, password}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Student student = new Student();
            student.setId(cursor.getInt(0));
            student.setName(cursor.getString(1));
            student.setEmail(cursor.getString(2));
            student.setPhone(cursor.getString(3));
            cursor.close();
            return student;
        }
        return null;
    }

    // Payment operations
    public boolean makePayment(int studentId, double amount, String type, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_ID, studentId);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_DATE, date);

        long result = db.insert(TABLE_PAYMENTS, null, values);
        return result != -1;
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT p.*, s." + COLUMN_NAME + " FROM " + TABLE_PAYMENTS + " p " +
                "INNER JOIN " + TABLE_STUDENTS + " s ON p." + COLUMN_STUDENT_ID + " = s." + COLUMN_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Payment payment = new Payment();
                payment.setPaymentId(cursor.getInt(0));
                payment.setStudentId(cursor.getInt(1));
                payment.setAmount(cursor.getDouble(2));
                payment.setType(cursor.getString(3));
                payment.setStatus(cursor.getString(4));
                payment.setDate(cursor.getString(5));
                payment.setStudentName(cursor.getString(6));
                payments.add(payment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return payments;
    }

    public List<Payment> getStudentPayments(int studentId) {
        List<Payment> payments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PAYMENTS, null, COLUMN_STUDENT_ID + " = ?",
                new String[]{String.valueOf(studentId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Payment payment = new Payment();
                payment.setPaymentId(cursor.getInt(0));
                payment.setStudentId(cursor.getInt(1));
                payment.setAmount(cursor.getDouble(2));
                payment.setType(cursor.getString(3));
                payment.setStatus(cursor.getString(4));
                payment.setDate(cursor.getString(5));
                payments.add(payment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return payments;
    }

    public boolean updatePaymentStatus(int paymentId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);

        int result = db.update(TABLE_PAYMENTS, values, COLUMN_PAYMENT_ID + " = ?",
                new String[]{String.valueOf(paymentId)});
        return result > 0;
    }
}
