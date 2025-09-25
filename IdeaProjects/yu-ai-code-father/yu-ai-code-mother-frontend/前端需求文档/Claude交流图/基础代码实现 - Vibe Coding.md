#### 3、用户分页查询自己的应用列表

下面验收主页，可以通过修改数据库，给自己创建的应用统一设置封面，比如随机占位图 https://picsum.photos/320/180。

效果如图，AI 直接完美生成了：

![img](https://typora-sun.oss-cn-beijing.aliyuncs.com/picgo/0AwjvNw1aAzQAOvj.webp)KyWvlZ38oMwFWkAbZ8msxfqA8UGZself9lL8peZCpzo=

#### 4、分页查询精选的应用列表

可以通过修改数据库，将一些应用的优先级修改为 99（表示精选）。

##### 常见问题 - 排序错误

一般来说，我们要按照时间降序对精选应用进行排序。这个问题可以直接自己修复，找到调用获取精选应用接口的位置，设置排序参数即可：

```javascript
▼javascript复制代码// 加载精选应用
const loadFeaturedApps = async () => {
  try {
    const res = await listGoodAppVoByPage({
      pageNum: featuredAppsPage.current,
      pageSize: featuredAppsPage.pageSize,
      sortField: 'createTime',
      sortOrder: 'desc',
    })

    if (res.data.code === 0 && res.data.data) {
      featuredApps.value = res.data.data.records || []
      featuredAppsPage.total = res.data.data.totalRow || 0
    }
  } catch (error) {
    console.error('加载精选应用失败：', error)
  }
}
```

效果如图：uLeAcUhGYSfc3lu9cX0I36yALgNZKxrGRip1t3AXEzI=

![img](https://typora-sun.oss-cn-beijing.aliyuncs.com/picgo/bX9QJ7QwArICX3IY.webp)

#### 5、查看应用详情

点击应用卡片后，可以查看应用详情。参考大厂平台、同样也是为了方便，应用详情页面可以复用应用对话页面。U9u85Cj+7mDDyA0QBhBzESiQaqdPG15s1RgEU4gjDls=

##### 常见问题 - 应用卡片补充查看作品按钮

由于之前生成时没有提供足够的原型图，AI 应该不会生成 “查看作品” 按钮。

![img](https://typora-sun.oss-cn-beijing.aliyuncs.com/picgo/U3nqF4D030iZbeLO.webp)mU1y2yfy8JDhs4k9g6KJ5vvrfR+3r/IyvgMVxT7IF2Q=

无论是我的作品还是精选页面，所有应用卡片展示按钮的逻辑一致。鼠标悬浮时，展示 2 个按钮：

- 查看对话：始终显示，点击后跳转到应用对话页面
- 查看作品：如果查看的应用有 deployKey，会展示查看作品按钮，点击后新页面打开部署地址。部署地址目前为 localhost/{deployKey}。注意，部署地址和生成网站的浏览地址不同，不要修改错了！