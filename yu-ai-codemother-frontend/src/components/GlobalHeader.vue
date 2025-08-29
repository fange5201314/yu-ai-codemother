<template>
  <div class="global-header">
    <div class="header-content">
      <div class="header-left">
        <div class="logo">
          <img src="/src/assets/logo.svg" alt="Logo" class="logo-img" />
          <span class="site-title">编程导航</span>
        </div>
        <a-menu
          v-model:selectedKeys="selectedKeys"
          mode="horizontal"
          class="header-menu"
          :items="menuItems"
          @click="handleMenuClick"
        />
      </div>
      <div class="header-right">
        <a-button type="primary">登录</a-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import type { MenuProps } from 'ant-design-vue'

const router = useRouter()
const selectedKeys = ref<string[]>([router.currentRoute.value.path])

const menuItems: MenuProps['items'] = [
  {
    key: '/',
    label: '首页'
  },
  {
    key: '/about',
    label: '关于'
  }
]

const handleMenuClick: MenuProps['onClick'] = (e) => {
  const key = e.key as string
  selectedKeys.value = [key]
  if (key.startsWith('/')) {
    router.push(key)
  }
}

// 监听路由变化，更新当前选中菜单
router.afterEach((to) => {
  selectedKeys.value = [to.path]
})
</script>

<style scoped>
.global-header {
  height: 64px;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.header-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.logo {
  display: flex;
  align-items: center;
  margin-right: 40px;
  cursor: pointer;
}

.logo-img {
  height: 32px;
  width: 32px;
  margin-right: 12px;
}

.site-title {
  font-size: 18px;
  font-weight: 600;
  color: #1890ff;
}

.header-menu {
  border-bottom: none;
  line-height: 64px;
  flex: 1;
}

.header-right {
  display: flex;
  align-items: center;
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 16px;
  }
  
  .logo {
    margin-right: 20px;
  }
  
  .site-title {
    display: none;
  }
  
  .header-menu {
    display: none;
  }
}

@media (max-width: 480px) {
  .logo-img {
    height: 28px;
    width: 28px;
    margin-right: 8px;
  }
}
</style>