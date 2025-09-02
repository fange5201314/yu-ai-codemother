<script setup lang="ts">
// 无需额外的逻辑
</script>

<template>
  <div class="home">
    <div class="hero-section">
      <h1>鱼皮AI零代码应用生成平台</h1>
      <p>使用AI的力量，让代码生成变得简单而高效</p>
      <div class="cta-buttons">
        <router-link to="/generator">
          <a-button type="primary" size="large">开始生成代码</a-button>
        </router-link>
        <router-link to="/about">
          <a-button size="large">了解更多</a-button>
        </router-link>
      </div>
    </div>

    <div class="features-section">
      <h2>产品特性</h2>
      <div class="features-grid">
        <div class="feature-card">
          <h3>🤖 AI 驱动</h3>
          <p>基于先进AI技术，智能理解需求并生成高质量代码</p>
        </div>
        <div class="feature-card">
          <h3>⚡ 高效快速</h3>
          <p>秒级生成，极大提高开发效率，让你专注于业务逻辑</p>
        </div>
        <div class="feature-card">
          <h3>🎯 多语言支持</h3>
          <p>支持Java、JavaScript、Python等多种编程语言</p>
        </div>
        <div class="feature-card">
          <h3>🛠️ 全栈支持</h3>
          <p>从前端组件到后端 API，一站式代码生成方案</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home {
  padding: 0;
}

.hero-section {
  text-align: center;
  padding: 80px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.hero-section h1 {
  font-size: 3rem;
  margin-bottom: 16px;
  font-weight: bold;
}

.hero-section p {
  font-size: 1.2rem;
  margin-bottom: 32px;
  opacity: 0.9;
}

.cta-buttons {
  display: flex;
  gap: 16px;
  justify-content: center;
  align-items: center;
}

.features-section {
  padding: 80px 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.features-section h2 {
  text-align: center;
  font-size: 2.5rem;
  margin-bottom: 48px;
  color: #333;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
}

.feature-card {
  padding: 32px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  text-align: center;
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-4px);
}

.feature-card h3 {
  font-size: 1.5rem;
  margin-bottom: 16px;
  color: #1890ff;
}

.feature-card p {
  color: #666;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .hero-section h1 {
    font-size: 2rem;
  }
  
  .cta-buttons {
    flex-direction: column;
  }
  
  .features-grid {
    grid-template-columns: 1fr;
  }
}
</style>
