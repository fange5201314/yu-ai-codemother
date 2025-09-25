<template>
  <div class="home-page">
    <!-- 顶部标题区域 -->
    <div class="header-section">
      <h1 class="title">AI 应用生成平台</h1>
      <p class="subtitle">不写一行代码，生成完整应用</p>
    </div>

    <!-- 应用创建输入框 -->
    <div class="create-section">
      <a-input-search
        v-model:value="initPrompt"
        placeholder="请输入您的应用需求，例如：做一个旅游网站"
        enter-button="创建应用"
        size="large"
        @search="handleCreateApp"
      />
    </div>

    <!-- 我的应用分页列表 -->
    <div class="section">
      <h2>我的应用</h2>
      <a-table
        :columns="myAppColumns"
        :data-source="myAppData"
        :pagination="{
          ...myAppPagination.value,
          showSizeChanger: true,
          showTotal: (total: number) => `共 ${total} 条`,
        }"
        :loading="myAppLoading"
        @change="handleMyAppTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-button type="link" @click="goToAppDetail(record)">查看</a-button>
            <a-button type="link" @click="editApp(record)">编辑</a-button>
            <a-button type="link" danger @click="deleteApp(record)">删除</a-button>
          </template>
        </template>
      </a-table>
    </div>

    <!-- 精选应用分页列表 -->
    <div class="section">
      <h2>精选应用</h2>
      <a-table
        :columns="goodAppColumns"
        :data-source="goodAppData"
        :pagination="{
          ...goodAppPagination.value,
          showSizeChanger: true,
          showTotal: (total: number) => `共 ${total} 条`,
        }"
        :loading="goodAppLoading"
        @change="handleGoodAppTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-button type="link" @click="goToAppDetail(record)">查看</a-button>
          </template>
        </template>
      </a-table>
    </div>
  </div>

  <AppDetailModal
    v-model:open="detailModalVisible"
    :app="currentApp"
    :show-actions="false"
  />
</template>

<script setup lang="ts">
import { ref, onMounted, h } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import type { PaginationProps } from 'ant-design-vue'
import API from '@/api'
import AppDetailModal from '@/components/AppDetailModal.vue'

// 路由
const router = useRouter()

// 应用创建提示词
const initPrompt = ref('')

// 应用详情模态框相关
const detailModalVisible = ref(false)
const currentApp = ref<API.AppVO>()

// 我的应用表格相关
const myAppData = ref<API.AppVO[]>([])
const myAppLoading = ref(false)
const myAppPagination = ref({
  current: 1,
  pageSize: 20,
  total: 0,
  showSizeChanger: true,
})

// 精选应用表格相关
const goodAppData = ref<API.AppVO[]>([])
const goodAppLoading = ref(false)
const goodAppPagination = ref({
  current: 1,
  pageSize: 20,
  total: 0,
  showSizeChanger: true,
})

// 我的应用表格列定义
const myAppColumns = [
  {
    title: '封面',
    key: 'cover',
    customRender: ({ record }: { record: API.AppVO }) => {
      return record.cover ? (
        h('img', {
          src: record.cover,
          alt: '应用封面',
          style: { width: '50px', height: '50px', objectFit: 'cover' }
        })
      ) : (
        h('div', {
          style: {
            width: '50px',
            height: '50px',
            backgroundColor: '#f0f0f0',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center'
          }
        }, '无封面')
      );
    },
  },
  {
    title: '应用名称',
    dataIndex: 'appName',
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

// 精选应用表格列定义
const goodAppColumns = [
  {
    title: '封面',
    key: 'cover',
    customRender: ({ record }: { record: API.AppVO }) => {
      return record.cover ? (
        h('img', {
          src: record.cover,
          alt: '应用封面',
          style: { width: '50px', height: '50px', objectFit: 'cover' }
        })
      ) : (
        h('div', {
          style: {
            width: '50px',
            height: '50px',
            backgroundColor: '#f0f0f0',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center'
          }
        }, '无封面')
      );
    },
  },
  {
    title: '应用名称',
    dataIndex: 'appName',
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

/**
 * 处理创建应用
 */
const handleCreateApp = async () => {
  if (!initPrompt.value) {
    message.warning('请输入应用需求')
    return
  }

  try {
    const res = await API.appController.addApp({
      initPrompt: initPrompt.value,
    })

    if (res.data.code === 0 && res.data.data) {
      message.success('应用创建成功')
      // 跳转到对话页面，确保使用字符串类型的appId
      router.push(`/app/${res.data.data.toString()}/chat`)
    } else {
      message.error('应用创建失败: ' + res.data.message)
    }
  } catch (error) {
    message.error('应用创建失败')
  }
}

/**
 * 获取我的应用列表
 */
const fetchMyAppList = async () => {
  myAppLoading.value = true
  try {
    // 构造只包含必要字段的请求参数
    const queryParams = {
      pageNum: myAppPagination.value.current,
      pageSize: myAppPagination.value.pageSize,
    }
    const res = await API.appController.listMyAppVoByPage(queryParams)

    if (res.data.code === 0 && res.data.data) {
      myAppData.value = res.data.data.records || []
      myAppPagination.value.total = res.data.data.totalRow || 0
    } else {
      message.error('获取我的应用列表失败: ' + (res.data.message || '未知错误'))
    }
  } catch (error: any) {
    console.error('获取我的应用列表失败:', error)
    message.error('获取我的应用列表失败: ' + (error.message || '网络错误'))
  } finally {
    myAppLoading.value = false
  }
}

/**
 * 获取精选应用列表
 */
const fetchGoodAppList = async () => {
  goodAppLoading.value = true
  try {
    // 构造只包含必要字段的请求参数
    const queryParams = {
      pageNum: goodAppPagination.value.current,
      pageSize: goodAppPagination.value.pageSize,
    }
    const res = await API.appController.listGoodAppVoByPage(queryParams)

    if (res.data.code === 0 && res.data.data) {
      goodAppData.value = res.data.data.records || []
      goodAppPagination.value.total = res.data.data.totalRow || 0
    } else {
      message.error('获取精选应用列表失败: ' + (res.data.message || '未知错误'))
    }
  } catch (error: any) {
    console.error('获取精选应用列表失败:', error)
    message.error('获取精选应用列表失败: ' + (error.message || '网络错误'))
  } finally {
    goodAppLoading.value = false
  }
}

/**
 * 处理我的应用表格分页变化
 */
const handleMyAppTableChange: PaginationProps['onChange'] = (page, pageSize) => {
  myAppPagination.value.current = Math.max(1, Number(page))
  myAppPagination.value.pageSize = Math.max(1, Number(pageSize)) || 20
  fetchMyAppList()
}

/**
 * 处理精选应用表格分页变化
 */
const handleGoodAppTableChange: PaginationProps['onChange'] = (page, pageSize) => {
  goodAppPagination.value.current = Math.max(1, Number(page))
  goodAppPagination.value.pageSize = Math.max(1, Number(pageSize)) || 20
  fetchGoodAppList()
}

/**
 * 查看应用详情
 */
const goToAppDetail = (record: API.AppVO) => {
  currentApp.value = record
  detailModalVisible.value = true
}

/**
 * 编辑应用
 */
const editApp = (record: API.AppVO) => {
  router.push(`/app/${record.id.toString()}/edit`)
}

/**
 * 删除应用
 */
const deleteApp = async (record: API.AppVO) => {
  try {
    const res = await API.appController.deleteApp({
      id: record.id, // 保持原始ID类型，由API层处理
    })

    if (res.data.code === 0) {
      message.success('应用删除成功')
      // 重新加载我的应用列表
      fetchMyAppList()
    } else {
      message.error('应用删除失败: ' + res.data.message)
    }
  } catch (error) {
    message.error('应用删除失败')
  }
}

// 页面加载时获取数据
onMounted(() => {
  fetchMyAppList()
  fetchGoodAppList()
})
</script>

<style scoped>
.home-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.header-section {
  text-align: center;
  margin-bottom: 32px;
}

.title {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 8px;
}

.subtitle {
  font-size: 16px;
  color: #666;
}

.create-section {
  margin-bottom: 32px;
}

.section {
  margin-bottom: 32px;
}

.section h2 {
  margin-bottom: 16px;
}
  <AppDetailModal
    v-model:open="detailModalVisible"
    :app="currentApp"
    :show-actions="false"
  />
</template>

<style scoped>