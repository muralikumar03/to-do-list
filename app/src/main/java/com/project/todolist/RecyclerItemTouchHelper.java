package com.project.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.telephony.RadioAccessSpecifier;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.project.todolist.adaptor.ToDoAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private ToDoAdapter adopter;
    public RecyclerItemTouchHelper(ToDoAdapter adopter){
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adopter=adopter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target){
        return false;
    }
    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder,int direction){
        final int position = viewHolder.getBindingAdapterPosition();
        if(direction==ItemTouchHelper.LEFT){
            AlertDialog.Builder builder=new AlertDialog.Builder(adopter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure,wnat to delete the task");
            builder.setPositiveButton("Confirm",
                    (dialogInterface, i) -> {
                        adopter.deleteItem(position);
                    });
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                adopter.notifyItemChanged(viewHolder.getBindingAdapterPosition());
            });
            AlertDialog dialog=builder.create();
            dialog.show();
        }
        else {
            adopter.editItem(position);
        }

    }

    @Override
    public void onChildDraw(Canvas c,RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,float dX,float dY,int actionState,boolean isCurrentlyActive){
        super.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;
        View itemView=viewHolder.itemView;
        int backgroundCornerOffset=20;

        if(dX>0){
            icon= ContextCompat.getDrawable(adopter.getContext(),R.drawable.baseline_edit_);
            background=new ColorDrawable(ContextCompat.getColor(adopter.getContext(),R.color.colorPrimaryDark));
        }
        else{
            icon= ContextCompat.getDrawable(adopter.getContext(),R.drawable.baseline_delete);
            background=new ColorDrawable(Color.RED);
        }

        int iconMargin=(itemView.getHeight()-icon.getIntrinsicHeight())/2;
        int iconTop=itemView.getTop()+(itemView.getHeight()-icon.getIntrinsicHeight())/2;
        int iconBottom=iconTop+icon.getIntrinsicHeight();

        if(dX>0){//swipe to right
            int iconLeft = itemView.getLeft()+iconMargin;
            int iconRight= itemView.getLeft()+iconMargin+icon.getIntrinsicHeight();
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        }
        else if(dX<0){
            //swiping left
            int iconLeft = itemView.getRight()-iconMargin-icon.getIntrinsicHeight();
            int iconRight= itemView.getRight()-iconMargin;
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,itemView.getTop(), itemView.getRight(), itemView.getBottom());

        }
        else{
            background.setBounds(0,0,0,0);
        }
        background.draw(c);
        icon.draw(c);
    }

}
