<template>
  <a-modal
    v-model:open="modalVisible"
    title="部署成功"
    width="500px"
    :footer="null"
    @cancel="handleCancel"
  >
    <div class="deploy-success-modal">
      <div class="success-icon">
        <CheckCircleOutlined />
      </div>
      <h3>应用部署成功！</h3>
      <p class="success-message">您的应用已成功部署到以下地址：</p>

      <div class="deploy-url">
        <a-input
          :value="deployUrl"
          readonly
          @click="copyUrl"
        >
          <template #suffix>
            <a-tooltip title="复制链接">
              <CopyOutlined @click="copyUrl" />
            </a-tooltip>
          </template>
        </a-input>
      </div>

      <div class="modal-actions">
        <a-button @click="handleCancel">
          稍后查看
        </a-button>
        <a-button type="primary" @click="openDeployedSite">
          立即访问
        </a-button>
      </div>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { CheckCircleOutlined, CopyOutlined } from '@ant-design/icons-vue'

const props = defineProps<{
  open: boolean
  deployUrl: string
}>()

const emit = defineEmits<{
  (e: 'update:open', value: boolean): void
  (e: 'open-site'): void
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

// 处理取消
const handleCancel = () => {
  modalVisible.value = false
}

// 复制URL
const copyUrl = () => {
  if (props.deployUrl) {
    navigator.clipboard.writeText(props.deployUrl).then(() => {
      message.success('链接已复制到剪贴板')
    }).catch(() => {
      message.error('复制失败')
    })
  }
}

// 打开部署的网站
const openDeployedSite = () => {
  emit('open-site')
  modalVisible.value = false
}
</script>

<style scoped>
.deploy-success-modal {
  text-align: center;
  padding: 24px 0;
}

.success-icon {
  font-size: 48px;
  color: #52c41a;
  margin-bottom: 16px;
}

.success-icon :deep(.anticon) {
  font-size: 48px;
}

h3 {
  margin: 0 0 8px 0;
  font-size: 20px;
  font-weight: 600;
  color: #1a1a1a;
}

.success-message {
  margin: 0 0 24px 0;
  color: #666;
  font-size: 14px;
}

.deploy-url {
  margin-bottom: 32px;
}

.deploy-url :deep(.ant-input) {
  cursor: pointer;
}

.deploy-url :deep(.ant-input:hover) {
  border-color: #1890ff;
}

.modal-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
}
</style>