<template>
  <a-modal
    v-model:open="modalVisible"
    title="应用详情"
    width="600px"
    :footer="null"
    @cancel="handleCancel"
  >
    <div class="app-detail-modal">
      <a-descriptions bordered :column="1" size="small">
        <a-descriptions-item label="应用名称">
          {{ app?.appName || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="应用ID">
          {{ app?.id || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="创建时间">
          {{ formatDateTime(app?.createTime) || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="更新时间">
          {{ formatDateTime(app?.updateTime) || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="部署标识">
          {{ app?.deployKey || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="部署时间">
          {{ formatDateTime(app?.deployedTime) || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="初始提示词">
          <div class="prompt-content">
            {{ app?.initPrompt || '-' }}
          </div>
        </a-descriptions-item>
      </a-descriptions>

      <div v-if="showActions" class="modal-actions">
        <a-button type="primary" @click="handleEdit">
          <template #icon>
            <EditOutlined />
          </template>
          编辑
        </a-button>
        <a-button danger @click="handleDelete">
          <template #icon>
            <DeleteOutlined />
          </template>
          删除
        </a-button>
      </div>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { EditOutlined, DeleteOutlined } from '@ant-design/icons-vue'

const props = defineProps<{
  open: boolean
  app?: API.AppVO
  showActions?: boolean
}>()

const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
  (e: 'edit'): void
  (e: 'delete'): void
}>()

const modalVisible = ref(false)

// 监听open属性变化
watch(
  () => props.open,
  (newVal) => {
    modalVisible.value = newVal
  }
)

// 监听modalVisible变化
watch(modalVisible, (newVal) => {
  emit('update:open', newVal)
})

// 格式化日期时间
const formatDateTime = (dateString?: string) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString('zh-CN')
}

// 处理取消
const handleCancel = () => {
  modalVisible.value = false
}

// 处理编辑
const handleEdit = () => {
  emit('edit')
  modalVisible.value = false
}

// 处理删除
const handleDelete = () => {
  emit('delete')
  modalVisible.value = false
}
</script>

<style scoped>
.app-detail-modal {
  padding: 16px 0;
}

.prompt-content {
  white-space: pre-wrap;
  word-break: break-word;
  color: #666;
  font-size: 12px;
  line-height: 1.5;
}

.modal-actions {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

:deep(.ant-descriptions-item-label) {
  font-weight: 500;
}
</style>