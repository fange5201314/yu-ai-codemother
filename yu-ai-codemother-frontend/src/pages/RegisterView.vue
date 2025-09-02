<template>
  <div class="register-container">
    <div class="register-box">
      <div class="register-header">
        <h2>用户注册</h2>
        <p>加入鱼皮AI零代码应用生成平台</p>
      </div>

      <a-form
        :model="registerForm"
        :rules="rules"
        @finish="handleRegister"
        layout="vertical"
      >
        <a-form-item label="用户账号" name="userAccount">
          <a-input
            v-model:value="registerForm.userAccount"
            placeholder="请输入用户账号，至少4个字符"
            size="large"
          />
        </a-form-item>

        <a-form-item label="密码" name="userPassword">
          <a-input-password
            v-model:value="registerForm.userPassword"
            placeholder="请输入密码，至少8个字符"
            size="large"
          />
        </a-form-item>

        <a-form-item label="确认密码" name="checkPassword">
          <a-input-password
            v-model:value="registerForm.checkPassword"
            placeholder="请再次输入密码"
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
            注册
          </a-button>
        </a-form-item>
      </a-form>

      <div class="register-footer">
        <span>已有账号？</span>
        <router-link to="/user/login">立即登录</router-link>
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

const registerForm = reactive({
  userAccount: '',
  userPassword: '',
  checkPassword: ''
})

const rules = {
  userAccount: [
    { required: true, message: '请输入用户账号', trigger: 'blur' },
    { min: 4, message: '账号至少4个字符', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, message: '密码至少8个字符', trigger: 'blur' }
  ],
  checkPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (_rule: any, value: string) => {
        if (value && value !== registerForm.userPassword) {
          return Promise.reject(new Error('两次输入的密码不一致'))
        }
        return Promise.resolve()
      },
      trigger: 'blur'
    }
  ]
}

const handleRegister = async () => {
  loading.value = true
  try {
    const response = await request('/user/register', {
      method: 'POST',
      data: registerForm
    })

    if (response.code === 0) {
      message.success('注册成功!')
      // 跳转到登录页面
      router.push('/user/login')
    } else {
      message.error(response.message || '注册失败')
    }
  } catch (error) {
    console.error('注册失败:', error)
    message.error('注册失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.register-header {
  text-align: center;
  margin-bottom: 32px;
}

.register-header h2 {
  color: #1890ff;
  margin-bottom: 8px;
}

.register-header p {
  color: #666;
  font-size: 14px;
}

.register-footer {
  text-align: center;
  margin-top: 24px;
  color: #666;
}

.register-footer a {
  color: #1890ff;
  text-decoration: none;
  margin-left: 8px;
}

.register-footer a:hover {
  text-decoration: underline;
}
</style>