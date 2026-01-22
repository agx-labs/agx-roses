ALTER TABLE `sys_menu`
MODIFY COLUMN `menu_type` tinyint NULL DEFAULT NULL COMMENT '菜单类型：10-后台菜单，20-纯前台路由界面，30-内部链接，40-外部链接，50-应用设计' AFTER `remark`,
ADD COLUMN `app_design_business_id` bigint NULL COMMENT '应用设计的业务id' AFTER `antdv_visible`;