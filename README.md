# TVListing
Simple REST API loader/parser app

<br />
TASKS : 
<br />
 Data are loaded asynchronously and are written in a local SQL database and are shown to a user from there.  DONE
 Firstly, you should download program schedule for today, then for a week/month.    DONE (partly)
* Optionally. Manual database synchronization.  DONE
* Optionally. Automatically synchronize data for today several times a day.  NOT
* Optionally Show a progress of download/synchronization.  NOT
* Optionally Show Notification while loading.  NOT
* Optionally Use ContentProvider for work with a database.  DONE

  On the main screen of the application is located TabLayout with channel titles and ViewPager with programs for these channels. The program is displayed for today by default. DONE
* Optionally ability to select a date for which a TV schedule is shown. DONE
* Optionally select channels which are preferred on TV schedule page. DONE
* Optionally sort the list of channels on the main screen. DONE (not for schedule yet)

Menu includes the following items:  DONE
list of categories;  
list of channels;
list of preferred channels;
program schedule. (Main screen)
* Optionally use NavigationDrawer  DONE

The list of categories is shown on the screen with categories. When user click on some category, the list of channels of this category is displayed. When user sees the list of channels he can click on the item and by this action add or remove single item to the list of preferred channels. DONE
For a list of all the channels and preferred ones, actions are the same.  DONE
