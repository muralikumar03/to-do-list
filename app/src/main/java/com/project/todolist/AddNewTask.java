package com.project.todolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.project.todolist.model.ToDoModel;
import com.project.todolist.utils.DatabaseHandler;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG="ActionBottomDialog";
    private EditText newTaskText;
    private Button newTaskSaveButton;
    private DatabaseHandler db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceSate){
        super.onCreate(savedInstanceSate);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.new_task,container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        newTaskText=getView().findViewById(R.id.newTaskText);
        newTaskSaveButton=getView().findViewById(R.id.newTaskButton);

        db=new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate=false;//checks whether update or save
        final Bundle bundle=getArguments();
        if(bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            if (task.length() > 0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

        }

            newTaskText.addTextChangedListener(new TextWatcher() {
                //change the color of save button ,
                // if nothing entered no color change of button
                // if some thing is entered button color will be changed
                @Override// may throw some problem args change
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(charSequence.toString().equals("")){
                        newTaskSaveButton.setEnabled(false);
                        newTaskSaveButton.setTextColor(Color.GRAY);
                    }
                    else{
                        newTaskSaveButton.setEnabled(true);
                        newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            boolean finalIsUpdate = isUpdate;
            newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text=newTaskText.getText().toString();
                    if(finalIsUpdate){
                        db.updateTask(bundle.getInt("id"),text);
                    }
                    else{
                        ToDoModel task=new ToDoModel();
                        task.setTask(text);
                        task.setStatus(0);
                        db.insertTask(task);
                    }
                    dismiss();
                }
            });
        }
        @Override
        public void onDismiss(DialogInterface dialog){
            Activity activity=getActivity();
            if(activity instanceof DialogCloseListener){
                ((DialogCloseListener)activity).handleDialogClose(dialog);
            }
        }

    }




