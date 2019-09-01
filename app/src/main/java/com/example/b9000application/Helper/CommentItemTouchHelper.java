package com.example.b9000application.Helper;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b9000application.Adapters.CommentAdapter;
import com.example.b9000application.Models.ViewModel.UserViewModel;

public class CommentItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private CommentItemTouchHelperListener listener;
    private String userId;
    private CommentAdapter commentAdapter;

    @Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof CommentAdapter.MyViewHolder)
        {
            if(((CommentAdapter.MyViewHolder) viewHolder).getUid().equals(userId))
            {
                return super.getSwipeDirs(recyclerView,viewHolder);
            }
        }
        return 0;
    }

    public CommentItemTouchHelper(int dragDirs, int swipeDirs,String userId , CommentItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
        this.userId = userId;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if(listener != null)
        {
                listener.onSwiped(viewHolder,direction,viewHolder.getAdapterPosition());


        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        View foregroundView = ((CommentAdapter.MyViewHolder)viewHolder).viewForeground;
        getDefaultUIUtil().clearView(foregroundView);

    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
     if(viewHolder !=null)
     {
         View foregroundView = ((CommentAdapter.MyViewHolder)viewHolder).viewForeground;
         getDefaultUIUtil().onSelected(foregroundView);

     }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((CommentAdapter.MyViewHolder)viewHolder).viewForeground;
        getDefaultUIUtil().onDraw(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }


    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((CommentAdapter.MyViewHolder)viewHolder).viewForeground;
        getDefaultUIUtil().onDrawOver(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive);    }

    public interface CommentItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);


    }
}
