## RefreshLayout
An easy pull-to-refresh and load-more widget on the basis of  NestedScroll


## Why
It is a long story.Sometimes i need ptr in my project to refresh and load more,but i cannot find any appropriate
ptr widget.SwipeRefreshLayout is ok ,but not support loadMore(when pulling up the item list).I look through
github,still disappointed.

Some pull-to-refresh project is very hot(including ""),but i think 
they are out of date after "NestedScroll" was published in android-support 22.1.0.
These project is kind of a replacement  limited to android "Touch Event" before 
"NestedScroll" was published.These project manually send the touch event to achieve the ptr.
So i gave up on them all,because they are not elegant.

And there is other solution ,adding extra item as ptr in RecyclerView,but i think it is
not real ptr with intruding RecyclerView as well as manually send the touch event

I have to say NestedScroll has been published for a long time.But we often use nested scroll to do
"Pinned Header" or some other UI,is it really made full use of?
Nested scroll is the best way to implement our ptr.I write this ptr quiet easy.Hope this project can jet your brain,and you can achieve
your better nicer ptr.


## Usage
If your target view is subclass of NestedScrollingChild,your layout file is like this
```xml
        <com.refresh.RefreshLayout
            android:id="@+id/refreshlayout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:targetId="@+id/recyclerView">
    
            <android.support.v7.widget.RecyclerView
                android:id="@+id/recyclerView"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:background="@android:color/darker_gray" />
    
        </com.refresh.RefreshLayout>
```
or not ,you can wrap  view in NestedScrollView

```xml
    
    <com.refresh.RefreshLayout
        android:id="@+id/refreshlayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:targetId="@+id/wrapper">

        <android.support.v4.widget.NestedScrollView
            android:id="@+id/wrapper"
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <android.support.v7.widget.AppCompatTextView
                android:id="@+id/textview"
                android:layout_margin="16dp"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="@string/large_text" />
        </android.support.v4.widget.NestedScrollView>


    </com.refresh.RefreshLayout>
```

