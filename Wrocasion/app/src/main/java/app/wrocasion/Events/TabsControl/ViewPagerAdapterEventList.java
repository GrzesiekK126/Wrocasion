package app.wrocasion.Events.TabsControl;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.wrocasion.Events.TabsControl.Tabs.AllEventsTab;
import app.wrocasion.Events.TabsControl.Tabs.PhotosTab;
import app.wrocasion.Events.TabsControl.Tabs.UserEventsTab;

public class ViewPagerAdapterEventList extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapterEventList(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            UserEventsTab userEventsTab = new UserEventsTab();
            return userEventsTab;
        }
        else            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            AllEventsTab allEventsTab = new AllEventsTab();
            return allEventsTab;
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}

