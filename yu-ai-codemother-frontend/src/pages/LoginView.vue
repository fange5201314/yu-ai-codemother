<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h2>用户登录</h2>
        <p>欢迎使用鱼皮AI零代码应用生成平台</p>
      </div>

      <a-form
        :model="loginForm"
        :rules="rules"
        @finish="handleLogin"
        layout="vertical"
      >
        <a-form-item label="用户账号" name="userAccount">
          <a-input
            v-model:value="loginForm.userAccount"
            placeholder="请输入用户账号"
            size="large"
          />
        </a-form-item>

        <a-form-item label="密码" name="userPassword">
          <a-input-password
            v-model:value="loginForm.userPassword"
            placeholder="请输入密码"
            size="large"
          />
        </a-form-item>

        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            size="large"
            :loading="loading"
            block
          >
            登录
          </a-button>
        </a-form-item>
      </a-form>

      <div class="login-footer">
        <span>还没有账号？</span>
        <router-link to="/user/register">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import request from '@/request'

const router = useRouter()
const loading = ref(false)

const loginForm = reactive({
  userAccount: '',
  userPassword: ''
})

const rules = {
  userAccount: [
    { required: true, message: '请输入用户账号', trigger: 'blur' },
    { min: 4, message: '账号至少4个字符', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, message: '密码至少8个字符', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  loading.value = true
  try {
    const response = await request('/user/login', {
      method: 'POST',
      data: loginForm
    })

    if (response.code === 0) {
      message.success('登录成功!')
      // 保存用户信息到本地存储
      localStorage.setItem('userInfo', JSON.stringify(response.data))
      // 跳转到首页
      router.push('/')
    } else {
      message.error(response.message || '登录失败')
    }
  } catch (error) {
    console.error('登录失败:', error)
    message.error('登录失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.login-header h2 {
  color: #1890ff;
  margin-bottom: 8px;
}

.login-header p {
  color: #666;
  font-size: 14px;
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  color: #666;
}

.login-footer a {
  color: #1890ff;
  text-decoration: none;
  margin-left: 8px;
}

.login-footer a:hover {
  text-decoration: underline;
}
</style>