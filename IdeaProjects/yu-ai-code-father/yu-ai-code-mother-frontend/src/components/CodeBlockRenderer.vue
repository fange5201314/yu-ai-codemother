<template>
  <div class="code-block-renderer">
    <pre class="code-content">{{ content }}</pre>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  content: string
}>()
</script>

<style scoped>
.code-block-renderer {
  width: 100%;
  overflow-x: auto;
}

.code-content {
  margin: 0;
  padding: 12px;
  background-color: #f5f5f5;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.4;
  white-space: pre-wrap;
  word-break: break-word;
  color: #333;
  border: 1px solid #e8e8e8;
}

/* 滚动条样式 */
.code-block-renderer::-webkit-scrollbar {
  height: 8px;
}

.code-block-renderer::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.code-block-renderer::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.code-block-renderer::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>