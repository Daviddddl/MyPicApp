# MyPicApp 书法修复大师APP
## 登陆/注册模块
- 用户注册或登录，用户名与密码采用SHA-1在后端数据库中进行加密存储。
- LoginActivity
- RegisterActivity
- SessionManager
- SQLiteManager
- SQLiteOpenHelper
- layout/activity_login.xml

## 用户管理模块
- 管理用户的账户信息，包括用户名、密码、权限、登陆状态、推出登录等操作。
- UserDetailActivity
- IS_ROOT
- SessionManager
- layout/activity_user_detail.xml

## 四大功能模块
### 残损修复
- 从相册或相机拍摄获取图片，上传图片，选择风格模型和预测缺损对象（若有更好选项可在横线上填写），点击获取，得到修补以后的结果图片。
- UploadActivity
- UploadFragment
- UploadTestActivity
- layout/activity_upload_test.xml
- layout/fragment_upload.xml


### 笔迹鉴定
- 从相册或相机拍摄分别获取不同字迹的图片，上传图片，点击评价，等待一段时间后，将得到相似度评判得分。
- IdentifyActivity
- layout/fragment_identify.xml
- layout/activity_identify.xml


### 风格模仿
- 在文本框中输入需要生成的文本对象，选择风格模型，点击生成，等待一段时间后，将获得风格模仿的结果图片.
- ImitateActivity
- layout/activity_imitate.xml

### 手写识别
- 从相册或相机拍摄分别获取不同字迹的图片，上传图片，选择单字识别将或得单个文字的，TOP5识别结果，点击文本识别将获得文本段落的最佳识别结果。
- ManufactureActivity
- layout/fragment_manufacture.xml
- layout/activity_manufacture.xml

## 其他模块
### 帮助
- 对本项目功能进行详细介绍解释。
- HelpActivity
### 应用程序权限设置
- 对本程序的所需权限进行申请授权。
