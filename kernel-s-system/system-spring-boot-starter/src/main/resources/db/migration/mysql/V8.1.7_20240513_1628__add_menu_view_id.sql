ALTER TABLE `sys_menu`
ADD COLUMN `app_design_view_id` bigint NULL COMMENT '应用设计对应业务的视图id' AFTER `app_design_business_id`;