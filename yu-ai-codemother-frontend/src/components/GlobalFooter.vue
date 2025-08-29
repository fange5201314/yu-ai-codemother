<template>
  <div class="global-footer">
    <div class="footer-content">
      <p class="copyright">
        编程导航原创项目 by 
        <a 
          href="https://www.codefather.cn" 
          target="_blank" 
          rel="noopener noreferrer"
          class="author-link"
        >
          程序员鱼皮
        </a>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.global-footer {
  background: #fff;
  border-top: 1px solid #f0f0f0;
  position: sticky;
  bottom: 0;
  z-index: 10;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 16px 24px;
  text-align: center;
}

.copyright {
  margin: 0;
  color: #666;
  font-size: 14px;
  line-height: 1.5;
}

.author-link {
  color: #1890ff;
  text-decoration: none;
  transition: color 0.3s;
}

.author-link:hover {
  color: #40a9ff;
  text-decoration: underline;
}

@media (max-width: 768px) {
  .footer-content {
    padding: 12px 16px;
  }
  
  .copyright {
    font-size: 12px;
  }
}
</style>