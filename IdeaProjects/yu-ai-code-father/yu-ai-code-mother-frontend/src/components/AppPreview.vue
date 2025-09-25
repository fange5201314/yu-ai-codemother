<template>
  <div class="app-preview">
    <div class="preview-header">
      <h3>生成后的网页展示</h3>
      <div class="preview-actions">
        <a-button v-if="previewUrl && previewReady" type="link" @click="openInNewTab">
          <template #icon>
            <ExportOutlined />
          </template>
          新窗口打开
        </a-button>
        <a-button v-if="previewUrl && previewReady" type="link" @click="refreshPreview">
          <template #icon>
            <RedoOutlined />
          </template>
          刷新
        </a-button>
      </div>
    </div>
    <div class="preview-content">
      <div v-if="!previewUrl && !loading" class="preview-placeholder">
        <div class="placeholder-icon">🌐</div>
        <p>网站文件生成完成后将在这里展示</p>
      </div>
      <div v-else-if="loading" class="preview-loading">
        <a-spin size="large" />
        <p>正在生成网站...</p>
      </div>
      <div v-else-if="!previewReady && !loading" class="preview-loading">
        <a-spin size="large" />
        <p>正在加载预览...</p>
      </div>
      <iframe
        v-show="previewReady"
        :src="previewUrl"
        class="preview-iframe"
        frameborder="0"
        @load="onIframeLoad"
        @error="onIframeError"
      ></iframe>
      <div v-if="previewError" class="preview-error">
        <div class="error-icon">⚠️</div>
        <p>预览加载失败，请稍后重试</p>
        <a-button type="primary" @click="retryLoad">重新加载</a-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { ExportOutlined, RedoOutlined } from '@ant-design/icons-vue'

const props = defineProps<{
  previewUrl: string
  loading?: boolean
}>()

const previewReady = ref(false)
const previewError = ref(false)

// 在新窗口打开预览
const openInNewTab = () => {
  if (props.previewUrl) {
    window.open(props.previewUrl, '_blank')
  }
}

// iframe加载完成
const onIframeLoad = () => {
  previewReady.value = true
  previewError.value = false
}

// iframe加载错误
const onIframeError = () => {
  previewReady.value = false
  previewError.value = true
}

// 刷新预览
const refreshPreview = () => {
  previewReady.value = false
  previewError.value = false
  // 通过重新设置src来强制刷新iframe
  const currentUrl = props.previewUrl
  if (currentUrl) {
    // 移除现有时间戳参数并添加新的时间戳
    const urlWithoutTimestamp = currentUrl.replace(/([&?])_t=[^&]*/g, '$1').replace(/[?&]$/, '')
    // 添加时间戳强制刷新
    let timestampedUrl = urlWithoutTimestamp
    if (urlWithoutTimestamp.includes('?')) {
      timestampedUrl = `${urlWithoutTimestamp}&_t=${Date.now()}`
    } else {
      timestampedUrl = `${urlWithoutTimestamp}?_t=${Date.now()}`
    }

    // 先清空再重新设置URL以触发重新加载
    emit('update:previewUrl', '')
    setTimeout(() => {
      emit('update:previewUrl', timestampedUrl)
    }, 10)
  }
}

// 重新加载预览
const retryLoad = () => {
  previewError.value = false
  previewReady.value = false
  // 通过重新设置URL来触发重新加载
  emit('update:previewUrl', props.previewUrl)
}

// 监听previewUrl变化，重置状态
watch(() => props.previewUrl, (newUrl) => {
  if (newUrl) {
    previewReady.value = false
    previewError.value = false
  }
})

// 定义事件发射器
const emit = defineEmits<{
  (e: 'update:previewUrl', url: string): void
}>()
</script>

<style scoped>
.app-preview {
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  height: 100%;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #e8e8e8;
}

.preview-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.preview-actions {
  display: flex;
  gap: 8px;
}

.preview-content {
  flex: 1;
  position: relative;
  overflow: hidden;
}

.preview-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #666;
  text-align: center;
  padding: 20px;
}

.placeholder-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.hint-text {
  font-size: 14px;
  color: #999;
  margin-top: 8px;
}

.error-hint {
  font-size: 14px;
  color: #999;
  margin: 8px 0;
}

.preview-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #666;
}

.preview-loading p {
  margin-top: 16px;
}

.preview-error {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #666;
  padding: 20px;
  text-align: center;
}

.error-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.preview-error p {
  margin: 16px 0;
}

.preview-iframe {
  width: 100%;
  height: 100%;
  border: none;
}
</style>