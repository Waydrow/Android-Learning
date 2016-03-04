## 理解Activity的生命周期
可参照下面的链接，我的博客中的内容
[Activity 生命周期详解](http://blog.waydrow.com/2016/02/13/activity-lifecycle/)

```
public class Activity extends ApplicationContext {
   protected void onCreate(Bundle savedInstanceState);
   
   protected void onStart();   
   
   protected void onRestart();
   
   protected void onResume();
   
   protected void onPause();
   
   protected void onStop();
   
   protected void onDestroy();
}
```