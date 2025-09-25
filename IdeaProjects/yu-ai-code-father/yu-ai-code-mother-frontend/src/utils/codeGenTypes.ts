// 代码生成类型枚举

export enum CodeGenTypeEnum {
  HTML = 'HTML',
  MULTI_FILE = 'MULTI_FILE',
}

// 获取代码生成类型的显示名称
export const getCodeGenTypeDisplayName = (type: string): string => {
  switch (type) {
    case CodeGenTypeEnum.HTML:
      return 'HTML单页面'
    case CodeGenTypeEnum.MULTI_FILE:
      return '多文件项目'
    default:
      return '未知类型'
  }
}