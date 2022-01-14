package com.example.rotondeando.adapter;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder != null){
            switch (viewHolder.getAdapterPosition()){
                case 0:
                case 1:
                case 2:
                    break;
                default:
                    View foregroundView = ((RoutesAdapter.ListRoutesViewHolder) viewHolder).layoutDelete;
                    getDefaultUIUtil().onSelected(foregroundView);
                    break;
            }
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        switch (viewHolder.getAdapterPosition()){
            case 0:
            case 1:
            case 2:
                break;
            default:
                View foregroundView = ((RoutesAdapter.ListRoutesViewHolder) viewHolder).layoutDelete;
                getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
                break;
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        switch (viewHolder.getAdapterPosition()){
            case 0:
            case 1:
            case 2:
                break;
            default:
                View foregroundView = ((RoutesAdapter.ListRoutesViewHolder) viewHolder).layoutDelete;
                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
                break;
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        switch (viewHolder.getAdapterPosition()){
            case 0:
            case 1:
            case 2:
                break;
            default:
                View foregroundView = ((RoutesAdapter.ListRoutesViewHolder) viewHolder).layoutDelete;
                getDefaultUIUtil().clearView(foregroundView);
                break;
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        switch (viewHolder.getAdapterPosition()){
            case 0:
            case 1:
            case 2:
                break;
            default:
                listener.onSwipe(viewHolder, direction, viewHolder.getAdapterPosition());
                break;
        }
    }

    public interface RecyclerItemTouchHelperListener{
        void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
