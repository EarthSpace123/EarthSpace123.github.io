# 项目安全上传指南

## 敏感信息检查清单

### ✅ 已处理的安全问题
1. **数据库配置** - 已创建示例文件
   - 原文件：`application.properties`（包含真实密码）
   - 示例文件：`application.properties.example`（使用占位符）
   - .gitignore已配置：忽略真实的`application.properties`

2. **代码检查** - 未发现硬编码敏感信息
   - ✅ 无硬编码密码
   - ✅ 无API密钥
   - ✅ 无访问令牌
   - ✅ 无个人信息

### 📋 上传前检查清单

#### 文件检查
- [ ] 确认`.gitignore`文件包含`application.properties`
- [ ] 确认`application.properties.example`文件已创建
- [ ] 检查是否有其他配置文件包含敏感信息
- [ ] 确认没有临时文件或日志文件

#### 代码检查
- [ ] 检查代码中是否有硬编码的密码、密钥或令牌
- [ ] 检查是否有调试信息包含敏感数据
- [ ] 确认没有包含个人信息的注释

#### 仓库设置
- [ ] 选择合适的仓库可见性（Public/Private）
- [ ] 添加适当的仓库描述
- [ ] 配置仓库许可证（如果需要）

### 🔒 安全最佳实践

#### 1. 配置文件管理
```properties
# ✅ 正确做法：使用示例文件
spring.datasource.username=your_username
spring.datasource.password=your_password

# ❌ 错误做法：硬编码敏感信息
spring.datasource.username=root
spring.datasource.password=123456
```

#### 2. 环境变量（推荐）
对于生产环境，建议使用环境变量：
```java
@Value("${spring.datasource.username}")
private String username;

@Value("${spring.datasource.password}")
private String password;
```

#### 3. 密钥管理
- 不要将API密钥、数据库密码等提交到Git
- 使用环境变量或配置管理工具
- 定期轮换敏感凭据

### 📝 上传步骤

#### 第一步：准备本地仓库
```bash
# 1. 确认.gitignore配置正确
cat .gitignore

# 2. 检查将被忽略的文件
git status
```

#### 第二步：验证安全配置
```bash
# 1. 确认敏感文件被忽略
git check-ignore application.properties

# 2. 查看将要提交的文件
git add --dry-run .
```

#### 第三步：创建GitHub仓库
1. 访问 https://github.com
2. 创建新仓库
3. 根据需要选择Public或Private

#### 第四步：上传项目
```bash
# 1. 添加文件到暂存区
git add .

# 2. 查看将要提交的文件
git status

# 3. 提交更改
git commit -m "初始提交：图书商城管理系统"

# 4. 添加远程仓库
git remote add origin https://github.com/your-username/your-repo.git

# 5. 推送到GitHub
git push -u origin main
```

### 🚨 如果发现敏感信息已提交

#### 1. 从历史记录中删除敏感文件
```bash
# 删除文件并从历史记录中移除
git filter-branch --force --index-filter \
  "git rm --cached --ignore-unmatch application.properties" \
  --prune-empty --tag-name-filter cat -- --all

# 强制推送
git push origin --force --all
```

#### 2. 更新.gitignore
```bash
# 确保敏感文件被忽略
echo "application.properties" >> .gitignore
git add .gitignore
git commit -m "更新.gitignore"
git push
```

#### 3. 轮换凭据
如果敏感信息已经泄露：
1. 立即更改数据库密码
2. 更新API密钥
3. 通知相关人员

### 📞 获取帮助

如果遇到安全问题：
- GitHub安全文档：https://docs.github.com/en/security
- Git安全最佳实践：https://git-scm.com/book/en/v2/Git-Tools-Credential-Storage

---

**重要提示**：在上传到GitHub之前，请务必仔细检查所有文件，确保不会泄露任何敏感信息。