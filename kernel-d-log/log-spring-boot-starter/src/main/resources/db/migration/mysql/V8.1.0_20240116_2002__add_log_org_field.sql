ALTER TABLE `sys_log_api`
ADD COLUMN `user_current_org_id` bigint NULL COMMENT '用户请求时候的登录机构id' AFTER `user_id`;