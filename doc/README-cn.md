## RefreshLayout
基于嵌套滚动实现的支持下刷新和上拉加载更多的刷新控件

## Why
说来话长，很多时候下拉刷新和上拉加载更多都需要用到刷新控件，但是其实一直对刷新控件不太满意。虽然谷歌
官方提供了SwipeRefreshLayout,但是却不支持下拉加载更多，在github上找了一圈没找到合适的。

现在已有的比较热门的刷新控件，包括ultra-pull-to-refresh等等，感觉有一些“过时”了，因为谷歌在support 22.1.0
开始支持嵌套滚动，类似RecylerView已经实现了对应的接口，另外还有NestedScrollView。所以之前的那些刷新控件，此时
显得有一些“过时”，毕竟那些控件出来的时候受限于Android的事件机制，只能手动分发事件来实现现在用嵌套滚动可以轻而易举
实现的效果。
当然还有另外一种，针对RecyclerView来做的，就是添加额外的Item做为刷新头和尾，其实这样我觉得RecyclerView有些变味了，
多了一层封装，另外也是一样手动分发事件。
我想说的是，既然嵌套滚动已经推出这么久了，但是貌似没怎么看到利用这套东西去做什么，更多的是做 类似滚动固定头部之类的，
其实，这套机制是最最最适合做刷新控件的。自己写的这个逻辑很清楚，希望这个小项目能够jet your brain ,实现你自己的更好更漂亮的
刷新控件！

## Usage
使用很简单，如果控件已经实现了NestedScrollingChild的像这样
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
要是没有实现这个接口的，就包裹在NestedScrollView里就好了，记得给RefreshLayout指定targetId

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

