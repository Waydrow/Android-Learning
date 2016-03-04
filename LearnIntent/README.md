## 学习安卓 Intent的相关知识

### 说明
在Activity的相互启动中研究Intent，添加的标签等均是在`AndroidManifest.xml`文件中的`<application>`内。

### 显式Intent
明确指出要启动的Activity，如下面的`MyAty.class`: 
```java
New Intent(MainActivity.this, MyAty.class)
```

### 隐式Intent
- 可以从A应用启动B应用，方法 :  通过添加`Action`标记来找到相应的Activity
- 禁止从外部应用访问 : 在`<activity></>` 中添加 `android: exported = "false"`

### Intent过滤器
在`<intent-filter>`中添加`<data android:scheme = "example" />`
启动时
```java
New Intent("action", Uri.parse("example://xxx"))
```

### 通过浏览器链接启动本地Activity
``` 
    <activity
        android:name=".MyAty"
        android:label="@string/app_name"
        android:exported="false">
        <intent-filter>
            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />
            <action android:name="com.waydrow.learnintent.myaty" />
            <action android:name="android.intent.action.VIEW" />
            <data android:scheme="app"/>
        </intent-filter>
    </activity>
```

通过安卓模拟器中的浏览器访问本机时: 
google模拟器: `IP: 10.0.2.2`
Genymotion: `IP: 10.0.3.2`
>模拟器中要打开wifi，默认是打开的