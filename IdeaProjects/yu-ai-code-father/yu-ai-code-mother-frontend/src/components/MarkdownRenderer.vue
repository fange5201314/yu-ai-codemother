<template>
  <div class="markdown-content" v-html="renderedContent"></div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  content: string
}>()

// 简单的Markdown到HTML转换函数
// 在实际项目中，建议使用专门的Markdown解析库如markdown-it
const renderedContent = computed(() => {
  if (!props.content) return ''

  let html = props.content
    // 转换粗体
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    // 转换斜体
    .replace(/\*(.*?)\*/g, '<em>$1</em>')
    // 转换代码块
    .replace(/```([\s\S]*?)```/g, '<pre><code>$1</code></pre>')
    // 转换行内代码
    .replace(/`(.*?)`/g, '<code>$1</code>')
    // 转换链接
    .replace(/\[(.*?)\]\((.*?)\)/g, '<a href="$2" target="_blank">$1</a>')
    // 转换段落
    .replace(/\n\n/g, '</p><p>')
    // 转换单行换行
    .replace(/\n/g, '<br>')

  // 包裹在段落标签中
  if (!html.startsWith('<p>')) {
    html = '<p>' + html + '</p>'
  }

  return html
})
</script>

<style scoped>
.markdown-content {
  line-height: 1.6;
  color: #1a1a1a;
}

.markdown-content pre {
  background-color: #f5f5f5;
  padding: 12px;
  border-radius: 4px;
  overflow-x: auto;
  margin: 8px 0;
}

.markdown-content code {
  background-color: #f5f5f5;
  padding: 2px 4px;
  border-radius: 2px;
  font-family: 'Courier New', monospace;
}

.markdown-content a {
  color: #1890ff;
  text-decoration: none;
}

.markdown-content a:hover {
  text-decoration: underline;
}

.markdown-content strong {
  font-weight: 600;
}

.markdown-content em {
  font-style: italic;
}
</style>