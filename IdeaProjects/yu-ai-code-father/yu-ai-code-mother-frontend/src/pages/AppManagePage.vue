<template>
  <div class="app-manage-page">
    <h1>应用管理</h1>

    <!-- 管理员操作提示 -->
    <a-alert
      message="您是管理员，可以管理所有应用"
      type="info"
      show-icon
      style="margin-bottom: 24px"
    />

    <!-- 搜索表单 -->
    <a-form layout="inline" :model="searchParams" @finish="doSearch" style="margin-bottom: 24px">
      <a-form-item label="应用名称">
        <a-input v-model:value="searchParams.appName" placeholder="输入应用名称" />
      </a-form-item>
      <a-form-item label="应用类型">
        <a-select v-model:value="searchParams.appType" style="width: 120px">
          <a-select-option value="">全部</a-select-option>
          <a-select-option value="html">HTML</a-select-option>
          <a-select-option value="multi_file">多文件</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>

    <!-- 应用表格 -->
    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      :loading="loading"
      @change="handleTableChange"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'appIcon'">
          <a-image :src="record.appIcon" :width="60" />
        </template>
        <template v-else-if="column.dataIndex === 'appType'">
          <a-tag v-if="record.appType === 'html'" color="blue">HTML</a-tag>
          <a-tag v-else color="green">多文件</a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'priority'">
          <a-tag v-if="record.priority >= 99" color="red">精选</a-tag>
          <span v-else>{{ record.priority }}</span>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-button type="link" @click="editApp(record)">编辑</a-button>
          <a-button type="link" @click="setFeatured(record)" v-if="record.priority < 99">精选</a-button>
          <a-button type="link" danger @click="deleteApp(record)">删除</a-button>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import API from '@/api'
import type { AppQueryRequest, AppVO, AppUpdateRequest } from '@/api/typings.d.ts'

// 表格列定义
const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
  },
  {
    title: '应用名称',
    dataIndex: 'appName',
  },
  {
    title: '应用图标',
    dataIndex: 'appIcon',
  },
  {
    title: '应用类型',
    dataIndex: 'appType',
  },
  {
    title: '优先级',
    dataIndex: 'priority',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '操作',
    key: 'action',
  },
]

// 表格数据
const data = ref<AppVO[]>([])
const loading = ref(false)
const total = ref(0)

// 搜索参数
const searchParams = ref<AppQueryRequest>({
  pageNum: 1,
  pageSize: 20,
})

// 分页配置
const pagination = ref({
  current: 1,
  pageSize: 20,
  total: 0,
  showSizeChanger: true,
  showTotal: (total: number) => `共 ${total} 条`,
})

/**
 * 获取应用列表
 */
const fetchAppList = async () => {
  loading.value = true
  try {
    const res = await API.appController.listAppVoByPageByAdmin({
      ...searchParams.value,
    })

    if (res.data.code === 0 && res.data.data) {
      data.value = res.data.data.records || []
      total.value = res.data.data.totalRow || 0
      pagination.value.total = total.value
    } else {
      message.error('获取应用列表失败: ' + res.data.message)
    }
  } catch (error) {
    message.error('获取应用列表失败')
  } finally {
    loading.value = false
  }
}

/**
 * 处理表格分页变化
 */
const handleTableChange = (page: any) => {
  searchParams.value.pageNum = Number(page.current)
  searchParams.value.pageSize = Number(page.pageSize)
  pagination.value.current = Number(page.current)
  pagination.value.pageSize = Number(page.pageSize)
  fetchAppList()
}

/**
 * 搜索
 */
const doSearch = () => {
  searchParams.value.pageNum = 1
  pagination.value.current = 1
  fetchAppList()
}

/**
 * 编辑应用
 * @param record 应用记录
 */
const editApp = (record: AppVO) => {
  // 跳转到应用信息修改页面，确保使用字符串类型的appId
  window.open(`/app/${record.id.toString()}/edit`, '_blank')
}

/**
 * 设置为精选应用
 * @param record 应用记录
 */
const setFeatured = async (record: AppVO) => {
  try {
    const updateRequest: AppUpdateRequest = {
      id: record.id, // 保持原始ID类型，由API层处理
      priority: 99,
    }

    const res = await API.appController.updateAppByAdmin(updateRequest)

    if (res.data.code === 0) {
      message.success('设置精选成功')
      // 重新加载数据
      fetchAppList()
    } else {
      message.error('设置精选失败: ' + res.data.message)
    }
  } catch (error) {
    message.error('设置精选失败')
  }
}

/**
 * 删除应用
 * @param record 应用记录
 */
const deleteApp = async (record: AppVO) => {
  try {
    const res = await API.appController.deleteAppByAdmin({
      id: record.id, // 保持原始ID类型，由API层处理
    })

    if (res.data.code === 0) {
      message.success('删除成功')
      // 重新加载数据
      fetchAppList()
    } else {
      message.error('删除失败: ' + res.data.message)
    }
  } catch (error) {
    message.error('删除失败')
  }
}

// 页面加载时获取数据
onMounted(() => {
  fetchAppList()
})
</script>

<style scoped>
.app-manage-page {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}
</style>