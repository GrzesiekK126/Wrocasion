package app.wrocasion;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

/**
* Created by Grzegorz on 2015-12-11.
*/
class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.setTitle("Select Items");
        mode.setSubtitle("One item selected");
        return true;
    }

    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return true;
    }

    public void onDestroyActionMode(ActionMode mode) {
    }

    public void onItemCheckedStateChanged(ActionMode mode, int position, long id,
                                          boolean checked) {
        int selectCount = GridViewActivity.mGrid.getCheckedItemCount();
        switch (selectCount) {
            case 1:
                //Toast.makeText(getApplicationContext(), "One item selected", Toast.LENGTH_SHORT).show();
                break;
            default:
                //Toast.makeText(getApplicationContext(), selectCount + " items selected", Toast.LENGTH_SHORT).show();
                break;
        }

    }

}
