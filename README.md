## RefreshLayout
基于嵌套滚动实现的支持下刷新和上拉加载更多的刷新控件

## Why
就想要个简单的刷新控件，用嵌套滚动这套机制去做最简单.实现起来逻辑很清晰


## Display
<img src="static/pure.gif"/>
<img src="static/ll.gif"/>
<img src="static/ele.gif"/>


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

        <com.refreh.SafeNestedScrollView
            android:id="@+id/wrapper"
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <android.support.v7.widget.AppCompatTextView
                android:id="@+id/textview"
                android:layout_margin="16dp"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="@string/large_text" />
        </com.refreh.SafeNestedScrollView>


    </com.refresh.RefreshLayout>
```
