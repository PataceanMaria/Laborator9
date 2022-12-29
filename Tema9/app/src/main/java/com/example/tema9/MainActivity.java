package com.example.tema9;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etName, etAge;
    CheckBox cbStatus;
    Button btnView, btnAdd,btnSearch;
    ListView mainListView;
    ArrayAdapter studentArrayAdapter;
    TextView textView;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        cbStatus = findViewById(R.id.cbStatus);
        btnView = findViewById(R.id.btnView);
        btnAdd = findViewById(R.id.btnAdd);
        btnSearch = findViewById(R.id.btnSearch);
        mainListView = findViewById(R.id.mainListView);
        textView = findViewById(R.id.textView);

        //displaying the DB content inside the listview as soon as the app starts
        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        //DisplayStudentsInListView(dataBaseHelper);
        studentArrayAdapter = new ArrayAdapter<StudentModel>(MainActivity.this, android.R.layout.simple_list_item_1,dataBaseHelper.getEveryone());
        mainListView.setAdapter(studentArrayAdapter);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper = new DataBaseHelper(MainActivity.this);
                List<StudentModel> everyone = dataBaseHelper.getEveryone();
                //DisplayStudentsInListView(dataBaseHelper);
                studentArrayAdapter = new ArrayAdapter<StudentModel>(MainActivity.this, android.R.layout.simple_list_item_1, everyone);
                mainListView.setAdapter(studentArrayAdapter);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StudentModel studentModel = null;

                try{
                    studentModel = new StudentModel(-1, etName.getText().toString(),Integer.parseInt(etAge.getText().toString()), cbStatus.isChecked());
                }
                catch(Exception exception){
                    studentModel = new StudentModel(-1,"Error",0,false);
                    Toast.makeText(getApplicationContext(), exception.toString(),Toast.LENGTH_LONG).show();
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                boolean actionSuccess = dataBaseHelper.addOne(studentModel);

                //update the listview content
                dataBaseHelper = new DataBaseHelper(MainActivity.this);
                //DisplayStudentsInListView(dataBaseHelper);
                studentArrayAdapter = new ArrayAdapter<StudentModel>(MainActivity.this, android.R.layout.simple_list_item_1,dataBaseHelper.getEveryone());
                mainListView.setAdapter(studentArrayAdapter);

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = etName.getText().toString();

                // Search for the word in the database.
                Cursor cursor = dataBaseHelper.search(word);

                // Only process a non-null cursor with rows.
                if (cursor != null & cursor.getCount() > 0) {
                    // You must move the cursor to the first item.
                    cursor.moveToFirst();
                    int index_name, index_age, index_status;
                    String result_name, result_age, result_status;
                    // Iterate over the cursor, while there are entries.
                    do {
                        // Don't guess at the column index.
                        // Get the index for the named column.
                        index_name = cursor.getColumnIndex(DataBaseHelper.COLUMN_STUDENT_NAME);
                        index_age = cursor.getColumnIndex(DataBaseHelper.COLUMN_STUDENT_AGE);
                        index_status = cursor.getColumnIndex(DataBaseHelper.COLUMN_ACTIVE_STATUS);
                        // Get the value from the column for the current cursor.
                        result_name = cursor.getString(index_name);
                        result_age = cursor.getString(index_age);
                        if(index_status==1){
                            result_status = "active";
                        }else{
                            result_status = "inactive";
                        }
                        // Add result to what's already in the text view.
                        textView.setText(result_name+", "+result_age+", "+result_status+ "\n");
                    } while (cursor.moveToNext()); // Returns true or false
                    cursor.close();
                }else{
                    Toast.makeText(getApplicationContext(),"This student isn't registered.", Toast.LENGTH_LONG).show();
                }
            }
        });

//        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                StudentModel clickedStudent = (StudentModel) parent.getItemAtPosition(position);
//                dataBaseHelper.deleteOne(clickedStudent);
//
//                studentArrayAdapter = new ArrayAdapter<StudentModel>(MainActivity.this, android.R.layout.simple_list_item_1,dataBaseHelper.getEveryone());
//                mainListView.setAdapter(studentArrayAdapter);
//
//                Toast.makeText(getApplicationContext(), "Student deleted: "+clickedStudent.toString(),Toast.LENGTH_LONG).show();
//            }
//        });


    }//onCreate

/*    //method to use for updating the listview
private void DisplayStudentsInListView(DataBaseHelper dataBaseHelper){
    studentArrayAdapter = new ArrayAdapter<StudentModel>(MainActivity.this, android.R.layout.simple_list_item_1,dataBaseHelper.getEveryone());
    mainListView.setAdapter(studentArrayAdapter);
}
*/

}//main