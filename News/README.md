## 一个简单的新闻客户端

### 界面预览
![](http://7xrmgx.com1.z0.glb.clouddn.com/2016-03-08_170448.png)    

点击相应新闻即可跳转到详细内容页:   
![](http://7xrmgx.com1.z0.glb.clouddn.com/2016-03-08_170509.png)

### 使用php生成新闻JSON数据
```php
<?php
/*
获得JSON数据
返回值: title desc time content_url pic_url
*/

//链接数据库
$con = new mysqli("localhost", "root", "1234", "newsdemo");
if(mysqli_connect_error()){
    echo mysqli_connect_error();
}
$con->set_charset("utf8");

$n = 0;
$query = "select * from news";
$result = $con->query($query);
if($result === false){//执行失败
    echo $con->error;
    echo $con->errno;
}
while($row = mysqli_fetch_array($result)){
    $arr[$n++] = array("title" => $row['title'],
                       "desc" => $row['desc'],
                       "time" => $row['time'],
                       "content_url" => $row['content_url'],
                       "pic_url" => $row['pic_url']
                       );
}

//数组转换为JSON字符串
echo json_encode($arr);

 ?>
```

如下图: 
![](http://7xrmgx.com1.z0.glb.clouddn.com/2016-03-08_170839.png)  

### 实现新闻列表的布局

#### 在XML中添加`ListView`
#### 使用`BaseAdapter`作为适配器

### 实现新闻列表界面的 JSON 数据解析和填充

```java
public class NewsAdapter extends BaseAdapter {

    private Context context;
    private List<News> newsList;

    public NewsAdapter(Context context, List<News> newsList){
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item, null);
        }
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        ImageView ivPic = (ImageView) convertView.findViewById(R.id.ivPic);

        News news = newsList.get(position);
        tvTitle.setText(news.getTitle());
        tvTime.setText(news.getTime());
        tvDesc.setText(news.getDesc());

        String pic_url = news.getPic_url();
        HttpUtils.setPicBitmap(ivPic, pic_url);

        return convertView;
    }
}
```

### 实现新闻列表界面的跳转并展示详情

```java
 @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news = newsList.get(position);
        Intent intent = new Intent(this,BrowseNewsActivity.class);
        intent.putExtra("content_url", news.getContent_url());
        startActivity(intent);
    }
```
