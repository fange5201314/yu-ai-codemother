// 代码生成预览管理类
import { STATIC_BASE_URL } from '@/config/env'

/**
 * 代码生成预览管理类
 * 专门用于管理应用的生成代码预览URL（与部署访问完全独立）
 */
class StaticPreviewManager {
  /**
   * 根据应用信息获取生成预览URL（始终使用生成目录）
   * @param app 应用信息
   * @returns 生成预览URL（与部署无关）
   */
  getPreviewUrl(app: API.AppVO): string {
    // 如果应用不存在，返回空字符串
    if (!app) {
      return ''
    }

    // 始终使用生成文件名格式访问生成目录中的文件（与是否部署无关）
    if (app.id) {
      const codeGenType = app.codeGenType || 'MULTI_FILE'
      switch (codeGenType) {
        case 'HTML':
          return `${STATIC_BASE_URL}/HTML_${app.id}/index.html`
        case 'MULTI_FILE':
        default:
          return `${STATIC_BASE_URL}/MULTI_FILE_${app.id}/`
      }
    }

    return ''
  }

  /**
   * 获取部署访问URL（独立功能，不用于预览）
   * @param deployKey 部署密钥
   * @returns 部署访问URL
   */
  getDeployUrl(deployKey: string): string {
    if (!deployKey) {
      return ''
    }
    // 部署访问使用独立的URL（注意端口不同）
    return `http://localhost:8080/${deployKey}/`
  }
}

// 创建单例实例
const staticPreviewManager = new StaticPreviewManager()

export default staticPreviewManager