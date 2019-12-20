12.19
Git内容复习
git init
git add .
git status
git commit -m '备注'
git branch 分支
git checkout 分支
git checkout master
git merge 分支名称        与master合并
推送到git
git remote add origin 链接
git push -u origin master

12.20
1.index.jsp通过重定向跳转到login.jsp页面
2.验证码的显示：
servlet流程：用户有一个行为，把行为传递到服务器上，服务器对其响应
用I/O流输出一张图片，在图片上随机出现几个数字，放在session中，然后与用户输入的数字进行比对

第四讲数据库连接信息封装
创建基本操作的类，基本查询，关闭数据库，释放资源。

第五讲实现登录功能
shift+ctrl+o：自动导包
int type 分角色登录的实现
数据库不用时应及时断开

第六讲登录拦截功能
拦截对象，系统，管理员，学生，老师，年级
未登录则跳转到登录页面