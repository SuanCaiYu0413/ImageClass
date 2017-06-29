# 图片认知分类系统设计与开发
## 来到南航我们吃什么

#### **- 系统设计细节**

* * *

###### - 图片上传及图片存储
    文件上传我们采用ajax分块上传
```
$('#form').diyUpload({
	url:'__PUBLIC__/Home/server/fileupload.php',
	success:function( data ) {
		console.info( data );
	},
	error:function( err ) {
		console.info( err )；
	},
	buttonText : '选择图片／图片压缩包',
	chunked:true,
	// 分片大小
	chunkSize:512 * 1024,
	//最大上传的文件数量, 总文件大小,单个文件大小(单位字节);
	fileNumLimit:1000,
	fileSizeLimit:1000000 * 1024,
	fileSingleSizeLimit:50000 * 1024,
	accept: {}
});
```
    图片的存储：我们采用取上传图片时间的时间戳求MD5哈希值取前五位作文文件夹名。
    求上传图片文件名MD5哈希值后五位作为文件名。并将图片的关键信息存入数据库。

###### - 预定义标签

    预定义标签我们采用的是优图图片内容识别技术
    通过调用已有Api对图片进行第一次打标签。
>   [腾讯优图](https://open.youtu.qq.com/)


###### - 词云(关注列表)

    我们利用逆向文件频率(idf)对所有图片中的标签求标签的普遍重要性。
    经过排序拿出重要性前100的标签生成词云给用户选择自己喜欢的标签作为关注。

###### - 图片推送策略

    通过用户在词云上选择的关注标签，我们在数据库中找出与标有该标签的所有图片，
    并在标签数据库中排除该用户已经打过标签的图片，进行推送。如果该用户未选择关注的标签，
    我们将随机推送图片。

###### - 图片回收机制(当图片标签达到预期停止推送)

    这里我们采用投票算法，威尔逊置信区间则是我们最佳的选择。
    根据公式，P为该标签在该图片所有标签中的占比(就是认同该标签的人数),
    z的统计量我们取1.96，N为给该图片打标签的总人数。在一个用户给图片打标签时，
    我们将计算出该图片所有标签的威尔逊置信区间最小值进行排序，当第5个标签的置信区间最小值为20时，
    我们认为该图片已经达到预期值，取出前五个标签作为该图片的最终标签。

![WilsonMin](http://119.29.194.163/WilsonMin.png)
```
//获取威尔逊置信区间最小值
function getWilsonMin($picId,$labelId){
	$n = $this->getN($picId);
	$p = $this->getP($picId,$labelId);
	$z = 1.96;
	$z2 = pow($z,2);

	$arg1 = (1/(2*$n))*$z2;
	$arg2_1 = ($p*(1-$p))/n;
	$arg2_2 = $z2/(4*pow($n,2));
	$arg2 = $z*sqrt($arg2_1 + $arg2_2);

	$arg_up = $p + $arg1 - $arg2;
	$arg_down = 1 + ((1/$n) * $z2);

	return $arg_up/$arg_down;
}
```

###### - 同义标签处理策略（==未完成==）

	通过哈工大的同义词词林，在数据库中做一个对比库，当用户在进行标签提交的时候，
    在词林中寻找该词是否存在同义词，如果存在，则将该词替换成标签库中已存在的标签词。

* * *
#### **- 图片标签获取接口**

    获取所有图片ID:http://114.115.220.20/tp/ImgGet/idList

```
//返回事例
[{picture_id: "64", name: "1ba9e.jpeg"}, {picture_id: "65", name: "1ba9e.jpg"},…]
```
    根据Id获取图片属性:http://114.115.220.20/tp/ImgGet/picGet?picture_id=90

```
//返回事例
{
	finish_time:"2017-06-25 12:51:33"
	labels:["建筑", "房屋", "天空", "树木", "湖", "山"]
	picture_name:"65531.jpg"
}
```

* * *

#### **- 源代码参考**

* * *
* [安卓源代码](http://42.123.127.93:10080/SuanCaiYu/Image_Classification/tree/master/Android/PictureClass)
* [Server端ThinkPHP代码](http://42.123.127.93:10080/SuanCaiYu/Image_Classification/tree/master/Server)
* [项目文档](http://42.123.127.93:10080/SuanCaiYu/Image_Classification/tree/master/Doc)
* [App下载](http://42.123.127.93:10080/SuanCaiYu/Image_Classification/tree/master/App)
* [视频演示1](http://www.bilibili.com/video/av11720485/) -（推荐）
* [视频演示2](http://119.29.194.163/video/)

* * *

#### **- 团队成员**

* * *
* 佘草原
* 何自豪
* 周丽

* * *
