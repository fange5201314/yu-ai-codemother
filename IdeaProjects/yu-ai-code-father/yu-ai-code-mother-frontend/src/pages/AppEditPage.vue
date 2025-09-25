<template>
  <div class="app-edit-page">
    <h1>{{ isOwnApp ? '编辑应用信息' : '应用详情' }}</h1>

    <a-form
      :model="formState"
      :label-col="{ span: 4 }"
      :wrapper-col="{ span: 14 }"
      :rules="rules"
      @finish="handleSubmit"
    >
      <a-form-item label="应用ID" name="id">
        <a-input v-model:value="formState.id" disabled />
      </a-form-item>

      <a-form-item label="应用名称" name="appName">
        <a-input v-model:value="formState.appName" :disabled="!isEditable" />
      </a-form-item>

      <a-form-item label="应用图标" name="appIcon">
        <a-input v-model:value="formState.appIcon" :disabled="!isEditable" />
      </a-form-item>

      <a-form-item label="应用类型" name="appType">
        <a-select v-model:value="formState.appType" :disabled="!isEditable">
          <a-select-option value="html">HTML</a-select-option>
          <a-select-option value="multi_file">多文件</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="优先级" name="priority" v-if="loginUserStore.loginUser.userRole === 'admin'">
        <a-input-number v-model:value="formState.priority" :min="0" :disabled="!isEditable" />
      </a-form-item>

      <a-form-item label="创建时间" name="createTime">
        <a-input :value="dayjs(formState.createTime).format('YYYY-MM-DD HH:mm:ss')" disabled />
      </a-form-item>

      <a-form-item label="更新时间" name="updateTime">
        <a-input :value="dayjs(formState.updateTime).format('YYYY-MM-DD HH:mm:ss')" disabled />
      </a-form-item>

      <a-form-item :wrapper-col="{ span: 14, offset: 4 }">
        <a-button type="primary" html-type="submit" v-if="isEditable">保存</a-button>
        <a-button @click="handleCancel">返回</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import API from '@/api'
import { useLoginUserStore } from '@/stores/loginUser'
import type { AppVO, AppUpdateRequest } from '@/api/typings.d.ts'

// 路由
const route = useRoute()
const router = useRouter()

// 登录用户状态
const loginUserStore = useLoginUserStore()

// 应用ID
const appId = ref<string>(route.params.appId as string)

// 表单状态
const formState = ref<AppVO>({
  id: '',
  appName: '',
  appIcon: '',
  appType: 'html',
  priority: 0,
  createTime: '',
  updateTime: '',
})

// 表单规则
const rules = {
  appName: [{ required: true, message: '请输入应用名称' }],
  appIcon: [{ required: false }],
  appType: [{ required: true, message: '请选择应用类型' }],
  priority: [{ required: false }],
}

// 是否是自己的应用
const isOwnApp = computed(() => {
  return formState.value.userId === loginUserStore.loginUser.id
})

// 是否可编辑
const isEditable = computed(() => {
  // 管理员可以编辑所有应用
  if (loginUserStore.loginUser.userRole === 'admin') {
    return true
  }
  // 普通用户只能编辑自己的应用
  return isOwnApp.value
})

/**
 * 获取应用信息
 */
const fetchAppInfo = async () => {
  try {
    const res = await API.appController.getAppVoById({
      id: appId.value, // 使用字符串类型的appId，避免精度丢失
    })

    if (res.data.code === 0 && res.data.data) {
      formState.value = res.data.data
    } else {
      message.error('获取应用信息失败: ' + res.data.message)
    }
  } catch (error) {
    message.error('获取应用信息失败')
  }
}

/**
 * 提交表单
 * @param values 表单值
 */
const handleSubmit = async (values: any) => {
  try {
    let res: any

    // 管理员使用管理员接口
    if (loginUserStore.loginUser.userRole === 'admin') {
      const updateRequest: AppUpdateRequest = {
        ...formState.value,
      }
      res = await API.appController.updateAppByAdmin(updateRequest)
    } else {
      // 普通用户使用普通接口
      const updateRequest: AppUpdateRequest = {
        id: formState.value.id,
        appName: formState.value.appName,
      }
      res = await API.appController.updateApp(updateRequest)
    }

    if (res.data.code === 0) {
      message.success('保存成功')
      // 返回上一页
      router.back()
    } else {
      message.error('保存失败: ' + res.data.message)
    }
  } catch (error) {
    message.error('保存失败')
  }
}

/**
 * 取消编辑
 */
const handleCancel = () => {
  router.back()
}

// 页面加载时获取应用信息
onMounted(() => {
  fetchAppInfo()
})
</script>

<style scoped>
.app-edit-page {
  padding: 24px;
  max-width: 800px;
  margin: 0 auto;
}
</style>